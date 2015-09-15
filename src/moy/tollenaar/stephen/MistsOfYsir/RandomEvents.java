package moy.tollenaar.stephen.MistsOfYsir;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import moy.tollenaar.stephen.NPC.NPCEntity;
import moy.tollenaar.stephen.NPC.NPCProfile;
import moy.tollenaar.stephen.NPC.NPCSpawnReason;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;


public class RandomEvents implements Listener {
	private MoY plugin;
	private int scheduler;
	
	private Playerinfo playerinfo;
	
	public  HashMap<UUID, String> playerzombie = new HashMap<UUID, String>();	//temp made for ribesal
	public  HashMap<Entity, Integer> schedulerid = new HashMap<Entity, Integer>();	//scheduler to despawn ribesal
	private  HashMap<Entity, Integer> zomb = new HashMap<Entity, Integer>();	//needed for the scheduler id for the target
	private ArrayList<UUID> playerom = new ArrayList<UUID>();	//set to check if player is killing ribesal
	
	private HashMap<UUID, HashMap<Location, ItemStack[]>> playerdeathinv = new HashMap<UUID, HashMap<Location, ItemStack[]>>(); //if player died by ribesal
	private HashMap<UUID, ItemStack[]> playerarmor = new HashMap<UUID, ItemStack[]>(); //if player died by ribesal
	
	public void cancelsced(){
		List<Entity> keys  = new ArrayList<Entity>();
		keys.addAll(schedulerid.keySet());
		for(Entity uuid : keys){
			int id = schedulerid.get(uuid);
			
			uuid.remove();
			Bukkit.getScheduler().cancelTask(id);
		}
		Bukkit.getScheduler().cancelTask(scheduler);
		
	}
	
	@EventHandler
	public void onplayerespawn(PlayerRespawnEvent event){
		 Player player = event.getPlayer();
		if(playerdeathinv.containsKey(player.getUniqueId())){
			ArrayList<Location> temp = new ArrayList<Location>();
			temp.addAll(playerdeathinv.get(player.getUniqueId()).keySet());
			 Location loc = temp.get(0);
			ItemStack[] inv = playerdeathinv.get(player.getUniqueId()).get(loc);
			
			for(ItemStack item : inv){
				if(item == null){
					item = new ItemStack(Material.AIR);
				}
				player.getInventory().addItem(item);
			}
			player.getInventory().setArmorContents(playerarmor.get(player.getUniqueId()));
			playerdeathinv.remove(player.getUniqueId());
			playerarmor.remove(player.getUniqueId());
			player.teleport(loc);
			
		}
	}
	
	
	@EventHandler
	public void onplayerdeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		EntityDamageEvent de = player.getLastDamageCause();
		if(de instanceof EntityDamageByEntityEvent){
			Entity damager = ((EntityDamageByEntityEvent) de).getDamager();
			if(damager instanceof Zombie){
				Monster ent = (Monster) damager;
				if(playerom.contains(player.getUniqueId()) && ent.getCustomName().contains("Ribesal")){
					event.setDroppedExp(0);
					HashMap<Location, ItemStack[]> temp = new HashMap<Location, ItemStack[]>();
					temp.put(player.getLocation(), player.getInventory().getContents());
					playerdeathinv.put(player.getUniqueId(), temp);
					playerarmor.put(player.getUniqueId(), player.getInventory().getArmorContents());
					event.getDrops().clear();
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.RED + "Ribesal" + ChatColor.DARK_PURPLE + "]" + ChatColor.GREEN + " You have been annihilated.");
					ent.remove();
				}
			}
		}
	}
	
	public void playerevent(){
		int minutes = Calendar.getInstance().get(Calendar.MINUTE);
	int seconds = Calendar.getInstance().get(Calendar.SECOND);
	int nearest = ((minutes + 30)/30)*30 %60;
		int next;
		if(nearest == 0 && minutes >= 45){
		next = 60 - minutes;
			}else{
		next = nearest - minutes;
				}
			if(next < 0){
		next = -next;
				}
				seconds = (60 - seconds) + next*60;
		scheduler = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			
			@Override
			public void run(){
				ArrayList<Player> tempplayers = new ArrayList<Player>();
				for(Player players: Bukkit.getOnlinePlayers()){
					Playerstats p = playerinfo.getplayer(players.getUniqueId());
				if(chance() == true && p.getLevel() >= 10 && players.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
						tempplayers.add(players);
						
						}
				}
				
				runevent(tempplayers);
				playerevent();
			}
		}, seconds*20L);
	}
	
	private void runevent(ArrayList<Player> players){
		for( Player pl: players){
			Random r = new Random();
			int index = r.nextInt(3);
			String type = null;
			switch(index){
			case 0:
				type = "zombie";
				break;
			case 1:
				type = "npcitem";
				break;
			case 2:
				type = "skill";
				break;
			case 3:
				type = "choice";
				break;
			}
			final Player player = pl;
			if(type.equals("zombie")){
				playerzombie.put(player.getUniqueId(), "Ribesal");
				Location loc = player.getLocation().toVector().add(player.getLocation().getDirection().multiply(3)).toLocation(player.getWorld());
				loc.setY(loc.getY() + 1);
				Zombie zomb = (Zombie) player.getWorld().spawnEntity(loc, EntityType.ZOMBIE); 
				zomb.setCustomName("Ribesal");
				zomb.setCustomNameVisible(true);
				player.getWorld().playSound(player.getLocation(), Sound.GHAST_SCREAM, 15, 1);
				player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 10);
				player.sendMessage(ChatColor.DARK_PURPLE
								+ "["
								+ ChatColor.GOLD
								+ "Ribesal"
								+ ChatColor.DARK_PURPLE
								+ "]"
								+ ChatColor.GREEN
								+ " Prepare to be annihilated.");
				zombietarget(zomb, player);
				zombdespawner(zomb);
				playerom.add(player.getUniqueId());
				}else if(type.equals("npcitem")){
					@SuppressWarnings("unchecked")
					List<Integer> idlist = (List<Integer>) plugin.getConfig().getList("randomeventrewardid");
					
					Location loc = player.getLocation().toVector().add(player.getLocation().getDirection().multiply(3)).toLocation(player.getWorld());
					final NPCEntity npc = new NPCEntity(loc.getWorld(), loc, new NPCProfile("Antee"), plugin.getNetwork(), plugin, "normal", -1);
					npc.spawn(loc, NPCSpawnReason.NORMAL_SPAWN, npc);
					loc.setY(loc.getY() + 1);
					final Location loc2 = loc;
					npc.lookatEntity(player);
					player.getWorld().playSound(loc, Sound.EXPLODE, 10, 1);
					player.getWorld().playEffect(loc2, Effect.EXPLOSION_HUGE, 10);
					Random f = new Random();
					int d = f.nextInt(idlist.size() -1);
					@SuppressWarnings("deprecation")
					ItemStack it = new ItemStack(Material.getMaterial(idlist.get(d)));
					player.getInventory().addItem(it);
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + npc.getName() + ChatColor.DARK_PURPLE + "]" + ChatColor.GREEN + " You have been a good boy. Enjoy this.");
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
						@Override
						public void run(){
							player.getWorld().playEffect(loc2, Effect.EXPLOSION_HUGE, 10);
							player.getWorld().playSound(loc2, Sound.EXPLODE, 10, 1);
							npc.despawn(NPCSpawnReason.DESPAWN);
						}
					}, 60);
				}else if(type.equals("skill")){
					skillinv(player);
				}else if(type.equals("choice")){
					choiceinv(player);
				}
		}
	}
	
	public void zombdespawner(final Zombie ent){
		int id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){
				Bukkit.getScheduler().cancelTask(zomb.get(ent));
				ent.remove();
			}
		}, 60*20L);
		schedulerid.put(ent, id);

	}
	public void zombietarget(final Zombie ent, final Player player){
		int id =Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				ent.getTarget();
				ent.setTarget(player);
				ent.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20, 1), true);
			}
		}, 1, 1);
		zomb.put(ent, id);
	}
	
	
	private void choiceinv(Player player){
		Inventory choice = Bukkit.createInventory(null, 9, "Secret Chest");
		
		ItemStack choice1 = new ItemStack(Material.CHEST);
		{
			ItemMeta temp = choice1.getItemMeta();
			temp.setDisplayName("Choice 1");
			choice1.setItemMeta(temp);
		}
		ItemStack choice2 = new ItemStack(Material.CHEST);
		{
			ItemMeta temp = choice2.getItemMeta();
			temp.setDisplayName("Choice 2");
			choice2.setItemMeta(temp);
		}
		ItemStack choice3 = new ItemStack(Material.CHEST);
		{
			ItemMeta temp = choice3.getItemMeta();
			temp.setDisplayName("Choice 3");
			choice3.setItemMeta(temp);
		}
		ItemStack choice4 = new ItemStack(Material.CHEST);
		{
			ItemMeta temp = choice4.getItemMeta();
			temp.setDisplayName("Choice 4");
			choice4.setItemMeta(temp);
		}
		choice.setItem(1, choice1);
		choice.setItem(3, choice2);
		choice.setItem(5, choice3);
		choice.setItem(7, choice4);
		player.openInventory(choice);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void oninvclick(InventoryClickEvent event){
		if(event.getInventory().getName().equals("Secret Chest")){
			if(event.getCurrentItem() != null){
			Player player = (Player) event.getWhoClicked();
			
			Random r = new Random();
			int index = r.nextInt(3);
			event.setCancelled(true);
			player.closeInventory();
			switch(index){
			case 0:
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "Genie" + ChatColor.DARK_PURPLE + "]" + ChatColor.GREEN + " Here is your gold.");
					player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 34));
					break;
			case 1:
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "Genie" + ChatColor.DARK_PURPLE + "]" + ChatColor.GREEN + " Here is your bread.");
					player.getInventory().addItem(new ItemStack(Material.BREAD, 25));
					break;
			case 2:
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "Genie" + ChatColor.DARK_PURPLE + "]" + ChatColor.GREEN + " Better luck next time.");
					break;
			case 3:
				playerzombie.put(player.getUniqueId(), "Ribesal");
				Location loc = player.getLocation().toVector().add(player.getLocation().getDirection().multiply(3)).toLocation(player.getWorld());
				loc.setY(loc.getY() + 1);
				Zombie zomb = (Zombie) player.getWorld().spawnEntity(loc, EntityType.ZOMBIE); 
				zomb.setCustomName("Ribesal");
				zomb.setCustomNameVisible(true);
				player.getWorld().playSound(player.getLocation(), Sound.GHAST_SCREAM, 15, 1);
				player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 10);
				player.sendMessage(ChatColor.DARK_PURPLE
								+ "["
								+ ChatColor.GOLD
								+ "Ribesal"
								+ ChatColor.DARK_PURPLE
								+ "]"
								+ ChatColor.GREEN
								+ " Prepare to be annihilated.");
				zombietarget(zomb, player);
				zombdespawner(zomb);
				playerom.add(player.getUniqueId());
				break;
			}
		
			}
		}else if(event.getInventory().getName().equals("Dusty Lamp")){
			
			if(event.getCurrentItem() != null){
				Player player = (Player) event.getWhoClicked();
				Playerstats p = playerinfo.getplayer(player.getUniqueId());
				switch (event.getCurrentItem().getItemMeta().getDisplayName()){
				case "Woodcutting Lvl":
					p.setWoodcutting(p.getWoodcutting()+1);
					event.setCancelled(true);
					player.closeInventory();
					break;
				case "Mining Lvl":
					p.setMining(p.getMining()+1);
					event.setCancelled(true);
					player.closeInventory();
					break;
				case "Smelting Lvl":
					p.setSmelting(p.getSmelting()+1);
					event.setCancelled(true);
					player.closeInventory();
					break;
				case "Cooking Lvl":
					p.setCooking(p.getCooking()+1);
					event.setCancelled(true);
					player.closeInventory();
					break;
				case "Fishing Lvl":
					p.setFishing(p.getFishing()+1);
					event.setCancelled(true);
					player.closeInventory();
					break;
				}
				playerinfo.saveplayerdata(p);
				
			}
		}
	}
	
	private void skillinv(Player player){
		Inventory skill = Bukkit.createInventory(null, 9, "Dusty Lamp");
		Playerstats p = playerinfo.getplayer(player.getUniqueId());
		ItemStack woodcutting = new ItemStack(Material.DIAMOND_AXE);
		{
			ArrayList<String> temp = new ArrayList<String>();
			temp.add("Lvl: " +Integer.toString(p.getWoodcutting()));
			ItemMeta t  = woodcutting.getItemMeta();
			t.setDisplayName("Woodcutting Lvl");
			t.setLore(temp);
			woodcutting.setItemMeta(t);
		}
		
		ItemStack mining = new ItemStack(Material.DIAMOND_PICKAXE);
		{
			ArrayList<String> temp = new ArrayList<String>();
			temp.add("Lvl: " +Integer.toString(p.getMining()));
			ItemMeta t = mining.getItemMeta();
			t.setDisplayName("Mining Lvl");
			t.setLore(temp);
			mining.setItemMeta(t);
		}
		
		ItemStack smelting = new ItemStack(Material.IRON_INGOT);
		{
			ArrayList<String> temp = new ArrayList<String>();
			temp.add("Lvl: " +Integer.toString(p.getSmelting()));
			ItemMeta t = smelting.getItemMeta();
			t.setDisplayName("Smelting Lvl");
			t.setLore(temp);
			smelting.setItemMeta(t);
		}
		
		ItemStack cooking = new ItemStack(Material.COOKED_BEEF);
		{
			ArrayList<String> temp = new ArrayList<String>();
			temp.add("Lvl: " +Integer.toString(p.getCooking()));
			ItemMeta t = cooking.getItemMeta();
			t.setDisplayName("Cooking Lvl");
			t.setLore(temp);
			cooking.setItemMeta(t);
		}
		
		ItemStack fishing = new ItemStack(Material.FISHING_ROD);
		{
			ArrayList<String> temp = new ArrayList<String>();
			temp.add("Lvl: " +Integer.toString(p.getFishing()));
			ItemMeta t = fishing.getItemMeta();
			t.setDisplayName("Fishing Lvl");
			t.setLore(temp);
			fishing.setItemMeta(t);
		}
		
		skill.setItem(5, woodcutting);
		skill.setItem(4, mining);
		skill.setItem(2, smelting);
		skill.setItem(1, cooking);
		skill.setItem(7, fishing);
		
		player.openInventory(skill);
	}
	
	private boolean chance(){
		Random r = new Random();
		int k = r.nextInt(100);
		if(k >= 89){
			return true;
		}else{
			return false;
		}
	}
	
	public RandomEvents(MoY instance){
		this.plugin = instance;
		this.playerinfo = instance.playerinfo;
	}
}
