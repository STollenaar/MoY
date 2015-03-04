package com.tollenaar.stephen.Quests;

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

public class Warps {
	private int warpid;
	
	private String name;

	private Location endlocation;
	private Location harborwaiting;
	private Location trip;
	
	private String type;
	
	private double costs;

	
	public void npcsettingswarplists(UUID npcuuid, Player player) {

		Inventory warpinv = Bukkit.createInventory(null, 18, "WarpList");
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

		// transportation
		ItemStack transport = new ItemStack(Material.BOAT);
		{
			List<String> temp = new ArrayList<String>();
			temp.add("X: " +trip.getX());
			temp.add("Y: " + trip.getY());
			temp.add("Z: " + trip.getZ());
			temp.add("World: " + trip.getWorld().getName());
			ItemMeta tem = transport.getItemMeta();
			tem.setDisplayName("Transportation Location");
			tem.setLore(temp);
			transport.setItemMeta(tem);
		}
		// endlocation
		ItemStack end = new ItemStack(Material.COMPASS);
		{
			List<String> temp = new ArrayList<String>();
			temp.add("X: " + endlocation.getX());
			temp.add("Y: " + endlocation.getY());
			temp.add("Z: " + endlocation.getZ());
			temp.add("World: " + endlocation.getWorld().getName());

			ItemMeta tem = end.getItemMeta();
			tem.setDisplayName("End Location");
			tem.setLore(temp);
			end.setItemMeta(tem);
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
		// fakeboat
		ItemStack fake = new ItemStack(Material.WATER_BUCKET);
		{
			List<String> temp = new ArrayList<String>();
			temp.add("X: " + harborwaiting.getX());
			temp.add("Y: " + harborwaiting.getY());
			temp.add("Z: " + harborwaiting.getZ());
			temp.add("World: " + harborwaiting.getWorld().getName());
			
			ItemMeta tem = fake.getItemMeta();
			tem.setDisplayName("Boarded Harbor");
			tem.setLore(temp);
			fake.setItemMeta(tem);
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
			temp.add(npcuuid.toString());
			temp.add(Integer.toString(warpid));
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
		warpinv.addItem(end);
		warpinv.addItem(fake);
		warpinv.addItem(typei);
		warpinv.addItem(money);
		warpinv.setItem(warpinv.getSize() - 2, delete);
		warpinv.setItem(warpinv.getSize() - 1, main);
		player.openInventory(warpinv);

	}
	
	
	public Warps(int number) {
		this.warpid = number;
		this.name = "title";
		Location t = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		this.endlocation = t;
		this.harborwaiting = t;
		this.trip = t;
		this.type = "none";
		this.costs = 0.0;
	}

	
	
	public double getCosts() {
		return costs;
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

	public Location getEndlocation() {
		return endlocation;
	}

	public void setEndlocation(Location endlocation) {
		this.endlocation = endlocation;
	}

	public Location getHarborwaiting() {
		return harborwaiting;
	}

	public void setHarborwaiting(Location harborwaiting) {
		this.harborwaiting = harborwaiting;
	}

	public Location getTrip() {
		return trip;
	}

	public void setTrip(Location trip) {
		this.trip = trip;
	}

	public int getWarpid() {
		return warpid;
	}

}
