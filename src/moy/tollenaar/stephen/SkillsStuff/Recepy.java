package moy.tollenaar.stephen.SkillsStuff;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class Recepy {
	private static HashMap<Edibles, Recepy> recepies =  new HashMap<Edibles, Recepy>(); 	
	
	private final ItemStack output;
	private final ItemStack input;
	private final int chance;
	
	public Recepy(Edibles food, ItemStack in, ItemStack out, int chance){
		this.input = in;
		this.output = out;
		this.chance = chance;
		recepies.put(food, this);
	}
	
	
	public static Recepy getRecepy(Edibles food){
		return recepies.get(food);
	}


	public ItemStack getOutput() {
		return output;
	}


	public ItemStack getInput() {
		return input;
	}


	public int getChance() {
		return chance;
	}
	
	
	
}
