package MoY.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import MoY.tollenaar.stephen.Files.Filewriters;
import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.NPC.NPC;
import MoY.tollenaar.stephen.NPC.NPCHandler;
import MoY.tollenaar.stephen.PlayerInfo.Playerinfo;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Questinteracts implements Listener {
	private Filewriters fw;
	private MoY plugin;
	private Playerinfo playerinfo;

	@EventHandler
	public void onEvent(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() != null) {
			if (event.getRightClicked() instanceof Entity) {
				Player player = event.getPlayer();
				Playerstats p = playerinfo.getplayer(player.getUniqueId());

				PermissionUser user = PermissionsEx.getUser(player);
				boolean dummy = true;
				Entity entity = event.getRightClicked();
				if (entity.hasMetadata("NPC")) {
					if (entity.getMetadata("NPC").get(0).asString()
							.equals("shop")) {
						// open shop menu
						Bukkit.dispatchCommand(player, "/shopmenu");
						return;
					}
				}
				NPCHandler handler = plugin.getNPCHandler();
				NPC npc = handler.getNPCByEntity(entity);
				if (npc != null) {

					/**
					 * important to check if player isn't completing a
					 * talktoquest
					 */
					if (p.getactivetype() != null) {
						if (p.getactives("talkto") != null) {
							for (int number : p.getactives("talkto")) {
								QuestTalkto talk = plugin.questers
										.returntalkto(number);
								if (talk != null) {
									if (plugin.questers.uniquenpcid.get(talk
											.getNpcid()) == npc.getUniqueId()) {
										for (UUID npcuuid : plugin.questers
												.GetKeysSets("talkto")) {
											if (plugin.questers.GetIds(
													"talkto", npcuuid)
													.contains(number)) {
												NPC temp = handler
														.getNPCByUUID(npcuuid);
												plugin.questers
														.AddCompletedQuest(
																player, number,
																"talkto",
																temp.getName());
												break;
											}
										}
										break;
									}
								}
							}
						}
					}
					/**
					 * regular inv open or so check
					 */
					if (player.getItemInHand() != null
							&& player.getItemInHand().getType() != Material.AIR) {
						if (player.getItemInHand().hasItemMeta()) {
							if (player.getItemInHand().getItemMeta().hasLore()) {
								if (player.getItemInHand().getItemMeta()
										.getLore()
										.contains("NPC adjustment Tool")) {
									if (user.has("Ysir.npctool")) {
										plugin.questers.npcsettingsmain(
												npc.getUniqueId(), player);
										dummy = false;
									} else {
										if (plugin.questers.GetKeysSets("kill")
												.contains(npc.getUniqueId())
												|| plugin.questers.GetKeysSets(
														"harvest").contains(
														npc.getUniqueId())
												|| plugin.questers.GetKeysSets(
														"talkto").contains(
														npc.getUniqueId())
												|| plugin.questers.GetKeysSets(
														"warp").contains(
														npc.getUniqueId())
												|| plugin.questers.GetKeysSets(
														"event").contains(
														npc.getUniqueId())) {
											plugin.qqc.avquest(npc, player,
													npc.getName());
											dummy = false;
										}
									}

								} else {
									if (plugin.questers.GetKeysSets("kill")
											.contains(npc.getUniqueId())
											|| plugin.questers.GetKeysSets(
													"harvest").contains(
													npc.getUniqueId())
											|| plugin.questers.GetKeysSets(
													"talkto").contains(
													npc.getUniqueId())
											|| plugin.questers.GetKeysSets(
													"warp").contains(
													npc.getUniqueId())
											|| plugin.questers.GetKeysSets(
													"event").contains(
													npc.getUniqueId())) {
										plugin.qqc.avquest(npc, player,
												npc.getName());
										dummy = false;
									}
								}
							} else {
								if (plugin.questers.GetKeysSets("kill")
										.contains(npc.getUniqueId())
										|| plugin.questers.GetKeysSets(
												"harvest").contains(
												npc.getUniqueId())
										|| plugin.questers
												.GetKeysSets("talkto")
												.contains(npc.getUniqueId())
										|| plugin.questers.GetKeysSets("warp")
												.contains(npc.getUniqueId())
										|| plugin.questers.GetKeysSets("event")
												.contains(npc.getUniqueId())) {
									plugin.qqc.avquest(npc, player,
											npc.getName());
									dummy = false;
								}
							}
						} else {
							if (plugin.questers.GetKeysSets("kill").contains(
									npc.getUniqueId())
									|| plugin.questers.GetKeysSets("harvest")
											.contains(npc.getUniqueId())
									|| plugin.questers.GetKeysSets("talkto")
											.contains(npc.getUniqueId())
									|| plugin.questers.GetKeysSets("warp")
											.contains(npc.getUniqueId())
									|| plugin.questers.GetKeysSets("event")
											.contains(npc.getUniqueId())) {
								plugin.qqc.avquest(npc, player, npc.getName());
								dummy = false;
							}
						}
					} else {
						if (plugin.questers.GetKeysSets("kill").contains(
								npc.getUniqueId())
								|| plugin.questers.GetKeysSets("harvest")
										.contains(npc.getUniqueId())
								|| plugin.questers.GetKeysSets("talkto")
										.contains(npc.getUniqueId())
								|| plugin.questers.GetKeysSets("warp")
										.contains(npc.getUniqueId())
								|| plugin.questers.GetKeysSets("event")
										.contains(npc.getUniqueId())) {
							plugin.qqc.avquest(npc, player, npc.getName());
							dummy = false;
						}
					}
					if (dummy) {
						dummymessage(player, npc);
					}
				}
			}
		}
	}

	protected void dummymessage(Player player, NPC npc) {
		ArrayList<String> message = fw.dummymessage();
		Random r = new Random();
		int index = r.nextInt(message.size() - 1);
		player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD
				+ npc.getName() + ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA
				+ message.get(index));

	}

	public Questinteracts(MoY instance) {
		this.fw = instance.fw;
		this.plugin = instance;
		this.playerinfo = instance.playerinfo;
	}

}
