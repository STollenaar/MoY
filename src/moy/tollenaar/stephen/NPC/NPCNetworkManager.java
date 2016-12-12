package moy.tollenaar.stephen.NPC;

import io.netty.channel.Channel;

import java.lang.reflect.Field;
import java.net.SocketAddress;


import net.minecraft.server.v1_11_R1.EnumProtocolDirection;
import net.minecraft.server.v1_11_R1.NetworkManager;

public class NPCNetworkManager extends NetworkManager{
	public NPCNetworkManager() {
		super(EnumProtocolDirection.CLIENTBOUND);
		try {
	            Field channel = NetworkManager.class.getDeclaredField("channel");
	            Field address = NetworkManager.class.getDeclaredField("l");

	            channel.setAccessible(true);
	            address.setAccessible(true);

	            Channel parent = new NPCChannel(null);
	            channel.set(this, parent);
	            address.set(this, new SocketAddress() {
	                private static final long serialVersionUID = 6994835504305404545L;
	            });
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	}

}
