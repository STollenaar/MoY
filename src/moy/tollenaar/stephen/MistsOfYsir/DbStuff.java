package moy.tollenaar.stephen.MistsOfYsir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.fusesource.jansi.Ansi;

import moy.tollenaar.stephen.NPC.NPC;
import moy.tollenaar.stephen.NPC.NPCEntity;
import moy.tollenaar.stephen.NPC.NPCHandler;
import moy.tollenaar.stephen.NPC.NPCMetadataStore;
import moy.tollenaar.stephen.PlayerInfo.Party;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;
import moy.tollenaar.stephen.Quests.QuestTalkto;
import moy.tollenaar.stephen.Quests.QuestsServerSide;
import moy.tollenaar.stephen.Quests.Warps;
import moy.tollenaar.stephen.Travel.HarborWaitLocations;
import moy.tollenaar.stephen.Travel.Travel;
import moy.tollenaar.stephen.Travel.TripLocations;
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
	private Travel travel;

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
							+ "enchanting INTEGER NOT NULL, "
							+ "alchemy INTEGER NOT NULL,"

							+ "woodcuttingprog INTEGER NOT NULL, "
							+ "miningprog INTEGER NOT NULL, "
							+ "smeltingprog INTEGER NOT NULL, "
							+ "cookingprog INTEGER NOT NULL, "
							+ "fishingprog INTEGER NOT NULL, "
							+ "enchantingprog INTEGER NOT NULL, "
							+ "alchemyprog INTEGER NOT NULL, "

							+ "abilitie INTEGER NOT NULL, "
							+ "strength INTEGER NOT NULL, "
							+ "dex INTEGER NOT NULL, "
							+ "lvl INTEGER NOT NULL, "

							+ "levelprog INTEGER NOT NULL," + "libray TEXT);");

			// party database table
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_PartyData ("
							+ "partyname VARCHAR(50) PRIMARY KEY, "
							+ "partyleader VARCHAR(50) NOT NULL);");

			// npc database table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_NPCData ("
					+ "npcid INTEGER PRIMARY KEY, "
					+ "npcname VARCHAR(45) NOT NULL, "
					+ "npcskinname VARCHAR(45) NOT NULL,"
					+ "active  TINYINT(1) NOT NULL,"
					+ "spawnlocationx INTEGER NOT NULL, "
					+ "spawnlocationy INTEGER NOT NULL, "
					+ "spawnlocationz INTEGER NOT NULL, "
					+ "spawnworld VARCHAR(45) NOT NULL, " + "killquests TEXT, "
					+ "harvestquests TEXT, " + "talktoquests TEXT, "
					+ "warps INTEGER, eventquests TEXT);");

			// active quests
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_QuestActive ("
							+ "useruuid VARCHAR(45) PRIMARY KEY, "
							+ "killquests TEXT, "
							+ "harvestquests TEXT, "
							+ "talktoquests TEXT," + "eventquests TEXT);");

			// quest ready to recieve award
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_QuestRewardable ("
							+ "useruuid VARCHAR(45) PRIMARY KEY,"
							+ "killquests TEXT,"
							+ "harvestquests TEXT,"
							+ "talktoquests TEXT," + "eventquests TEXT);");

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
			
			// quest completed event
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_QuestCompleteEvent ("
							+ "useruuid VARCHAR(45) PRIMARY KEY, "
							+ "questnumber TEXT, " + "timereward TEXT);");

			// quest completed talkto
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_QuestCompleteTalkto ("
							+ "useruuid VARCHAR(45) PRIMARY KEY, "
							+ "questnumber TEXT, " + "timereward TEXT);");

			// warps
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_Warp ("
							+ "id INTEGER PRIMARY KEY, "
							+ "title TEXT NOT NULL, "
							+ "startloc TEXT NOT NULL, "
							+ "type VARCHAR(45) NOT NULL, state VARCHAR(45) NOT NULL,  "
							+ "costs DOUBLE, "
							+ "overrideTime VARCHAR(10) NOT NULL, overrideOnly TINYINT(1) NOT NULL, overrideID TEXT);");

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

			// npc merchant
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Mist_Shop ("
					+ "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
					+ "name VARCHAR(45) NOT NULL,"
					+ "shopname VARCHAR(45) NOT NULL," + "x INTEGER NOT NULL,"
					+ "y INTEGER NOT NULL," + "z INTEGER NOT NULL,"
					+ "world VARCHAR(45) NOT NULL);");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveshops() {
		String insert = "INSERT INTO Mist_Shop (`name`, `shopname`, `x`, `y`, `z`, `world`) VALUES (?,?,?,?,?,?);";
		String update = "UPDATE Mist_Shop SET `name`=?, `shopname`=?, `x`=?, `y`=?, `z`=?, `world`=? WHERE `id`=?;";

		NPCHandler handler = plugin.getNPCHandler();
		for (UUID uuid : handler.getStores()) {
			NPCEntity npc = handler.getNPCByUUID(uuid);
			NPCMetadataStore stored = (NPCMetadataStore) npc.getBukkitEntity()
					.getMetadata("NPC").get(0).value();
			int id = stored.getId();
			String name = stored.getOwner();
			String shop = stored.getShop();
			int x = npc.getCurrentloc().getBlockX();
			int y = npc.getCurrentloc().getBlockY();
			int z = npc.getCurrentloc().getBlockZ();
			String world = npc.getCurrentloc().getWorld().getName();
			PreparedStatement pst = null;

			if (id != -1) {
				try {
					pst = con.prepareStatement(update);
					pst.setString(1, name);
					pst.setString(2, shop);
					pst.setInt(3, x);
					pst.setInt(4, y);
					pst.setInt(5, z);
					pst.setString(6, world);
					pst.setInt(stored.getId(), id);
					pst.execute();
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
			} else {
				try {
					pst = con.prepareStatement(insert);
					pst.setString(1, name);
					pst.setString(2, shop);
					pst.setInt(3, x);
					pst.setInt(4, y);
					pst.setInt(5, z);
					pst.setString(6, world);
					pst.execute();
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
	}

	public void saveharbors() {
		String insert = "INSERT INTO Mist_Harbors (`id`, `type`, `locationx`, `locationy`, `locationz`, `world`) VALUES (?,?,?,?,?,?);";

		String update = "UPDATE  Mist_Harbors SET `type`=?, `locationx`=?, `locationy`=?, `locationz`=?, `world`=? WHERE `id`=?;";
		String select = "SELECT `type` FROM Mist_Harbors WHERE `id`=?;";
		for (int id : travel.AllHarbors()) {
			HarborWaitLocations h = travel.GetHarbor(id);
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

		for (int id : travel.AllTrips()) {
			TripLocations h = travel.GetTrip(id);
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

	public void savecompleted() {
		String test = "SELECT * FROM Mist_QuestRewardable WHERE `useruuid` like ?;";
		String insert = "INSERT INTO Mist_QuestRewardable (`useruuid`, `killquests`, `harvestquests`, `talktoquests`, `eventquests`) VALUES (?,?,?,?,?);";
		String update = "UPDATE Mist_QuestRewardable SET killquests=?, harvestquests=?, talktoquests=?, eventquests=? WHERE useruuid=?;";
		for (UUID player : playerinfo.getplayers()) {
			Playerstats p = playerinfo.getplayer(player);
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(test);
				pst.setString(1, player.toString());
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					pst.close();
					pst = con.prepareStatement(update);
					String kill = "";
					String harvest = "";
					String talkto = "";
					String event = "";
					for (String type : p.getcompletedtype()) {
						switch (type) {
						case "kill":
							for (int numbers : p.getcompleted(type)) {
								kill += numbers + "_";
							}
							break;
						case "harvest":
							for (int numbers : p.getcompleted(type)) {
								harvest += numbers + "_";
							}
							break;
						case "talkto":
							for (int numbers : p.getcompleted(type)) {
								talkto += numbers + "_";
							}
							break;
						case "event":
						case "eventharvest":
						case "eventkill":
							for (int numbers : p.getcompleted(type)) {
								event += numbers + "_";
							}
							break;
						}
					}
					pst.setString(1, kill);
					pst.setString(2, harvest);
					pst.setString(3, talkto);
					pst.setString(4, event);
					pst.setString(5, player.toString());
					pst.execute();

				} else {
					pst.close();
					pst = con.prepareStatement(insert);
					String kill = "";
					String harvest = "";
					String talkto = "";
					String event = "";
					for (String type : p.getcompletedtype()) {
						switch (type) {
						case "kill":
							for (int numbers : p.getcompleted(type)) {
								kill += numbers + "_";
							}
							break;
						case "harvest":
							for (int numbers : p.getcompleted(type)) {
								harvest += numbers + "_";
							}
							break;
						case "talkto":
							for (int numbers : p.getcompleted(type)) {
								talkto += numbers + "_";
							}
							break;
						case "event":
						case "eventharvest":
						case "eventkill":
							for (int numbers : p.getcompleted(type)) {
								event += numbers + "_";
							}
							break;
						}
					}
					pst.setString(2, kill);
					pst.setString(3, harvest);
					pst.setString(4, talkto);
					pst.setString(5, event);
					pst.setString(1, player.toString());
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

	public void saveactivequest() {
		String test = "SELECT * FROM Mist_QuestActive WHERE `useruuid` like ?;";
		String insert = "INSERT INTO Mist_QuestActive (`useruuid`, `killquests`, `harvestquests`, `talktoquests`, `eventquests`) VALUES (?,?,?,?,?);";
		String update = "UPDATE Mist_QuestActive SET killquests=?, harvestquests=?, talktoquests=?, eventquests=? WHERE useruuid=?;";
		for (UUID player : playerinfo.getplayers()) {
			Playerstats p = playerinfo.getplayer(player);
			PreparedStatement pst = null;
			try {
				pst = con.prepareStatement(test);
				pst.setString(1, player.toString());
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					pst.close();
					pst = con.prepareStatement(update);
					String kill = "";
					String harvest = "";
					String talkto = "";
					String event = "";
					for (String type : p.getactivetype()) {
						switch (type) {
						case "kill":
							for (int numbers : p.getactives(type)) {
								kill += numbers + "-" + questers.getProgress(player).get(type).get(numbers) +  "_";
							}
							break;
						case "harvest":
							for (int numbers : p.getactives(type)) {
								harvest += numbers + "-" + questers.getProgress(player).get(type).get(numbers) +  "_";
							}
							break;
						case "talkto":
							for (int numbers : p.getactives(type)) {
								talkto += numbers + "_";
							}
							break;
						case "event":
						case "eventharvest":
						case "eventkill":
							for (int numbers : p.getactives(type)) {
								event += numbers + "_";
							}
							break;
						}
					}
					pst.setString(1, kill);
					pst.setString(2, harvest);
					pst.setString(3, talkto);
					pst.setString(4, event);
					pst.setString(5, player.toString());
					pst.execute();

				} else {
					pst.close();
					pst = con.prepareStatement(insert);
					String kill = "";
					String harvest = "";
					String talkto = "";
					String event = "";
					for (String type : p.getactivetype()) {
						switch (type) {
						case "kill":
							for (int numbers : p.getactives(type)) {
								kill += numbers + "_";
							}
							break;
						case "harvest":
							for (int numbers : p.getactives(type)) {
								harvest += numbers + "_";
							}
							break;
						case "talkto":
							for (int numbers : p.getactives(type)) {
								talkto += numbers + "_";
							}
							break;
						case "event":
						case "eventharvest":
						case "eventkill":
							for (int numbers : p.getactives(type)) {
								event += numbers + "-" +questers.getProgress(player).get(type).get(numbers) +  "_";
							}
							break;
						}
					}
					pst.setString(2, kill);
					pst.setString(3, harvest);
					pst.setString(4, talkto);
					pst.setString(5, event);
					pst.setString(1, player.toString());
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

			String insert = "INSERT INTO Mist_PlayerData ("
					+ "`useruuid`, "
					+ "`partyname`, "
					+ "`partyinvite`,"
					+ "`woodcutting`, "
					+ "`mining`, "
					+ "`smelting`, "
					+ "`cooking`, "
					+ "`fishing`, "
					+ "`woodcuttingprog`,"
					+ "`miningprog`,"
					+ "`smeltingprog`,"
					+ "`cookingprog`,"
					+ "`fishingprog`,"
					+ "`abilitie`,"
					+ "`strength`,"
					+ "`dex`,"
					+ "`lvl`,"
					+ "`levelprog`, `enchanting`, `enchantingprog`, `alchemy`, `alchemyprog`, `library`)"
					+ " VALUES("
					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			String update = "UPDATE Mist_PlayerData SET "
					+ "partyname=?,"
					+ "partyinvite=?,"
					+ "woodcutting=?,"
					+ "mining=?,"
					+ "smelting=?,"
					+ "cooking=?,"
					+ "fishing=?,"
					+ "woodcuttingprog=?,"
					+ "miningprog=?,"
					+ "smeltingprog=?,"
					+ "cookingprog=?,"
					+ "fishingprog=?,"
					+ "abilitie=?,"
					+ "strength=?,"
					+ "dex=?,"
					+ "lvl=?,"
					+ "levelprog=?, enchanting=?, enchantingprog=?, alchemy=?, alchemyprog=?, library=?"
					+ " WHERE useruuid=?";

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
					pst.setInt(18, p.getEnchanting());
					pst.setInt(19, p.getEnchantingprog());
					pst.setInt(20, p.getAlchemy());
					pst.setInt(21, p.getAlchemyprog());
					String lib = "";
					if (p.getBooks() != null && p.getBooks().size() != 0) {
						for (String in : p.getBooks()) {
							lib += in + "-";
						}
					}
					pst.setString(22, lib);
					pst.setString(23, player.toString());

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
					pst.setInt(19, p.getEnchanting());
					pst.setInt(20, p.getEnchantingprog());
					pst.setInt(21, p.getAlchemy());
					pst.setInt(22, p.getAlchemyprog());
					String lib = "";
					if (p.getBooks() != null && p.getBooks().size() != 0) {
						for (String in : p.getBooks()) {
							lib += in + "-";
						}
					}
					pst.setString(23, lib);
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

	public void savewarp() {
		String insert = "INSERT INTO Mist_Warp ("
				+ "`id`,"
				+ "`title`,"
				+ "`startloc`,"
				+ "`type`,"
				+ "`costs`, `state`, `overrideTime`, `overrideOnly`, `overrideID`) VALUES"
				+ "(?,?,?,?,?,?,?,?,?);";

		String update = "UPDATE Mist_Warp SET"
				+ "`title`=?, `startloc`=?, `type`=?,"
				+ "`costs`=?, `state`=?, `overrideTime`=?, `overrideOnly`=?, `overrideID`=? WHERE `id`=?;";

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
					String build = "";
					for (String in : warp.getType()) {
						build += in + "_";
					}
					pst.setString(4, build);
					pst.setDouble(5, warp.getCosts());
					pst.setString(6, warp.getState());
					pst.setString(7, warp.getBypassedTime());
					int o = 0;
					if (warp.isOverrideOnly()) {
						o = 1;
					}
					pst.setInt(8, o);
					String ids = "";
					for (int in : warp.getByPassID()) {
						ids += in + "-";
					}
					pst.setString(9, ids);
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
					String build = "";
					for (String in : warp.getType()) {
						build += in + "_";
					}
					pst.setString(3, build);
					pst.setDouble(4, warp.getCosts());
					pst.setString(5, warp.getState());
					pst.setString(6, warp.getBypassedTime());
					int o = 0;
					if (warp.isOverrideOnly()) {
						o = 1;
					}
					pst.setInt(7, o);
					String ids = "";
					for (int in : warp.getByPassID()) {
						ids += in + "-";
					}
					pst.setString(8, ids);
					pst.setInt(9, warp.getWarpid());
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
		String insert = "INSERT INTO Mist_NPCData ("
				+ "`npcid`,"
				+ "`npcname`,"
				+ "`spawnlocationx`,"
				+ "`spawnlocationy`,"
				+ "`spawnlocationz`, "
				+ "`killquests`,"
				+ "`harvestquests`,"
				+ "`talktoquests`,"
				+ "`warps`, "
				+ "`spawnworld`, `npcskinname`, `eventquests` ,`active`) VALUES ("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?);";

		String update = "UPDATE Mist_NPCData SET"
				+ "`npcname`=?,"
				+ "`spawnlocationx`=?,"
				+ "`spawnlocationy`=?,"
				+ "`spawnlocationz`=?,"
				+ "`killquests`=?,"
				+ "`harvestquests`=?,"
				+ "`talktoquests`=?,"
				+ "`warps`=?, "
				+ "`spawnworld`=?, `npcskinname`=? , `eventquests`=?, `active`=? WHERE `npcid`=?;";

		String test = "SELECT * FROM Mist_NPCData WHERE `npcid`=?;";
		for (Integer npcid : questers.uniquenpcid.keySet()) {
			UUID npcuuid = questers.uniquenpcid.get(npcid);
			String killquestids = null;
			if (questers.GetIds("kill", npcuuid) != null) {
				for (Integer killquest : questers.GetIds("kill", npcuuid)) {
					if (killquestids == null) {
						killquestids = killquest + "_";
					} else {
						killquestids += killquest + "_";
					}
				}
			}
			String harvestquestids = null;
			if (questers.GetIds("harvest", npcuuid) != null) {
				for (Integer harvestquest : questers.GetIds("harvest", npcuuid)) {
					if (harvestquestids == null) {
						harvestquestids = harvestquest + "_";
					} else {
						harvestquestids += harvestquest + "_";
					}
				}
			}
			String talktoquestids = null;
			if (questers.GetIds("talkto", npcuuid) != null) {
				for (Integer talktoquest : questers.GetIds("talkto", npcuuid)) {
					if (talktoquestids == null) {
						talktoquestids = talktoquest + "_";
					} else {
						talktoquestids += talktoquest + "_";
					}
				}
			}
			String warpids = null;
			if (questers.getWarpId(npcuuid) != -1) {
				warpids = Integer.toString(questers.getWarpId(npcuuid));
			}

			String eventquestids = null;
			if (questers.GetIds("event", npcuuid) != null) {
				for (Integer quests : questers.GetIds("event", npcuuid)) {
					if (eventquestids == null) {
						eventquestids = quests + "_";
					} else {
						eventquestids += quests + "_";
					}
				}
			}

			NPCHandler handler = plugin.getNPCHandler();
			NPC pc = handler.getNPCByUUID(npcuuid);
			String name = pc.getName();
			int x = (int) questers.spawnlocation.get(npcuuid).getX();
			int y = (int) questers.spawnlocation.get(npcuuid).getY();
			int z = (int) questers.spawnlocation.get(npcuuid).getZ();
			String world = questers.spawnlocation.get(npcuuid).getWorld()
					.getName();
			int active;
			if (questers.activenpc.get(npcuuid) != null) {
				active = 1;
			} else {
				active = 0;
			}

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
					pst.setString(6, killquestids);
					pst.setString(7, harvestquestids);
					pst.setString(8, talktoquestids);
					pst.setString(9, warpids);
					pst.setString(10, world);
					pst.setString(11, pc.getSkinName());
					pst.setString(12, eventquestids);
					pst.setInt(13, active);
					pst.execute();
				} else {
					pst.close();
					pst = con.prepareStatement(update);

					pst.setString(1, name);
					pst.setInt(2, x);
					pst.setInt(3, y);
					pst.setInt(4, z);
					pst.setString(5, killquestids);
					pst.setString(6, harvestquestids);
					pst.setString(7, talktoquestids);
					pst.setString(8, warpids);
					pst.setString(9, world);
					pst.setString(10, pc.getSkinName());
					pst.setString(11, eventquestids);
					pst.setInt(12, active);
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

	public void saverewarded() {
		for (UUID player : playerinfo.getplayers()) {
			Playerstats p = playerinfo.getplayer(player);
			for (String type : p.getrewardedtype()) {
				String test = "SELECT * FROM Mist_QuestComplete%type% WHERE useruuid=?;";
				String update = "UPDATE Mist_QuestComplete%type% SET timereward=?, questnumber=? WHERE useruuid=?;";
				String insert = "INSERT INTO Mist_QuestComplete%type% (`useruuid`, `timereward`, `questnumber`) VALUES (?,?,?);";
				switch(type){
				case "kill":
					test = test.replace("%type%", "Kill");
					update = update.replace("%type%", "Kill");
					insert = insert.replace("%type%", "Kill");
					break;
				case "harvest":
					test = test.replace("%type%", "Harvest");
					update = update.replace("%type%", "Harvest");
					insert = insert.replace("%type%", "Harvest");
					break;
				case "talkto":
					test = test.replace("%type%", "Talkto");
					update = update.replace("%type%", "Talkto");
					insert = insert.replace("%type%", "Talkto");
					break;
				case "event":
				case "eventharvest":
				case "eventkill":
					test = test.replace("%type%", "Event");
					update = update.replace("%type%", "Event");
					insert = insert.replace("%type%", "Event");
					break;
				}
				PreparedStatement pst = null;
				try {
					pst = con.prepareStatement(test);
					pst.setString(1, player.toString());
					ResultSet rs = pst.executeQuery();
					if(rs.next()){
						pst.close();
						pst = con.prepareStatement(update);
						String numbers = "";
						String time = "";
						for(int number : p.getrewardednumber(type)){
							numbers += number + "_";
							time += p.getrewardedtime(type, number) + "_";
						}
						pst.setString(1, time);
						pst.setString(2, numbers);
						pst.setString(3, player.toString());
						pst.execute();
					}else{
						pst.close();
						pst = con.prepareStatement(insert);
						String numbers = "";
						String time = "";
						for(int number : p.getrewardednumber(type)){
							numbers += number + "_";
							time += p.getrewardedtime(type, number) + "_";
						}
						pst.setString(2, time);
						pst.setString(3, numbers);
						pst.setString(1, player.toString());
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
	}

	public void saveall() {
		checkcon();

		plugin.getLogger().info(
				Ansi.ansi().fg(Ansi.Color.GREEN) + "SAVING"
						+ Ansi.ansi().fg(Ansi.Color.WHITE));
		saveplayerdata();
		savepartydata();
		savewarp();
		saveeventboat();
		saveeventcart();
		saveeventdragon();
		savenpcdata();
		savemining();
		savewood();
		saveharbors();
		savetrips();
		saveshops();
		saveactivequest();
		savecompleted();
		saverewarded();
		plugin.getLogger().info(
				Ansi.ansi().fg(Ansi.Color.GREEN) + "SAVING COMPLETE"
						+ Ansi.ansi().fg(Ansi.Color.WHITE));
	}

	/**
	 * public void loadshops() { String load = "SELECT * FROM Mist_Shop;";
	 * PreparedStatement pst = null; try { pst = con.prepareStatement(load);
	 * ResultSet rs = pst.executeQuery(); while (rs.next()) { int id =
	 * rs.getInt("id"); String name = rs.getString("name"); String shop =
	 * rs.getString("shopname"); World world =
	 * Bukkit.getWorld(rs.getString("world")); int x = rs.getInt("x"); int y =
	 * rs.getInt("y"); int z = rs.getInt("z"); Location loc = new
	 * Location(world, x, y, z); NPCEntity npc = new NPCEntity(world, loc,
	 * NPCProfile.loadProfile(name, name, ((CraftWorld) world).getHandle(),
	 * plugin), plugin.getNetwork(), plugin); npc.spawn(loc,
	 * NPCSpawnReason.SHOP_SPAWN); } } catch (SQLException e) {
	 * e.printStackTrace(); try { if (pst != null) { pst.close(); } } catch
	 * (SQLException ex) { System.out.println(ex.getStackTrace()); } } finally {
	 * try { if (pst != null) { pst.close(); } } catch (SQLException ex) {
	 * System.out.println(ex.getStackTrace()); } } }
	 **/

	
	public void loadactive(){
		String load = "SELECT * FROM Mist_QuestActive;";
		PreparedStatement pst = null;
		try{
			pst = con.prepareStatement(load);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				UUID player = UUID.fromString(rs.getString("useruuid"));
				Playerstats p = playerinfo.getplayer(player);
				if(rs.getString("killquests") != null || !rs.getString("killquests").equals("")){
					for(String quests : rs.getString("killquests").split("_")){
						if(quests == ""){
						continue;
						}
						String[] split = quests.split("-");
						int id = Integer.parseInt(split[0]);
						int prog  = Integer.parseInt(split[1]);
						p.addactive("kill", id);
						questers.addProgress(player, "kill", id, prog);
					}
				}
				if(rs.getString("harvestquests") != null|| !rs.getString("harvestquests").equals("")){
					for(String quests : rs.getString("harvestquests").split("_")){
						if(quests == ""){
							continue;
							}
						String[] split = quests.split("-");
						int id = Integer.parseInt(split[0]);
						int prog  = Integer.parseInt(split[1]);
						p.addactive("harvest", id);
						questers.addProgress(player, "harvest", id, prog);
					}
				}
				if(rs.getString("talktoquests") != null|| !rs.getString("talktoquests").equals("")){
					for(String quests : rs.getString("talktoquests").split("_")){
						if(quests == ""){
							continue;
							}
						int id = Integer.parseInt(quests);
						p.addactive("talkto", id);
					}
					
				}
				if(rs.getString("eventquests") != null|| !rs.getString("eventquests").equals("")){
					for(String quests : rs.getString("eventquests").split("_")){
						if(quests == ""){
							continue;
							}
						String[] split = quests.split("-");
						int id = Integer.parseInt(split[0]);
						int prog  = Integer.parseInt(split[1]);
						p.addactive("event", id);
						questers.addProgress(player, "event", id, prog);
					}
				}
				
			}
		}catch (SQLException e) {
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
	
	public void loadcompleted(){
		String comp = "SELECT * FROM Mist_QuestRewardable;";
		PreparedStatement pst = null;
		
		try{
			pst = con.prepareStatement(comp);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				UUID player = UUID.fromString(rs.getString("useruuid"));
				Playerstats p = playerinfo.getplayer(player);
				if(rs.getString("killquests") != null || !rs.getString("killquests").equals("")){
					for(String quests : rs.getString("killquests").split("_")){
						if(quests == ""){
							continue;
							}
						int id = Integer.parseInt(quests);
						p.addcompleted("kill", id);
					}
				}
				if(rs.getString("harvestquests") != null || !rs.getString("harvestquests").equals("")){
					for(String quests : rs.getString("harvestquests").split("_")){
						if(quests == ""){
							continue;
							}
						int id = Integer.parseInt(quests);
						p.addcompleted("harvest", id);
					}
				}
				if(rs.getString("talktoquests") != null || !rs.getString("eventquests").equals("")){
					for(String quests : rs.getString("talktoquests").split("_")){
						if(quests == ""){
							continue;
							}
						int id = Integer.parseInt(quests);
						p.addcompleted("talkto", id);
					}
				}
				if(rs.getString("eventquests") != null || !rs.getString("eventquests").equals("")){
					for(String quests : rs.getString("eventquests").split("_")){
						if(quests == ""){
							continue;
							}
						int id = Integer.parseInt(quests);
						p.addcompleted("event", id);
					}
				}
			}
		}
		catch (SQLException e) {
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

	public void loadrewardedKill(){
	String load = "SELECT * FROM Mist_QuestCompleteKill;";
	PreparedStatement pst = null;
	try{
		pst = con.prepareStatement(load);
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			if(rs.getString("questnumber") == null|| rs.getString("questnumber").equals("")){
				continue;
			}
			UUID player = UUID.fromString(rs.getString("useruuid"));
			Playerstats p = playerinfo.getplayer(player);
			String[] quest = rs.getString("questnumber").split("_");
			String[] rewards = rs.getString("timereward").split("_");
			for(int i = 0; i < quest.length; i++){
				int id = Integer.parseInt(quest[i]);
				long r = Long.parseLong(rewards[i]);
				p.addrewarded("kill", id, r);
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
	public void loadrewardedHarvest(){
		String load = "SELECT * FROM Mist_QuestCompleteHarvest;";
		PreparedStatement pst = null;
		try{
			pst = con.prepareStatement(load);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				if(rs.getString("questnumber") == null|| rs.getString("questnumber").equals("")){
					continue;
				}
				UUID player = UUID.fromString(rs.getString("useruuid"));
				Playerstats p = playerinfo.getplayer(player);
				String[] quest = rs.getString("questnumber").split("_");
				String[] rewards = rs.getString("timereward").split("_");
				for(int i = 0; i < quest.length; i++){
					int id = Integer.parseInt(quest[i]);
					long r = Long.parseLong(rewards[i]);
					p.addrewarded("harvest", id, r);
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
	public void loadrewardedTalkto(){
		String load = "SELECT * FROM Mist_QuestCompleteTalkto;";
		PreparedStatement pst = null;
		try{
			pst = con.prepareStatement(load);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				if(rs.getString("questnumber") == null|| rs.getString("questnumber").equals("")){
					continue;
				}
				UUID player = UUID.fromString(rs.getString("useruuid"));
				Playerstats p = playerinfo.getplayer(player);
				String[] quest = rs.getString("questnumber").split("_");
				String[] rewards = rs.getString("timereward").split("_");
				for(int i = 0; i < quest.length; i++){
					int id = Integer.parseInt(quest[i]);
					long r = Long.parseLong(rewards[i]);
					p.addrewarded("talkto", id, r);
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
	public void loadrewardedevent(){
		String load = "SELECT * FROM Mist_QuestCompleteEvent;";
		PreparedStatement pst = null;
		try{
			pst = con.prepareStatement(load);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				if(rs.getString("questnumber") == null || rs.getString("questnumber").equals("")){
					continue;
				}
				UUID player = UUID.fromString(rs.getString("useruuid"));
				Playerstats p = playerinfo.getplayer(player);
				String[] quest = rs.getString("questnumber").split("_");
				String[] rewards = rs.getString("timereward").split("_");
				for(int i = 0; i < quest.length; i++){
					int id = Integer.parseInt(quest[i]);
					long r = Long.parseLong(rewards[i]);
					p.addrewarded("event", id, r);
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
				travel.loadhardbor(id, type, l);
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
				travel.loadtrip(id, type, l);
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
				if (playerinfo.getplayer(player) == null) {
					Playerstats p = playerinfo.createplayer(player);

					p.setWoodcutting(rs.getInt("woodcutting"));
					p.setMining(rs.getInt("mining"));
					p.setSmelting(rs.getInt("smelting"));
					p.setCooking(rs.getInt("cooking"));
					p.setFishing(rs.getInt("fishing"));
					p.setEnchanting(rs.getInt("enchanting"));
					p.setAlchemy(rs.getInt("alchemy"));

					p.setWoodcuttingprog(rs.getInt("woodcuttingprog"));
					p.setMiningprog(rs.getInt("miningprog"));
					p.setSmeltingprog(rs.getInt("smeltingprog"));
					p.setCookingprog(rs.getInt("cookingprog"));
					p.setFishingprog(rs.getInt("fishingprog"));
					p.setEnchantingprog(rs.getInt("enchantingprog"));
					p.setAlchemyprog(rs.getInt("alchemyprog"));

					p.setAbility(rs.getInt("abilitie"));
					p.setStrength(rs.getInt("strength"));
					p.setDex(rs.getInt("dex"));
					p.setLevel(rs.getInt("lvl"));
					p.setLevelprog(rs.getInt("levelprog"));
					if (rs.getString("library") != null
							|| !rs.getString("library").equals("")) {
						String[] books = rs.getString("library").split("-");
						for (String in : books) {
							p.addBook(in);
						}
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
				String state = rs.getString("state");
				String overrideTime = rs.getString("overrideTime");
				boolean overrideOnly = false;
				if (rs.getInt("overrideOnly") == 1) {
					overrideOnly = true;
				}
				Set<Integer> overrideID = new HashSet<Integer>();
				if (!rs.getString("overrideID").equals("")) {
					for (String in : rs.getString("overrideID").split("-")) {
						overrideID.add(Integer.parseInt(in));
					}
				}

				questers.loadwarps(number, title, startloc, costs, type, state,
						overrideTime, overrideOnly, overrideID);

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
				for (UUID tu : questers.GetKeysSets("talkto")) {
					for (Integer qn : questers.GetIds("talkto", tu)) {
						QuestTalkto talk = questers.returntalkto(qn);
						if (talk == null) {
							questers.GetIds("talkto", tu).remove(qn);
							continue;
						}
						if (talk.getNpcid() == rs.getInt("npcid")) {
							questers.targetnpcs.put(npc, qn);
							break;
						}
					}
				}
				if (rs.getString("killquests") != null) {
					String[] killtemp = rs.getString("killquests").split("_");
					for (String t : killtemp) {
						questers.addKillquest(npc, Integer.parseInt(t));
					}
				}
				if (rs.getString("harvestquests") != null) {
					String[] killtemp = rs.getString("harvestquests")
							.split("_");
					for (String t : killtemp) {
						questers.addHarvestquest(npc, Integer.parseInt(t));
					}
				}
				if (rs.getString("talktoquests") != null) {
					String[] killtemp = rs.getString("talktoquests").split("_");
					for (String t : killtemp) {
						questers.addTalktoquest(npc, Integer.parseInt(t));
					}
				}
				if (rs.getString("warps") != null) {
					questers.addWarp(npc, rs.getInt("warps"));
				}

				if (rs.getString("eventquests") != null) {
					String[] temp = rs.getString("eventquests").split("_");
					for (String in : temp) {
						questers.addEventquest(npc, Integer.parseInt(in));
					}
				}
				int active = rs.getInt("active");
				if (active == 1) {
					questers.runpoints(npc);
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

		
		loadactive();
		loadcompleted();
		loadrewardedevent();
		loadrewardedHarvest();
		loadrewardedKill();
		loadrewardedTalkto();
		
		
		loadeventboat();
		loadeventcart();
		loadeventdragon();
		loadwarp();

		loadnpc();

		loadmining();
		loadwood();
		loadharbors();
		loadtrips();
		// loadshops();
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
							checkcon();
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
		this.questers = instance.qserver;
		this.party = instance.party;
		this.playerinfo = instance.playerinfo;
		this.travel = instance.tr;
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

	public void setcon(Connection connect) {
		con = connect;
	}

	public Connection GetCon() {
		return con;
	}

	public void checkcon() {
		try {
			if (con.isClosed()) {
				opencon();
				intvar();
				OpenConnect();
			}
		} catch (SQLException | NullPointerException e) {
			opencon();
			intvar();
			OpenConnect();
		}
	}

	public void OpenConnect() {
		setcon(MySQl.openConnection());
	}
}
