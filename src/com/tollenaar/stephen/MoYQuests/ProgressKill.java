package com.tollenaar.stephen.MoYQuests;

import java.util.HashMap;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.tollenaar.stephen.MoYMistsOfYsir.MoY;
import com.tollenaar.stephen.MoYPlayerInfo.Playerstats;

public class ProgressKill implements Listener {

	private QuestsServerSide q;
	private MoY plugin;
	
	public ProgressKill(MoY instance) {
		this.q = instance.questers;
		this.plugin = instance;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void OnMobKill(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			return;
		}
		if (event.getEntity().getKiller() instanceof Player) {
			Player player = event.getEntity().getKiller();
			Entity ent = event.getEntity();
			if (Playerstats.activequests.get(player.getUniqueId()) != null) {
				if (Playerstats.activequests.get(player.getUniqueId()).get(
						"kill") != null) {
					for (int quests : Playerstats.activequests.get(
							player.getUniqueId()).get("kill")) {
						QuestKill kill = q.returnkill(quests);
						if (EntityType.valueOf(kill.getMonster().toUpperCase()).getName().equals(ent.getType().getName())) {
							HashMap<Integer, Integer> amount = q.progress.get(
									player.getUniqueId()).get("kill");
							if (amount.get(quests) + 1 != kill.getCount()) {
								amount.put(quests, amount.get(quests) + 1);
							} else {
								NPCRegistry reg =	CitizensAPI.getNPCRegistry();
								
								for(UUID npcuuid : plugin.questers.killquests.keySet()){
									
									if(plugin.questers.killquests.get(npcuuid).contains(quests)){
										NPC npc = reg.getByUniqueId(npcuuid);
										q.AddCompletedQuest(player, quests, "kill", npc.getName());
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
