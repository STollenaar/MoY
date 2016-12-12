package moy.tollenaar.stephen.SkillsStuff;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
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

import moy.tollenaar.stephen.CEvents.ProgEvent;
import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Skillimprovement implements Listener {
	private MoY plugin;

	private FishingSkill fish;
	private Playerinfo playerinfo;
	
	
	public HashSet<Block> miningblocksplaces = new HashSet<Block>();
	public HashSet<Block> woodblocksplaces = new HashSet<Block>();

	@EventHandler
	public void onblockplace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		PermissionUser user = PermissionsEx.getUser(player);
		if (user.has("MMOTEST") && player.getGameMode() != GameMode.CREATIVE) {
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
		Playerstats p = playerinfo.getplayer(player.getUniqueId());
		PermissionUser user = PermissionsEx.getUser(player);
		if (user.has("MMOTEST") && player.getGameMode() != GameMode.CREATIVE) {
			if (event.getState() == State.CAUGHT_FISH) {
				event.setCancelled(true);
				
				int lvl = p.getFishing();
				Map<Integer, ItemStack> c = fish.GetItem(lvl);
				int chance = (int) c.keySet().toArray()[0];
				ItemStack item = c.get(chance);
				player.getWorld().dropItem(player.getLocation(), item);
				ProgEvent e = new ProgEvent(player.getUniqueId(),
						5, chance);
				
				Bukkit.getServer().getPluginManager().callEvent(e);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		PermissionUser user = PermissionsEx.getUser(player);
		if (user.has("MMOTEST") && player.getGameMode() != GameMode.CREATIVE) {

			Block block = event.getBlock();
			if (!miningblocksplaces.contains(block)
					&& !woodblocksplaces.contains(block)) {
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
		Playerstats p = playerinfo.getplayer(player.getUniqueId());
		ProgEvent e;
		if (SkillLvls.GetSkill(block).getLvl() <= p.getWoodcutting()) {
			e = new ProgEvent(player.getUniqueId(), SkillLvls.GetSkill(
					block).getTree(), block, 1);
		} else {
			event.setCancelled(true);
			e = new ProgEvent(player.getUniqueId(), SkillLvls.GetSkill(
					block).getTree(), block, -1);

		}
		Bukkit.getServer().getPluginManager().callEvent(e);
	}

	private void mining(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Playerstats p = playerinfo.getplayer(player.getUniqueId());
		ProgEvent e;
		if (SkillLvls.GetSkill(block).getLvl() <= p.getMining()) {
			e = new ProgEvent(player.getUniqueId(), block.getType(),
					block, 2);
		} else {
			event.setCancelled(true);
			e = new ProgEvent(player.getUniqueId(), block.getType(),
					block, -2);
		}
		Bukkit.getServer().getPluginManager().callEvent(e);
	}

	@EventHandler
	public void onSmelt(FurnaceSmeltEvent event) {
			if (((Furnace) event.getBlock().getState()).getInventory()
					.getSmelting().getType().isEdible() == true) {
				cooking(event);
			} else {
				smelting(event);
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
		Playerstats p = playerinfo.getplayer(storage.returnfirst(storage.getPlayers().keySet()));
		int current = p.getCooking();
		int chance = r.nextInt(100);
		if (chance <= item.SuccesCalc(current) || chance <= item.SuccesCalc(current, Recepy.getRecepy(item), p)) {
			storage.addsucces(res);
		}	
		else {
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
		this.fish = new FishingSkill();
		this.playerinfo = instance.playerinfo;
	}

}
