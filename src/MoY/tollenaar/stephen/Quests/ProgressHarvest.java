package MoY.tollenaar.stephen.Quests;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import MoY.tollenaar.stephen.CEvents.QuestProgEvent;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;

public class ProgressHarvest implements Listener {



	@EventHandler
	public void onbucketFillBlock(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			if (player.getItemInHand() != null
					&& player.getItemInHand().getType() == Material.BUCKET) {
				if (Playerstats.activequests.get(player.getUniqueId()) != null) {
					if (Playerstats.activequests.get(player.getUniqueId()).get(
							"harvest") != null) {
						ItemStack item;
						if (event.getClickedBlock().getType() == Material.WATER
								|| event.getClickedBlock().getType() == Material.STATIONARY_WATER) {
							item = new ItemStack(Material.WATER_BUCKET);
						} else if (event.getClickedBlock().getType() == Material.LAVA
								|| event.getClickedBlock().getType() == Material.STATIONARY_LAVA) {
							item = new ItemStack(Material.LAVA_BUCKET);
						} else {
							item = player.getItemInHand();
						}
						for (int questn : Playerstats.activequests.get(
								player.getUniqueId()).get("harvest")) {
							QuestProgEvent e = new QuestProgEvent(player,
									questn, "harvest", item);
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onbucketFillEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.COW) {
			Player player = event.getPlayer();
			if (player.getItemInHand() != null
					&& player.getItemInHand().getType() == Material.BUCKET) {
				if (Playerstats.activequests.get(player.getUniqueId()) != null) {
					if (Playerstats.activequests.get(player.getUniqueId()).get(
							"harvest") != null) {
						for (int quest : Playerstats.activequests.get(
								player.getUniqueId()).get("harvest")) {
							QuestProgEvent e = new QuestProgEvent(player,
									quest, "harvest", new ItemStack(
											Material.MILK_BUCKET));
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (Playerstats.activequests.get(player.getUniqueId()) != null) {
			if (Playerstats.activequests.get(player.getUniqueId()).get(
					"harvest") != null) {
				for (int questn : Playerstats.activequests.get(
						player.getUniqueId()).get("harvest")) {
					QuestProgEvent e = new QuestProgEvent(player, questn,
							"harvest", event.getItem().getItemStack());
					Bukkit.getServer().getPluginManager().callEvent(e);
				}
			}
		}
	}

	@EventHandler
	public void onItemFromChest(InventoryClickEvent event) {
		if (event.getClickedInventory() != null) {
			if (event.getClickedInventory().getType() == InventoryType.PLAYER
					&& (event.getAction() == InventoryAction.PLACE_ALL
							|| event.getAction() == InventoryAction.PLACE_SOME || event
							.getAction() == InventoryAction.PLACE_ONE)
					|| event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				Player player = (Player) event.getWhoClicked();
				if (Playerstats.activequests.get(player.getUniqueId()) != null) {
					if (Playerstats.activequests.get(player.getUniqueId()).get(
							"harvest") != null) {
						for (int questn : Playerstats.activequests.get(
								player.getUniqueId()).get("harvest")) {
							QuestProgEvent e = new QuestProgEvent(player,
									questn, "harvest", event.getCurrentItem());
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
					}
				}
			}
		}
	}

}
