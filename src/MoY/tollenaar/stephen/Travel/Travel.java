package MoY.tollenaar.stephen.Travel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;




import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import MoY.tollenaar.stephen.Files.Filewriters;
import MoY.tollenaar.stephen.InventoryUtils.ItemGenerator;
import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.NPC.NPC;
import MoY.tollenaar.stephen.NPC.NPCHandler;
import MoY.tollenaar.stephen.Quests.QuestsServerSide;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.google.common.collect.Lists;

public class Travel implements Listener {
	private TravelBoatEvent boat;
	private TravelCartEvent cart;
	private TravelDragonEvent dragon;
	private static MoY plugin;
	private Filewriters fw;
	private QuestsServerSide questers;

	// no db
	private HashMap<UUID, Boolean> travelevent = new HashMap<UUID, Boolean>(); // boolean
																				// if
																				// event
																				// would
																				// happen
	public static HashMap<UUID, Integer> schedulerstor = new HashMap<UUID, Integer>(); // integer
																						// for
																						// the
																						// travel
																						// scheduler
																						// to
																						// prevent
																						// null
																						// pointers

	public static HashMap<String, Set<UUID>> players = new HashMap<String, Set<UUID>>(); // player
																							// with
																							// their
																							// trip
																							// id

	private HashMap<String, HarborWaitLocations> groupedwaiting = new HashMap<String, HarborWaitLocations>(); // harborlocations
																												// in
																												// use
																												// with
																												// their
																												// trip
																												// id.
	private HashMap<String, TripLocations> groupedtrip = new HashMap<String, TripLocations>(); // triplocations
																								// in
																								// use
																								// with
																								// their
																								// trip
																								// id

	private HashMap<String, Set<HarborWaitLocations>> waiting = new HashMap<String, Set<HarborWaitLocations>>(); // harborlocations
																													// free
																													// to
																													// use
																													// with
																													// type
	private HashMap<String, Set<TripLocations>> trip = new HashMap<String, Set<TripLocations>>(); // triploctions
																									// free
																									// to
																									// use
																									// with
																									// type

	private HashMap<String, Set<HarborWaitLocations>> waitingac = new HashMap<String, Set<HarborWaitLocations>>(); // harborlocations
																													// in
																													// use
	private HashMap<String, Set<TripLocations>> tripac = new HashMap<String, Set<TripLocations>>(); // triplocations
																									// in
																									// use

	@SuppressWarnings("deprecation")
	public void boardcheck(UUID npcuuid, int questnumber, Player player,
			Location end, String type, String id) {
		double needed = questers.returnwarp(questnumber).getCosts();
		int seconds = Calendar.getInstance().get(Calendar.SECOND);
		int minutes = 10; // Calendar.getInstance().get(Calendar.MINUTE);
		int nearest = ((minutes + 5) / 10) * 10 % 60;

		NPCHandler handler = plugin.getNPCHandler();
		NPC npc = handler.getNPCByUUID(npcuuid);

		if (!type.equals("dragoncoach")) {
			// time checking for next trip
			int next;
			if (nearest == 0 && minutes >= 55) {
				next = 60 - minutes;
			} else {
				next = nearest - minutes;
			}
			if (next < 0) {
				next = -next;
			}
			int next2 = 10 - next;
			seconds = 60 - seconds;
			int time = next * 60 + seconds;
			if (nearest < minutes && (nearest != 0 && minutes <= 55)) {
				if (next2 > 5) {
					next2 = next - 5;
					if (next2 < 0) {
						next2 = -next2;
					}
				}
				String min;
				if (next2 > 1 || waiting.get(type) == null
						|| trip.get(type) == null || waiting.get(type) == null) {
					// just left
					min = "minutes.";
					String message;
					if (type.equals("boat")) {
						message = boatmassmiss();
					} else {
						message = oxcartmassmiss();
					}

					player.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npc.getName()
							+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA
							+ message + next2 + " " + min);
				} else {
					// almost able to board
					min = "minute.";
					String message;
					if (type.equals("boat")) {
						message = boatalmostthere();
					} else {
						message = oxcartalmostthere();
					}
					player.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npc.getName()
							+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA
							+ message);
				}

			} else if (next != 0) {
				// enough time to board
				String min;
				if (next > 1) {
					min = "minutes";
				} else {
					min = "minute";
				}
				String message = fw.GetUtilityLine("Boarding");
				message = message.replace("%type%", type).replace("%min%", min)
						.replace("%sec%", Integer.toString(seconds));
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD
						+ "YTravel" + ChatColor.DARK_PURPLE + "] "
						+ ChatColor.AQUA + message);

				tempboard(npcuuid, questnumber, player, time, type, end, id);

			} else {
				// if last minute board
				String message = fw.GetUtilityLine("AlmostLeaving");
				message = message.replace("%type%", type).replace("%time%",
						Integer.toString(seconds) + " seconds");
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD
						+ npc.getName() + ChatColor.DARK_PURPLE + "] "
						+ ChatColor.AQUA + message);
				tempboard(npcuuid, questnumber, player, time, type, end, id);
			}
		} else {
			// checking if enought money
			try {
				if (Economy.hasEnough(player.getName(), needed)) {
					travel(npcuuid, questnumber, player, type, end, id);
					Economy.subtract(player.getName(), needed);
				} else {
					player.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npc.getName()
							+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA
							+ fw.GetUtilityLine("DragonNotEnoughMoney"));
				}
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			} catch (NoLoanPermittedException e) {
				e.printStackTrace();
			}
		}

	}

	public void tempboard(final UUID npcuuid, final int questnumber,
			final Player player, int time, final String type,
			final Location end, final String tripid) {
		HashSet<UUID> p = new HashSet<UUID>();
		p.add(player.getUniqueId());
		if (players.get(tripid) == null) {
			for (HarborWaitLocations harbor : waiting.get(type)) {
				players.put(tripid, p);
				groupedwaiting.put(tripid, harbor);
				player.teleport(harbor.getLocation());

				waiting.get(type).remove(harbor);
				if (waitingac.get(type) != null) {
					waitingac.get(type).add(harbor);
				} else {
					Set<HarborWaitLocations> h = new HashSet<HarborWaitLocations>();
					h.add(harbor);
					waitingac.put(type, h);
				}
				break;
			}

		} else if (groupedtrip.get(tripid) != null) {

		} else {
			players.get(tripid).add(player.getUniqueId());
			player.teleport(groupedwaiting.get(tripid).getLocation());
		}
		int id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
				new Runnable() {
					@Override
					public void run() {
						if (groupedwaiting.get(tripid) != null) {
							HarborWaitLocations h = groupedwaiting.get(tripid);
							waitingac.get(type).remove(h);
							waiting.get(type).add(h);
							groupedwaiting.remove(tripid);
						}
						travel(npcuuid, questnumber, player, type, end, tripid);
					}
				}, time * 20L);
		schedulerstor.put(player.getUniqueId(), id);

	}

	public void travel(UUID npcuuid, final int questnumber,
			final Player player, final String type, final Location end,
			final String tripid) {
		if (groupedtrip.get(tripid) == null) {
			for (TripLocations trips : trip.get(type)) {
				groupedtrip.put(tripid, trips);
				trip.get(type).remove(trips);
				if (tripac.get(type) != null) {
					tripac.get(type).add(trips);
				} else {
					HashSet<TripLocations> t = new HashSet<TripLocations>();
					t.add(trips);
					tripac.put(type, t);
				}
				break;
			}
		} else {
			player.teleport(groupedtrip.get(tripid).getLocation());
		}

		NPCHandler handler = plugin.getNPCHandler();
		NPC npc =handler.getNPCByUUID(npcuuid);
		final Location start = npc.getCurrentloc();

		int time = traveltime(npcuuid, questnumber, type, end);

		travelevent.put(player.getUniqueId(), chance());

		player.teleport(start);
		String message;
		if (type.equals("boat")) {
			message = boatleaving();
		} else if (type.equals("oxcart")) {
			message = oxcartleaving();
		} else if (type.equals("dragoncoach")) {
			message = dragoncartleavgin();
		} else {
			message = " not yet";
		}
		String mestemp;
		if (travelevent.get(player.getUniqueId()) == false) {
			mestemp = " You have arrived at your destination.";
		} else {
			mestemp = "EVENT";
		}
		int timetemp = 0;
		if (mestemp.equals("EVENT")) {
			timetemp = time;
			time = 10;
		}
		final int time2 = timetemp;
		final String endmessage = mestemp;

		player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD
				+ "YTravel" + ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA
				+ message);

		int id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
				new Runnable() {
					@Override
					public void run() {
						if (groupedtrip.get(tripid) != null) {
							TripLocations trips = groupedtrip.get(tripid);
							groupedtrip.remove(tripid);
							tripac.get(type).remove(trips);
							trip.get(type).add(trips);
						}

						if (!endmessage.equals("EVENT")) {
							player.teleport(end);
							player.sendMessage(ChatColor.DARK_PURPLE + "["
									+ ChatColor.GOLD + "YTravel"
									+ ChatColor.DARK_PURPLE + "] "
									+ ChatColor.AQUA
									+ fw.GetUtilityLine("TravelArrive"));
							schedulerstor.remove(player.getUniqueId());
							players.get(tripid).remove(player.getUniqueId());
						} else {
							if (type.equals("boat")) {
								player.sendMessage(ChatColor.DARK_PURPLE + "["
										+ ChatColor.GOLD + "YTravel"
										+ ChatColor.DARK_PURPLE + "] "
										+ ChatColor.AQUA
										+ fw.GetUtilityLine("TravelEvent"));
								boat.eventint(player, start, end, time2, tripid);
							}
							if (type.equals("oxcart")) {
								player.sendMessage(ChatColor.DARK_PURPLE + "["
										+ ChatColor.GOLD + "YTravel"
										+ ChatColor.DARK_PURPLE + "] "
										+ ChatColor.AQUA
										+ fw.GetUtilityLine("TavelEvent"));
								cart.eventint(player, start, end, time2, tripid);
							}
							if (type.equals("dragoncoach")) {
								player.sendMessage(ChatColor.DARK_PURPLE + "["
										+ ChatColor.GOLD + "YTravel"
										+ ChatColor.DARK_PURPLE + "] "
										+ ChatColor.AQUA
										+ fw.GetUtilityLine("TravelEvent"));
								dragon.eventint(player, start, end, time2,
										tripid);

							}
						}
					}
				}, time * 20L);
		schedulerstor.put(player.getUniqueId(), id);

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onplayerleave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID playeruuid = player.getUniqueId();
		if (schedulerstor.containsKey(playeruuid)) {
			Bukkit.getScheduler().cancelTask(schedulerstor.get(playeruuid));
			schedulerstor.remove(playeruuid);
			if (travelevent.containsKey(playeruuid) == true) {
				travelevent.remove(playeruuid);
			}
			String tripid = null;
			for (String in : players.keySet()) {
				if (players.get(in).contains(playeruuid)) {
					tripid = in;
					break;
				}
			}
			String[] splitted = tripid.split("-");
			int n = Integer.parseInt(splitted[0].trim());
			Location startloc = questers.returnwarp(n).getStartloc();
			player.getLocation().setX(startloc.getX());
			player.getLocation().setY(startloc.getY());
			player.getLocation().setZ(startloc.getZ());
			player.getLocation().setWorld(startloc.getWorld());
			String type = splitted[2].trim();
			String message;
			switch (type) {
			case "boat":
				message = boatleavemessage();
				plugin.boat.playeratevent.remove(player.getUniqueId());
				break;
			case "oxcart":
				message = oxcartleavemessage();
				plugin.cart.playeratevent.remove(player.getUniqueId());
				break;
			case "dragoncoach":
				message = dragoncartleave();
				plugin.dragon.playeratevent.remove(player.getUniqueId());
				break;
			default:
				message = "you passed out.";
				break;
			}
			fw.AddDesserter(player.getUniqueId().toString(), message);
		}
	}

	@EventHandler
	public void onplayerjoind(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (Bukkit.getServer().getPluginManager().getPlugin("MistsOfYsir") != null) {
			File direct = new File(Bukkit.getServer().getPluginManager()
					.getPlugin("MistsOfYsir").getDataFolder(), "desserters");
			if (direct.exists()) {
				for (File content : direct.listFiles()) {
					if (content.getName().replace(".txt", "")
							.equals(player.getUniqueId().toString())) {
						Scanner reader;
						try {
							reader = new Scanner(content);
							player.sendMessage(ChatColor.DARK_PURPLE + "["
									+ ChatColor.GOLD + "YTravel"
									+ ChatColor.DARK_PURPLE + "] "
									+ ChatColor.AQUA + reader.nextLine());
							reader.close();
							content.delete();
							break;
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static int traveltime(UUID npcuuid, int questnumber, String type,
			Location end) {

		NPCHandler handler = plugin.getNPCHandler();
		NPC npc =handler.getNPCByUUID(npcuuid);
		Location start = npc.getCurrentloc();

		double sx = start.getX();
		double sy = start.getY();
		double sz = start.getZ();
		double ex = end.getX();
		double ey = end.getY();
		double ez = end.getZ();

		double dx = ex - sx;
		double dy = ey - sy;
		double dz = ez - sz;

		if (dx < 0) {
			dx = -dx;
		}
		if (dy < 0) {
			dy = -dy;
		}
		if (dz < 0) {
			dz = -dz;
		}
		double td = dx + dy + dz;
		double mindis = 0;
		double speed = 0;
		switch (type) {
		case "boat":
			mindis = 31.5;
			speed = 9;
			break;
		case "oxcart":
			mindis = 21;
			speed = 6;
			break;
		case "dragoncoach":
			mindis = 42;
			speed = 12;
			break;
		}

		double t = td - mindis;
		double time = t / speed;

		double time2 = Math.ceil(time);
		int timeint = (int) time2;
		return timeint;
	}

	public boolean chance() {
		Random r = new Random();
		int k = r.nextInt(100);
		if (k >= 89) {
			return true;
		} else {
			return false;
		}
	}

	// random messages
	public String boatmassmiss() {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("The boat is sailing the oceans, I'll expect it to arrive in");
		messages.add("Unless they stopped to raid a town they will arrive back in port in ");
		messages.add("I think they are about halfway now. They'll be here in ");
		messages.add("I heard they are battling a sea snake! If they win the fight they'll be here in ");
		messages.add("If the ship didn't sink it will be here in ");
		messages.add("They sailed right trough a storm but still I expect them to be here in ");
		messages.add("If the oarsman are as strong as you look I'm sure they'll be here in ");
		messages.add("Pray to the gods the captain knows what he is doing so he can be here in ");
		messages.add("They have passed the point of no return and will be here in ");
		messages.add("If the captain didn't fall for that mermaid again they will be here in ");

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	public String boatalmostthere() {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("I can hear the captain yell, they must be close!");
		messages.add("I see a dot at the horizon, that must be them!");
		messages.add("By my estimates they must be arriving any minute now.");
		messages.add("I'm surprised they aren't here already!");
		messages.add("Wait! They haven't arrived?");

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	public String boatleaving() {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("Ah the smell of salt in the air!");
		messages.add("A cold sea breeze hits you in the face as the ship moves.");
		messages.add("You feel the ship moving as it leaves port.");
		messages.add("Slowly the ship is moving towards the open water.");
		messages.add("And we are on the move!");
		messages.add("The harbor is getting smaller and smaller at the horizon.");
		messages.add("Our voyage has begun!");
		messages.add("I hope nothing bad happens to the ship while sailing!");

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	public String oxcartmassmiss() {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("If they didn't break a wheel they'll be here in ");
		messages.add("Unless they get stuck in the mud again they'll be here in ");
		messages.add("I think they are about halfway now. They'll be here in ");
		messages.add("Let's pray they didn't get jumped by a pack of wolfs so we will see them again in ");
		messages.add("Old Bessy is pulling the cart today and boy, does she know how to pull it! They''ll be here in ");
		messages.add("If they are on time they should be here in ");
		messages.add("Guess they are about half way now, still ");
		messages.add("They have to pass an inn so there's no knowing if they will be on time. Normally they are back in ");
		messages.add("Did those bastards left to early again? You'll have to wait ");
		messages.add("Bad luck there, they have just left. You can wait ");
		messages.add("They left in a hurry yelling that some smelly player was coming. They'll be back in ");
		messages.add("Too late! I expect them back in ");

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	public String oxcartalmostthere() {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("I can smell the oxes! They must be close.");
		messages.add("Unless that was another cow I heard moo'ing they must be arriving pretty soon.");
		messages.add("They should be here any minute now!");
		messages.add("I'm surprised they haven't arrived yet.");

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	public String oxcartleaving() {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("The oxes seem to be happy to depart with you onboard.");
		messages.add("Look at Old Bessy pull the cart!");
		messages.add("With a cracking sound the cart starts to move.");
		messages.add("Is that smell you or is is one of the oxes?");
		messages.add("The entire cart starts to shake as it departs.");
		messages.add("Pray that this journey ends well!");

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	public String dragoncartleavgin() {
		ArrayList<String> message = new ArrayList<String>();
		message.add("With one giant pull you're in the air and on your way.");
		message.add("The Dragon roars as he begins moving the coach.");
		message.add("You begin climbing so high that the air is thinner than you immagined.");
		message.add("You begin sitting confortly while the dragon moves to his take off place.");
		message.add("Pray that the dragon won't fly upside down.");
		message.add("While climbing you look down. You see everything becomes smaller and smaller.");
		Random r = new Random();
		int inex = r.nextInt(message.size() - 1);
		return message.get(inex);
	}

	public String boatleavemessage() {
		ArrayList<String> message = new ArrayList<String>();
		message.add("You jumped of the boat yelling something about a mermaid, luckily you were saved by a fisherman that brought you back.");
		message.add("You jumped naked in the ocean screaming you are the lizard queen. You washed back a shore and a stranger clothed you again.");
		message.add("Have you been drinking? Look where you turned up!");
		message.add("Don't drink salt water!");
		message.add("You've been sleeping the entire journey and ended up back where you started!");
		message.add("A stranger knocked you unconscious. You ended up back where you started.");
		Random r = new Random();
		int index = r.nextInt(message.size() - 1);
		return message.get(index);
	}

	public String oxcartleavemessage() {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("You fell a sleep and ended back at the beginning of your journey.");
		messages.add("While sleeping you fell of the cart. A friendly pig farmer brought you back.");
		messages.add("While traveling you were hit by a low hanging branch and knocked unconscious. You ended up back where you started.");
		messages.add("You ended up back at the beginning after you jumped of the cart screaming the cows were trying to kill you.");
		messages.add("To much alcohol? You got stranded back at the begin.");
		messages.add("A stranger tried robbing you, you jumped of the cart to follow him but lost him here.");

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	public String dragoncartleave() {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("You fell a sleep and the dragon flew you back.");
		messages.add("You fell out of the coach. Luckily a tree saved your life and you crawled back to the start.");
		messages.add("Your coach has crashed. A strange hairy creature brought you back to this city.");
		messages.add("I don't know what happened but somehow you turned up back here!");
		messages.add("A major dragon fight caused you to end up here again.");
		messages.add("Due to the stormy weather your dragon decided to return back here.");

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	public Travel(MoY instance) {
		this.plugin = instance;
		this.questers = instance.questers;
		this.fw = instance.fw;
		this.boat = instance.boat;
		this.cart = instance.cart;
		this.dragon = instance.dragon;
	}

	public void loadhardbor(int id, String type, Location loc) {
		HarborWaitLocations h = new HarborWaitLocations(id);
		h.setLocation(loc);
		h.setType(type);
		if (waiting.get(type) != null) {
			waiting.get(type).add(h);
		} else {
			Set<HarborWaitLocations> t = new HashSet<HarborWaitLocations>();
			t.add(h);
			waiting.put(type, t);
		}
	}

	public void loadtrip(int id, String type, Location loc) {
		TripLocations t = new TripLocations(id);
		t.setLocation(loc);
		t.setType(type);
		if (trip.get(type) != null) {
			trip.get(type).add(t);
		} else {
			Set<TripLocations> tr = new HashSet<TripLocations>();
			tr.add(t);
			trip.put(type, tr);
		}
	}

	public void removetrip(String type, int number) {
		for (TripLocations t : trip.get(type)) {
			if (t.getId() == number) {
				trip.get(type).remove(t);
				plugin.database.deletetrip(number);
				break;
			}
		}
	}

	public void removeharbor(String type, int number) {
		for (HarborWaitLocations h : waiting.get(type)) {
			if (h.getId() == number) {
				waiting.get(type).remove(h);
				plugin.database.deleteharbor(number);
				break;
			}
		}
	}

	public int createnewtrip() {
		int i = 0;
		String type = "none";
		ArrayList<TripLocations> t;
		if (trip.get(type) != null) {
			t = Lists.newArrayList(trip.get(type).iterator());
		} else {
			t = new ArrayList<TripLocations>();
		}
		if (t.size() != 0) {
			for (; i <= t.size(); i++) {
				if (t.get(i) == null) {
					break;
				}
			}
		}
		TripLocations tr = new TripLocations(i);
		if (trip.get(type) != null) {
			trip.get(type).add(tr);
		} else {
			trip.put(type, new HashSet<TripLocations>(Arrays.asList(tr)));
		}
		return i;
	}

	public int createnewharbor() {
		int i = 0;
		String type = "none";
		ArrayList<HarborWaitLocations> t;
		if (waiting.get(type) != null) {
			t = Lists.newArrayList(waiting.get(type).iterator());
		} else {
			t = new ArrayList<HarborWaitLocations>();
		}
		if (t.size() != 0) {
			for (; i <= t.size(); i++) {
				if (t.get(i) == null) {
					break;
				}
			}
		}
		HarborWaitLocations tr = new HarborWaitLocations(i);
		if (waiting.get(type) != null) {
			waiting.get(type).add(tr);
		} else {
			waiting.put(type,
					new HashSet<HarborWaitLocations>(Arrays.asList(tr)));
		}
		return i;
	}

	public TripLocations GetTrip(int number) {
		for (String in : trip.keySet()) {
			for (TripLocations i : trip.get(in)) {
				if (i.getId() == number) {
					return i;
				}
			}
		}

		for (String in : tripac.keySet()) {
			for (TripLocations i : tripac.get(in)) {
				if (i.getId() == number) {
					return i;
				}
			}
		}
		return null;
	}

	public HarborWaitLocations GetHarbor(int number) {
		for (String in : waiting.keySet()) {
			for (HarborWaitLocations i : waiting.get(in)) {
				if (i.getId() == number) {
					return i;
				}
			}
		}

		for (String in : waitingac.keySet()) {
			for (HarborWaitLocations i : waitingac.get(in)) {
				if (i.getId() == number) {
					return i;
				}
			}
		}
		return null;
	}

	protected static void RemoveTrip(String tripid, UUID player) {
		if (players.get(tripid) != null) {
			players.get(tripid).remove(player);
			if (players.get(tripid).size() == 0) {
				players.remove(tripid);
			}
		}
	}

	public Set<Integer> AllTrips() {
		HashSet<Integer> t = new HashSet<Integer>();
		for (String in : trip.keySet()) {
			for (TripLocations tr : trip.get(in)) {
				t.add(tr.getId());
			}
		}

		for (String in : tripac.keySet()) {
			for (TripLocations tr : tripac.get(in)) {
				t.add(tr.getId());
			}
		}
		return t;
	}

	public Set<Integer> AllHarbors() {
		HashSet<Integer> t = new HashSet<Integer>();
		for (String in : waiting.keySet()) {
			for (HarborWaitLocations tr : waiting.get(in)) {
				t.add(tr.getId());
			}
		}

		for (String in : waitingac.keySet()) {
			for (HarborWaitLocations tr : waitingac.get(in)) {
				t.add(tr.getId());
			}
		}
		return t;
	}

	public void EditHarbors(Player player) {
		ArrayList<ItemStack> t = new ArrayList<ItemStack>();
		for (String types : waiting.keySet()) {
			for (HarborWaitLocations in : waiting.get(types)) {
				ItemStack info = ItemGenerator.InfoQuest(in.getType(), in.getId(),
						6, null);
				t.add(info);
			}
		}

		for (String types : waitingac.keySet()) {
			for (HarborWaitLocations in : waitingac.get(types)) {
				ItemStack info = ItemGenerator.InfoQuest(in.getType(), in.getId(),
						6, null);
				t.add(info);
			}
		}

		int rowcount = 0;
		if (t != null) {
			rowcount = t.size();
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
		Inventory inv = Bukkit.createInventory(null, rowcount, "AllHarbors");
		for (ItemStack in : t) {
			inv.addItem(in);
		}
		player.openInventory(inv);

	}

	public void EditTrips(Player player) {
		ArrayList<ItemStack> t = new ArrayList<ItemStack>();
		for (String types : trip.keySet()) {
			for (TripLocations in : trip.get(types)) {
				ItemStack info = ItemGenerator.InfoQuest("trip", in.getId(), 6,
						null);
				t.add(info);
			}
		}

		for (String types : tripac.keySet()) {
			for (TripLocations in : tripac.get(types)) {
				ItemStack info = ItemGenerator.InfoQuest("trip", in.getId(), 6,
						null);
				t.add(info);
			}
		}

		int rowcount = 0;
		if (t != null) {
			rowcount = t.size();
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
		Inventory inv = Bukkit.createInventory(null, rowcount, "AllTrips");
		for (ItemStack in : t) {
			inv.addItem(in);
		}
		player.openInventory(inv);

	}

	public void TypeSwitchHarbor(HarborWaitLocations h, String old,
			String type, int id) {
		if (waiting.get(old) != null
				&& Lists.newArrayList(waiting.get(old).iterator()).get(id) != null) {
			waiting.get(old).remove(h);
			if (waiting.get(type) != null) {
				waiting.get(type).add(h);
			} else {
				HashSet<HarborWaitLocations> t = new HashSet<HarborWaitLocations>();
				t.add(h);
				waiting.put(type, t);
			}
			return;
		}
		if (waitingac.get(old) != null
				&& Lists.newArrayList(waitingac.get(old).iterator()).get(id) != null) {
			waitingac.get(old).remove(h);
			if (waiting.get(type) != null) {
				waiting.get(type).add(h);
			} else {
				HashSet<HarborWaitLocations> t = new HashSet<HarborWaitLocations>();
				t.add(h);
				waiting.put(type, t);
			}
			return;
		}

	}

	public void TypeSwitchTrip(TripLocations h, String old, String type, int id) {
		if (trip.get(old) != null
				&& Lists.newArrayList(trip.get(old).iterator()).get(id) != null) {
			trip.get(old).remove(h);
			if (trip.get(type) != null) {
				trip.get(type).add(h);
			} else {
				HashSet<TripLocations> t = new HashSet<TripLocations>();
				t.add(h);
				trip.put(type, t);
			}
			return;
		}
		if (tripac.get(old) != null
				&& Lists.newArrayList(tripac.get(old).iterator()).get(id) != null) {
			tripac.get(old).remove(h);
			if (trip.get(type) != null) {
				trip.get(type).add(h);
			} else {
				HashSet<TripLocations> t = new HashSet<TripLocations>();
				t.add(h);
				trip.put(type, t);
			}
			return;
		}

	}

}
