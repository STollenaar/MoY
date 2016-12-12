package moy.tollenaar.stephen.NPC;

import java.util.UUID;

import org.bukkit.Bukkit;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.NPCSkin.SkinRunner;
import net.minecraft.server.v1_11_R1.WorldServer;

public class NPCProfile {


	/**
	 * Load a profile with a custom skin
	 * 
	 * @param name
	 *            Display name of profile
	 * @param skinOwner
	 *            Owner of profile skin
	 * @return NPCProfile
	 */
	public static NPCProfile loadProfile(String name, String skinOwner,
			WorldServer nmsworld, MoY plugin) {
		final GameProfile profile = createProfile(name);
		Property prop = loadSkin(skinOwner, profile.getId(), plugin);
		if (prop != null) {
			profile.getProperties().put("textures", prop);
		}
		return new NPCProfile(profile, skinOwner);
	}

	private static GameProfile createProfile(String name) {
		return createProfile(UUID.randomUUID(), name);
	}

	public static GameProfile createProfile(UUID uuid, String name) {
		return new GameProfile(uuid, name);
	}

	public static NPCProfile presetProfile(GameProfile prof, String skin) {
		return new NPCProfile(prof, skin);
	}

	@SuppressWarnings("deprecation")
	protected static Property loadSkin(String skinOwner, UUID npcuuid, MoY plugin) {
		SkinRunner skin =plugin.getSkinRunner();
		UUID skinuuid = Bukkit.getOfflinePlayer(skinOwner).getUniqueId();
		if(skin.getCache(skinuuid) != null){
			return skin.getCache(skinuuid);
		}else{
			if(!skin.isRunning(npcuuid) || (skin.isRunning(npcuuid) && !skin.getSkinRunner(npcuuid).equals(skinOwner))){
			skin.addRunner(npcuuid, skinOwner, skinuuid,plugin);
			}
			return null;
		}
	}

	private final String skin;

	private final GameProfile handle;

	/**
	 * Return a profile with a steve skin.
	 * 
	 * @param name
	 *            Display name of profile
	 */
	public NPCProfile(String name) {
		this(new GameProfile(UUID.randomUUID(), name), name);
	}

	private NPCProfile(GameProfile handle, String skin) {
		this.handle = handle;
		this.skin = skin;
	}

	/**
	 * Get the profile UUID
	 * 
	 * @return Profile UUID
	 */
	public UUID getUUID() {
		return handle.getId();
	}

	/**
	 * Get the profile display name
	 * 
	 * @return Display name
	 */
	public String getDisplayName() {
		return handle.getName();
	}

	/**
	 * Get the original game profile
	 * 
	 * @return Original game profile
	 */
	public GameProfile getHandle() {
		return handle;
	}

	public String getSkin() {
		return skin;
	}

	
}