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
import MoY.tollenaar.stephen.PlayerInfo.Playerinfo;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.messages.InfoBar;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public final class ProgListener implements Listener {

	private InfoBar info;
	private Playerinfo playerinfo;

	public ProgListener(MoY instance) {
		this.info = new InfoBar(instance);
		this.playerinfo = instance.playerinfo;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void MiningListener(ProgEvent event) {
		Player player = Bukkit.getPlayer(event.getPlayer());
		Playerstats p = playerinfo.getplayer(player.getUniqueId());
		PermissionUser user = PermissionsEx.getUser(player);
		int currentlvl = 0;
		if (event.getType() > 0) {
			int xp = event.getXp();

			int currentprog;

			switch (event.getType()) {
			case 1:
				currentlvl = p.getWoodcutting();
				currentprog = p.getWoodcuttingprog();
				break;
			case 2:
				currentlvl = p.getMining();
				currentprog = p.getMiningprog();
				break;
			case 3:
				currentlvl = p.getCooking();
				currentprog = p.getCookingprog();
				break;
			case 4:
				currentlvl = p.getSmelting();
				currentprog = p.getSmeltingprog();
				break;
			case 5:
				currentlvl = p.getFishing();
				currentprog = p.getFishingprog();
				break;
			case 6:
				currentlvl = p.getLevel();
				currentprog = p.getLevelprog();
				break;
			default:
				return;
			}
			if (currentlvl != 50 && event.getType() != 6) {
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
				} else {
					currentprog += xp;
				}
			} else if (event.getType() == 6 && currentlvl != 140) {
				if (currentprog + xp >= currentlvl * 15) {
					currentlvl++;
					while (currentprog < currentlvl * 15 && xp > 0) {
						currentprog++;
						xp--;
					}
					p.setAbility(p.getAbility() + 1);
					currentprog = 0;
					if (xp > 0) {
						currentprog = xp;
					}
				} else {
					currentprog += xp;
				}
			}

			switch (event.getType()) {
			case 1:
				p.setWoodcutting(currentlvl);
				p.setWoodcuttingprog(currentprog);
				break;
			case 2:
				p.setMining(currentlvl);
				p.setMiningprog(currentprog);
				break;
			case 3:
				p.setCooking(currentlvl);
				p.setCookingprog(currentprog);
				break;
			case 4:
				p.setSmelting(currentlvl);
				p.setSmeltingprog(currentprog);
				break;
			case 5:
				p.setFishing(currentlvl);
				p.setFishingprog(currentprog);
				break;
			case 6:
				p.setLevel(currentlvl);
				p.setLevelprog(currentprog);
				break;
			default:
				return;
			}
			ArrayList<String> groups = new ArrayList<String>();
			for (String in : user.getGroupNames()) {
				groups.add(in);
			}
			if (!groups.contains("Peasant") && event.getType() < 3) {
				dubbledrop(currentlvl, event.getBlock());
			}
		}

		if (!event.getQ()) {
			if (currentlvl != 50 && event.getBlock() != null) {
				info.makemessage(event.getPlayer(), event.getType(),
						event.getBlock());
			} else if (currentlvl != 50 || event.getType() == 6) {
				info.makemessage(event.getPlayer(), event.getType());
			}
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
