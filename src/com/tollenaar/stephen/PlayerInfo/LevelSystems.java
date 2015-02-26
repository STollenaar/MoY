package com.tollenaar.stephen.PlayerInfo;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Tree;

import com.tollenaar.stephen.MistsOfYsir.Filewriters;
import com.tollenaar.stephen.MistsOfYsir.MoY;
import com.tollenaar.stephen.MistsOfYsir.Party;

public class LevelSystems implements Listener {
	MoY plugin;
	Filewriters fw;
	Party party;

	public void onmonsterdeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Monster
				|| event.getEntity() instanceof Slime
				|| event.getEntity() instanceof Ghast
				|| (event.getEntity() instanceof Projectile && ((Projectile) event
						.getEntity()).getShooter() instanceof Monster)) {
			Player player = event.getEntity().getKiller();
			if (player != null) {

				int playerlvl = Playerstats.level.get(player.getUniqueId());
				int lvlprog = Playerstats.levelprog.get(player.getUniqueId());
				int xp;
				if (event.getEntity() instanceof Skeleton
						|| event.getEntity() instanceof Zombie) {
					xp = skelzomblvl((Monster) event.getEntity());
				} else {
					xp = lvlmod(playerlvl);
				}

				// normal xp system
				if (lvlprog + xp < playerlvl * 15) {
					lvlprog += xp;
					Playerstats.levelprog.put(player.getUniqueId(), lvlprog);
				} else if (lvlprog + xp == playerlvl + 15) {
					if (playerlvl + 1 <= 140) {
						lvlprog = 0;
						playerlvl++;
						Playerstats.levelprog.put(player.getUniqueId(), 0);
						Playerstats.level.put(player.getUniqueId(), playerlvl);
					}
				} else {
					if (playerlvl + 1 <= 140) {
						lvlprog = lvlprog + xp - playerlvl * 15;
						playerlvl++;
						Playerstats.levelprog
								.put(player.getUniqueId(), lvlprog);
						Playerstats.level.put(player.getUniqueId(), playerlvl);
					}
				}
				partyrewards(player, xp);
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

	public int fishingxp(ItemStack item, int junk, int treasure) {
		if (item.getType() != Material.RAW_FISH) {
			if (junk == 1) {
				return 1;
			} else {
				return 5 * item.getAmount();
			}
		} else if (item.getDurability() == 0) {
			return 1 * item.getAmount();
		} else if (item.getDurability() == 1) {
			return 2 * item.getAmount();
		} else if (item.getDurability() == 2) {
			return 2 * item.getAmount();
		} else if (item.getDurability() == 3) {
			return 3 * item.getAmount();
		} else {
			return 1;
		}
	}

	public int miningxp(Block item) {
		if (item.getType() == Material.COAL_ORE) {
			return 2;
		} else if (item.getType() == Material.IRON_ORE) {
			return 4;
		} else if (item.getType() == Material.GOLD_ORE) {
			return 7;
		} else if (item.getType() == Material.LAPIS_ORE) {
			return 3;
		} else if (item.getType() == Material.REDSTONE_ORE) {
			return 2;
		} else if (item.getType() == Material.DIAMOND_ORE) {
			return 10;
		} else {
			return 1;
		}

	}

	public int woodxp(Block item, ArrayList<TreeSpecies> lowlvl,
			ArrayList<TreeSpecies> midlvl, ArrayList<TreeSpecies> midhighlvl,
			ArrayList<TreeSpecies> highlvl) {
		TreeSpecies log;
		if (item.getType() == Material.LOG) {
			log = ((Tree) item.getState().getData()).getSpecies();
		} else {
			@SuppressWarnings("deprecation")
			short data = item.getData();
			if (data == 0 || data == 4 || data == 8 || data == 12) {
				log = TreeSpecies.ACACIA;
			} else {
				log = TreeSpecies.DARK_OAK;
			}
		}
		if (lowlvl.contains(log)) {
			return 1;
		} else if (midlvl.contains(log)) {
			return 1;
		} else if (midhighlvl.contains(log)) {
			return 2;
		} else {
			return 3;
		}

	}

	public int smeltingxp(ItemStack item) {
		if (item.getType() == Material.GOLD_INGOT) {
			return 3;
		} else if (item.getType() == Material.IRON_INGOT) {
			return 2;
		} else {
			return 1;
		}
	}

	public int succes(int playerlvl, ItemStack item,
			ArrayList<Material> lowlvl, ArrayList<Material> midlowlvl,
			ArrayList<Material> midlvl, ArrayList<Material> midhighlvl,
			ArrayList<Material> highlvl) {
		if (playerlvl <= 10) {
			if (lowlvl.contains(item.getType())) {
				return 82;
			} else if (midlowlvl.contains(item.getType())) {
				return 20;
			} else if (midlvl.contains(item.getType())) {
				return 17;
			} else if (midhighlvl.contains(item.getType())) {
				return 14;
			} else {
				return 11;
			}
		} else if (playerlvl <= 20) {
			if (midlowlvl.contains(item.getType())) {
				return 82;
			} else if (midlvl.contains(item.getType())) {
				return 20;
			} else if (midhighlvl.contains(item.getType())) {
				return 17;
			} else {
				return 14;
			}
		} else if (playerlvl <= 30) {
			if (midlvl.contains(item.getType())) {
				return 78;
			} else if (midhighlvl.contains(item.getType())) {
				return 20;
			} else {
				return 17;
			}
		} else if (playerlvl <= 40) {
			if (midhighlvl.contains(item.getType())) {
				return 82;
			} else {
				return 20;
			}
		} else {
			return 78;
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
		this.plugin = instance;
		this.fw = instance.fw;
		this.party = instance.party;
	}
}
