package MoY.tollenaar.stephen.CEvents;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ProgEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private UUID player;
	private int xp;
	private Block block;
	private boolean q = false;

	private int type;

	public ProgEvent(UUID player, TreeSpecies wood, Block block, int type) {
		this.player = player;
		this.block = block;
		this.type = type;
		switch (wood) {
		case ACACIA:
			this.xp = 1;
			break;
		case BIRCH:
			this.xp = 1;
			break;
		case DARK_OAK:
			this.xp = 3;
			break;
		case GENERIC:
			this.xp = 1;
			break;
		case JUNGLE:
			this.xp = 2;
			break;
		case REDWOOD:
			this.xp = 1;
			break;
		default:
			this.xp = 1;
			break;
		}
	}

	public ProgEvent(UUID player, Material ore, Block block, int type) {
		this.player = player;
		this.block = block;
		this.type = type;
		if (ore == Material.COAL_ORE) {
			this.xp = 2;
		} else if (ore == Material.IRON_ORE) {
			this.xp = 4;
		} else if (ore == Material.GOLD_ORE) {
			this.xp = 7;
		} else if (ore == Material.LAPIS_ORE) {
			this.xp = 3;
		} else if (ore == Material.REDSTONE_ORE) {
			this.xp = 2;
		} else if (ore == Material.DIAMOND_ORE) {
			this.xp = 10;
		} else {
			this.xp = 1;
		}
	}

	public ProgEvent(UUID player, int type, int chance) {
		this.player = player;
		this.type = type;
		switch (chance) {
		case 100:
			this.xp = 1;
			break;
		case 17:
			this.xp = 4;
			break;
		case 20:
			this.xp = 2;
			break;
		case 14:
			this.xp = 6;
			break;
		case 11:
			this.xp = 8;
			break;
		}

	}

	public ProgEvent( UUID player,int type, int xp, boolean q) {
		this.player = player;
		this.type = type;
		this.xp = xp;
		this.q = q;
	}

	public UUID getPlayer() {
		return player;
	}

	public int getXp() {
		return xp;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Block getBlock() {
		return block;
	}

	public int getType() {
		return type;
	}
	
	public boolean getQ(){
		return q;
	}

}
