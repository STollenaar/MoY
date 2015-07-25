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
import MoY.tollenaar.stephen.PlayerInfo.Playerinfo;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.Quests.QuestsServerSide;

public class CommandsPlayerinfo implements CommandExecutor {

	private Playerinfo playerinfo;
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
			Playerstats p = playerinfo.getplayer(player.getUniqueId());
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
								p.setWoodcutting(newlvl);
							} else if (args[0].equalsIgnoreCase("mining")) {
								p.setMining(newlvl);
							} else if (args[0].equalsIgnoreCase("fishing")) {
								p.setFishing(newlvl);
							} else if (args[0].equalsIgnoreCase("cooking")) {
								p.setCooking(newlvl);
							} else if (args[0].equalsIgnoreCase("smelting")) {
								p.setSmelting(newlvl);
							} else if (args[0].equalsIgnoreCase("strength")) {
								p.setStrength(newlvl);
							} else if (args[0].equalsIgnoreCase("dex")) {
								p.setDex(newlvl);
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
							p.setLevel(p.getLevel() + 1);
						} else if (args[0].equals("down")) {
							p.setLevel(p.getLevel() - 1);
						} else {
							try {
								int lvl = Integer.parseInt(args[0]);
								p.setLevel(lvl);
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
			} else if (cmd.getName().equalsIgnoreCase("quest")) {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("active")) {
						questers.ActiveQuest(player);
						return true;
					} else if (args[0].equalsIgnoreCase("completed")) {
						questers.CompletedQuest(player);
						return true;
					} else if (args[0].equalsIgnoreCase("rewarded")) {
						questers.RewardedQuest(player);
						return true;
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "Please use /quest <active/completed/rewarded>.");
					return true;
				}

				return true;
			}

		} else {
			if (cmd.getName().equalsIgnoreCase("skill")) {
				
				if (args.length == 3) {
					try {
						Player player = Bukkit.getPlayer(args[2]);
						Playerstats p = playerinfo.getplayer(player.getUniqueId());
						int xp = Integer.parseInt(args[1]);
						int type = 0;
						if (args[0].equalsIgnoreCase("wood")) {
							type = 1;
						} else if (args[0].equalsIgnoreCase("mining")) {
							type = 2;
						} else if (args[0].equalsIgnoreCase("fishing")) {
							type = 5;
						} else if (args[0].equalsIgnoreCase("cooking")) {
							type = 3;
						} else if (args[0].equalsIgnoreCase("smelting")) {
							type = 4;
						} else if (args[0].equalsIgnoreCase("strength")) {
							type = -1;
							xp = 1;
						} else if (args[0].equalsIgnoreCase("dex")) {
							type = -2;
							xp = 1;
						} else if (args[0].equalsIgnoreCase("level")) {
							type = 6;
						}
						if (type != 0) {
							ProgEvent e = new ProgEvent(player.getUniqueId(),
									type, xp, true);
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
						playerinfo.saveplayerdata(p);
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
