package com.tollenaar.stephen.MoYCEvents;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MiningProgEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private UUID player;
	private int xp;
	private Block block;
	/**
	 * type
	 * 1 = wood
	 * 2 = mining
	 * 
	 */
	private int type;

	public MiningProgEvent(UUID player, TreeSpecies wood, Block block, int type) {
		this.player = player;
		this.block  = block;
		this.type = type;
		switch(wood){
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

	public MiningProgEvent(UUID player, Material ore, Block block, int type) {
		this.player = player;
		this.block  = block;
		this.type  = type;
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

	public UUID getPlayer() {
		return player;
	}

	public int getXp() {
		return xp;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Block getBlock() {
		return block;
	}

	public int getType() {
		return type;
	}



}
