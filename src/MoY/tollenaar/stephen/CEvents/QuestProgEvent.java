package MoY.tollenaar.stephen.CEvents;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class QuestProgEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private int quest;
	private String type;
	private ItemStack item;
	private Entity entity;
	
	
	public QuestProgEvent(Player player, int quest, String type, ItemStack item){
		this.player = player;
		this.quest = quest;
		this.type = type;
		this.item = item;
	}
	public QuestProgEvent(Player player, int quest, String type, Entity entity){
		this.player = player;
		this.quest = quest;
		this.type = type;
		this.entity = entity;
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
	public int getQuest() {
		return quest;
	}
	public String getType() {
		return type;
	}
	public ItemStack getItem() {
		return item;
	}
	public Entity getEntity() {
		return entity;
	}
}
