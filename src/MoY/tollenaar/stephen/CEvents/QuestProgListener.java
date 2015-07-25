package MoY.tollenaar.stephen.CEvents;

import java.util.HashMap;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.Quests.QuestEvent;
import MoY.tollenaar.stephen.Quests.QuestHarvest;
import MoY.tollenaar.stephen.Quests.QuestKill;
import MoY.tollenaar.stephen.Quests.QuestsServerSide;

public class QuestProgListener implements Listener {

	private QuestsServerSide quest;

	public QuestProgListener(MoY instance) {
		this.quest = instance.questers;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@EventHandler
	public void OnQuestProg(QuestProgEvent event) {
		if (event.getType().equals("kill")) {
			if (IsRightEntity(event.getEntity().getName(), event.getQuest(),
					false)) {
				QuestKill kill = quest.returnkill(event.getQuest());
				HashMap<Integer, Integer> amount = quest.progress.get(
						event.getPlayer().getUniqueId()).get("kill");
				if (amount.get(event.getQuest()) + 1 != kill.getCount()) {
					amount.put(event.getQuest(),
							amount.get(event.getQuest()) + 1);
				} else {
					NPCRegistry reg = CitizensAPI.getNPCRegistry();

					for (UUID npcuuid : quest.GetKeysSets("kill")) {

						if (quest.GetIds("kill", npcuuid).contains(
								event.getQuest())) {
							NPC npc = reg.getByUniqueId(npcuuid);
							quest.AddCompletedQuest(event.getPlayer(),
									event.getQuest(), "kill", npc.getName());
							break;
						}
					}
				}
			}
		} else if (event.getType().equals("harvest")) {
			String name = Integer.toString(event.getItem().getType().getId());
			if (event.getItem().getDurability() != 0) {
				name += ":" + event.getItem().getDurability();
			}
			if (name != null && IsRightItem(name, event.getQuest(), false)) {
				QuestHarvest harvest = quest.returnharvest(event.getQuest());
				if (event.getItem().getAmount() >= harvest.getCount()
						|| quest.progress.get(event.getPlayer().getUniqueId())
								.get("harvest").get(event.getQuest())
								+ event.getItem().getAmount() >= harvest
									.getCount()) {

					for (UUID npcuuid : quest.GetKeysSets("harvest")) {
						if (quest.GetIds("harvest", npcuuid)
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
		} else if (event.getType().equals("eventharvest")) {
			String name = Integer.toString(event.getItem().getType().getId());
			if (event.getItem().getDurability() != 0) {
				name += ":" + event.getItem().getDurability();
			}
			if (name != null && IsRightItem(name, event.getQuest(), true)) {
				QuestEvent harvest = quest.returneventquest(event.getQuest());
				if (event.getItem().getAmount() >= harvest.getCount()
						|| quest.progress.get(event.getPlayer().getUniqueId())
								.get("eventharvest").get(event.getQuest())
								+ event.getItem().getAmount() >= harvest
									.getCount()) {

					for (UUID npcuuid : quest.GetKeysSets("event")) {
						if (quest.GetIds("event", npcuuid).contains(
								event.getQuest())) {
							NPCRegistry reg = CitizensAPI.getNPCRegistry();
							NPC npc = reg.getByUniqueId(npcuuid);
							quest.AddCompletedQuest(event.getPlayer(),
									event.getQuest(), "eventharvest",
									npc.getName());
							break;
						}
					}
				} else {
					quest.progress
							.get(event.getPlayer().getUniqueId())
							.get("eventharvest")
							.put(event.getQuest(),
									quest.progress
											.get(event.getPlayer()
													.getUniqueId())
											.get("eventharvest")
											.get(event.getQuest())
											+ event.getItem().getAmount());
				}
			}
		} else if (event.getType().equals("eventkill")) {
			if (IsRightEntity(event.getEntity().getName(), event.getQuest(),
					true)) {
				QuestEvent kill = quest.returneventquest(event.getQuest());
				HashMap<Integer, Integer> amount = quest.progress.get(
						event.getPlayer().getUniqueId()).get("eventkill");
				if (amount.get(event.getQuest()) + 1 != kill.getCount()) {
					amount.put(event.getQuest(),
							amount.get(event.getQuest()) + 1);
				} else {
					NPCRegistry reg = CitizensAPI.getNPCRegistry();

					for (UUID npcuuid : quest.GetKeysSets("event")) {

						if (quest.GetIds("event", npcuuid).contains(
								event.getQuest())) {
							NPC npc = reg.getByUniqueId(npcuuid);
							quest.AddCompletedQuest(event.getPlayer(),
									event.getQuest(), "eventkill",
									npc.getName());
							break;
						}
					}
				}
			}
		}
	}

	private boolean IsRightItem(String name, int q, boolean event) {
		if (!event) {
			QuestHarvest harvest = quest.returnharvest(q);
			if (harvest != null) {
				if (harvest.getItemId().equals(name)) {
					return true;
				} else {
					return false;
				}
			} else {
				quest.removeharvest(q);
				return false;
			}
		} else {
			QuestEvent e = quest.returneventquest(q);
			if (e != null) {
				if (e.getType().equals(name)) {
					return true;
				} else {
					return false;
				}
			} else {
				quest.removeevent(q);
				return false;
			}
		}
	}

	private boolean IsRightEntity(String name, int q, boolean event) {
		if (!event) {
			QuestKill kill = quest.returnkill(q);
			if (kill != null) {
				if (kill.getMonster().equals(name)) {
					return true;
				} else {
					return false;
				}
			} else {
				quest.removeharvest(q);
				return false;
			}
		} else {
			QuestEvent e = quest.returneventquest(q);
			if (e != null) {
				if (e.getType().equals(name)) {
					return true;
				} else {
					return false;
				}
			} else {
				quest.removeevent(q);
				return false;
			}
		}
	}

}
