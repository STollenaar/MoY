package com.tollenaar.stephen.MoYCEvents;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.tollenaar.stephen.MoYMistsOfYsir.MoY;
import com.tollenaar.stephen.MoYPlayerInfo.Playerstats;
import com.tollenaar.stephen.MoYmessages.InfoBar;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ProgListener implements Listener {

	private InfoBar info;

	public ProgListener(MoY instance) {
		this.info = new InfoBar(instance);
	}

	@SuppressWarnings("deprecation")
	public void MiningListener(MiningProgEvent event) {
		Player player = Bukkit.getPlayer(event.getPlayer());
		PermissionUser user = PermissionsEx.getUser(player);
		int currentlvl = 0;
		if (event.getType() > 0) {
			int xp = event.getXp();

			int currentprog;

			switch (event.getType()) {
			case 1:
				currentlvl = Playerstats.woodcutting.get(event.getPlayer());
				currentprog = Playerstats.woodcuttingprog
						.get(event.getPlayer());
				break;
			case 2:
				currentlvl = Playerstats.mining.get(event.getPlayer());
				currentprog = Playerstats.miningprog.get(event.getPlayer());
				break;
			default:
				currentprog = 0;
				break;
			}
			if (currentlvl != 50) {
				if (currentprog + xp >= currentlvl * 10) {
					currentlvl++;
					while (currentprog < currentlvl * 10 && xp > 0) {
						currentprog++;
						xp--;
					}
					currentprog = 0;
					if (xp > 0) {
						currentprog = xp;
					}
				}
			}

			switch (event.getType()) {
			case 1:
				Playerstats.woodcutting.put(event.getPlayer(), currentlvl);
				Playerstats.woodcuttingprog.put(event.getPlayer(), currentprog);
				break;
			case 2:
				Playerstats.mining.put(event.getPlayer(), currentlvl);
				Playerstats.woodcuttingprog.put(event.getPlayer(), currentprog);
				break;
			default:
				break;
			}
			ArrayList<String> groups = new ArrayList<String>();
			for (String in : user.getGroupNames()) {
				groups.add(in);
			}
			if (!groups.contains("Peasant")) {
				dubbledrop(currentlvl, event.getBlock());
			}
		}
		if (currentlvl != 50) {
			info.makemessage(event.getPlayer(), event.getType(),
					event.getBlock());
		}
	}

	private void dubbledrop(int lvl, Block block) {
		Random r = new Random();
		int chance = lvl / 10 * 2;
		if (chance == 0) {
			chance = 1;
		}
		if (r.nextInt(100) <= chance) {
			block.getDrops().add((ItemStack) block.getDrops().toArray()[0]);
		}
	}
}
