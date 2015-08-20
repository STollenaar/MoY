package MoY.tollenaar.stephen.NPC;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCDespawnEvent extends Event implements Cancellable{

	private NPCEntity npc;
	
	private static final HandlerList handlerList = new HandlerList();
	private boolean canceled = false;
	
	public NPCDespawnEvent(NPCEntity npc){
		this.npc = npc;
	}
	
	public boolean isCancelled() {
		return canceled;
	}

	public void setCancelled(boolean cancel) {
		this.canceled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	public NPCEntity getNpc() {
		return npc;
	}

}
