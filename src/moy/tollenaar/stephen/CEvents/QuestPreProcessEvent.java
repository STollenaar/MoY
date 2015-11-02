package moy.tollenaar.stephen.CEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuestPreProcessEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	
	private final Player player;
	private final int questnumber;
	private final String type;
	
	public QuestPreProcessEvent(Player player, int number, String type){
		this.player = player;
		this.questnumber = number;
		this.type = type;
	}
	
	public Player getPlayer() {
		return player;
	}

	public int getQuestnumber() {
		return questnumber;
	}

	public String getType() {
		return type;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	
}
