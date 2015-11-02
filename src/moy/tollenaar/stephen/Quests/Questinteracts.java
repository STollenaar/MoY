package moy.tollenaar.stephen.Quests;

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
import org.fusesource.jansi.Ansi;

import moy.tollenaar.stephen.CEvents.QuestRewardEvent;
import moy.tollenaar.stephen.Files.Filewriters;
import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.NPC.NPC;
import moy.tollenaar.stephen.NPC.NPCHandler;
import moy.tollenaar.stephen.NPC.NPCMetadataStore;
import moy.tollenaar.stephen.NPC.NPCSpawnReason;
import moy.tollenaar.stephen.PlayerInfo.Playerinfo;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;
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
					if (entity.getMetadata("NPC").get(0) instanceof NPCMetadataStore
							&& ((NPCMetadataStore) entity.getMetadata("NPC").get(0)
									.value()).getReason() == NPCSpawnReason.SHOP_SPAWN) {
						// open shop menu
						Bukkit.dispatchCommand(player, "shopmenu");
						return;
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
									QuestTalkto talk = plugin.qserver
											.returntalkto(number);
									if (talk != null) {
										if (plugin.qserver.uniquenpcid
												.get(talk.getNpcid()) == npc
												.getUniqueId()) {
											for (UUID npcuuid : plugin.qserver
													.GetKeysSets("talkto")) {
												if (plugin.qserver.GetIds(
														"talkto", npcuuid)
														.contains(number)) {
													NPC temp = handler
															.getNPCByUUID(npcuuid);
													plugin.qserver
															.AddCompletedQuest(
																	player,
																	number,
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
								if (player.getItemInHand().getItemMeta()
										.hasLore()) {
									if (player.getItemInHand().getItemMeta()
											.getLore()
											.contains("NPC adjustment Tool")) {
										if (user.has("Ysir.npctool")) {
											plugin.qserver.npcsettingsmain(
													npc.getUniqueId(), player);
											dummy = false;
										} else {
											if (plugin.qserver.GetKeysSets(
													"kill").contains(
													npc.getUniqueId())
													|| plugin.qserver
															.GetKeysSets(
																	"harvest")
															.contains(
																	npc.getUniqueId())
													|| plugin.qserver
															.GetKeysSets(
																	"talkto")
															.contains(
																	npc.getUniqueId())
													|| plugin.qserver
															.GetKeysSets("warp")
															.contains(
																	npc.getUniqueId())
													|| plugin.qserver
															.GetKeysSets(
																	"event")
															.contains(
																	npc.getUniqueId())) {
												plugin.qclient.avquest(npc, player,
														npc.getName());
												dummy = false;
											}
										}

									} else {
										if (plugin.qserver.GetKeysSets("kill")
												.contains(npc.getUniqueId())
												|| plugin.qserver.GetKeysSets(
														"harvest").contains(
														npc.getUniqueId())
												|| plugin.qserver.GetKeysSets(
														"talkto").contains(
														npc.getUniqueId())
												|| plugin.qserver.GetKeysSets(
														"warp").contains(
														npc.getUniqueId())
												|| plugin.qserver.GetKeysSets(
														"event").contains(
														npc.getUniqueId())) {
											plugin.qclient.avquest(npc, player,
													npc.getName());
											dummy = false;
										}
									}
								} else {
									if (plugin.qserver.GetKeysSets("kill")
											.contains(npc.getUniqueId())
											|| plugin.qserver.GetKeysSets(
													"harvest").contains(
													npc.getUniqueId())
											|| plugin.qserver.GetKeysSets(
													"talkto").contains(
													npc.getUniqueId())
											|| plugin.qserver.GetKeysSets(
													"warp").contains(
													npc.getUniqueId())
											|| plugin.qserver.GetKeysSets(
													"event").contains(
													npc.getUniqueId())) {
										plugin.qclient.avquest(npc, player,
												npc.getName());
										dummy = false;
									}
								}
							} else {
								if (plugin.qserver.GetKeysSets("kill")
										.contains(npc.getUniqueId())
										|| plugin.qserver.GetKeysSets(
												"harvest").contains(
												npc.getUniqueId())
										|| plugin.qserver
												.GetKeysSets("talkto")
												.contains(npc.getUniqueId())
										|| plugin.qserver.GetKeysSets("warp")
												.contains(npc.getUniqueId())
										|| plugin.qserver.GetKeysSets("event")
												.contains(npc.getUniqueId())) {
									plugin.qclient.avquest(npc, player,
											npc.getName());
									dummy = false;
								}
							}
						} else {
							if (plugin.qserver.GetKeysSets("kill").contains(
									npc.getUniqueId())
									|| plugin.qserver.GetKeysSets("harvest")
											.contains(npc.getUniqueId())
									|| plugin.qserver.GetKeysSets("talkto")
											.contains(npc.getUniqueId())
									|| plugin.qserver.GetKeysSets("warp")
											.contains(npc.getUniqueId())
									|| plugin.qserver.GetKeysSets("event")
											.contains(npc.getUniqueId())) {
								plugin.qclient.avquest(npc, player, npc.getName());
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
	}

	protected void dummymessage(Player player, NPC npc) {
		ArrayList<String> message = fw.dummymessage();
		Random r = new Random();
		int index = r.nextInt(message.size() - 1);
		player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD
				+ npc.getName() + ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA
				+ message.get(index));

	}
	
	@EventHandler
	public void onReward(QuestRewardEvent event){
		String message = ChatColor.DARK_PURPLE +"[" + ChatColor.GOLD + event.getNpcname() + ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA + "Enjoy your reward.";
		event.getPlayer().sendMessage(message);
		System.out.println(Ansi.ansi().fg(
				Ansi.Color.RED)
				+ "QUEST COMPLETE"
				+ Ansi.ansi().fg(Ansi.Color.WHITE));
		for(String in : event.getReward()){
			System.out.println(in);
			in = in.replace("player", event.getPlayer().getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), in.trim());
		}
	}

	public Questinteracts(MoY instance) {
		this.fw = instance.fw;
		this.plugin = instance;
		this.playerinfo = instance.playerinfo;
	}

}
