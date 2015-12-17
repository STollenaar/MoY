package moy.tollenaar.stephen.NPCSkin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.properties.Property;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.NPC.NPCHandler;

public class SkinRunner {
	private static Map<UUID, SkinFetcher> NPCS_RUNNERS = new HashMap<UUID, SkinFetcher>();
	private static Map<UUID, Property> PROPERTY_CACHE = new HashMap<UUID, Property>();
	
	
	
	public void addRunner(UUID npcuuid, String skinName,UUID offline, MoY plugin){
		if(NPCS_RUNNERS.get(npcuuid) != null){
			NPCS_RUNNERS.get(npcuuid).cancel();
		}
		NPCS_RUNNERS.put(npcuuid, new SkinFetcher(npcuuid, offline, skinName, plugin, this));
	}
	
	protected static void invokeCompletion(UUID npcuuid, Property prop, String skin, UUID skinuuid, MoY plugin){
		NPCHandler handler = plugin.getNPCHandler();
		handler.invokeSkin(npcuuid, prop, skin);
		
		PROPERTY_CACHE.put(skinuuid, prop);
		NPCS_RUNNERS.remove(npcuuid);
	}
	public boolean isRunning(UUID npcuuid){
		if(NPCS_RUNNERS.get(npcuuid) != null){
			return true;
		}else{
			return false;
		}
	}
	protected boolean isAlreadyLoaded(UUID uuid){
		if(PROPERTY_CACHE.get(uuid) != null){
			return true;
		}else{
			return false;
		}
	}
	
	public String getSkinRunner(UUID npcuuid){
		return NPCS_RUNNERS.get(npcuuid).getSkin();
	}
	
	public Property getCache(UUID skinuuid){
		return PROPERTY_CACHE.get(skinuuid);
	}
	
}
