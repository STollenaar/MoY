package moy.tollenaar.stephen.CEvents;

import org.bukkit.entity.Player;

public class QuestProcessEvent extends QuestPreProcessEvent{
	
	
	public QuestProcessEvent(Player player, int number, String type) {
		super(player, number, type);
	}


}
