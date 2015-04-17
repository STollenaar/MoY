package com.tollenaar.stephen.MoYTravel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tollenaar.stephen.MoYMistsOfYsir.MoY;

@SuppressWarnings("deprecation")
public class TravelDragonEvent implements Listener {
	MoY plugin;
		
	private  HashMap<Integer, ArrayList<Location>> spawners = new HashMap<Integer, ArrayList<Location>>(); //all the spawner locations needed for the controlling of monster borders
	public  HashMap<Location, Entity> entities = new HashMap<Location, Entity>(); //all the spawner locations with their monster
	public  HashMap<Location, Integer> locationid = new HashMap<Location, Integer>(); //location of spawner to id of the event. needed for monster init 
	
	public HashMap<Integer, ArrayList<String>> eventlocations = new HashMap<Integer, ArrayList<String>>(); //all the needed info about one event
	
	private HashMap<UUID, ArrayList<String>> tempevent = new HashMap<UUID, ArrayList<String>>(); //needed for making and event
	
	
	private HashMap<Integer, Location> endlocid = new HashMap<Integer, Location>(); //END location of the event
	private HashMap<Location, HashMap<Location, ArrayList<UUID>>> tempplayers = new HashMap<Location, HashMap<Location, ArrayList<UUID>>>(); //getting all the players
	public HashMap<Integer, ArrayList<UUID>> playersatevent = new HashMap<Integer, ArrayList<UUID>>(); //proper storing of all the players in one event
	public HashMap<UUID, Integer> playeratevent = new HashMap<UUID, Integer>(); //player to event id. needed for the player border controlling
	
	
	private HashMap<Integer, Integer> schedulerborder = new HashMap<Integer, Integer>(); //needed for canceling scheduler that controls the border restrictions on monsters
	private HashMap<Location, Location> temprunonce = new HashMap<Location, Location>();

	
	
	
	private void eventghast(int time, final int id){
		Location Border1 = Bordercalc(id, 1);
		Location Border2 = Bordercalc(id, 2);
		final World world = Border1.getWorld();
		final ArrayList<Integer> coords = borderlocations(Border1, Border2);
		Location random1 = randomloc(coords, world);
		Location random2 = randomloc(coords, world);
		Location random3 = randomloc(coords, world);
		ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(random1);
		locations.add(random2);
		locations.add(random3);
		spawners.put(id, locations);
		locationid.put(random1, id);
		locationid.put(random2, id);
		locationid.put(random3, id);
		random1.getWorld().spawnEntity(random1, EntityType.GHAST);
		random1.getWorld().spawnEntity(random2, EntityType.GHAST);
		random1.getWorld().spawnEntity(random3, EntityType.GHAST);
		Bordercontrolmonster(coords, world, id);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				Bukkit.getScheduler().cancelTask(schedulerborder.get(id));
				entremove(coords, world);
				Location end = endlocid.get(id);
				for(UUID uuid : playersatevent.get(id)){
					for(Player onplayer : Bukkit.getOnlinePlayers()){
						if(onplayer.getUniqueId() == uuid){
							playeratevent.remove(uuid);
							onplayer.teleport(end);
							onplayer.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YTravel" + ChatColor.DARK_PURPLE + "]" + 
									ChatColor.AQUA + " After all this trouble we have finally arived at your destination.");
							Travel.schedulerstor.remove(uuid);
							Travel.startlocations.remove(uuid);
						}
						}
				}
				idremover(id);
			}
		}, time*20L);
	}
	
	private void eventghost(int time, final int id){
		Location Border1 = Bordercalc(id, 1);
		Location Border2 = Bordercalc(id, 2);
		final ArrayList<Integer> coords = borderlocations(Border1, Border2);
			int skel = 10;
			int zomb = 10;
			ArrayList<Location> locations = new ArrayList<Location>();
			for(int x = coords.get(0); x < coords.get(1); x++){
				for(int z = coords.get(4) ;z < coords.get(5); z++){
					for(int y = coords.get(2); y < coords.get(3); y++){
					Location loc = new Location(Border1.getWorld(), x, y, z);
					
					Block block = loc.getBlock();
					if(block.getType() == Material.SPONGE){
						locations.add(loc);
						locationid.put(loc, id);
						Random r = new Random();
						int next = r.nextInt(1);
						if(next == 0){
							if(skel > 0){
							skel--;
							Skeleton sk = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
							sk.setCustomName("skel dr");
							}else if(zomb > 0){
								zomb--;
							Zombie zm = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
							zm.setCustomName("zomb dr");
							}
						}else{
							if(zomb > 0) {
							zomb--;
							Zombie zm = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
							zm.setCustomName("zomb dr");							
							}else if(skel > 0){
								skel--;
								Skeleton sk = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
								sk.setCustomName("skel dr");
								}
						}
					}
				}
			}
		}
		spawners.put(id, locations);
		final World world = Border1.getWorld();
		Bordercontrolmonster(coords, world, id);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				Bukkit.getScheduler().cancelTask(schedulerborder.get(id));
			entremove(coords, world);
			Location end = endlocid.get(id);
			for(UUID uuid : playersatevent.get(id)){
				for(Player onplayer : Bukkit.getOnlinePlayers()){
					if(onplayer.getUniqueId() == uuid){
						playeratevent.remove(uuid);
						onplayer.teleport(end);
						onplayer.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YTravel" + ChatColor.DARK_PURPLE + "]" + 
								ChatColor.AQUA + " After all this trouble we have finally arived at your destination.");
						playeratevent.remove(uuid);
						Travel.schedulerstor.remove(uuid);
						Travel.startlocations.remove(uuid);
					}
					}
			}
			idremover(id);
			}
		},time*20L);
			
			
	}	
	
	private void eventwither(int time, final int id){
		Location Border1 = Bordercalc(id, 1);
		Location Border2 = Bordercalc(id, 2);
		final World world = Border1.getWorld();
		final ArrayList<Integer> coords = borderlocations(Border1, Border2);
		Location random = randomloc(coords, world);
		ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(random);
		spawners.put(id, locations);
		locationid.put(random, id);
		random.getWorld().spawnEntity(random, EntityType.WITHER);
		Bordercontrolmonster(coords, world, id);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				Bukkit.getScheduler().cancelTask(schedulerborder.get(id));
				entremove(coords, world);
				Location end = endlocid.get(id);
				for(UUID uuid : playersatevent.get(id)){
					for(Player onplayer : Bukkit.getOnlinePlayers()){
						if(onplayer.getUniqueId() == uuid){
							playeratevent.remove(uuid);
							onplayer.teleport(end);
							onplayer.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YTravel" + ChatColor.DARK_PURPLE + "]" + 
									ChatColor.AQUA + " After all this trouble we have finally arived at your destination.");
							Travel.schedulerstor.remove(uuid);
							Travel.startlocations.remove(uuid);
						}
						}
				}
				idremover(id);
			}
		}, time*20L);
	}
	
	public void eventint(final Player player, final Location start, final Location end, final int time){
		if(tempplayers.get(start) == null){
			HashMap<Location, ArrayList<UUID>> temp = new HashMap<Location, ArrayList<UUID>>();
			ArrayList<UUID> players = new ArrayList<UUID>();
			players.add(player.getUniqueId());
			temp.put(end, players);
			tempplayers.put(start, temp);
		}else if(!tempplayers.get(start).keySet().contains(end)){
			HashMap<Location, ArrayList<UUID>> temp = tempplayers.get(start);
			ArrayList<UUID> players = new ArrayList<UUID>();
			players.add(player.getUniqueId());
			temp.put(end, players);
		}else{
			ArrayList<UUID> players = tempplayers.get(start).get(end);
			players.add(player.getUniqueId());
		}
	
		if(temprunonce.get(start) != null){ 
			temprunonce.remove(start);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					
					ArrayList<Location> startlocs = new ArrayList<Location>();
					startlocs.addAll(tempplayers.keySet());
					for(Location loc : startlocs){
						ArrayList<Location> endlocs = new ArrayList<Location>();
						endlocs.addAll(tempplayers.get(loc).keySet());
						for(Location locs : endlocs){
							ArrayList<UUID> players = tempplayers.get(loc).get(locs);
							for(int index = 0; index < playersatevent.size(); index++){
								if(playersatevent.get(index) == null){
									playersatevent.put(index, players);
										playeratevent.put(player.getUniqueId(), index);
										
									break;
								}
							}
						}
					}
					runevents(time, start, end);
				}
			}, 2*20L);
		}
	}
	
	private void runevents(int time, Location start, Location end){
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ids.addAll(playersatevent.keySet());
		String[] locations = null;
		ArrayList<UUID> players = null;
		for(Integer id : ids){
			if(playersatevent.get(id) == tempplayers.get(start).get(end)){
				
				locations = eventlocations.get(id).get(0).split("=");
				players = playersatevent.get(id);
				Location spawn = new Location(Bukkit.getWorld(locations[3]), Integer.parseInt(locations[0]),Integer.parseInt(locations[1]) , Integer.parseInt(locations[2]));
				for(UUID uuid : players){
					for(Player p : Bukkit.getOnlinePlayers()){
						if(uuid == p.getUniqueId()){
									p.teleport(spawn);
									
						}
					}
				}
				tempplayers.remove(start);
				switch(eventlocations.get(id).get(3)){
				case "ghost":
						eventghost(time, id);
						endlocid.put(id, end);
						break;
				case "ghast":
						eventghast(time, id);
						endlocid.put(id, end);
						break;
				case "wither":
						eventwither(time, id);
						endlocid.put(id, end);
						break;
				}
			}
		}
	}
	
	private Location Bordercalc(int id, int type){
		Location border = null;
		String[] border1 = eventlocations.get(id).get(1).split("=");
		String[] border2 = eventlocations.get(id).get(2).split("=");
		switch (type){
		case 1:
			border = new Location(Bukkit.getWorld(border1[3]), Integer.parseInt(border1[0]), Integer.parseInt(border1[1]), Integer.parseInt(border1[2]));
			break;
		case 2:
			border =  new Location(Bukkit.getWorld(border2[3]), Integer.parseInt(border2[0]), Integer.parseInt(border2[1]), Integer.parseInt(border2[2]));
			break;
		}
		return border;
	}
	
	private void idremover(int id){
		endlocid.remove(id);
		playersatevent.remove(id);
		schedulerborder.remove(id);
		spawners.remove(id);
		
	}
	
	private void entremove(ArrayList<Integer> coords, World world){
			ArrayList<Location> temp = new ArrayList<Location>();
			temp.addAll(entities.keySet());
			for(Location loc : temp){
				int x = (int) loc.getX();
				int y = (int) loc.getY();
				int z = (int) loc.getZ();
				World w  = loc.getWorld();
				if((x >= coords.get(0) || x <= coords.get(1)) && 
					(y >= coords.get(2) || y <= coords.get(3)) &&
					(z >= coords.get(4) || z <= coords.get(5)) && w == world){
					Entity ent = entities.get(loc);
					ent.remove();
					entities.remove(loc);
				}
			}
	}
	
	private Location randomloc(ArrayList<Integer> coords, World world){
		Random r = new Random();
		ArrayList<Integer> cords = coordpositives(coords);
		int randx;
		if(coords.get(0) < 0 && coords.get(1) < 0){
			 randx = -(r.nextInt(cords.get(1) - cords.get(0) +  1) + cords.get(0));
			}else if(coords.get(0) < 0 && coords.get(1) > 0){
			randx = r.nextInt(cords.get(1) - coords.get(0) +  1) - cords.get(1);
			}else{
				 randx = (r.nextInt(cords.get(1) - cords.get(0) +  1) + cords.get(0));
			}
		int randy = coords.get(3);
		int randz;
		if(coords.get(4) < 0 && coords.get(5) < 0){
			 randz = -(r.nextInt(cords.get(5) - cords.get(4) +  1) + cords.get(4));
			}else if(coords.get(4) < 0 && coords.get(5) > 0){
			randz = r.nextInt(cords.get(5) - coords.get(4) +  1) - cords.get(5);
			}else{
				randz = (r.nextInt(cords.get(5) - cords.get(4) +  1) + cords.get(4));
			}
		Location rand = new Location(world, randx, randy, randz);
		return rand;
	}
	private ArrayList<Integer> coordpositives(ArrayList<Integer> coords){
		ArrayList<Integer> cords = new ArrayList<Integer>();
		for(Integer index : coords){
			if(index < 0){
				cords.add(-index);
			}else{
				cords.add(index);
			}
		}
		if(cords.get(0) > cords.get(1)){
			int t = cords.get(1);
			cords.set(1, cords.get(0));
			cords.set(0, t);
		}
		if(cords.get(4) > cords.get(5)){
			int t = cords.get(5);
			cords.set(5, cords.get(4));
			cords.set(4, t);
		}
		return cords;
	}
	private ArrayList<Integer> borderlocations(Location Border1, Location Border2){
		ArrayList<Integer> coords = new ArrayList<Integer>();
		int minx;
		int maxx;
		int miny;
		int maxy;
		int minz;
		int maxz;
		
			if(Border1.getX() < Border2.getX()){
				minx = (int) Border1.getX();
				maxx = (int) Border2.getX();
			}else{
				minx = (int) Border2.getX();
				maxx = (int) Border1.getX();
			}
			if(Border1.getY() < Border2.getY()){
				miny = (int) Border1.getY();
				maxy = (int) Border2.getY();
			}else{
				miny = (int) Border2.getY();
				maxy = (int) Border1.getY();
			}
			if(Border1.getZ() < Border2.getZ()){
				minz = (int) Border1.getZ();
				maxz = (int) Border2.getZ();
			}else{
				minz = (int) Border2.getZ();
				maxz = (int) Border1.getZ();
			}
		
		coords.add(minx);
		coords.add(maxx);
		coords.add(miny);
		coords.add(maxy);
		coords.add(minz);
		coords.add(maxz);
		
		return coords;
 	}
	
	//MAKING OF THE EVENTS
	@EventHandler
	public void onplayerchat(PlayerChatEvent event){
		Player player = event.getPlayer();
		String message = event.getMessage();
		if(tempevent.get(player.getUniqueId()) != null){
			if(tempevent.get(player.getUniqueId()).get(1).equals("dragonloc") && message.equalsIgnoreCase("save")){
				int id = Integer.parseInt(tempevent.get(player.getUniqueId()).get(0));
				ArrayList<String> temp = eventlocations.get(id);
				String loc = Integer.toString((int) player.getLocation().getX()) + "=" + 
							 Integer.toString((int) player.getLocation().getY()) + "=" +
							 Integer.toString((int) player.getLocation().getZ()) + "=" +
							 player.getWorld().getName();
				temp.set(0, loc);
				eventlocations.put(id, temp);
				player.sendMessage("location set");
				tempevent.remove(player.getUniqueId());
				
			}else if(tempevent.get(player.getUniqueId()).get(1).equals("dragonborder1") && message.equalsIgnoreCase("save")){
				int id = Integer.parseInt(tempevent.get(player.getUniqueId()).get(0));
				ArrayList<String> temp = eventlocations.get(id);
				String loc = Integer.toString((int) player.getLocation().getX()) + "=" + 
							 Integer.toString((int) player.getLocation().getY()) + "=" +
							 Integer.toString((int) player.getLocation().getZ()) + "=" +
							 player.getWorld().getName();
				temp.set(1, loc);
				eventlocations.put(id, temp);
				player.sendMessage("location set");
				tempevent.remove(player.getUniqueId());
			}else if(tempevent.get(player.getUniqueId()).get(1).equals("dragonborder2") && message.equalsIgnoreCase("save")){
				int id = Integer.parseInt(tempevent.get(player.getUniqueId()).get(0));
				ArrayList<String> temp = eventlocations.get(id);
				String loc = Integer.toString((int) player.getLocation().getX()) + "=" + 
							 Integer.toString((int) player.getLocation().getY()) + "=" +
							 Integer.toString((int) player.getLocation().getZ()) + "=" +
							 player.getWorld().getName();
				temp.set(2, loc);
				eventlocations.put(id, temp);
				player.sendMessage("location set");
				tempevent.remove(player.getUniqueId());
			}else if(tempevent.get(player.getUniqueId()).get(1).equals("dragontype")){
				if(message.equalsIgnoreCase("pirate") || message.equalsIgnoreCase("ghast") || message.equalsIgnoreCase("blaze") || message.equalsIgnoreCase("titanic")){
					message = message.toLowerCase();
				int id = Integer.parseInt(tempevent.get(player.getUniqueId()).get(0));
				ArrayList<String> temp = eventlocations.get(id);
				temp.set(3, message);
				eventlocations.put(id, temp);
				player.sendMessage("type set");
				tempevent.remove(player.getUniqueId());
				}
			}
		
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void oninventoryClick(InventoryClickEvent event){
		if(event.getInventory().getName().equals("Dragon Create Event") || event.getInventory().getName().equals("All Dragon Events") || event.getInventory().getName().equals("Dragon Edit Event")){
			if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR){
				Player player = (Player) event.getWhoClicked();
				
				if(event.getInventory().getName().equals("Dragon Create Event")){
					if(event.getCurrentItem().getType() == Material.COMPASS){
						String id = event.getInventory().getItem(0).getItemMeta().getDisplayName();
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(id);
						temp.add("dragonloc");
						tempevent.put(player.getUniqueId(), temp);
						player.sendMessage("go to the location and type save");
						event.setCancelled(true);
						player.closeInventory();
					}else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Border 1 Location")){
						String id = event.getInventory().getItem(0).getItemMeta().getDisplayName();
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(id);
						temp.add("dragonborder1");
						tempevent.put(player.getUniqueId(), temp);
						player.sendMessage("go to the location and type save");
						event.setCancelled(true);
						player.closeInventory();
					}else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Border 2 Location")){
						String id = event.getInventory().getItem(0).getItemMeta().getDisplayName();
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(id);
						temp.add("dragonborder2");
						tempevent.put(player.getUniqueId(), temp);
						player.sendMessage("go to the location and type save");
						event.setCancelled(true);
						player.closeInventory();
					}else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Type of Event")){
						String id = event.getInventory().getItem(0).getItemMeta().getDisplayName();
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(id);
						temp.add("dragontype");
						tempevent.put(player.getUniqueId(), temp);
						player.sendMessage("what type of event? ");
						event.setCancelled(true);
						player.closeInventory();
					}
				}else
				
					
				if(event.getInventory().getName().equals("Dragon Edit Event")){
					if(event.getCurrentItem().getType() == Material.COMPASS){
						String id = event.getInventory().getItem(0).getItemMeta().getDisplayName();
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(id);
						temp.add("dragonloc");
						tempevent.put(player.getUniqueId(), temp);
						player.sendMessage("go to the location and type save");
						event.setCancelled(true);
						player.closeInventory();
					}else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Border 1 Location")){
						String id = event.getInventory().getItem(0).getItemMeta().getDisplayName();
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(id);
						temp.add("dragonborder1");
						tempevent.put(player.getUniqueId(), temp);
						player.sendMessage("go to the location and type save");
						event.setCancelled(true);
						player.closeInventory();
					}else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Border 2 Location")){
						String id = event.getInventory().getItem(0).getItemMeta().getDisplayName();
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(id);
						temp.add("dragonborder2");
						tempevent.put(player.getUniqueId(), temp);
						player.sendMessage("go to the location and type save");
						event.setCancelled(true);
						player.closeInventory();
					}else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Type of Event")){
						String id = event.getInventory().getItem(0).getItemMeta().getDisplayName();
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(id);
						temp.add("dragontype");
						tempevent.put(player.getUniqueId(), temp);
						player.sendMessage("what type of event? ");
						event.setCancelled(true);
						player.closeInventory();
					}else 
						
					if(event.getCurrentItem().getType() == Material.BLAZE_ROD){
						int id = Integer.parseInt(event.getInventory().getItem(0).getItemMeta().getDisplayName());
						eventlocations.remove(id);
						playersatevent.remove(id);
						plugin.database.deleteeventdragon(id);
						event.setCancelled(true);
						player.closeInventory();
					}else{
						event.setCancelled(true);
					}
				}else
				if(event.getInventory().getName().equals("All Dragon Events")){
					int id = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
					event.setCancelled(true);
					player.closeInventory();
					eventedit(player, id);
				}
					
				
			}
		}
	}
	
	public void eventCreate(Player player){
		int uuid = 0;
		while(eventlocations.get(uuid) != null){
			uuid++;
		}
		Inventory inventory = Bukkit.createInventory(null, 9, "Dragon Create Event");
		String loc = "0=0=0=" + player.getWorld().getName();
		String border1 = "0=0=0=" + player.getWorld().getName();
		String border2 = "0=0=0=" + player.getWorld().getName();
		String eventtype = "none";
		ArrayList<String> listtemp = new ArrayList<String>();
		listtemp.add(loc);
		listtemp.add(border1);
		listtemp.add(border2);
		listtemp.add(eventtype);
		eventlocations.put(uuid, listtemp);
		playersatevent.put(uuid, null);
		ItemStack id = new ItemStack(Material.DIAMOND);
		{
			ItemMeta temp = id.getItemMeta();
			temp.setDisplayName(Integer.toString(uuid));
			id.setItemMeta(temp);
		}
		
		ItemStack location = new ItemStack(Material.COMPASS);
		{
			ItemMeta temp = location.getItemMeta();
			temp.setDisplayName("Event Location");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("0");
			lore.add("0");
			lore.add("0");
			lore.add(player.getWorld().getName());
			temp.setLore(lore);
			location.setItemMeta(temp);
		}
		ItemStack bordenloc1 = new ItemStack(Material.BRICK);
		{
			ItemMeta temp = bordenloc1.getItemMeta();
			temp.setDisplayName("Border 1 Location");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("0");
			lore.add("0");
			lore.add("0");
			lore.add(player.getWorld().getName());
			temp.setLore(lore);
			bordenloc1.setItemMeta(temp);
		}
		ItemStack bordenloc2 = new ItemStack(Material.BRICK);
		{
			ItemMeta temp = bordenloc1.getItemMeta();
			temp.setDisplayName("Border 2 Location");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("0");
			lore.add("0");
			lore.add("0");
			lore.add(player.getWorld().getName());
			temp.setLore(lore);
			bordenloc2.setItemMeta(temp);
		}
		ItemStack type = new ItemStack(Material.SKULL_ITEM);
		{
			ItemMeta temp = type.getItemMeta();
			temp.setDisplayName("Type of Event");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("none");
			temp.setLore(lore);
			type.setItemMeta(temp);
		}

		inventory.addItem(id);
		inventory.addItem(location);
		inventory.addItem(bordenloc1);
		inventory.addItem(bordenloc2);
		inventory.addItem(type);
		player.openInventory(inventory);
	}
	
	public void eventalledit(Player player){
		int rowcount = 0;
		if(eventlocations != null){
		rowcount = eventlocations.keySet().size();
		if(rowcount % 9 == 0){
			rowcount++;
		}
		while(rowcount % 9 != 0){
			rowcount++;
		}
		}
		if(rowcount == 0){
			rowcount = 9;
		}
		Inventory inventory = Bukkit.createInventory(null, rowcount, "All Dragon Events");
		ArrayList<Integer> idss = new ArrayList<Integer>();
		idss.addAll(eventlocations.keySet());
		for(Integer ids : idss){
			
			ItemStack id = new ItemStack(Material.DIAMOND);
			{
				
				ItemMeta temp = id.getItemMeta();
				temp.setDisplayName(Integer.toString(ids));
				ArrayList<String> lore = new ArrayList<String>();
				String[] location  = eventlocations.get(ids).get(0).split("=");
				lore.add(location[0]);
				lore.add(location[1]);
				lore.add(location[2]);
				lore.add(location[3]);
				temp.setLore(lore);
				id.setItemMeta(temp);
			}
			
		
			inventory.addItem(id);
			
		}
		player.openInventory(inventory);
	}
	
	private void eventedit(Player player, int eventid){
		
		Inventory inventory = Bukkit.createInventory(null, 9, "Dragon Edit Event");
		
		ItemStack id = new ItemStack(Material.DIAMOND);
		{
			ItemMeta temp = id.getItemMeta();
			temp.setDisplayName(Integer.toString(eventid));
			id.setItemMeta(temp);
		}
		
		ItemStack location = new ItemStack(Material.COMPASS);
		{
			ItemMeta temp = location.getItemMeta();
			temp.setDisplayName("Event Location");
			ArrayList<String> lore = new ArrayList<String>();
			String[] loc  = eventlocations.get(eventid).get(0).split("=");
			lore.add(loc[0]);
			lore.add(loc[1]);
			lore.add(loc[2]);
			lore.add(loc[3]);
			temp.setLore(lore);
			location.setItemMeta(temp);
		}
		ItemStack bordenloc1 = new ItemStack(Material.BRICK);
		{
			ItemMeta temp = bordenloc1.getItemMeta();
			temp.setDisplayName("Border 1 Location");
			ArrayList<String> lore = new ArrayList<String>();
			String[] loc  = eventlocations.get(eventid).get(1).split("=");
			lore.add(loc[0]);
			lore.add(loc[1]);
			lore.add(loc[2]);
			lore.add(loc[3]);
			temp.setLore(lore);
			bordenloc1.setItemMeta(temp);
		}
		ItemStack bordenloc2 = new ItemStack(Material.BRICK);
		{
			ItemMeta temp = bordenloc1.getItemMeta();
			temp.setDisplayName("Border 2 Location");
			ArrayList<String> lore = new ArrayList<String>();
			String[] loc  = eventlocations.get(eventid).get(2).split("=");
			lore.add(loc[0]);
			lore.add(loc[1]);
			lore.add(loc[2]);
			lore.add(loc[3]);
			temp.setLore(lore);
			bordenloc2.setItemMeta(temp);
		}
		ItemStack type = new ItemStack(Material.SKULL_ITEM);
		{
			ItemMeta temp = type.getItemMeta();
			temp.setDisplayName("Type of Event");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(eventlocations.get(eventid).get(3));
			temp.setLore(lore);
			type.setItemMeta(temp);
		}
		ItemStack delete = new ItemStack(Material.BLAZE_ROD);
		{
			ItemMeta temp = delete.getItemMeta();
			temp.setDisplayName("delete");
			delete.setItemMeta(temp);
		}
		
		
		inventory.addItem(id);
		inventory.addItem(location);
		inventory.addItem(bordenloc1);
		inventory.addItem(bordenloc2);
		inventory.addItem(type);
		inventory.setItem(8, delete);
		player.openInventory(inventory);
		
	}
	
	//RESTRICTERS
	private void Bordercontrolmonster(final ArrayList<Integer> coords, World world, final int id){
		int ids = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				ArrayList<Location> locations = new ArrayList<Location>();
				locations.addAll(spawners.get(id));
				for(Location loc : locations){
				Entity ent = entities.get(loc);

				if((int) ent.getLocation().getX() <= coords.get(0)){
					 ent.getLocation().setX(coords.get(0) + 1);
				}
				if( (int) ent.getLocation().getX() >= coords.get(1)){
					ent.getLocation().setX(coords.get(1) - 1);
				}
				if( (int) ent.getLocation().getY() >= (coords.get(3) + 5)){
					ent.getLocation().setY(coords.get(3) - 1);
				}
				if((int) ent.getLocation().getZ() <= coords.get(4)){
					 ent.getLocation().setZ(coords.get(4) + 1);
				}
				if( (int) ent.getLocation().getZ() >= coords.get(5)){
					ent.getLocation().setZ(coords.get(5) - 1);
				}
				
			}
			}
		},0,1);
		schedulerborder.put(id, ids);
	}
	
	@EventHandler
	public void onPlayermove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if(playeratevent.get(player.getUniqueId()) != null){
			int id = playeratevent.get(player.getUniqueId());
			ArrayList<Integer> coords = borderlocations(Bordercalc(id, 1), Bordercalc(id, 2));
			if((int) event.getTo().getX() <= coords.get(0)){
				event.setCancelled(true);
			}
			if((int) event.getTo().getX() >= coords.get(1)){
				event.setCancelled(true);
			}

			if((int) event.getTo().getZ() <= coords.get(4)){
				event.setCancelled(true);
			}
			if((int) event.getTo().getZ() >= coords.get(5)){
				event.setCancelled(true);
			}
			
		}
	}

	//just the main shit
	public TravelDragonEvent(MoY instance){
		this.plugin = instance;
	/*String spawn = "-640=5=-1452=anni_map";
	String border1 = "-633=4=-1453=anni_map";
	String border2 = "-652=8=-1434=anni_map";
	String type = "ghost";
	ArrayList<String> temp = new ArrayList<String>();
	temp.add(spawn);
	temp.add(border1);
	temp.add(border2);
	temp.add(type);
	eventlocations.put(0, temp);*/
	}
	
}