package moy.tollenaar.stephen.CEvents;

import java.util.HashMap;
import java.util.UUID;

 


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.NPC.NPC;
import moy.tollenaar.stephen.NPC.NPCHandler;
import moy.tollenaar.stephen.Quests.QuestEvent;
import moy.tollenaar.stephen.Quests.QuestHarvest;
import moy.tollenaar.stephen.Quests.QuestKill;
import moy.tollenaar.stephen.Quests.QuestsServerSide;

public class QuestProgListener implements Listener {

	private QuestsServerSide quest;
	private MoY plugin;
	public QuestProgListener(MoY instance) {
		this.quest = instance.qserver;
		this.plugin = instance;
	}

	@SuppressWarnings({ "deprecation", })
	@EventHandler
	public void OnQuestProg(QuestProgEvent event) {
		NPCHandler handler = plugin.getNPCHandler();
		if (event.getType().equals("kill")) {
			if (IsRightEntity(event.getEntity().getName(), event.getQuestnumber(),
					false)) {
				QuestKill kill = quest.returnkill(event.getQuestnumber());
				HashMap<Integer, Integer> amount = quest.getProgress(
						event.getPlayer().getUniqueId()).get("kill");
				if (amount.get(event.getQuestnumber()) + 1 != kill.getCount()) {
					amount.put(event.getQuestnumber(),
							amount.get(event.getQuestnumber()) + 1);
				} else {

					for (UUID npcuuid : quest.GetKeysSets("kill")) {

						if (quest.GetIds("kill", npcuuid).contains(
								event.getQuestnumber())) {
							NPC npc = handler.getNPCByUUID(npcuuid);
							quest.AddCompletedQuest(event.getPlayer(),
									event.getQuestnumber(), "kill", npc.getName());
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
			if (name != null && IsRightItem(name, event.getQuestnumber(), false)) {
				QuestHarvest harvest = quest.returnharvest(event.getQuestnumber());
				if (event.getItem().getAmount() >= harvest.getCount()
						|| quest.getProgress(event.getPlayer().getUniqueId())
								.get("harvest").get(event.getQuestnumber())
								+ event.getItem().getAmount() >= harvest
									.getCount()) {

					for (UUID npcuuid : quest.GetKeysSets("harvest")) {
						if (quest.GetIds("harvest", npcuuid)
								.contains(event.getQuestnumber())) {
							NPC npc = handler.getNPCByUUID(npcuuid);
							quest.AddCompletedQuest(event.getPlayer(),
									event.getQuestnumber(), "harvest", npc.getName());
							break;
						}
					}
				} else {
					quest.getProgress(event.getPlayer().getUniqueId())
							.get("harvest")
							.put(event.getQuestnumber(),
									quest.getProgress(event.getPlayer()
													.getUniqueId())
											.get("harvest")
											.get(event.getQuestnumber())
											+ event.getItem().getAmount());
				}
			}
		} else if (event.getType().equals("eventharvest")) {
			String name = Integer.toString(event.getItem().getType().getId());
			if (event.getItem().getDurability() != 0) {
				name += ":" + event.getItem().getDurability();
			}
			if (name != null && IsRightItem(name, event.getQuestnumber(), true)) {
				QuestEvent harvest = quest.returneventquest(event.getQuestnumber());
				if (event.getItem().getAmount() >= harvest.getCount()
						|| quest.getProgress(event.getPlayer().getUniqueId())
								.get("eventharvest").get(event.getQuestnumber())
								+ event.getItem().getAmount() >= harvest
									.getCount()) {

					for (UUID npcuuid : quest.GetKeysSets("event")) {
						if (quest.GetIds("event", npcuuid).contains(
								event.getQuestnumber())) {
							NPC npc = handler.getNPCByUUID(npcuuid);
							quest.AddCompletedQuest(event.getPlayer(),
									event.getQuestnumber(), "eventharvest",
									npc.getName());
							break;
						}
					}
				} else {
					quest.getProgress(event.getPlayer().getUniqueId())
							.get("eventharvest")
							.put(event.getQuestnumber(),
									quest.getProgress(event.getPlayer()
													.getUniqueId())
											.get("eventharvest")
											.get(event.getQuestnumber())
											+ event.getItem().getAmount());
				}
			}
		} else if (event.getType().equals("eventkill")) {
			if (IsRightEntity(event.getEntity().getName(), event.getQuestnumber(),
					true)) {
				QuestEvent kill = quest.returneventquest(event.getQuestnumber());
				HashMap<Integer, Integer> amount = quest.getProgress(
						event.getPlayer().getUniqueId()).get("eventkill");
				if (amount.get(event.getQuestnumber()) + 1 != kill.getCount()) {
					amount.put(event.getQuestnumber(),
							amount.get(event.getQuestnumber()) + 1);
				} else {

					for (UUID npcuuid : quest.GetKeysSets("event")) {

						if (quest.GetIds("event", npcuuid).contains(
								event.getQuestnumber())) {
							NPC npc = handler.getNPCByUUID(npcuuid);
							quest.AddCompletedQuest(event.getPlayer(),
									event.getQuestnumber(), "eventkill",
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
