package moy.tollenaar.stephen.InventoryUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

public class ItemGenerator {
	public static ItemStack InfoQuest(String name, int id, int type,
			String npcuuid) {
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> info = new ArrayList<String>();
		info.add(name);

		info.add("§" + type);
		info.add(Integer.toString(id));

		String builder = "";
		if (npcuuid != null) {
			for (String in : npcuuid.split("")) {
				builder += "§" + in;
			}
		}
		info.add(builder);
		meta.setDisplayName("Title");
		meta.setLore(info);
		item.setItemMeta(meta);
		return item;
	}

	// the difference in the quests

	@SuppressWarnings("deprecation")
	public static ItemStack QuestReq(String thing) {
		ItemStack item;
		try {

			EntityType ent = EntityType.valueOf(thing.toUpperCase());
			ent.getName().charAt(0);
			item = new ItemStack(Material.SKULL_ITEM);
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> info = new ArrayList<String>();
			info.add(thing);
			meta.setDisplayName("Mob");
			meta.setLore(info);
			item.setItemMeta(meta);
			return item;

		} catch (Exception ex) {
			item = new ItemStack(Material.IRON_BLOCK);
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> info = new ArrayList<String>();
			String[] tp = thing.split(":");
			int[] id = new int[2];
			for (int i = 0; i < tp.length; i++) {
				id[i] = Integer.parseInt(tp[i].trim());
			}
			ItemStack temp = null;
			if (id.length == 1) {
				temp = new ItemStack(Material.getMaterial(id[0]));
			} else if (id.length == 2) {
				temp = new ItemStack(Material.getMaterial(id[0]), 1,
						(short) id[1]);
			}
			info.add(thing);
			info.add(temp.toString());
			meta.setDisplayName("Item to get");
			meta.setLore(info);
			item.setItemMeta(meta);
			return item;
		}
	}

	public static ItemStack PersonQuest(int id) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> info = new ArrayList<String>();
		info.add(Integer.toString(id));
		meta.setDisplayName("Person");
		meta.setLore(info);
		item.setItemMeta(meta);
		return item;
	}

	// end difference

	public static ItemStack CountQuest(int count) {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> info = new ArrayList<String>();
		info.add(Integer.toString(count));
		meta.setDisplayName("Count");
		meta.setLore(info);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack RewardQuest(List<String> reward) {
		ItemStack item = new ItemStack(Material.GOLD_INGOT);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> info = new ArrayList<String>();
		for (String in : reward) {
			info.add(in);
		}
		meta.setDisplayName("Reward");
		meta.setLore(info);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack MinLvlQuest(int lvl) {
		ItemStack item = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> info = new ArrayList<String>();
		info.add(Integer.toString(lvl));
		meta.setDisplayName("Minimum Lvl");
		meta.setLore(info);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack MessageQuest(String message) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> info = new ArrayList<String>();
		info.add(message);
		meta.setDisplayName("Message");
		meta.setLore(info);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack RepeatQuest(String delay) {
		ItemStack item = new ItemStack(Material.WATCH);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> info = new ArrayList<String>();
		info.add(delay);
		meta.setDisplayName("Repeat Delay");
		meta.setLore(info);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack PrereqQuest(String preq) {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> info = new ArrayList<String>();
		info.add(preq);
		meta.setDisplayName("Prerequisite");
		meta.setLore(info);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack DateQuest(long date, boolean start) {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> info = new ArrayList<String>();
		info.add(new Date(date).toString());
		if (start) {
			meta.setDisplayName("Start Date");
		} else {
			meta.setDisplayName("End Date");
		}
		meta.setLore(info);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack ActiveQuest(String state) {
		Wool colored;
		if (state.equals("active")) {
			colored = new Wool(DyeColor.LIME);
		} else {
			colored = new Wool(DyeColor.RED);
		}
		ItemStack item = new ItemStack(colored.toItemStack());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Active Quest");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(state);
		meta.setLore(lore);
		item.setItemMeta(meta);

		return item;

	}

	public static ItemStack ByPassWarp(String bypassed, boolean isTime) {
		ItemStack item = new ItemStack(Material.WATCH);
		ItemMeta meta = item.getItemMeta();
		if (isTime) {
			meta.setDisplayName("Override");
		} else {
			meta.setDisplayName("OverrideID");
		}
		ArrayList<String> list = new ArrayList<String>();
		list.add(bypassed);
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}

}
