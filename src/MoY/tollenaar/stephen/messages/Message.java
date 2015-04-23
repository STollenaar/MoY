package MoY.tollenaar.stephen.messages;

import java.util.UUID;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
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
			CraftPlayer p = (CraftPlayer) getPlayer();

			IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message
					+ "\"}");
			PacketPlayOutChat chat = new PacketPlayOutChat(cbc, (byte) 2);
			p.getHandle().playerConnection.sendPacket(chat);
		}
	}
}
