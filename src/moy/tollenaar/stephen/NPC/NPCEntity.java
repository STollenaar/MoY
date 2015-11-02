package moy.tollenaar.stephen.NPC;

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

import com.google.common.base.Function;
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
	private final String shop;
	private String skin;
	private UUID uuid;
	private boolean spawned;
	private final int id;

	
	
	
	public boolean isSpawned() {
		return spawned;
	}

	public NPCEntity(World world, Location location, NPCProfile profile,
			NPCNetworkManager networkManager, MoY plugin, String shop, int id) {
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
		this.shop = shop;
		this.id = id;
		this.playerInteractManager.b(EnumGamemode.CREATIVE);
	}

	public NPCEntity(World world, Location location, NPCProfile profile,
			NPCNetworkManager networkManager, MoY plugin,
			NPCSpawnReason reason, String shop, int id) {
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
		this.shop = shop;
		this.id = id;
		if (reason != NPCSpawnReason.RESPAWN) {
			spawn(location, reason, this);
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

	@SuppressWarnings("rawtypes")
	private void sendPacketsToListed(Iterable<? extends Player> recipients,
			Packet... packets) {
			for(Player players : recipients){
				try{
				EntityPlayer nmsplayer = ((CraftPlayer)players).getHandle();
				sendPacketsToPlayer(nmsplayer, packets);
				}catch(ClassCastException ex){
					continue;
				}
			}
	}

	@SuppressWarnings("rawtypes")
	private void sendPacketsToPlayer(EntityPlayer player, Packet... packets) {
		if (player != null) {
			for (Packet p : packets) {
				if (p == null) {
					continue;
				}
				player.playerConnection.sendPacket(p);
			}
		}
	}

	public void playerJoinPacket(Player player) {
		sendPacketsToPlayer(((CraftPlayer)player).getHandle(), new PacketPlayOutPlayerInfo(
				EnumPlayerInfoAction.ADD_PLAYER, this), new PacketPlayOutNamedEntitySpawn(this), new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this));
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
					.getHandle());
		}
		despawn(NPCSpawnReason.RESPAWN);

		spawn(old, NPCSpawnReason.RESPAWN,
				new NPCEntity(old.getWorld(), old, prof, plugin.getNetwork(),
						plugin, NPCSpawnReason.RESPAWN, shop, id));
	}

	@Override
	public void setSkin(String name) {
		setSkin(NPCProfile.loadSkin(name, getWorld().getWorld().getHandle(),
				plugin, getName(), getProfile()), name);
	}

	@Override
	public void setSkin(Property prop, String skin) {
		if(prop != null){
		getProfile().getProperties().removeAll("textures");
		getProfile().getProperties().put("textures", prop);
		Location old = getCurrentloc();
		despawn(NPCSpawnReason.RESPAWN);

		spawn(old,
				NPCSpawnReason.RESPAWN,
				new NPCEntity(old.getWorld(), old, NPCProfile.presetProfile(
						getProfile(), skin), plugin.getNetwork(), plugin,
						NPCSpawnReason.RESPAWN, shop, id));
		}
	}
	
	
	@Override
	public void spawn(Location location, NPCSpawnReason reason, NPCEntity npc) {
		World world = location.getWorld();
		sendPacketsToListed(Bukkit.getOnlinePlayers(), new PacketPlayOutPlayerInfo(
				EnumPlayerInfoAction.ADD_PLAYER, npc), new PacketPlayOutNamedEntitySpawn(npc), new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
		WorldServer worldServer = ((CraftWorld) world).getHandle();

		npc.setPosition(location.getX(), location.getY(),
				location.getZ());
		worldServer.addEntity(npc, SpawnReason.CUSTOM);
		sendPacketsToListed(Bukkit.getOnlinePlayers(), new PacketPlayOutPlayerInfo(
				EnumPlayerInfoAction.ADD_PLAYER, npc), new PacketPlayOutNamedEntitySpawn(npc), new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
		this.spawned = true;
		Bukkit.getPluginManager().callEvent(
				new NPCSpawnEvent(npc, location, reason, shop, id));
		if(reason != NPCSpawnReason.SHOP_SPAWN){
		plugin.qserver.resetHear(getUniqueId());
		}else{
			plugin.qserver.npchear(getUniqueId());
		}
	}

	@Override
	public void despawn(NPCSpawnReason reason) {
		this.spawned = false;
		plugin.qserver.canceltasks(getUniqueId());
		sendPacketsToListed(Bukkit.getOnlinePlayers(), new PacketPlayOutEntityDestroy(this.getId()));
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
		spawn(respawnloc, NPCSpawnReason.RESPAWN, npc);
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
}
