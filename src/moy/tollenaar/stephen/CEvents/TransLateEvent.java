package moy.tollenaar.stephen.CEvents;


import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_11_R1.ContainerEnchantTable;
import net.minecraft.server.v1_11_R1.EntityPlayer;

public class TransLateEvent extends EnchantEvent{

	private ItemStack item;
	private final Location enchantblock;
	
	public TransLateEvent(Player player, int cost, ItemStack item, Location block) {
		super(player, cost);
		this.item = item;
		this.enchantblock = block;
	}

	public ItemStack getItem() {
		return item;
	}

	public Location getEnchantblock() {
		return enchantblock;
	}
	
	public void deleteItem(){
		EntityPlayer player = ((CraftPlayer)getPlayer()).getHandle();
		player.activeContainer.setItem(0, null);
	}
	
	public void succes(){
		EntityPlayer player = ((CraftPlayer)getPlayer()).getHandle();
		ContainerEnchantTable table = (ContainerEnchantTable) player.activeContainer;
		table.costs[0] = 0;
		table.costs[1] = 0;
		table.costs[2] = 0;
		table.b();
	}
}
