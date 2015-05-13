package MoY.tollenaar.stephen.Quests;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.Travel.HarborWaitLocations;
import MoY.tollenaar.stephen.Travel.TripLocations;

public class Quest {

	protected MoY plugin;

	public Quest(MoY instance) {
		this.plugin = instance;

	}


	private HashMap<Integer, QuestKill> killquests = new HashMap<Integer, QuestKill>();

	private HashMap<Integer, QuestHarvest> harvestquests = new HashMap<Integer, QuestHarvest>();

	private HashMap<Integer, QuestTalkto> talktoquests = new HashMap<Integer, QuestTalkto>();

	private HashMap<Integer, Warps> warplist = new HashMap<Integer, Warps>();

	private HashMap<Integer, TripLocations> triplocations = new HashMap<Integer, TripLocations>();

	private HashMap<Integer, HarborWaitLocations> harborlocation = new HashMap<Integer, HarborWaitLocations>();

	public HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>> progress = new HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>>();

	public int returnProgress(UUID playeruuid, String type, int number) {
		return progress.get(playeruuid).get(type).get(number);
	}

	public void removetrip(int number) {
		triplocations.remove(number);
		plugin.database.deletetrip(number);
	}

	public void removeharbor(int number) {
		harborlocation.remove(number);
		plugin.database.deleteharbor(number);
	}

	public void removekill(int number) {
		killquests.remove(number);
		plugin.fw.deletequest("kill", number);
	}

	public void removeharvest(int number) {
		harvestquests.remove(number);
		plugin.fw.deletequest("harvest", number);
	}

	public void removetalkto(int number) {
		talktoquests.remove(number);
		plugin.fw.deletequest("talkto", number);
	}

	public void removewarp(int number) {
		Warps.allwarps.remove(warplist.get(number));
		warplist.remove(number);
		plugin.database.deletewarp(number);
	}

	public int createnewtrip() {
		int i = 0;
		for (; i <= triplocations.size(); i++) {
			if (triplocations.get(i) == null) {
				break;
			}
		}
		TripLocations trip = new TripLocations(i);
		triplocations.put(i, trip);
		return i;
	}

	public int createnewharbor() {
		int i = 0;
		for (; i <= harborlocation.size(); i++) {
			if (harborlocation.get(i) == null) {
				break;
			}
		}
		HarborWaitLocations trip = new HarborWaitLocations(i);
		harborlocation.put(i, trip);
		return i;
	}

	public int createnewkill() {
		int i = 0;
		for (; i <= killquests.size(); i++) {
			if (killquests.get(i) == null) {
				break;
			}
		}

		QuestKill kill = new QuestKill(i);

		killquests.put(i, kill);

		return i;
	}

	public int createnewhar() {
		int i = 0;
		for (; i <= harvestquests.size(); i++) {
			if (harvestquests.get(i) == null) {
				break;
			}
		}

		QuestHarvest kill = new QuestHarvest(i, plugin);

		harvestquests.put(i, kill);

		return i;
	}

	public int createnewtalk() {
		int i = 0;
		for (; i <= talktoquests.size(); i++) {
			if (talktoquests.get(i) == null) {
				break;
			}
		}

		QuestTalkto talk = new QuestTalkto(i);
		talktoquests.put(i, talk);
		return i;
	}

	public int createnewwarp(Location l) {
		int i = 0;
		for (; i <= warplist.size(); i++) {
			if (warplist.get(i) == null) {
				break;
			}
		}
		Warps warp = new Warps(i, l);

		warplist.put(i, warp);
		return i;
	}

	public QuestKill returnkill(int questnumber) {
		if (killquests.get(questnumber) != null) {
			return killquests.get(questnumber);
		} else {
			return null;
		}
	}

	public QuestHarvest returnharvest(int questnumber) {
		if (harvestquests.get(questnumber) != null) {
			return harvestquests.get(questnumber);
		} else {
			return null;
		}
	}

	public QuestTalkto returntalkto(int questnumber) {
		if (talktoquests.get(questnumber) != null) {
			return talktoquests.get(questnumber);
		} else {
			return null;
		}
	}

	public Warps returnwarp(int number) {
		if (warplist.get(number) != null) {
			return warplist.get(number);
		} else {
			return null;
		}
	}

	public TripLocations returntrip(int number) {
		return triplocations.get(number);
	}

	public HarborWaitLocations returnharbor(int number) {
		return harborlocation.get(number);
	}

	public void loadkill(int number, String name, List<String> reward, String delay,
			int minlvl, String message, String prereq, int count, String monster) {
		QuestKill kill = new QuestKill(number);

		kill.setName(name);
		kill.setReward(reward);
		kill.setDelay(delay);
		kill.setMinlvl(minlvl);
		kill.setPrereq(prereq);
		kill.setCount(count);
		kill.setMonster(monster);
		kill.setMessage(message);

		killquests.put(number, kill);
	}

	public void loadharvest(int number, String name, List<String> reward,
			String delay, int minlvl, String message, String prereq, int count,
			String itemid) {
		QuestHarvest kill = new QuestHarvest(number, plugin);

		kill.setName(name);
		kill.setReward(reward);
		kill.setDelay(delay);
		kill.setMinlvl(minlvl);
		kill.setPrereq(prereq);
		kill.setCount(count);
		kill.setMessage(message);
		kill.setItem(itemid);

		harvestquests.put(number, kill);
	}

	public void loadtalkto(int number, String name, int id, String message,
			String delay, int minlvl, String prereq, List<String> reward) {
		QuestTalkto temp = new QuestTalkto(number);
		temp.setName(name);
		temp.setNpcid(id);
		temp.setDelay(delay);
		temp.setMessage(message);
		temp.setMinlvl(minlvl);
		temp.setReward(reward);
		temp.setPrereq(prereq);

		talktoquests.put(number, temp);
	}

	public void loadwarps(int number, String name, Location start,
			double costs, String type) {
		Warps warp = new Warps(number, start);
		warp.setCosts(costs);
		warp.setName(name);
		warp.SetType(type);

		warplist.put(number, warp);
	}

	public void loadhardbor(int id, String type, Location loc) {
		HarborWaitLocations h = new HarborWaitLocations(id);
		h.setLocation(loc);
		h.setType(type);
		harborlocation.put(id, h);
	}

	public void loadtrip(int id, String type, Location loc) {
		TripLocations t = new TripLocations(id);
		t.setLocation(loc);
		t.setType(type);
		triplocations.put(id, t);
	}

	public void allkill(Player player, HashSet<Integer> quests, UUID npcuuid) {
		int rowcount = 0;
		if (quests != null) {
			rowcount = quests.size();
			if (rowcount % 9 == 0) {
				rowcount++;
			}
			while (rowcount % 9 != 0) {
				rowcount++;
			}
		}
		if (rowcount == 0) {
			rowcount = 9;
		}

		Inventory inv = Bukkit.createInventory(null, rowcount, "AllKill");
		if (quests != null) {
			for (Integer i : quests) {
				if (killquests.get(i) != null) {
					QuestKill kill = killquests.get(i);
					ItemStack title = new ItemStack(Material.BOOK);
					{
						ArrayList<String> lore = new ArrayList<String>();
						lore.add(Integer.toString(kill.getQuestnumber()));
						ItemMeta meta = title.getItemMeta();
						meta.setLore(lore);
						meta.setDisplayName(kill.getName());
						title.setItemMeta(meta);
					}
					inv.addItem(title);
				}
			}
		}

		Wool wool = new Wool(DyeColor.LIME);
		ItemStack create = wool.toItemStack();
		{
			ItemMeta meta = create.getItemMeta();
			meta.setDisplayName("Create New");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(npcuuid.toString());
			meta.setLore(lore);
			create.setItemMeta(meta);
		}

		inv.setItem(inv.getSize() - 1, create);
		player.openInventory(inv);

	}

	public void allharvest(Player player, HashSet<Integer> quests, UUID npcuuid) {
		int rowcount = 0;
		if (quests != null) {
			rowcount = quests.size();
			if (rowcount % 9 == 0) {
				rowcount++;
			}
			while (rowcount % 9 != 0) {
				rowcount++;
			}
		}
		if (rowcount == 0) {
			rowcount = 9;
		}

		Inventory inv = Bukkit.createInventory(null, rowcount, "AllHarvest");
		if (quests != null) {
			for (Integer i : quests) {
				if (harvestquests.get(i) != null) {
					QuestHarvest kill = harvestquests.get(i);
					ItemStack title = new ItemStack(Material.BOOK);
					{
						ArrayList<String> lore = new ArrayList<String>();
						lore.add(Integer.toString(kill.getQuestnumber()));
						ItemMeta meta = title.getItemMeta();
						meta.setLore(lore);
						meta.setDisplayName(kill.getName());
						title.setItemMeta(meta);
					}
					inv.addItem(title);
				}
			}
		}

		Wool wool = new Wool(DyeColor.LIME);
		ItemStack create = wool.toItemStack();
		{
			ItemMeta meta = create.getItemMeta();
			meta.setDisplayName("Create New");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(npcuuid.toString());
			meta.setLore(lore);
			create.setItemMeta(meta);
		}

		inv.setItem(inv.getSize() - 1, create);
		player.openInventory(inv);

	}

	public void alltalkto(Player player, HashSet<Integer> quests, UUID npcuuid) {
		int rowcount = 0;
		if (quests != null) {
			rowcount = quests.size();
			if (rowcount % 9 == 0) {
				rowcount++;
			}
			while (rowcount % 9 != 0) {
				rowcount++;
			}
		}
		if (rowcount == 0) {
			rowcount = 9;
		}

		Inventory inv = Bukkit.createInventory(null, rowcount, "AllTalkTo");
		if (quests != null) {
			for (Integer i : quests) {
				if (talktoquests.get(i) != null) {
					QuestTalkto kill = talktoquests.get(i);
					ItemStack title = new ItemStack(Material.BOOK);
					{
						ArrayList<String> lore = new ArrayList<String>();
						lore.add(Integer.toString(kill.getQuestnumber()));
						ItemMeta meta = title.getItemMeta();
						meta.setLore(lore);
						meta.setDisplayName(kill.getName());
						title.setItemMeta(meta);
					}
					inv.addItem(title);
				}
			}
		}

		Wool wool = new Wool(DyeColor.LIME);
		ItemStack create = wool.toItemStack();
		{
			ItemMeta meta = create.getItemMeta();
			meta.setDisplayName("Create New");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(npcuuid.toString());
			meta.setLore(lore);
			create.setItemMeta(meta);
		}

		inv.setItem(inv.getSize() - 1, create);
		player.openInventory(inv);

	}

	public void allwarps(Player player, Integer quests, UUID npcuuid) {
		int rowcount = 9;
		if (quests != null) {
			// rowcount = quests.size();
			if (rowcount % 9 == 0) {
				rowcount++;
			}
			while (rowcount % 9 != 0) {
				rowcount++;
			}
		}
		if (rowcount == 0) {
			rowcount = 9;
		}

		Inventory inv = Bukkit.createInventory(null, rowcount, "AllWarps");
		Warps kill = warplist.get(quests);
		if (kill != null) {
			ItemStack warp = new ItemStack(Material.BOAT);
			{
				ItemMeta meta = warp.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(Integer.toString(kill.getWarpid()));
				meta.setLore(lore);
				meta.setDisplayName(kill.getName());
				warp.setItemMeta(meta);
				inv.addItem(warp);
			}
		}
		Wool wool = new Wool(DyeColor.LIME);
		ItemStack create = wool.toItemStack();
		{
			ItemMeta meta = create.getItemMeta();
			meta.setDisplayName("Create New");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(npcuuid.toString());
			meta.setLore(lore);
			create.setItemMeta(meta);
		}

		inv.setItem(inv.getSize() - 1, create);
		player.openInventory(inv);

	}

	public void AddActiveQuest(Player player, int number, String quetstype) {
		if (Playerstats.activequests.get(player.getUniqueId()) == null) {
			HashMap<String, HashSet<Integer>> types = new HashMap<String, HashSet<Integer>>();
			HashSet<Integer> numbers = new HashSet<Integer>();
			numbers.add(number);
			types.put(quetstype, numbers);
			Playerstats.activequests.put(player.getUniqueId(), types);
		} else {
			HashMap<String, HashSet<Integer>> types = Playerstats.activequests
					.get(player.getUniqueId());
			if (types.get(quetstype) == null) {
				HashSet<Integer> numbers = new HashSet<Integer>();
				numbers.add(number);
				types.put(quetstype, numbers);
				Playerstats.activequests.put(player.getUniqueId(), types);
			} else {
				HashSet<Integer> numbers = types.get(quetstype);
				numbers.add(number);
				types.put(quetstype, numbers);
				Playerstats.activequests.put(player.getUniqueId(), types);
			}
		}
	}

	public void AddCompletedQuest(Player player, int number, String questtype,
			String npcname) {
		HashSet<Integer> numbers = Playerstats.activequests.get(
				player.getUniqueId()).get(questtype);
		for (int i : numbers) {
			if (i == number) {
				numbers.remove(i);
				break;
			}
		}
		if (questtype.equals("kill") || questtype.equals("harvest")) {
			progress.get(player.getUniqueId()).get(questtype).remove(number);
		}

		if (Playerstats.completedquests.get(player.getUniqueId()) != null) {
			if (Playerstats.completedquests.get(player.getUniqueId()).get(
					questtype) != null) {
				Playerstats.completedquests.get(player.getUniqueId())
						.get(questtype).add(number);
			} else {
				HashMap<String, HashSet<Integer>> types = Playerstats.completedquests
						.get(player.getUniqueId());
				HashSet<Integer> temp = new HashSet<Integer>();
				temp.add(number);
				types.put(questtype, temp);
				Playerstats.completedquests.put(player.getUniqueId(), types);
			}
		} else {
			HashMap<String, HashSet<Integer>> types = new HashMap<String, HashSet<Integer>>();
			HashSet<Integer> temp = new HashSet<Integer>();
			temp.add(number);
			types.put(questtype, temp);
			Playerstats.completedquests.put(player.getUniqueId(), types);
		}
		String name = returnname(questtype, number);
		String message = plugin.fw.GetUtilityLine("QuestComplete")
				.replace("%questname%", name).replace("%npc%", npcname);
		player.sendMessage(ChatColor.BLUE + "[" + ChatColor.BLUE + "YQuest"
				+ ChatColor.BLUE + "] " + ChatColor.GRAY + message);

	}

	@SuppressWarnings("deprecation")
	public void ActiveQuest(Player player) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if (Playerstats.activequests.get(player.getUniqueId()) != null) {
			for (String type : Playerstats.activequests.get(
					player.getUniqueId()).keySet()) {
				for (int number : Playerstats.activequests.get(
						player.getUniqueId()).get(type)) {
					switch (type) {
					case "kill":
						QuestKill kill = returnkill(number);
						ItemStack kil = new ItemStack(Material.STONE_SWORD);
						{
							ItemMeta meta = kil.getItemMeta();
							meta.setDisplayName(kill.getName());
							ArrayList<String> lore = new ArrayList<String>();
							lore.add("Kill: " + kill.getMonster());
							lore.add("Progress: "
									+ progress.get(player.getUniqueId())
											.get(type).get(number) + "/"
									+ kill.getCount());
							meta.setLore(lore);
							kil.setItemMeta(meta);
							items.add(kil);
						}
						break;
					case "harvest":
						QuestHarvest harvest = returnharvest(number);
						ItemStack har = new ItemStack(Material.STONE_PICKAXE);
						{
							ItemMeta meta = har.getItemMeta();
							meta.setDisplayName(harvest.getName());
							ArrayList<String> lore = new ArrayList<String>();
							String[] itemid = harvest.getItemId().split(":");
							ItemStack item;
							if(itemid.length == 2){
								item = new ItemStack(Material.getMaterial(Integer.parseInt(itemid[0].trim())), 1, Short.parseShort(itemid[1].trim()));
							}else{
								item = new ItemStack(Material.getMaterial(Integer.parseInt(itemid[0].trim())));
							}
							
							
							lore.add("get: " + GetItemName(item));
							lore.add("Progress: "
									+ progress.get(player.getUniqueId())
											.get(type).get(number) + "/"
									+ harvest.getCount());
							meta.setLore(lore);
							har.setItemMeta(meta);
							items.add(har);
						}
						break;
					case "talkto":
						QuestTalkto talk = returntalkto(number);
						ItemStack tal = new ItemStack(Material.FEATHER);
						{
							ItemMeta meta = tal.getItemMeta();
							meta.setDisplayName(talk.getName());
							ArrayList<String> lore = new ArrayList<String>();
							NPCRegistry reg = CitizensAPI.getNPCRegistry();
							NPC npc = reg
									.getByUniqueId(plugin.questers.uniquenpcid
											.get(talk.getNpcid()));
							lore.add("Talk to: " + npc.getName());
							meta.setLore(lore);
							tal.setItemMeta(meta);
							items.add(tal);
						}
						break;
					}
				}
			}
		}
		int rowcount = items.size();
		if (rowcount % 9 == 0) {
			rowcount++;
		}
		while (rowcount % 9 != 0) {
			rowcount++;
		}
		if (rowcount == 0) {
			rowcount = 9;
		}
		Inventory inv = Bukkit.createInventory(null, rowcount, "Active Quests");
		for (ItemStack item : items) {
			inv.addItem(item);
		}
		player.openInventory(inv);
	}

	public void CompletedQuest(Player player) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if (Playerstats.completedquests.get(player.getUniqueId()) != null) {
			for (String type : Playerstats.completedquests.get(
					player.getUniqueId()).keySet()) {
				for (int number : Playerstats.completedquests.get(
						player.getUniqueId()).get(type)) {
					switch (type) {
					case "kill":
						QuestKill kill = returnkill(number);
						if(kill == null){
							removekill(number);
							continue;
						}
						ItemStack kil = new ItemStack(Material.IRON_SWORD);
						{
							ItemMeta meta = kil.getItemMeta();
							meta.setDisplayName(kill.getName());
							kil.setItemMeta(meta);
							items.add(kil);
						}
						break;
					case "harvest":
						QuestHarvest harvest = returnharvest(number);
						if(harvest == null){
							removeharvest(number);
							continue;
						}
						ItemStack har = new ItemStack(Material.IRON_PICKAXE);
						{
							ItemMeta meta = har.getItemMeta();
							meta.setDisplayName(harvest.getName());
							har.setItemMeta(meta);
							items.add(har);
						}
						break;
					case "talkto":
						QuestTalkto talk = returntalkto(number);
						if(talk == null){
							removetalkto(number);
							continue;
						}
						ItemStack tal = new ItemStack(Material.FEATHER);
						{
							ItemMeta meta = tal.getItemMeta();
							meta.setDisplayName(talk.getName());
							tal.setItemMeta(meta);
							items.add(tal);
						}
						break;
					}
				}
			}
		}
		int rowcount = items.size();
		if (rowcount % 9 == 0) {
			rowcount++;
		}
		while (rowcount % 9 != 0) {
			rowcount++;
		}
		if (rowcount == 0) {
			rowcount = 9;
		}
		Inventory inv = Bukkit.createInventory(null, rowcount,
				"Completed Quests");
		for (ItemStack item : items) {
			inv.addItem(item);
		}
		player.openInventory(inv);
	}

	public void RewardedQuest(Player player) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if (Playerstats.rewardedlist.get(player.getUniqueId()) != null) {
			for (String type : Playerstats.rewardedlist.get(
					player.getUniqueId()).keySet()) {
				for (int number : Playerstats.rewardedlist
						.get(player.getUniqueId()).get(type).keySet()) {
					long dated = Playerstats.rewardedlist
							.get(player.getUniqueId()).get(type).get(number);
					switch (type) {
					case "kill":
						QuestKill kill = returnkill(number);
						if (kill != null) {
							ItemStack kil = new ItemStack(Material.GOLD_SWORD);
							{
								ItemMeta meta = kil.getItemMeta();
								meta.setDisplayName(kill.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("Rewarded on " + new Timestamp(dated));
								meta.setLore(lore);
								kil.setItemMeta(meta);
								items.add(kil);
							}
						} else {
							Playerstats.rewardedlist.get(player.getUniqueId())
									.get(type).remove(number);
							plugin.database.deletecomkill(Integer.toString(number), player.getUniqueId().toString());
							removekill(number);
						}
						break;
					case "harvest":
						QuestHarvest harvest = returnharvest(number);
						if (harvest != null) {
							ItemStack har = new ItemStack(Material.GOLD_PICKAXE);
							{
								ItemMeta meta = har.getItemMeta();
								meta.setDisplayName(harvest.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("Rewarded on " + new Timestamp(dated));
								meta.setLore(lore);
								har.setItemMeta(meta);
								items.add(har);
							}
						} else {
							Playerstats.rewardedlist.get(player.getUniqueId())
									.get(type).remove(number);
							plugin.database.deletecomhar(Integer.toString(number), player.getUniqueId().toString());
							removeharvest(number);
						}
						break;
					case "talkto":
						QuestTalkto talk = returntalkto(number);
						if (talk != null) {
							ItemStack tal = new ItemStack(Material.FEATHER);
							{
								ItemMeta meta = tal.getItemMeta();
								meta.setDisplayName(talk.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("Rewarded on " + new Timestamp(dated));
								meta.setLore(lore);
								tal.setItemMeta(meta);
								items.add(tal);
							}
							break;
						} else {
							Playerstats.rewardedlist.get(player.getUniqueId())
									.get(type).remove(number);
							plugin.database.deletecomtalk(Integer.toString(number), player.getUniqueId().toString());
							removetalkto(number);

						}
					}
				}
			}
		}
		int rowcount = items.size();
		if (rowcount % 9 == 0) {
			rowcount++;
		}
		while (rowcount % 9 != 0) {
			rowcount++;
		}
		if (rowcount == 0) {
			rowcount = 9;
		}
		Inventory inv = Bukkit.createInventory(null, rowcount,
				"Rewarded Quests");
		for (ItemStack item : items) {
			inv.addItem(item);
		}
		player.openInventory(inv);
	}

	public Set<Integer> AllKillId() {
		return killquests.keySet();
	}

	public Set<Integer> AllHarvestId() {
		return harvestquests.keySet();
	}

	public Set<Integer> AllTalkToId() {
		return talktoquests.keySet();
	}

	public Set<Integer> AllWarpId() {
		return warplist.keySet();
	}

	public Set<Integer> AllHarbors() {
		return harborlocation.keySet();
	}

	public Set<Integer> AllTrips() {
		return triplocations.keySet();
	}

	private String returnname(String type, int number) {
		switch (type) {
		case "kill":
			return returnkill(number).getName();
		case "harvest":
			return returnharvest(number).getName();
		case "talkto":
			return returntalkto(number).getName();
		default:
			return "null";
		}
	}
	public String GetItemName(ItemStack item){
		return CraftItemStack.asNMSCopy(item).getName();
	}
}
