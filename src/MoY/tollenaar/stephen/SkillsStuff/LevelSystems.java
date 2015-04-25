package MoY.tollenaar.stephen.SkillsStuff;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import MoY.tollenaar.stephen.CEvents.ProgEvent;
import MoY.tollenaar.stephen.Files.Filewriters;
import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.MistsOfYsir.Party;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;

public class LevelSystems implements Listener {
	private Filewriters fw;
	private Party party;

	@EventHandler
	public void onmonsterdeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Monster
				|| event.getEntity() instanceof Slime
				|| event.getEntity() instanceof Ghast
				|| (event.getEntity() instanceof Projectile && ((Projectile) event
						.getEntity()).getShooter() instanceof Player)) {
			Player player = event.getEntity().getKiller();
			if (player != null) {

				int playerlvl = Playerstats.level.get(player.getUniqueId());
				int xp;
				if (event.getEntity() instanceof Skeleton
						|| event.getEntity() instanceof Zombie) {
					xp = skelzomblvl((Monster) event.getEntity());
				} else {
					xp = lvlmod(playerlvl);
				}

				// normal xp system
				
				partyrewards(player, xp);
					ProgEvent e = new ProgEvent(player.getUniqueId(), 6, xp, false);
					Bukkit.getServer().getPluginManager().callEvent(e);
			}
		}
	}

	private void partyrewards(Player player, int xp) {
		if (party.Partymembers.get(player.getUniqueId()) != null) {
			Set<UUID> memberspartu = party.Partyswithmembers
					.get(party.Partymembers.get(player.getUniqueId()));
			ArrayList<UUID> members = new ArrayList<UUID>();
			members.addAll(memberspartu);
			for (UUID member : members) {
				if (member != player.getUniqueId()) {
					Player victim = Bukkit.getPlayer(member);
					if (victim != null) {

						int memberprog = Playerstats.levelprog.get(member);
						int memberlvl = Playerstats.level.get(member);
						int memberneeds = memberlvl * 15;
						if (memberprog + xp >= memberneeds) {
							if (memberlvl + 1 < 140) {
								int rest = 0;
								if (memberprog + xp > memberneeds) {
									rest = (memberprog + xp - memberneeds);
								} else {
									rest = 0;
								}
								Player mp = Bukkit.getPlayer(member);

								memberprog = rest;
								memberlvl++;
								Playerstats.levelprog.put(member, memberprog);
								Playerstats.level.put(member, memberlvl);
								if (mp != null) {
									String up = fw.GetUtilityLine("Partylvlup");
									up = up.replace("%playername%",
											player.getName());
									up = up.replace("%lvl%",
											Integer.toString(memberlvl));
									mp.sendMessage(ChatColor.DARK_PURPLE + "["
											+ ChatColor.GOLD + "YParty"
											+ ChatColor.DARK_PURPLE + "] "
											+ ChatColor.AQUA + up);
								}
							}
						} else {
							Playerstats.levelprog.put(member, memberprog + xp);
						}
					}
				}
			}
		}
	}

	

	private int skelzomblvl(Monster ent) {
		Material chest = ent.getEquipment().getChestplate().getType();
		Material legg = ent.getEquipment().getLeggings().getType();
		Material boots = ent.getEquipment().getLeggings().getType();

		if (chest == Material.AIR) {
			return 1;
		} else if (chest == Material.LEATHER_CHESTPLATE && legg == Material.AIR) {
			return 2;
		} else if (chest == Material.LEATHER_CHESTPLATE
				&& legg == Material.LEATHER_LEGGINGS && boots == Material.AIR) {
			return 3;
		} else if (chest == Material.LEATHER_CHESTPLATE
				&& legg == Material.LEATHER_LEGGINGS
				&& boots == Material.LEATHER_BOOTS) {
			return 4;
		} else if (chest == Material.CHAINMAIL_CHESTPLATE
				&& legg == Material.LEATHER_LEGGINGS
				&& boots == Material.LEATHER_BOOTS) {
			return 5;
		} else if (chest == Material.CHAINMAIL_CHESTPLATE
				&& legg == Material.CHAINMAIL_LEGGINGS
				&& boots == Material.LEATHER_BOOTS) {
			return 6;
		} else if (chest == Material.CHAINMAIL_CHESTPLATE
				&& legg == Material.CHAINMAIL_LEGGINGS
				&& boots == Material.CHAINMAIL_BOOTS) {
			return 7;
		} else if (chest == Material.IRON_CHESTPLATE
				&& legg == Material.CHAINMAIL_LEGGINGS
				&& boots == Material.CHAINMAIL_BOOTS) {
			return 8;
		} else if (chest == Material.IRON_CHESTPLATE
				&& legg == Material.IRON_LEGGINGS
				&& boots == Material.CHAINMAIL_BOOTS) {
			return 9;
		} else {
			return 10;
		}

	}

	private int lvlmod(int playerlvl) {
		if ((playerlvl * 15) / (Math.ceil(playerlvl / 10)) > 10) {
			return 10;
		} else {
			return (int) ((playerlvl * 15) / (Math.ceil(playerlvl / 10)));
		}
	}

	public LevelSystems(MoY instance) {
		this.fw = instance.fw;
		this.party = instance.party;
	}
}
