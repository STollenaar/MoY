package moy.tollenaar.stephen.NPC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.fusesource.jansi.Ansi;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.mojang.authlib.properties.Property;

import moy.tollenaar.stephen.MistsOfYsir.MoY;

public class NPCHandler implements Listener {
	private static Map<UUID, NPCEntity> npcs = new HashMap<UUID, NPCEntity>();

	private static Set<UUID> stores = new HashSet<UUID>();
	
	private static MoY plugin;

	@SuppressWarnings("static-access")
	public NPCHandler(MoY instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onShopCreate(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().contains("/manageshop create") && hasPermission(event.getPlayer())) {
			String player = event.getPlayer().getDisplayName();
			Location center = event.getPlayer().getLocation();
			NPCEntity npc = new NPCEntity(center.getWorld(), center,
					NPCProfile.loadProfile(player, event.getPlayer().getName(),
							((CraftWorld) center.getWorld()).getHandle()),
					plugin.getNetwork(), plugin, event.getMessage().split(" ")[2], -1);
			npc.spawn(center, NPCSpawnReason.SHOP_SPAWN, npc);
		}
	}
	
	private boolean hasPermission(Player player){
		PermissionUser user = PermissionsEx.getUser(player);
		return user.has("hyperconomy.playershop.create");
	}

	@EventHandler
	public void onNPCSpawn(NPCSpawnEvent event) {
		if(event.getReason() == NPCSpawnReason.SHOP_SPAWN){
			NPCMetadataStore meta = new NPCMetadataStore(event.getNpc().getName(), event.getShop(), event.getId());
		event.getNpc()
				.getBukkitEntity()
				.setMetadata("NPC",
						new FixedMetadataValue(plugin, meta));
		}else{
			event.getNpc().getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(plugin, false));
		}
		npcs.put(event.getNpc().getUniqueID(), event.getNpc());

		if (event.getReason() == NPCSpawnReason.RESPAWN) {
			plugin.questers.resetHear(event.getNpc().getUniqueId());
		}else if(event.getReason() == NPCSpawnReason.SHOP_SPAWN){
			stores.add(event.getNpc().getUniqueId());
		}

	}

	@EventHandler
	public void onNPCDespawn(NPCDespawnEvent event) {
		npcs.remove(event.getNpc().getUniqueID());
	}

	public void onDisableEvent() {
		for (UUID uuid : npcs.keySet()) {
			npcs.get(uuid).despawn(NPCSpawnReason.DESPAWN);
		}
	}

	public NPCEntity getNPCByUUID(UUID uuid) {
		return npcs.get(uuid);
	}

	public NPCEntity getNPCByEntity(Entity ent) {
		return getNPCByUUID(ent.getUniqueId());
	}

	public void DublicateKiller() {
		plugin.getLogger()
				.info(Ansi.ansi().fg(Ansi.Color.RED)
						+ "CRASH OCCURED. DELETING DUPLICATE NPCS. BEWARE FOR DELAY"
						+ Ansi.ansi().fg(Ansi.Color.WHITE));
		for (World world : Bukkit.getWorlds()) {
			for (Entity ent : world.getEntities()) {
				if (ent.hasMetadata("NPC")) {
					ent.remove();
				}
			}
		}
	}

	public static void invokeSkin(UUID npc, Property prop, String skin) {
		if (npcs.get(npc) != null) {
			npcs.get(npc).setSkin(prop, skin);
		} else {
			skinTimer(npc, prop, skin);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for (UUID all : npcs.keySet()) {
			npcs.get(all).playerJoinPacket(event.getPlayer());
		}
	}

	private static void skinTimer(final UUID npc, final Property prop,
			final String skin) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (npcs.get(npc) != null) {
					npcs.get(npc).setSkin(prop, skin);
				} else {
					skinTimer(npc, prop, skin);
				}
			}
		}, 2L);
	}
	
	public Set<UUID> getStores(){
		return stores;
	}
}
