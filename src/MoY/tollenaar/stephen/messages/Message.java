package MoY.tollenaar.stephen.messages;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;

public class Message extends BukkitRunnable {
	private String message;
	private UUID player;


	public Message(MoY plugin, String message, UUID player) {
		this.message = message;
		this.player = player;
		run();
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(player);
	}

	public String getMessage() {
		return message;
	}

	@Override
	public void run() {
		if (getPlayer() != null) {
			
			try {
				
				
				Class<?> Chats = this.getNMSClass("ChatSerializer");
				Method i = Chats.getMethod("a", String.class);
				Object info = i.invoke(this.getNMSClass("IChatBaseComponent"), "{\"text\": \"" + message
					+ "\"}");
				Class<?> packet = this.getNMSClass("PacketPlayOutChat");
				Constructor<?> packetconstr = packet.getConstructor(this.getNMSClass("IChatBaseComponent"), byte.class);
				Object p = packetconstr.newInstance(info, (byte)2);
				Method sendp = getNMSClass("PlayerConnection").getMethod("sendPacket", this.getNMSClass("Packet"));
				sendp.invoke(this.getConnection(getPlayer()), p);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}

		}
	}
	
	private Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
	    String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
	    String name = "net.minecraft.server." + version + nmsClassString;
	    Class<?> nmsClass = Class.forName(name);
	    return nmsClass;
	}
	private Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	    Method getHandle = player.getClass().getMethod("getHandle");
	    Object nmsPlayer = getHandle.invoke(player);
	    Field conField = nmsPlayer.getClass().getField("playerConnection");
	    Object con = conField.get(nmsPlayer);
	    return con;
	}
}
