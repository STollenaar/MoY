package moy.tollenaar.stephen.SkillsStuff;

import moy.tollenaar.stephen.PlayerInfo.Playerstats;

import org.bukkit.inventory.ItemStack;

public enum Edibles {
	POTATO(1, "Potato"), FISH(1, "Fish"), STEAK(11, "Steak"), SALMON(21,
			"Salmon"), CHICKEN(31, "Chicken"), PORKCHOP(41, "Porkchop"), RABBIT(
			11, "Rabbit"), MUTTON(21, "Mutton");

	private final int lvl;
	private final String name;

	Edibles(int lvl, String name) {
		this.lvl = lvl;
		this.name = name;
	}

	public int getLvl() {
		return lvl;

	}

	public static Edibles GetEdible(ItemStack item) {
		switch (item.getType()) {
		case POTATO:
			return POTATO;
		case RAW_BEEF:
			return STEAK;
		case RAW_CHICKEN:
			return CHICKEN;
		case RAW_FISH:
			if (item.getDurability() == 0) {
				return FISH;
			} else {
				return SALMON;
			}
		case MUTTON:
			return MUTTON;
		case RABBIT:
			return RABBIT;
		default:
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public int SuccesCalc(int current) {
		int chance;
		if (getLvl() <= current) {
			chance = 100;
		} else {
			int cinc = current / 10;
			int vinc = getLvl() / 10;

			int diff = vinc - cinc;

			chance = 23 - diff * 3;

		}
		return chance;

	}

	public int SuccesCalc(int current, Recepy recepy, Playerstats p) {
		int initial = SuccesCalc(current);
		if (p.hasRecepy(recepy)) {
			if (initial < 50) {
				initial += (recepy.getChance() * 2);
			}
		}

		return initial;
	}

}
