package MoY.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;

public class QuestsServerSide extends Quest {

	public HashMap<UUID, Location> spawnlocation = new HashMap<UUID, Location>(); // db
	private HashMap<UUID, Location> npclocation = new HashMap<UUID, Location>(); // needed
																					// for
																					// the
																					// facial
																					// location

	// quest storages all to db
	public HashMap<UUID, HashSet<Integer>> killquests = new HashMap<UUID, HashSet<Integer>>();
	public HashMap<UUID, HashSet<Integer>> harvestquests = new HashMap<UUID, HashSet<Integer>>();
	public HashMap<UUID, HashSet<Integer>> talktoquests = new HashMap<UUID, HashSet<Integer>>();
	public HashMap<UUID, HashSet<Integer>> eventquests = new HashMap<UUID, HashSet<Integer>>();
	public HashMap<UUID, Integer> warplists = new HashMap<UUID, Integer>();

	// active npc's
	public HashMap<UUID, Integer> activenpc = new HashMap<UUID, Integer>(); // to
																			// db.
																			// needed
																			// for
																			// walking
	public HashMap<UUID, Integer> facelooker = new HashMap<UUID, Integer>(); // no
																				// db.
																				// needed
																				// for
																				// the
																				// face
																				// location

	public HashMap<UUID, Integer> targetnpcs = new HashMap<UUID, Integer>(); // target
																				// npc's
																				// uuid
																				// to
																				// the
																				// uniquetalkto
																				// id.

	// list of npc's not to db
	public HashMap<Integer, UUID> uniquenpcid = new HashMap<Integer, UUID>(); // all
																				// npc
																				// with
																				// unique
																				// id

	// not to db
	protected HashMap<UUID, ArrayList<String>> npcpos = new HashMap<UUID, ArrayList<String>>();

	// DYNAMIC NPC
	public void npchear(final UUID npcuuid) {
		NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.getByUniqueId(npcuuid);

		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
					int tickdelay = 0;

					@Override
					public void run() {
						Collection<? extends Player> ptemps = plugin.getServer().getOnlinePlayers();
							Iterator<? extends Player> players = 	ptemps.iterator();

							Location location = npclocation.get(npcuuid);
						double radiusSquared = 10 * 10;

						ArrayList<Location> allclosep = new ArrayList<Location>();
						if (ptemps.size() >= 1) {
							Location closest = players.next().getLocation();

							while(players.hasNext()){
								Player player = players.next();
								if (player.getWorld().getName()
										.equals(location.getWorld().getName())) {
									if (player.getLocation()
											.distanceSquared(location) <= radiusSquared) {

										allclosep.add(player.getLocation());

									}
								}
							}
								for (Location loc : allclosep) {
									if (loc.getWorld()
											.getName()
											.equals(location.getWorld()
													.getName())) {
										if (loc.distanceSquared(location) < closest
												.distanceSquared(location)) {
											closest = loc;
										}
									}
								}
								npc.faceLocation(closest);
							}
						tickdelay++;
						if (tickdelay == 20) {
							QuestParticles.collectplayers(location, npcuuid);
							tickdelay = 0;
						}

					}
				}, 0L, 1L);
		facelooker.put(npcuuid, id);

	}

	public void runpoints(final UUID npcuuid) {
		NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.getByUniqueId(npcuuid);
		final Location startFrom = spawnlocation.get(npcuuid);
		final Random rndGen = new Random();
		final int[] values = new int[3];

		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
					@Override
					public void run() {

						for (int i = 0; i < 3; i++) {
							values[i] = rndGen.nextInt(4) + 1;

						}
						Location output = startFrom.clone();
						Random x = new Random();
						int y = x.nextInt(3) + 1;
						switch (y) {
						case 1:
							output.add(values[0], 0, values[2]);
							break;
						case 2:
							output.subtract(values[0], 0, values[2]);
							break;
						case 3:
							int k = values[0] - values[0] * 2;
							output.add(k, 0, values[2]);
							break;
						case 4:
							int l = values[2] - values[2] * 2;
							output.add(values[0], 0, l);
							break;
						}
						Bukkit.getScheduler().cancelTask(
								facelooker.get(npcuuid));
						npc.getNavigator().setTarget(output);
						npclocation.put(npc.getUniqueId(), output);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
								new Runnable() {
									@Override
									public void run() {
										npchear(npcuuid);
									}
								}, 40L);

					}
				}, 0L, 200L);
		activenpc.put(npcuuid, id);
	}

	public void stop(UUID npcuuid) {
		Bukkit.getScheduler().cancelTask(activenpc.get(npcuuid));
		activenpc.remove(npcuuid);
	}

	public void spawnNpc(Location location, String name, int uniqueid, String skin) {
		NPCRegistry registry = CitizensAPI.getNPCRegistry();
		NPC npc = registry.createNPC(EntityType.PLAYER, name);
		
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, NPCSkin.getNonPlayerProfile(skin).getName());
		npc.spawn(location);
		UUID npcuuid = npc.getUniqueId();
		
		NPCSkin.getNonPlayerProfile(skin);
		spawnlocation.put(npcuuid, location);
		npclocation.put(npcuuid, location);
		if (uniqueid != -1) {
			uniquenpcid.put(uniqueid, npcuuid);
		} else {
			int id = 0;
			while (uniquenpcid.containsKey(id)) {
				id++;
			}
			uniquenpcid.put(id, npcuuid);
		}

		npchear(npcuuid);

	}

	public void despawnNPC(UUID npcuuid) {
		NPCRegistry registry = CitizensAPI.getNPCRegistry();
		NPC npc = registry.getByUniqueId(npcuuid);
		if (npc != null) {
			if (killquests.get(npcuuid) != null) {
				for (int quest : killquests.get(npcuuid)) {
					removekill(quest);
				}
				killquests.remove(npcuuid);
			}
			if (harvestquests.get(npcuuid) != null) {
				for (int quest : harvestquests.get(npcuuid)) {
					removeharvest(quest);
				}
				harvestquests.remove(npcuuid);
			}
			if (talktoquests.get(npcuuid) != null) {
				for (int quest : talktoquests.get(npcuuid)) {
					removetalkto(quest);
				}
				talktoquests.remove(npcuuid);
			}
			if (warplists.get(npcuuid) != null) {
				warplists.remove(npcuuid);
			}
			if (activenpc.get(npcuuid) != null) {
				Bukkit.getScheduler().cancelTask(activenpc.get(npcuuid));
				activenpc.remove(npcuuid);
			}
			if (targetnpcs.get(npcuuid) != null) {
				for (UUID tu : talktoquests.keySet()) {
					if (talktoquests.get(tu) != null) {
						for (Integer number : talktoquests.get(tu)) {
							if (returntalkto(number) != null) {
								if (returntalkto(number).getNpcid() == targetnpcs
										.get(npcuuid)) {
									NPC tnpc = registry.getByUniqueId(tu);
									Bukkit.broadcast(
											ChatColor.DARK_PURPLE
													+ "["
													+ ChatColor.GOLD
													+ "MOYE"
													+ ChatColor.DARK_PURPLE
													+ "] "
													+ ChatColor.DARK_AQUA
													+ "A talkto quest npc is removed. Please check npc "
													+ tnpc.getName()
													+ ". At X:"
													+ tnpc.getStoredLocation()
															.getX()
													+ " Y:"
													+ tnpc.getStoredLocation()
															.getY()
													+ " Z:"
													+ tnpc.getStoredLocation()
															.getZ()
													+ " In the world "
													+ tnpc.getStoredLocation()
															.getWorld()
															.getName(),
											"MistCore.chat");
									returntalkto(number).setNpcid(0);
									break;
								}
							}
						}
						break;
					}
				}

			}
			targetnpcs.remove(npcuuid);
			Bukkit.getScheduler().cancelTask(facelooker.get(npcuuid));
			facelooker.remove(npcuuid);
			spawnlocation.remove(npcuuid);
			for (int i : uniquenpcid.keySet()) {
				if (uniquenpcid.get(i) == npc.getUniqueId()) {
					uniquenpcid.remove(i);
					plugin.database.deletenpc(i);
					break;
				}
			}

			npc.despawn();
			registry.deregister(npc);
		}
	}

	public void npcsettingsmain(UUID npcuuid, Player player) {
		NPCRegistry registry = CitizensAPI.getNPCRegistry();
		NPC npc = registry.getByUniqueId(npcuuid);

		// npc name
		ItemStack npcname = new ItemStack(Material.SKULL_ITEM);
		{
			ItemMeta npcnamemeta = npcname.getItemMeta();
			npcnamemeta.setDisplayName("NPC name");
			List<String> nn = new ArrayList<String>();
			nn.add(npc.getFullName());
			npcnamemeta.setLore(nn);
			npcname.setItemMeta(npcnamemeta);
		}
		
		ItemStack npcskin = new ItemStack(Material.SKULL_ITEM);
		{
			ItemMeta meta = npcskin.getItemMeta();
			meta.setDisplayName("NPC skinName");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add((String) npc.data().get(NPC.PLAYER_SKIN_UUID_METADATA));
			meta.setLore(lore);
			npcskin.setItemMeta(meta);
		}

		// npc uuid
		ItemStack npcuid = new ItemStack(Material.BOOK);
		{
			ItemMeta npcuuidm = npcuid.getItemMeta();
			npcuuidm.setDisplayName("NPC UUID");
			List<String> nu = new ArrayList<String>();
			nu.add(npcuuid.toString());
			for (int t : uniquenpcid.keySet()) {
				if (uniquenpcid.get(t) == npc.getUniqueId()) {
					nu.add(Integer.toString(t));
					break;
				}
			}
			npcuuidm.setLore(nu);
			npcuid.setItemMeta(npcuuidm);
		}
		// active/passive
		ItemStack activeorpassive;
		if (activenpc.get(npcuuid) != null) {
			Wool wool = new Wool(DyeColor.LIME);
			activeorpassive = wool.toItemStack();
			ItemMeta activem = activeorpassive.getItemMeta();
			activem.setDisplayName("Active/Passive");
			List<String> at = new ArrayList<String>();
			at.add("This npc is active.");
			at.add("This means it walks.");
			activem.setLore(at);
			activeorpassive.setItemMeta(activem);
		} else {
			Wool wool = new Wool(DyeColor.RED);
			activeorpassive = wool.toItemStack();
			ItemMeta activem = activeorpassive.getItemMeta();
			activem.setDisplayName("Active/Passive");
			List<String> at = new ArrayList<String>();
			at.add("This npc is passive.");
			at.add("This means it doens't move.");
			activem.setLore(at);
			activeorpassive.setItemMeta(activem);
		}
		// killquest
		ItemStack killquest = new ItemStack(Material.DIAMOND_SWORD);
		{
			ItemMeta temp = killquest.getItemMeta();
			temp.setDisplayName("Kill Quests");
			killquest.setItemMeta(temp);
		}

		// harvestquest
		ItemStack harvestquest = new ItemStack(Material.DIAMOND_PICKAXE);
		{
			ItemMeta temp = harvestquest.getItemMeta();
			temp.setDisplayName("Harvest Quests");
			harvestquest.setItemMeta(temp);
		}

		// warplists
		ItemStack warplists = new ItemStack(Material.BOAT);
		{
			ItemMeta temp = warplists.getItemMeta();
			temp.setDisplayName("Warp Lists");
			warplists.setItemMeta(temp);
		}

		// talktoquest
		ItemStack talktoquest = new ItemStack(Material.FEATHER);
		{
			ItemMeta temp = talktoquest.getItemMeta();
			temp.setDisplayName("Talk to Quest");
			talktoquest.setItemMeta(temp);
		}

		// delete
		ItemStack delete = new ItemStack(Material.BLAZE_ROD);
		{
			ItemMeta temp = delete.getItemMeta();
			temp.setDisplayName("Delete NPC");
			List<String> nu = new ArrayList<String>();
			nu.add(npcuuid.toString());
			temp.setLore(nu);
			delete.setItemMeta(temp);
		}
		// event quest
		ItemStack event = new ItemStack(Material.ENCHANTED_BOOK);
		{
			ItemMeta temp = event.getItemMeta();
			temp.setDisplayName("Event Quest");
			event.setItemMeta(temp);
		}
		

		String temp = "Main settings";
		Inventory myinventory = Bukkit.createInventory(null, 18, temp);
		myinventory.addItem(npcname);
		myinventory.addItem(npcskin);
		myinventory.addItem(npcuid);
		myinventory.addItem(activeorpassive);
		myinventory.addItem(killquest);
		myinventory.addItem(harvestquest);
		myinventory.addItem(warplists);
		myinventory.addItem(talktoquest);
		myinventory.addItem(event);
		myinventory.setItem(myinventory.getSize() - 1, delete);
		player.openInventory(myinventory);
	}

	public QuestsServerSide(MoY instance) {
		super(instance);
	}

}
