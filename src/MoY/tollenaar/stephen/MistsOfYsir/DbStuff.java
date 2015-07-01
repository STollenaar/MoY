package MoY.tollenaar.stephen.MistsOfYsir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.fusesource.jansi.Ansi;

import MoY.tollenaar.stephen.PlayerInfo.Playerinfo;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.Quests.Quest;
import MoY.tollenaar.stephen.Quests.QuestTalkto;
import MoY.tollenaar.stephen.Quests.QuestsServerSide;
import MoY.tollenaar.stephen.Quests.Warps;
import MoY.tollenaar.stephen.Travel.HarborWaitLocations;
import MoY.tollenaar.stephen.Travel.TripLocations;
import code.husky.mysql.MySQL;

public class DbStuff {
	private MoY plugin;
	private Connection con;
	public MySQL MySQl;
	private QuestsServerSide questers;
	private Party party;
	private String mysqlpass;
	private String mysqluser;
	private String mysqldb;
	private String mysqlpot;
	private String mysqlhost;
	private int scheduler;
	private int dbsaver;
	private Playerinfo playerinfo;

	public void TableCreate() {
		Statement statement;
		try {
			statement = con.createStatement();
			// player database table
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_PlayerData ("

							+ "useruuid VARCHAR(45) PRIMARY KEY, "
							+ "partyname VARCHAR(50), "
							+ "partyinvite VARCHAR(50), "

							+ "woodcutting INTEGER NOT NULL, "
							+ "mining INTEGER NOT NULL, "
							+ "smelting INTEGER NOT NULL, "
							+ "cooking INTEGER NOT NULL, "
							+ "fishing INTEGER NOT NULL, "

							+ "woodcuttingprog INTEGER NOT NULL, "
							+ "miningprog INTEGER NOT NULL, "
							+ "smeltingprog INTEGER NOT NULL, "
							+ "cookingprog INTEGER NOT NULL, "
							+ "fishingprog INTEGER NOT NULL, "

							+ "abilitie INTEGER NOT NULL, "
							+ "strength INTEGER NOT NULL, "
							+ "dex INTEGER NOT NULL, "
							+ "lvl INTEGER NOT NULL, "

							+ "levelprog INTEGER NOT NULL);");

			// party database table
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_PartyData ("
							+ "partyname VARCHAR(50) PRIMARY KEY, "
							+ "partyleader VARCHAR(50) NOT NULL);");

			// npc database table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_NPCData ("
					+ "npcid INTEGER PRIMARY KEY, "
					+ "npcname VARCHAR(45) NOT NULL, "
					+ "npcskinname VARCHAR(45) NOT NULL, "
					+ "spawnlocationx INTEGER NOT NULL, "
					+ "spawnlocationy INTEGER NOT NULL, "
					+ "spawnlocationz INTEGER NOT NULL, "
					+ "spawnworld VARCHAR(45) NOT NULL, "
					+ "walking VARCHAR(6) NOT NULL, " + "killquests TEXT, "
					+ "harvestquests TEXT, " + "talktoquests TEXT, "
					+ "warps INTEGER, eventquests TEXT);");

			// active quests
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_QuestActive ("
							+ "useruuid VARCHAR(45) PRIMARY KEY, "
							+ "killquests TEXT, "
							+ "harvestquests TEXT, "
							+ "talktoquests TEXT);");

			// quest ready to recieve award
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_QuestRewardable ("
							+ "useruuid VARCHAR(45) PRIMARY KEY,"
							+ "killquests TEXT,"
							+ "harvestquests TEXT,"
							+ "talktoquests TEXT);");

			// quest completed kill
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_QuestCompleteKill ("
							+ "useruuid VARCHAR(45) PRIMARY KEY, "
							+ "questnumber TEXT, " + "timereward TEXT);");

			// quest completed harvest
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_QuestCompleteHarvest ("
							+ "useruuid VARCHAR(45) PRIMARY KEY, "
							+ "questnumber TEXT, " + "timereward TEXT);");

			// quest completed talkto
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_QuestCompleteTalkto ("
							+ "useruuid VARCHAR(45) PRIMARY KEY, "
							+ "questnumber TEXT, " + "timereward TEXT);");

			// warps
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_Warp ("
					+ "id INTEGER PRIMARY KEY, " + "title TEXT NOT NULL, "
					+ "startloc TEXT NOT NULL, "
					+ "type VARCHAR(45) NOT NULL, " + "costs DOUBLE);");

			// event boat
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_EventBoat ("
							+ "id INTEGER PRIMARY KEY,"
							+ "spawnlocation TEXT NOT NULL,"
							+ "border1 TEXT NOT NULL,"
							+ "border2 TEXT NOT NULL,"
							+ "type VARCHAR(45) NOT NULL);");

			// event cart
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_EventCart ("
							+ "id INTEGER PRIMARY KEY,"
							+ "spawnlocation TEXT NOT NULL,"
							+ "border1 TEXT NOT NULL,"
							+ "border2 TEXT NOT NULL,"
							+ "type VARCHAR(45) NOT NULL);");

			// event dragon
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_EventDragon ("
							+ "id INTEGER PRIMARY KEY,"
							+ "spawnlocation TEXT NOT NULL,"
							+ "border1 TEXT NOT NULL,"
							+ "border2 TEXT NOT NULL,"
							+ "type VARCHAR(45) NOT NULL);");

			// mining blocks
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_MiningBlocks ("
							+ "id INTEGER PRIMARY KEY,"
							+ "locationx INTEGER NOT NULL,"
							+ "locationy INTEGER NOT NULL,"
							+ "locationz INTEGER NOT NULL,"
							+ "world VARCHAR(50) NOT NULL);");
			// wood blocks
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_WoodBlocks ("
							+ "id INTEGER PRIMARY KEY,"
							+ "locationx INTEGER NOT NULL,"
							+ "locationy INTEGER NOT NULL,"
							+ "locationz INTEGER NOT NULL,"
							+ "world VARCHAR(50) NOT NULL);");

			// harborwaiting
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_Harbors ("
					+ "id INTEGER PRIMARY KEY," + "locationx INTEGER NOT NULL,"
					+ "locationy INTEGER NOT NULL,"
					+ "locationz INTEGER NOT NULL,"
					+ "world VARCHAR(50) NOT NULL,"
					+ "type VARCHAR(16) NOT NULL);");

			// trip
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_Trips ("
					+ "id INTEGER PRIMARY KEY," + "locationx INTEGER NOT NULL,"
					+ "locationy INTEGER NOT NULL,"
					+ "locationz INTEGER NOT NULL,"
					+ "world VARCHAR(50) NOT NULL,"
					+ "type VARCHAR(16) NOT NULL);");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveharbors() {
		String insert = "INSERT INTO Mist_Harbors (`id`, `type`, `locationx`, `locationy`, `locationz`, `world`) VALUES (?,?,?,?,?,?);";

		String update = "UPDATE  Mist_Harbors SET `type`=?, `locationx`=?, `locationy`=?, `locationz`=?, `world`=? WHERE `id`=?;";
		String select = "SELECT `type` FROM Mist_Harbors WHERE `id`=?;";

		for (int id : questers.AllHarbors()) {
			HarborWaitLocations h = questers.returnharbor(id);
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(select);
				pst.setInt(1, id);
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					pst.close();
					pst = con.prepareStatement(update);
					pst.setString(1, h.getType());
					pst.setInt(2, (int) h.getLocation().getX());
					pst.setInt(3, (int) h.getLocation().getY());
					pst.setInt(4, (int) h.getLocation().getZ());
					pst.setString(5, h.getLocation().getWorld().getName());
					pst.setInt(6, id);
					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setInt(1, id);
					pst.setString(2, h.getType());
					pst.setInt(3, (int) h.getLocation().getX());
					pst.setInt(4, (int) h.getLocation().getY());
					pst.setInt(5, (int) h.getLocation().getZ());
					pst.setString(6, h.getLocation().getWorld().getName());
					pst.execute();
				}
			} catch (SQLException e) {
				this.plugin.getLogger().severe(e.getMessage());
				plugin.getLogger().info(
						"There was a error during the savings of the data to the database: "
								+ e.getMessage());
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}

			finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
	}

	public void savetrips() {
		String insert = "INSERT INTO Mist_Trips (`id`, `type`, `locationx`, `locationy`, `locationz`, `world`) VALUES (?,?,?,?,?,?);";

		String update = "UPDATE  Mist_Trips SET `type`=?, `locationx`=?, `locationy`=?, `locationz`=?, `world`=? WHERE `id`=?;";
		String select = "SELECT `type` FROM Mist_Trips WHERE `id`=?;";

		for (int id : questers.AllTrips()) {
			TripLocations h = questers.returntrip(id);
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(select);
				pst.setInt(1, id);
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					pst.close();
					pst = con.prepareStatement(update);
					pst.setString(1, h.getType());
					pst.setInt(2, (int) h.getLocation().getX());
					pst.setInt(3, (int) h.getLocation().getY());
					pst.setInt(4, (int) h.getLocation().getZ());
					pst.setString(5, h.getLocation().getWorld().getName());
					pst.setInt(6, id);
					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setInt(1, id);
					pst.setString(2, h.getType());
					pst.setInt(3, (int) h.getLocation().getX());
					pst.setInt(4, (int) h.getLocation().getY());
					pst.setInt(5, (int) h.getLocation().getZ());
					pst.setString(6, h.getLocation().getWorld().getName());
					pst.execute();
				}
			} catch (SQLException e) {
				this.plugin.getLogger().severe(e.getMessage());
				plugin.getLogger().info(
						"There was a error during the savings of the data to the database: "
								+ e.getMessage());
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}

			finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
	}

	public void saveplayerdata() {
		ArrayList<UUID> players = new ArrayList<UUID>();
		players.addAll(playerinfo.getplayers());
		for (UUID player : players) {
			Playerstats p = playerinfo.getplayer(player);

			String test = "SELECT `strength` FROM `Mist_PlayerData` WHERE `useruuid` LIKE ?;";

			String insert = "INSERT INTO Mist_PlayerData (" + "`useruuid`, "
					+ "`partyname`, " + "`partyinvite`," + "`woodcutting`, "
					+ "`mining`, " + "`smelting`, " + "`cooking`, "
					+ "`fishing`, " + "`woodcuttingprog`," + "`miningprog`,"
					+ "`smeltingprog`," + "`cookingprog`," + "`fishingprog`,"
					+ "`abilitie`," + "`strength`," + "`dex`," + "`lvl`,"
					+ "`levelprog`)" + " VALUES("
					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			String update = "UPDATE Mist_PlayerData SET " + "partyname=?,"
					+ "partyinvite=?," + "woodcutting=?," + "mining=?,"
					+ "smelting=?," + "cooking=?," + "fishing=?,"
					+ "woodcuttingprog=?," + "miningprog=?,"
					+ "smeltingprog=?," + "cookingprog=?," + "fishingprog=?,"
					+ "abilitie=?," + "strength=?," + "dex=?," + "lvl=?,"
					+ "levelprog=?" + " WHERE useruuid=?";

			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(test);
				pst.setString(1, player.toString());
				ResultSet rs = pst.executeQuery();
				if (rs.next() == true) {
					pst.close();
					pst = con.prepareStatement(update);
					if (party.Partymembers.get(player) != null) {
						pst.setString(1, party.Partymembers.get(player));
					} else {
						pst.setString(1, null);
					}
					if (party.invites.get(player) != null) {
						pst.setString(2, party.invites.get(player));
					} else {
						pst.setString(2, null);
					}
					pst.setInt(3, p.getWoodcutting());
					pst.setInt(4, p.getMining());
					pst.setInt(5, p.getSmelting());
					pst.setInt(6, p.getCooking());
					pst.setInt(7, p.getFishing());

					pst.setInt(8, p.getWoodcuttingprog());
					pst.setInt(9, p.getMiningprog());
					pst.setInt(10, p.getSmeltingprog());
					pst.setInt(11, p.getCookingprog());
					pst.setInt(12, p.getFishingprog());

					pst.setInt(13, p.getAbility());
					pst.setInt(14, p.getStrength());
					pst.setInt(15, p.getDex());
					pst.setInt(16, p.getLevel());
					pst.setInt(17, p.getLevelprog());
					pst.setString(18, player.toString());

					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setString(1, player.toString());
					if (party.Partymembers.get(player) != null) {
						pst.setString(2, party.Partymembers.get(player));
					} else {
						pst.setString(2, null);
					}
					if (party.invites.get(player) != null) {
						pst.setString(3, party.invites.get(player));
					} else {
						pst.setString(3, null);
					}
					pst.setInt(4, p.getWoodcutting());
					pst.setInt(5, p.getMining());
					pst.setInt(6, p.getSmelting());
					pst.setInt(7, p.getCooking());
					pst.setInt(8, p.getFishing());

					pst.setInt(9, p.getWoodcuttingprog());
					pst.setInt(10, p.getMiningprog());
					pst.setInt(11, p.getSmeltingprog());
					pst.setInt(12, p.getCookingprog());
					pst.setInt(13, p.getFishingprog());

					pst.setInt(14, p.getAbility());
					pst.setInt(15, p.getStrength());
					pst.setInt(16, p.getDex());
					pst.setInt(17, p.getLevel());
					pst.setInt(18, p.getLevelprog());

					pst.execute();
				}

			} catch (SQLException e) {
				this.plugin.getLogger().severe(e.getMessage());
				plugin.getLogger().info(
						"There was a error during the savings of the data to the database: "
								+ e.getMessage());
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}

			finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
	}

	public void savepartydata() {
		ArrayList<String> partys = new ArrayList<String>();
		partys.addAll(party.Partyleaders.keySet());
		for (String part : partys) {
			String test = "SELECT `partyleader` FROM Mist_PartyData WHERE `partyname`=?";

			String insert = "INSERT INTO Mist_PartyData (" + "`partyname`,"
					+ "`partyleader`) " + "VALUES (" + "?,?);";

			String update = "UPDATE Mist_PartyData SET `partyleader`=? WHERE `partyname`=?;";
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(test);
				pst.setString(1, part);
				ResultSet rs = pst.executeQuery();
				if (rs.next() == false) {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setString(1, part);
					pst.setString(2, party.Partyleaders.get(part).toString());

					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(update);
					pst.setString(1, party.Partyleaders.get(part).toString());
					pst.setString(2, part);

					pst.execute();
				}
			} catch (SQLException e) {
				this.plugin.getLogger().severe(e.getMessage());
				plugin.getLogger().info(
						"There was a error during the savings of the data to the database: "
								+ e.getMessage());
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
	}

	public void saveactivequest() {
		ArrayList<UUID> players = new ArrayList<UUID>();
		players.addAll(playerinfo.getplayers());
		for (UUID player : players) {
			Playerstats p = playerinfo.getplayer(player);
			String insert = "INSERT INTO Mist_QuestActive (" + "`useruuid`,"
					+ "`killquests`," + "`harvestquests`,"
					+ "`talktoquests`) VALUES" + "(?,?,?,?);";

			String update = "UPDATE Mist_QuestActive SET"
					+ "`killquests`=?, `harvestquests`=?, `talktoquests`=? WHERE `useruuid`=?;";

			String test = "SELECT * FROM Mist_QuestActive WHERE `useruuid`=?;";
			PreparedStatement pst = null;
			if (p.getactivetype() != null) {
				try {
					pst = con.prepareStatement(test);
					pst.setString(1, player.toString());
					ResultSet rs = pst.executeQuery();
					if (rs.next() == false) {
						pst.close();
						pst = con.prepareStatement(insert);
						pst.setString(1, player.toString());
						if (p.getactives("kill") != null) {
							String quests = null;
							for (int number : p.getactives("kill")) {
								String toadd = number
										+ "-"
										+ questers.returnProgress(player,
												"kill", number);
								if (quests == null) {
									quests = toadd + "_";
								} else {
									quests += toadd + "_";
								}
							}

							pst.setString(2, quests);
						} else {
							pst.setString(2, null);
						}
						if (p.getactives("harvest") != null) {
							String quests = null;
							for (int number : p.getactives("harvest")) {
								String toadd = number
										+ "-"
										+ questers.returnProgress(player,
												"harvest", number);
								if (quests == null) {
									quests = toadd + "_";
								} else {
									quests += toadd + "_";
								}
							}
							pst.setString(3, quests);
						} else {
							pst.setString(3, null);
						}
						if (p.getactives("talkto") != null) {
							String quests = null;
							for (int number : p.getactives("talkto")) {
								String toadd = Integer.toString(number);
								if (quests == null) {
									quests = toadd + "_";
								} else {
									quests += toadd + "_";
								}
							}
							pst.setString(4, quests);
						} else {
							pst.setString(4, null);
						}
						pst.execute();

					} else {
						pst.close();
						pst = con.prepareStatement(update);

						if (p.getactives("kill") != null) {
							String quests = null;
							for (int number : p.getactives("kill")) {
								String toadd = number
										+ "-"
										+ questers.returnProgress(player,
												"kill", number);
								if (quests == null) {
									quests = toadd + "_";
								} else {
									quests += toadd + "_";
								}
							}

							pst.setString(1, quests);
						} else {
							pst.setString(1, null);
						}
						if (p.getactives("harvest") != null) {
							String quests = null;
							for (int number : p.getactives("harvest")) {
								String toadd = number
										+ "-"
										+ questers.returnProgress(player,
												"harvest", number);
								if (quests == null) {
									quests = toadd + "_";
								} else {
									quests += toadd + "_";
								}
							}
							pst.setString(2, quests);
						} else {
							pst.setString(2, null);
						}
						if (p.getactives("talkto") != null) {
							String quests = null;
							for (int number : p.getactives("talkto")) {
								String toadd = Integer.toString(number);
								if (quests == null) {
									quests = toadd + "_";
								} else {
									quests += toadd + "_";
								}
							}
							pst.setString(3, quests);
						} else {
							pst.setString(3, null);
						}
						pst.setString(4, player.toString());

						pst.execute();
					}
				} catch (SQLException e) {
					this.plugin.getLogger().severe(e.getMessage());
					plugin.getLogger().info(
							"There was a error during the savings of the data to the database: "
									+ e.getMessage());
					try {
						if (pst != null) {
							pst.close();
						}
					} catch (SQLException ex) {
						System.out.println(ex.getStackTrace());
					}
				} finally {
					try {
						if (pst != null) {
							pst.close();
						}
					} catch (SQLException ex) {
						System.out.println(ex.getStackTrace());
					}
				}
			}
		}
	}

	public void saverewardable() {
		String insert = "INSERT INTO Mist_QuestRewardable (" + "`useruuid`,"
				+ "`killquests`," + "`harvestquests`,"
				+ "`talktoquests`) VALUES" + "(?,?,?,?);";

		String update = "UPDATE Mist_QuestRewardable SET"
				+ "`killquests`=?, `harvestquests`=?, `talktoquests`=? WHERE `useruuid`=?;";

		String test = "SELECT * FROM Mist_QuestRewardable WHERE `useruuid`=?;";
		ArrayList<UUID> players = new ArrayList<UUID>();
		players.addAll(playerinfo.getplayers());
		for (UUID player : players) {
			Playerstats p = playerinfo.getplayer(player);
			if (p.getcompletedtype() != null) {
				PreparedStatement pst = null;
				try {
					pst = con.prepareStatement(test);
					pst.setString(1, player.toString());
					ResultSet rs = pst.executeQuery();
					if (rs.next() == false) {
						pst.close();
						pst = con.prepareStatement(insert);
						pst.setString(1, player.toString());
						if (p.getcompleted("kill") != null) {
							String quests = null;
							for (int number : p.getcompleted("kill")) {

								if (quests == null) {
									quests = number + "_";
								} else {
									quests += number + "_";
								}
							}

							pst.setString(2, quests);
						} else {
							pst.setString(2, null);
						}
						if (p.getcompleted("harvest") != null) {
							String quests = null;
							for (int number : p.getcompleted("harvest")) {
								if (quests == null) {
									quests = number + "_";
								} else {
									quests += number + "_";
								}
							}
							pst.setString(3, quests);
						} else {
							pst.setString(3, null);
						}
						if (p.getcompleted("talkto") != null) {
							String quests = null;
							for (int number : p.getcompleted("talkto")) {
								String toadd = Integer.toString(number);
								if (quests == null) {
									quests = toadd + "_";
								} else {
									quests += toadd + "_";
								}
							}
							pst.setString(4, quests);
						} else {
							pst.setString(4, null);
						}

						pst.execute();
					} else {
						pst.close();
						pst = con.prepareStatement(update);
						if (p.getcompleted("kill") != null) {
							String quests = null;
							for (int number : p.getcompleted("kill")) {

								if (quests == null) {
									quests = number + "_";
								} else {
									quests += number + "_";
								}
							}

							pst.setString(1, quests);
						} else {
							pst.setString(1, null);
						}
						if (p.getcompleted("harvest") != null) {
							String quests = null;
							for (int number : p.getcompleted("harvest")) {

								if (quests == null) {
									quests = number + "_";
								} else {
									quests += number + "_";
								}
							}
							pst.setString(2, quests);
						} else {
							pst.setString(2, null);
						}
						if (p.getcompleted("talkto") != null) {
							String quests = null;
							for (int number : p.getcompleted("talkto")) {
								String toadd = Integer.toString(number);
								if (quests == null) {
									quests = toadd + "_";
								} else {
									quests += toadd + "_";
								}
							}
							pst.setString(3, quests);
						} else {
							pst.setString(3, null);
						}
						pst.setString(4, player.toString());

						pst.execute();
					}
				} catch (SQLException e) {
					this.plugin.getLogger().severe(e.getMessage());
					plugin.getLogger().info(
							"There was a error during the savings of the data to the database: "
									+ e.getMessage());
					try {
						if (pst != null) {
							pst.close();
						}
					} catch (SQLException ex) {
						System.out.println(ex.getStackTrace());
					}
				} finally {
					try {
						if (pst != null) {
							pst.close();
						}
					} catch (SQLException ex) {
						System.out.println(ex.getStackTrace());
					}
				}
			}
		}
	}

	public void savecompletekill() {

		String insert = "INSERT INTO Mist_QuestCompleteKill (" + "`useruuid` ,"
				+ "`questnumber`," + "`timereward`) VALUES" + "(?,?,?);";

		String update = "UPDATE Mist_QuestCompleteKill SET "
				+ "`questnumber`=?, `timereward`=? WHERE `useruuid`=?;";

		String test = "SELECT * FROM Mist_QuestCompleteKill WHERE `useruuid`=?;";

		ArrayList<UUID> players = new ArrayList<UUID>();
		players.addAll(playerinfo.getplayers());
		for (UUID player : players) {
			Playerstats p = playerinfo.getplayer(player);
			if (p.getrewardedtype() != null) {
				if (p.getrewardednumber("kill") != null) {
					PreparedStatement pst = null;
					try {
						pst = con.prepareStatement(test);
						pst.setString(1, player.toString());
						ResultSet rs = pst.executeQuery();
						if (rs.next() == false) {
							pst.close();
							pst = con.prepareStatement(insert);
							pst.setString(1, player.toString());
							ArrayList<Integer> quests = new ArrayList<Integer>();
							quests.addAll(p.getrewardednumber("kill"));
							String questnumb = null;
							String time = null;
							for (Integer quest : quests) {
								if (questnumb != null) {
									questnumb = questnumb + quest + "_";
								} else {
									questnumb = Integer.toString(quest) + "_";
								}
								if (time == null) {
									time = Long.toString(p.getrewardedtime(
											"kill", quest)) + "_";
								} else {
									time = time
											+ Long.toString(p.getrewardedtime(
													"kill", quest)) + "_";

								}
							}
							pst.setString(2, questnumb);
							pst.setString(3, time);

							pst.execute();
						} else {
							pst.close();
							pst = con.prepareStatement(update);

							ArrayList<Integer> quests = new ArrayList<Integer>();
							quests.addAll(p.getrewardednumber("kill"));
							String questnumb = null;
							String time = null;
							for (Integer quest : quests) {
								if (questnumb != null) {
									questnumb = questnumb + quest + "_";
								} else {
									questnumb = Integer.toString(quest) + "_";
								}
								if (time == null) {
									time = Long.toString(p.getrewardedtime(
											"kill", quest))

									+ "_";
								} else {
									time = time
											+ Long.toString(p.getrewardedtime(
													"kill", quest)

											) + "_";

								}
							}
							pst.setString(1, questnumb);
							pst.setString(2, time);
							pst.setString(3, player.toString());
							pst.execute();
						}
					} catch (SQLException e) {
						this.plugin.getLogger().severe(e.getMessage());
						plugin.getLogger().info(
								"There was a error during the savings of the data to the database: "
										+ e.getMessage());
						try {
							if (pst != null) {
								pst.close();
							}
						} catch (SQLException ex) {
							System.out.println(ex.getStackTrace());
						}
					} finally {
						try {
							if (pst != null) {
								pst.close();
							}
						} catch (SQLException ex) {
							System.out.println(ex.getStackTrace());
						}
					}

				}
			}
		}
	}

	public void savecompleteharvest() {

		String insert = "INSERT INTO Mist_QuestCompleteHarvest ("
				+ "`useruuid` ," + "`questnumber`," + "`timereward`) VALUES"
				+ "(?,?,?);";

		String update = "UPDATE Mist_QuestCompleteHarvest SET "
				+ "`questnumber`=?, `timereward`=? WHERE `useruuid`=?;";

		String test = "SELECT * FROM Mist_QuestCompleteHarvest WHERE `useruuid`=?;";

		ArrayList<UUID> players = new ArrayList<UUID>();
		players.addAll(playerinfo.getplayers());
		for (UUID player : players) {
			Playerstats p = playerinfo.getplayer(player);
			if (p.getrewardedtype() != null) {
				if (p.getrewardednumber("harvest") != null) {
					PreparedStatement pst = null;
					try {
						pst = con.prepareStatement(test);
						pst.setString(1, player.toString());
						ResultSet rs = pst.executeQuery();
						if (rs.next() == false) {
							pst.close();
							pst = con.prepareStatement(insert);
							pst.setString(1, player.toString());
							ArrayList<Integer> quests = new ArrayList<Integer>();
							quests.addAll(p.getrewardednumber("harvest"));
							String questnumb = null;
							String time = null;
							for (Integer quest : quests) {
								if (questnumb != null) {
									questnumb = questnumb + quest + "_";
								} else {
									questnumb = Integer.toString(quest) + "_";
								}
								if (time == null) {
									time = Long.toString(p.getrewardedtime(
											"harvest", quest)) + "_";
								} else {
									time = time
											+ Long.toString(p.getrewardedtime(
													"harvest", quest)) + "_";

								}
							}
							pst.setString(2, questnumb);
							pst.setString(3, time);

							pst.execute();
						} else {
							pst.close();
							pst = con.prepareStatement(update);

							ArrayList<Integer> quests = new ArrayList<Integer>();
							quests.addAll(p.getrewardednumber("harvest"));
							String questnumb = null;
							String time = null;
							for (Integer quest : quests) {
								if (questnumb != null) {
									questnumb = questnumb + quest + "_";
								} else {
									questnumb = Integer.toString(quest) + "_";
								}
								if (time == null) {
									time = Long.toString(p.getrewardedtime(
											"harvest", quest)) + "_";
								} else {
									time = time
											+ Long.toString(p.getrewardedtime(
													"harvest", quest)) + "_";
								}
							}
							pst.setString(1, questnumb);
							pst.setString(2, time);
							pst.setString(3, player.toString());
							pst.execute();
						}
					} catch (SQLException e) {
						this.plugin.getLogger().severe(e.getMessage());
						plugin.getLogger().info(
								"There was a error during the savings of the data to the database: "
										+ e.getMessage());
						try {
							if (pst != null) {
								pst.close();
							}
						} catch (SQLException ex) {
							System.out.println(ex.getStackTrace());
						}
					} finally {
						try {
							if (pst != null) {
								pst.close();
							}
						} catch (SQLException ex) {
							System.out.println(ex.getStackTrace());
						}
					}

				}
			}
		}
	}

	public void savecompletetalkto() {

		String insert = "INSERT INTO Mist_QuestCompleteTalkto ("
				+ "`useruuid` ," + "`questnumber`," + "`timereward`) VALUES"
				+ "(?,?,?);";

		String update = "UPDATE Mist_QuestCompleteTalkto SET "
				+ "`questnumber`=?, `timereward`=? WHERE `useruuid`=?;";

		String test = "SELECT * FROM Mist_QuestCompleteTalkto WHERE `useruuid`=?;";

		ArrayList<UUID> players = new ArrayList<UUID>();
		players.addAll(playerinfo.getplayers());
		for (UUID player : players) {
			Playerstats p = playerinfo.getplayer(player);
			if (p.getrewardedtype() != null) {
				if (p.getrewardednumber("talkto") != null) {
					PreparedStatement pst = null;
					try {
						pst = con.prepareStatement(test);
						pst.setString(1, player.toString());
						ResultSet rs = pst.executeQuery();
						if (rs.next() == false) {
							pst.close();
							pst = con.prepareStatement(insert);
							pst.setString(1, player.toString());
							ArrayList<Integer> quests = new ArrayList<Integer>();
							quests.addAll(p.getrewardednumber("talkto"));
							String questnumb = null;
							String time = null;
							for (Integer quest : quests) {
								if (questnumb != null) {
									questnumb = questnumb + quest + "_";
								} else {
									questnumb = Integer.toString(quest) + "_";
								}
								if (time == null) {
									time = Long.toString(p.getrewardedtime(
											"talkto", quest)) + "_";
								} else {
									time = time
											+ Long.toString(p.getrewardedtime(
													"talkto", quest)) + "_";

								}
							}
							pst.setString(2, questnumb);
							pst.setString(3, time);

							pst.execute();
						} else {
							pst.close();
							pst = con.prepareStatement(update);

							ArrayList<Integer> quests = new ArrayList<Integer>();
							quests.addAll(p.getrewardednumber("talkto"));

							String questnumb = null;
							String time = null;
							for (Integer quest : quests) {
								if (questnumb != null) {
									questnumb = questnumb + quest + "_";
								} else {
									questnumb = Integer.toString(quest) + "_";
								}
								if (time == null) {
									time = Long.toString(p.getrewardedtime(
											"talkto", quest)) + "_";
								} else {
									time = time
											+ Long.toString(p.getrewardedtime(
													"talkto", quest)) + "_";

								}
							}
							pst.setString(1, questnumb);
							pst.setString(2, time);
							pst.setString(3, player.toString());
							pst.execute();
						}
					} catch (SQLException e) {
						this.plugin.getLogger().severe(e.getMessage());
						plugin.getLogger().info(
								"There was a error during the savings of the data to the database: "
										+ e.getMessage());
						try {
							if (pst != null) {
								pst.close();
							}
						} catch (SQLException ex) {
							System.out.println(ex.getStackTrace());
						}
					} finally {
						try {
							if (pst != null) {
								pst.close();
							}
						} catch (SQLException ex) {
							System.out.println(ex.getStackTrace());
						}
					}
				}
			}
		}
	}

	public void savewarp() {
		String insert = "INSERT INTO Mist_Warp (" + "`id`," + "`title`,"
				+ "`startloc`," + "`type`," + "`costs`) VALUES"
				+ "(?,?,?,?,?);";

		String update = "UPDATE Mist_Warp SET"
				+ "`title`=?, `startloc`=?, `type`=?,"
				+ "`costs`=? WHERE `id`=?;";

		String test = "SELECT * FROM Mist_Warp WHERE `id`=?;";
		for (int warps : questers.AllWarpId()) {
			Warps warp = questers.returnwarp(warps);
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(test);
				pst.setInt(1, warp.getWarpid());
				ResultSet rs = pst.executeQuery();
				if (rs.next() == false) {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setInt(1, warp.getWarpid());
					pst.setString(2, warp.getName());
					String fake = warp.getStartloc().getX() + "-"
							+ warp.getStartloc().getY() + "-"
							+ warp.getStartloc().getZ() + "-"
							+ warp.getStartloc().getWorld().getName();
					pst.setString(3, fake);
					pst.setString(4, warp.GetType());
					pst.setDouble(5, warp.getCosts());

					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(update);
					pst.setString(1, warp.getName());
					String fake = warp.getStartloc().getX() + "-"
							+ warp.getStartloc().getY() + "-"
							+ warp.getStartloc().getZ() + "-"
							+ warp.getStartloc().getWorld().getName();
					pst.setString(2, fake);
					pst.setString(3, warp.GetType());
					pst.setDouble(4, warp.getCosts());
					pst.setInt(5, warp.getWarpid());
					pst.execute();
				}
			} catch (SQLException e) {
				this.plugin.getLogger().severe(e.getMessage());
				plugin.getLogger().info(
						"There was a error during the savings of the data to the database: "
								+ e.getMessage());
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
	}

	public void saveeventboat() {
		String insert = "INSERT INTO Mist_EventBoat (" + "`id`,"
				+ "`spawnlocation`," + "`border1`," + "`border2`,"
				+ "`type`) VALUES (" + "?,?,?,?,?);";

		String update = "UPDATE Mist_EventBoat SET" + "`spawnlocation`=?,"
				+ "`border1`=?," + "`border2`=?," + "`type`=? WHERE `id`=?;";

		String test = "SELECT * FROM Mist_EventBoat WHERE `id`=?;";
		ArrayList<Integer> events = new ArrayList<Integer>();
		events.addAll(plugin.boat.eventlocations.keySet());
		for (Integer event : events) {
			ArrayList<String> info = plugin.boat.eventlocations.get(event);
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(test);
				pst.setInt(1, event);
				ResultSet rs = pst.executeQuery();
				if (rs.next() == false) {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setInt(1, event);
					pst.setString(2, info.get(0));
					pst.setString(3, info.get(1));
					pst.setString(4, info.get(2));
					pst.setString(5, info.get(3));

					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(update);
					pst.setString(1, info.get(0));
					pst.setString(2, info.get(1));
					pst.setString(3, info.get(2));
					pst.setString(4, info.get(3));
					pst.setInt(5, event);

					pst.execute();
				}
			} catch (SQLException e) {
				this.plugin.getLogger().severe(e.getMessage());
				plugin.getLogger().info(
						"There was a error during the savings of the data to the database: "
								+ e.getMessage());
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
	}

	public void saveeventcart() {
		String insert = "INSERT INTO Mist_EventCart (" + "`id`,"
				+ "`spawnlocation`," + "`border1`," + "`border2`,"
				+ "`type`) VALUES (" + "?,?,?,?,?);";

		String update = "UPDATE Mist_EventCart SET" + "`spawnlocation`=?,"
				+ "`border1`=?," + "`border2`=?," + "`type`=? WHERE `id`=?;";

		String test = "SELECT * FROM Mist_EventCart WHERE `id`=?;";
		ArrayList<Integer> events = new ArrayList<Integer>();
		events.addAll(plugin.cart.eventlocations.keySet());
		for (Integer event : events) {
			ArrayList<String> info = plugin.cart.eventlocations.get(event);
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(test);
				pst.setInt(1, event);
				ResultSet rs = pst.executeQuery();
				if (rs.next() == false) {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setInt(1, event);
					pst.setString(2, info.get(0));
					pst.setString(3, info.get(1));
					pst.setString(4, info.get(2));
					pst.setString(5, info.get(3));

					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(update);
					pst.setString(1, info.get(0));
					pst.setString(2, info.get(1));
					pst.setString(3, info.get(2));
					pst.setString(4, info.get(3));
					pst.setInt(5, event);

					pst.execute();
				}
			} catch (SQLException e) {
				this.plugin.getLogger().severe(e.getMessage());
				plugin.getLogger().info(
						"There was a error during the savings of the data to the database: "
								+ e.getMessage());
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
	}

	public void saveeventdragon() {
		String insert = "INSERT INTO Mist_EventDragon (" + "`id`,"
				+ "`spawnlocation`," + "`border1`," + "`border2`,"
				+ "`type`) VALUES (" + "?,?,?,?,?);";

		String update = "UPDATE Mist_EventDragon SET" + "`spawnlocation`=?,"
				+ "`border1`=?," + "`border2`=?," + "`type`=? WHERE `id`=?;";

		String test = "SELECT * FROM Mist_EventDragon WHERE `id`=?;";
		ArrayList<Integer> events = new ArrayList<Integer>();
		events.addAll(plugin.dragon.eventlocations.keySet());
		for (Integer event : events) {
			ArrayList<String> info = plugin.dragon.eventlocations.get(event);
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(test);
				pst.setInt(1, event);
				ResultSet rs = pst.executeQuery();
				if (rs.next() == false) {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setInt(1, event);
					pst.setString(2, info.get(0));
					pst.setString(3, info.get(1));
					pst.setString(4, info.get(2));
					pst.setString(5, info.get(3));

					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(update);
					pst.setString(1, info.get(0));
					pst.setString(2, info.get(1));
					pst.setString(3, info.get(2));
					pst.setString(4, info.get(3));
					pst.setInt(5, event);

					pst.execute();
				}
			} catch (SQLException e) {
				this.plugin.getLogger().severe(e.getMessage());
				plugin.getLogger().info(
						"There was a error during the savings of the data to the database: "
								+ e.getMessage());
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
	}

	public void savenpcdata() {
		String insert = "INSERT INTO Mist_NPCData (" + "`npcid`,"
				+ "`npcname`," + "`spawnlocationx`," + "`spawnlocationy`,"
				+ "`spawnlocationz`, " + "`walking`," + "`killquests`,"
				+ "`harvestquests`," + "`talktoquests`," + "`warps`, "
				+ "`spawnworld`, `npcskinname`, `eventquests`) VALUES ("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?);";

		String update = "UPDATE Mist_NPCData SET" + "`npcname`=?,"
				+ "`spawnlocationx`=?," + "`spawnlocationy`=?,"
				+ "`spawnlocationz`=?," + "`walking`=?," + "`killquests`=?,"
				+ "`harvestquests`=?," + "`talktoquests`=?," + "`warps`=?, "
				+ "`spawnworld`=?, `npcskinname`=? , `eventquests`=? WHERE `npcid`=?;";

		String test = "SELECT * FROM Mist_NPCData WHERE `npcid`=?;";
		for (Integer npcid : questers.uniquenpcid.keySet()) {
			UUID npc = questers.uniquenpcid.get(npcid);
			String killquestids = null;
			if (questers.killquests.get(npc) != null) {
				for (Integer killquest : questers.killquests.get(npc)) {
					if (killquestids == null) {
						killquestids = killquest + "_";
					} else {
						killquestids += killquest + "_";
					}
				}
			}
			String harvestquestids = null;
			if (questers.harvestquests.get(npc) != null) {
				for (Integer harvestquest : questers.harvestquests.get(npc)) {
					if (harvestquestids == null) {
						harvestquestids = harvestquest + "_";
					} else {
						harvestquestids += harvestquest + "_";
					}
				}
			}
			String talktoquestids = null;
			if (questers.talktoquests.get(npc) != null) {
				for (Integer talktoquest : questers.talktoquests.get(npc)) {
					if (talktoquestids == null) {
						talktoquestids = talktoquest + "_";
					} else {
						talktoquestids += talktoquest + "_";
					}
				}
			}
			String warpids = null;
			if (questers.warplists.get(npc) != null) {
				warpids = Integer.toString(questers.warplists.get(npc));
			}
			
			String eventquestids = null;
			if(questers.eventquests.get(npc) != null){
				for(Integer quests : questers.eventquests.get(npc)){
					if(eventquestids == null){
						eventquestids = quests + "_";
					}else{
						eventquestids += quests + "_";
					}
				}
			}

			NPCRegistry registry = CitizensAPI.getNPCRegistry();
			NPC pc = registry.getByUniqueId(npc);
			String name = pc.getName();
			int x = (int) questers.spawnlocation.get(npc).getX();
			int y = (int) questers.spawnlocation.get(npc).getY();
			int z = (int) questers.spawnlocation.get(npc).getZ();
			String world = questers.spawnlocation.get(npc).getWorld().getName();
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(test);
				pst.setInt(1, npcid);
				ResultSet rs = pst.executeQuery();
				if (rs.next() == false) {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setInt(1, npcid);
					pst.setString(2, name);
					pst.setInt(3, x);
					pst.setInt(4, y);
					pst.setInt(5, z);
					if (questers.activenpc.get(npc) != null) {
						pst.setString(6, "true");
					} else {
						pst.setString(6, "false");
					}
					pst.setString(7, killquestids);
					pst.setString(8, harvestquestids);
					pst.setString(9, talktoquestids);
					pst.setString(10, warpids);
					pst.setString(11, world);
					pst.setString(
							12,
							(String) pc.data().get(
									NPC.PLAYER_SKIN_UUID_METADATA));
					pst.setString(13, eventquestids);
					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(update);

					pst.setString(1, name);
					pst.setInt(2, x);
					pst.setInt(3, y);
					pst.setInt(4, z);
					if (questers.activenpc.get(npc) != null) {
						pst.setString(5, "true");
					} else {
						pst.setString(5, "false");
					}
					pst.setString(6, killquestids);
					pst.setString(7, harvestquestids);
					pst.setString(8, talktoquestids);
					pst.setString(9, warpids);
					pst.setString(10, world);
					pst.setString(
							11,
							(String) pc.data().get(
									NPC.PLAYER_SKIN_UUID_METADATA));
					pst.setString(12, eventquestids);
					pst.setInt(13, npcid);
					pst.execute();
				}
			} catch (SQLException e) {
				this.plugin.getLogger().severe(e.getMessage());
				plugin.getLogger().info(
						"There was a error during the savings of the data to the database: "
								+ e.getMessage());
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			}
		}

	}

	public void savemining() {
		String insert = "INSERT INTO Mist_MiningBlocks (" + "`id`,"
				+ "`locationx`," + "`locationy`," + "`locationz`,"
				+ "`world`) VALUES (" + "?,?,?,?,?);";

		String update = "UPDATE Mist_MiningBlocks SET" + "`locationx`=?, "
				+ "`locationy`=?," + "`locationz`=?,"
				+ "`world`=? WHERE `id`=?;";

		String test = "SELECT * FROM Mist_MiningBlocks WHERE `id`=?;";

		PreparedStatement pst = null;
		HashSet<Block> blocks = plugin.skill.miningblocksplaces;
		ArrayList<Block> blockindex = new ArrayList<Block>();
		blockindex.addAll(blocks);
		for (int index = 0; index < blocks.size() - 1; index++) {
			try {
				pst = con.prepareStatement(test);

				pst.setInt(1, index);
				ResultSet rs = pst.executeQuery();
				if (rs.next() == false) {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setInt(1, index);
					pst.setInt(2, (int) blockindex.get(index).getLocation()
							.getX());
					pst.setInt(3, (int) blockindex.get(index).getLocation()
							.getY());
					pst.setInt(4, (int) blockindex.get(index).getLocation()
							.getZ());
					pst.setString(5, blockindex.get(index).getLocation()
							.getWorld().getName());

					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(update);

					pst.setInt(1, (int) blockindex.get(index).getLocation()
							.getX());
					pst.setInt(2, (int) blockindex.get(index).getLocation()
							.getY());
					pst.setInt(3, (int) blockindex.get(index).getLocation()
							.getZ());
					pst.setString(4, blockindex.get(index).getLocation()
							.getWorld().getName());
					pst.setInt(5, index);

					pst.execute();
				}

			} catch (SQLException e) {
				e.printStackTrace();
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}

			}
		}
	}

	public void savewood() {
		String insert = "INSERT INTO Mist_WoodBlocks (" + "`id`,"
				+ "`locationx`," + "`locationy`," + "`locationz`,"
				+ "`world`) VALUES (" + "?,?,?,?,?);";

		String update = "UPDATE Mist_WoodBlocks SET" + "`locationx`=?, "
				+ "`locationy`=?," + "`locationz`=?,"
				+ "`world`=? WHERE `id`=?;";

		String test = "SELECT * FROM Mist_WoodBlocks WHERE `id`=?;";

		PreparedStatement pst = null;
		HashSet<Block> blocks = plugin.skill.woodblocksplaces;
		ArrayList<Block> blockindex = new ArrayList<Block>();
		blockindex.addAll(blocks);
		for (int index = 0; index < blocks.size() - 1; index++) {
			try {
				pst = con.prepareStatement(test);

				pst.setInt(1, index);
				ResultSet rs = pst.executeQuery();
				if (rs.next() == false) {
					pst.close();
					pst = con.prepareStatement(insert);
					pst.setInt(1, index);
					pst.setInt(2, (int) blockindex.get(index).getLocation()
							.getX());
					pst.setInt(3, (int) blockindex.get(index).getLocation()
							.getY());
					pst.setInt(4, (int) blockindex.get(index).getLocation()
							.getZ());
					pst.setString(5, blockindex.get(index).getLocation()
							.getWorld().getName());

					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(update);

					pst.setInt(1, (int) blockindex.get(index).getLocation()
							.getX());
					pst.setInt(2, (int) blockindex.get(index).getLocation()
							.getY());
					pst.setInt(3, (int) blockindex.get(index).getLocation()
							.getZ());
					pst.setString(4, blockindex.get(index).getLocation()
							.getWorld().getName());
					pst.setInt(5, index);

					pst.execute();
				}

			} catch (SQLException e) {
				e.printStackTrace();
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException ex) {
					System.out.println(ex.getStackTrace());
				}

			}
		}
	}

	public void saveall() {
		plugin.getLogger().info(
				Ansi.ansi().fg(Ansi.Color.GREEN) + "SAVING"
						+ Ansi.ansi().fg(Ansi.Color.WHITE));
		saveplayerdata();
		savepartydata();
		saveactivequest();
		saverewardable();
		savecompletekill();
		savecompleteharvest();
		savecompletetalkto();
		savewarp();
		saveeventboat();
		saveeventcart();
		saveeventdragon();
		savenpcdata();
		savemining();
		savewood();
		saveharbors();
		savetrips();
		plugin.getLogger().info(
				Ansi.ansi().fg(Ansi.Color.GREEN) + "SAVING COMPLETE"
						+ Ansi.ansi().fg(Ansi.Color.WHITE));
	}

	public void loadharbors() {
		String loading = "SELECT * FROM Mist_Harbors;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String type = rs.getString("type");
				int x = rs.getInt("locationx");
				int y = rs.getInt("locationy");
				int z = rs.getInt("locationz");
				String world = rs.getString("world");
				Location l = new Location(Bukkit.getWorld(world), x, y, z);
				questers.loadhardbor(id, type, l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadtrips() {
		String loading = "SELECT * FROM Mist_Trips;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String type = rs.getString("type");
				int x = rs.getInt("locationx");
				int y = rs.getInt("locationy");
				int z = rs.getInt("locationz");
				String world = rs.getString("world");
				Location l = new Location(Bukkit.getWorld(world), x, y, z);
				questers.loadtrip(id, type, l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadpartydata() {
		String loading = "SELECT * FROM Mist_PartyData;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				UUID player = UUID.fromString(rs.getString("partyleader"));
				party.Partyleaders.put(rs.getString("partyname"), player);
				Set<UUID> members = new HashSet<UUID>();
				members.add(player);
				party.Partyswithmembers.put(rs.getString("partyname"), members);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadplayerstats() {
		String loading = "SELECT * FROM Mist_PlayerData;";
		PreparedStatement pst = null;

		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				UUID player = UUID.fromString(rs.getString("useruuid"));
				if (rs.getString("partyname") != null) {
					party.Partymembers.put(player, rs.getString("partyname"));
					Set<UUID> members = party.Partyswithmembers.get(rs
							.getString("partyname"));
					members.add(player);
					party.Partyswithmembers.put(rs.getString("partyname"),
							members);
					party.partymatchmaking.put(player, true);
				}
				if (rs.getString("partyinvite") != null) {
					party.invites.put(player, rs.getString("partyinvite"));
				}
				Playerstats p = playerinfo.createplayer(player);

				p.setWoodcutting(rs.getInt("woodcutting"));
				p.setMining(rs.getInt("mining"));
				p.setSmelting(rs.getInt("smelting"));
				p.setCooking(rs.getInt("cooking"));
				p.setFishing(rs.getInt("fishing"));

				p.setWoodcuttingprog(rs.getInt("woodcuttingprog"));
				p.setMiningprog(rs.getInt("miningprog"));
				p.setSmeltingprog(rs.getInt("smeltingprog"));
				p.setCookingprog(rs.getInt("cookingprog"));
				p.setFishingprog(rs.getInt("fishingprog"));

				p.setAbility(rs.getInt("abilitie"));
				p.setStrength(rs.getInt("strength"));
				p.setDex(rs.getInt("dex"));
				p.setLevel(rs.getInt("lvl"));
				p.setLevelprog(rs.getInt("levelprog"));
				playerinfo.saveplayerdata(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}

	}

	public void loadeventboat() {
		String loading = "SELECT * FROM Mist_EventBoat;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				ArrayList<String> info = new ArrayList<String>();
				info.add(rs.getString("spawnlocation"));
				info.add(rs.getString("border1"));
				info.add(rs.getString("border2"));
				info.add(rs.getString("type"));
				plugin.boat.eventlocations.put(rs.getInt("id"), info);
				plugin.boat.playersatevent.put(rs.getInt("id"), null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}

	}

	public void loadeventcart() {
		String loading = "SELECT * FROM Mist_EventCart;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				ArrayList<String> info = new ArrayList<String>();
				info.add(rs.getString("spawnlocation"));
				info.add(rs.getString("border1"));
				info.add(rs.getString("border2"));
				info.add(rs.getString("type"));
				plugin.cart.eventlocations.put(rs.getInt("id"), info);
				plugin.cart.playersatevent.put(rs.getInt("id"), null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadeventdragon() {
		String loading = "SELECT * FROM Mist_EventDragon;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				ArrayList<String> info = new ArrayList<String>();
				info.add(rs.getString("spawnlocation"));
				info.add(rs.getString("border1"));
				info.add(rs.getString("border2"));
				info.add(rs.getString("type"));
				plugin.dragon.eventlocations.put(rs.getInt("id"), info);
				plugin.dragon.playersatevent.put(rs.getInt("id"), null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadwarp() {
		String loading = "SELECT * FROM Mist_Warp;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int number = rs.getInt("id");
				String title = rs.getString("title");
				String[] trans = rs.getString("startloc").split("-");
				Location startloc = new Location(Bukkit.getWorld(trans[3]),
						Double.parseDouble(trans[0]),
						Double.parseDouble(trans[1]),
						Double.parseDouble(trans[2]));

				double costs = rs.getDouble("costs");
				String type = rs.getString("type");
				questers.loadwarps(number, title, startloc, costs, type);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadnpc() {
		String loading = "SELECT * FROM Mist_NPCData;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Location spawn = new Location(Bukkit.getWorld(rs
						.getString("spawnworld")), rs.getInt("spawnlocationx"),
						rs.getInt("spawnlocationy"),
						rs.getInt("spawnlocationz"));
				questers.spawnNpc(spawn, rs.getString("npcname"),
						rs.getInt("npcid"), rs.getString("npcskinname"));
				UUID npc = questers.uniquenpcid.get(rs.getInt("npcid"));
				for (UUID tu : questers.talktoquests.keySet()) {
					for (Integer qn : questers.talktoquests.get(tu)) {
						QuestTalkto talk = questers.returntalkto(qn);
						if (talk.getNpcid() == rs.getInt("npcid")) {
							questers.targetnpcs.put(npc, qn);
							break;
						}
					}
				}
				if (rs.getString("killquests") != null) {
					String[] killtemp = rs.getString("killquests").split("_");
					HashSet<Integer> quests = new HashSet<Integer>();
					for (String t : killtemp) {
						quests.add(Integer.parseInt(t.trim()));
					}
					questers.killquests.put(npc, quests);
				}
				if (rs.getString("harvestquests") != null) {
					String[] killtemp = rs.getString("harvestquests")
							.split("_");
					HashSet<Integer> quests = new HashSet<Integer>();
					for (String t : killtemp) {
						quests.add(Integer.parseInt(t.trim()));
					}
					questers.harvestquests.put(npc, quests);
				}
				if (rs.getString("talktoquests") != null) {
					String[] killtemp = rs.getString("talktoquests").split("_");
					HashSet<Integer> quests = new HashSet<Integer>();
					for (String t : killtemp) {
						quests.add(Integer.parseInt(t.trim()));
					}
					questers.talktoquests.put(npc, quests);
				}
				if (rs.getString("warps") != null) {
					questers.warplists.put(npc, rs.getInt("warps"));
				}
				
				if(rs.getString("eventquests") != null){
					String[] temp = rs.getString("eventquests").split("_");
					HashSet<Integer> quests = new HashSet<Integer>();
					for(String in : temp){
						quests.add(Integer.parseInt(in.trim()));
					}
					questers.eventquests.put(npc, quests);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadactivequests() {
		String loading = "SELECT * FROM Mist_QuestActive;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				UUID player = UUID.fromString(rs.getString("useruuid"));
				Playerstats p = playerinfo.getplayer(player);
				HashMap<String, HashMap<Integer, Integer>> progress;
				if (Quest.progress.get(player) != null) {
					progress = Quest.progress.get(player);
				} else {
					progress = new HashMap<String, HashMap<Integer, Integer>>();
				}

				if (rs.getString("killquests") != null) {
					HashMap<Integer, Integer> amount;
					if (progress.get("kill") != null) {
						amount = progress.get("kill");
					} else {
						amount = new HashMap<Integer, Integer>();
					}
					String[] allkill = rs.getString("killquests").split("_");
					for (String in : allkill) {
						String[] prog = in.split("-");
						p.addactive("kill",Integer.parseInt(prog[0]));
						amount.put(Integer.parseInt(prog[0]),
								Integer.parseInt(prog[1]));
					}
					progress.put("kill", amount);
			
				}
				if (rs.getString("harvestquests") != null) {
					HashMap<Integer, Integer> amount;
					if (progress.get("harvest") != null) {
						amount = progress.get("harvest");
					} else {
						amount = new HashMap<Integer, Integer>();
					}
					String[] allharvest = rs.getString("harvestquests").split(
							"_");
					for (String in : allharvest) {
						String[] prog = in.split("-");
						p.addactive("harvest", Integer.parseInt(prog[0]));
						amount.put(Integer.parseInt(prog[0]),
								Integer.parseInt(prog[1]));
					}
					progress.put("harvest", amount);
				}
				if (rs.getString("talktoquests") != null) {
					String[] allkill = rs.getString("talktoquests").split("_");
					for (String in : allkill) {
						p.addactive("talkto", Integer.parseInt(in));
					}
				}
				playerinfo.saveplayerdata(p);
				Quest.progress.put(player, progress);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadrewardable() {
		String loading = "SELECT * FROM Mist_QuestRewardable;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				UUID player = UUID.fromString(rs.getString("useruuid"));
				Playerstats p = playerinfo.getplayer(player);
				if (rs.getString("killquests") != null) {
					String[] allkill = rs.getString("killquests").split("_");
					for (String in : allkill) {
						p.addcompleted("kill", Integer.parseInt(in));
					}
				}
				if (rs.getString("harvestquests") != null) {
					String[] allkill = rs.getString("harvestquests").split("_");
					for (String in : allkill) {
					p.addcompleted("harvest", Integer.parseInt(in));
					}
				}
				if (rs.getString("talktoquests") != null) {
					String[] allkill = rs.getString("talktoquests").split("_");
					for (String in : allkill) {
						p.addcompleted("talkto", Integer.parseInt(in));
					}
				}
				playerinfo.saveplayerdata(p);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadcompletekill() {
		String loading = "SELECT * FROM Mist_QuestCompleteKill;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				if (rs.getString("questnumber") != null) {
					UUID player = UUID.fromString(rs.getString("useruuid"));
					Playerstats p = playerinfo.getplayer(player);
					playerinfo.saveplayerdata(p);
					String[] questnumbers = rs.getString("questnumber").split(
							"_");
					String[] loggedtimes = rs.getString("timereward")
							.split("_");
					for(int x = 0; x < questnumbers.length; x++){
						p.addrewarded("kill", Integer.parseInt(questnumbers[x]), Long.parseLong(loggedtimes[x]));
					}
					playerinfo.saveplayerdata(p);
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadcompleteharvest() {
		String loading = "SELECT * FROM Mist_QuestCompleteHarvest;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				UUID player = UUID.fromString(rs.getString("useruuid"));
				Playerstats p = playerinfo.getplayer(player);
				if (rs.getString("questnumber") != null) {
					String[] questnumbers = rs.getString("questnumber").split(
							"_");
					String[] loggedtimes = rs.getString("timereward")
							.split("_");
					for(int x = 0; x < questnumbers.length; x++){
						p.addrewarded("harvest", Integer.parseInt(questnumbers[x]), Long.parseLong(loggedtimes[x]));
					}
				}
				playerinfo.saveplayerdata(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadcompletetalkto() {
		String loading = "SELECT * FROM Mist_QuestCompleteTalkto;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				UUID player = UUID.fromString(rs.getString("useruuid"));
				Playerstats p = playerinfo.getplayer(player);
				if (rs.getString("questnumber") != null) {
					String[] questnumbers = rs.getString("questnumber").split(
							"_");
					String[] loggedtimes = rs.getString("timereward")
							.split("_");
					for(int x = 0; x < questnumbers.length; x++){
						p.addrewarded("talkto", Integer.parseInt(questnumbers[x]), Long.parseLong(loggedtimes[x]));
					}
				}
				playerinfo.saveplayerdata(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void loadmining() {
		String loading = "SELECT * FROM Mist_MiningBlocks;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				World world = Bukkit.getWorld(rs.getString("world"));
				int x = rs.getInt("locationx");
				int y = rs.getInt("locationy");
				int z = rs.getInt("locationz");
				Location blockloc = new Location(world, x, y, z);
				Block block = blockloc.getBlock();
				plugin.skill.miningblocksplaces.add(block);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}

		}
	}

	public void loadwood() {
		String loading = "SELECT * FROM Mist_WoodBlocks;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(loading);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				World world = Bukkit.getWorld(rs.getString("world"));
				int x = rs.getInt("locationx");
				int y = rs.getInt("locationy");
				int z = rs.getInt("locationz");
				Location blockloc = new Location(world, x, y, z);
				Block block = blockloc.getBlock();
				plugin.skill.woodblocksplaces.add(block);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}

		}
	}

	public void loadall() {
		plugin.getLogger().info(
				Ansi.ansi().fg(Ansi.Color.GREEN) + "LOADING"
						+ Ansi.ansi().fg(Ansi.Color.WHITE));
		opencon();

		loadpartydata();
		loadplayerstats();

		loadeventboat();
		loadeventcart();
		loadeventdragon();
		loadwarp();

		loadnpc();
		loadactivequests();
		loadrewardable();

		loadcompletekill();
		loadcompleteharvest();
		loadcompletetalkto();

		loadmining();
		loadwood();
		loadharbors();
		loadtrips();
		plugin.getLogger().info(
				Ansi.ansi().fg(Ansi.Color.GREEN) + "LOADING COMPLETE"
						+ Ansi.ansi().fg(Ansi.Color.WHITE));
		closecon();

	}

	public void closecon() {
		int timeout = 10;

		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
					@Override
					public void run() {
						PreparedStatement pst;
						try {
							pst = con
									.prepareStatement("SELECT useruuid FROM `Mist_PlayerData` LIMIT 1;");
							pst.execute();
						} catch (SQLException ex) {
							System.out.println(ex.getMessage());
						}
					}
				}, 0L, timeout * 20L);
		scheduler = id;
	}

	public void dbsaveclock() {
		int minutes = Calendar.getInstance().get(Calendar.MINUTE);
		int nearest = ((minutes) / 60) * 60 % 60;
		if (nearest == 0 || minutes > 30) {
			nearest = 60;
		}
		int needed = minutes - nearest;
		if (needed < 0) {
			needed = -needed;
		}
		int id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
				new Runnable() {
					@Override
					public void run() {
						saveall();
						plugin.getLogger().info(
								ChatColor.GREEN + "Back-up complete.");
						dbsaveclock();
					}
				}, needed);
		dbsaver = id;
		dbsaveclock();
	}

	public void opencon() {
		Bukkit.getScheduler().cancelTask(scheduler);
		Bukkit.getScheduler().cancelTask(dbsaver);
	}

	public void intvar() {
		mysqlpass = plugin.getConfig().getString("mysqlpass");
		mysqluser = plugin.getConfig().getString("mysqluser");
		mysqldb = plugin.getConfig().getString("mysqldb");
		mysqlpot = plugin.getConfig().getString("mysqlport");
		mysqlhost = plugin.getConfig().getString("mysqlhost");
		MySQl = new MySQL(plugin, mysqlhost, mysqlpot, mysqldb, mysqluser,
				mysqlpass);
	}

	public DbStuff(MoY instance) {
		this.plugin = instance;
		this.questers = instance.questers;
		this.party = instance.party;
		this.playerinfo = instance.playerinfo;
	}

	public void deletetrip(int id) {
		String test = "SELECT * FROM Mist_Trips WHERE `id`=?";
		String delete = "DELETE FROM Mist_Trips WHERE `id`=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				pst.close();
				pst = con.prepareStatement(delete);
				pst.setInt(1, id);
				pst.execute();
			}
		} catch (SQLException e) {
			this.plugin.getLogger().severe(e.getMessage());
			plugin.getLogger().info(
					"There was a error during the savings of the data to the database: "
							+ e.getMessage());
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void deleteharbor(int id) {
		String test = "SELECT * FROM Mist_Harbors WHERE `id`=?";
		String delete = "DELETE FROM Mist_Harbors WHERE `id`=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				pst.close();
				pst = con.prepareStatement(delete);
				pst.setInt(1, id);
				pst.execute();
			}
		} catch (SQLException e) {
			this.plugin.getLogger().severe(e.getMessage());
			plugin.getLogger().info(
					"There was a error during the savings of the data to the database: "
							+ e.getMessage());
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void deletenpc(int npcid) {
		String test = "SELECT * FROM Mist_NPCData WHERE `npcid`=?;";
		String delete = "DELETE FROM Mist_NPCData WHERE `npcid`=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setInt(1, npcid);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				pst.close();
				pst = con.prepareStatement(delete);
				pst.setInt(1, npcid);
				pst.execute();
			}
		} catch (SQLException e) {
			this.plugin.getLogger().severe(e.getMessage());
			plugin.getLogger().info(
					"There was a error during the savings of the data to the database: "
							+ e.getMessage());
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}

	}

	public void deletewarp(int id) {
		String test = "SELECT * FROM Mist_Warp WHERE `id`=?;";
		String delete = "DELETE FROM Mist_Warp WHERE `id`=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				pst.close();
				pst = con.prepareStatement(delete);
				pst.setInt(1, id);
				pst.execute();
			}
		} catch (SQLException e) {
			this.plugin.getLogger().severe(e.getMessage());
			plugin.getLogger().info(
					"There was a error during the savings of the data to the database: "
							+ e.getMessage());
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void deleteeventboat(int id) {
		String test = "SELECT * FROM Mist_EventBoat WHERE `id`=?;";
		String delete = "DELETE FROM Mist_EventBoat WHERE `id`=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				pst.close();
				pst = con.prepareStatement(delete);
				pst.setInt(1, id);
				pst.execute();
			}
		} catch (SQLException e) {
			this.plugin.getLogger().severe(e.getMessage());
			plugin.getLogger().info(
					"There was a error during the savings of the data to the database: "
							+ e.getMessage());
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void deleteeventcart(int id) {
		String test = "SELECT * FROM Mist_EventCart WHERE `id`=?;";
		String delete = "DELETE FROM Mist_EventCart WHERE `id`=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				pst.close();
				pst = con.prepareStatement(delete);
				pst.setInt(1, id);
				pst.execute();
			}
		} catch (SQLException e) {
			this.plugin.getLogger().severe(e.getMessage());
			plugin.getLogger().info(
					"There was a error during the savings of the data to the database: "
							+ e.getMessage());
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void deleteeventdragon(int id) {
		String test = "SELECT * FROM Mist_EventDragon WHERE `id`=?;";
		String delete = "DELETE FROM Mist_EventDragon WHERE `id`=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				pst.close();
				pst = con.prepareStatement(delete);
				pst.setInt(1, id);
				pst.execute();
			}
		} catch (SQLException e) {
			this.plugin.getLogger().severe(e.getMessage());
			plugin.getLogger().info(
					"There was a error during the savings of the data to the database: "
							+ e.getMessage());
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void deleteparty(String name) {
		String test = "SELECT * FROM Mist_PartyData WHERE `partyname`=?;";
		String delete = "DELETE FROM Mist_PartyData WHERE `partyname`=?;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setString(1, name);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				pst.close();
				pst = con.prepareStatement(delete);
				pst.setString(1, name);
				pst.execute();
			}
		} catch (SQLException e) {
			this.plugin.getLogger().severe(e.getMessage());
			plugin.getLogger().info(
					"There was a error during the deleting of the data to the database: "
							+ e.getMessage());
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		}
	}

	public void deletecomtalk(String id, String playeruuid) {
		String update = "UPDATE Mist_QuestCompleteTalkto SET "
				+ "`questnumber`=?, `timereward`=? WHERE `useruuid`=?;";
		String test = "SELECT * FROM Mist_QuestCompleteTalkto WHERE `useruuid`=?;";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setString(1, playeruuid);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getString("questnumber") != null) {
					String[] nt = rs.getString("questnumber").split("_");
					String[] lt = rs.getString("timereward").split("_");
					ArrayList<String> numbers = new ArrayList<String>();
					ArrayList<String> logged = new ArrayList<String>();
					for (String in : nt) {
						numbers.add(in);
					}
					for (String in : lt) {
						logged.add(in);
					}
					for (int i = 0; i < numbers.size(); i++) {
						if (numbers.get(i).equals(id)) {
							numbers.remove(i);
							logged.remove(i);
							String newnumber = null;
							for (String in : numbers) {
								if (newnumber == null) {
									newnumber = in + "_";
								} else {
									newnumber += in + "_";
								}
							}
							String newlogged = null;
							for (String in : logged) {
								if (newlogged == null) {
									newlogged = in + "_";
								} else {
									newlogged += in + "_";
								}
							}
							pst.close();
							pst = con.prepareStatement(update);
							pst.setString(1, newnumber);
							pst.setString(2, newlogged);
							pst.setString(3, playeruuid);
							pst.execute();
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}

		}
	}

	public void deletecomkill(String id, String playeruuid) {
		String update = "UPDATE Mist_QuestCompleteKill SET "
				+ "`questnumber`=?, `timereward`=? WHERE `useruuid`=?;";

		String test = "SELECT * FROM Mist_QuestCompleteKill WHERE `useruuid`=?;";

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setString(1, playeruuid);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getString("questnumber") != null) {
					String[] nt = rs.getString("questnumber").split("_");
					String[] lt = rs.getString("timereward").split("_");
					ArrayList<String> numbers = new ArrayList<String>();
					ArrayList<String> logged = new ArrayList<String>();
					for (String in : nt) {
						numbers.add(in);
					}
					for (String in : lt) {
						logged.add(in);
					}
					for (int i = 0; i < numbers.size(); i++) {
						if (numbers.get(i).equals(id)) {
							numbers.remove(i);
							logged.remove(i);
							String newnumber = null;
							for (String in : numbers) {
								if (newnumber == null) {
									newnumber = in + "_";
								} else {
									newnumber += in + "_";
								}
							}
							String newlogged = null;
							for (String in : logged) {
								if (newlogged == null) {
									newlogged = in + "_";
								} else {
									newlogged += in + "_";
								}
							}
							pst.close();
							pst = con.prepareStatement(update);
							pst.setString(1, newnumber);
							pst.setString(2, newlogged);
							pst.setString(3, playeruuid);
							pst.execute();
							break;
						}
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}

		}
	}

	public void deletecomhar(String id, String playeruuid) {
		String update = "UPDATE Mist_QuestCompleteHarvest SET "
				+ "`questnumber`=?, `timereward`=? WHERE `useruuid`=?;";

		String test = "SELECT * FROM Mist_QuestCompleteHarvest WHERE `useruuid`=?;";

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(test);
			pst.setString(1, playeruuid);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getString("questnumber") != null) {
					String[] nt = rs.getString("questnumber").split("_");
					String[] lt = rs.getString("timereward").split("_");
					ArrayList<String> numbers = new ArrayList<String>();
					ArrayList<String> logged = new ArrayList<String>();
					for (String in : nt) {
						numbers.add(in);
					}
					for (String in : lt) {
						logged.add(in);
					}
					for (int i = 0; i < numbers.size(); i++) {
						if (numbers.get(i).equals(id)) {
							numbers.remove(i);
							logged.remove(i);
							String newnumber = null;
							for (String in : numbers) {
								if (newnumber == null) {
									newnumber = in + "_";
								} else {
									newnumber += in + "_";
								}
							}
							String newlogged = null;
							for (String in : logged) {
								if (newlogged == null) {
									newlogged = in + "_";
								} else {
									newlogged += in + "_";
								}
							}
							pst.close();
							pst = con.prepareStatement(update);
							pst.setString(1, newnumber);
							pst.setString(2, newlogged);
							pst.setString(3, playeruuid);
							pst.execute();
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getStackTrace());
			}

		}
	}

	public void setcon(Connection connect) {
		con = connect;
	}
}
