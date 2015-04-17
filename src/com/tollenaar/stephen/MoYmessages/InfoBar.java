package com.tollenaar.stephen.MoYmessages;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;

import com.tollenaar.stephen.MoYMistsOfYsir.MoY;
import com.tollenaar.stephen.MoYPlayerInfo.Playerstats;
import com.tollenaar.stephen.MoYSkillsStuff.SkillLvls;

public class InfoBar {

	private static HashMap<UUID, Message> playermessage = new HashMap<UUID, Message>();
	private static MoY plugin;

	@SuppressWarnings("static-access")
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
	
	public void makemessage(UUID player, int type, Block block){
		int prog;
		int lvl;
		switch(type){
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
		}
		String message = message(type, prog, lvl, block);
		playermessage.put(player, new Message(plugin, message, player));
	}

	/**
	 * 
	 * @param typ
	 *        -2 = error mining, -1 = error wood,    1 = wood, 2 = mining
	 * @param prog
	 * @param lvl
	 * @return
	 */
	private static String message(int type, int prog, int lvl, Block block) {
		String message;
		switch (type) {
		case -1:
			message = ChatColor.RED + "Your Woodcutting is not high enough. It has to be at least level " + SkillLvls.GetSkill(block);
			break;
		case -2:
			message = ChatColor.RED + "Your Mining is not high enough. It has to be at least level " + SkillLvls.GetSkill(block);
			break;
		case 1:
			message = ChatColor.GRAY + "Progression Woodcutting: "
					+ ChatColor.GREEN + prog + "/" + lvl;
			break;

		case 2:
			message = ChatColor.GRAY + "Progression Mining: " + ChatColor.GREEN
					+ prog + "/" + lvl;

			break;
		default:
			message = "";
			break;
		}
		return message;
	}


}
