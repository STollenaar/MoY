package moy.tollenaar.stephen.Util;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import moy.tollenaar.stephen.MistsOfYsir.MoY;

public class RewardsFormatter {

	private final List<String> rewardcommands;

	public RewardsFormatter(MoY instance) {
		this.rewardcommands = instance.fw.getRewards();
	}
	public ItemStack formatReward(String reward, ItemStack item){
		
		String r = getRewardNamed(reward);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add(r);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	
	@SuppressWarnings( "deprecation")
	private String getRewardNamed(String reward) {
		reward = reward.trim();
		String[] splitted = reward.split(" ");
		String builder = "";
		if (rewardcommands.contains(splitted[0])) {
			switch (splitted[0]) {
			// give back a format of the most important rewards
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
			case "eco":
				builder += "Pays you %amount%";
				builder = builder.replace("%amount%", splitted[3]);
				break;
			}
		}else{
			builder = "Gives you another reward. Which is secretly passed to you.";
		}
		return builder;
	}

	private String GetItemName(ItemStack item) {
		return CraftItemStack.asNMSCopy(item).getName();
	}
}
