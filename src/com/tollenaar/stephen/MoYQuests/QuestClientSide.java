package com.tollenaar.stephen.MoYQuests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.v1_8_R1.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.fusesource.jansi.Ansi;

import com.tollenaar.stephen.MoYMistsOfYsir.MoY;
import com.tollenaar.stephen.MoYPlayerInfo.Playerstats;
import com.tollenaar.stephen.MoYTravel.Travel;

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
				if (i != null) {

					if (check("kill", i, player, npcname)) {
						QuestKill kill = questers.returnkill(i);
						boolean pass = false;
						if (!kill.getPrereq().split("=")[0].equals("none")) {
							if (check(kill.getPrereq().split("=")[0],
									Integer.parseInt(kill.getPrereq()
											.split("=")[1].trim()), player,
									npcname)) {
								pass = true;
							}
						} else {
							pass = true;
						}
						if (pass) {
							ItemStack killquest = new ItemStack(
									Material.DIAMOND_SWORD);
							{
								ItemMeta meta = killquest.getItemMeta();
								meta.setDisplayName(kill.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add(kill.getMessage());
								lore.add(Integer.toString(kill.getQuestnumber()));
								meta.setLore(lore);
								killquest.setItemMeta(meta);
								
								net.minecraft.server.v1_8_R1.ItemStack nms = CraftItemStack.asNMSCopy(killquest);
								NBTTagCompound tag = null;
								if(nms.getTag() != null)tag = nms.getTag();
								else
								{
								    nms.setTag(new NBTTagCompound());
								    nms.setTag(tag);
								}
								int x = 1;
								for(String l : meta.getLore()){
									tag.setString("Lore" + x,  l);
									x++;
								}
								killquest = CraftItemStack.asCraftMirror(nms);
								inv.addItem(killquest);
							}
						}
					}
				}
			}
		}
		if (questers.harvestquests.get(npcuuid) != null) {
			for (Integer i : questers.harvestquests.get(npcuuid)) {
				if (i != null) {
					if (check("harvest", i, player, npcname)) {
						QuestHarvest kill = questers.returnharvest(i);
						boolean pass = false;
						if (!kill.getPrereq().split("=")[0].equals("none")) {
							if (check(kill.getPrereq().split("=")[0],
									Integer.parseInt(kill.getPrereq()
											.split("=")[1].trim()), player,
									npcname)) {
								pass = true;
							}
						} else {
							pass = true;
						}
						if (pass) {
							ItemStack harvestquest = new ItemStack(
									Material.DIAMOND_PICKAXE);
							{
								ItemMeta meta = harvestquest.getItemMeta();
								meta.setDisplayName(kill.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add(kill.getMessage());
								lore.add(Integer.toString(kill.getQuestnumber()));
								meta.setLore(lore);
								harvestquest.setItemMeta(meta);
								inv.addItem(harvestquest);
							}
						}
					}
				}
			}
		}
		if (questers.talktoquests.get(npcuuid) != null) {
			for (Integer i : questers.talktoquests.get(npcuuid)) {
				if (i != null) {
					if (check("talkto", i, player, npcname)) {
						QuestTalkto kill = questers.returntalkto(i);
						boolean pass = false;
						if (!kill.getPrereq().split("=")[0].equals("none")) {
							if (check(kill.getPrereq().split("=")[0],
									Integer.parseInt(kill.getPrereq()
											.split("=")[1].trim()), player,
									npcname)) {
								pass = true;
							}
						} else {
							pass = true;
						}
						if (pass) {
							ItemStack talktoquest = new ItemStack(
									Material.FEATHER);
							{
								ItemMeta meta = talktoquest.getItemMeta();
								meta.setDisplayName(kill.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add(kill.getMessage());
								lore.add(Integer.toString(kill.getQuestnumber()));
								meta.setLore(lore);
								talktoquest.setItemMeta(meta);
								inv.addItem(talktoquest);
							}
						}
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
							SimpleDateFormat df = new SimpleDateFormat(
									"mm:ss");
							Date date = new Date((long) (time*1000));
							date.setHours(date.getHours()-9);
							date.setMinutes(date.getMinutes()-30);
							lore.add("Duration: " + df.format(date));
							lore.add(Integer.toString(w.getWarpid()));
							lore.add(Integer.toString(kill.getWarpid()));
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
				lore.add(npcuuid.toString());
				meta.setDisplayName("NPC Information");
				meta.setLore(lore);
				npcinfo.setItemMeta(meta);
				newinv.setItem(newinv.getSize() - 1, npcinfo);
			}
		} else {
			ItemStack npcinfo = new ItemStack(Material.BEDROCK);
			{
				ItemMeta meta = npcinfo.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(npcuuid.toString());
				meta.setDisplayName("NPC Information");
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

	@SuppressWarnings("deprecation")
	public boolean check(String type, int number, Player player, String npcname) {
		UUID playeruuid = player.getUniqueId();
		boolean pass = true;

		if (Playerstats.activequests.get(playeruuid) != null) {
			if (Playerstats.activequests.get(playeruuid).get(type) != null) {
				if (Playerstats.activequests.get(playeruuid).get(type)
						.contains(number)) {
					pass = false;
				} else {
					pass = true;
				}
			}
		}
		/**
		 * add the rewarded thingy
		 */
		if (Playerstats.completedquests.get(playeruuid) != null) {
			if (Playerstats.completedquests.get(playeruuid).get(type) != null) {
				if (Playerstats.completedquests.get(playeruuid).get(type)
						.contains(number)) {
					for (int i = 0; i < Playerstats.completedquests
							.get(playeruuid).get(type).size(); i++) {
						if (Playerstats.completedquests.get(playeruuid)
								.get(type).get(i) == number) {
							if (type.equals("kill")) {

								String reward = questers
										.returnkill(number)
										.getReward()
										.replace(
												"player",
												Bukkit.getPlayer(playeruuid)
														.getName());
								System.out.println(Ansi.ansi().fg(
										Ansi.Color.RED)
										+ "QUEST COMPLETE"
										+ Ansi.ansi().fg(Ansi.Color.WHITE));
								Bukkit.dispatchCommand(
										Bukkit.getConsoleSender(), reward);

								Playerstats.completedquests.get(playeruuid)
										.get(type).remove(i);

								player.sendMessage(ChatColor.DARK_PURPLE + "["
										+ ChatColor.GOLD + npcname
										+ ChatColor.DARK_PURPLE + "] "
										+ ChatColor.AQUA + "Enjoy your reward.");

								if (Playerstats.rewardedlist.get(playeruuid) != null) {
									if (Playerstats.rewardedlist
											.get(playeruuid).get(type) != null) {
										Playerstats.rewardedlist
												.get(playeruuid)
												.get(type)
												.put(number,
														System.currentTimeMillis());
									} else {
										HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
										temp.put(number,
												System.currentTimeMillis());
										Playerstats.rewardedlist
												.get(playeruuid)
												.put(type, temp);
									}
								} else {
									HashMap<String, HashMap<Integer, Long>> total = new HashMap<String, HashMap<Integer, Long>>();
									HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
									temp.put(number, System.currentTimeMillis());
									total.put(type, temp);
									Playerstats.rewardedlist.put(playeruuid,
											total);
								}

							}

							else if (type.equals("harvest")) {
								boolean passing = false;
								int needed = 0;
								String name = null;
								ItemStack saveditem = null;
								for (ItemStack items : player.getInventory()
										.getContents()) {
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
												if (saveditem == null) {
													saveditem = new ItemStack(
															items.getType(),
															items.getAmount(),
															items.getDurability());
												} else {
													saveditem
															.setAmount(saveditem
																	.getAmount()
																	+ items.getAmount());
													if (saveditem.getAmount() >= questers
															.returnharvest(
																	number)
															.getCount()) {

														passing = true;
														break;
													}
												}
											}

										}

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

									String reward = questers
											.returnharvest(number)
											.getReward()
											.replace(
													"player",
													Bukkit.getPlayer(playeruuid)
															.getName());
									System.out.println(Ansi.ansi().fg(
											Ansi.Color.RED)
											+ "QUEST COMPLETE"
											+ Ansi.ansi().fg(Ansi.Color.WHITE));
									Bukkit.dispatchCommand(
											Bukkit.getConsoleSender(), reward);
									player.sendMessage(ChatColor.DARK_PURPLE
											+ "[" + ChatColor.GOLD + npcname
											+ ChatColor.DARK_PURPLE + "] "
											+ ChatColor.AQUA
											+ "Enjoy your reward.");

									Playerstats.completedquests.get(playeruuid)
											.get(type).remove(i);

									if (Playerstats.rewardedlist
											.get(playeruuid) != null) {
										if (Playerstats.rewardedlist.get(
												playeruuid).get(type) != null) {
											Playerstats.rewardedlist
													.get(playeruuid)
													.get(type)
													.put(number,
															System.currentTimeMillis());
										} else {
											HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
											temp.put(number,
													System.currentTimeMillis());
											Playerstats.rewardedlist.get(
													playeruuid).put(type, temp);
										}
									} else {
										HashMap<String, HashMap<Integer, Long>> total = new HashMap<String, HashMap<Integer, Long>>();
										HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
										temp.put(number,
												System.currentTimeMillis());
										total.put(type, temp);
										Playerstats.rewardedlist.put(
												playeruuid, total);
									}

								} else {
									player.sendMessage(ChatColor.DARK_PURPLE
											+ "["
											+ ChatColor.GOLD
											+ npcname
											+ ChatColor.DARK_PURPLE
											+ "] "
											+ ChatColor.AQUA
											+ "You didn't bring me "
											+ needed
											+ " "
											+ questers.ids.get(name)
											+ ". Come back with the right amount for your reward");
									pass = false;
								}
							} else {
								String reward = questers
										.returntalkto(number)
										.getReward()
										.replace(
												"player",
												Bukkit.getPlayer(playeruuid)
														.getName());
								System.out.println(Ansi.ansi().fg(
										Ansi.Color.RED)
										+ "QUEST COMPLETE"
										+ Ansi.ansi().fg(Ansi.Color.WHITE));
								Bukkit.dispatchCommand(
										Bukkit.getConsoleSender(), reward);
								player.sendMessage(ChatColor.DARK_PURPLE + "["
										+ ChatColor.GOLD + npcname
										+ ChatColor.DARK_PURPLE + "] "
										+ ChatColor.AQUA + "Enjoy your reward.");

								Playerstats.completedquests.get(playeruuid)
										.get(type).remove(i);

								if (Playerstats.rewardedlist.get(playeruuid) != null) {
									if (Playerstats.rewardedlist
											.get(playeruuid).get(type) != null) {
										Playerstats.rewardedlist
												.get(playeruuid)
												.get(type)
												.put(number,
														System.currentTimeMillis());
									} else {
										HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
										temp.put(number,
												System.currentTimeMillis());
										Playerstats.rewardedlist
												.get(playeruuid)
												.put(type, temp);
									}
								} else {
									HashMap<String, HashMap<Integer, Long>> total = new HashMap<String, HashMap<Integer, Long>>();
									HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
									temp.put(number, System.currentTimeMillis());
									total.put(type, temp);
									Playerstats.rewardedlist.put(playeruuid,
											total);
								}
							}
						}
					}
				}
			}
		}
		if (Playerstats.rewardedlist.get(playeruuid) != null) {
			if (Playerstats.rewardedlist.get(playeruuid).get(type) != null) {
				if (Playerstats.rewardedlist.get(playeruuid).get(type)
						.get(number) != null) {
					pass = false;
				}
			}
		}
		return pass;
	}

	protected String speccieal(String id) {
		String spec = questers.ids.get(id);
		return spec;
	}

}
