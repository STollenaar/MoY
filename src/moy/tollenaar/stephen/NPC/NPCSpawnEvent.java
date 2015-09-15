package moy.tollenaar.stephen.NPC;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCSpawnEvent extends Event implements Cancellable{
	private static final HandlerList handlerList = new HandlerList();
	private boolean canceled = false;
	private final NPCSpawnReason reason;
	private NPCEntity npc;
	private Location spawnlocation;
	private final int id;
	
	private final String shop;
	
	public NPCEntity getNpc() {
		return npc;
	}

	public Location getSpawnlocation() {
		return spawnlocation;
	}

	public NPCSpawnEvent(NPCEntity npc, Location loc, NPCSpawnReason reason, String shop, int id){
		this.npc = npc;
		this.spawnlocation = loc;
		this.reason = reason;
		this.shop = shop;
		this.id = id;
	}
	
	public NPCSpawnEvent(NPCEntity npc, Location loc, NPCSpawnReason reason, String shop){
		this(npc, loc, reason, shop, -1);
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

	public String getShop() {
		return shop;
	}

	public int getId() {
		return id;
	}

}
