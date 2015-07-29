package MoY.tollenaar.stephen.Quests;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.language.bm.Rule.RPattern;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
@SuppressWarnings("unused")
public class NPCSkin {
	private MoY plugin;

    private static final Random random = new Random();
    private static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Method getWorldHandle;
    private static Method getWorldTileEntity;
    private static Method setGameProfile;
    private static final Base64 base64 = new Base64();
    
	public NPCSkin(MoY instance){
		this.plugin = instance;
	}
	
	
	
	private static String returnUrl(String name){
		switch(name){
		case "dawn_nl":
			return "https://i.imgur.com/wQg8efk.png";
		case "guitargun":
			return "https://i.imgur.com/dTmFuhw.png";
		default:
			return null;
		}
	}
	
	
	
	
	
    // Method
    public static GameProfile getNonPlayerProfile(String name) {
            GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), name);
            newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + returnUrl(name) + "\"}}}")));
            return newSkinProfile;
    }
    
    public static GameProfile getUrlBasedSkin(String name){
    	String url = returnUrl(name);
    	GameProfile prof = new GameProfile(UUID.randomUUID(), null);
    	PropertyMap pm = prof.getProperties();
    	if(pm == null){
    		throw new IllegalArgumentException("Profile doesn't contain a property map");
    	}
    	byte[] encoded = base64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
    	pm.put("textures", new Property("textures", new String(encoded)));
    	
    	
    	return prof;
    }

    
    
    
    // Example
    public static String getRandomString(int length) {
            StringBuilder b = new StringBuilder(length);
            for(int j = 0; j < length; j++)
                    b.append(chars.charAt(random.nextInt(chars.length())));
            return b.toString();
    }

    // Refletion
    private static Class<?> getMCClass(String name) {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            String className = "net.minecraft.server." + version + name;
            Class<?> clazz = null;
            try {
                    clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                    e.printStackTrace();
            }
            return clazz;
    }

    // Refletion
    private static Class<?> getCraftClass(String name) {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            String className = "org.bukkit.craftbukkit." + version + name;
            Class<?> clazz = null;
            try {
                    clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                    e.printStackTrace();
            }
            return clazz;
    }
}
