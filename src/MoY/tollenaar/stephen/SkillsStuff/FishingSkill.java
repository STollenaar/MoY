package MoY.tollenaar.stephen.SkillsStuff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ItemFish.EnumFish;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.PossibleFishingResult;
import net.minecraft.server.v1_8_R3.WeightedRandom;


public class FishingSkill {
	private static final List<PossibleFishingResult> d = Arrays
			.asList(new PossibleFishingResult[] {
					(new PossibleFishingResult(new ItemStack(
							Items.LEATHER_BOOTS), 10)).a(0.9F),
					new PossibleFishingResult(new ItemStack(Items.LEATHER), 10),
					new PossibleFishingResult(new ItemStack(Items.BONE), 10),
					new PossibleFishingResult(new ItemStack(Items.POTION), 10),
					new PossibleFishingResult(new ItemStack(Items.STRING), 5),
					(new PossibleFishingResult(
							new ItemStack(Items.FISHING_ROD), 2)).a(0.9F),
					new PossibleFishingResult(new ItemStack(Items.BOWL), 10),
					new PossibleFishingResult(new ItemStack(Items.STICK), 5),
					new PossibleFishingResult(new ItemStack(Items.DYE, 10, 0),
							1),
					new PossibleFishingResult(new ItemStack(
							Blocks.TRIPWIRE_HOOK), 10),
					new PossibleFishingResult(
							new ItemStack(Items.ROTTEN_FLESH), 10) });
	private static final List<PossibleFishingResult> e = Arrays
			.asList(new PossibleFishingResult[] {
					new PossibleFishingResult(new ItemStack(Blocks.WATERLILY),
							1),
					new PossibleFishingResult(new ItemStack(Items.NAME_TAG), 1),
					new PossibleFishingResult(new ItemStack(Items.SADDLE), 1),
					(new PossibleFishingResult(new ItemStack(Items.BOW), 1)).a(
							0.25F).a(),
					(new PossibleFishingResult(
							new ItemStack(Items.FISHING_ROD), 1)).a(0.25F).a(),
					(new PossibleFishingResult(new ItemStack(Items.BOOK), 1))
							.a() });
	private static final List<PossibleFishingResult> f = Arrays
			.asList(new PossibleFishingResult[] {
					new PossibleFishingResult(new ItemStack(Items.FISH, 1,
							EnumFish.COD.a()), 60),
					new PossibleFishingResult(new ItemStack(Items.FISH, 1,
							EnumFish.SALMON.a()), 25),
					new PossibleFishingResult(new ItemStack(Items.FISH, 1,
							EnumFish.CLOWNFISH.a()), 2),
					new PossibleFishingResult(new ItemStack(Items.FISH, 1,
							EnumFish.PUFFERFISH.a()), 13) });
	
	
	public Map<Integer, org.bukkit.inventory.ItemStack> GetItem(int lvl){
		Map<Integer, ItemStack> m = Calc(lvl);
		Map<Integer, org.bukkit.inventory.ItemStack> a = new HashMap<Integer, org.bukkit.inventory.ItemStack>();
		int i = (int) m.keySet().toArray()[0];
		a.put(i, CraftItemStack.asBukkitCopy(m.get(i)));
		
		return a;
	}

	private Map<Integer, ItemStack> Calc(int lvl) {
		Random r = new Random();
		int n = r.nextInt(100);
		Map<Integer, ItemStack> m = new HashMap<Integer, ItemStack>();
		if (n <= CalcNormal(lvl)) {
			m.put(100, f.get(0).a(r));
		} else if (n <= CalcSalm(lvl) + CalcNormal(lvl)) {
			m.put(20, f.get(1).a(r));
		} else if (n <= CalcJunk(lvl) + CalcSalm(lvl) + CalcNormal(lvl)) {
			m.put(100, ((PossibleFishingResult) WeightedRandom.a(r, d))
					.a(r));
		} else if (n <= CalcClown(lvl) + CalcJunk(lvl) + CalcSalm(lvl)
				+ CalcNormal(lvl)) {
			m.put(14, f.get(2).a(r));
		} else if (n <= CalcTreasure(lvl) + CalcClown(lvl) + CalcJunk(lvl)
				+ CalcSalm(lvl) + CalcNormal(lvl)) {
			m.put(11, ((PossibleFishingResult) WeightedRandom.a(r, e))
					.a(r));
		} else {
			m.put(17, f.get(3).a(r));
		}
		return m;
	}

	private double CalcSalm(int lvl) {
		return 7 + (3 * Math.ceil(lvl / 10.0));
	}

	private double CalcJunk(int lvl) {
		return 34 - (5 * Math.ceil(lvl / 10.0));
	}

	private double CalcClown(int lvl) {
		return -0.5 + (3 * Math.ceil(lvl / 10.0));
	}

	private double CalcTreasure(int lvl) {
		return -2 + (3 * Math.ceil(lvl / 10));
	}

	private double CalcNormal(int lvl) {
		if (55 - (7 * (Math.ceil(lvl / 10.0))) < 25) {
			return 25;
		} else {
			return 55 - (7 * (Math.ceil(lvl / 10.0)));
		}
	}
}
