package MoY.tollenaar.stephen.CEvents;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.messages.InfoBar;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public final class ProgListener implements Listener {

	private InfoBar info;

	public ProgListener(MoY instance) {
		this.info = new InfoBar(instance);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
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
			case 3:
				currentlvl = Playerstats.cooking.get(event.getPlayer());
				currentprog = Playerstats.cookingprog.get(event.getPlayer());
				break;
			case 4:
				currentlvl = Playerstats.smelting.get(event.getPlayer());
				currentprog = Playerstats.smeltingprog.get(event.getPlayer());
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
				}else{
					currentprog+= xp;
				}
			}

			switch (event.getType()) {
			case 1:
				Playerstats.woodcutting.put(event.getPlayer(), currentlvl);
				Playerstats.woodcuttingprog.put(event.getPlayer(), currentprog);
				break;
			case 2:
				Playerstats.mining.put(event.getPlayer(), currentlvl);
				Playerstats.miningprog.put(event.getPlayer(), currentprog);
				break;
			case 3:
				Playerstats.cooking.put(event.getPlayer(), currentlvl);
				Playerstats.cookingprog.put(event.getPlayer(), currentprog);
				break;
			case 4:
				Playerstats.smelting.put(event.getPlayer(), currentlvl);
				Playerstats.smeltingprog.put(event.getPlayer(), currentprog);
				break;
			default:
				break;
			}
			ArrayList<String> groups = new ArrayList<String>();
			for (String in : user.getGroupNames()) {
				groups.add(in);
			}
			if (!groups.contains("Peasant") && event.getType() < 3) {
				dubbledrop(currentlvl, event.getBlock());
			}
		}
		if (currentlvl != 50 && event.getBlock() != null) {
			info.makemessage(event.getPlayer(), event.getType(),
					event.getBlock());
		}else if(currentlvl != 50){
			info.makemessage(event.getPlayer(), event.getType());
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
