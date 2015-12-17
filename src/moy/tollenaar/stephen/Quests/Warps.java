package moy.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

import moy.tollenaar.stephen.InventoryUtils.ItemGenerator;

public class Warps {
	private static int BY_ID_COUNT = 0;
	
	
	private int warpid;

	private String name;

	private Location startloc;

	private ArrayList<String> type;

	private double costs;

	private String state;
	
	private String byPassTime;
	private Set<Integer> byPassID;
	private boolean overrideonly;
	
	public static ArrayList<Warps> allwarps = new ArrayList<Warps>();

	public void npcsettingswarplists(UUID npcuuid, Player player) {

		Inventory warpinv = Bukkit.createInventory(null, 18, "WarpList");
		// title
		ItemStack title = ItemGenerator.InfoQuest(name, warpid, 4,
				npcuuid.toString());

		// transportation
		ItemStack transport = new ItemStack(Material.BOAT);
		{
			List<String> temp = new ArrayList<String>();
			temp.add("X: " + startloc.getX());
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
			for (String in : type) {
				temp.add(in);
			}
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

		//queststate
		ItemStack qstate=  ItemGenerator.ActiveQuest(state);
		ItemMeta meta = qstate.getItemMeta();
		meta.setDisplayName("Active Warp");
		qstate.setItemMeta(meta);
		
		// delete
		Wool wool = new Wool(DyeColor.RED);
		ItemStack delete = new ItemStack(wool.toItemStack());
		{
			ItemMeta temp = delete.getItemMeta();
			temp.setDisplayName("Delete Warp");
			delete.setItemMeta(temp);
		}
		ItemStack override = ItemGenerator.ByPassWarp(getBypassedTime(), true);
		ItemStack overrideOnly = ItemGenerator.ByPassWarp(Boolean.toString(overrideonly), false);
		ItemStack overrideid = ItemGenerator.ByPassWarpID(byPassID);

		warpinv.addItem(title);
		warpinv.addItem(transport);
		warpinv.addItem(typei);
		warpinv.addItem(money);
		warpinv.addItem(qstate);
		warpinv.addItem(override);
		warpinv.addItem(overrideOnly);
		warpinv.addItem(overrideid);
		warpinv.setItem(warpinv.getSize() - 2, delete);
		warpinv.setItem(warpinv.getSize() - 1, main);
		
		player.openInventory(warpinv);

	}
	
	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Warps(int number, Location l) {
		this.warpid = number;
		this.name = "title";
		this.startloc = l;
		this.type = new ArrayList<String>(Arrays.asList("none"));
		this.costs = 0.0;
		this.state = "disabled";
		this.setBypassedTime("-1");
		allwarps.add(this);
		this.byPassID = new HashSet<Integer>();
	}
	public void deleteOverRideId(int id){
		this.byPassID.remove(id);
	}
	
	public double getCosts() {
		return costs;
	}

	public static ArrayList<Warps> TypeReturned(String type) {
		ArrayList<Warps> thewarps = new ArrayList<Warps>();
		for (Warps w : allwarps) {
			if (w.getType().contains(type)) {
				thewarps.add(w);
			}
		}
		return thewarps;
	}

	public void setCosts(double costs2) {
		this.costs = costs2;
	}

	public void SetLine(int line, String typed) {
		type.set(line, typed);
	}
	
	public void AddLine(String typed){
		type.add(typed);
	}

	public ArrayList<String> getType(){
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

	public String getBypassedTime() {
		return byPassTime;
	}

	public void setBypassedTime(String bypassed) {
		this.byPassTime = bypassed;
	}

	public Set<Integer> getByPassID() {
		return byPassID;
	}

	public void addByPassID(int byPassID) {
		this.byPassID.add(byPassID);
	}

	public void generateByPass(){
		this.byPassID.add(BY_ID_COUNT);
		BY_ID_COUNT++;
	}
	public boolean isOverrideOnly(){
		return overrideonly;
	}
	public void setOverrideOnly(boolean override){
		this.overrideonly  = override;
	}
	
}
