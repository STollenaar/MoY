package moy.tollenaar.stephen.CEvents;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuestProgEvent extends QuestPreProcessEvent{

	
	private final ItemStack item;
	private final Entity entity;
	
	
	public QuestProgEvent(Player player, int quest, String type, ItemStack item){
		super(player, quest, type);
		this.item = item;
		this.entity = null;
	}
	public QuestProgEvent(Player player, int quest, String type, Entity entity){
		super(player, quest, type);
		this.entity = entity;
		this.item = null;
		
	}
	

	public ItemStack getItem() {
		return item;
	}
	public Entity getEntity() {
		return entity;
	}
}
