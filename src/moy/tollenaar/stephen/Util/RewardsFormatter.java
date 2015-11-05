package moy.tollenaar.stephen.Util;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import moy.tollenaar.stephen.Files.Filewriters;
import moy.tollenaar.stephen.MistsOfYsir.MoY;

public class RewardsFormatter {
	private MoY plugin;

	private final List<String> rewardcommands;

	public RewardsFormatter(MoY instance) {
		this.plugin = instance;
		this.rewardcommands = instance.fw.getRewards();
	}

	@SuppressWarnings("deprecation")
	private String getRewardNamed(String reward) {
		String[] splitted = reward.split(" ");
		String builder = "";
		if (rewardcommands.contains(splitted[0])) {
			switch (splitted[0]) {
			// give back a format of the most recurring rewards
			case "give":
				builder += "Gives %amount% %item%";
				builder = builder.replace("%amount%", splitted[3]);
				builder = builder.replace("%item%", GetItemName(new ItemStack(
						Material.getMaterial(Integer.parseInt(splitted[2])))));
				break;
			case "skill":
				builder += "Gives %amount% %sort% xp";
				builder = builder.replace("%sort%", splitted[1]);
				builder = builder.replace("%amount%", splitted[2]);
				break;
			default:
				for (String in : rewardcommands) {
					if (in.equals(splitted[0])) {
						// do some default stuff here

					}
				}
			}
		}
		return builder;
	}

	private String GetItemName(ItemStack item) {
		return CraftItemStack.asNMSCopy(item).getName();
	}
}
