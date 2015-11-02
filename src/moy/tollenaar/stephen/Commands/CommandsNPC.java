package moy.tollenaar.stephen.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.Quests.QuestsServerSide;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CommandsNPC implements CommandExecutor {
	private QuestsServerSide questers;

	public CommandsNPC(MoY instance) {
		this.questers = instance.qserver;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			PermissionUser user = PermissionsEx.getUser(player);
			// QNPC HANDLER

			if (cmd.getName().equalsIgnoreCase("qnpc")) {
				if (user.has("Ysir.npcadjuster")) {
					if (args.length >= 1) {
						if (args[0].equalsIgnoreCase("create")) {
							if (args.length == 3) {
								questers.spawnNpc(player.getLocation(),
										args[1], -1, args[2]);
								return true;
							} else {
								player.sendMessage(ChatColor.RED
										+ "Please use /qnpc <create><npcname><npcskinname>.");
								return true;
							}
						}

						if (args[0].equals("tool")) {
							Inventory playerinv = player.getInventory();
							ItemStack specialtool = new ItemStack(
									Material.DIAMOND_HOE);
							ItemMeta toolmeta = specialtool.getItemMeta();
							List<String> tem = new ArrayList<String>();
							tem.add("NPC adjustment Tool");
							toolmeta.setLore(tem);
							toolmeta.setDisplayName("NPC TOOL");
							specialtool.setItemMeta(toolmeta);
							playerinv.addItem(specialtool);
							return true;
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "Please use /qnpc <tool/create>.");
						return true;

					}
				} else {
					return false;
				}
			}
		}
		return false;
	}

}