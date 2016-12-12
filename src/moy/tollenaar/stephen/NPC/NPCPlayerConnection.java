package moy.tollenaar.stephen.NPC;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;

import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.NetworkManager;
import net.minecraft.server.v1_11_R1.PlayerConnection;

public class NPCPlayerConnection extends PlayerConnection{
    public NPCPlayerConnection(NetworkManager networkmanager, EntityPlayer entityplayer) {
        super(((CraftServer) Bukkit.getServer()).getServer(), networkmanager, entityplayer);
    }
}
