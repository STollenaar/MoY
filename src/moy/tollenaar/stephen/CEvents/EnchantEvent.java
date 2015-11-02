package moy.tollenaar.stephen.CEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EnchantEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	
	private final Player player;
	private final int cost;
	boolean cancel = false;
	
	public EnchantEvent(Player player, int cost){
		this.player = player;
		this.cost = cost;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public int getCost() {
		return cost;
	
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
