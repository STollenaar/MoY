package com.tollenaar.stephen.MoYSkillsStuff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;






import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

import com.tollenaar.stephen.MoYCEvents.MiningProgEvent;
import com.tollenaar.stephen.MoYMistsOfYsir.MoY;
import com.tollenaar.stephen.MoYPlayerInfo.Playerstats;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Skillimprovement implements Listener {
	private MoY plugin;
	private LevelSystems level;

	public HashSet<Block> miningblocksplaces = new HashSet<Block>();
	public HashSet<Block> woodblocksplaces = new HashSet<Block>();

	@EventHandler
	public void onblockplace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		PermissionUser user = PermissionsEx.getUser(player);
		if (user.has("MMOTEST")) {
			Block block = event.getBlock();
			if (SkillLvls.GetSkill(block).getOre() != null) {
				miningblocksplaces.add(block);
			} else {
				woodblocksplaces.add(block);
			}
		}
	}

	@EventHandler
	public void onPlayerfish(PlayerFishEvent event) {

		Player player = event.getPlayer();
		PermissionUser user = PermissionsEx.getUser(player);
		if (user.has("MMOTEST")) {

			if (event.getState() == State.CAUGHT_FISH) {
				fishcatch(player, event);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		PermissionUser user = PermissionsEx.getUser(player);
		if (user.has("MMOTEST")) {
			Block block = event.getBlock();
			if (!miningblocksplaces.contains(block)
					|| !woodblocksplaces.contains(block)) {
				if (SkillLvls.GetSkill(block).getOre() != null) {
					mining(event);
				} else {
					woodcutting(event);
				}
			} else if (miningblocksplaces.contains(block)) {
				miningblocksplaces.remove(block);
			} else {
				woodblocksplaces.remove(block);
			}
		}
	}

	private void woodcutting(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();

		MiningProgEvent e;
		if (SkillLvls.GetSkill(block).getLvl() <= Playerstats.woodcutting
				.get(player.getUniqueId())) {
			e = new MiningProgEvent(player.getUniqueId(), SkillLvls.GetSkill(
					block).getTree(), block, 1);
		} else {
			event.setCancelled(true);
			e = new MiningProgEvent(player.getUniqueId(), SkillLvls.GetSkill(
					block).getTree(), block, -1);

		}
		Bukkit.getPluginManager().callEvent(e);
	}

	private void mining(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();

		MiningProgEvent e;
		if (SkillLvls.GetSkill(block).getLvl() <= Playerstats.mining.get(player
				.getUniqueId())) {
			e = new MiningProgEvent(player.getUniqueId(), block.getType(),
					block, 2);
		} else {
			event.setCancelled(true);
			e = new MiningProgEvent(player.getUniqueId(), block.getType(),
					block, -2);
		}
		Bukkit.getPluginManager().callEvent(e);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSmelt(FurnaceSmeltEvent event) {
		Furnace furnace = (Furnace) event.getFurnace().getState();
		if (furnace.getLocation().getWorld().getName().equals("MMOTESTWORLD")) {
			if (furnace.getInventory().getSmelting().getType().isEdible() == true) {
				cooking(furnace, event);
			} else {
				smelting(furnace);
			}
		}
	}

	

	private void cooksucces(Player player, Furnace furnace, boolean succes,
			boolean online, ItemStack smelteditem) {
		
	}

	@EventHandler
	public void onFurnaceempty(InventoryClickEvent event) {
		if (event.getClickedInventory() != null
				&& event.getClickedInventory().getType() == InventoryType.FURNACE) {
			if(event.getSlot() == 0){
				if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR){
					ItemStack current = event.getCurrentItem();
					ItemStack mouse = event.getCursor();
					if(event.getAction() == InventoryAction.SWAP_WITH_CURSOR){
						
					}
				}
			}
		}
	}



	private void cooking(Furnace furnace, FurnaceSmeltEvent event) {
		

	}

	private void smelting(Furnace furnace) {
		
	}

	public Skillimprovement(MoY instance) {
		this.plugin = instance;
		this.level = instance.lvl;
	}


	private ArrayList<Integer> chance(int playerlvl) {
		ArrayList<Integer> chance = new ArrayList<Integer>();
		if (playerlvl < 11) {
			chance.add(55);
			chance.add(65);
			chance.add(70);
			chance.add(100);
		} else if (playerlvl < 21) {
			chance.add(46);
			chance.add(63);
			chance.add(72);
			chance.add(100);
		} else if (playerlvl < 31) {
			chance.add(42);
			chance.add(61);
			chance.add(71);
			chance.add(95);
			chance.add(100);
		} else if (playerlvl < 41) {
			chance.add(35);
			chance.add(59);
			chance.add(73);
			chance.add(92);
			chance.add(100);
		} else {
			chance.add(30);
			chance.add(58);
			chance.add(76);
			chance.add(85);
			chance.add(100);
		}

		return chance;
	}

	@SuppressWarnings("deprecation")
	private void fishcatch(Player player, PlayerFishEvent event) {
		int fishinglvl = Playerstats.fishing.get(player.getUniqueId());
		PermissionUser user = PermissionsEx.getUser(player);
		Item it = (Item) event.getCaught();
		ItemStack item = it.getItemStack();
		ArrayList<Material> junks = new ArrayList<Material>();
		{
			junks.add(Material.BOWL);
			junks.add(Material.FISHING_ROD);
			junks.add(Material.LEATHER);
			junks.add(Material.LEATHER_BOOTS);
			junks.add(Material.ROTTEN_FLESH);
			junks.add(Material.STICK);
			junks.add(Material.STRING);
			junks.add(Material.getMaterial(373));
			junks.add(Material.BONE);
			junks.add(Material.INK_SACK);
			junks.add(Material.TRIPWIRE_HOOK);
		}
		int junk = 0;
		int treasure = 0;

		if (item.getType() != Material.RAW_FISH) {
			if (junks.contains(item.getType())) {
				if (item.getType() == Material.FISHING_ROD) {
					if (item.getEnchantments() == null) {
						junk = 1;
					} else {
						treasure = 1;
					}
				}
			} else {
				treasure = 1;
			}
		}

		{
			ArrayList<Integer> chance = chance(fishinglvl);
			Random r = new Random();
			int n = r.nextInt(100);
			if (chance.size() == 4) {
				if (n <= chance.get(0)) {
					if (item.getType() != Material.RAW_FISH
							&& item.getDurability() != 0) {
						item = new ItemStack(Material.RAW_FISH);
					}
				} else if (n <= chance.get(1)) {
					if (item.getType() != Material.RAW_FISH
							&& item.getDurability() != 1) {
						item = new ItemStack(Material.RAW_FISH, 1, (short) 1);
					}
				} else if (n <= chance.get(2)) {
					if (item.getType() != Material.RAW_FISH
							&& (item.getDurability() != 2 || item
									.getDurability() != 3)) {
						int t = r.nextInt(1);
						if (t == 0) {
							item = new ItemStack(Material.RAW_FISH, 1,
									(short) 2);
						} else {
							item = new ItemStack(Material.RAW_FISH, 1,
									(short) 3);

						}
					}
				} else {
					if (!junks.contains(item.getType())) {
						item = new ItemStack(junks.get(6));
					}
				}
			} else {
				if (n <= chance.get(0)) {
					if (item.getType() != Material.RAW_FISH
							&& item.getDurability() != 0) {
						item = new ItemStack(Material.RAW_FISH);
					}
				} else if (n <= chance.get(1)) {
					if (item.getType() != Material.RAW_FISH
							&& item.getDurability() != 1) {
						item = new ItemStack(Material.RAW_FISH, 1, (short) 1);
					}
				} else if (n <= chance.get(2)) {
					if (item.getType() != Material.RAW_FISH
							&& (item.getDurability() != 2 || item
									.getDurability() != 3)) {
						int t = r.nextInt(1);
						if (t == 0) {
							item = new ItemStack(Material.RAW_FISH, 1,
									(short) 2);
						} else {
							item = new ItemStack(Material.RAW_FISH, 1,
									(short) 3);

						}
					}
				} else if (n <= chance.get(3)) {
					if (!junks.contains(item.getType())) {
						item = new ItemStack(junks.get(6));
					}
				}
			}
		}

		if (user.inGroup("Peasant")) {
			if (fishinglvl != 50) {
				if ((Playerstats.fishingprog.get(player.getUniqueId()) + level
						.fishingxp(item, junk, treasure)) >= (fishinglvl * 10)) {
					int smelting = Playerstats.fishing
							.get(player.getUniqueId());
					smelting++;
					int rest = 0;
					if ((Playerstats.fishingprog.get(player.getUniqueId()) + level
							.fishingxp(item, junk, treasure)) > (fishinglvl * 10)) {
						rest = (Playerstats.fishingprog.get(player
								.getUniqueId()) + level.fishingxp(item, junk,
								treasure))
								- (fishinglvl * 10);
					} else {
						rest = 0;
					}
					Playerstats.fishing.put(player.getUniqueId(), smelting);
					Playerstats.fishingprog.put(player.getUniqueId(), rest);
					player.sendMessage(ChatColor.DARK_PURPLE
							+ "["
							+ ChatColor.GOLD
							+ "YSkill"
							+ ChatColor.DARK_PURPLE
							+ "]"
							+ ChatColor.AQUA
							+ " Fishing has raised with 1 level. You are now level "
							+ smelting + ".");

				} else {
					Playerstats.fishingprog.put(player.getUniqueId(),
							Playerstats.fishingprog.get(player.getUniqueId())
									+ level.fishingxp(item, junk, treasure));
				}
			}
		} else {
			Random q = new Random();
			int n = q.nextInt(100);
			int chance = 0;
			int lvl = Playerstats.fishing.get(player.getUniqueId());
			if (lvl <= 10) {
				chance = 1;
			}
			if (lvl >= 11 && lvl <= 20) {
				chance = 4;
			}
			if (lvl >= 21 && lvl <= 30) {
				chance = 6;
			}
			if (lvl >= 31 && lvl <= 40) {
				chance = 7;
			}
			if (lvl >= 41 && lvl <= 50) {
				chance = 8;
			}
			if (n <= chance) {
				item.setAmount(item.getAmount() + 1);
			}
			if (fishinglvl != 50) {
				if ((Playerstats.fishingprog.get(player.getUniqueId()) + level
						.fishingxp(item, junk, treasure)) >= (fishinglvl * 10)) {
					int smelting = Playerstats.fishing
							.get(player.getUniqueId());
					smelting++;
					int rest = 0;
					if ((Playerstats.fishingprog.get(player.getUniqueId()) + level
							.fishingxp(item, junk, treasure)) > (fishinglvl * 10)) {
						rest = (Playerstats.fishingprog.get(player
								.getUniqueId()) + level.fishingxp(item, junk,
								treasure))
								- (fishinglvl * 10);
					} else {
						rest = 0;
					}
					Playerstats.fishing.put(player.getUniqueId(), smelting);
					Playerstats.fishingprog.put(player.getUniqueId(), rest);
					player.sendMessage(ChatColor.DARK_PURPLE
							+ "["
							+ ChatColor.GOLD
							+ "YSkill"
							+ ChatColor.DARK_PURPLE
							+ "]"
							+ ChatColor.AQUA
							+ " Fishing has raised with 1 level. You are now level "
							+ smelting + ".");

				} else {
					Playerstats.fishingprog.put(player.getUniqueId(),
							Playerstats.fishingprog.get(player.getUniqueId())
									+ level.fishingxp(item, junk, treasure));
				}
			}
		}
	}
}
