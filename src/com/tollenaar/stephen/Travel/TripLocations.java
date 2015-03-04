package com.tollenaar.stephen.Travel;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TripLocations {
	
	public static HashSet<TripLocations> locations = new HashSet<TripLocations>();
	public static HashSet<TripLocations> inuse = new HashSet<TripLocations>();
	
	
	public TripLocations(int id){
		this.id = id;
		this.type = "undefined";
		this.location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		locations.add(this);
	}
	
	
	private int id;
	private String type;
	private Location location;
	
	public void information(Player player){
		Inventory inv =  Bukkit.createInventory(null, 9, "Trip Location info");
		
		ItemStack infoid = new ItemStack(Material.BOOK);
		{
			ItemMeta im = infoid.getItemMeta();
			im.setDisplayName("Id");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(Integer.toString(id));
			im.setLore(lore);
			infoid.setItemMeta(im);
			inv.addItem(infoid);
		}
		
		ItemStack triptype = new ItemStack(Material.MINECART);
		{
			ItemMeta im = triptype.getItemMeta();
			im.setDisplayName("Trip Type");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(type);
			im.setLore(lore);
			triptype.setItemMeta(im);
			inv.addItem(triptype);
		}
		
		ItemStack loc = new ItemStack(Material.COMPASS);
		{
			ItemMeta im = loc.getItemMeta();
			im.setDisplayName("Trip Location");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("X: " + location.getX());
			lore.add("Y: " + location.getY());
			lore.add("Z: " + location.getZ());
			lore.add("World: " + location.getWorld().getName());
			im.setLore(lore);
			loc.setItemMeta(im);
			inv.addItem(loc);
		}
		
		
		
		player.openInventory(inv);
	}

	public static HashSet<TripLocations> getLocations() {
		return locations;
	}

	public static void setLocations(HashSet<TripLocations> locations) {
		TripLocations.locations = locations;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public static HashSet<TripLocations> getInuse() {
		return inuse;
	}

	public static void setInuse(HashSet<TripLocations> inuse) {
		TripLocations.inuse = inuse;
	}


	
	
	
	
}
