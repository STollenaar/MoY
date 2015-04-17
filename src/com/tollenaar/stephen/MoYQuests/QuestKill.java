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

public class QuestKill {

	private int questnumber;

	private String name;

	private String reward;

	private int count;
	private int minlvl;

	private String delay;

	private String prereq;

	private String message;

	private String monster;

	
	public void npcsettingskill(UUID npcuuid, Player player) {

		Inventory killquest = Bukkit.createInventory(null, 18, "KillQuest");

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

		// monster
		ItemStack monsteri = new ItemStack(Material.SKULL_ITEM);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(monster);
			ItemMeta tem = monsteri.getItemMeta();
			tem.setDisplayName("Mob");
			tem.setLore(temp);
			monsteri.setItemMeta(tem);
		}
		// count monsters
		ItemStack counti = new ItemStack(Material.ARROW);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(Integer.toString(count));
			ItemMeta tem = counti.getItemMeta();
			tem.setDisplayName("Count");
			tem.setLore(temp);
			counti.setItemMeta(tem);
		}
		// reward
		ItemStack rewardi = new ItemStack(Material.GOLD_INGOT);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(reward);
			ItemMeta tem = rewardi.getItemMeta();
			tem.setDisplayName("Reward");
			tem.setLore(temp);
			rewardi.setItemMeta(tem);
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

		killquest.addItem(title);
		killquest.addItem(monsteri);
		killquest.addItem(counti);
		killquest.addItem(rewardi);
		killquest.addItem(lvl);
		killquest.addItem(repeat);
		killquest.addItem(messagei);
		killquest.addItem(prereqi);
		killquest.setItem(killquest.getSize() - 2, delete);
		killquest.setItem(killquest.getSize() - 1, main);
		player.openInventory(killquest);
	}
	
	
	public QuestKill(int number) {
		this.questnumber = number;
		this.name = "title";
		this.count = 0;
		this.minlvl = 0;
		this.delay = "0s";
		this.reward = "unkown";
		this.message = "message";
		this.prereq = "none=0";
		this.monster = "unknown";
		
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

	public String getMonster() {
		return monster;
	}

	public void setMonster(String monster) {
		this.monster = monster;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
