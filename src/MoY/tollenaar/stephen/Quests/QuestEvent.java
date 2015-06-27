package MoY.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.Arrays;
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

import MoY.tollenaar.stephen.InventoryUtils.ItemGenerator;

public class QuestEvent {
	private int number;
	private String title;
	private long startdate;
	private long enddate;
	private List<String> reward;
	private int count;
	private int minlvl;
	private String message;
	private String repeat;
	
	private String type;
	
	public QuestEvent(int number){
		this.number = number;
		this.title = "title";
		this.startdate = 0;
		this.enddate = 0;
		this.reward = new ArrayList<String>(Arrays.asList("unknown"));
		this.count = 0;
		this.minlvl = 0;
		this.message = "message";
		this.repeat = "-1";
		this.type = "0";
	}

	public int getNumber() {
		return number;
	}


	public void openinv(Player player, UUID npcuuid){
		Inventory inv = Bukkit.createInventory(null, 18, "EventQuest");
		
		ItemStack info = ItemGenerator.InfoQuest(title, number, 7, npcuuid.toString());
		
		ItemStack thing = ItemGenerator.QuestReq(type);
		
		ItemStack minl = ItemGenerator.MinLvlQuest(minlvl);
		ItemStack coun = ItemGenerator.CountQuest(count);
		ItemStack rew = ItemGenerator.RewardQuest(reward);
		ItemStack mes = ItemGenerator.MessageQuest(message);
		ItemStack start = ItemGenerator.DateQuest(startdate, true);
		ItemStack end = ItemGenerator.DateQuest(enddate, false);
		ItemStack rep = ItemGenerator.RepeatQuest(getRepeat());
		
		
		inv.addItem(info);
		inv.addItem(thing);
		inv.addItem(coun);
		inv.addItem(start);
		inv.addItem(end);
		inv.addItem(minl);
		inv.addItem(rew);
		inv.addItem(mes);
		inv.addItem(rep);
		
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
			ItemMeta tem = main.getItemMeta();
			tem.setDisplayName("To Main");
			tem.setLore(temp);
			main.setItemMeta(tem);
		}
		inv.setItem(inv.getSize() - 2, delete);
		inv.setItem(inv.getSize() - 1, main);
		player.openInventory(inv);
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public long getStartdate() {
		return startdate;
	}


	public void setStartdate(long startdate) {
		this.startdate = startdate;
	}


	public long getEnddate() {
		return enddate;
	}


	public void setEnddate(long enddate) {
		this.enddate = enddate;
	}


	public List<String> getReward() {
		return reward;
	}


	public void setReward(List<String> reward) {
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


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public void setReward(String reward) {
		this.reward.add(reward);
	}
	
}
