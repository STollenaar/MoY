package moy.tollenaar.stephen.messages;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;
import moy.tollenaar.stephen.SkillsStuff.SkillLvls;

public class InfoBar {

	private static HashMap<UUID, Message> playermessage = new HashMap<UUID, Message>();
	private MoY plugin;
	private Playerinfo playerinfo;
	public InfoBar(MoY instance) {
		this.plugin = instance;
		this.playerinfo = instance.playerinfo;
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
		Playerstats p = playerinfo.getplayer(player);
		int prog;
		int lvl;
		switch (type) {
		case 1:
			prog = p.getWoodcuttingprog();
			lvl = p.getWoodcutting();
			break;
		case 2:
			prog = p.getMiningprog();
			lvl =p.getMining();
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
		Playerstats p = playerinfo.getplayer(player);
		switch (type) {
		case 3:
			lvl = p.getCooking();
			prog = p.getCookingprog();
			break;
		case -3:
			lvl = p.getCooking();
			prog = p.getCookingprog();
			break;
		case 4:
			lvl =p.getSmelting();
			prog = p.getSmeltingprog();
			break;
		case 5:
			lvl = p.getFishing();
			prog = p.getFishingprog();
			break;
		case 6:
			lvl = p.getLevel();
			prog = p.getLevelprog();
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
