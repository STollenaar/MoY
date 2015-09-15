package moy.tollenaar.stephen.Quests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.fusesource.jansi.Ansi;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.NPC.NPC;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;
import moy.tollenaar.stephen.Travel.Travel;

public class QuestClientSide {
	private QuestsServerSide questers;

	public QuestClientSide(MoY instance) {
		this.questers = instance.questers;
	}

	private HashMap<String, ArrayList<Integer>> tempav = new HashMap<String, ArrayList<Integer>>();

	// private Main plugin;

	@SuppressWarnings("deprecation")
	public void avquest(NPC npc, Player player, String npcname) {
		UUID npcuuid = npc.getUniqueId();
		int rowcount = 0;
		if (questers.GetIds("kill", npcuuid) != null) {
			rowcount += questers.GetIds("kill", npcuuid).size();
		}
		if (questers.GetIds("harvest", npcuuid) != null) {
			rowcount += questers.GetIds("harvest", npcuuid).size();
		}
		if (questers.GetIds("talkto", npcuuid) != null) {
			rowcount += questers.GetIds("talkto", npcuuid).size();
		}
		if (questers.getId(npcuuid) != -1) {
			rowcount++;
		}
		if (questers.GetIds("event", npcuuid) != null) {
			for (int numbers : questers.GetIds("event", npcuuid)) {
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

		boolean bounced = false;

		Inventory inv = Bukkit
				.createInventory(null, rowcount, "Aviable Quests");
		if (questers.GetIds("kill", npcuuid) != null) {
			for (Integer i : questers.GetIds("kill", npcuuid)) {

				QuestKill kill = questers.returnkill(i);
				if (kill != null) {
					if (kill.getState().equals("active")) {
						boolean pass = false;
						if (!kill.getPrereq().split("=")[0].equals("none")) {
							if (check(kill.getPrereq().split("=")[0],
									Integer.parseInt(kill.getPrereq()
											.split("=")[1].trim()), player,
									npcname, true, kill.getMinlvl())
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
							ItemStack killquest = new ItemStack(
									Material.DIAMOND_SWORD);
							{
								ItemMeta meta = killquest.getItemMeta();
								meta.setDisplayName(kill.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("§"
										+ Integer.toString(kill
												.getQuestnumber()));
								meta.setLore(lore);
								killquest.setItemMeta(meta);
								inv.addItem(killquest);
							}
						}
					}
				} else {
					questers.removekill(i);
				}
			}
		}
		if (questers.GetIds("harvest", npcuuid) != null) {
			for (Integer i : questers.GetIds("harvest", npcuuid)) {
				QuestHarvest kill = questers.returnharvest(i);
				if (kill != null) {
					if (kill.getState().equals("active")) {
						boolean pass = false;
						if (!bounced) {
							bounced = !rewardcheck("harvest", i, player, null);
						}
						if (!kill.getPrereq().split("=")[0].equals("none")) {
							if (check(kill.getPrereq().split("=")[0],
									Integer.parseInt(kill.getPrereq()
											.split("=")[1].trim()), player,
									npcname, true, kill.getMinlvl())
									&& check("harvest", i, player, npcname,
											false, kill.getMinlvl())) {
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
								lore.add("§"
										+ Integer.toString(kill
												.getQuestnumber()));
								meta.setLore(lore);
								harvestquest.setItemMeta(meta);
								inv.addItem(harvestquest);
							}
						}
					}
				} else {
					questers.removeharvest(i);
				}
			}
		}
		if (questers.GetIds("talkto", npcuuid) != null) {
			for (Integer i : questers.GetIds("talkto", npcuuid)) {
				QuestTalkto kill = questers.returntalkto(i);
				if (kill != null) {
					if (kill.getState().equals("active")) {
						boolean pass = false;
						if (!kill.getPrereq().split("=")[0].equals("none")) {
							if (check(kill.getPrereq().split("=")[0],
									Integer.parseInt(kill.getPrereq()
											.split("=")[1].trim()), player,
									npcname, true, kill.getMinlvl())
									&& check("talkto", i, player, npcname,
											false, kill.getMinlvl())) {
								pass = true;
							}
						} else {
							if (check("talkto", i, player, npcname, false,
									kill.getMinlvl())) {
								pass = true;
							}
						}
						if (pass) {
							ItemStack talktoquest = new ItemStack(
									Material.FEATHER);
							{
								ItemMeta meta = talktoquest.getItemMeta();
								meta.setDisplayName(kill.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("§"
										+ Integer.toString(kill
												.getQuestnumber()));
								meta.setLore(lore);
								talktoquest.setItemMeta(meta);
								inv.addItem(talktoquest);
							}
						}
					}
				} else {
					questers.removetalkto(i);
				}
			}
		}
		if (questers.getId(npcuuid) != -1) {
			// for (Integer i : questers.warplists.get(npcuuid)) {
			int i = questers.getId(npcuuid);
			Warps kill = questers.returnwarp(i);
			if (kill != null) {
				if (kill.getState().equals("active")) {
					for (String type : kill.getType()) {
						for (Warps w : Warps.TypeReturned(type)) {
							if (w.getWarpid() != kill.getWarpid()) {
								ItemStack title = new ItemStack(Material.BOAT);
								{
									ArrayList<String> lore = new ArrayList<String>();
									int time = Travel.traveltime(npcuuid,
											kill.getWarpid(), type,
											w.getStartloc());
									SimpleDateFormat df = new SimpleDateFormat(
											"mm:ss");
									Date date = new Date(time * 1000);
									date.setHours(date.getHours() - 9);
									date.setMinutes(date.getMinutes() - 30);
									lore.add("Duration: " + df.format(date));
									lore.add(type);
									lore.add("§"
											+ Integer.toString(w.getWarpid()));
									lore.add("§"
											+ Integer.toString(kill.getWarpid()));

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
			} else {
				questers.removewarp(i);
			}

		}
		if (questers.GetIds("event", npcuuid) != null) {
			for (int i : questers.GetIds("event", npcuuid)) {
				if (questers.EventCheck(i)) {
					QuestEvent event = questers.returneventquest(i);
					if (event != null) {
						if (event.getState().equals("active")) {
							if (lvlcheck(player, event.getMinlvl())) {
								ItemStack item = new ItemStack(
										Material.ENCHANTED_BOOK);
								ItemMeta meta = item.getItemMeta();
								ArrayList<String> lore = new ArrayList<String>();
								meta.setDisplayName(event.getTitle());

								try {
									EntityType.valueOf(event.getType()
											.toUpperCase());
									String build = "";
									for (String in : "eventkill".split("")) {
										build += "§" + in;
									}
									lore.add(build);
								} catch (Exception e) {
									String build = "";
									for (String in : "eventharvest".split("")) {
										build += "§" + in;
									}
									lore.add(build);
								}
								lore.add("§" + event.getNumber());
								meta.setLore(lore);
								item.setItemMeta(meta);
								inv.addItem(item);
							}
						}
					} else {
						questers.removeevent(i);
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
		} else if (!bounced) {
			questers.plugin.qinteract.dummymessage(player, npc);
		}
		tempav.clear();
	}

	public boolean check(String type, int number, Player player,
			String npcname, boolean ispreq, int minlvl) {
		UUID playeruuid = player.getUniqueId();
		boolean pass = rewardcheck(type, number, player, npcname);
		Playerstats p = questers.playerinfo.getplayer(playeruuid);
		if (p.getactivetype() != null) {
			if (p.getactives(type) != null) {
				if (p.getactives(type).contains(number)) {
					pass = false;
				}
			}
		}

		if (p.getrewardedtype() != null) {
			if (p.getrewardednumber(type) != null) {
				if (p.getrewardedtime(type, number) == 0 && ispreq) {
					pass = false;
				} else if (!ispreq && p.getrewardedtime(type, number) != 0) {
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
		Playerstats p = questers.playerinfo.getplayer(player.getUniqueId());
		if (p.getLevel() < minlvl) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("deprecation")
	private boolean rewardcheck(String type, int number, Player player,
			String npcname) {
		Playerstats p = questers.playerinfo.getplayer(player.getUniqueId());
		boolean pass = true;
		/**
		 * add the rewarded thingy
		 */
		if (p.getcompletedtype() != null) {
			if (p.getcompleted(type) != null) {
				if (p.getcompleted(type).contains(number)) {
					for (int i : p.getcompleted(type)) {

						if (i == number) {
							List<String> reward = null;
							// killquest
							if (type.equals("kill")) {
								reward = questers.returnkill(number)
										.getReward();
							} else if (type.equals("eventkill")) {
								reward = questers.returneventquest(number)
										.getReward();
							}
							// harvestquest
							else if (type.equals("harvest")
									|| type.equals("eventharvest")) {
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
										if (type.equals("harvest")) {
											if (questers.returnharvest(number)
													.getItemId().equals(name)) {
												needed = questers
														.returnharvest(number)
														.getCount();

												if (saveditem == null
														|| saveditem.getType() == Material.AIR) {
													saveditem = new ItemStack(
															items.getType(),
															0,
															items.getDurability());
												}
												if (items.getAmount() >= questers
														.returnharvest(number)
														.getCount()) {
													saveditem
															.setAmount(questers
																	.returnharvest(
																			number)
																	.getCount());
													passing = true;
													break;
												} else {
													if (saveditem.getAmount()
															+ items.getAmount() < questers
															.returnharvest(
																	number)
															.getCount()) {
														saveditem
																.setAmount(saveditem
																		.getAmount()
																		+ items.getAmount());
													} else if (saveditem
															.getAmount()
															+ items.getAmount() == questers
															.returnharvest(
																	number)
															.getCount()) {
														saveditem
																.setAmount(saveditem
																		.getAmount()
																		+ items.getAmount());
														passing = true;
														break;
													} else {
														saveditem
																.setAmount(questers
																		.returnharvest(
																				number)
																		.getCount());
														passing = true;
														break;
													}
												}

											}

										} else {
											if (questers
													.returneventquest(number)
													.getType().equals(name)) {

												if (saveditem == null
														|| saveditem.getType() == Material.AIR) {
													saveditem = new ItemStack(
															items.getType(),
															0,
															items.getDurability());
												}

												needed = questers
														.returneventquest(
																number)
														.getCount();
												if (items.getAmount() >= questers
														.returneventquest(
																number)
														.getCount()) {
													saveditem
															.setAmount(questers
																	.returnharvest(
																			number)
																	.getCount());
													passing = true;
													break;
												} else {
													if (saveditem.getAmount()
															+ items.getAmount() < questers
															.returneventquest(
																	number)
															.getCount()) {
														saveditem
																.setAmount(saveditem
																		.getAmount()
																		+ items.getAmount());
													} else if (saveditem
															.getAmount()
															+ items.getAmount() == questers
															.returneventquest(
																	number)
															.getCount()) {
														saveditem
																.setAmount(saveditem
																		.getAmount()
																		+ items.getAmount());
														passing = true;
														break;
													} else {
														saveditem
																.setAmount(questers
																		.returneventquest(
																				number)
																		.getCount());
														passing = true;
														break;
													}
												}

											}
										}
									}
								}
								if (saveditem == null
										|| saveditem.getType() == Material.AIR) {
									String[] id;
									if (type.equals("harvest")) {
										id = questers.returnharvest(number)
												.getItemId().split(":");
									} else {
										id = questers.returneventquest(number)
												.getType().split(":");
									}
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
									for (int index = 0; index < player
											.getInventory().getContents().length; index++) {
										ItemStack item = player.getInventory()
												.getContents()[index];
										if (item != null
												&& item.getType() != Material.AIR) {
											if (item.getDurability() != 0) {
												name = item.getTypeId() + ":"
														+ item.getDurability();
											} else {
												name = Integer.toString(item
														.getTypeId());
											}
											if ((type.equals("harvest") && questers
													.returnharvest(number)
													.getItemId().equals(name))
													|| (type.equals("eventharvest") && questers
															.returneventquest(
																	number)
															.getType()
															.equals(name))) {
												if (item.getAmount() > needed) {
													item.setAmount(item
															.getAmount()
															- needed);
													needed = 0;
													player.updateInventory();
												} else if (item.getAmount() == needed) {
													needed = 0;
													player.getInventory()
															.setItem(
																	index,
																	new ItemStack(
																			Material.AIR));
													player.updateInventory();
												} else {
													needed -= item.getAmount();
													player.getInventory()
															.setItem(
																	index,
																	new ItemStack(
																			Material.AIR));
													player.updateInventory();
												}
											}
										}
										if (needed == 0) {
											break;
										}
									}
									if (type.equals("harvest")) {
										reward = questers.returnharvest(number)
												.getReward();
									} else {
										reward = questers.returneventquest(
												number).getReward();
									}

								} else {
									String count;
									if (type.equals("harvest")) {
										count = Integer.toString(questers
												.returnharvest(number)
												.getCount());
									} else {
										count = Integer.toString(questers
												.returneventquest(number)
												.getCount());
									}
									if (npcname != null) {
										if (tempav.get(type) == null) {
											ArrayList<Integer> t = new ArrayList<Integer>();
											tempav.put(type, t);
										}
										if (!tempav.get(type).contains(number)) {
											player.sendMessage(ChatColor.DARK_PURPLE
													+ "["
													+ ChatColor.GOLD
													+ npcname
													+ ChatColor.DARK_PURPLE
													+ "] "
													+ ChatColor.AQUA
													+ "You didn't bring me "
													+ count
													+ " "
													+ questers
															.GetItemName(saveditem)
													+ ". Come back with the right amount for your reward");
											tempav.get(type).add(number);
										}
									}
									pass = false;
									return pass;
								}
								// talktoquest
							} else if (type.equals("talkto")) {
								reward = questers.returntalkto(number)
										.getReward();

							}
							// giving the reward
							if (reward != null && !reward.contains("unknown")) {
								System.out.println(Ansi.ansi().fg(
										Ansi.Color.RED)
										+ "QUEST COMPLETE"
										+ Ansi.ansi().fg(Ansi.Color.WHITE));

								for (String in : reward) {
									String r = in.replace("player",
											player.getName());
									Bukkit.dispatchCommand(
											Bukkit.getConsoleSender(), r);
								}
								p.deletecompleted(type, i);
								p.addrewarded(type, number,
										System.currentTimeMillis());
								questers.playerinfo.saveplayerdata(p);
								return pass;
							}
						}
					}
				}
			}
		}
		return pass;
	}

}
