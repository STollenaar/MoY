package com.tollenaar.stephen.MoYQuests;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.tollenaar.stephen.MoYMistsOfYsir.Filewriters;
import com.tollenaar.stephen.MoYMistsOfYsir.MoY;
import com.tollenaar.stephen.MoYPlayerInfo.Playerstats;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Questinteracts implements Listener {
	private Filewriters fw;
	private MoY plugin;

	@EventHandler
	public void onEvent(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() != null) {
			if (event.getRightClicked() instanceof Entity) {
				Player player = event.getPlayer();
				PermissionUser user = PermissionsEx.getUser(player);
				boolean dummy = true;
				Entity entity = event.getRightClicked();
				NPCRegistry registry = CitizensAPI.getNPCRegistry();
				NPC npc = registry.getNPC(entity);
				if (npc != null) {

					/**
					 * important to check if player isn't completing a
					 * talktoquest
					 */
					if (Playerstats.activequests.get(player.getUniqueId()) != null) {
						if (Playerstats.activequests.get(player.getUniqueId())
								.get("talkto") != null) {
							for (int number : Playerstats.activequests.get(
									player.getUniqueId()).get("talkto")) {
								QuestTalkto talk = plugin.questers
										.returntalkto(number);
								if (plugin.questers.uniquenpcid.get(talk
										.getNpcid()) == npc.getUniqueId()) {
									for (UUID npcuuid : plugin.questers.talktoquests
											.keySet()) {
										if (plugin.questers.talktoquests.get(
												npcuuid).contains(number)) {
											NPC temp = registry
													.getByUniqueId(npcuuid);
											plugin.questers.AddCompletedQuest(player,
													number, "talkto",
													temp.getName());
											break;
										}
									}
									break;
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
										if (plugin.questers.killquests
												.containsKey(npc.getUniqueId())
												|| plugin.questers.harvestquests
														.containsKey(npc
																.getUniqueId())
												|| plugin.questers.talktoquests
														.containsKey(npc
																.getUniqueId())
												|| plugin.questers.warplists
														.containsKey(npc
																.getUniqueId())) {
											plugin.qqc.avquest(
													npc, player,
													npc.getName());
											dummy = false;
										}
									}

								} else {
									if (plugin.questers.killquests
											.containsKey(npc.getUniqueId())
											|| plugin.questers.harvestquests
													.containsKey(npc
															.getUniqueId())
											|| plugin.questers.talktoquests
													.containsKey(npc
															.getUniqueId())
											|| plugin.questers.warplists
													.containsKey(npc
															.getUniqueId())) {
										plugin.qqc.avquest(npc,
												player, npc.getName());
										dummy = false;
									}
								}
							} else {
								if (plugin.questers.killquests.containsKey(npc
										.getUniqueId())
										|| plugin.questers.harvestquests
												.containsKey(npc.getUniqueId())
										|| plugin.questers.talktoquests
												.containsKey(npc.getUniqueId())
										|| plugin.questers.warplists
												.containsKey(npc.getUniqueId())) {
									plugin.qqc.avquest(npc,
											player, npc.getName());
									dummy = false;
								}
							}
						} else {
							if (plugin.questers.killquests.containsKey(npc
									.getUniqueId())
									|| plugin.questers.harvestquests
											.containsKey(npc.getUniqueId())
									|| plugin.questers.talktoquests
											.containsKey(npc.getUniqueId())
									|| plugin.questers.warplists
											.containsKey(npc.getUniqueId())) {
								plugin.qqc.avquest(npc, player,
										npc.getName());
								dummy = false;
							}
						}
					} else {
						if (plugin.questers.killquests.containsKey(npc
								.getUniqueId())
								|| plugin.questers.harvestquests
										.containsKey(npc.getUniqueId())
								|| plugin.questers.talktoquests.containsKey(npc
										.getUniqueId())
								|| plugin.questers.warplists.containsKey(npc
										.getUniqueId())) {
							plugin.qqc.avquest(npc, player,
									npc.getName());
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
	}

}
