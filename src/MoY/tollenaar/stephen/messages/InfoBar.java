package MoY.tollenaar.stephen.messages;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.SkillsStuff.SkillLvls;

public class InfoBar {

	private static HashMap<UUID, Message> playermessage = new HashMap<UUID, Message>();
	private MoY plugin;

	public InfoBar(MoY instance) {
		this.plugin = instance;
	}

	public void cancelAll() {
		for (UUID pl : playermessage.keySet()) {
			try {
				playermessage.get(pl).cancel();
			} catch (IllegalStateException ex) {
				continue;
			}
		}
	}

	public void makemessage(UUID player, int type, Block block) {

		int prog;
		int lvl;
		switch (type) {
		case 1:
			prog = Playerstats.woodcuttingprog.get(player);
			lvl = Playerstats.woodcutting.get(player);
			break;
		case 2:
			prog = Playerstats.miningprog.get(player);
			lvl = Playerstats.mining.get(player);
			break;
		default:
			prog = 0;
			lvl = 0;
			break;
		}
		String message = message(type, prog, lvl, block);
		playermessage.put(player, new Message(plugin, message, player));
	}

	public void makemessage(UUID player, int type) {
		int prog;
		int lvl;
		switch (type) {
		case 3:
			lvl = Playerstats.cooking.get(player);
			prog = Playerstats.cookingprog.get(player);
			break;
		case -3:
			lvl = Playerstats.cooking.get(player);
			prog = Playerstats.cookingprog.get(player);
			break;
		case 4:
			lvl = Playerstats.smelting.get(player);
			prog = Playerstats.smeltingprog.get(player);
			break;
		case 5:
			lvl = Playerstats.fishing.get(player);
			prog = Playerstats.fishingprog.get(player);
			break;
		case 6:
			lvl = Playerstats.level.get(player);
			prog = Playerstats.levelprog.get(player);
			break;
		default:
			prog = 0;
			lvl = 0;
			break;
		}
		String message = message(type, prog, lvl);
		playermessage.put(player, new Message(plugin, message, player));
	}

	/**
	 * 
	 * @param typ
	 *            -2 = error mining, -1 = error wood, 1 = wood, 2 = mining, 3 =
	 *            cook succes, -3 = cook failure, 4 = smelting succes
	 * @param prog
	 * @param lvl
	 * @return
	 */
	private static String message(int type, int prog, int lvl, Block block) {
		String message;
		switch (type) {
		case -1:
			message = ChatColor.RED
					+ "Your Woodcutting is not high enough. It has to be at least level "
					+ SkillLvls.GetSkill(block).getLvl();
			break;
		case -2:
			message = ChatColor.RED
					+ "Your Mining is not high enough. It has to be at least level "
					+ SkillLvls.GetSkill(block).getLvl();
			break;
		case 1:
			message = ChatColor.GRAY + "Progression Woodcutting: "
					+ ChatColor.GREEN + prog + "/" + lvl * 10;
			break;

		case 2:
			message = ChatColor.GRAY + "Progression Mining: " + ChatColor.GREEN
					+ prog + "/" + lvl * 10;

			break;
		default:
			message = "";
			break;
		}
		return message;
	}

	private static String message(int type, int prog, int lvl) {
		String message;
		switch (type) {
		case 3:
			message = ChatColor.GRAY
					+ "Cooking succesful, progress increased: "
					+ ChatColor.GREEN + prog + "/" + lvl * 10;
			break;
		case -3:
			message = ChatColor.GRAY + "Cooking failed, progress stayed: "
					+ ChatColor.GREEN + prog + "/" + lvl * 10;
			break;
		case 4:
			message = ChatColor.GRAY
					+ "Smelting Succesful, progress increased: "
					+ ChatColor.GREEN + prog + "/" + lvl * 10;
			break;
		case 5:
			message = ChatColor.GRAY
					+ "Fishing Succesful, progress increased: "
					+ ChatColor.GREEN + prog + "/" + lvl * 10;
			break;
		case 6:
			message = ChatColor.GRAY + "Level Progress: " + ChatColor.GREEN
					+ prog + "/" + lvl * 15;
			break;
		default:
			message = "";
			break;
		}
		return message;
	}
}
