package moy.tollenaar.stephen.NPC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.fusesource.jansi.Ansi;





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
	public void onNPCSpawn(NPCSpawnEvent event) {
			event.getNpc().getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(plugin, false));
			npcs.put(event.getNpc().getUniqueID(), event.getNpc());
			plugin.fw.loadTraits(event.getNpc().getUniqueId());
	}
	
	

	@EventHandler
	public void onNPCDespawn(NPCDespawnEvent event) {
		npcs.remove(event.getNpc().getUniqueID());
	}

	public void onDisableEvent() {
		for (UUID uuid : npcs.keySet()) {
				plugin.fw.saveTraits(uuid);
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

	public void invokeSkin(UUID npc, Property prop, String skin) {
		if (npcs.get(npc) != null) {
			npcs.get(npc).setSkin(prop, skin);
		} else {
			skinTimer(npc, prop, skin);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for (UUID all : npcs.keySet()) {
			npcs.get(all).playerJoinPacket(event.getPlayer(), true);
		}
	}
	
	public void onLoadCompletion(){
		for(UUID all : npcs.keySet()){
			for(Player player : Bukkit.getOnlinePlayers()){
				npcs.get(all).playerJoinPacket(player, true);
			}
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
