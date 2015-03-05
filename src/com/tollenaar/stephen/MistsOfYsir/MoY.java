package com.tollenaar.stephen.MistsOfYsir;

import java.sql.Connection;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.tollenaar.stephen.Commands.CommandsEvent;
import com.tollenaar.stephen.Commands.CommandsNPC;
import com.tollenaar.stephen.Commands.CommandsParty;
import com.tollenaar.stephen.Commands.CommandsPlayerinfo;
import com.tollenaar.stephen.PlayerInfo.LevelSystems;
import com.tollenaar.stephen.PlayerInfo.Playerinfoding;
import com.tollenaar.stephen.PlayerInfo.Skillimprovement;
import com.tollenaar.stephen.Quests.ProgressHarvest;
import com.tollenaar.stephen.Quests.ProgressKill;
import com.tollenaar.stephen.Quests.Quest;
import com.tollenaar.stephen.Quests.QuestChat;
import com.tollenaar.stephen.Quests.QuestClientSide;
import com.tollenaar.stephen.Quests.QuestInvClick;
import com.tollenaar.stephen.Quests.QuestParticles;
import com.tollenaar.stephen.Quests.Questinteracts;
import com.tollenaar.stephen.Quests.QuestsServerSide;
import com.tollenaar.stephen.Travel.Travel;
import com.tollenaar.stephen.Travel.TravelBoatEvent;
import com.tollenaar.stephen.Travel.TravelCartEvent;
import com.tollenaar.stephen.Travel.TravelDragonEvent;

import code.husky.mysql.MySQL;

public class MoY extends JavaPlugin {
	Connection con = null;

	private MoY plugin;
	public QuestsServerSide questers;
	public Questinteracts qinteract;
	public QuestParticles qp;
	public DbStuff database;
	public Playerinfoding playerinfo;
	public Travel tr;
	public RandomEvents re;
	public TravelBoatEvent boat;
	public TravelCartEvent cart;
	public TravelDragonEvent dragon;
	public QuestClientSide qqc;
	public Party party;
	public Quest q;
	public MobSpawns mob;
	public LevelSystems lvl;
	public Skillimprovement skill;
	public Filewriters fw;
	public QuestChat qc;
	public QuestInvClick qi;
	
	
	
	@Override
	public void onEnable() {
		final FileConfiguration config = this.getConfig();
		config.options().copyDefaults(true);
		saveConfig();
		plugin = this;
		q = new Quest(this);

		fw = new Filewriters(this);
		party = new Party(this);
		lvl = new LevelSystems(this);
		skill = new Skillimprovement(this);
		playerinfo = new Playerinfoding(this);
		re = new RandomEvents(this);

		questers = new QuestsServerSide(this);
		qqc = new QuestClientSide(this);
		qinteract = new Questinteracts(this);

		boat = new TravelBoatEvent(this);
		cart = new TravelCartEvent(this);
		dragon = new TravelDragonEvent(this);
		tr = new Travel(this);

		qc = new QuestChat(this);
		qi = new QuestInvClick(this);
		qp = new QuestParticles(this);

		mob = new MobSpawns(this);

		database = new DbStuff(this);
		database.intvar();
		MySQL MySQl = database.MySQl;
		int poging = 0;
		while (con == null) {
			con = MySQl.openConnection();
			if (con == null) {
				poging++;
				getLogger()
						.info("Database connection lost. Reconection will be started");

			}
			if (poging == 2) {
				getLogger()
						.info("No Connection to Database. Plugin is deactivating. Reload server for database Connection");
				Bukkit.getPluginManager().disablePlugin(plugin);
				break;
			}

		}
		if (con != null) {
			database.setcon(con);
			getLogger().info("Databse connection has succeed");
			database.TableCreate();
			database.closecon();
			database.loadall();
			PluginManager pm = getServer().getPluginManager();

			pm.registerEvents(qinteract, this);

			pm.registerEvents(new Party(this), this);

			pm.registerEvents(playerinfo, this);
			pm.registerEvents(party, this);

			pm.registerEvents(lvl, this);
			pm.registerEvents(skill, this);
			pm.registerEvents(new ChatController(this), this);

			pm.registerEvents(tr, this);
			pm.registerEvents(re, this);

			pm.registerEvents(boat, this);
			pm.registerEvents(cart, this);
			pm.registerEvents(dragon, this);
			pm.registerEvents(mob, this);

			pm.registerEvents(qc, this);

			pm.registerEvents(new ProgressKill(this), this);
			pm.registerEvents(new ProgressHarvest(this), this);

			getCommand("party").setExecutor(new CommandsParty(this));
			getCommand("skill").setExecutor(new CommandsPlayerinfo(this));
			getCommand("qnpc").setExecutor(new CommandsNPC(this));
			getCommand("lvl").setExecutor(new CommandsPlayerinfo(this));
			getCommand("event").setExecutor(new CommandsEvent(this));
			getCommand("quest").setExecutor(new CommandsPlayerinfo(this));
			getCommand("trip").setExecutor(new CommandsEvent(this));
			getCommand("harbor").setExecutor(new CommandsEvent(this));
			re.playerevent();
		}
	}

	@Override
	public void onDisable() {
		database.saveall();
		for (UUID uuid : questers.uniquenpcid.values()) {
			NPCRegistry registry = CitizensAPI.getNPCRegistry();
			NPC npc = registry.getByUniqueId(uuid);
			npc.despawn();
			registry.deregister(npc);
		}
		re.cancelsced();
	}

}
