package moy.tollenaar.stephen.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import moy.tollenaar.stephen.MistsOfYsir.MoY;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.Pathfinder;
import net.minecraft.server.v1_8_R3.PathfinderNormal;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class NPCEntity extends EntityPlayer implements NPC {
	private static final int RADIUS = Bukkit.getViewDistance() * 16;
	private MoY plugin;

	private final Pathfinder pathfinder;
	@SuppressWarnings("unused")
	private NPCPath path;
	private String skin;
	private UUID uuid;
	private boolean spawned;
	private List<Integer> speechNodes = new ArrayList<Integer>();
	public boolean isSpawned() {
		return spawned;
	}

	public NPCEntity(World world, Location location, NPCProfile profile,
			NPCNetworkManager networkManager, MoY plugin) {
		super(((CraftServer) Bukkit.getServer()).getServer(),
				((CraftWorld) world).getHandle(), profile.getHandle(),
				new PlayerInteractManager(((CraftWorld) world).getHandle()));

		this.skin = profile.getSkin();
		this.uuid = profile.getUUID();
		this.fauxSleeping = true;
		this.playerConnection = new NPCPlayerConnection(networkManager, this);
		this.bukkitEntity = new CraftPlayer((CraftServer) Bukkit.getServer(),
				this);
		// making the pathfinder for the npc
		PathfinderNormal normal = new PathfinderNormal();
		normal.a(true);
		this.pathfinder = new Pathfinder(normal);
		this.spawned = false;
		this.plugin = plugin;
		this.playerInteractManager.b(EnumGamemode.CREATIVE);
	}

	public NPCEntity(World world, Location location, NPCProfile profile,
			NPCNetworkManager networkManager, MoY plugin, NPCSpawnReason reason) {
		super(((CraftServer) Bukkit.getServer()).getServer(),
				((CraftWorld) world).getHandle(), profile.getHandle(),
				new PlayerInteractManager(((CraftWorld) world).getHandle()));

		this.skin = profile.getSkin();
		this.uuid = profile.getUUID();
		this.fauxSleeping = true;
		this.playerConnection = new NPCPlayerConnection(networkManager, this);
		this.bukkitEntity = new CraftPlayer((CraftServer) Bukkit.getServer(),
				this);
		// making the pathfinder for the npc
		PathfinderNormal normal = new PathfinderNormal();
		normal.a(true);
		this.pathfinder = new Pathfinder(normal);
		this.spawned = false;
		this.plugin = plugin;
		if (reason != NPCSpawnReason.RESPAWN) {
			spawn(location, reason);
		}
		this.playerInteractManager.b(EnumGamemode.CREATIVE);
	}

	public boolean pathFinder(Location loc) {
		return pathFinder(loc, 0.2);
	}

	public Location getCurrentloc() {
		return new Location(world.getWorld(), locX, locY, locZ);
	}

	public void lookatLocation(Location loc) {
		setYaw(getLocalAngle(new Vector(locX, 0, locZ), loc.toVector()));

		double xDiff, yDiff, zDiff;
		xDiff = loc.getX() - getCurrentloc().getX();
		yDiff = loc.getY() - getCurrentloc().getY();
		zDiff = loc.getZ() - getCurrentloc().getZ();

		double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

		pitch = (float) (Math.toDegrees(Math.acos(yDiff / distanceY)) - 90);
	}

	public void lookatEntity(org.bukkit.entity.Entity ent) {
		lookatLocation(ent.getLocation());
	}

	public void playerAnimation(NPCAnimation animation) {
		broadcastLocalPacket(new PacketPlayOutAnimation(this, animation.getId()));
	}

	public boolean pathFinder(Location loc, double speed) {
		return pathFinder(loc, speed, 30);
	}

	public boolean pathFinder(Location loc, double speed, double range) {
		NPCPath path = NPCPath.find(this, loc, range, speed);
		if ((this.path = path) != null) {
			lookatLocation(loc);
			new Move(path).runTaskTimer(plugin, 0, (long) 1.5);
			return true;
		}
		return false;

	}

	public Pathfinder getPathfinder() {
		return pathfinder;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
		this.aK = yaw;
		this.aI = yaw;
		this.aL = yaw;
	}

	private final float getLocalAngle(Vector point1, Vector point2) {
		double dx = point2.getX() - point1.getX();
		double dz = point2.getZ() - point1.getZ();
		float angle = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
		if (angle < 0) {
			angle += 360.0F;
		}
		return angle;
	}

	@SuppressWarnings("rawtypes")
	private final void broadcastLocalPacket(Packet packet) {
		for (Player p : getBukkitEntity().getWorld().getPlayers()) {
			if (getBukkitEntity().getLocation()
					.distanceSquared(p.getLocation()) <= RADIUS * RADIUS) {
				((CraftPlayer) p).getHandle().playerConnection
						.sendPacket(packet);
			}
		}
	}

	private void sendPacketsToListed(Iterable<? extends Player> recipients,
			boolean spawn) {
		for (Player players : recipients) {
			playerJoinPacket(players, spawn);
		}
	}

	@SuppressWarnings("rawtypes")
	private void sendPacketsToPlayer(final EntityPlayer player, boolean spawn,
			Packet... packets) {
		if (player != null) {
			for (Packet p : packets) {
				if (p == null) {
					continue;
				}
				player.playerConnection.sendPacket(p);
				if (spawn) {
					final NPCEntity npc = this;
					new BukkitRunnable() {

						@Override
						public void run() {
							player.playerConnection
									.sendPacket(new PacketPlayOutPlayerInfo(
											EnumPlayerInfoAction.REMOVE_PLAYER,
											npc));
						}
					}.runTaskLater(plugin, 10L);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void playerJoinPacket(Player player, boolean spawn) {
		Packet[] p;
		if (spawn) {
			p = new Packet[] {
					new PacketPlayOutPlayerInfo(
							EnumPlayerInfoAction.ADD_PLAYER, this),
					new PacketPlayOutNamedEntitySpawn(this) };
		} else {
			p = new Packet[] {
					new PacketPlayOutPlayerInfo(
							EnumPlayerInfoAction.REMOVE_PLAYER, this),
					new PacketPlayOutEntityDestroy(getId()) };
		}
		sendPacketsToPlayer(((CraftPlayer) player).getHandle(), spawn, p);
	}

	public static Entity getHandle(org.bukkit.entity.Entity bukkitEntity) {
		if (!(bukkitEntity instanceof CraftEntity))
			return null;
		return ((CraftEntity) bukkitEntity).getHandle();
	}

	@Override
	public String getSkinName() {
		return this.skin;
	}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public void setName(String name) {
		Location old = getCurrentloc();
		GameProfile profile = new GameProfile(getProfile().getId(), name);
		NPCProfile prof;
		if (getProfile().getProperties().get("textures") != null) {
			profile.getProperties()
					.put("textures",
							Iterables.getFirst(getProfile().getProperties()
									.get("textures"), null));
			prof = NPCProfile.presetProfile(profile, skin);
		} else {
			prof = NPCProfile.loadProfile(name, skin, getWorld().getWorld()
					.getHandle(), plugin);
		}
		despawn(NPCSpawnReason.RESPAWN);
		NPCEntity npc = new NPCEntity(old.getWorld(), old, prof,
				plugin.getNetwork(), plugin, NPCSpawnReason.RESPAWN);
		npc.spawn(old, NPCSpawnReason.RESPAWN);
	}

	@Override
	public void setSkin(String name) {
		setSkin(NPCProfile.loadSkin(name, getUniqueId(), plugin), name);
	}

	@Override
	public void setSkin(Property prop, String skin) {
		if (prop != null) {
			getProfile().getProperties().removeAll("textures");
			getProfile().getProperties().put("textures", prop);
			Location old = getCurrentloc();
			despawn(NPCSpawnReason.RESPAWN);
			NPCEntity npc = new NPCEntity(old.getWorld(), old,
					NPCProfile.presetProfile(getProfile(), skin),
					plugin.getNetwork(), plugin, NPCSpawnReason.RESPAWN);
			npc.spawn(old, NPCSpawnReason.RESPAWN);
		}

	}

	@Override
	public void spawn(Location location, NPCSpawnReason reason) {
		World world = location.getWorld();
		WorldServer worldServer = ((CraftWorld) world).getHandle();
		worldServer.addEntity(this, SpawnReason.CUSTOM);
		setPosition(location.getX(), location.getY(), location.getZ());
		sendPacketsToListed(Bukkit.getOnlinePlayers(), true);
		this.spawned = true;
		Bukkit.getPluginManager().callEvent(
				new NPCSpawnEvent(this, location, reason));
		if (reason != NPCSpawnReason.SHOP_SPAWN) {
			plugin.qserver.resetHear(getUniqueId());
		} else {
			plugin.qserver.npchear(getUniqueId());
		}
	}

	@Override
	public void despawn(NPCSpawnReason reason) {
		this.spawned = false;
		plugin.qserver.canceltasks(getUniqueId());
		sendPacketsToListed(Bukkit.getOnlinePlayers(), false);
		if (reason == NPCSpawnReason.DESPAWN) {
			Bukkit.getPluginManager().callEvent(new NPCDespawnEvent(this));
		}
		this.getWorld().getWorld().getHandle().getTracker().untrackEntity(this);
		this.getWorld().removeEntity(this);
	}

	@Override
	public void respawn() {
		Location respawnloc = getCurrentloc();
		this.spawned = false;
		NPCEntity npc = this;
		despawn(NPCSpawnReason.RESPAWN);
		npc.spawn(respawnloc, NPCSpawnReason.RESPAWN);
	}

	private static class Move extends BukkitRunnable {
		private NPCPath path;

		public Move(NPCPath path) {
			this.path = path;
		}

		@Override
		public void run() {
			if (!path.update()) {
				cancel();
			}
		}

	}
	public void addNode(int id){
		this.speechNodes.add(id);
	}
	public int getNode(int index){
		return speechNodes.get(index);
	}
	public List<Integer> getNodes(){
		return speechNodes;
	}
	public void removeNodeIndex(int index){
		speechNodes.remove(index);
	}
	public void removeNodeID(int id){
		for(int i = 0; i < speechNodes.size(); i++){
			if(speechNodes.get(i) == id){
				removeNodeIndex(i);
			}
		}
	}
}
