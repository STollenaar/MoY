package MoY.tollenaar.stephen.CEvents;

import java.util.HashMap;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.Quests.QuestHarvest;
import MoY.tollenaar.stephen.Quests.QuestKill;
import MoY.tollenaar.stephen.Quests.QuestsServerSide;

public class QuestProgListener implements Listener {

	private MoY plugin;
	private QuestsServerSide quest;

	public QuestProgListener(MoY instance) {
		this.plugin = instance;
		this.quest = instance.questers;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void OnQuestProg(QuestProgEvent event) {
		if (event.getType().equals("kill")) {
			if (IsRightEntity(event.getEntity().getName(), event.getQuest())) {
				QuestKill kill = quest.returnkill(event.getQuest());
				HashMap<Integer, Integer> amount = quest.progress.get(
						event.getPlayer().getUniqueId()).get("kill");
				if (amount.get(event.getQuest()) + 1 != kill.getCount()) {
					amount.put(event.getQuest(),
							amount.get(event.getQuest()) + 1);
				} else {
					NPCRegistry reg = CitizensAPI.getNPCRegistry();

					for (UUID npcuuid : plugin.questers.killquests.keySet()) {

						if (quest.killquests.get(npcuuid).contains(
								event.getQuest())) {
							NPC npc = reg.getByUniqueId(npcuuid);
							quest.AddCompletedQuest(event.getPlayer(),
									event.getQuest(), "kill", npc.getName());
							break;
						}
					}
				}
			}
		} else {
			String name = Integer.toString(event.getItem().getType().getId());
			if (event.getItem().getDurability() != 0) {
				name += ":" + event.getItem().getDurability();
			}
			if (name != null &&IsRightItem(name, event.getQuest())) {
				QuestHarvest harvest = quest.returnharvest(event.getQuest());
				if (event.getItem().getAmount() >= harvest.getCount()
						|| quest.progress.get(event.getPlayer().getUniqueId())
								.get("harvest").get(event.getQuest())
								+ event.getItem().getAmount() >= harvest
									.getCount()) {

					for (UUID npcuuid : plugin.questers.harvestquests.keySet()) {
						if (plugin.questers.harvestquests.get(npcuuid)
								.contains(event.getQuest())) {
							NPCRegistry reg = CitizensAPI.getNPCRegistry();
							NPC npc = reg.getByUniqueId(npcuuid);
							quest.AddCompletedQuest(event.getPlayer(),
									event.getQuest(), "harvest", npc.getName());
							break;
						}
					}
				} else {
					quest.progress
							.get(event.getPlayer().getUniqueId())
							.get("harvest")
							.put(event.getQuest(),
									quest.progress
											.get(event.getPlayer()
													.getUniqueId())
											.get("harvest")
											.get(event.getQuest())
											+ event.getItem().getAmount());
				}
			}
		}
	}

	private boolean IsRightItem(String name, int q) {
		QuestHarvest harvest = quest.returnharvest(q);
		if (harvest.getItemId().equals(name)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean IsRightEntity(String name, int q) {
		QuestKill kill = quest.returnkill(q);
		if (kill.getMonster().equals(name)) {
			return true;
		} else {
			return false;
		}
	}
}
