package moy.tollenaar.stephen.NPC;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.bukkit.Bukkit;

import moy.tollenaar.stephen.MistsOfYsir.MoY;

import com.comphenix.protocol.async.AsyncRunnable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.server.v1_8_R3.WorldServer;

public class NPCProfile {
	private static final Map<String, Property> TEXTURE_CACHE = Maps
			.newConcurrentMap();

	private static Set<UUID> toberespawned = new HashSet<UUID>();

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
			WorldServer nmsworld) {
		final GameProfile profile = createProfile(name);
		Property prop = loadSkin(skinOwner, nmsworld, (MoY) Bukkit
				.getPluginManager().getPlugin("MistsOfYsir"), name, profile);
		if (prop != null) {
			profile.getProperties().put("textures", prop);

		} else {
			toberespawned.add(profile.getId());
		}
		return new NPCProfile(profile, skinOwner);
	}

	
	
	private static GameProfile createProfile(String name){
		return createProfile(UUID.randomUUID(), name);
	}
	
	public static GameProfile createProfile(UUID uuid, String name){
		return new GameProfile(uuid, name);
	}

	public static NPCProfile presetProfile(GameProfile prof, String skin) {
		return new NPCProfile(prof, skin);
	}

	@SuppressWarnings("deprecation")
	protected static Property loadSkin(String skinOwner, WorldServer nmsworld,
			MoY plugin, String real, GameProfile profile) {
		UUID skinuuid = Bukkit.getOfflinePlayer(skinOwner).getUniqueId();
		if (TEXTURE_CACHE.get(skinuuid.toString()) != null) {
			return TEXTURE_CACHE.get(skinuuid.toString());
		} else {
			SkinFetch fetch = new SkinFetch(skinOwner, skinuuid, nmsworld
					.getMinecraftServer().aD(), profile.getId());
			if (SKIN_THREAD == null) {
				Bukkit.getScheduler().runTaskTimer(
						Bukkit.getPluginManager().getPlugin("MistsOfYsir"),
						SKIN_THREAD = new SkinThread(), 0, 5);
			}
				SKIN_THREAD.addRunnable(fetch);
				toberespawned.add(profile.getId());
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

	private static class SkinFetch implements AsyncRunnable {
		private final String owner;
		private final UUID realuuid;
		private final MinecraftSessionService repo;
		private final UUID npcuuid;

		public SkinFetch(String owner, UUID realuuid,
				MinecraftSessionService repo, UUID npcuuid) {
			this.owner = owner;
			this.realuuid = realuuid;
			this.repo = repo;
			this.npcuuid = npcuuid;
		}

		@Override
		public void run() {
			Property cached = TEXTURE_CACHE.get(realuuid.toString());
			if (cached != null) {
				// add returning texture
				if (toberespawned.contains(npcuuid)) {
					NPCHandler.invokeSkin(npcuuid, cached, owner);
				}
				return;
			} else {
				GameProfile temp;

				try {
					temp = fillProfileProperties(
							((YggdrasilMinecraftSessionService) repo)
									.getAuthenticationService(),
							new GameProfile(realuuid, ""), true);

				} catch (Exception ex) {
					SKIN_THREAD.delay();
					SKIN_THREAD.addRunnable(this);
					return;
				}
				if (temp != null) {
					Property textures = Iterables.getFirst(temp.getProperties()
							.get("textures"), null);
					if (textures != null) {
						if (textures.getValue() == null
								|| textures.getSignature() == null) {
							return;
						}
						TEXTURE_CACHE.put(realuuid.toString(),
								new Property("textures", textures.getValue(),
										textures.getSignature()));

						// add returning texture
						if (toberespawned.contains(npcuuid)) {
							NPCHandler.invokeSkin(npcuuid, textures, owner);
						}
						return;
					}
				}

			}
		}

		/*
		 * Yggdrasil's default implementation of this method silently fails
		 * instead of throwing an Exception like it should.
		 */
		private GameProfile fillProfileProperties(
				YggdrasilAuthenticationService auth, GameProfile profile,
				boolean requireSecure) throws Exception {
			URL url = HttpAuthenticationService
					.constantURL(new StringBuilder()
							.append("https://sessionserver.mojang.com/session/minecraft/profile/")
							.append(UUIDTypeAdapter.fromUUID(profile.getId()))
							.toString());
			url = HttpAuthenticationService.concatenateURL(
					url,
					new StringBuilder().append("unsigned=")
							.append(!requireSecure).toString());
			MinecraftProfilePropertiesResponse response = (MinecraftProfilePropertiesResponse) REQUEST
					.invoke(auth, url, null,
							MinecraftProfilePropertiesResponse.class);
			if (response == null) {
				return profile;
			}
			GameProfile result = new GameProfile(response.getId(),
					response.getName());
			result.getProperties().putAll(response.getProperties());
			profile.getProperties().putAll(response.getProperties());
			return result;
		}

		@Override
		public int getID() {
			return 0;
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isRunning() {
			return false;
		}

		@Override
		public boolean stop() throws InterruptedException {
			return false;
		}

	}

	private static class SkinThread implements AsyncRunnable {
		private volatile int delay = 0;
		
		private final BlockingDeque<Runnable> tasks = new LinkedBlockingDeque<Runnable>();

		public void addRunnable(Runnable r) {
			Iterator<Runnable> itr = tasks.iterator();
			while (itr.hasNext()) {
				if (((SkinFetch) itr.next()).realuuid
						.equals(((SkinFetch) r).realuuid)) {
					itr.remove();
				}
			}
			tasks.offer(r);
		}

		public void delay() {
			delay = 120;
		}

		@Override
		public void run() {
			if (delay > 0) {
				delay--;
				return;
			}
			Runnable r = tasks.pollFirst();
			if (r == null) {
				return;
			}
			r.run();
		}

		@Override
		public int getID() {
			return 0;
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isRunning() {
			return false;
		}

		@Override
		public boolean stop() throws InterruptedException {
			return false;
		}

	}

	private static Method REQUEST;
	private static SkinThread SKIN_THREAD;

	static {
		try {
			REQUEST = YggdrasilAuthenticationService.class.getDeclaredMethod(
					"makeRequest", URL.class, Object.class, Class.class);
			REQUEST.setAccessible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
