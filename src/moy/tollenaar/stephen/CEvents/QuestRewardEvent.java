package moy.tollenaar.stephen.CEvents;

import java.util.ArrayList;
import java.util.List;

import moy.tollenaar.stephen.Quests.QuestsServerSide;

import org.bukkit.entity.Player;

public class QuestRewardEvent extends QuestPreProcessEvent{
	private QuestsServerSide quest;
	private final String npcname;
	
	public QuestRewardEvent(Player player, int number, String type, String npcname, QuestsServerSide quest) {
		super(player, number, type);
		this.quest = quest;
		this.npcname = npcname;
	}
	
	public List<String> getReward(){
		switch(getType()){
		case "kill":
			return quest.returnkill(getQuestnumber()).getReward();
		case "eventkill":
		case "eventharvest":
			return quest.returneventquest(getQuestnumber()).getReward();
		case "harvest":
			return quest.returnharvest(getQuestnumber()).getReward();
		case "talkto":
			return quest.returntalkto(getQuestnumber()).getReward();
		default:
			return new ArrayList<String>();
		}
	}

	public String getNpcname() {
		return npcname;
	}

}
