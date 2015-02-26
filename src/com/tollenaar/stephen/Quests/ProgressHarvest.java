package com.tollenaar.stephen.Quests;

import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.tollenaar.stephen.MistsOfYsir.MoY;
import com.tollenaar.stephen.PlayerInfo.Playerstats;

public class ProgressHarvest implements Listener {

	private MoY plugin;
	private QuestsServerSide q;
	
	public ProgressHarvest(MoY instance) {
		this.plugin = instance;
		this.q = instance.questers;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (Playerstats.activequests.get(player.getUniqueId()) != null) {
			if (Playerstats.activequests.get(player.getUniqueId()).get(
					"harvest") != null) {
				ItemStack item = event.getItem().getItemStack();
				String name;
				if(item.getDurability() != 0){
					name = Integer.toString(item.getTypeId()) + ":" +Short.toString(item.getDurability());
				}else{
					name = Integer.toString(item.getTypeId());
				}


					for (int questn : Playerstats.activequests.get(
							player.getUniqueId()).get("harvest")) {
						QuestHarvest harvest = q.returnharvest(questn);
						if (harvest.getItemId().equals(name)) {
							NPCRegistry reg = CitizensAPI.getNPCRegistry();
							if (item.getAmount() >= harvest.getCount()) {

								for (UUID npcuuid : plugin.questers.harvestquests
										.keySet()) {
									if (plugin.questers.harvestquests.get(
											npcuuid).contains(questn)) {
										NPC npc = reg.getByUniqueId(npcuuid);
										q.AddCompletedQuest(player, questn,
												"harvest", npc.getName());
										return;
									}
								}
							} else {
								if (q.progress.get(player.getUniqueId())
										.get("harvest").get(questn)
										+ item.getAmount() >= harvest
											.getCount()) {
									for (UUID npcuuid : plugin.questers.harvestquests
											.keySet()) {
										if (plugin.questers.harvestquests.get(
												npcuuid).contains(questn)) {
											NPC npc = reg
													.getByUniqueId(npcuuid);
											q.AddCompletedQuest(player, questn,
													"harvest", npc.getName());
											return;
										}
									}
								} else {
									q.progress.get(player.getUniqueId())
											.get("harvest")
											.put(questn,
													q.progress.get(
															player.getUniqueId())
															.get("harvest")
															.get(questn)
															+ item.getAmount());
								}
							}
						}
					}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onItemFromChest(InventoryClickEvent event) {
		if(event.getClickedInventory() != null){
		if (event.getClickedInventory().getType() == InventoryType.PLAYER
				&& (event.getAction() == InventoryAction.PLACE_ALL
						|| event.getAction() == InventoryAction.PLACE_SOME || event
						.getAction() == InventoryAction.PLACE_ONE) || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
			Player player = (Player) event.getWhoClicked();
			if (Playerstats.activequests.get(player.getUniqueId()) != null) {
				if (Playerstats.activequests.get(player.getUniqueId()).get(
						"harvest") != null) {
					
					ItemStack item;
					if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
						item = event.getCurrentItem();
					}else{
						item = event.getCursor();
					}
					if (item != null && item.getType() != Material.AIR) {
						String name = null;
						if(item.getDurability() != 0){
							name = item.getTypeId() + ":" + item.getDurability();
						}else{
							name = Integer.toString(item.getTypeId());
						}
						if (name != null) {
							for (int questn : Playerstats.activequests.get(
									player.getUniqueId()).get("harvest")) {
								QuestHarvest harvest = q.returnharvest(questn);
								if (harvest.getItemId().equals(name)) {
									NPCRegistry reg = CitizensAPI
											.getNPCRegistry();
									if (item.getAmount() >= harvest.getCount()) {

										for (UUID npcuuid : plugin.questers.harvestquests
												.keySet()) {
											if (plugin.questers.harvestquests
													.get(npcuuid).contains(
															questn)) {
												NPC npc = reg
														.getByUniqueId(npcuuid);
												q.AddCompletedQuest(player,
														questn, "harvest",
														npc.getName());
												return;
											}
										}
									} else {
										if (q.progress.get(player.getUniqueId())
												.get("harvest").get(questn)
												+ item.getAmount() >= harvest
													.getCount()) {
											for (UUID npcuuid : plugin.questers.harvestquests
													.keySet()) {
												if (plugin.questers.harvestquests
														.get(npcuuid).contains(
																questn)) {
													NPC npc = reg
															.getByUniqueId(npcuuid);
													q.AddCompletedQuest(player,
															questn, "harvest",
															npc.getName());
													return;
												}
											}
										} else {
											q.progress.get(player.getUniqueId())
													.get("harvest")
													.put(questn,
															q.progress.get(
																	player.getUniqueId())
																	.get("harvest")
																	.get(questn)
																	+ item.getAmount());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		}
	}

}
