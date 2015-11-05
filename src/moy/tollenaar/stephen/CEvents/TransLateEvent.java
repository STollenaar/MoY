package moy.tollenaar.stephen.CEvents;

import net.minecraft.server.v1_8_R3.ContainerEnchantTable;
import net.minecraft.server.v1_8_R3.EntityPlayer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
