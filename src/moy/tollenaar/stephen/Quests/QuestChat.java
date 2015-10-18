package moy.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;





import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.NPC.NPC;
import moy.tollenaar.stephen.NPC.NPCHandler;
import moy.tollenaar.stephen.Travel.HarborWaitLocations;
import moy.tollenaar.stephen.Travel.Travel;
import moy.tollenaar.stephen.Travel.TripLocations;

@SuppressWarnings("deprecation")
public class QuestChat extends QuestInvClick implements Listener {

	private QuestChatEvent e;
	private Travel travel;
	private MoY plugin;
	
	public QuestChat(MoY instance) {
		super(instance);
		this.plugin = instance;
		this.e = new QuestChatEvent(instance);
		this.travel = instance.tr;
	}

	@EventHandler
	public void onChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		if (questers.npcpos.get(player.getUniqueId()) != null) {
			String typed = event.getMessage();
			ArrayList<String> info = questers.npcpos.get(player.getUniqueId());
			String type = info.get(0);
			if (info.get(0).equals("1")) {
				type = "killquest";
			} else if (info.get(0).equals("2")) {
				type = "harvestquest";
			} else if (info.get(0).equals("3")) {
				type = "talktoquest";
			} else if (info.get(0).equals("4")) {
				type = "warplists";
			} else if (info.get(0).equals("5")) {
				type = "trip";
			} else if (info.get(0).equals("6")) {
				type = "harbor";
			} else if (info.get(0).equals("7")) {
				type = "eventquest";
			}
			if(!type.equals("harbor") && !type.equals("trip") && !type.equals("eventquest")){
			UUID npcuuid = UUID.fromString(info.get(1));
			if (type.equals("Main") && typed.length() < 16) {
				NPCHandler handler = plugin.getNPCHandler();
				NPC npc = handler.getNPCByUUID(npcuuid);
				
				npc.setName(typed);
				
				event.setCancelled(true);
				questers.npcpos.remove(player.getUniqueId());
			} else if (type.equals("Skin") && typed.length() < 16) {
				NPCHandler handler = plugin.getNPCHandler();
				NPC npc = handler.getNPCByUUID(npcuuid);
				npc.setSkin(typed);
				
				event.setCancelled(true);
				questers.npcpos.remove(player.getUniqueId());
			} else if (type.equals("killquest")) {
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
					if (typed.contains("add")) {
						typed = typed.replace("add", "");
						kill.setReward(typed);
					} else {
						try {

							int line = Integer.parseInt(Character
									.toString(typed.charAt(0)));
							typed = typed.replaceFirst(
									Character.toString(typed.charAt(0)), "");
							if (line >= 0) {
								List<String> rew = kill.getReward();
								if (rew.size() - 1 <= line) {
									rew.set(line, typed);
									kill.setReward(rew);
									pass = true;
								} else {
									pass = false;
								}
							} else {
								pass = false;
							}
						} catch (NumberFormatException ex) {
							pass = false;
						}
					}
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
						} else {
							player.sendMessage("Choose out killquest/harvestquest/talktoquest");
						}
					} else if (typeda[0].equals("none")) {
						String pre = typeda[0] + "=0";
						kill.setPrereq(pre);
						pass = true;
					}
					break;
				case "state":
					if(typed.equalsIgnoreCase("true")){
						kill.setState("active");
						pass = true;
					}else if(typed.equalsIgnoreCase("false")){
						kill.setState("disabled");
						pass = true;
					}else{
						player.sendMessage("type true or false");
					}
					break;
				}

				event.setCancelled(true);
				if (pass) {
					questers.npcpos.remove(player.getUniqueId());

					kill.npcsettingskill(npcuuid, player);
				}
			} else if (type.equals("harvestquest")) {
				boolean pass = false;
				int questnumber = Integer.parseInt(info.get(2));
				QuestHarvest kill = questers.returnharvest(questnumber);
				switch (info.get(info.size() - 1)) {
				case "title":
					kill.setName(typed);
					pass = true;
					break;
				case "item":
					try {
						String[] tp = typed.split(" ");
						String item = tp[0];
						if (tp.length == 1) {
							if (Material.getMaterial(Integer.parseInt(tp[0]
									.trim())) == null) {
								throw new Exception();
							}
						} else if (tp.length == 2) {

							if (Material.getMaterial(Integer.parseInt(tp[0]
									.trim())) == null) {
								throw new Exception();
							} else {
								Short.parseShort(tp[1].trim());
							}
						} else {
							throw new Exception();
						}

						kill.setItem(item);

						pass = true;
					} catch (Exception ex) {
						player.sendMessage("item not found, type again");
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
					if (typed.contains("add")) {
						typed = typed.replace("add", "");
						kill.setReward(typed);
						pass = true;
					} else {
						try {

							int line = Integer.parseInt(Character
									.toString(typed.charAt(0)));
							typed = typed.replaceFirst(
									Character.toString(typed.charAt(0)), "");
							if (line >= 0) {
								List<String> rew = kill.getReward();
								if (rew.size() - 1 <= line) {
									rew.set(line, typed);
									kill.setReward(rew);
									pass = true;
								} else {
									pass = false;
								}
							} else {
								pass = false;
							}
						} catch (NumberFormatException ex) {
							pass = false;
							;
						}
					}
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
						} else {
							player.sendMessage("Choose out killquest/harvestquest/talktoquest");
						}
					} else if (typeda[0].equals("none")) {
						String pre = typeda[0] + "=0";
						kill.setPrereq(pre);
						pass = true;
					}
					break;
				case "state":
					if(typed.equalsIgnoreCase("true")){
						kill.setState("active");
						pass = true;
					}else if(typed.equalsIgnoreCase("false")){
						kill.setState("disabled");
						pass = true;
					}else{
						player.sendMessage("type true or false");
					}
					break;
				}
				event.setCancelled(true);
				if (pass) {
					questers.npcpos.remove(player.getUniqueId());

					kill.qinventory(player, npcuuid);
				}

			} else if (type.equals("talktoquest")) {
				int questnumber = Integer.parseInt(info.get(2));
				boolean pass = false;
				QuestTalkto talk = questers.returntalkto(questnumber);
				switch (info.get(info.size() - 1)) {
				case "title":
					talk.setName(typed);
					pass = true;
					break;
				case "person":
					NPCHandler handler = plugin.getNPCHandler();
					ArrayList<NPC> allpossible = new ArrayList<NPC>();
					HashMap<NPC, Integer> revervse = new HashMap<NPC, Integer>();
					for (Integer id : questers.uniquenpcid.keySet()) {
						NPC npc = handler.getNPCByUUID(questers.uniquenpcid.get(id));
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
							lore.add("X: " + npc.getCurrentloc().getX());
							lore.add("Y: " + npc.getCurrentloc().getY());
							lore.add("Z: " + npc.getCurrentloc().getZ());
							lore.add("World: "
									+ npc.getCurrentloc().getWorld()
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
					inv.setItem(inv.getSize() - 1, dummy);

					player.openInventory(inv);
					pass = true;
					break;
				case "reward":
					if (typed.contains("add")) {
						typed = typed.replace("add", "");
						talk.setReward(typed);
						pass = true;
					} else {
						try {

							int line = Integer.parseInt(Character
									.toString(typed.charAt(0)));
							typed = typed.replaceFirst(
									Character.toString(typed.charAt(0)), "");
							if (line >= 0) {
								List<String> rew = talk.getReward();
								if (rew.size() - 1 <= line) {
									rew.set(line, typed);
									talk.setReward(rew);
									pass = true;
								} else {
									pass = false;;
								}
							} else {
								pass = false;;
							}
						} catch (NumberFormatException ex) {
							pass = false;;
						}
					}
					break;
				case "repeat":
					talk.setDelay(typed);
					pass = true;
					break;
				case "message":
					talk.setMessage(typed);
					pass = true;
					break;
				case "minimum":
					try {
						talk.setMinlvl(Integer.parseInt(typed));
						pass = true;
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
								pass = true;
							} catch (NumberFormatException ex) {
								player.sendMessage("the second word wasn't a number");
							}
						} else {
							player.sendMessage("Choose out killquest/harvestquest/talktoquest");
						}
					} else if (typeda[0].equals("none")) {
						String pre = typeda[0] + "=0";
						talk.setPrereq(pre);
						pass = true;
					}
					break;
					
				case "state":
					if(typed.equalsIgnoreCase("true")){
						talk.setState("active");
						pass = true;
					}else if(typed.equalsIgnoreCase("false")){
						talk.setState("disabled");
						pass = true;
					}else{
						player.sendMessage("type true or false");
					}
					break;
				}
				event.setCancelled(true);
				if (pass) {
					questers.npcpos.remove(player.getUniqueId());

					talk.npcsettingstalkto(npcuuid, player);
				}

			} else if (type.equals("warplists")) {
				System.out.println(info);
				int number = Integer.parseInt(info.get(2));
				Warps warp = questers.returnwarp(number);
				boolean pass = false;
				switch (info.get(info.size() - 1)) {
				case "title":
					warp.setName(typed);
					pass = true;
					break;
				case "type":
					String[] splitted = typed.split(" ");
					if(splitted[0].equals("add")){
						String build = "";
						for(int i = 1; i < splitted.length; i++){
							build += splitted[i] + " ";
						}
						warp.AddLine(build.trim().toLowerCase());
						pass =true;
					}else{
						try{
							int line = Integer.parseInt(splitted[0]);
							String build = "";
							for(int i = 1;i < splitted.length; i++){
								build += splitted[i] + " ";
							}
							warp.SetLine(line, build.trim().toLowerCase());
							pass = true;
						}catch(NumberFormatException e){
							pass = false;
						}
					}
					break;
				case "cost":
					try {
						warp.setCosts(Integer.parseInt(typed));
						pass = true;
					} catch (NumberFormatException ex) {
						player.sendMessage("this was not a number try again");
					}
					break;
					
				case "state":
					if(typed.equalsIgnoreCase("true")){
						warp.setState("active");
						pass = true;
					}else if(typed.equalsIgnoreCase("false")){
						warp.setState("disabled");
						pass = true;
					}else{
						player.sendMessage("type true or false");
					}
					break;
				}
				if (pass) {
					questers.npcpos.remove(player.getUniqueId());
				}
				event.setCancelled(true);
			}
			} else if (type.equals("trip")) {
				if (info.get(1).equals("type")) {
					if (typed.equals("boat") || typed.equals("oxcart")
							|| typed.equals("dragon")) {
						int id = Integer.parseInt(info.get(info.size() - 1));
						TripLocations t = travel.GetTrip(id);
						String old = t.getType();
						t.setType(typed);
						travel.TypeSwitchTrip(t, old, typed, id);
						questers.npcpos.remove(player.getUniqueId());
						event.setCancelled(true);
					}
				} else if (info.get(1).equals("location")) {
					if (typed.equals("save")) {
						int id = Integer.parseInt(info.get(info.size() - 1));
						TripLocations t = travel.GetTrip(id);
						t.setLocation(player.getLocation());
						questers.npcpos.remove(player.getUniqueId());
						event.setCancelled(true);
					}
				}
			} else if (type.equals("harbor")) {
				if (info.get(1).equals("type")) {
					if (typed.equals("boat") || typed.equals("oxcart")
							|| typed.equals("dragon")) {
						int id = Integer.parseInt(info.get(info.size() - 1));
						HarborWaitLocations t = travel.GetHarbor(id);
						String old = t.getType();
						t.setType(typed);
						travel.TypeSwitchHarbor(t, old, typed, id);
						questers.npcpos.remove(player.getUniqueId());
						event.setCancelled(true);
					}
				} else if (info.get(1).equals("location")) {
					if (typed.equals("save")) {
						int id = Integer.parseInt(info.get(info.size() - 1));
						HarborWaitLocations t = travel.GetHarbor(id);
						t.setLocation(player.getLocation());
						questers.npcpos.remove(player.getUniqueId());
						event.setCancelled(true);
					}
				}
			} else if (type.equals("eventquest")) {
				if (!e.InfoSet(info, event.getMessage(), player)) {
					event.setCancelled(true);
					player.sendMessage("something happend try again");
				} else {
					event.setCancelled(true);
				}
			}
		}
	}
}
