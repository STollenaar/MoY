package MoY.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import MoY.tollenaar.stephen.InventoryUtils.ItemGenerator;

public class Warps {
	private int warpid;
	
	private String name;

	private Location startloc;
	
	private String type;
	
	private double costs;

	public static ArrayList<Warps> allwarps = new ArrayList<Warps>();
	
	public void npcsettingswarplists(UUID npcuuid, Player player) {

		Inventory warpinv = Bukkit.createInventory(null, 18, "WarpList");
		// title
		ItemStack title = ItemGenerator.InfoQuest(name, warpid, 4, npcuuid.toString());

		// transportation
		ItemStack transport = new ItemStack(Material.BOAT);
		{
			List<String> temp = new ArrayList<String>();
			temp.add("X: " +startloc.getX());
			temp.add("Y: " + startloc.getY());
			temp.add("Z: " + startloc.getZ());
			temp.add("World: " + startloc.getWorld().getName());
			ItemMeta tem = transport.getItemMeta();
			tem.setDisplayName("Start Location");
			tem.setLore(temp);
			transport.setItemMeta(tem);
		}
		
		// type
		ItemStack typei = new ItemStack(Material.MINECART);
		{
			List<String> temp = new ArrayList<String>();
			temp.add(type);
			ItemMeta tem = typei.getItemMeta();
			tem.setDisplayName("Type of transportation");
			tem.setLore(temp);
			typei.setItemMeta(tem);
		}
		
		// costs
		ItemStack money = new ItemStack(Material.GOLD_NUGGET);
		{
			ItemMeta temp = money.getItemMeta();
			temp.setDisplayName("Cost of Ride");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(Double.toString(costs));
			temp.setLore(lore);
			money.setItemMeta(temp);
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

		// delete
		Wool wool = new Wool(DyeColor.RED);
		ItemStack delete = new ItemStack(wool.toItemStack());
		{
			ItemMeta temp = delete.getItemMeta();
			temp.setDisplayName("Delete Warp");
			delete.setItemMeta(temp);
		}

		warpinv.addItem(title);
		warpinv.addItem(transport);
		warpinv.addItem(typei);
		warpinv.addItem(money);
		warpinv.setItem(warpinv.getSize() - 2, delete);
		warpinv.setItem(warpinv.getSize() - 1, main);
		player.openInventory(warpinv);

	}
	
	
	public Warps(int number, Location l) {
		this.warpid = number;
		this.name = "title";
		this.startloc = l;
		this.type = "none";
		this.costs = 0.0;
		allwarps.add(this);
	}

	
	
	public double getCosts() {
		return costs;
	}



	public static ArrayList<Warps> TypeReturned(String type){
		ArrayList<Warps> thewarps = new ArrayList<Warps>();
		for(Warps w : allwarps){
			if(w.GetType().contains(type)){
				thewarps.add(w);
			}
		}
		return thewarps;
	}
	
	public void setCosts(double costs2) {
		this.costs = costs2;
	}



	public void SetType(String t){
		this.type = t;
	}
	
	public String GetType(){
		return type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public int getWarpid() {
		return warpid;
	}


	public Location getStartloc() {
		return startloc;
	}

}
