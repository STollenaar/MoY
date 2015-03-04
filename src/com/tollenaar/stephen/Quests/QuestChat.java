package com.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tollenaar.stephen.MistsOfYsir.MoY;
import com.tollenaar.stephen.Travel.HarborWaitLocations;
import com.tollenaar.stephen.Travel.TripLocations;

@SuppressWarnings("deprecation")
public class QuestChat extends QuestInvClick implements Listener {

	public QuestChat(MoY instance) {
		super(instance);
	}

	@EventHandler
	public void onChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		if (questers.npcpos.get(player.getUniqueId()) != null) {
			String typed = event.getMessage();
			ArrayList<String> info = questers.npcpos.get(player.getUniqueId());
			UUID npcuuid = UUID.fromString(info.get(1));
			if (info.get(0).equals("Main")) {
				NPCRegistry registry = CitizensAPI.getNPCRegistry();
				NPC npc = registry.getByUniqueId(npcuuid);
				final Location loc = npc.getStoredLocation();
				npc.setName(typed);
				npc.despawn();
				npc.spawn(loc);
				event.setCancelled(true);
				questers.npcpos.remove(player.getUniqueId());
			} else if(info.get(0).equals("Skin")){
				NPCRegistry registry = CitizensAPI.getNPCRegistry();
				NPC npc = registry.getByUniqueId(npcuuid);
				final Location loc = npc.getStoredLocation();
				npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, typed);
				npc.despawn();
				npc.spawn(loc);
				event.setCancelled(true);
				questers.npcpos.remove(player.getUniqueId());
			}		else if (info.get(0).equals("killquest")) {
				boolean pass = false;
				int questnumber = Integer.parseInt(info.get(2));
				QuestKill kill = questers.returnkill(questnumber);
				switch (info.get(info.size() - 1)) {
				case "title":
					kill.setName(typed);
					pass = true;
					
					break;
				case "mob":
					try {
						EntityType.valueOf(typed.toUpperCase());
						kill.setMonster(typed);
						pass = true;
					} catch (Exception ex) {
						player.sendMessage("Monster not found try again");
					}
					break;
				case "count":
					try {
						kill.setCount(Integer.parseInt(typed));
						pass = true;
					} catch (NumberFormatException ex) {
						player.sendMessage("This was not a number try again");
					}
					break;
				case "reward":
					kill.setReward(typed);
					pass = true;
					break;
				case "repeat":
					kill.setDelay(typed);
					pass = true;
					break;
				case "minimum":
					try {
						kill.setMinlvl(Integer.parseInt(typed));
						pass = true;
					} catch (NumberFormatException ex) {
						player.sendMessage("This was not a number try again");
					}
					break;
				case "message":
					kill.setMessage(typed);
					pass = true;
					break;
				case "prereq":
					String[] typeda = typed.split(" ");
					if (typeda.length == 2) {
						if (typeda[0].equals("harvestquest")
								|| typeda[0].equals("killquest")
								|| typeda[0].equals("talktoquest")) {
							try {
								typeda[0] = typeda[0].replace("quest", "");
								Integer.parseInt(typeda[1]);
								String pre = typeda[0] + "=" + typeda[1];
								kill.setPrereq(pre);
								pass = true;
							} catch (NumberFormatException ex) {
								player.sendMessage("the second word wasn't a number");
							}
						}else{
							player.sendMessage("Choose out killquest/harvestquest/talktoquest");
						}
					} else if (typeda[0].equals("none")) {
						String pre = typeda[0] + "=0";
						kill.setPrereq(pre);
						pass = true;
					}
					break;
				}
				
				event.setCancelled(true);
				if(pass){
					questers.npcpos.remove(player.getUniqueId());
					
					kill.npcsettingskill(npcuuid, player);
				}
			} else if (info.get(0).equals("harvestquest")) {
				int questnumber = Integer.parseInt(info.get(2));
				QuestHarvest kill = questers.returnharvest(questnumber);
				switch (info.get(info.size() - 1)) {
				case "title":
					kill.setName(typed);
					questers.npcpos.remove(player.getUniqueId());
					event.setCancelled(true);
					break;
				case "item":
					try {
						String[] tp = typed.split(" ");
						String item = tp[0];
						if (tp.length == 1) {
							if(questers.ids.get(tp[0]) == null){
								throw new Exception();
							}
						} else if (tp.length == 2) {
						item += ":" + tp[1];
						if(questers.ids.get(item) == null){
							throw new Exception();
						}
						} else {
							throw new Exception();
						}
						
						kill.setItem(item);
						questers.npcpos.remove(player.getUniqueId());
						
						event.setCancelled(true);
					} catch (Exception ex) {
						player.sendMessage("item not found, type again");
					}
					break;
				case "count":
					try {
						kill.setCount(Integer.parseInt(typed));
						questers.npcpos.remove(player.getUniqueId());
						event.setCancelled(true);
					} catch (NumberFormatException ex) {
						player.sendMessage("This was not a number try again");
					}
					break;
				case "reward":
					kill.setReward(typed);
					questers.npcpos.remove(player.getUniqueId());
					event.setCancelled(true);
					break;
				case "repeat":
					kill.setDelay(typed);
					questers.npcpos.remove(player.getUniqueId());
					event.setCancelled(true);
					break;
				case "minimum":
					try {
						kill.setMinlvl(Integer.parseInt(typed));
						questers.npcpos.remove(player.getUniqueId());
						event.setCancelled(true);
					} catch (NumberFormatException ex) {
						player.sendMessage("This was not a number try again");
					}
					break;
				case "message":
					kill.setMessage(typed);
					questers.npcpos.remove(player.getUniqueId());
					event.setCancelled(true);
					break;
				case "prereq":
					String[] typeda = typed.split(" ");
					if (typeda.length == 2) {
						if (typeda[0].equals("harvestquest")
								|| typeda[0].equals("killquest")
								|| typeda[0].equals("talktoquest")) {
							try {
								typeda[0] = typeda[0].replace("quest", "");
								Integer.parseInt(typeda[1]);
								String pre = typeda[0] + "=" + typeda[1];
								kill.setPrereq(pre);
								questers.npcpos.remove(player.getUniqueId());
								event.setCancelled(true);
							} catch (NumberFormatException ex) {
								player.sendMessage("the second word wasn't a number");
							}
						}else{
							player.sendMessage("Choose out killquest/harvestquest/talktoquest");
						}
					} else if (typeda[0].equals("none")) {
						String pre = typeda[0] + "=0";
						kill.setPrereq(pre);
						questers.npcpos.remove(player.getUniqueId());
						event.setCancelled(true);
					}
					break;
				}
			} else if (info.get(0).equals("talktoquest")) {
				int questnumber = Integer.parseInt(info.get(2));
				QuestTalkto talk = questers.returntalkto(questnumber);
				switch (info.get(info.size() - 1)) {
				case "title":
					talk.setName(typed);
					questers.npcpos.remove(player.getUniqueId());
					event.setCancelled(true);
					break;
				case "person":
					NPCRegistry registry = CitizensAPI.getNPCRegistry();
					ArrayList<NPC> allpossible = new ArrayList<NPC>();
					HashMap<NPC, Integer> revervse = new HashMap<NPC, Integer>();
					for (Integer id : questers.uniquenpcid.keySet()) {
						NPC npc = registry.getByUniqueId(questers.uniquenpcid.get(id));
						if (npc.getName().equalsIgnoreCase(typed)) {
							allpossible.add(npc);
							revervse.put(npc, id);
						}
					}

					int rowcount = 0;
					if (allpossible != null) {
						rowcount = allpossible.size();
						if (rowcount % 9 == 0) {
							rowcount++;
						}
						while (rowcount % 9 != 0) {
							rowcount++;
						}
					}
					if (rowcount == 0) {
						rowcount = 9;
					}
					Inventory inv = Bukkit.createInventory(null, rowcount,
							"NPCNames");
					for (NPC npc : allpossible) {
						ItemStack item = new ItemStack(Material.SKULL_ITEM);
						{
							ArrayList<String> lore = new ArrayList<String>();
							lore.add(npc.getUniqueId().toString());
							lore.add(Integer.toString(revervse.get(npc)));
							lore.add("X: " + npc.getStoredLocation().getX());
							lore.add("Y: " + npc.getStoredLocation().getY());
							lore.add("Z: " + npc.getStoredLocation().getZ());
							lore.add("World: "
									+ npc.getStoredLocation().getWorld()
											.getName());
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName(npc.getName());
							meta.setLore(lore);
							item.setItemMeta(meta);
						}
						inv.addItem(item);
					}
					ItemStack dummy = new ItemStack(Material.BEDROCK);
					{
						ArrayList<String> lore = new ArrayList<String>();
						lore.add(npcuuid.toString());
						lore.add(Integer.toString(questnumber));
						ItemMeta meta = dummy.getItemMeta();
						meta.setDisplayName("NPC Info");
						meta.setLore(lore);
						dummy.setItemMeta(meta);
					}
					inv.setItem(inv.getSize()-1, dummy);
					
					player.openInventory(inv);
					questers.npcpos.remove(player.getUniqueId());
					event.setCancelled(true);
					break;
				case "reward":
					talk.setReward(typed);
					questers.npcpos.remove(player.getUniqueId());
					event.setCancelled(true);
					break;
				case "repeat":
					talk.setDelay(typed);
					questers.npcpos.remove(player.getUniqueId());
					event.setCancelled(true);
					break;
				case "message":
					talk.setMessage(typed);
					questers.npcpos.remove(player.getUniqueId());
					event.setCancelled(true);
					break;
				case "minimum":
					try {
						talk.setMinlvl(Integer.parseInt(typed));
						questers.npcpos.remove(player.getUniqueId());
						event.setCancelled(true);
					} catch (NumberFormatException ex) {
						player.sendMessage("this was not a nummber try again");
					}
					break;
				case "prereq":
					String[] typeda = typed.split(" ");
					if (typeda.length == 2) {
						if (typeda[0].equals("harvestquest")
								|| typeda[0].equals("killquest")
								|| typeda[0].equals("talktoquest")) {
							try {
								typeda[0] = typeda[0].replace("quest", "");
								Integer.parseInt(typeda[1]);
								String pre = typeda[0] + "=" + typeda[1];
								talk.setPrereq(pre);
								questers.npcpos.remove(player.getUniqueId());
								event.setCancelled(true);
							} catch (NumberFormatException ex) {
								player.sendMessage("the second word wasn't a number");
							}
						}else{
							player.sendMessage("Choose out killquest/harvestquest/talktoquest");
						}
					} else if (typeda[0].equals("none")) {
						String pre = typeda[0] + "=0";
						talk.setPrereq(pre);
						questers.npcpos.remove(player.getUniqueId());
						event.setCancelled(true);
					}					break;
				}
			} else if (info.get(0).equals("warplists")) {
				int number = Integer.parseInt(info.get(2));
				Warps warp = questers.returnwarp(number);
				boolean pass = false;
				switch (info.get(info.size() - 1)) {
				case "title":
					warp.setName(typed);
					pass = true;
					break;
				case "type":
					warp.SetType(typed);
					pass = true;
					break;
				case "cost":
					try {
						warp.setCosts(Integer.parseInt(typed));
						pass = true;
					} catch (NumberFormatException ex) {
						player.sendMessage("this was not a number try again");
					}
					break;
				}
				if (pass) {
					questers.npcpos.remove(player.getUniqueId());
				}
				event.setCancelled(true);
			}else if(info.get(0).equals("trip")){
				if(info.get(1).equals("type")){
					if(typed.equals("boat") || typed.equals("oxcart") || typed.equals("dragon")){
						int id = Integer.parseInt(info.get(info.size()-1));
						TripLocations t = questers.returntrip(id);
						t.setType(typed);
						questers.npcpos.remove(player.getUniqueId());
					}
				}else if(info.get(1).equals("location")){
					if(typed.equals("save")){
						int id = Integer.parseInt(info.get(info.size()-1));
						TripLocations t = questers.returntrip(id);
						t.setLocation(player.getLocation());
						questers.npcpos.remove(player.getUniqueId());
					}
				}
			}else if(info.get(0).equals("harbor")){
				if(info.get(1).equals("type")){
					if(typed.equals("boat") || typed.equals("oxcart") || typed.equals("dragon")){
						int id = Integer.parseInt(info.get(info.size()-1));
						HarborWaitLocations t = questers.returnharbor(id);
						t.setType(typed);
						questers.npcpos.remove(player.getUniqueId());
					}
				}else if(info.get(1).equals("location")){
					if(typed.equals("save")){
						int id = Integer.parseInt(info.get(info.size()-1));
						HarborWaitLocations t = questers.returnharbor(id);
						t.setLocation(player.getLocation());
						questers.npcpos.remove(player.getUniqueId());
					}
				}
			}
		}
	}
}
