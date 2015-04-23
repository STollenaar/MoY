package MoY.tollenaar.stephen.SkillsStuff;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Tree;

public enum SkillLvls {
	ACACIA(TreeSpecies.ACACIA, 11, null), OAK(TreeSpecies.GENERIC, 1, null), BIRCH(
			TreeSpecies.BIRCH, 1, null), DARKOAK(TreeSpecies.DARK_OAK, 41, null), SPRUCE(
			TreeSpecies.REDWOOD, 11, null), JUNGLE(TreeSpecies.JUNGLE, 21, null), IRON(
			null, 11, Material.IRON_ORE), CLAY(null, 1, Material.CLAY), COAL(
			null, 1, Material.COAL_ORE), GOLD(null, 31, Material.GOLD_ORE), REDSTONE(
			null, 21, Material.REDSTONE_ORE), LAPIS(null, 21,
			Material.LAPIS_ORE), DIAMOND(null, 41, Material.DIAMOND_ORE)

	;

	private final int lvl;
	private final TreeSpecies tree;
	private final Material ore;

	SkillLvls(TreeSpecies tree, int lvl, Material ore) {
		this.lvl = lvl;
		this.tree = tree;
		this.ore = ore;
	}

	public int getLvl() {
		return lvl;
	}

	public static SkillLvls GetSkill(Block block) {
		if (block.getType() == Material.LOG
				|| block.getType() == Material.LOG_2) {
			TreeSpecies log;
			if (block.getType() == Material.LOG) {
				log = ((Tree) block.getState().getData()).getSpecies();
			} else {
				@SuppressWarnings("deprecation")
				short data = block.getData();
				if (data == 0 || data == 4 || data == 8 || data == 12) {
					log = TreeSpecies.ACACIA;
				} else {
					log = TreeSpecies.DARK_OAK;
				}
			}
			for (SkillLvls val : values()) {
				if (val.getTree() != null && val.getTree() == log) {
					return val;
				}
			}
		} else {
			for (SkillLvls val : values()) {
				if (val.getOre() != null && val.getOre() == block.getType()) {
					return val;
				}
			}
		}
		return null;
	}

	public static SkillLvls GetSkill(ItemStack block) {
		if (block.getType() == Material.LOG
				|| block.getType() == Material.LOG_2) {
			TreeSpecies log;
			if (block.getType() == Material.LOG) {
				log = ((Tree) block.getData()).getSpecies();
			} else {
				short data = block.getDurability();
				if (data == 0 || data == 4 || data == 8 || data == 12) {
					log = TreeSpecies.ACACIA;
				} else {
					log = TreeSpecies.DARK_OAK;
				}
			}
			for (SkillLvls val : values()) {
				if (val.getTree() != null && val.getTree() == log) {
					return val;
				}
			}
		} else {
			for (SkillLvls val : values()) {
				if (val.getOre() != null && val.getOre() == block.getType()) {
					return val;
				}
			}
		}
		return null;
	}
	
	public TreeSpecies getTree() {
		return tree;
	}

	public Material getOre() {
		return ore;
	}
}
