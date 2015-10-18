package moy.tollenaar.stephen.MistsOfYsir;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import moy.tollenaar.stephen.CEvents.ProgListener;
import moy.tollenaar.stephen.CEvents.QuestProgListener;
import moy.tollenaar.stephen.Commands.CommandsEvent;
import moy.tollenaar.stephen.Commands.CommandsNPC;
import moy.tollenaar.stephen.Commands.CommandsParty;
import moy.tollenaar.stephen.Commands.CommandsPlayerinfo;
import moy.tollenaar.stephen.Commands.SpeechCommand;
import moy.tollenaar.stephen.Files.Filewriters;
import moy.tollenaar.stephen.MobHandlers.MobSpawns;
import moy.tollenaar.stephen.MobHandlers.RandomEvents;
import moy.tollenaar.stephen.NPC.NPCNetworkManager;
import moy.tollenaar.stephen.NPC.NPCHandler;
import moy.tollenaar.stephen.PlayerInfo.Party;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.Quests.ProgressHarvest;
import moy.tollenaar.stephen.Quests.ProgressKill;
import moy.tollenaar.stephen.Quests.Quest;
import moy.tollenaar.stephen.Quests.QuestChat;
import moy.tollenaar.stephen.Quests.QuestClientSide;
import moy.tollenaar.stephen.Quests.QuestEvent;
import moy.tollenaar.stephen.Quests.QuestInvClick;
import moy.tollenaar.stephen.Quests.QuestParticles;
import moy.tollenaar.stephen.Quests.Questinteracts;
import moy.tollenaar.stephen.Quests.QuestsServerSide;
import moy.tollenaar.stephen.SkillsStuff.EnchantingSkill;
import moy.tollenaar.stephen.SkillsStuff.LevelSystems;
import moy.tollenaar.stephen.SkillsStuff.Skillimprovement;
import moy.tollenaar.stephen.Travel.Travel;
import moy.tollenaar.stephen.Travel.TravelBoatEvent;
import moy.tollenaar.stephen.Travel.TravelCartEvent;
import moy.tollenaar.stephen.Travel.TravelDragonEvent;
import moy.tollenaar.stephen.Util.ChatController;
import moy.tollenaar.stephen.Util.Runic;
import code.husky.mysql.MySQL;

public class MoY extends JavaPlugin {

	public QuestsServerSide questers;
	public Questinteracts qinteract;
	public QuestParticles qp;
	public DbStuff database;
	public Playerinfo playerinfo;
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
	private List<QuestEvent> stortemp = new ArrayList<QuestEvent>();
	public SpeechCommand speech;
	private NPCHandler handler;
	private NPCNetworkManager network;
	public Runic runic;
	private FileConfiguration config;

	@Override
	public void onEnable() {
		config = this.getConfig();
		config.options().copyDefaults(true);
		saveConfig();
		
		initVar();
		runic = new Runic();
		database = new DbStuff(this);
		database.intvar();

		MySQL MySQl = database.MySQl;
		int poging = 0;
		while (MySQl.getConnection() == null) {
			MySQl.openConnection();
			if (MySQl.getConnection() == null) {
				poging++;
				getLogger()
						.info("Database connection lost. Reconection will be started");

			}
			if (poging == 2) {
				getLogger()
						.info("No Connection to Database. Plugin is running in offline mode. Reconnection Attempts will be made");
				break;
			}

		}
		if (MySQl.getConnection() != null) {
			registerEvents();
	
			database.setcon(MySQl.getConnection());
			getLogger().info("Databse connection has succeed");
			database.TableCreate();
			database.closecon();
			database.loadall();

			config.set("status", false);
			saveConfig();
			this.speech = new SpeechCommand();
			getCommand("party").setExecutor(new CommandsParty(this));
			getCommand("skill").setExecutor(new CommandsPlayerinfo(this));
			getCommand("qnpc").setExecutor(new CommandsNPC(this));
			getCommand("lvl").setExecutor(new CommandsPlayerinfo(this));
			getCommand("event").setExecutor(new CommandsEvent(this));
			getCommand("harbor").setExecutor(new CommandsEvent(this));
			getCommand("trip").setExecutor(new CommandsEvent(this));
			getCommand("quest").setExecutor(new CommandsPlayerinfo(this));
			getCommand("speechtrait").setExecutor(speech);
			re.playerevent();
		}
	}

	public void registerEvents(){
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
		pm.registerEvents(new EnchantingSkill(this), this);
		pm.registerEvents(new ProgressKill(this), this);
		pm.registerEvents(new ProgressHarvest(playerinfo), this);
		pm.registerEvents(new QuestProgListener(this), this);

		pm.registerEvents(new ProgListener(this), this);

		pm.registerEvents(handler, this);

	}
	
	
	public void initVar(){


		handler = new NPCHandler(this);
		if (!config.getBoolean("status")) {
			handler.DublicateKiller();
			config.set("status", true);
			saveConfig();
		}
		network = new NPCNetworkManager();

		playerinfo = new Playerinfo(this);

		q = new Quest(this);

		party = new Party(this);
		lvl = new LevelSystems(this);
		skill = new Skillimprovement(this);

		re = new RandomEvents(this);

		questers = new QuestsServerSide(this);

		fw = new Filewriters(this);
		for (QuestEvent in : stortemp) {
			fw.SaveEvent(in);
		}
		stortemp.clear();
		
		boat = new TravelBoatEvent(this);
		cart = new TravelCartEvent(this);
		dragon = new TravelDragonEvent(this);
		tr = new Travel(this);
		
		qqc = new QuestClientSide(this);
		qinteract = new Questinteracts(this);

		qc = new QuestChat(this);
		qi = new QuestInvClick(this);
		qp = new QuestParticles(this);

		mob = new MobSpawns(this);
	}
	
	
	
	@Override
	public void onDisable() {
		database.saveall();
		fw.saveall();
		re.cancelsced();
		getNPCHandler().onDisableEvent();
		config.set("status", true);
		saveConfig();
	}

	public void addStorage(QuestEvent event) {
		stortemp.add(event);
	}

	public NPCHandler getNPCHandler() {
		return handler;
	}

	public NPCNetworkManager getNetwork() {
		return network;
	}

}
