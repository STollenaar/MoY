package moy.tollenaar.stephen.SkillsStuff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import moy.tollenaar.stephen.CEvents.EnchantEvent;
import moy.tollenaar.stephen.CEvents.ProgEvent;
import moy.tollenaar.stephen.CEvents.TransLateEvent;
import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;
import moy.tollenaar.stephen.Util.ParticleEffect;
import moy.tollenaar.stephen.Util.Runic;
import net.minecraft.server.v1_8_R3.ContainerEnchantTable;
import net.minecraft.server.v1_8_R3.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class EnchantingSkill implements Listener {
	private MoY plugin;
	private Playerinfo playerinfo;
	private Runic runic;

	public EnchantingSkill(MoY instance) {
		this.plugin = instance;
		this.playerinfo = instance.playerinfo;
		this.runic = instance.runic;
	}

	@EventHandler
	public void onTranslate(TransLateEvent event) {
		int r = event.getCost();
		int p = playerinfo.getplayer(event.getPlayer().getUniqueId())
				.getEnchanting();
		int chance = 60 - r + p;
		Random ran = new Random();
		int rand = ran.nextInt(61);
		if (rand < chance) {
			event.getItem().setItemMeta(
					runic.translateBook((BookMeta) event.getItem()
							.getItemMeta()));
			event.succes();
			event.getPlayer()
			.sendMessage(
					ChatColor.DARK_PURPLE
							+ "["
							+ ChatColor.GOLD
							+ "YSkill"
							+ ChatColor.DARK_PURPLE
							+ "] "
							+ ChatColor.AQUA
							+ "You feel a powerfull force coming from the book."
							+ "You try and try, and you are able to harness the power."
							+ "This dark power reveals the ancient power of this book.");
		} else {
			event.deleteItem();
			event.getPlayer()
					.sendMessage(
							ChatColor.DARK_PURPLE
									+ "["
									+ ChatColor.GOLD
									+ "YSkill"
									+ ChatColor.DARK_PURPLE
									+ "] "
									+ ChatColor.AQUA
									+ "You feel a powerfull force coming from the book."
									+ "You try and try, but you are unable to harness the power."
									+ "This dark power destroys the book.");
			for (Location in : calcArc(event.getEnchantblock())) {
				ParticleEffect.SPELL_MOB.display(0, 0, 0, 0, 0, in, 10);
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEnchant(EnchantEvent event) {
		int xp = (int) Math.ceil((double) event.getCost() / 10.0);
		// calling the progress event
		if (!event.isCancelled()) {
			Bukkit.getServer()
					.getPluginManager()
					.callEvent(
							new ProgEvent(event.getPlayer().getUniqueId(), 7,
									xp, true));
			// add some effects?
		}
	}

	@EventHandler
	public void onEnchant(EnchantItemEvent event) {
		if (event.getItem() != null
				&& event.getItem().getType() == Material.WRITTEN_BOOK) {
			Bukkit.getServer()
					.getPluginManager()
					.callEvent(
							new TransLateEvent(event.getEnchanter(), event
									.getExpLevelCost(), event.getItem(), event
									.getEnchantBlock().getLocation()));
		} else {
			Bukkit.getServer()
					.getPluginManager()
					.callEvent(
							new EnchantEvent(event.getEnchanter(), event
									.getExpLevelCost()));
		}
	}

	@EventHandler
	public void onEnchantInvenTory(final InventoryClickEvent event) {
		if (event.getClickedInventory() != null
				&& event.getClickedInventory().getType() == InventoryType.ENCHANTING) {
			if (event.getSlotType() == SlotType.CRAFTING
					&& event.getSlot() == 0) {
				if (event.getCursor() != null
						&& event.getCursor().getType() == Material.WRITTEN_BOOK) {
					final BookMeta book = (BookMeta) event.getCursor()
							.getItemMeta();
					if (book.getAuthor() != null
							&& runic.isRunic(book.getAuthor())) {
						new BukkitRunnable() {

							@Override
							public void run() {
								EntityPlayer player = ((CraftPlayer) ((Player) event
										.getWhoClicked())).getHandle();
								ContainerEnchantTable enchant = (ContainerEnchantTable) player.activeContainer;
								int lvl = playerinfo.getplayer(
										event.getWhoClicked().getUniqueId())
										.getEnchanting();
								// set the values! needed for skill
								int cost = runic.calcLvl(book.getTitle(), lvl);
								enchant.costs[0] = cost;
								enchant.costs[1] = cost;
								enchant.costs[2] = cost;

								// update the menu
								enchant.b();
							}
						}.runTask(plugin);
					}
				}
			}
		}
	}

	private List<Location> calcArc(Location enchant) {
		List<Location> circle = buildCircle(enchant, 2);
		List<Location> arc = new ArrayList<Location>();
		for (Location loc : circle) {

			double i = 0.125;

			while (i * 2 <= Math.abs(enchant.getX() - loc.getX()) + 1
					&& i * 2 <= Math.abs(enchant.getX() - loc.getX()) + 1) {

				double x = enchant.getX() + 0.5;
				double z = enchant.getZ() + 0.5;
				double step = i + 0.125;
				if (enchant.getX() - loc.getX() < 0) {
					x += i;
					if (enchant.getZ() - loc.getZ() < 0) {
						z += i;
					} else if (enchant.getZ() - loc.getZ() > 0) {
						z -= i;
					}
				} else if (enchant.getX() - loc.getX() > 0) {
					x -= i;
					if (enchant.getZ() - loc.getZ() < 0) {
						z += i;
					} else if (enchant.getZ() - loc.getZ() > 0) {
						z -= i;
					}

				} else {
					if (enchant.getZ() - loc.getZ() < 0) {
						z += i;
					} else if (enchant.getZ() - loc.getZ() > 0) {
						z -= i;
					}
				}
				double y1;
				if (Math.pow(i, 2) + enchant.getBlockY() < (enchant.getBlockY() - 1) * 2) {
					y1 = Math.pow(i, 2) + enchant.getBlockY();
				} else {
					y1 = (enchant.getBlockY() - 1) * 2;
				}
				double y2;
				if (-(Math.pow(i, 2)) + enchant.getBlockY() > (enchant
						.getBlockY() - 1)) {
					y2 = -(Math.pow(i, 2)) + enchant.getBlockY();
				} else {
					y2 = (enchant.getBlockY() - 1);
				}
				Location newt = new Location(enchant.getWorld(), x, y1 + 0.5, z);
				Location newb = new Location(enchant.getWorld(), x, y2 + 0.5, z);
				arc.add(newt);
				arc.add(newb);
				i = step;
			}
		}
		return arc;
	}

	private List<Location> buildCircle(Location loc, int r) {
		Set<Location> circleblocks = new HashSet<Location>();
		for (double i = 0.0; i < 360.0; i += 3) {
			double angle = i * Math.PI / 180;
			double x = loc.getX() + r * Math.cos(angle);
			double z = loc.getZ() + r * Math.sin(angle);
			Location l = new Location(loc.getWorld(), x, loc.getY(), z);
			circleblocks.add(l);
		}
		return new ArrayList<Location>(circleblocks);
	}

}
