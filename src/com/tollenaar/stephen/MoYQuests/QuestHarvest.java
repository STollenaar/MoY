package com.tollenaar.stephen.MoYQuests;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import com.tollenaar.stephen.MoYMistsOfYsir.MoY;

public class QuestHarvest extends Quest {

	private int questnumber;

	private String name;

	private String reward;

	private int count;
	private int minlvl;

	private String delay;

	private String prereq;

	private String message;

	private String itemid;

	@SuppressWarnings("deprecation")
	public void qinventory(Player player, UUID npcuuid) {

		Inventory harvestques = Bukkit
				.createInventory(null, 18, "HarvestQuest");

		// title
		ItemStack title = new ItemStack(Material.BOOK);
		{

			List<String> temp = new ArrayList<String>();
			temp.add(name);
			ItemMeta tem = title.getItemMeta();
			tem.setDisplayName("Title");
			tem.setLore(temp);
			title.setItemMeta(tem);
		}

		// Item
		ItemStack monster = new ItemStack(Material.IRON_BLOCK);
		{
			List<String> temp = new ArrayList<String>();
			String[] tp = itemid.split(":");
			int[] id = new int[2];
			for (int i = 0; i < tp.length; i++) {
				id[i] = Integer.parseInt(tp[i].trim());
			}
			ItemStack item = null;
			if (id.length == 1) {
				item = new ItemStack(Material.getMaterial(id[0]));
			} else if (id.length == 2) {
				item = new ItemStack(Material.getMaterial(id[0]), 1,
						(short) id[1]);
			}
			System.out.println(item.toString());
			temp.add(itemid);
			temp.add(item.toString());
			ItemMeta tem = monster.getItemMeta();
			tem.setDisplayName("Item to get");
			tem.setLore(temp);
			monster.setItemMeta(tem);
		}
		// count items
		ItemStack counta = new ItemStack(Material.ARROW);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(Integer.toString(count));
			ItemMeta tem = counta.getItemMeta();
			tem.setDisplayName("Count");
			tem.setLore(temp);
			counta.setItemMeta(tem);
		}
		// reward
		ItemStack rewardc = new ItemStack(Material.GOLD_INGOT);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(reward);
			ItemMeta tem = rewardc.getItemMeta();
			tem.setDisplayName("Reward");
			tem.setLore(temp);
			rewardc.setItemMeta(tem);
		}
		// min level
		ItemStack lvl = new ItemStack(Material.EXP_BOTTLE);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(Integer.toString(minlvl));
			ItemMeta tem = lvl.getItemMeta();
			tem.setDisplayName("Minimum Lvl");
			tem.setLore(temp);
			lvl.setItemMeta(tem);
		}
		// repeat
		ItemStack repeat = new ItemStack(Material.WATCH);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(delay);
			ItemMeta tem = repeat.getItemMeta();
			tem.setDisplayName("Repeat Delay");
			tem.setLore(temp);
			repeat.setItemMeta(tem);
		}
		// message
		ItemStack messagei = new ItemStack(Material.PAPER);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(message);
			ItemMeta tem = messagei.getItemMeta();
			tem.setDisplayName("Message");
			tem.setLore(temp);
			messagei.setItemMeta(tem);
		}
		// prerequisite
		ItemStack prereqi = new ItemStack(Material.ENCHANTED_BOOK);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(prereq);
			ItemMeta tem = prereqi.getItemMeta();
			tem.setDisplayName("Prerequisite");
			tem.setLore(temp);
			prereqi.setItemMeta(tem);
		}
		// delete
		Wool wool = new Wool(DyeColor.RED);
		ItemStack delete = new ItemStack(wool.toItemStack());
		{
			ItemMeta temp = delete.getItemMeta();
			temp.setDisplayName("Delete Quest");
			delete.setItemMeta(temp);
		}
		// tomain
		ItemStack main = new ItemStack(Material.NETHER_STAR);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(npcuuid.toString());
			temp.add(Integer.toString(questnumber));
			ItemMeta tem = main.getItemMeta();
			tem.setDisplayName("To Main");
			tem.setLore(temp);
			main.setItemMeta(tem);
		}

		harvestques.addItem(title);
		harvestques.addItem(monster);
		harvestques.addItem(counta);
		harvestques.addItem(rewardc);
		harvestques.addItem(lvl);
		harvestques.addItem(repeat);
		harvestques.addItem(messagei);
		harvestques.addItem(prereqi);
		harvestques.setItem(harvestques.getSize() - 2, delete);
		harvestques.setItem(harvestques.getSize() - 1, main);
		player.openInventory(harvestques);

	}

	public QuestHarvest(int number, MoY instance) {
		super(instance);

		this.questnumber = number;
		this.name = "title";
		this.count = 0;
		this.minlvl = 0;
		this.delay = "0s";
		this.prereq = "none=0";
		this.itemid = "1";
		this.message = "message";
		this.reward = "unknown";
	}

	public int getQuestnumber() {
		return questnumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMinlvl() {
		return minlvl;
	}

	public void setMinlvl(int minlvl) {
		this.minlvl = minlvl;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getPrereq() {
		return prereq;
	}

	public void setPrereq(String prereq) {
		this.prereq = prereq;
	}

	public String getItemId() {
		return itemid;
	}

	public void setItem(String itemid) {
		this.itemid = itemid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
