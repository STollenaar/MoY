package MoY.tollenaar.stephen.MistsOfYsir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import MoY.tollenaar.stephen.PlayerInfo.Playerinfo;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.Travel.TravelBoatEvent;
import MoY.tollenaar.stephen.Travel.TravelCartEvent;
import MoY.tollenaar.stephen.Travel.TravelDragonEvent;

public class MobSpawns implements Listener {
	private MoY plugin;
	private Playerinfo playerinfo;
	private TravelBoatEvent boat;
	private TravelCartEvent cart;
	private TravelDragonEvent dragon;
	private RandomEvents rand;
	public static HashMap<UUID, UUID> zombuuidtoplayeruuid = new HashMap<UUID, UUID>();

	@EventHandler
	public void onmobspawn(EntitySpawnEvent event) {
		if(event.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
		Entity ent = event.getEntity();
		//I just want to know if it is a zombie or skeleton
		if (ent instanceof Monster) {
			//this is for the default monster spawn or the event spawn
				if(((Monster) ent).getCustomName() == null){
					Location location = event.getLocation();
					double radiusSquared = 70 * 70;
					for(Player player : plugin.getServer().getOnlinePlayers()){
					Playerstats p = playerinfo.getplayer(player.getUniqueId());
					if (player.getWorld().getName()
								.equals(location.getWorld().getName())) {
							if (player.getLocation().distanceSquared(
									location) <= radiusSquared) {
								int playerlvl = p.getLevel();
								
									int lvl;
									if(!ent.hasMetadata("protect")){
										lvl = (int) Math.ceil((playerlvl+.0)/10);
									}else{
										lvl = ((int[])ent.getMetadata("protect").get(0).value())[0];
									}
									if (ent instanceof Zombie) {
										zombieitems(ent, lvl);
										break;
									} else if (ent instanceof Skeleton) {
										skeletonitems(ent, lvl);
										break;
									}

								}
						}
					}
					}else if(((Monster) ent).getCustomName().equals("Ribesal")){
						int lvl = 10;
						zombieitems(ent, lvl);
					}else if(((Monster) ent).getCustomName().equals("skel")){
						int lvl  = 4;
						((Monster) ent).setCustomName(null);
						skeletonitems(ent, lvl);
					}else if(((Monster) ent).getCustomName().equals("zom")){
						int lvl = 4;
						((Monster) ent).setCustomName(null);
						zombieitems(ent, lvl);
					}else if(((Monster) ent).getCustomName().equals("skel dr")){
						int lvl  = 7;
						((Monster) ent).setCustomName(null);
						skeletonitems(ent, lvl);
					}else if(((Monster) ent).getCustomName().equals("zom dr")){
						int lvl = 7;
						((Monster) ent).setCustomName(null);
						zombieitems(ent, lvl);
					}
			}
		}
	}

	
	@EventHandler
	public void spiderdodging(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player && event.getEntity() instanceof CaveSpider){
			Player player = (Player) event.getDamager();
			Playerstats p = playerinfo.getplayer(player.getUniqueId());
			CaveSpider cave = (CaveSpider) event.getEntity();
			int lvl;
			if(!cave.hasMetadata("protect")){
				lvl = (int) Math.ceil(p.getLevel()/10.0);
			}else{
				lvl = cave.getMetadata("protect").get(0).asInt();
			}
			if(lvl >= 9){
				int constance = chance(lvl);
				Random r = new Random();
				int x = r.nextInt(100);
				if(x <= constance){
					event.setCancelled(true);
					Vector veco;
					String direct = getCardinalDirection(cave);
					if(direct.equals("N") || direct.equals("S")){
						int d = r.nextInt(1);
						if(d == 0){
							veco = new Vector(1.97, 0, 0);
						}else{
								veco = new Vector(-1.97, 0, 0);
						}
					}else if(direct.equals("E") || direct.equals("W")){
						int d = r.nextInt(1);
						if(d == 0){
							veco = new Vector(0, 0, 1.97);
						}else{
							
						veco = new Vector(0, 0, -1.97);
						}
					}else{
						if(r.nextInt(1) == 1){
						int d = r.nextInt(3);
						if(d == 0){
							veco = new Vector(1.20, 0, 0.67);
						}else if(d == 1){
							veco = new Vector(-1.20, 0, 0.67);
						}else if(d == 2){
							veco = new Vector(-1.20, 0, -0.67);
						}else{
							veco = new Vector(1.20, 0, -0.67);
						}
						}else{
							int d = r.nextInt(3);
							if(d == 0){
								veco = new Vector(0.67, 0, 1.20);
							}else if(d == 1){
								veco = new Vector(-0.67, 0, 1.20);
							}else if(d == 2){
								veco = new Vector(-0.67, 0, -1.20);
							}else{
								veco = new Vector(0.67, 0, -1.20);
							}
						}
					}
					cave.setVelocity(veco);
				}
			}
		}
	}
	
	
	@EventHandler
	public void witchdead(EntityDeathEvent event){
		if(event.getEntity().getType() == EntityType.WITCH){
			
			for(Entity ent : event.getEntity().getNearbyEntities(3.65, 3.65, 3.65)){
				if(ent instanceof Player){
					Playerstats p = playerinfo.getplayer(((Player)ent).getUniqueId());

					if(!event.getEntity().hasMetadata("protect")){
					witchdamage((Player) ent, (int) Math.ceil((p.getLevel()+.0)/10));
					}else{
					witchdamage((Player) ent, event.getEntity().getMetadata("protect").get(0).asInt());
					}
				}
			}
			
			List<Location> locs = buildCircle(event.getEntity().getLocation(), 4);
			for(Location loc : locs){
				loc.getWorld().playEffect(loc, Effect.POTION_BREAK, 4);
			}
		}
	}
	
	private void zombieitems(Entity ent, int lvl) {
		Zombie zombie = (Zombie) ent;
		EntityEquipment eq = zombie.getEquipment();
		eq.clear();
		if (lvl != 1) {
			ItemStack sword;
			switch (lvl) {
			case 2:
				eq.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
				break;
			case 3:
				eq.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
				break;
			case 4:
				eq.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
				eq.setBoots(new ItemStack(Material.LEATHER_BOOTS));
				eq.setHelmet(new ItemStack(Material.LEATHER_HELMET));
				eq.setItemInHand(new ItemStack(Material.WOOD_SWORD));
				break;
			case 5:
				eq.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
				eq.setBoots(new ItemStack(Material.LEATHER_BOOTS));
				eq.setHelmet(new ItemStack(Material.LEATHER_HELMET));
				eq.setItemInHand(new ItemStack(Material.WOOD_SWORD));
				break;
			case 6:
				eq.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
				eq.setBoots(new ItemStack(Material.LEATHER_BOOTS));
				eq.setHelmet(new ItemStack(Material.LEATHER_HELMET));
				sword = new ItemStack(Material.WOOD_SWORD);
				sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				eq.setItemInHand(sword);
				break;
			case 7:
				eq.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
				eq.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
				eq.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
				sword = new ItemStack(Material.STONE_SWORD);
				eq.setItemInHand(sword);
				break;
			case 8:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
				eq.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
				eq.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
				sword = new ItemStack(Material.STONE_SWORD);
				sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				eq.setItemInHand(sword);
				break;
			case 9:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
				eq.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
				sword = new ItemStack(Material.STONE_SWORD);
				sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				eq.setItemInHand(sword);
				break;
			case 10:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword = new ItemStack(Material.STONE_SWORD);
				sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				eq.setItemInHand(sword);
				break;
			case 11:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword = new ItemStack(Material.STONE_SWORD);
				sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
				eq.setItemInHand(sword);
				break;
			case 12:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword = new ItemStack(Material.STONE_SWORD);
				sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
				sword.addEnchantment(Enchantment.KNOCKBACK, 1);
				eq.setItemInHand(sword);
				break;
			case 13:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword = new ItemStack(Material.STONE_SWORD);
				sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
				sword.addEnchantment(Enchantment.KNOCKBACK, 2);
				eq.setItemInHand(sword);
				break;
			case 14:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword = new ItemStack(Material.STONE_SWORD);
				sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
				sword.addEnchantment(Enchantment.KNOCKBACK, 2);
				eq.setItemInHand(sword);
				break;
			}
		}
	}

	private void skeletonitems(Entity ent, int lvl) {
		Skeleton skel = (Skeleton) ent;
		EntityEquipment eq = skel.getEquipment();
		eq.clear();
		if (lvl != 1) {
			ItemStack sword = new ItemStack(Material.BOW);
			switch (lvl) {
			case 2:
				eq.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));

				eq.setItemInHand(sword);
				break;
			case 3:
				eq.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));

				eq.setItemInHand(sword);
				break;
			case 4:
				eq.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
				eq.setBoots(new ItemStack(Material.LEATHER_BOOTS));
				eq.setHelmet(new ItemStack(Material.LEATHER_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
				eq.setItemInHand(sword);
				break;
			case 5:
				eq.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
				eq.setBoots(new ItemStack(Material.LEATHER_BOOTS));
				eq.setHelmet(new ItemStack(Material.LEATHER_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);

				eq.setItemInHand(sword);
				break;
			case 6:
				eq.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
				eq.setBoots(new ItemStack(Material.LEATHER_BOOTS));
				eq.setHelmet(new ItemStack(Material.LEATHER_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);

				eq.setItemInHand(sword);
				break;
			case 7:
				eq.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
				eq.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
				eq.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
				sword.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
				eq.setItemInHand(sword);
				break;
			case 8:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
				eq.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
				eq.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
				sword.addEnchantment(Enchantment.ARROW_DAMAGE, 2);

				eq.setItemInHand(sword);
				break;
			case 9:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
				eq.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
				sword.addEnchantment(Enchantment.ARROW_DAMAGE, 2);

				eq.setItemInHand(sword);
				break;
			case 10:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
				sword.addEnchantment(Enchantment.ARROW_DAMAGE, 2);

				eq.setItemInHand(sword);
				break;
			case 11:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
				sword.addEnchantment(Enchantment.ARROW_DAMAGE, 3);

				eq.setItemInHand(sword);
				break;
			case 12:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
				sword.addEnchantment(Enchantment.ARROW_DAMAGE, 3);

				eq.setItemInHand(sword);
				break;
			case 13:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
				sword.addEnchantment(Enchantment.ARROW_DAMAGE, 3);

				eq.setItemInHand(sword);
				break;
			case 14:
				eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				eq.setBoots(new ItemStack(Material.IRON_BOOTS));
				eq.setHelmet(new ItemStack(Material.IRON_HELMET));
				sword.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
				sword.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
				sword.addEnchantment(Enchantment.ARROW_FIRE, 1);
				eq.setItemInHand(sword);
				break;
			}
		} else {
			eq.setItemInHand(new ItemStack(Material.BOW));
		}
	}

	public ChatColor mobdifficulty(int playerlvl) {
		ChatColor test = ChatColor.WHITE;
		if (playerlvl <= 1) {
			test = ChatColor.GREEN;
		} else if (playerlvl <= 2) {
			test = ChatColor.DARK_GREEN;
		} else if (playerlvl <= 3) {
			test = ChatColor.AQUA;
		} else if (playerlvl <= 4) {
			test = ChatColor.DARK_AQUA;
		} else if (playerlvl <= 5) {
			test = ChatColor.BLUE;
		} else if (playerlvl <= 6) {
			test = ChatColor.DARK_BLUE;
		} else if (playerlvl <= 7) {
			test = ChatColor.LIGHT_PURPLE;
		} else if (playerlvl <= 8) {
			test = ChatColor.DARK_PURPLE;
		} else if (playerlvl <= 9) {
			test = ChatColor.YELLOW;
		} else if (playerlvl <= 10) {
			test = ChatColor.GOLD;
		} else if (playerlvl <= 11) {
			test = ChatColor.RED;
		} else if (playerlvl > 12) {
			test = ChatColor.DARK_RED;
		}
		return test;
	}

	@EventHandler
	//This is only for basic mobs attacking the players. else is the random event where player strikes the wrong mob.
	public void mobattack(EntityDamageByEntityEvent event) {
		if ((event.getDamager() instanceof Monster || event.getDamager() instanceof Projectile || event.getDamager() instanceof Slime)
				&& event.getEntity() instanceof Player) {
			Monster monster = null;
			Ghast ghast = null;
			Slime slime = null;
			MagmaCube cube = null;
			boolean pass = false;
			
			if (event.getDamager() instanceof Projectile) {
				Projectile p = (Projectile) event.getDamager();
				if(p.getShooter() instanceof Ghast){
					ghast = (Ghast) p.getShooter();
					pass = true;
				}
			}else if (event.getDamager() instanceof Monster){
				monster = (Monster) event.getDamager();
				pass = true;
			}else if(event.getDamager() instanceof MagmaCube){
				cube = (MagmaCube) event.getDamager();
				pass = true;
			}else if(event.getDamager() instanceof Slime){
				slime = (Slime) event.getDamager();
				pass = true;
			}
			
			if (pass == true) {
				Playerstats p = playerinfo.getplayer(((Player) event.getEntity()).getUniqueId());
				if (monster instanceof CaveSpider) {
					if(!monster.hasMetadata("protect")){
					cavedamage((Player) event.getEntity(), (int) Math.ceil(p.getLevel()+.0)/10);
					}else{
						
					cavedamage((Player) event.getEntity(),  ((int[])monster.getMetadata("protect").get(0).value())[0]);
					}
				} else if (monster instanceof Spider
						|| slime != null) {
					if(!monster.hasMetadata("protect")){
					slimspiderdamage((Player) event.getEntity(), (int) Math.ceil((p.getLevel()+.0)/10));
					}else{
					slimspiderdamage((Player) event.getEntity(), ((int[])monster.getMetadata("protect").get(0).value())[0]);
					}
				} else if (cube != null) {
					if(!monster.hasMetadata("protect")){
					magmacubedamage((Player) event.getEntity(), (int) Math.ceil((p.getLevel()+.0)/10));
					}else{
					magmacubedamage((Player) event.getEntity(), ((int[])monster.getMetadata("protect").get(0).value())[0]);
					}
				} else if (ghast != null) {
					ghastdamage((Player) event.getEntity());
				}
			}
		} else if ((event.getDamager() instanceof Player || event.getCause() == DamageCause.PROJECTILE
				&& event.getDamager() instanceof Arrow)
				&& event.getEntity() instanceof Monster) {
			Monster monster = (Monster) event.getEntity();
			if (monster.getCustomName() != null) {
				Player player = null;
				boolean pass = false;
				if (event.getCause() == DamageCause.PROJECTILE) {
					Arrow arrow = (Arrow) event.getDamager();
					if (arrow.getShooter() instanceof Player) {
						player = (Player) arrow.getShooter();
						pass = true;
					}
				} else {
					player = (Player) event.getDamager();
					pass = true;
				}
				if (pass == true) {
					String[] par = monster.getCustomName().split(" ");
					par = par[0].split("[0-9]");
					if (par.length == 2) {
						String name = par[1];

						if (name.equals("Ribesal")
								&& rand.playerzombie.get(player.getUniqueId()) == null) {
							player.sendMessage(ChatColor.DARK_PURPLE
									+ "["
									+ ChatColor.GOLD
									+ name
									+ ChatColor.DARK_PURPLE
									+ "]"
									+ ChatColor.GREEN
									+ " You are not the victem. Don't bother me.");
							event.setCancelled(true);
						}
					}
				}
			
		}
	}
	}

	private void cavedamage(Player player, int lvl) {
		
		PotionEffect pot = new PotionEffect(PotionEffectType.POISON, 0, 0);
		Collection<PotionEffect> ac = player.getActivePotionEffects();
		for (PotionEffect t : ac) {
			if (t.getType().getName().equals(PotionEffectType.POISON.getName())) {
				pot = t;
				break;
			}
		}


		int duration = 0;

		switch (lvl) {
		case 2:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 4*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 0));
			break;
		case 3:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 0));
			break;
		case 4:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 0));
			break;
		case 5:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 3*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 3*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));

			break;
		case 6:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 4*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));
			break;
		case 7:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 6*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 6*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));
			break;
		case 8:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));
			break;
		case 9:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));
			break;
		case 10:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));
			break;
		case 11:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));
			break;
		case 12:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));
			break;
		case 13:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));
			break;
		case 14:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 1));
			break;
		}
	}

	private void slimspiderdamage(Player player, int lvl) {
		PotionEffect pot = null;
		PotionEffect pot2 = null;
		Collection<PotionEffect> ac = player.getActivePotionEffects();
		for (PotionEffect t : ac) {
			if (t.getType().getName().equals(PotionEffectType.SLOW.getName())) {
				pot = t;
			}
			if (t.getType().getName().equals(PotionEffectType.POISON.getName())) {
				pot2 = t;
			}
		}
		if (pot == null) {
			pot = new PotionEffect(PotionEffectType.SLOW, 0, 0);
		}
		if (pot2 == null) {
			pot2 = new PotionEffect(PotionEffectType.POISON, 0, 0);
		}
		int duration;
		switch (lvl) {
		case 2:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 4*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 0));
			break;
		case 3:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 0));
			break;
		case 4:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 0));
			break;
		case 5:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 3*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			break;
		case 6:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 4*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			break;
		case 7:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 6*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 6*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			break;
		case 8:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			break;
		case 9:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			// second effect is posion:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot2.getDuration() + 2*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 2*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 0));
			break;
		case 10:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			// second effect is posion:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot2.getDuration() + 3*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 3*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 0));
			break;
		case 11:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			// second effect is posion:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot2.getDuration() + 4*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 0));
			break;
		case 12:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			// second effect is posion:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot2.getDuration() + 4*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 0));
			break;
		case 13:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			// second effect is posion:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot2.getDuration() + 4*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 0));
			break;
		case 14:
			player.removePotionEffect(PotionEffectType.SLOW);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
					duration, 1));
			// second effect is posion:
			player.removePotionEffect(PotionEffectType.POISON);
			if (pot2.getDuration() + 4*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					duration, 0));
			break;
		}
	}

	private void magmacubedamage(Player player, int lvl) {
		PotionEffect pot = null;
		Collection<PotionEffect> ac = player.getActivePotionEffects();
		for (PotionEffect t : ac) {
			if (t.getType().getName().equals(PotionEffectType.WEAKNESS.getName())) {
				pot = t;
				break;
			}
		}
		if (pot == null) {
			pot = new PotionEffect(PotionEffectType.WEAKNESS, 0, 0);
		}
		int duration;
		switch (lvl) {
		case 2:
			player.setFireTicks(2*20);
			break;
		case 3:
			player.setFireTicks(4*20);
			break;
		case 4:
			player.setFireTicks(6*20);
			break;
		case 5: // START WEAKNESS POTS
			player.setFireTicks(1*20);
			player.removePotionEffect(PotionEffectType.WEAKNESS);
			if (pot.getDuration() + 2*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 2*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		case 6:
			player.setFireTicks(4*20);
			if (pot.getDuration() + 4*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 4*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		case 7:
			player.setFireTicks(6*20);
			if (pot.getDuration() + 6*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 6*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		case 8:
			player.setFireTicks(7*20);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		case 9:
			player.setFireTicks(7*20);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		case 10:
			player.setFireTicks(7*20);
			if (pot.getDuration() + 10*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 10*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		case 11:
			player.setFireTicks(7*20);
			if (pot.getDuration() + 10*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 10*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		case 12:
			player.setFireTicks(7*20);
			if (pot.getDuration() + 10*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 10*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		case 13:
			player.setFireTicks(7*20);
			if (pot.getDuration() + 10*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 10*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		case 14:
			player.setFireTicks(7*20);
			if (pot.getDuration() + 10*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 10*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					duration, 0));
			break;
		}
	}

	private void witchdamage(Player player, int lvl){
		switch (lvl){
		case 2:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 2*20, 1));
			break;
		case 3:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4*20, 1));
			break;
		case 4:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5*20, 1));
			break;
		case 5:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 6*20, 1));
			break;
		case 6:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 7*20, 1));
			break;
		case 7:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 8*20, 1));
			break;
		case 9:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 2*20, 2));
			break;
		case 10:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4*20, 2));
			break;
		case 11:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5*20, 2));
			break;
		case 12:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 6*20, 2));
			break;
		case 13:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 7*20, 2));
			break;
		case 14:
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 8*20, 2));
			break;
		}
	}
	
	private void ghastdamage(Player player) {
		Playerstats p = playerinfo.getplayer(player.getUniqueId());
		int lvl = (int) Math.ceil((p.getLevel()+.0)/10);
		if(boat.playeratevent.get(player.getUniqueId()) != null){
			lvl = 5;
		}else if(dragon.playeratevent.get(player.getUniqueId()) != null){
			lvl = 7;
		}
		PotionEffect pot = null;
		Collection<PotionEffect> ac = player.getActivePotionEffects();
		for (PotionEffect t : ac) {
			if (t.getType().getName().equals(PotionEffectType.BLINDNESS.getName())) {
				pot = t;
				break;
			}
		}
		if (pot == null) {
			pot = new PotionEffect(PotionEffectType.BLINDNESS, 0, 0);
		}
		int duration;
		switch (lvl) {
		case 1:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 2*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 2*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			break;
		case 3:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 5*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 5*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			break;
		case 4:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			break;
		case 5:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 9*20 > 18*20) {
				duration = 18;
			} else {
				duration = pot.getDuration() + 9*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			break;
		case 6:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			player.removePotionEffect(PotionEffectType.CONFUSION);
			if (pot.getDuration() + 3*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 3*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					duration, 0));
			break;
		case 7:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 5*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 5*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					duration, 0));
			break;
		case 8:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					duration, 0));
			break;
		case 9:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					duration, 0));
			break;
		case 10:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					duration, 0));
			break;
		case 11:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					duration, 0));
			break;
		case 12:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					duration, 0));
			break;
		case 13:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					duration, 0));
			break;
		case 14:
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			if (pot.getDuration() + 7*20 > 18*20) {
				duration = 18*20;
			} else {
				duration = pot.getDuration() + 7*20;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					duration, 0));
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					duration, 0));
			break;
		}
	}

	private int chance(int lvl){
		if(lvl == 9){
			return 39;
		}else if(lvl == 10){
			return 48;
		}else{
			return 100;
		}
	}
	
    private List<Location> buildCircle (Location loc, int r){
    	boolean sphere = false;
    	boolean hollow  = false;
    	int plus_y = 1;
    	int h = 1;
        List<Location> circleblocks = new ArrayList<Location>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx +r; x++)
            for (int z = cz - r; z <= cz +r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r*r && !(hollow && dist < (r-1)*(r-1))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        circleblocks.add(l);
                        }
                    }
 
        return circleblocks;
    }
	
	public String getCardinalDirection(CaveSpider player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
	}
	
	public MobSpawns(MoY instance) {
		this.plugin = instance;
		this.boat = instance.boat;
		this.cart = instance.cart;
		this.dragon = instance.dragon;
		this.rand = instance.re;
		this.playerinfo = instance.playerinfo;
	}
}
