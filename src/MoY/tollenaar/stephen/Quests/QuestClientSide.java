package MoY.tollenaar.stephen.Quests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.fusesource.jansi.Ansi;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.Travel.Travel;

public class QuestClientSide {
	private QuestsServerSide questers;

	public QuestClientSide(MoY instance) {
		this.questers = instance.questers;
	}

	// private Main plugin;

	@SuppressWarnings("deprecation")
	public void avquest(NPC npc, Player player, String npcname) {
		UUID npcuuid = npc.getUniqueId();
		int rowcount = 0;
		if (questers.killquests.get(npcuuid) != null) {
			rowcount += questers.killquests.get(npcuuid).size();
		}
		if (questers.harvestquests.get(npcuuid) != null) {
			rowcount += questers.harvestquests.get(npcuuid).size();
		}
		if (questers.talktoquests.get(npcuuid) != null) {
			rowcount += questers.talktoquests.get(npcuuid).size();
		}
		if (questers.warplists.get(npcuuid) != null) {
			// rowcount += questers.warplists.get(npcuuid).size();
			rowcount++;
		}
		if (questers.eventquests.get(npcuuid) != null) {
			for (int numbers : questers.eventquests.get(npcuuid)) {
				if (questers.EventCheck(numbers)) {
					rowcount++;
				}
			}
		}

		if (rowcount % 9 == 0) {
			rowcount++;
		}
		while (rowcount % 9 != 0) {
			rowcount++;
		}
		if (rowcount == 0) {
			rowcount = 9;
		}
		Inventory inv = Bukkit
				.createInventory(null, rowcount, "Aviable Quests");
		if (questers.killquests.get(npcuuid) != null) {
			for (Integer i : questers.killquests.get(npcuuid)) {

				QuestKill kill = questers.returnkill(i);
				boolean pass = false;
				if (!kill.getPrereq().split("=")[0].equals("none")) {
					if (check(kill.getPrereq().split("=")[0],
							Integer.parseInt(kill.getPrereq().split("=")[1]
									.trim()), player, npcname, true,
							kill.getMinlvl())
							&& check("kill", i, player, npcname, false,
									kill.getMinlvl())) {
						pass = true;
					}
				} else {
					if (check("kill", i, player, npcname, false,
							kill.getMinlvl())) {
						pass = true;
					}
				}
				if (pass) {
					ItemStack killquest = new ItemStack(Material.DIAMOND_SWORD);
					{
						ItemMeta meta = killquest.getItemMeta();
						meta.setDisplayName(kill.getName());
						ArrayList<String> lore = new ArrayList<String>();
						lore.add("§" + Integer.toString(kill.getQuestnumber()));
						meta.setLore(lore);
						killquest.setItemMeta(meta);
						inv.addItem(killquest);
					}
				}
			}
		}
		if (questers.harvestquests.get(npcuuid) != null) {
			for (Integer i : questers.harvestquests.get(npcuuid)) {
				QuestHarvest kill = questers.returnharvest(i);
				boolean pass = false;
				if (!kill.getPrereq().split("=")[0].equals("none")) {
					if (check(kill.getPrereq().split("=")[0],
							Integer.parseInt(kill.getPrereq().split("=")[1]
									.trim()), player, npcname, true,
							kill.getMinlvl())
							&& check("harvest", i, player, npcname, false,
									kill.getMinlvl())) {
						pass = true;
					}
				} else {
					if (check("harvest", i, player, npcname, false,
							kill.getMinlvl())) {
						pass = true;
					}
				}
				if (pass) {
					ItemStack harvestquest = new ItemStack(
							Material.DIAMOND_PICKAXE);
					{
						ItemMeta meta = harvestquest.getItemMeta();
						meta.setDisplayName(kill.getName());
						ArrayList<String> lore = new ArrayList<String>();
						lore.add("§" + Integer.toString(kill.getQuestnumber()));
						meta.setLore(lore);
						harvestquest.setItemMeta(meta);
						inv.addItem(harvestquest);
					}
				}
			}
		}
		if (questers.talktoquests.get(npcuuid) != null) {
			for (Integer i : questers.talktoquests.get(npcuuid)) {
				QuestTalkto kill = questers.returntalkto(i);
				boolean pass = false;
				if (!kill.getPrereq().split("=")[0].equals("none")) {
					if (check(kill.getPrereq().split("=")[0],
							Integer.parseInt(kill.getPrereq().split("=")[1]
									.trim()), player, npcname, true,
							kill.getMinlvl())
							&& check("talkto", i, player, npcname, false,
									kill.getMinlvl())) {
						pass = true;
					}
				} else {
					if (check("talkto", i, player, npcname, false,
							kill.getMinlvl())) {
						pass = true;
					}
				}
				if (pass) {
					ItemStack talktoquest = new ItemStack(Material.FEATHER);
					{
						ItemMeta meta = talktoquest.getItemMeta();
						meta.setDisplayName(kill.getName());
						ArrayList<String> lore = new ArrayList<String>();
						lore.add("§" + Integer.toString(kill.getQuestnumber()));
						meta.setLore(lore);
						talktoquest.setItemMeta(meta);
						inv.addItem(talktoquest);
					}
				}
			}
		}
		if (questers.warplists.get(npcuuid) != null) {
			// for (Integer i : questers.warplists.get(npcuuid)) {
			int i = questers.warplists.get(npcuuid);
			Warps kill = questers.returnwarp(i);
			for (String type : kill.GetType().split(",")) {
				for (Warps w : Warps.TypeReturned(type)) {
					if (w.getWarpid() != kill.getWarpid()) {
						ItemStack title = new ItemStack(Material.BOAT);
						{
							ArrayList<String> lore = new ArrayList<String>();
							int time = Travel.traveltime(npcuuid,
									kill.getWarpid(), type, w.getStartloc());
							SimpleDateFormat df = new SimpleDateFormat("mm:ss");
							Date date = new Date((long) (time * 1000));
							date.setHours(date.getHours() - 9);
							date.setMinutes(date.getMinutes() - 30);
							lore.add("Duration: " + df.format(date));
							lore.add("§" + Integer.toString(w.getWarpid()));
							lore.add("§" + Integer.toString(kill.getWarpid()));
							ItemMeta meta = title.getItemMeta();
							meta.setLore(lore);
							meta.setDisplayName(kill.getName() + " - "
									+ w.getName());
							title.setItemMeta(meta);
						}

						inv.addItem(title);
					}
				}
			}

		}
		if (questers.eventquests.get(npcuuid) != null) {
			for (int i : questers.eventquests.get(npcuuid)) {
				if (questers.EventCheck(i)) {
					QuestEvent event = questers.returneventquest(i);

					if (lvlcheck(player, event.getMinlvl())) {
						ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
						ItemMeta meta = item.getItemMeta();
						ArrayList<String> lore = new ArrayList<String>();
						meta.setDisplayName(event.getTitle());
						lore.add("§" + event.getNumber());
						meta.setLore(lore);
						item.setItemMeta(meta);
						inv.addItem(item);
					}
				}
			}
		}

		if (inv.getItem(inv.getSize() - 1) != null) {
			Inventory newinv = Bukkit.createInventory(null, rowcount + 9,
					"Aviable Quests");
			for (ItemStack items : inv.getContents()) {
				newinv.addItem(items);
			}
			ItemStack npcinfo = new ItemStack(Material.BEDROCK);
			{
				ItemMeta meta = npcinfo.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				String builder = "";
				for (String in : npcuuid.toString().split("")) {
					builder += "§" + in;
				}
				lore.add(builder);
				meta.setLore(lore);
				npcinfo.setItemMeta(meta);
				newinv.setItem(newinv.getSize() - 1, npcinfo);
			}
		} else {
			ItemStack npcinfo = new ItemStack(Material.BEDROCK);
			{
				ItemMeta meta = npcinfo.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				String builder = "";
				for (String in : npcuuid.toString().split("")) {
					builder += "§" + in;
				}
				lore.add(builder);
				meta.setLore(lore);
				npcinfo.setItemMeta(meta);
				inv.setItem(inv.getSize() - 1, npcinfo);
			}
		}
		boolean poss = inv.getContents()[0] != null;
		boolean type;
		if (poss) {
			type = inv.getContents()[0].getType() != null;
		} else {
			type = false;
		}
		if (poss && type) {
			player.openInventory(inv);
		} else {
			questers.plugin.qinteract.dummymessage(player, npc);
		}
	}

	public boolean check(String type, int number, Player player,
			String npcname, boolean ispreq, int minlvl) {
		UUID playeruuid = player.getUniqueId();
		boolean pass = rewardcheck(type, number, player, npcname);

		if (Playerstats.activequests.get(playeruuid) != null) {
			if (Playerstats.activequests.get(playeruuid).get(type) != null) {
				if (Playerstats.activequests.get(playeruuid).get(type)
						.contains(number)) {
					pass = false;
				}
			}
		}

		if (Playerstats.rewardedlist.get(playeruuid) != null) {
			if (Playerstats.rewardedlist.get(playeruuid).get(type) != null) {
				if (Playerstats.rewardedlist.get(playeruuid).get(type)
						.get(number) == null
						&& ispreq) {
					pass = false;
				} else if (!ispreq
						&& Playerstats.rewardedlist.get(playeruuid).get(type)
								.get(number) != null) {
					pass = false;
				}
			} else if (ispreq) {
				pass = false;
			}
		} else if (ispreq) {
			pass = false;
		}
		if (!lvlcheck(player, minlvl)) {
			pass = false;
		}

		return pass;
	}

	private boolean lvlcheck(Player player, int minlvl) {
		if (Playerstats.level.get(player.getUniqueId()) < minlvl) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("deprecation")
	private boolean rewardcheck(String type, int number, Player player,
			String npcname) {
		UUID playeruuid = player.getUniqueId();
		boolean pass = true;
		/**
		 * add the rewarded thingy
		 */
		if (Playerstats.completedquests.get(playeruuid) != null) {
			if (Playerstats.completedquests.get(playeruuid).get(type) != null) {
				if (Playerstats.completedquests.get(playeruuid).get(type)
						.contains(number)) {
					for (int i : Playerstats.completedquests.get(playeruuid)
							.get(type)) {

						if (i == number) {
							List<String> reward = null;
							// killquest
							if (type.equals("kill")) {
								reward = questers.returnkill(number)
										.getReward();
							}
							// harvestquest
							else if (type.equals("harvest")) {
								boolean passing = false;
								int needed = 0;
								String name = null;
								ItemStack saveditem = null;
								for (ItemStack items : player.getInventory()
										.getContents()) {
									saveditem = items;
									if (items != null
											&& items.getType() != Material.AIR) {
										if (items.getDurability() != 0) {
											name = items.getTypeId() + ":"
													+ items.getDurability();
										} else {
											name = Integer.toString(items
													.getTypeId());
										}

										if (questers.returnharvest(number)
												.getItemId().equals(name)) {
											needed = questers.returnharvest(
													number).getCount();
											if (items.getAmount() >= questers
													.returnharvest(number)
													.getCount()) {

												passing = true;
												break;
											} else {

												saveditem.setAmount(saveditem
														.getAmount()
														+ items.getAmount());
												if (saveditem.getAmount() >= questers
														.returnharvest(number)
														.getCount()) {

													passing = true;
													break;
												}
											}

										}

									}
								}
								if (saveditem == null) {
									String[] id = questers
											.returnharvest(number).getItemId()
											.split(":");
									if (id.length == 2) {
										saveditem = new ItemStack(
												Material.getMaterial(Integer
														.parseInt(id[0].trim())),
												1, Short.parseShort(id[1]
														.trim()));
									} else {
										saveditem = new ItemStack(
												Material.getMaterial(Integer
														.parseInt(id[0].trim())));
									}
								}

								if (passing) {
									while (needed != 0) {
										for (ItemStack item : player
												.getInventory().getContents()) {
											if (item != null
													&& item.getType() != Material.AIR) {
												if (item.getDurability() != 0) {
													name = item.getTypeId()
															+ ":"
															+ item.getDurability();
												} else {
													name = Integer
															.toString(item
																	.getTypeId());
												}
												if (questers
														.returnharvest(number)
														.getItemId()
														.equals(name)) {
													if (needed
															- item.getAmount() <= 0) {
														if (item.getAmount()
																- needed == 0) {
															player.getInventory()
																	.remove(item);
															player.updateInventory();
														} else {
															item.setAmount(item
																	.getAmount()
																	- needed);
															player.updateInventory();
														}
														needed = 0;
													} else {
														needed -= item
																.getAmount();
														player.getInventory()
																.remove(item);
														player.updateInventory();
													}
												}
											}
										}
									}
									reward = questers.returnharvest(number)
											.getReward();

								} else {
									player.sendMessage(ChatColor.DARK_PURPLE
											+ "["
											+ ChatColor.GOLD
											+ npcname
											+ ChatColor.DARK_PURPLE
											+ "] "
											+ ChatColor.AQUA
											+ "You didn't bring me "
											+ questers.returnharvest(number)
													.getCount()
											+ " "
											+ questers.GetItemName(saveditem)
											+ ". Come back with the right amount for your reward");
									pass = false;
									return pass;
								}
								// talktoquest
							} else {
								reward = questers.returntalkto(number)
										.getReward();

							}

							// giving the reward
							System.out.println(Ansi.ansi().fg(Ansi.Color.RED)
									+ "QUEST COMPLETE"
									+ Ansi.ansi().fg(Ansi.Color.WHITE));
							if (reward != null && !reward.contains("unknown")) {
								for (String in : reward) {
									String r = in.replace("player",
											player.getName());
									Bukkit.dispatchCommand(
											Bukkit.getConsoleSender(), r);
								}
							}
							Playerstats.completedquests.get(playeruuid)
									.get(type).remove(i);

							if (Playerstats.rewardedlist.get(playeruuid) != null) {
								if (Playerstats.rewardedlist.get(playeruuid)
										.get(type) != null) {
									Playerstats.rewardedlist
											.get(playeruuid)
											.get(type)
											.put(number,
													System.currentTimeMillis());
								} else {
									HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
									temp.put(number, System.currentTimeMillis());
									Playerstats.rewardedlist.get(playeruuid)
											.put(type, temp);
								}
							} else {
								HashMap<String, HashMap<Integer, Long>> total = new HashMap<String, HashMap<Integer, Long>>();
								HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
								temp.put(number, System.currentTimeMillis());
								total.put(type, temp);
								Playerstats.rewardedlist.put(playeruuid, total);
							}
						}
					}
				}
			}
		}
		return pass;
	}

}
