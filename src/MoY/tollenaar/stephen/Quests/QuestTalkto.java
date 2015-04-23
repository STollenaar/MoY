package MoY.tollenaar.stephen.Quests;

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

public class QuestTalkto {
	private int questnumber;
	
	private String name;
	
	private int npcid;
	
	private String reward;
	
	private String delay;
	private String message;
	
	private int minlvl;
	
	private String prereq;
	
	public QuestTalkto(int number){
		this.questnumber = number;
		this.name = "title";
		this.npcid = 0;
		this.reward = "unknown";
		this.delay = "0s";
		this.message = "message";
		this.minlvl = 0;
		this.prereq = "none=0";
	}
	
	public void npcsettingstalkto(UUID npcuuid, Player player) {

		Inventory talktoques = Bukkit.createInventory(null, 18, "TalktoQuest");

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
		// person
		ItemStack person = new ItemStack(Material.SKULL_ITEM);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(Integer.toString(npcid));
			ItemMeta tem = person.getItemMeta();
			tem.setDisplayName("Person");
			tem.setLore(temp);
			person.setItemMeta(tem);
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
		// min lvl
		ItemStack lvl = new ItemStack(Material.EXP_BOTTLE);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(Integer.toString(minlvl));
			ItemMeta tem = lvl.getItemMeta();
			tem.setDisplayName("Minimum Lvl");
			tem.setLore(temp);
			lvl.setItemMeta(tem);
		}
		// repeat delay
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

		talktoques.addItem(title);
		talktoques.addItem(person);
		talktoques.addItem(rewardi);
		talktoques.addItem(lvl);
		talktoques.addItem(repeat);
		talktoques.addItem(messagei);
		talktoques.addItem(prereqi);
		talktoques.setItem(talktoques.getSize() - 2, delete);
		talktoques.setItem(talktoques.getSize() - 1, main);
		player.openInventory(talktoques);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNpcid() {
		return npcid;
	}

	public void setNpcid(int npcid) {
		this.npcid = npcid;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMinlvl() {
		return minlvl;
	}

	public void setMinlvl(int minlvl) {
		this.minlvl = minlvl;
	}

	public String getPrereq() {
		return prereq;
	}

	public void setPrereq(String prereq) {
		this.prereq = prereq;
	}

	public int getQuestnumber() {
		return questnumber;
	}
	
	
}
