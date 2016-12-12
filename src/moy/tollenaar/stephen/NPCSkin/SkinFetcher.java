package moy.tollenaar.stephen.NPCSkin;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.UUID;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import net.minecraft.server.v1_11_R1.MinecraftServer;

import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.util.UUIDTypeAdapter;

public class SkinFetcher extends BukkitRunnable{

	private final UUID npcuuid;
	private final UUID skinuuid;
	private final MinecraftSessionService server;
	private final String skin;
	private MoY plugin;
	private final SkinRunner runner;
	
	@SuppressWarnings("deprecation")
	public SkinFetcher(UUID npcuuid, UUID skinuuid,String skin, MoY plugin, SkinRunner runner ){
		this.npcuuid = npcuuid;
		this.skinuuid = skinuuid;
		this.server = MinecraftServer.getServer().az();
		this.skin  = skin;
		this.plugin = plugin;
		this.runner = runner;
		runTaskTimer(plugin, 20L, 40L);
	}
	public String getSkin(){
		return skin;
	}
	
	@Override
	public void run() {
		if(runner.isAlreadyLoaded(skinuuid)){
			this.cancel();
			return;
		}
		GameProfile temp = new GameProfile(skinuuid, skin);
		try {
			temp = fillProfileProperties(temp, true);
			Property prop = Iterables.getFirst(temp.getProperties().get("textures"), null);
			if(prop != null){
				SkinRunner.invokeCompletion(npcuuid, prop, skin,skinuuid, plugin);
				this.cancel();
			}
		} catch (Exception e) {
		}
		
	}
	
	private GameProfile fillProfileProperties(GameProfile profile,
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
				.invoke(((YggdrasilMinecraftSessionService) server)
						.getAuthenticationService(), url, null,
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
	private static Method REQUEST;
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
