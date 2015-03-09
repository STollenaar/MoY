package com.tollenaar.stephen.PlayerInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Tree;










import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.tollenaar.stephen.MistsOfYsir.MoY;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Skillimprovement implements Listener{
	private MoY plugin;
	private LevelSystems level; 
	
	
	public HashSet<Block> miningblocksplaces = new HashSet<Block>();
	public HashSet<Block> wooodblocksplaces = new HashSet<Block>();
	
	@EventHandler
	public void onblockplace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if(player.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
		ArrayList<Material> miningblocks = blockbreaking("highlvl");
		Block block = event.getBlock();
		if(miningblocks.contains(block.getType())){
			miningblocksplaces.add(block);
		}else if(block.getType() == Material.LOG || block.getType() == Material.LOG_2){
			wooodblocksplaces.add(block);
		}
	}
	}
	
	
	@EventHandler
	public void onPlayerfish(PlayerFishEvent event){
		
		Player player = event.getPlayer();
		if(player.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
		
		if(event.getState() == State.CAUGHT_FISH){
			fishcatch(player, event);
		}
		}
		}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(player.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
		Block block = event.getBlock();
		Material blockmat = block.getType();
		ArrayList<Material> highlvl = blockbreaking("highlvl");
		
		if(highlvl.contains(blockmat)){
			if(!miningblocksplaces.contains(block)){
			mining(event);
			}else{
				miningblocksplaces.remove(block);
			}
		}else
		if(blockmat == Material.LOG || blockmat == Material.LOG_2){
			if(!wooodblocksplaces.contains(block)){
			woodcutting(event);
		}else{
			wooodblocksplaces.remove(block);
		}
		}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void woodcutting(BlockBreakEvent event){
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		
		TreeSpecies log;
		if(event.getBlock().getType() == Material.LOG){
		 log = ((Tree) block.getState().getData()).getSpecies();
		}else{
			short data = block.getData();
			if(data == 0 || data == 4 || data == 8 || data == 12){
				log = TreeSpecies.ACACIA;
			}else{
				log = TreeSpecies.DARK_OAK;
			}
		}
		PermissionUser user = PermissionsEx.getUser(player);
		ArrayList<TreeSpecies>	lowlvl = wood("lowlvl");
		ArrayList<TreeSpecies> lowmidlvl = wood("lowmidlvl");
		ArrayList<TreeSpecies>	midlvl = wood("midlvl");
		ArrayList<TreeSpecies> highlvl = wood("highlvl");
		int woodcutting = Playerstats.woodcutting.get(player.getUniqueId());
		if(woodcutting <= 10){
			if(lowlvl.contains(log)){
				if(!user.getGroups().equals("Peasant")){
					woodcuttingdubbeldrop(player, block);
				}
				if(player.isOnline() == true){
					if( woodcutting != 50){
					if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) >= (woodcutting * 10)){
						int miner = Playerstats.woodcutting.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) > (woodcutting * 10)){
								rest = (Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) - (woodcutting * 10);
							}else{
								rest = 0;
							}
							Playerstats.woodcutting.put(player.getUniqueId(), miner);
							Playerstats.woodcuttingprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Woodcutting has raised with 1 level. Its now level " + Playerstats.woodcutting.get(player.getUniqueId()) + ".");
					}else{
					Playerstats.woodcuttingprog.put(player.getUniqueId(), Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl));
				}
					}
				}else{
					if( woodcutting != 50){
					if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) >= (woodcutting * 10)){
						int miner = Playerstats.woodcutting.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) > (woodcutting * 10)){
								rest = (Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) - (woodcutting * 10);
							}else{
								rest = 0;
							}
							Playerstats.woodcutting.put(player.getUniqueId(), miner);
							Playerstats.woodcuttingprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Woodcutting has raised with 1 level. Its now level " + Playerstats.woodcutting.get(player.getUniqueId()) + ".");
					}else{
						Playerstats.woodcuttingprog.put(player.getUniqueId(), Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl));				}
					}
				}
			}else{
				event.setCancelled(true);
				int neededlvl = 0;
				if(lowmidlvl.contains(log)){
					neededlvl = 11;
				}else if(midlvl.contains(log)){
					neededlvl = 21;
				}else {
					neededlvl = 41;
				}
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry, your woodcutting level is not high enough to mine this bock. You need to be level " + neededlvl + "for this wood kind.");

			}
		}else
		if(woodcutting <= 20){
			if(lowmidlvl.contains(log)){
				if(!user.getGroups().equals("Peasant ")){
					woodcuttingdubbeldrop(player, block);
				}
				if(player.isOnline() == true){
					if( woodcutting != 50){
					if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) >= (woodcutting * 10)){
						int miner = Playerstats.woodcutting.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) > (woodcutting * 10)){
								rest = (Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) - (woodcutting * 10);
							}else{
								rest = 0;
							}
							Playerstats.woodcutting.put(player.getUniqueId(), miner);
							Playerstats.woodcuttingprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Woodcutting has raised with 1 level. Its now level " + Playerstats.woodcutting.get(player.getUniqueId()) + ".");
					}else{
						Playerstats.woodcuttingprog.put(player.getUniqueId(), Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl));				}
					}
				}else{
					if( woodcutting != 50){
					if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) >= (woodcutting * 10)){
						int miner = Playerstats.woodcutting.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) > (woodcutting * 10)){
								rest = (Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) - (woodcutting * 10);
							}else{
								rest = 0;
							}
							Playerstats.woodcutting.put(player.getUniqueId(), miner);
							Playerstats.woodcuttingprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Woodcutting has raised with 1 level. Its now level " + Playerstats.woodcutting.get(player.getUniqueId()) + ".");
					}else{
						Playerstats.woodcuttingprog.put(player.getUniqueId(), Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl));				}
					}
				}
			}else{
				event.setCancelled(true);
				int neededlvl = 0;
				
				if(midlvl.contains(log)){
					neededlvl = 21;
				}else {
					neededlvl = 41;
				}
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry, your woodcutting level is not high enough to mine this bock. You need to be level " + neededlvl + "for this wood kind.");

			}
		}else
		if(woodcutting <= 40){
			if(midlvl.contains(log)){
				if(!user.getGroups().equals("Peasant ")){
					woodcuttingdubbeldrop(player, block);
				}
				if(player.isOnline() == true){
					if( woodcutting != 50){
					if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) >= (woodcutting * 10)){
						int miner = Playerstats.woodcutting.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) > (woodcutting * 10)){
								rest = (Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) - (woodcutting * 10);
							}else{
								rest = 0;
							}
							Playerstats.woodcutting.put(player.getUniqueId(), miner);
							Playerstats.woodcuttingprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Woodcutting has raised with 1 level. Its now level " + Playerstats.woodcutting.get(player.getUniqueId()) + ".");
					}else{
						Playerstats.woodcuttingprog.put(player.getUniqueId(), Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl));				}
					}
				}else{
					if( woodcutting != 50){
					if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) >= (woodcutting * 10)){
						int miner = Playerstats.woodcutting.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) > (woodcutting * 10)){
								rest = (Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) - (woodcutting * 10);
							}else{
								rest = 0;
							}
							Playerstats.woodcutting.put(player.getUniqueId(), miner);
							Playerstats.woodcuttingprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Woodcutting has raised with 1 level. Its now level " + Playerstats.woodcutting.get(player.getUniqueId()) + ".");
					}else{
						Playerstats.woodcuttingprog.put(player.getUniqueId(), Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl));				}
					}
				}
			}else{
				event.setCancelled(true);
				int neededlvl = 0;
				if(midlvl.contains(log)){
					neededlvl = 21;
				}else {
					neededlvl = 41;
				}
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry, your woodcutting level is not high enough to mine this bock. You need to be level " + neededlvl + "for this wood kind.");

			}
		}else
		if(woodcutting <= 50){
			if(highlvl.contains(log)){
				if(!user.getGroups().equals("Peasant ")){
					woodcuttingdubbeldrop(player, block);
				}
				if(player.isOnline() == true){
					if( woodcutting != 50){
					if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) >= (woodcutting * 10)){
						int miner = Playerstats.woodcutting.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) > (woodcutting * 10)){
								rest = (Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) - (woodcutting * 10);
							}else{
								rest = 0;
							}
							Playerstats.woodcutting.put(player.getUniqueId(), miner);
							Playerstats.woodcuttingprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Woodcutting has raised with 1 level. Its now level " + Playerstats.woodcutting.get(player.getUniqueId()) + ".");
					}else{
						Playerstats.woodcuttingprog.put(player.getUniqueId(), Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl));				}
					}
				}else{
					if( woodcutting != 50){
					if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) >= (woodcutting * 10)){
						int miner = Playerstats.woodcutting.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) > (woodcutting * 10)){
								rest = (Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl)) - (woodcutting * 10);
							}else{
								rest = 0;
							}
							Playerstats.woodcutting.put(player.getUniqueId(), miner);
							Playerstats.woodcuttingprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Woodcutting has raised with 1 level. Its now level " + Playerstats.woodcutting.get(player.getUniqueId()) + ".");
					}else{
						Playerstats.woodcuttingprog.put(player.getUniqueId(), Playerstats.woodcuttingprog.get(player.getUniqueId()) + level.woodxp(block, lowlvl, lowmidlvl, midlvl, highlvl));				}
					}
				}
			}
		}
		
		if(!event.isCancelled()){
			Location l = player.getTargetBlock(null, 1).getLocation();
			l.setY(l.getY()+1);
			final Hologram holo = HologramsAPI.createHologram(plugin, l);
			holo.appendTextLine(ChatColor.RED + "Level Progress");
			holo.appendTextLine( ChatColor.GOLD + "Gained +" + level.woodxp(block, lowlvl, midlvl, lowmidlvl, highlvl)
					+ "xp");
			holo.appendTextLine(ChatColor.GRAY + "[Progress "
					+ Playerstats.levelprog.get(player.getUniqueId()) + "/"
					+ Playerstats.levelprog.get(player.getUniqueId()) * 15
					+ "]");
			VisibilityManager vm = holo.getVisibilityManager();
			vm.showTo(player);
			
	
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					holo.delete();
				}
			}, 2*20L);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void mining(BlockBreakEvent event){
		Player player = event.getPlayer();
		Block block = event.getBlock();
		PermissionUser user = PermissionsEx.getUser(player);
		ArrayList<Material> lowlvl = blockbreaking("lowlvl");
		ArrayList<Material> midlvl = blockbreaking("midlvl");
		ArrayList<Material> highlvl = blockbreaking("highlvl");
		ArrayList<Material> midhighlvl = blockbreaking("midhighlvl");
		int mining = Playerstats.mining.get(player.getUniqueId());
		if(mining <= 10){
			if(lowlvl.contains(block.getType())){
				if(!user.getGroups().equals("Peasant")){
					miningdubbeldrop(player, block);
				}
				if(player.isOnline() == true){
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
					Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));
				}
					}
				}else{
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
						Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));
				}
					}
				}
			}else{
				event.setCancelled(true);
				int neededlvl = 0;
				if(midlvl.contains(block.getType())){
					neededlvl = 21;
				}else if(midhighlvl.contains(block.getType())){
					neededlvl = 31;
				}else {
					neededlvl = 41;
				}

				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry, your mining level is not high enough to mine this block.You need to be level " + neededlvl + "for this ore kind.");
			}
		} else
		if(mining >= 11 && mining <= 20){
			if(lowlvl.contains(block.getType())){
				if(!user.getGroups().equals("Peasant")){
					miningdubbeldrop(player, block);
				}
				if(player.isOnline() == true){
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
						Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));
				}
					}
				}else{
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
						Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));
				}
					}
				}
			}else{
				event.setCancelled(true);
				int neededlvl = 0;
				if(midlvl.contains(block.getType())){
					neededlvl = 21;
				}else 
				if(midhighlvl.contains(block.getType())){
					neededlvl = 31;
				}else {
					neededlvl = 41;
				}

				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry, your mining level is not high enough to mine this block.You need to be level " + neededlvl + "for this ore kind.");
			}
		} else
		if(mining >= 21 && mining <= 30){
			if(midlvl.contains(block.getType())){
				if(!user.getGroups().equals("Peasant")){
					miningdubbeldrop(player, block);
				}
				if(player.isOnline() == true){
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
						Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));
				}
					}
				}else{
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
						Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));
				}
					}
				}
			}else{
				event.setCancelled(true);
				int neededlvl = 0;
				if(midhighlvl.contains(block.getType())){
					neededlvl = 31;
				}else {
					neededlvl = 41;
				}

				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry, your mining level is not high enough to mine this block.You need to be level " + neededlvl + "for this ore kind.");			}
		}else 
		if(mining >= 31 && mining <= 40){
			if(midhighlvl.contains(block.getType())){
				if(!user.getGroups().equals("Peasant")){
					miningdubbeldrop(player, block);
				}
				if(player.isOnline() == true){
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
						Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));
				}
					}
				}else{
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
						Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));
				}
					}
				}
			}else{
				event.setCancelled(true);
				int neededlvl = 0;

					neededlvl = 41;

				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry, your mining level is not high enough to mine this block.You need to be level " + neededlvl + "for this ore kind.");			}
		}else 
		if(mining >= 41 && mining <= 50){
			if(highlvl.contains(block.getType())){
				if(!user.getGroups().equals("Peasant")){
					miningdubbeldrop(player, block);
				}
				if(player.isOnline() == true){
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
						Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));
				}
					}
				}else{
					if( mining != 50){
					if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) >= (mining * 10)){
						int miner = Playerstats.mining.get(player.getUniqueId());
							miner++;
							int rest = 0;
							if((Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) > (mining * 10)){
								rest = (Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block)) - (mining * 10);
							}else{
								rest = 0;
							}
							Playerstats.mining.put(player.getUniqueId(), miner);
							Playerstats.miningprog.put(player.getUniqueId(), rest);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Mining has raised with 1 level. Its now level " + Playerstats.mining.get(player.getUniqueId()) + ".");

					}else{
						Playerstats.miningprog.put(player.getUniqueId(), Playerstats.miningprog.get(player.getUniqueId()) + level.miningxp(block));

					}
					}
				}
			}
		}
		if(!event.isCancelled()){
			Location l = player.getTargetBlock(null, 1).getLocation();
			l.setY(l.getY()+1);
			final Hologram holo = HologramsAPI.createHologram(plugin, l);
			holo.appendTextLine(ChatColor.RED + "Level Progress");
			holo.appendTextLine( ChatColor.GOLD + "Gained +" + level.miningxp(block)
					+ "xp");
			holo.appendTextLine(ChatColor.GRAY + "[Progress "
					+ Playerstats.levelprog.get(player.getUniqueId()) + "/"
					+ Playerstats.levelprog.get(player.getUniqueId()) * 15
					+ "]");
			VisibilityManager vm = holo.getVisibilityManager();
			vm.showTo(player);
			
	
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					holo.delete();
				}
			}, 2*20L);
		}
		}
	
	@SuppressWarnings("deprecation")
	@EventHandler
 	public void onSmelt(FurnaceSmeltEvent event){
		Furnace furnace = (Furnace) event.getFurnace().getState();
		if(furnace.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
		if(furnace.getInventory().getSmelting().getType().isEdible() == true){
			cooking(furnace, event);
		}else{
			smelting(furnace);
		}
		}
	}
	@EventHandler
	public void onFurnaceOPen(InventoryOpenEvent event){
		Player player = (Player) event.getPlayer();
		if(player.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
		if(event.getInventory().getType() == InventoryType.FURNACE){
			Furnace furnace = (Furnace) event.getInventory().getHolder();
			if(furnace.getBurnTime() != 0){
				if(Playerstats.allthosefurnaces.get(furnace.getBlock()) != null){
				if(player.getUniqueId().equals(Playerstats.allthosefurnaces.get(furnace.getBlock()))){
					
				}else{
					event.setCancelled(true);
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " This furnace is used by someone else. Come back later.");
				}
			}else{
				Playerstats.allthosefurnaces.put(furnace.getBlock(), player.getUniqueId());
			}
			}else{
				Playerstats.allthosefurnaces.put(furnace.getBlock(), player.getUniqueId());
			}
		}
	}
	}

	private void miningdubbeldrop(Player player, Block block){
		Random r = new Random();
		int n = r.nextInt(100);
		int chance = 0;
		int lvl = Playerstats.mining.get(player.getUniqueId());
		if(lvl <= 10){
			chance = 1;
		}
		if(lvl >= 11 && lvl <=20){
			chance = 4;
		}
		if(lvl >= 21 && lvl <= 30){
			chance = 6;
		}
		if(lvl >= 31 && lvl <= 40){
			chance = 7;
		}
		if(lvl >= 41 && lvl <= 50){
			chance = 8;
		}
		if(n <= chance){
			block.getWorld().dropItem(block.getLocation(), new ItemStack(block.getType(), 2));
		}
		
	}
	private void woodcuttingdubbeldrop(Player player, Block block){
		Random r = new Random();
		int n = r.nextInt(100);
		int chance = 0;
		int lvl = Playerstats.woodcutting.get(player.getUniqueId());
		if(lvl <= 10){
			chance = 1;
		}
		if(lvl >= 11 && lvl <=20){
			chance = 4;
		}
		if(lvl >= 21 && lvl <= 30){
			chance = 6;
		}
		if(lvl >= 31 && lvl <= 40){
			chance = 7;
		}
		if(lvl >= 41 && lvl <= 50){
			chance = 8;
		}
		if(n <= chance){
			block.getWorld().dropItem(block.getLocation(), new ItemStack(block.getType(), 2));
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void cooksucces(Player player, Furnace furnace, boolean succes, boolean online, ItemStack smelteditem){

		
		if(online == true){
		if(succes == true){
			int xp;
			if(smelteditem.getType() == Material.COOKED_FISH && smelteditem.getDurability() == 1){
				xp = 3;
			}else
			if(smelteditem.getType() != Material.COOKED_FISH && smelteditem.getType() != Material.BAKED_POTATO){
				xp = 2;
			}else{
				xp = 1;
			}
			int cooker = Playerstats.cooking.get(Playerstats.allthosefurnaces.get(furnace.getBlock()));
			if((Playerstats.cookingprog.get(player.getUniqueId()) + xp) >= (cooker * 10)){
				int cooking = Playerstats.cooking.get(player.getUniqueId());
					cooking++;
					int rest = 0;
					if((Playerstats.cookingprog.get(player.getUniqueId()) + xp) > (cooker * 10)){
						rest = (Playerstats.cookingprog.get(player.getUniqueId()) + xp) - (cooker * 10);
					}else{
						rest = 0;
					}
					Playerstats.cooking.put(player.getUniqueId(), cooking);
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Cooking has raised with 1 level. You are now level " + cooking + ".");
					Playerstats.cookingprog.put(player.getUniqueId(), rest);
			}else{
			Playerstats.cookingprog.put(player.getUniqueId(), Playerstats.cookingprog.get(player.getUniqueId()) + xp);
			}
		}else{
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Something has gone wrong. Your food is burned.");
		if(Playerstats.furnacestore.get(furnace.getBlock()) == null){
				ItemStack burned = new ItemStack(Material.ROTTEN_FLESH);
				ItemMeta bm = burned.getItemMeta();
				String foodtype = cookname(smelteditem.getTypeId(), smelteditem.getDurability());
				bm.setDisplayName("Burned " + foodtype);
				burned.setItemMeta(bm);
				Playerstats.furnacestore.put(furnace.getBlock(), burned);
			}else{
				ItemStack stack = Playerstats.furnacestore.get(furnace.getBlock());
				stack.setAmount(stack.getAmount() + 1);
				Playerstats.furnacestore.put(furnace.getBlock(), stack);
	}
		}
		}else{
			if(succes == true){
				int xp;
				if(smelteditem.getType() == Material.COOKED_FISH && smelteditem.getDurability() == 1){
					xp = 3;
				}else
				if(smelteditem.getType() != Material.COOKED_FISH && smelteditem.getType() != Material.BAKED_POTATO){
					xp = 2;
				}else{
					xp = 1;
				}
				int cooker = Playerstats.cooking.get(Playerstats.allthosefurnaces.get(furnace.getBlock()));
				if((Playerstats.cookingprog.get(player.getUniqueId()) + xp) >= (cooker * 10)){
					int cooking = Playerstats.cooking.get(player.getUniqueId());
						cooking++;
						int rest = 0;
						if((Playerstats.cookingprog.get(player.getUniqueId()) + xp) > (cooker * 10)){
							rest = (Playerstats.cookingprog.get(player.getUniqueId()) + xp) - (cooker * 10);
						}else{
							rest = 0;
						}
						Playerstats.cooking.put(player.getUniqueId(), cooking);
						Playerstats.cookingprog.put(player.getUniqueId(), rest);
				}else{
				Playerstats.cookingprog.put(player.getUniqueId(), Playerstats.cookingprog.get(player.getUniqueId()) + xp);
				}
			}else{
			if(Playerstats.furnacestore.get(furnace.getBlock()) == null){
				ItemStack burned = new ItemStack(Material.ROTTEN_FLESH);
				ItemMeta bm = burned.getItemMeta();
				String foodtype = cookname(smelteditem.getTypeId(), smelteditem.getDurability());
				bm.setDisplayName("Burned " + foodtype);
				burned.setItemMeta(bm);
				Playerstats.furnacestore.put(furnace.getBlock(), burned);
				}else{
					ItemStack stack = Playerstats.furnacestore.get(furnace.getBlock());
					stack.setAmount(stack.getAmount() + 1);
					Playerstats.furnacestore.put(furnace.getBlock(), stack);
		}
			}
		}
}
	
	private ArrayList<TreeSpecies> wood(String name){
		if(name.equals("lowlvl")){
			ArrayList<TreeSpecies> temp = new ArrayList<TreeSpecies>();
			temp.add(TreeSpecies.GENERIC);
			temp.add(TreeSpecies.BIRCH);
			return temp;
			
		}
		if(name.equals("lowmidlvl")){
			ArrayList<TreeSpecies> temp = new ArrayList<TreeSpecies>();
			temp.add(TreeSpecies.GENERIC);
			temp.add(TreeSpecies.BIRCH);
			temp.add(TreeSpecies.ACACIA);
			temp.add(TreeSpecies.REDWOOD);
			return temp;
		}
		if(name.equals("midlvl")){
			ArrayList<TreeSpecies> temp = new ArrayList<TreeSpecies>();
			temp.add(TreeSpecies.GENERIC);
			temp.add(TreeSpecies.BIRCH);
			temp.add(TreeSpecies.ACACIA);
			temp.add(TreeSpecies.REDWOOD);
			temp.add(TreeSpecies.JUNGLE);
			return temp;
		}
		if(name.equals("highlvl")){
			ArrayList<TreeSpecies> temp = new ArrayList<TreeSpecies>();
			temp.add(TreeSpecies.GENERIC);
			temp.add(TreeSpecies.BIRCH);
			temp.add(TreeSpecies.ACACIA);
			temp.add(TreeSpecies.REDWOOD);
			temp.add(TreeSpecies.JUNGLE);
			temp.add(TreeSpecies.DARK_OAK);
			return temp;
		}
		else{
			ArrayList<TreeSpecies> oops = new ArrayList<TreeSpecies>();
			oops.add(TreeSpecies.GENERIC);
			plugin.getLogger().info("Something went terrible wrong");
			return oops;
		}
		
	}
	
	@EventHandler
	public void onFurnaceempty(InventoryClickEvent event){
		if(event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.FURNACE){
			Furnace furnace = (Furnace) event.getInventory().getHolder();
			if(furnace.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
			if(event.getSlotType() == SlotType.RESULT){
				if(Playerstats.furnacestore.get(furnace.getBlock()) != null){
					int space = 0;
					Player player = (Player) event.getWhoClicked();
					for(ItemStack item : player.getInventory().getContents()){
						if(item == null){
						space++;
					}
					}
					if(space >= 0){
						player.getInventory().addItem(Playerstats.furnacestore.get(furnace.getBlock()));
					Playerstats.furnacestore.remove(furnace.getBlock());
				}
				}
			}

		}
		}
	}

	private ArrayList<Material> blockbreaking(String name){
		if(name.equals("lowlvl")){
			ArrayList<Material> lowlvl = new ArrayList<Material>();
			lowlvl.add(Material.CLAY);
			lowlvl.add(Material.COAL_ORE);
			lowlvl.add(Material.IRON_ORE);
			return lowlvl;
		}
		if(name.equals("midlvl")){
			ArrayList<Material> midlvl = new ArrayList<Material>();
			midlvl.add(Material.CLAY);
			midlvl.add(Material.COAL_ORE);
			midlvl.add(Material.IRON_ORE);
			midlvl.add(Material.REDSTONE_ORE);
			midlvl.add(Material.LAPIS_ORE);
			return midlvl;
		}
		if(name.equals("midhighlvl")){
			ArrayList<Material> midhighlvl = new ArrayList<Material>();
			midhighlvl.add(Material.CLAY);
			midhighlvl.add(Material.COAL_ORE);
			midhighlvl.add(Material.IRON_ORE);
			midhighlvl.add(Material.REDSTONE_ORE);
			midhighlvl.add(Material.LAPIS_ORE);
			midhighlvl.add(Material.GOLD_ORE);
			return midhighlvl;
		}
		if(name.equals("highlvl")){
			ArrayList<Material> highlvl = new ArrayList<Material>();
			highlvl.add(Material.CLAY);
			highlvl.add(Material.COAL_ORE);
			highlvl.add(Material.IRON_ORE);
			highlvl.add(Material.REDSTONE_ORE);
			highlvl.add(Material.LAPIS_ORE);
			highlvl.add(Material.GOLD_ORE);
			highlvl.add(Material.DIAMOND_ORE);
			return highlvl;
		}else{
			ArrayList<Material> oops = new ArrayList<Material>();
			oops.add(Material.AIR);
			plugin.getLogger().info("something went terrible wrong!!!!");
			return oops;
		}
	}
	
	private ArrayList<Material> listin(String name){
		if(name.equals("lowlvl")){
			ArrayList<Material> lowlvl = new ArrayList<Material>();
			lowlvl.add(Material.POTATO_ITEM);
			lowlvl.add(Material.RAW_FISH);
			lowlvl.add(Material.RAW_CHICKEN);
			return lowlvl;
		}
		if(name.equals("midlowlvl")){
			ArrayList<Material> midlowlvl = new ArrayList<Material>();
			midlowlvl.add(Material.POTATO_ITEM);
			midlowlvl.add(Material.RAW_FISH);
			midlowlvl.add(Material.RAW_BEEF);
			midlowlvl.add(Material.RAW_CHICKEN);
			return midlowlvl;
		}
		if(name.equals("midlvl")){
			ItemStack salmon = new ItemStack(Material.RAW_FISH,  1, (short) 1);
			ArrayList<Material> midlvl = new ArrayList<Material>();
			midlvl.add(Material.POTATO_ITEM);
			midlvl.add(Material.RAW_FISH);
			midlvl.add(Material.RAW_BEEF);
			midlvl.add(Material.RAW_CHICKEN);
			midlvl.add(salmon.getType());
			return midlvl;
		}
		if(name.equals("midhighlvl")){
			ItemStack salmon = new ItemStack(Material.RAW_FISH,  1, (short) 1);
			ArrayList<Material> midhighlvl = new ArrayList<Material>();
			midhighlvl.add(Material.POTATO_ITEM);
			midhighlvl.add(Material.RAW_FISH);
			midhighlvl.add(Material.RAW_BEEF);
			midhighlvl.add(salmon.getType());
			midhighlvl.add(Material.RAW_CHICKEN);
			return midhighlvl;
		}
		if(name.equals("highlvl")){
			ItemStack salmon = new ItemStack(Material.RAW_FISH,  1, (short) 1);
			ArrayList<Material> highlvl = new ArrayList<Material>();
			highlvl.add(Material.POTATO_ITEM);
			highlvl.add(Material.RAW_FISH);
			highlvl.add(Material.RAW_BEEF);
			highlvl.add(salmon.getType());
			highlvl.add(Material.RAW_CHICKEN);
			highlvl.add(Material.PORK);
			return highlvl;
		}else{
			ArrayList<Material> oops = new ArrayList<Material>();
			oops.add(Material.AIR);
			plugin.getLogger().info("DE LISTEN ZIJN NIET GOED!!!!!!!" + name);
			return oops;
		}
	}
	
	private void cooking(Furnace furnace, FurnaceSmeltEvent event){
		ArrayList<Material> lowlvl = listin("lowlvl");
		ArrayList<Material> midlowlvl = listin("midlowlvl");
		ArrayList<Material> midlvl = listin("midlvl");
		ArrayList<Material> midhighlvl = listin("midhighlvl");
		ArrayList<Material> highlvl = listin("highlvl");
		int cooker = Playerstats.cooking.get(Playerstats.allthosefurnaces.get(furnace.getBlock()));
		int cooksucces = level.succes(cooker, event.getResult(), lowlvl, midlowlvl, midlvl, midhighlvl, highlvl);
		Random r = new Random();
		int x = r.nextInt(100);
			UUID playeruuid = Playerstats.allthosefurnaces.get(furnace.getBlock());
			String playername = Bukkit.getPlayer(playeruuid).getName();
					
			if(x <= cooksucces){
					boolean on;
					Player player = Bukkit.getPlayer(playername);
					if(player.isOnline() == true){
						on = true;
					}else{
						on = false;
					}
					cooksucces(player, furnace, true, on, furnace.getInventory().getResult());
				}else{
					boolean on;
					Player player = Bukkit.getPlayer(playername);
					if(player.isOnline() == true){
						on = true;
					}else{
						on = false;
					}
					ItemStack item = furnace.getInventory().getSmelting();
					item.setAmount(item.getAmount()  - 1);
					furnace.getInventory().setSmelting(item);
					cooksucces(player, furnace, false, on, furnace.getInventory().getResult());
					event.setCancelled(true);
					
				}
				
		
	}
		
	private void smelting(Furnace furnace){
		int smelter = Playerstats.smelting.get(Playerstats.allthosefurnaces.get(furnace.getBlock()));
		UUID playeruuid = Playerstats.allthosefurnaces.get(furnace.getBlock());
		String playername = Bukkit.getPlayer(playeruuid).getName();
		Player player = Bukkit.getPlayer(playername);		
		ItemStack item;
				if(furnace.getInventory().getResult() == null || furnace.getInventory().getResult().getType() == Material.AIR){
					item = new ItemStack(Material.AIR);
				}else{
					item = furnace.getInventory().getResult();
				}
		if(player.isOnline() == true){
			if(smelter != 50){

				
			if((Playerstats.smeltingprog.get(player.getUniqueId()) + level.smeltingxp(item)) >= (smelter * 10)){
				int smelting = Playerstats.smelting.get(player.getUniqueId());
					smelting++;
					int rest = 0;
					if((Playerstats.smeltingprog.get(player.getUniqueId()) + level.smeltingxp(item)) > (smelter * 10)){
						rest = (Playerstats.smeltingprog.get(player.getUniqueId()) + level.smeltingxp(item)) - (smelter * 10);
					}else{
						rest = 0;
					}
					Playerstats.smelting.put(player.getUniqueId(), smelting);
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Smelting has raised with 1 level. You are now level " + smelting + ".");
					Playerstats.smeltingprog.put(player.getUniqueId(), rest);
			}else{
			Playerstats.smeltingprog.put(player.getUniqueId(), Playerstats.smeltingprog.get(player.getUniqueId()) + level.smeltingxp(item));
		}
			}
		}else{
			if(smelter != 50){
			if((Playerstats.smeltingprog.get(player.getUniqueId()) + level.smeltingxp(item)) >= (smelter * 10)){
				int smelting = Playerstats.smelting.get(player.getUniqueId());
					smelting++;
					int rest = 0;
					if((Playerstats.smeltingprog.get(player.getUniqueId()) + level.smeltingxp(item)) > (smelter * 10)){
						rest = (Playerstats.smeltingprog.get(player.getUniqueId()) + level.smeltingxp(item)) - (smelter * 10);
					}else{
						rest = 0;
					}
					Playerstats.smelting.put(player.getUniqueId(), smelting);
					Playerstats.smeltingprog.put(player.getUniqueId(), rest);
			}else{
			Playerstats.smeltingprog.put(player.getUniqueId(), Playerstats.smeltingprog.get(player.getUniqueId()) + level.smeltingxp(item));
		}
			}	
		}
	}

	public Skillimprovement(MoY instance){
		this.plugin = instance;
		this.level = instance.lvl;
	}
	
	private String cookname(int id, short dura){
		switch(id){
		case 319:
			return "Porkchop";
		case 363:
			return "Steak";
		case 423:
			return "Mutton";
		case 349:
				if(dura == 0){
					return "Fish";
				}else{
					return "Salmon";
				}
		case 365:
			return "Chicken";
		case 411:
			return "Rabbit";
		case 392:
			return "Potato";
		default:
			return "Flesh";
		}
	}
	
	private ArrayList<Integer> chance(int playerlvl){
		ArrayList<Integer> chance = new ArrayList<Integer>();
		if(playerlvl < 11){
			chance.add(55);
			chance.add(65);
			chance.add(70);
			chance.add(100);
		}else if(playerlvl < 21){
			chance.add(46);
			chance.add(63);
			chance.add(72);
			chance.add(100);
		}else if(playerlvl < 31){
			chance.add(42);
			chance.add(61);
			chance.add(71);
			chance.add(95);
			chance.add(100);
		}else if(playerlvl < 41){
			chance.add(35);
			chance.add(59);
			chance.add(73);
			chance.add(92);
			chance.add(100);
		}else{
			chance.add(30);
			chance.add(58);
			chance.add(76);
			chance.add(85);
			chance.add(100);
		}
		
		return chance;
	}
	
	@SuppressWarnings("deprecation")
	private void fishcatch(Player player, PlayerFishEvent event){
		int fishinglvl = Playerstats.fishing.get(player.getUniqueId());
		PermissionUser user = PermissionsEx.getUser(player);
		Item it = (Item) event.getCaught();
		ItemStack item = it.getItemStack();	
		ArrayList<Material> junks = new ArrayList<Material>();
		{
			junks.add(Material.BOWL);
			junks.add(Material.FISHING_ROD);
			junks.add(Material.LEATHER);
			junks.add(Material.LEATHER_BOOTS);
			junks.add(Material.ROTTEN_FLESH);
			junks.add(Material.STICK);
			junks.add(Material.STRING);
			junks.add(Material.getMaterial(373));
			junks.add(Material.BONE);
			junks.add(Material.INK_SACK);
			junks.add(Material.TRIPWIRE_HOOK);
		}
		int junk = 0;
		int treasure = 0;

		if(item.getType() != Material.RAW_FISH){
			if(junks.contains(item.getType())){
				if(item.getType() == Material.FISHING_ROD){
					if(item.getEnchantments() == null){
						junk = 1;
					}else{
						treasure = 1;
					}
				}
			}else{
				treasure = 1;
			}
		}
		
		{
			ArrayList<Integer> chance = chance(fishinglvl);
			Random r = new Random();
			int n = r.nextInt(100);
			if(chance.size() == 4){
			if(n <= chance.get(0)){
				if(item.getType() != Material.RAW_FISH && item.getDurability() != 0){
					item = new ItemStack(Material.RAW_FISH);
				}
			}else if(n <= chance.get(1)){
				if(item.getType() != Material.RAW_FISH && item.getDurability() != 1){
					item = new ItemStack(Material.RAW_FISH, 1, (short) 1);
				}
			}else if(n <= chance.get(2)){
				if(item.getType() != Material.RAW_FISH && (item.getDurability() != 2 || item.getDurability() != 3)){
					int t = r.nextInt(1);
					if(t == 0){
					item = new ItemStack(Material.RAW_FISH, 1, (short) 2);
				}else{
					item = new ItemStack(Material.RAW_FISH, 1, (short) 3);

				}
				}
			}else{
				if(!junks.contains(item.getType())){
					item = new ItemStack(junks.get(6));
				}
			}
		}else{
			if(n <= chance.get(0)){
				if(item.getType() != Material.RAW_FISH && item.getDurability() != 0){
					item = new ItemStack(Material.RAW_FISH);
				}
			}else if(n <= chance.get(1)){
				if(item.getType() != Material.RAW_FISH && item.getDurability() != 1){
					item = new ItemStack(Material.RAW_FISH, 1, (short) 1);
				}
			}else if(n <= chance.get(2)){
				if(item.getType() != Material.RAW_FISH && (item.getDurability() != 2 || item.getDurability() != 3)){
					int t = r.nextInt(1);
					if(t == 0){
					item = new ItemStack(Material.RAW_FISH, 1, (short) 2);
				}else{
					item = new ItemStack(Material.RAW_FISH, 1, (short) 3);

				}
				}
			}else if(n <= chance.get(3)){
				if(!junks.contains(item.getType())){
					item = new ItemStack(junks.get(6));
				}
			}
		}
		}

		if(user.inGroup("Peasant")){
		if(fishinglvl != 50){
			if((Playerstats.fishingprog.get(player.getUniqueId()) + level.fishingxp(item, junk, treasure)) >= (fishinglvl * 10)){
				int smelting = Playerstats.fishing.get(player.getUniqueId());
					smelting++;
					int rest = 0;
					if((Playerstats.fishingprog.get(player.getUniqueId()) + level.fishingxp(item, junk, treasure)) > (fishinglvl *10)){
						rest = (Playerstats.fishingprog.get(player.getUniqueId()) + level.fishingxp(item, junk, treasure))  - (fishinglvl*10);
					}else{
						rest = 0;
					}
					Playerstats.fishing.put(player.getUniqueId(), smelting);
					Playerstats.fishingprog.put(player.getUniqueId(), rest);
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Fishing has raised with 1 level. You are now level " + smelting + ".");

			}else{
			Playerstats.fishingprog.put(player.getUniqueId(), Playerstats.fishingprog.get(player.getUniqueId()) + level.fishingxp(item, junk, treasure));
		}
			}
		}else{
			Random q = new Random();
			int n = q.nextInt(100);
			int chance = 0;
			int lvl = Playerstats.fishing.get(player.getUniqueId());
			if(lvl <= 10){
				chance = 1;
			}
			if(lvl >= 11 && lvl <=20){
				chance = 4;
			}
			if(lvl >= 21 && lvl <= 30){
				chance = 6;
			}
			if(lvl >= 31 && lvl <= 40){
				chance = 7;
			}
			if(lvl >= 41 && lvl <= 50){
				chance = 8;
			}
			if(n <= chance){
				item.setAmount(item.getAmount()+1);
			}
			if(fishinglvl != 50){
				if((Playerstats.fishingprog.get(player.getUniqueId()) + level.fishingxp(item, junk, treasure)) >= (fishinglvl * 10)){
					int smelting = Playerstats.fishing.get(player.getUniqueId());
						smelting++;
						int rest = 0;
						if((Playerstats.fishingprog.get(player.getUniqueId()) + level.fishingxp(item, junk, treasure)) > (fishinglvl *10)){
							rest = (Playerstats.fishingprog.get(player.getUniqueId()) + level.fishingxp(item, junk, treasure))  - (fishinglvl*10);
						}else{
							rest = 0;
						}
						Playerstats.fishing.put(player.getUniqueId(), smelting);
						Playerstats.fishingprog.put(player.getUniqueId(), rest);
						player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Fishing has raised with 1 level. You are now level " + smelting + ".");

				}else{
				Playerstats.fishingprog.put(player.getUniqueId(), Playerstats.fishingprog.get(player.getUniqueId()) + level.fishingxp(item, junk, treasure));
			}
				}
		}
	}
}
