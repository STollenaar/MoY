package MoY.tollenaar.stephen.NPC;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCSpawnEvent extends Event implements Cancellable{
	private static final HandlerList handlerList = new HandlerList();
	private boolean canceled = false;
	private final NPCSpawnReason reason;
	private final String type;
	private NPCEntity npc;
	private Location spawnlocation;
	
	
	public NPCEntity getNpc() {
		return npc;
	}

	public Location getSpawnlocation() {
		return spawnlocation;
	}

	public NPCSpawnEvent(NPCEntity npc, Location loc, NPCSpawnReason reason, String type){
		this.npc = npc;
		this.spawnlocation = loc;
		this.reason = reason;
		this.type = type;
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

	public NPCSpawnReason getReason() {
		return reason;
	}

	public String getType() {
		return type;
	}
}
