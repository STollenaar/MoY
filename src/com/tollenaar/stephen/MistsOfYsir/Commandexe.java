package com.tollenaar.stephen.MistsOfYsir;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.earth2me.essentials.OfflinePlayer;
import com.tollenaar.stephen.PlayerInfo.Playerinfoding;
import com.tollenaar.stephen.PlayerInfo.Playerstats;
import com.tollenaar.stephen.Quests.QuestsServerSide;
import com.tollenaar.stephen.Travel.TravelBoatEvent;
import com.tollenaar.stephen.Travel.TravelCartEvent;
import com.tollenaar.stephen.Travel.TravelDragonEvent;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class Commandexe implements CommandExecutor  {
 private MoY plugin;
 private QuestsServerSide questers;
 private Playerinfoding playerinfo;
 private TravelBoatEvent boat;
 private TravelCartEvent cart;
 private TravelDragonEvent dragon;
 private Party party;
 private Filewriters fw;
 
public Commandexe(MoY instance){
	this.plugin = instance;
	this.questers = instance.questers;
	this.playerinfo = instance.playerinfo;
	this.boat = instance.boat;
	this.cart = instance.cart;
	this.dragon = instance.dragon;
	this.party = instance.party;
	this.fw = instance.fw;
}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player){
		 Player player = (Player)sender;
			PermissionUser user = PermissionsEx.getUser(player);
			//QNPC HANDLER
		
		 if(cmd.getName().equalsIgnoreCase("qnpc")){
			 System.out.println(user.has("Ysir.npcadjuster"));
		 if(user.has("Ysir.npcadjuster")){
			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("create")){
					if(args.length == 3){
					questers.spawnNpc(player.getLocation(), args[1], -1, args[2]);
					return true;
				}else{
					player.sendMessage(ChatColor.RED + "Please use /qnpc <create><npcname><npcskinname>.");
					return true;
				}
				}
			
			if(args[0].equals("tool")){
				Inventory playerinv = player.getInventory();
				ItemStack specialtool = new ItemStack(Material.DIAMOND_HOE);
				ItemMeta toolmeta = specialtool.getItemMeta();
				List<String> tem = new ArrayList<String>();
				tem.add("NPC adjustment Tool");
				toolmeta.setLore(tem);
				toolmeta.setDisplayName("NPC TOOL");
				specialtool.setItemMeta(toolmeta);
				playerinv.addItem(specialtool);
				return true;
			}	
			}else{
				player.sendMessage(ChatColor.RED + "Please use /qnpc <tool/create>.");
				return true;

			}
			 }else{
				 return false;
			 }
			//SKILL INVENTORY
			}else if(cmd.getName().equalsIgnoreCase("skill")){
				if(args.length == 0){
				playerinfo.playerstatsinv(player);
				return true;
				}else{
					if(args.length == 2){
						try{
						int newlvl = Integer.parseInt(args[1]);
						if(args[0].equalsIgnoreCase("wood")){
							Playerstats.woodcutting.put(player.getUniqueId(), newlvl);
						}else if(args[0].equalsIgnoreCase("mining")){
							Playerstats.mining.put(player.getUniqueId(), newlvl);
						}else if(args[0].equalsIgnoreCase("fishing")){
							Playerstats.fishing.put(player.getUniqueId(), newlvl);
						}else if(args[0].equalsIgnoreCase("cooking")){
							Playerstats.cooking.put(player.getUniqueId(), newlvl);
						}else if(args[0].equalsIgnoreCase("smelting")){
							Playerstats.smelting.put(player.getUniqueId(), newlvl);
						}else if(args[0].equalsIgnoreCase("strength")){
							Playerstats.Strengthscore.put(player.getUniqueId(), newlvl);
						}else if(args[0].equalsIgnoreCase("dex")){
							Playerstats.Dexscore.put(player.getUniqueId(), newlvl);
						}
						}catch(NumberFormatException ex){
							player.sendMessage("last argument wasn't a number.  Please use /skill <wood/mining/fishing/cooking/smelting> <lvl>.");
						}
						return true;
					}else{
						player.sendMessage("Please use /skill <wood/mining/fishing/cooking/smelting> <lvl>.");
						return true;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("lvl")){	
				if(user.has("Ysir.stafflvl")){
				if(args.length != 0){
				if(args[0].equalsIgnoreCase("up")){
					Playerstats.level.put(player.getUniqueId(), Playerstats.level.get(player.getUniqueId()) + 1);
				}else
				if(args[0].equals("down")){
					Playerstats.level.put(player.getUniqueId(), Playerstats.level.get(player.getUniqueId()) - 1);
				}else{
					try{
						int lvl = Integer.parseInt(args[0]);
						Playerstats.level.put(player.getUniqueId(), lvl);
					}catch(NumberFormatException ex){
						ex.printStackTrace();
					}
				}
				
				return true;
			}else{
				player.sendMessage("/lvl <parameter>");
				return true;
			}
			}
			}
		 //PARTY HANDLER
		else if(cmd.getName().equalsIgnoreCase("party")){
//			System.out.println(Thread.currentThread().getStackTrace()[1] + " leaders " + party.Partyleaders);
//			System.out.println(Thread.currentThread().getStackTrace()[1] + " with members " + party.Partyswithmembers);
//			System.out.println(Thread.currentThread().getStackTrace()[1] + " members " + party.Partymembers);
			
			if(args.length >= 1 && args.length <= 2 ){
			if((args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("setleader")) && args.length == 1){
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] Please use /party " + args[0] + " <name>.");
				return true;
			}
				if(args[0].equalsIgnoreCase("create")){
					party.createParty(player.getUniqueId(), args[1], player);
				}else
				if(args[0].equalsIgnoreCase("kick")){ //needs fixing
					if(party.Partymembers.get(player.getUniqueId()) != null){
					if(party.Partyleaders.get(party.Partymembers.get(player.getUniqueId())).compareTo(player.getUniqueId()) == 0 && !player.getName().equalsIgnoreCase(args[1])){
						Set<UUID> k = party.Partyswithmembers.get(party.Partymembers.get(player.getUniqueId())); //retrieving the members from the party name
							for(UUID f : k){
								Player member = Bukkit.getPlayer(args[1]);
								if(member != null){
								if(f.compareTo(member.getUniqueId()) == 0){
									party.kickmember(party.Partymembers.get(player.getUniqueId()), member.getUniqueId());
									String leader = fw.utilitiesconfig.getString("Partykick.leader");
									leader = leader.replace("%member%", member.getName());
									
									String victim = fw.utilitiesconfig.getString("Partykick.member");
									player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + leader);
									member.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + victim);
									break;
								}
								}else{
									@SuppressWarnings("deprecation")
									OfflinePlayer m = (OfflinePlayer) Bukkit.getOfflinePlayer(args[1]);
									if(m.getUniqueId() == f){
										party.kickmember(party.Partymembers.get(player.getUniqueId()), m.getUniqueId());
										String leader = fw.utilitiesconfig.getString("Partykick.leader");
										leader = leader.replace("%member%", m.getName());
										
										player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + leader);
									}
								}
						}
					}else{
						player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + fw.utilitiesconfig.getString("Partykick.himself"));

					}
				}
				}else if(args[0].equalsIgnoreCase("setleader")){
					if(party.Partymembers.get(player.getUniqueId()) != null){
						if(party.Partyleaders.get(party.Partymembers.get(player.getUniqueId())).compareTo(player.getUniqueId()) == 0){
							Player newleader = Bukkit.getPlayer(args[1]);
							if(newleader != null){
								if(party.Partymembers.get(newleader.getUniqueId()) != null){
									if(party.Partymembers.get(newleader.getUniqueId()).equals(party.Partymembers.get(player.getUniqueId()))){
										party.Partyleaders.put(party.Partymembers.get(player.getUniqueId()), newleader.getUniqueId());
									}else{
										player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + fw.utilitiesconfig.getString("newpartyleader.notinparty"));
									}
								}else{
									player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + fw.utilitiesconfig.getString("newpartyleader.wrongparty"));
								}
							}else{
								player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + fw.utilitiesconfig.getString("newpartyleader.notfound"));
							}
						}
					}
				}else
				if(args[0].equalsIgnoreCase("invite")){
					if(party.Partymembers.get(player.getUniqueId()) != null){
						if(party.Partyleaders.get(party.Partymembers.get(player.getUniqueId())).compareTo(player.getUniqueId()) == 0){
								Player t = Bukkit.getPlayer(args[1]);
								if(t!= null){
								String message = fw.utilitiesconfig.getString("Partyinvite.leader");
								message = message.replace("%player%", t.getName());
								message = message.replace("%partyname%", party.Partymembers.get(player.getUniqueId()));
								player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + message);
								party.inviteplayer(t, party.Partymembers.get(player.getUniqueId()), player.getName());
								}
						}
					}
				}else
				if(args[0].equalsIgnoreCase("accept")){
					if(party.invites.get(player.getUniqueId()) != null){
						party.addmember(party.invites.get(player.getUniqueId()), player.getUniqueId());
						return true;
					}else{
						player.sendMessage(fw.utilitiesconfig.getString("Partyinvite.no-open"));
						return true;
					}
				}else if(args[0].equalsIgnoreCase("info")){
					if(args.length == 1){
						if(party.Partymembers.get(player.getUniqueId()) != null){
							party.partyinfo(party.Partymembers.get(player.getUniqueId()), player);
						}else{
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + "You must be in a party to use this command, or use it as /party info <partyname>.");
						}
					}else{
						if(party.Partyswithmembers.get(args[1]) != null){
							party.partyinfo(args[1], player);
						}else{
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + "This party doesn't exists.");
						}
					}
				}else
				if(args[0].equalsIgnoreCase("chat")){
					if(party.Partymembers.get(player.getUniqueId()) != null){
						if(party.Partychat.contains(player.getUniqueId())){
						party.Partychat.remove(player.getUniqueId());
						}else{
						party.Partychat.add(player.getUniqueId());	
						}
					}else{
						player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + "You must be in a party to use this command.");
					}
				return true;
			}else if(args[0].equalsIgnoreCase("disband")){
				if(party.Partymembers.get(player.getUniqueId()) != null){
					if(party.Partyleaders.get(party.Partymembers.get(player.getUniqueId())).compareTo(player.getUniqueId()) == 0){
						party.Partyswithmembers.remove(party.Partymembers.get(player.getUniqueId()));
						String name = party.Partymembers.get(player.getUniqueId());
						ArrayList<UUID> members = new ArrayList<UUID>();
						members.addAll(party.Partymembers.keySet());
						for(UUID member : members){
							if(party.Partymembers.get(member).equals(name)){
								party.Partymembers.remove(member);
								Player m = Bukkit.getPlayer(member);
								if(m != null){
									m.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + fw.utilitiesconfig.getString("Partydisband"));

								}
							}
						}
						plugin.database.deleteparty(name);
						party.Partyleaders.remove(name);
					}
				}
			}else if(args[0].equalsIgnoreCase("leave")){
				if(party.Partymembers.get(player.getUniqueId()) != null){
					if(party.Partyleaders.get(party.Partymembers.get(player.getUniqueId())).compareTo(player.getUniqueId()) != 0){
						player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + fw.utilitiesconfig.getString("Partyleave.member"));
												party.Partyswithmembers.get(party.Partymembers.get(player.getUniqueId())).remove(player.getUniqueId());
												party.Partymembers.remove(player.getUniqueId());
												party.partymatchmaking.remove(player.getUniqueId());

												return true;
					}else{
						player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + fw.utilitiesconfig.getString("Partyleave.leader"));
					}
				}
			}else if(args[0].equalsIgnoreCase("match")){
				if(party.partymatchmaking.get(player.getUniqueId())){
					party.partymatchmaking.put(player.getUniqueId(), false);
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] Party matchmaking turned off.");

				}else{
					party.partymatchmaking.put(player.getUniqueId(), true);
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] Party matchmaking turned on");

				}
			}
			
			else{
				player.sendMessage(ChatColor.RED + "Please use /party <create/kick/invite/accept/chat/leave/disband/match>.");
			}
			}else{
				player.sendMessage(ChatColor.RED + "Please use /party <create/kick/invite/accept/chat/leave/disband/match>.");
			}
			return true;
		}else if(cmd.getName().equalsIgnoreCase("event")){
			if(user.has("Ysir.event")){
			if(args.length == 2){
				if(args[0].equalsIgnoreCase("boat")){
					if(args[1].equalsIgnoreCase("create")){
						boat.eventCreate(player);
					}else if(args[1].equalsIgnoreCase("edit")){
						boat.eventalledit(player);
					}else {
						player.sendMessage(ChatColor.RED + "Please use /event <boat/cart/dragon> <create/edit>.");
						return true;
					}
				}else if(args[0].equalsIgnoreCase("cart")){
					if(args[1].equalsIgnoreCase("create")){
						cart.eventCreate(player);
					}else if(args[1].equalsIgnoreCase("edit")){
						cart.eventalledit(player);
					}else {
						player.sendMessage(ChatColor.RED + "Please use /event <boat/cart/dragon> <create/edit>.");
						return true;
						
					}
				}else if(args[0].equalsIgnoreCase("dragon")){
					if(args[1].equalsIgnoreCase("create")){
						dragon.eventCreate(player);
					}else if(args[1].equalsIgnoreCase("edit")){
						dragon.eventalledit(player);
					}else {
						player.sendMessage(ChatColor.RED + "Please use /event <boat/cart/dragon> <create/edit>.");
						return true;
					}
				}
			}else{
				player.sendMessage(ChatColor.RED + "Please use /event <boat/cart/dragon> <create/edit>.");
				return true;
			}
			return true;
		}
		}else if(cmd.getName().equalsIgnoreCase("quest")){
			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("active")){
					questers.ActiveQuest(player);
					return true;
				}else if(args[0].equalsIgnoreCase("completed")){
					questers.CompletedQuest(player);
					return true;
				}else if(args[0].equalsIgnoreCase("rewarded")){
					questers.RewardedQuest(player);
					return true;
				}
			}else{
				player.sendMessage(ChatColor.RED + "Please use /quest <active/completed/rewared>.");
				return true;
			}
			
			return true;
		}
		}
		return false;
	}
}
