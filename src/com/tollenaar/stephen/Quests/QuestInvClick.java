package com.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.HashSet;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.tollenaar.stephen.MistsOfYsir.MoY;
import com.tollenaar.stephen.Travel.Travel;

public class QuestInvClick implements Listener {

	private Travel tr;
	protected QuestsServerSide questers;

	public QuestInvClick(MoY instance) {
		this.tr = instance.tr;
		this.questers = instance.questers;
	}

	@EventHandler
	public void StaffQuestSelector(InventoryClickEvent event) {
		Inventory clickinv = event.getClickedInventory();
		if (event.getCurrentItem() != null
				&& event.getCurrentItem().getType() != Material.AIR) {
			if (clickinv != null
					&& (clickinv.getName().equals("AllKill")
							|| clickinv.getName().equals("AllHarvest")
							|| clickinv.getName().equals("AllTalkTo")
							|| clickinv.getName().equals("WarpList")
							|| clickinv.getName().equals("AllWarps")
							|| clickinv.getName().equals("TalktoQuest")
							|| clickinv.getName().equals("KillQuest")
							|| clickinv.getName().equals("HarvestQuest") || clickinv
							.getName().equals("Main settings"))) {
				UUID npcuuid = UUID.fromString(clickinv
						.getItem(clickinv.getSize() - 1).getItemMeta()
						.getLore().get(0));
				ItemStack item = event.getCurrentItem();

				String name = item.getItemMeta().getDisplayName();
				Player player = (Player) event.getWhoClicked();
				if (clickinv.getName().equals("Main settings")) {
					if (name.equals("Kill Quests")) {
						questers.allkill(player,
								questers.killquests.get(npcuuid), npcuuid);
					} else if (name.equals("Harvest Quests")) {
						questers.allharvest(player,
								questers.harvestquests.get(npcuuid), npcuuid);
					} else if (name.equals("Warp Lists")) {
						questers.allwarps(player,
								questers.warplists.get(npcuuid), npcuuid);
					} else if (name.equals("Talk to Quest")) {
						questers.alltalkto(player,
								questers.talktoquests.get(npcuuid), npcuuid);
					} else if (event.getCurrentItem().getItemMeta()
							.getDisplayName().equals("Active/Passive")) {
						if (questers.activenpc.get(npcuuid) != null) {
							event.setCancelled(true);
							player.closeInventory();
							questers.stop(npcuuid);
							questers.npcsettingsmain(npcuuid, player);

						} else {
							event.setCancelled(true);
							player.closeInventory();
							questers.runpoints(npcuuid);
							questers.npcsettingsmain(npcuuid, player);
						}
					} else if (event.getCurrentItem().getItemMeta()
							.getDisplayName().equals("NPC name")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("Type the new npc name");
						ArrayList<String> t = new ArrayList<String>();
						t.add("Main");
						t.add(npcuuid.toString());
						questers.npcpos.put(player.getUniqueId(), t);

					} else if (event.getCurrentItem().getItemMeta()
							.getDisplayName().equals("Delete NPC")) {
						questers.despawnNPC(npcuuid);
						event.setCancelled(true);
						player.closeInventory();
					} else if (event.getCurrentItem().getItemMeta()
							.getDisplayName().equals("NPC skinName")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("Type the new npc skin name");
						ArrayList<String> t = new ArrayList<String>();
						t.add("Skin");
						t.add(npcuuid.toString());
						questers.npcpos.put(player.getUniqueId(), t);
					}
				} else if (clickinv.getName().equals("AllKill")) {
					if (name.equals("Create New")) {
						event.setCancelled(true);
						HashSet<Integer> all;
						if (questers.killquests.get(npcuuid) != null) {
							all = questers.killquests.get(npcuuid);
						} else {
							all = new HashSet<Integer>();
						}
						int n = questers.createnewkill();
						all.add(n);
						questers.killquests.put(npcuuid, all);
						questers.returnkill(n).npcsettingskill(npcuuid, player);
					} else {
						event.setCancelled(true);
						questers.returnkill(
								Integer.parseInt(item.getItemMeta().getLore()
										.get(0))).npcsettingskill(npcuuid,
								player);

					}
				} else if (clickinv.getName().equals("AllHarvest")) {
					if (name.equals("Create New")) {
						event.setCancelled(true);
						HashSet<Integer> all;
						if (questers.harvestquests.get(npcuuid) != null) {
							all = questers.harvestquests.get(npcuuid);
						} else {
							all = new HashSet<Integer>();
						}
						int n = questers.createnewhar();
						all.add(n);
						questers.harvestquests.put(npcuuid, all);
						questers.returnharvest(n).qinventory(player, npcuuid);
					} else {
						event.setCancelled(true);
						questers.returnharvest(
								Integer.parseInt(item.getItemMeta().getLore()
										.get(0))).qinventory(player, npcuuid);
					}
				} else if (clickinv.getName().equals("AllTalkTo")) {
					if (name.equals("Create New")) {
						event.setCancelled(true);
						HashSet<Integer> all;
						if (questers.talktoquests.get(npcuuid) != null) {
							all = questers.talktoquests.get(npcuuid);
						} else {
							all = new HashSet<Integer>();
						}
						int n = questers.createnewtalk();
						all.add(n);
						questers.talktoquests.put(npcuuid, all);
						questers.returntalkto(n).npcsettingstalkto(npcuuid,
								player);
					} else {
						event.setCancelled(true);
						questers.returntalkto(
								Integer.parseInt(item.getItemMeta().getLore()
										.get(0))).npcsettingstalkto(npcuuid,
								player);
					}
				} else if (clickinv.getName().equals("AllWarps")) {
					if (name.equals("Create New")) {
						event.setCancelled(true);

						if (questers.warplists.get(npcuuid) != null) {
							event.setCancelled(true);
							return;
						} else {
						}
						int n = questers.createnewwarp(player.getLocation());

						questers.warplists.put(npcuuid, n);
						questers.returnwarp(n).npcsettingswarplists(npcuuid,
								player);
					} else {
						event.setCancelled(true);
						questers.returnwarp(
								Integer.parseInt(item.getItemMeta().getLore()
										.get(0))).npcsettingswarplists(npcuuid,
								player);
					}
				}
				if (clickinv.getName().equals("KillQuest")) {
					if (item.getItemMeta().getDisplayName().equals("To Main")) {
						event.setCancelled(true);
						player.closeInventory();
						questers.npcsettingsmain(npcuuid, player);
					}
					if (item.getItemMeta().getDisplayName().equals("Title")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the new title of this killquest");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("killquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("title");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Mob")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the monster name");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("killquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("mob");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Count")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type how many monsters you need to kill");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("killquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("count");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Reward")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the command withouth the /. use player for the players who will use it");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("killquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("reward");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Repeat Delay")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number for the repeat delay. If there isn't any repeat possible type -1");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("killquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("repeat");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Minimum Lvl")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number for the minimum lvl of the player (in score board not xp lvl).");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("killquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("minimum");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Message")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the sentence the player would read when selecting the quest.");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("killquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("message");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Prerequisite")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("prere if nothing is required prior to this type none. otherwise type: <killquest/harvestquest/talktoquest> <questnumber>");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("killquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("prereq");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Delete Quest")) {
						event.setCancelled(true);
						int questnumber = Integer.parseInt(clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1));
						questers.killquests.get(npcuuid).remove(questnumber);
						questers.removekill(questnumber);
						player.closeInventory();
						questers.allkill(player,
								questers.killquests.get(npcuuid), npcuuid);
					}
				}
				if (clickinv.getName().equals("HarvestQuest")) {
					if (item.getItemMeta().getDisplayName().equals("To Main")) {
						event.setCancelled(true);
						player.closeInventory();
						questers.npcsettingsmain(npcuuid, player);
					}
					if (item.getItemMeta().getDisplayName().equals("Title")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the new title of this harvestquest");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harvestquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("title");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Item to get")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the item id");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harvestquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("item");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Count")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type how many items you need");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harvestquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("count");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Reward")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the command withouth the /. use player for the players who will use it");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harvestquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("reward");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Repeat Delay")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number for the repeat delay. If there isn't any repeat possible type -1");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harvestquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("repeat");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Minimum Lvl")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number for the minimum lvl of the player (in score board not xp lvl).");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harvestquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("minimum");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Message")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the sentence the player would read when selecting the quest.");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harvestquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("message");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Prerequisite")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("prere if nothing is required prior to this type none. otherwise type: <killquest/harvestquest/talktoquest> <questnumber>");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harvestquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("prereq");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Delete Quest")) {
						event.setCancelled(true);
						int questnumber = Integer.parseInt(clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1));
						questers.harvestquests.get(npcuuid).remove(questnumber);
						questers.removeharvest(questnumber);
						player.closeInventory();
						questers.allharvest(player,
								questers.harvestquests.get(npcuuid), npcuuid);
					}
				}

				if (clickinv.getName().equals("TalktoQuest")) {
					if (item.getItemMeta().getDisplayName().equals("To Main")) {
						event.setCancelled(true);
						player.closeInventory();
						questers.npcsettingsmain(npcuuid, player);
					}
					if (item.getItemMeta().getDisplayName().equals("Title")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the new title of this talktoquest");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("talktoquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("title");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Person")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the name of the npc.");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("talktoquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("person");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Reward")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the command without the /");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("talktoquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("reward");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Minimum Lvl")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number that is the minimum lvl of this quest");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("talktoquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("minimum");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Repeat Delay")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number for how many seconds this quest to accept again. type -1 for no repeat possible");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("talktoquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("repeat");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Message")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the message the npc would say when accepting the quest.");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("talktoquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("message");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Prerequisite")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("prere if nothing is required prior to this type none. otherwise type: <killquest/harvestquest/talktoquest> <questnumber>");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("talktoquest");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("prereq");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Delete Quest")) {
						event.setCancelled(true);
						int questnumber = Integer.parseInt(clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1));
						questers.talktoquests.get(npcuuid).remove(questnumber);
						questers.removetalkto(questnumber);
						player.closeInventory();
						questers.alltalkto(player,
								questers.talktoquests.get(npcuuid), npcuuid);
					}
				}

				if (clickinv.getName().equals("WarpList")) {
					if (item.getItemMeta().getDisplayName().equals("To Main")) {
						event.setCancelled(true);
						player.closeInventory();
						questers.npcsettingsmain(npcuuid, player);
					}
					if (item.getItemMeta().getDisplayName().equals("Title")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the new title of this warplists");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("warplists");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("title");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("To Main")) {
						event.setCancelled(true);
						player.closeInventory();
						questers.npcsettingsmain(npcuuid, player);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Delete Warp")) {
						event.setCancelled(true);
						questers.warplists.remove(npcuuid);
						player.closeInventory();
						questers.allwarps(player,
								questers.warplists.get(npcuuid), npcuuid);
					}

					if (item.getItemMeta().getDisplayName()
							.equals("Type of transportation")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("boat, oxcart or dragoncoach. multiple types seperate with ,. ex: boat,oxcart ");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("warplists");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("type");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Cost of Ride")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number for how much money the ride costs, if free type 0");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("warplists");
						temp.add(npcuuid.toString());
						String questnumber = clickinv
								.getItem(clickinv.getSize() - 1).getItemMeta()
								.getLore().get(1);
						temp.add(questnumber);
						temp.add("cost");
						questers.npcpos.put(player.getUniqueId(), temp);
					}

				}

				if (!event.isCancelled()) {
					event.setCancelled(true);
				}
			} else if (clickinv.getName().equals("NPCNames")) {
				System.out.println(Thread.currentThread().getStackTrace()[1]
						+ " " + event.getCurrentItem());
				if (event.getCurrentItem() != null) {
					if (event.getCurrentItem().getItemMeta() != null) {
						if (event.getWhoClicked() != null) {
							Inventory npcinv = event.getInventory();
							Player player = (Player) event.getWhoClicked();
							UUID tuuid = UUID.fromString(event.getCurrentItem()
									.getItemMeta().getLore().get(0));
							NPCRegistry registry = CitizensAPI.getNPCRegistry();
							NPC npc = registry.getByUniqueId(tuuid);
							UUID npcuid = npc.getUniqueId();
							UUID npcuuid = UUID.fromString(npcinv
									.getItem(npcinv.getSize() - 1)
									.getItemMeta().getLore().get(0));
							ItemStack item = event.getCurrentItem();
							int id = Integer.parseInt(item.getItemMeta()
									.getLore().get(1));
							int questn = Integer.parseInt(npcinv
									.getItem(npcinv.getSize() - 1)
									.getItemMeta().getLore().get(1));
							questers.targetnpcs.put(npcuid, id);
							questers.returntalkto(questn).setNpcid(id);
							questers.npcpos.remove(player.getUniqueId());
							player.closeInventory();
							questers.returntalkto(questn).npcsettingstalkto(
									npcuuid, player);
							event.setCancelled(true);
						}
					}
				}
				event.setCancelled(true);
			} else if (clickinv.getName().equals("Wait Location info")
					|| clickinv.getName().equals("Trip Location info")) {
				ItemStack item = event.getCurrentItem();
				String name = item.getItemMeta().getDisplayName();
				Player player = (Player) event.getWhoClicked();
				String id = event.getClickedInventory().getItem(0)
						.getItemMeta().getLore().get(0);
				if (clickinv.getName().equals("Trip Location info")) {
					if (name.equals("Trip Type")) {
						player.closeInventory();
						player.sendMessage("boat, oxcart or dragoncoach. type 1 type");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("trip");
						temp.add("type");
						temp.add(id);
						questers.npcpos.put(player.getUniqueId(), temp);
					} else if (name.equals("Trip Location")) {
						player.closeInventory();
						player.sendMessage("go to the loction and type save");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("trip");
						temp.add("location");
						temp.add(id);
						questers.npcpos.put(player.getUniqueId(), temp);
					} else if (name.equals("Delete")) {
						player.closeInventory();
						questers.removetrip(Integer.parseInt(id));
					}
				} else if (clickinv.getName().equals("Wait Location info")) {
					if (name.equals("Trip Type")) {
						player.closeInventory();
						player.sendMessage("boat, oxcart or dragoncoach. type 1 type");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harbor");
						temp.add("type");
						temp.add(id);
						questers.npcpos.put(player.getUniqueId(), temp);
					} else if (name.equals("Trip Location")) {
						player.closeInventory();
						player.sendMessage("go to the loction and type save");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harbor");
						temp.add("location");
						temp.add(id);
						questers.npcpos.put(player.getUniqueId(), temp);
					} else if (name.equals("Delete")) {
						player.closeInventory();
						questers.removeharbor(Integer.parseInt(id));
					}
				}
			}

		}
	}

	@EventHandler
	public void PlayerQuestSelector(InventoryClickEvent event) {
		Inventory clickedinv = event.getClickedInventory();
		if (event.getCurrentItem() != null
				&& event.getCurrentItem().getType() != Material.AIR) {
			if (clickedinv != null
					&& clickedinv.getName().equals("Aviable Quests")) {

				Player player = (Player) event.getWhoClicked();
				ItemStack item = event.getCurrentItem();
				if (item.getType() == Material.BEDROCK) {
					event.setCancelled(true);
					return;
				}
				int number = Integer.parseInt(item.getItemMeta().getLore()
						.get(item.getItemMeta().getLore().size() - 1));
				String message = GetMessage(item.getType(), number);
				String quetstype = GetTypeQuest(item.getType());

				UUID npcuuid = UUID.fromString(clickedinv
						.getItem(clickedinv.getSize() - 1).getItemMeta()
						.getLore().get(0));
				/**
				 * this is for adding new acitve quests
				 */
				if (!quetstype.equals("warp")) {
					questers.AddActiveQuest(player, number, quetstype);
					/**
					 * going to send the player his confirmation etc.
					 */
					player.closeInventory();
					player.sendMessage(ChatColor.BLUE + "[" + ChatColor.BLUE
							+ "YQuest" + ChatColor.BLUE + "] " + ChatColor.GRAY
							+ message);
					if (!quetstype.equals("talkto")) {
						if (questers.progress.get(player.getUniqueId()) != null) {
							if (questers.progress.get(player.getUniqueId())
									.get(quetstype) != null) {
								questers.progress.get(player.getUniqueId())
										.get(quetstype).put(number, 0);
							} else {
								HashMap<Integer, Integer> numberq = new HashMap<Integer, Integer>();
								numberq.put(number, 0);
								questers.progress.get(player.getUniqueId())
										.put(quetstype, numberq);
							}
						} else {
							HashMap<String, HashMap<Integer, Integer>> total = new HashMap<String, HashMap<Integer, Integer>>();
							HashMap<Integer, Integer> numberq = new HashMap<Integer, Integer>();
							numberq.put(number, 0);
							total.put(quetstype, numberq);
							questers.progress.put(player.getUniqueId(), total);
						}
					}
				} else {
					int Number2 = Integer.parseInt(item.getItemMeta().getLore()
							.get(item.getItemMeta().getLore().size() - 2));
					player.closeInventory();
					tr.boardcheck(npcuuid, number, player,
							questers.returnwarp(Number2).getStartloc());
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void QuestBlockers(InventoryClickEvent event) {
		Inventory clickedinv = event.getClickedInventory();
		if (clickedinv != null) {
			if (clickedinv.getName().startsWith("Rewarded")
					|| clickedinv.getName().startsWith("Completed")
					|| clickedinv.getName().startsWith("Active")) {
				event.setCancelled(true);
			}
		}
	}

	private String GetMessage(Material itemtype, int quetsnumber) {
		switch (itemtype) {
		case DIAMOND_SWORD:
			QuestKill kill = questers.returnkill(quetsnumber);
			return kill.getMessage();
		case DIAMOND_PICKAXE:
			QuestHarvest harvest = questers.returnharvest(quetsnumber);
			return harvest.getMessage();
		case FEATHER:
			QuestTalkto talk = questers.returntalkto(quetsnumber);
			return talk.getMessage();
		default:
			return "Enjoy your trip.";
		}
	}

	private String GetTypeQuest(Material itemtype) {
		switch (itemtype) {
		case DIAMOND_SWORD:
			return "kill";
		case DIAMOND_PICKAXE:
			return "harvest";
		case FEATHER:
			return "talkto";
		default:
			return "warp";
		}
	}
}
