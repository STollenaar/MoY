package moy.tollenaar.stephen.Quests;

import java.util.HashMap;
import java.util.UUID;



import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.NPC.NPC;
import moy.tollenaar.stephen.NPC.NPCHandler;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;

public class ProgressKill implements Listener {

	private QuestsServerSide q;
	private MoY plugin;
	private Playerinfo playerinfo;
	
	public ProgressKill(MoY instance) {
		this.q = instance.qserver;
		this.plugin = instance;
		this.playerinfo = instance.playerinfo;
	}

	@SuppressWarnings({ "deprecation" })
	@EventHandler
	public void OnMobKill(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			return;
		}
		if (event.getEntity().getKiller() instanceof Player) {
			NPCHandler handler = plugin.getNPCHandler();
			Player player = event.getEntity().getKiller();
			Playerstats p = playerinfo.getplayer(player.getUniqueId());
			Entity ent = event.getEntity();
			if (p.getactivetype() != null) {
				if (p.getactives("kill") != null) {
					for (int quests : p.getactives("kill")) {
						QuestKill kill = q.returnkill(quests);
						if(kill == null){
							p.deleteactive("kill", quests);
							q.removekill(quests);
							continue;
						}
						if (EntityType.valueOf(kill.getMonster().toUpperCase()).getName().equals(ent.getType().getName())) {
							HashMap<Integer, Integer> amount = q.getProgress(
									player.getUniqueId()).get("kill");
							if (amount.get(quests) + 1 != kill.getCount()) {
								amount.put(quests, amount.get(quests) + 1);
							} else {
								
								for(UUID npcuuid : plugin.qserver.GetKeysSets("kill")){
									
									if(plugin.qserver.GetIds("kill", npcuuid).contains(quests)){
										NPC npc = handler.getNPCByUUID(npcuuid);
										q.AddCompletedQuest(player, quests, "kill", npc.getName());
										break;
									}
								}
								
							}
							break;
						}
					}
				}
				if(p.getactives("eventkill") != null){
					for (int quests : p.getactives("eventkill")) {
						QuestEvent kill = q.returneventquest(quests);
						if(kill == null){
							p.deleteactive("eventkill", quests);
							q.removeevent(quests);
							continue;
						}
						if (EntityType.valueOf(kill.getType().toUpperCase()).getName().equals(ent.getType().getName())) {
							HashMap<Integer, Integer> amount = q.getProgress(
									player.getUniqueId()).get("eventkill");
							if (amount.get(quests) + 1 != kill.getCount()) {
								amount.put(quests, amount.get(quests) + 1);
							} else {
								
								for(UUID npcuuid : plugin.qserver.GetKeysSets("event")){
									
									if(plugin.qserver.GetIds("event", npcuuid).contains(quests)){
										NPC npc = handler.getNPCByUUID(npcuuid);
										q.AddCompletedQuest(player, quests, "eventkill", npc.getName());
										break;
									}
								}
								
							}
							break;
						}
					}
				}
			}
		}

	}

}
