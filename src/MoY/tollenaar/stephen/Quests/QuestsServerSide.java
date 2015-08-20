package MoY.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.NPC.NPC;
import MoY.tollenaar.stephen.NPC.NPCEntity;
import MoY.tollenaar.stephen.NPC.NPCProfile;
import MoY.tollenaar.stephen.NPC.NPCHandler;
import MoY.tollenaar.stephen.NPC.NPCSpawnReason;

public class QuestsServerSide extends Quest {

	public HashMap<UUID, Location> spawnlocation = new HashMap<UUID, Location>(); // db
	private HashMap<UUID, Location> npclocation = new HashMap<UUID, Location>(); // needed
																					// for
																					// the
																					// facial
																					// location

	// quest storages all to db
	private static HashMap<UUID, HashSet<Integer>> killquests = new HashMap<UUID, HashSet<Integer>>();
	private static HashMap<UUID, HashSet<Integer>> harvestquests = new HashMap<UUID, HashSet<Integer>>();
	private static HashMap<UUID, HashSet<Integer>> talktoquests = new HashMap<UUID, HashSet<Integer>>();
	private static HashMap<UUID, HashSet<Integer>> eventquests = new HashMap<UUID, HashSet<Integer>>();
	private static HashMap<UUID, Integer> warplists = new HashMap<UUID, Integer>();

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

	public void canceltasks(UUID npcuuid) {
		if (facelooker.get(npcuuid) != null) {
			Bukkit.getScheduler().cancelTask(facelooker.get(npcuuid));
		}
		if (activenpc.get(npcuuid) != null) {
			Bukkit.getScheduler().cancelTask(activenpc.get(npcuuid));
		}
	}

	public void resetHear(UUID npcuuid) {
		if (activenpc.get(npcuuid) == null) {
			if (facelooker.get(npcuuid) != null) {
				npchear(npcuuid);
			}
		} else {
			runpoints(npcuuid);
		}
	}

	// DYNAMIC NPC
	public void npchear(final UUID npcuuid) {
		NPCHandler handler = plugin.getNPCHandler();
		final NPC npc = handler.getNPCByUUID(npcuuid);

		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
					int tickdelay = 0;

					@Override
					public void run() {
						// HOTFIX
						if (npc != null) {
							Location location = npclocation.get(npcuuid);
							double radiusSquared = 10 * 10;

							ArrayList<Location> allclosep = new ArrayList<Location>();
							if (Bukkit.getOnlinePlayers().size() >= 1) {
								Location closest = null;

								for (Player player : Bukkit.getOnlinePlayers()) {
									if (!player.hasMetadata("NPC")) {
										if (player
												.getWorld()
												.getName()
												.equals(location.getWorld()
														.getName())) {
											if (closest == null) {
												closest = player.getLocation();
											}

											if (player.getLocation()
													.distanceSquared(location) <= radiusSquared) {

												allclosep.add(player
														.getLocation());

											}
										}
									}
								}
								for (Location loc : allclosep) {
									if (loc.getWorld()
											.getName()
											.equals(location.getWorld()
													.getName())) {
										if (closest != null) {
											if (loc.distanceSquared(location) < closest
													.distanceSquared(location)) {
												closest = loc;
											}
										}
									}
								}
								if (closest != null) {
									npc.lookatLocation(closest);
								}
							}
							tickdelay++;
							if (tickdelay == 20) {
								QuestParticles
										.collectplayers(location, npcuuid);
								tickdelay = 0;
							}
						}
					}
				}, 0L, 1L);
		facelooker.put(npcuuid, id);

	}

	public void runpoints(final UUID npcuuid) {
		NPCHandler handler = plugin.getNPCHandler();
		final NPC npc = handler.getNPCByUUID(npcuuid);
		final Location startFrom = spawnlocation.get(npcuuid);
		final Random rndGen = new Random();
		final int[] values = new int[3];

		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
					@Override
					public void run() {
						// HOTFIX
						if (npc != null) {
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
							npc.pathFinder(output);
							npclocation.put(npc.getUniqueId(), output);
							Bukkit.getScheduler().scheduleSyncDelayedTask(
									plugin, new Runnable() {
										@Override
										public void run() {
											npchear(npcuuid);
										}
									}, 40L);

						}
					}
				}, 0L, 200L);
		activenpc.put(npcuuid, id);
	}

	public void stop(UUID npcuuid) {
		Bukkit.getScheduler().cancelTask(activenpc.get(npcuuid));
		activenpc.remove(npcuuid);
	}

	public void spawnNpc(Location location, String name, int uniqueid,
			String skin, String type) {
		NPCEntity npc = new NPCEntity(location.getWorld(), location,
				NPCProfile.loadProfile(name, skin,
						((CraftWorld) location.getWorld()).getHandle()),
				plugin.getNetwork(), plugin, type);

		UUID npcuuid = npc.getUniqueId();

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
		if (!npc.isSpawned()) {
			npc.spawn(location, NPCSpawnReason.NORMAL_SPAWN, npc);
		}
		npchear(npcuuid);

	}

	public void despawnNPC(UUID npcuuid) {
		NPCHandler handler = plugin.getNPCHandler();
		NPC npc = handler.getNPCByUUID(npcuuid);
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
									NPC tnpc = handler.getNPCByUUID(tu);
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
													+ tnpc.getCurrentloc()
															.getX()
													+ " Y:"
													+ tnpc.getCurrentloc()
															.getY()
													+ " Z:"
													+ tnpc.getCurrentloc()
															.getZ()
													+ " In the world "
													+ tnpc.getCurrentloc()
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
			npc.despawn(NPCSpawnReason.DESPAWN);
		}
	}

	public void npcsettingsmain(UUID npcuuid, Player player) {
		NPCHandler handler = plugin.getNPCHandler();
		NPC npc = handler.getNPCByUUID(npcuuid);

		// npc name
		ItemStack npcname = new ItemStack(Material.SKULL_ITEM);
		{
			ItemMeta npcnamemeta = npcname.getItemMeta();
			npcnamemeta.setDisplayName("NPC name");
			List<String> nn = new ArrayList<String>();
			nn.add(npc.getName());
			npcnamemeta.setLore(nn);
			npcname.setItemMeta(npcnamemeta);
		}

		ItemStack npcskin = new ItemStack(Material.SKULL_ITEM);
		{
			ItemMeta meta = npcskin.getItemMeta();
			meta.setDisplayName("NPC skinName");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(npc.getSkinName());
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

	public void addKillquest(UUID npcuuid, int number) {
		if (killquests.get(npcuuid) == null) {
			HashSet<Integer> t = new HashSet<Integer>();
			t.add(number);
			killquests.put(npcuuid, t);
		} else {
			killquests.get(npcuuid).add(number);
		}
	}

	public void addHarvestquest(UUID npcuuid, int number) {
		if (harvestquests.get(npcuuid) == null) {
			HashSet<Integer> t = new HashSet<Integer>();
			t.add(number);
			harvestquests.put(npcuuid, t);
		} else {
			harvestquests.get(npcuuid).add(number);
		}
	}

	public void addTalktoquest(UUID npcuuid, int number) {
		if (talktoquests.get(npcuuid) == null) {
			HashSet<Integer> t = new HashSet<Integer>();
			t.add(number);
			talktoquests.put(npcuuid, t);
		} else {
			talktoquests.get(npcuuid).add(number);
		}
	}

	public void addEventquest(UUID npcuuid, int number) {
		if (eventquests.get(npcuuid) == null) {
			HashSet<Integer> t = new HashSet<Integer>();
			t.add(number);
			eventquests.put(npcuuid, t);
		} else {
			eventquests.get(npcuuid).add(number);
		}
	}

	public void addWarp(UUID npcuuid, int number) {
		warplists.put(npcuuid, number);
	}

	public HashSet<Integer> GetIds(String type, UUID npcuuid) {
		HashSet<Integer> t;
		switch (type) {
		case "kill":
			t = killquests.get(npcuuid);
			break;
		case "harvest":
			t = harvestquests.get(npcuuid);
			break;
		case "talkto":
			t = talktoquests.get(npcuuid);
			break;
		case "event":
			t = eventquests.get(npcuuid);
			break;
		default:
			return null;
		}
		return t;
	}

	public Integer getId(UUID npcuuid) {
		if (warplists.get(npcuuid) != null) {
			return warplists.get(npcuuid);
		} else {
			return -1;
		}
	}

	public void RemoveQuest(String type, UUID npcuuid, int number) {
		switch (type) {
		case "kill":
			if (killquests.get(npcuuid) != null) {
				killquests.get(npcuuid).remove(number);
			}
			break;
		case "harvest":
			if (harvestquests.get(npcuuid) != null) {
				harvestquests.get(npcuuid).remove(number);
			}
			break;
		case "talkto":
			if (talktoquests.get(npcuuid) != null) {
				talktoquests.get(npcuuid).remove(number);
			}
			break;
		case "event":
			if (eventquests.get(npcuuid) != null) {
				eventquests.get(npcuuid).remove(number);
			}
			break;
		case "warp":
			warplists.remove(npcuuid);
			break;
		}
	}

	public Set<UUID> GetKeysSets(String type) {
		switch (type) {
		case "kill":
			return killquests.keySet();
		case "harvest":
			return harvestquests.keySet();
		case "talkto":
			return talktoquests.keySet();
		case "warp":
			return warplists.keySet();
		case "event":
			return eventquests.keySet();
		default:
			return null;
		}
	}
}
