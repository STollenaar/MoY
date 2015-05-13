package MoY.tollenaar.stephen.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import MoY.tollenaar.stephen.CEvents.ProgEvent;
import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.PlayerInfo.Playerinfoding;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.Quests.QuestsServerSide;

public class CommandsPlayerinfo implements CommandExecutor {

	private Playerinfoding playerinfo;
	private QuestsServerSide questers;

	public CommandsPlayerinfo(MoY instance) {
		this.playerinfo = instance.playerinfo;
		this.questers = instance.questers;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			PermissionUser user = PermissionsEx.getUser(player);
			if (cmd.getName().equalsIgnoreCase("skill")) {
				if (args.length == 0) {
					playerinfo.playerstatsinv(player);
					return true;
				} else {
					if (args.length == 2) {
						try {
							int newlvl = Integer.parseInt(args[1]);
							if (args[0].equalsIgnoreCase("wood")) {
								Playerstats.woodcutting.put(
										player.getUniqueId(), newlvl);
							} else if (args[0].equalsIgnoreCase("mining")) {
								Playerstats.mining.put(player.getUniqueId(),
										newlvl);
							} else if (args[0].equalsIgnoreCase("fishing")) {
								Playerstats.fishing.put(player.getUniqueId(),
										newlvl);
							} else if (args[0].equalsIgnoreCase("cooking")) {
								Playerstats.cooking.put(player.getUniqueId(),
										newlvl);
							} else if (args[0].equalsIgnoreCase("smelting")) {
								Playerstats.smelting.put(player.getUniqueId(),
										newlvl);
							} else if (args[0].equalsIgnoreCase("strength")) {
								Playerstats.Strengthscore.put(
										player.getUniqueId(), newlvl);
							} else if (args[0].equalsIgnoreCase("dex")) {
								Playerstats.Dexscore.put(player.getUniqueId(),
										newlvl);
							}
						} catch (NumberFormatException ex) {
							player.sendMessage("last argument wasn't a number.  Please use /skill <wood/mining/fishing/cooking/smelting> <lvl>.");
						}
						return true;
					} else {
						player.sendMessage("Please use /skill <wood/mining/fishing/cooking/smelting> <lvl>.");
						return true;
					}
				}
			} else if (cmd.getName().equalsIgnoreCase("lvl")) {
				if (user.has("Ysir.stafflvl")) {
					if (args.length != 0) {
						if (args[0].equalsIgnoreCase("up")) {
							Playerstats.level
									.put(player.getUniqueId(),
											Playerstats.level.get(player
													.getUniqueId()) + 1);
						} else if (args[0].equals("down")) {
							Playerstats.level
									.put(player.getUniqueId(),
											Playerstats.level.get(player
													.getUniqueId()) - 1);
						} else {
							try {
								int lvl = Integer.parseInt(args[0]);
								Playerstats.level
										.put(player.getUniqueId(), lvl);
							} catch (NumberFormatException ex) {
								ex.printStackTrace();
							}
						}

						return true;
					} else {
						player.sendMessage("/lvl <parameter>");
						return true;
					}
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
					player.sendMessage(ChatColor.RED + "Please use /quest <active/completed/rewarded>.");
					return true;
				}
				
				return true;
			}
			
		}else{
			if(cmd.getName().equalsIgnoreCase("skill")){
				if(args.length == 3){
					try {
						Player player = Bukkit.getPlayer(args[2]);
						int newlvl = Integer.parseInt(args[1]);
						int type = 0;
						if (args[0].equalsIgnoreCase("wood")) {
						type =1;
						} else if (args[0].equalsIgnoreCase("mining")) {
						type = 2;
						} else if (args[0].equalsIgnoreCase("fishing")) {
						type = 5;
						} else if (args[0].equalsIgnoreCase("cooking")) {
						type = 3;
						} else if (args[0].equalsIgnoreCase("smelting")) {
						type = 4;
						} else if (args[0].equalsIgnoreCase("strength")) {
							Playerstats.Strengthscore.put(
									player.getUniqueId(), newlvl);
						} else if (args[0].equalsIgnoreCase("dex")) {
							Playerstats.Dexscore.put(player.getUniqueId(),
									newlvl);
						}else if (args[0].equalsIgnoreCase("level")){
							type = 6;
						}
						if(type != 0){
							ProgEvent e = new ProgEvent(player.getUniqueId(), type, Integer.parseInt(args[1]), true);
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
					} catch (NumberFormatException ex) {
						sender.sendMessage("last argument wasn't a number.  Please use /skill <wood/mining/fishing/cooking/smelting> <lvl>.");
					}
					return true;
				}
			}
		}
			return false;
	}
}
