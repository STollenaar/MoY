package moy.tollenaar.stephen.Quests;

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

import moy.tollenaar.stephen.CEvents.QuestProgEvent;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;

public class ProgressHarvest implements Listener {

	private Playerinfo playerinfo;

	public ProgressHarvest(Playerinfo playerinfo) {
		this.playerinfo = playerinfo;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onbucketFillBlock(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Playerstats p = playerinfo.getplayer(player.getUniqueId());
			if (player.getItemInHand() != null
					&& player.getItemInHand().getType() == Material.BUCKET) {
				if (p.getactivetype() != null) {
					if (p.getactives("harvest") != null) {
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
						for (int questn : p.getactives("harvest")) {
							QuestProgEvent e = new QuestProgEvent(player,
									questn, "harvest", item);
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
					} else if (p.getactives("eventharvest") != null) {
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
						for (int questn : p.getactives("eventharvest")) {
							QuestProgEvent e = new QuestProgEvent(player,
									questn, "eventharvest", item);
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onbucketFillEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.COW) {
			Player player = event.getPlayer();
			Playerstats p = playerinfo.getplayer(player.getUniqueId());
			if (player.getItemInHand() != null
					&& player.getItemInHand().getType() == Material.BUCKET) {
				if (p.getactivetype() != null) {
					if (p.getactives("harvest") != null) {
						for (int quest : p.getactives("harvest")) {
							QuestProgEvent e = new QuestProgEvent(player,
									quest, "harvest", new ItemStack(
											Material.MILK_BUCKET));
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
					} else if (p.getactives("eventharvest") != null) {
						for (int quest : p.getactives("eventharvest")) {
							QuestProgEvent e = new QuestProgEvent(player,
									quest, "eventharvest", new ItemStack(
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
		Playerstats p = playerinfo.getplayer(player.getUniqueId());
		if (p.getactivetype() != null) {
			if (p.getactives("harvest") != null) {
				for (int questn : p.getactives("harvest")) {
					QuestProgEvent e = new QuestProgEvent(player, questn,
							"harvest", event.getItem().getItemStack());
					Bukkit.getServer().getPluginManager().callEvent(e);
				}
			} else if (p.getactives("eventharvest") != null) {
				for (int questn : p.getactives("eventharvest")) {
					QuestProgEvent e = new QuestProgEvent(player, questn,
							"eventharvest", event.getItem().getItemStack());
					Bukkit.getServer().getPluginManager().callEvent(e);
				}
			}
		}
	}

	@EventHandler
	public void onItemFromChest(InventoryClickEvent event) {
		if (event.getClickedInventory() != null) {
			if (event.getClickedInventory().getType() == InventoryType.PLAYER
					&& (event.getAction().equals(InventoryAction.PLACE_ALL)
							|| event.getAction().equals(
									InventoryAction.PLACE_SOME) || event
							.getAction().equals(InventoryAction.PLACE_ONE))
					|| event.getAction().equals(
							InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
				ItemStack item;
				if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
					item = event.getCurrentItem();
				}else{
					item = event.getCursor();
				}
				Player player = (Player) event.getWhoClicked();
				Playerstats p = playerinfo.getplayer(player.getUniqueId());
				if (p.getactivetype() != null) {
					if (p.getactives("harvest") != null) {
						for (int questn : p.getactives("harvest")) {
							QuestProgEvent e = new QuestProgEvent(player,
									questn, "harvest", item);
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
					} else if (p.getactives("eventharvest") != null) {
						for (int questn : p.getactives("eventharvest")) {
							QuestProgEvent e = new QuestProgEvent(player,
									questn, "eventharvest",
									item);
							Bukkit.getServer().getPluginManager().callEvent(e);
						}
					}
				}
			}
		}
	}

}
