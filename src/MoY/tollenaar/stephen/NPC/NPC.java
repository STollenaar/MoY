package MoY.tollenaar.stephen.NPC;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public interface NPC {
	/**
	 * Gets the npc as entity
	 * @return
	 */
	public Player getBukkitEntity();
	
	/**
	 * see if there is a path to the location and walk there
	 * @param loc
	 * @return
	 */
	public boolean pathFinder(Location loc);
	
	
	
	/**
	 * find path with speed
	 * @param loc
	 * @param speed
	 * @return
	 */
	public boolean pathFinder(Location loc, double speed);
	
	
	/**
	 * find path with speed in range
	 * @param loc
	 * @param speed
	 * @param range
	 * @return
	 */
	public boolean pathFinder(Location loc, double speed, double range);
	
	
	
	/**
	 * get current location
	 * @return
	 */
	public Location getCurrentloc();
	
	/**
	 * set the NPC head towards that location
	 * @param loc
	 */
	public void lookatLocation(Location loc);
	
	/**
	 * set the NCP head towards the entity
	 * @param ent
	 */
	public void lookatEntity(Entity ent);
	
	/**
	 * plays the NPCAnimation
	 * @param animation
	 */
	public void playerAnimation(NPCAnimation animation); 
	
	/**
	 * sets yaw of NPC
	 * @param yar
	 */
	public void setYaw(float yaw);
	
	/**
	 * despawn the NPC
	 */
	public void despawn(NPCSpawnReason reason);
	
	/**
	 * get the NPC name
	 * @return
	 */
	public String getName();
	
	/**
	 * get the skin of the NPC
	 * @return
	 */
	public String getSkinName();
	
	/**
	 * get the uuid of the NPC
	 * @return
	 */
	public UUID getUniqueId();
	
	/**
	 * set the name of the NPC
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * set the skin of the NPC
	 * @param name
	 */
	public void setSkin(String name);
	
	
	/**
	 * spawn the npc
	 */
	public void spawn(Location loc, NPCSpawnReason reason, NPCEntity npc);
	
	/**
	 * respawn the npc
	 */
	public void respawn();
	
	/**
	 * set the skin of the npc
	 * @param prop
	 * @param profile
	 * @param skin
	 */
	public void setSkin(Property prop,  String skin);
	
}
