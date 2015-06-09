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

public class QuestKill {

	private int questnumber;

	private String name;

	private List<String> reward;

	private int count;
	private int minlvl;

	private String delay;

	private String prereq;

	private String message;

	private String monster;

	
	public void npcsettingskill(UUID npcuuid, Player player) {

		Inventory killquest = Bukkit.createInventory(null, 18, "KillQuest");

		// title
		ItemStack title = ItemGenerator.InfoQuest(name, questnumber, 1, npcuuid.toString());

		// monster
		ItemStack monsteri = ItemGenerator.QuestReq(monster);
		// count monsters
		ItemStack counti =ItemGenerator.CountQuest(count);
		// reward
		ItemStack rewardi = ItemGenerator.RewardQuest(reward);
		// min level
		ItemStack lvl = ItemGenerator.MinLvlQuest(minlvl);
		// repeat
		ItemStack repeat =ItemGenerator.RepeatQuest(delay);
		// message
		ItemStack messagei = ItemGenerator.MessageQuest(message);
		// prerequisite
		ItemStack prereqi = ItemGenerator.PrereqQuest(prereq);
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
		this.reward = new ArrayList<String>(Arrays.asList("unknown"));
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

	public List<String> getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward.add(reward);
	}

	public void setReward(List<String> reward){
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
