package moy.tollenaar.stephen.SkillsStuff;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import moy.tollenaar.stephen.CEvents.ProgEvent;
import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;
import moy.tollenaar.stephen.messages.InfoBar;

public class FurnaceStorage {

	private static HashMap<Block, FurnaceStorage> furnaces = new HashMap<Block, FurnaceStorage>();

	private Block furnace;
	private ItemStack failures;
	private ItemStack toBurn;
	private InfoBar info;
	
	private Playerinfo playerinfo;
	
	
	private HashMap<UUID, Integer> players;

	public FurnaceStorage(Block block, MoY instance) {
		this.furnace = block;
		this.players = new HashMap<UUID, Integer>();
		this.info = new InfoBar(instance);
		this.playerinfo = instance.playerinfo;
		save();

	}

	public void addfailure(ItemStack item) {
		if (failures == null) {
			failures = item;
		} else {
			failures.setAmount(failures.getAmount() + 1);
		}
		UUID first = returnfirst(players.keySet());
		players.put(first, players.get(first) - 1);

		info.makemessage(first, -3);
		save();
	}

	public void removefailure(InventoryClickEvent event, UUID player) {
		ItemStack current = event.getCurrentItem();
		if (current.getAmount() == failures.getAmount()) {
			failures = null;
		} else if (current.getAmount() < failures.getAmount()) {
			failures.setAmount(failures.getAmount() - current.getAmount());
		}

		save();
	}

	public void addsucces(ItemStack item) {

		UUID first = returnfirst(players.keySet());
		players.put(first, players.get(first) - 1);
		Playerstats p = playerinfo.getplayer(first);
		
		ProgEvent e;

		if (item.getType().isEdible()) {
			e = new ProgEvent(first, 3, Edibles.GetEdible(item)
					.SuccesCalc(p.getCooking()));
		} else {
			e = new ProgEvent(first, item.getType(), null, 4);
		}
		Bukkit.getServer().getPluginManager().callEvent(e);
		save();
	}

	public void removeburing(InventoryClickEvent event, UUID player) {
		if (players.containsKey(player)) {
			ItemStack current = event.getCurrentItem();
			if (current.getAmount() == players.get(player)) {
				players.remove(player);
			} else if (current.getAmount() < players.get(player)) {
				players.put(player, players.get(player) - current.getAmount());
			} else if (current.getAmount() > players.get(player)) {
				current.setAmount(players.get(player));
			}
		} else {
			event.setCancelled(true);
		}
		save();
	}

	public void addburning(ItemStack item, UUID player) {
		if (toBurn == null) {
			toBurn = item;
		} else {
			toBurn.setAmount(toBurn.getAmount() + item.getAmount());
		}

		if (players.containsKey(player)) {
			players.put(player, players.get(player) + item.getAmount());
		} else {
			players.put(player, item.getAmount());
		}
		save();
	}

	public UUID returnfirst(Set<UUID> keys) {
		for (UUID k : keys) {
			return k;
		}
		return null;
	}

	public static FurnaceStorage GetStorage(Block block) {
		if (furnaces.get(block) != null) {
			return furnaces.get(block);
		} else {
			return null;
		}
	}

	public Block getFurnace() {
		return furnace;
	}

	public ItemStack getFailures() {
		return failures;
	}

	public ItemStack getToBurn() {
		return toBurn;
	}

	public HashMap<UUID, Integer> getPlayers() {
		return players;
	}

	public static Set<Block> GetAll() {
		return furnaces.keySet();
	}

	public static void RemoveStorage(Block block) {
		furnaces.remove(block);
	}

	public void save() {
		furnaces.put(furnace, this);
	}

	public void loadall(HashMap<UUID, Integer> pl, ItemStack tob,
			ItemStack failures) {
		this.players = pl;
		this.toBurn = tob;
		this.failures = failures;
		save();
	}
}
