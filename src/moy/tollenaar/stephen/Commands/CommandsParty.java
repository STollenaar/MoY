package moy.tollenaar.stephen.Commands;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;






import moy.tollenaar.stephen.Files.Filewriters;
import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.PlayerInfo.Party;

import com.earth2me.essentials.OfflinePlayer;

public class CommandsParty implements CommandExecutor{
	 private MoY plugin;
	 private Party party;
	 private Filewriters fw;
	 
	
	public CommandsParty(MoY instance){
		this.plugin = instance;
		this.party = instance.party;
		this.fw = instance.fw;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player){
			 Player player = (Player)sender;
				if(cmd.getName().equalsIgnoreCase("party")){
					
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
				}
		
		}
		return false;
	}
}
