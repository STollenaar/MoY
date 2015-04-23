package MoY.tollenaar.stephen.SkillsStuff;

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

import MoY.tollenaar.stephen.CEvents.MiningProgEvent;
import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
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
			if (SkillLvls.GetSkill(block) != null) {
				if (SkillLvls.GetSkill(block).getOre() != null) {
					miningblocksplaces.add(block);
				} else if (SkillLvls.GetSkill(block).getTree() != null) {
					woodblocksplaces.add(block);
				}
			} else if (block.getType() == Material.FURNACE) {
				new FurnaceStorage(block, plugin);
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
				if (SkillLvls.GetSkill(block) != null) {
					if (SkillLvls.GetSkill(block).getOre() != null) {
						mining(event);
					} else {
						woodcutting(event);
					}
				} else if (block.getType() == Material.FURNACE
						|| block.getType() == Material.BURNING_FURNACE) {
					plugin.fw.removefile(FurnaceStorage.GetStorage(block));
					FurnaceStorage.RemoveStorage(block);
				}
			} else if (miningblocksplaces.contains(block)) {
				miningblocksplaces.remove(block);
			} else if (woodblocksplaces.contains(block)) {
				woodblocksplaces.remove(block);
			} else if (block.getType() == Material.FURNACE
					|| block.getType() == Material.BURNING_FURNACE) {
				plugin.fw.removefile(FurnaceStorage.GetStorage(block));
				FurnaceStorage.RemoveStorage(block);
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
		Bukkit.getServer().getPluginManager().callEvent(e);
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

	@EventHandler
	public void onSmelt(FurnaceSmeltEvent event) {
		if (event.getBlock().getLocation().getWorld().getName()
				.equals("MMOTESTWORLD")) {
			if (((Furnace) event.getBlock().getState()).getInventory()
					.getSmelting().getType().isEdible() == true) {
				cooking(event);
			} else {
				smelting(event);
			}
		}
	}

	@EventHandler
	public void onFurnaceempty(InventoryClickEvent event) {
		if (event.getClickedInventory() != null
				&& event.getClickedInventory().getType() == InventoryType.FURNACE) {
			Player player = (Player) event.getWhoClicked();
			Block block = ((Furnace) event.getInventory().getHolder())
					.getBlock();
			FurnaceStorage storage = FurnaceStorage.GetStorage(block);
			if (event.getCurrentItem() != null
					&& event.getCurrentItem().getType() != Material.AIR) {

				if (event.getAction() == InventoryAction.PICKUP_ALL
						|| event.getAction() == InventoryAction.PICKUP_HALF
						|| event.getAction() == InventoryAction.PICKUP_ONE
						|| event.getAction() == InventoryAction.PICKUP_SOME) {
					if (event.getSlotType() == SlotType.CRAFTING) {
						storage.removeburing(event, player.getUniqueId());
					}
				} else if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
					if (event.getSlotType() == SlotType.CRAFTING) {
						storage.removeburing(event, player.getUniqueId());
					} else if (event.getSlotType() == SlotType.RESULT) {
						if (event.getCurrentItem().getType() == Material.ROTTEN_FLESH) {
							storage.removeburing(event, player.getUniqueId());
						}
					} else if ((SkillLvls.GetSkill(event.getCurrentItem()) != null && SkillLvls
							.GetSkill(event.getCurrentItem()).getOre() != null)
							|| (Edibles.GetEdible(event.getCurrentItem()) != null)) {
						storage.addburning(event.getCurrentItem(),
								player.getUniqueId());
					}
				}
			} else if ((event.getAction() == InventoryAction.PLACE_ALL
					|| event.getAction() == InventoryAction.PLACE_ONE || event
					.getAction() == InventoryAction.PLACE_SOME)
					&& event.getCursor() != null
					&& event.getCursor().getType() != Material.AIR) {

				if (event.getSlotType() == SlotType.CRAFTING) {
					storage.addburning(event.getCursor(), player.getUniqueId());
				}
			}
			if ((event.getInventory().getItem(2) == null || event
					.getInventory().getItem(2).getType() == Material.AIR)
					&& event.getSlotType() == SlotType.RESULT) {
				event.getInventory().setItem(2, storage.getFailures());
				storage.removefailure(event, player.getUniqueId());
			}
		} else if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
				&& event.getInventory().getHolder() instanceof Furnace) {
			FurnaceStorage storage = FurnaceStorage.GetStorage(((Furnace) event
					.getInventory().getHolder()).getBlock());
			Player player = (Player) event.getWhoClicked();
			if (event.getSlotType() == SlotType.CRAFTING) {
				storage.removeburing(event, player.getUniqueId());
			} else if (event.getSlotType() == SlotType.RESULT) {
				if (event.getCurrentItem().getType() == Material.ROTTEN_FLESH) {
					storage.removeburing(event, player.getUniqueId());
				}
			} else if ((SkillLvls.GetSkill(event.getCurrentItem()) != null && SkillLvls
					.GetSkill(event.getCurrentItem()).getOre() != null)
					|| (Edibles.GetEdible(event.getCurrentItem()) != null)) {
				storage.addburning(event.getCurrentItem(), player.getUniqueId());
			}
		}
	}

	private void cooking(FurnaceSmeltEvent event) {
		Furnace furn = (Furnace) event.getBlock().getState();
		ItemStack res = furn.getInventory().getSmelting();
		Edibles item = Edibles.GetEdible(res);

		Random r = new Random();
		FurnaceStorage storage = FurnaceStorage.GetStorage(event.getBlock());

		int current = Playerstats.cooking.get(storage.returnfirst(storage
				.getPlayers().keySet()));
		if (r.nextInt(100) <= item.SuccesCalc(current)) {
			storage.addsucces(res);
		} else {
			res = new ItemStack(Material.ROTTEN_FLESH, 1);
			storage.addfailure(res);
			event.setCancelled(true);
			ItemStack source = event.getSource();
			source.setAmount(source.getAmount() - 1);
		}
	}

	private void smelting(FurnaceSmeltEvent event) {
		FurnaceStorage storage = FurnaceStorage.GetStorage(event.getBlock());
		storage.addsucces(event.getResult());
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
