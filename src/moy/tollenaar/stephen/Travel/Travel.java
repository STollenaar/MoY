package moy.tollenaar.stephen.Travel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import moy.tollenaar.stephen.Files.Filewriters;
import moy.tollenaar.stephen.InventoryUtils.ItemGenerator;
import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.NPC.NPC;
import moy.tollenaar.stephen.NPC.NPCHandler;
import moy.tollenaar.stephen.Quests.QuestsServerSide;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class Travel implements Listener {
	private TravelBoatEvent boat;
	private TravelCartEvent cart;
	private TravelDragonEvent dragon;
	private MoY plugin;
	private Filewriters fw;
	private QuestsServerSide questers;

	private static HashMap<Integer, TripLocations> trip = new HashMap<Integer, TripLocations>();
	private static HashMap<Integer, HarborWaitLocations> waiting = new HashMap<Integer, HarborWaitLocations>();
	private static HashMap<String, UUID> tempID = new HashMap<String, UUID>();
	private static HashMap<UUID, String> tripType = new HashMap<UUID, String>();
	
	private static HashMap<UUID, HarborWaitLocations> ACTIVE_HARBOR = new HashMap<>();
	private static HashMap<UUID, TripLocations> ACTIVE_TRIP = new HashMap<>();
	private static HashMap<UUID, Set<UUID>> players = new HashMap<>();
	private static HashMap<UUID, Location> START_LOC = new HashMap<>();
	
	private static HashMap<UUID, Set<Integer>> TRIP_SCHEDULE = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	public void boardcheck(UUID npcuuid, int questnumber, Player player,
			String type, String id) {
		NPCHandler handler = plugin.getNPCHandler();
		NPC npc = handler.getNPCByUUID(npcuuid);
		Location end = null;
		int endid = Integer.parseInt(id.split("-")[1]);
		for(UUID npcs : questers.GetKeysSets("warp")){
			if(questers.getWarpId(npcs) == endid){
				end = handler.getNPCByUUID(npcuuid).getCurrentloc();
			}
		}
		if(type.equals("dragoncoach")){
			// checking if enought money
			double needed = questers.returnwarp(questnumber).getCosts();
			try {
				if (Economy.hasEnough(player.getName(), needed)) {
					waitBoard(0, player, id, type, npcuuid, end);
					Economy.subtract(player.getName(), needed);
					return;
				} else {
					player.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npc.getName()
							+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA
							+ fw.GetUtilityLine("DragonNotEnoughMoney"));
					return;
				}
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			} catch (NoLoanPermittedException e) {
				e.printStackTrace();
			}
			return;
		}
		boolean canTravel = false;
		int seconds = Calendar.getInstance().get(Calendar.SECOND);
		int minutes =  Calendar.getInstance().get(Calendar.MINUTE);
		

		String message = ChatColor.DARK_PURPLE + "["
				+ ChatColor.GOLD + npc.getName()
				+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA;
		int next;
		if (minutes % 10 == 0) {
			next = 0;
			if(seconds < 60){
				canTravel = true;
				message = fw.GetUtilityLine("AlmostLeaving");
				message = message.replace("%type%", type).replace("%time%",
						Integer.toString(seconds) + " seconds");
			}else{
				if(type.equals("boat")){
					message += boatmassmiss();
				}else{
					message += oxcartmassmiss();
				}
				//to late
			}
			
		} else {
			next = (60 - minutes) % 10;
			String min;
			if(next/60 > 1 || next/60 == 0){
				min = "minutes";
			}else{
				min = "minute";
			}
		if(next <= 5){
		
			message += fw.GetUtilityLine("Boarding");
			message = message.replace("%type%", type).replace("%time%", Integer.toString(next/60)).replace("%min%", min)
					.replace("%sec%", Integer.toString(60-seconds));
			canTravel = true;
		}else if(next <= 7){
			if(type.equals("boat")){
				message += boatalmostthere();
			}else{
				message += oxcartalmostthere();
			}
			
		}else{
			if(type.equals("boat")){
				message += boatmassmiss();
			}else{
				message += oxcartmassmiss();
			}
			message += next + " " + min;
		}
		}
		if(canTravel){	
		int left = next * 60 + (60 - seconds); //gives the time for when it departures.. so give this time to the new method
		if(tempID.get(id) != null){
			player.setMetadata("Trip", new FixedMetadataValue(plugin, tempID.get(id)));
		}else{
			UUID generate = UUID.randomUUID();
			TripLocations t = pullTrip(type);
			HarborWaitLocations h = pullHarbor(type);
			if(t == null || h == null){
				switch(type){
				case "boat":
					player.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npc.getName()
							+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA + "O no. Looks like the ship needs to head to the shipyard. Come back in a few hours.");
					break;
				case "oxcart":
					player.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npc.getName()
							+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA + "O dear. The oxes are exhausted. They need rest now. Come back in a few hours.");
					break;
				case "dragoncoach":
					player.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npc.getName()
							+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA + "Do you see that leash. It needs to be repaired. Come back in a few hours.");
					break;
				default:
					player.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npc.getName()
							+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA + "What are you looking at? There is nothing here. All the vechicles are gone.");
					break;
				}
				return;
			}
			h.setActive(true);
			t.setActive(true);
			ACTIVE_HARBOR.put(generate, h);
			ACTIVE_TRIP.put(generate, t);
			tripType.put(generate, type);
			if(players.get(generate) != null){
				players.get(generate).add(player.getUniqueId());
			}else{
				Set<UUID> temp = new HashSet<UUID>();
				temp.add(player.getUniqueId());
				players.put(generate, temp);
			}
			START_LOC.put(generate, npc.getCurrentloc());
			player.setMetadata("Trip", new FixedMetadataValue(plugin, generate));
		}
		player.sendMessage(message);;
		waitBoard(left, player, id, type, npcuuid, end);
		}else{
			player.sendMessage(message);
		}
	}

	private void waitBoard(int timTillDeparture, final Player pl, final String id, final String type,final UUID npcuuid,final Location end){
		final UUID uuid = UUID.fromString(pl.getMetadata("Trip").get(0).asString());
	
		//insert message?
		HarborWaitLocations h = ACTIVE_HARBOR.get(uuid);
		pl.teleport(h.getLocation());
		int schedule = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				Location start = questers.returnwarp(questers.getWarpId(npcuuid)).getStartloc();
				ACTIVE_HARBOR.get(uuid).setActive(false);
				ACTIVE_HARBOR.remove(uuid);
				String message = ChatColor.DARK_PURPLE + "["
						+ ChatColor.GOLD + "YTravel"
						+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA;
				switch(type){
				case "boat":
					message += boatleaving();
					break;
				case "oxcart":
					message += oxcartleaving();
					break;
				case "dragoncoach":
					message += dragoncartleavgin();
					break;
				}
				pl.sendMessage(message);
				travel(npcuuid, pl, id, end, type, start);
			}
		}, timTillDeparture*20L);
		if(TRIP_SCHEDULE.get(uuid) != null){
			TRIP_SCHEDULE.get(uuid).add(schedule);
		}else{
			Set<Integer> temp = new HashSet<Integer>();
			temp.add(schedule);
			TRIP_SCHEDULE.put(uuid, temp);
		}
		
		
	}
	private void travel(UUID npcuuid, final Player player, final String id, final Location end, final String type, final Location start){
		final UUID uuid = UUID.fromString(player.getMetadata("Trip").get(0).asString());
		TRIP_SCHEDULE.remove(uuid);
		ACTIVE_TRIP.get(uuid).setActive(false);
		ACTIVE_TRIP.remove(uuid);
		int time = traveltime(npcuuid, Integer.parseInt(id.split("-")[0]), type, end, Integer.parseInt(id.split("-")[1]));
		boolean canEvent = true;
		boolean isEvent = chance();
		for(UUID pl : players.get(uuid)){
			if(plugin.playerinfo.getplayer(pl).getLevel() < 20){
				canEvent = false;
				break;
			}
		}
		final int time2 = time;
		if(canEvent && eventAviable(type)){
			if(isEvent){
				time = 10;
			}
		}else if(isEvent){
			isEvent = false;
		}
		final boolean event = isEvent;
		
		int schedule = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				
				if(!event){
				player.removeMetadata("Trip", plugin);
				player.teleport(end);
				players.remove(uuid);
				player.sendMessage(ChatColor.DARK_PURPLE + "["
						+ ChatColor.GOLD + "YTravel"
						+ ChatColor.DARK_PURPLE + "] "
						+ ChatColor.AQUA
						+ fw.GetUtilityLine("TravelArrive"));
				ACTIVE_TRIP.get(uuid).setActive(false);
				ACTIVE_TRIP.remove(uuid);
				tripType.remove(uuid);
				START_LOC.remove(uuid);
				}else{
					if (type.equals("boat")) {
						player.sendMessage(ChatColor.DARK_PURPLE + "["
								+ ChatColor.GOLD + "YTravel"
								+ ChatColor.DARK_PURPLE + "] "
								+ ChatColor.AQUA
								+ fw.GetUtilityLine("TravelEvent"));
						boat.eventint(player, start, end, time2, id);
					}
					if (type.equals("oxcart")) {
						player.sendMessage(ChatColor.DARK_PURPLE + "["
								+ ChatColor.GOLD + "YTravel"
								+ ChatColor.DARK_PURPLE + "] "
								+ ChatColor.AQUA
								+ fw.GetUtilityLine("TavelEvent"));
						cart.eventint(player, start, end, time2, id);
					}
					if (type.equals("dragoncoach")) {
						player.sendMessage(ChatColor.DARK_PURPLE + "["
								+ ChatColor.GOLD + "YTravel"
								+ ChatColor.DARK_PURPLE + "] "
								+ ChatColor.AQUA
								+ fw.GetUtilityLine("TravelEvent"));
						dragon.eventint(player, start, end, time2,
								id);

					}
				}
			}
		}, time*20L);
		if(TRIP_SCHEDULE.get(uuid) != null){
			TRIP_SCHEDULE.get(uuid).add(schedule);
		}else{
			Set<Integer> temp = new HashSet<Integer>();
			temp.add(schedule);
			TRIP_SCHEDULE.put(uuid, temp);
		}
	}
	
	
	private boolean eventAviable(String type){
		switch(type){
		case "boat":
			return false;
		case "oxcart":
			return false;
		case "dragoncaoch":
			return false;
		default:
			return false;
		}
	}
	
	
	private TripLocations pullTrip(String type){
		for(int id : trip.keySet()){
			TripLocations t = trip.get(id);
			if(t.getType().equals(type) && !t.isActive()){
				return t;
			}
		}
		return null;
	}
	private HarborWaitLocations pullHarbor(String type){
		for(int id : waiting.keySet()){
			HarborWaitLocations h = waiting.get(id);
			if(h.getType().equals(type) && !h.isActive()){
				return h;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onplayerjoind(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(player.hasMetadata("Desserter")){
			String type = player.getMetadata("Desserter").get(0).asString();
			String message = ChatColor.DARK_PURPLE + "["
					+ ChatColor.GOLD + "YTravel"
					+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA;
			switch(type){
			case "boat":
				message += boatleavemessage();
				break;
			case "oxcart":
				message += oxcartleavemessage();
				break;
			case "dragoncoach":
				message += dragoncartleave();
				break;
			}
			player.sendMessage(message);
			player.removeMetadata("Desserter", plugin);
		}
	}

	public boolean containsID(Set<Integer> first, Set<Integer> last) {
		for (int in1 : first) {
			for (int in2 : last) {
				if (in2 == in1) {
					return true;
				}
			}
		}

		return false;
	}

	public int traveltime(UUID npcuuid, int questnumber, String type,
			Location end, int destnumber) {

		NPCHandler handler = plugin.getNPCHandler();
		NPC npc = handler.getNPCByUUID(npcuuid);
		Location start = npc.getCurrentloc();

		if (questers.returnwarp(questnumber).getByPassID().size() != 0
				&& questers.returnwarp(destnumber).getByPassID().size() != 0
				&& containsID(questers.returnwarp(questnumber).getByPassID(),
						questers.returnwarp(destnumber).getByPassID())) {
			int time = getSeconds(questers.returnwarp(questnumber)
					.getBypassedTime());
			System.out.println(time);
			if (time != -1) {
				return time;
			}
		}

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

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		if(event.getPlayer().hasMetadata("Trip")){
			UUID trip = UUID.fromString(event.getPlayer().getMetadata("Trip").get(0).asString());
			players.get(trip).remove(trip);
			event.getPlayer().setMetadata("Desserter", new FixedMetadataValue(plugin, tripType.get(trip)));
		}
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
	private String boatmassmiss() {
		ArrayList<String> messages = fw.TravelBoatmiss();

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	private String boatalmostthere() {
		ArrayList<String> messages = fw.Travelboatnearmiss();

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	private String boatleaving() {
		ArrayList<String> messages = fw.TravelBoatLeaving();

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	private String oxcartmassmiss() {
		ArrayList<String> messages = fw.TravelCartmis();

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	private String oxcartalmostthere() {
		ArrayList<String> messages = fw.Travelcartnearmiss();

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	private String oxcartleaving() {
		ArrayList<String> messages = fw.TravelCartLeaving();

		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	private String dragoncartleavgin() {
		ArrayList<String> message = fw.TravelDragonLeaving();
		Random r = new Random();
		int inex = r.nextInt(message.size() - 1);
		return message.get(inex);
	}

	private String boatleavemessage() {
		ArrayList<String> message = fw.TravelBoatLogout();
		Random r = new Random();
		int index = r.nextInt(message.size() - 1);
		return message.get(index);
	}

	private String oxcartleavemessage() {
		ArrayList<String> messages = fw.TravelCartLogout();
		Random r = new Random();
		int index = r.nextInt(messages.size() - 1);
		return messages.get(index);
	}

	private String dragoncartleave() {
		ArrayList<String> messages = fw.TravelDragonLogout();
		Random r = new Random();
		int index = r.nextInt(messages.size()-1);
		return messages.get(index);
	}

	public Travel(MoY instance) {
		this.plugin = instance;
		this.questers = instance.qserver;
		this.fw = instance.fw;
		this.boat = instance.boat;
		this.cart = instance.cart;
		this.dragon = instance.dragon;
	}

	public void loadhardbor(int id, String type, Location loc) {
		HarborWaitLocations h = new HarborWaitLocations(id);
		h.setLocation(loc);
		h.setType(type);
		waiting.put(id, h);
	}

	public void loadtrip(int id, String type, Location loc) {
		TripLocations t = new TripLocations(id);
		t.setLocation(loc);
		t.setType(type);
		trip.put(id, t);
	}

	public void removetrip(int number) {
		trip.remove(number);
		plugin.database.deletetrip(number);
	}

	public void removeharbor( int number) {
			waiting.remove(number);
			plugin.database.deleteharbor(number);
	}

	public int createnewtrip() {
		TripLocations tr = new TripLocations();
		trip.put(tr.getId(), tr);
		return tr.getId();
	}

	public int createnewharbor() {
		HarborWaitLocations tr = new HarborWaitLocations();
		waiting.put(tr.getId(), tr);
		return tr.getId();
	}

	public TripLocations GetTrip(int number) {
		return trip.get(number);
	}

	public HarborWaitLocations GetHarbor(int number) {
		return waiting.get(number);
	}


	public Set<Integer> AllTrips() {
		return trip.keySet();
	}

	public Set<Integer> AllHarbors() {
		return waiting.keySet();
	}

	public void EditHarbors(Player player) {
		ArrayList<ItemStack> t = new ArrayList<ItemStack>();
		for (int types : waiting.keySet()) {
				HarborWaitLocations in = waiting.get(types);
				ItemStack info = ItemGenerator.InfoQuest(in.getType(),
						in.getId(), 6, null);
				t.add(info);
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
		for (int types : trip.keySet()) {
			TripLocations in = trip.get(types);
			ItemStack info = ItemGenerator.InfoQuest("trip", in.getId(), 6,
						null);
				t.add(info);
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


	private int getSeconds(String time) {
		String[] splitted = time.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		try {
			int duration = Integer.parseInt(splitted[0].trim());
			switch (splitted[1]) {
			case "s":
			case "S":
				break;
			case "m":
			case "M":
				duration *= 60;
				break;
			case "h":
			case "H":
				duration *= 60 * 60;
				break;
			}
			return duration;
		} catch (NumberFormatException ex) {
			return -1;
		}
	}
	public void disableFailSafe(){
		for(UUID in : players.keySet()){
			for(UUID player : players.get(in)){
				Bukkit.getPlayer(player).teleport(START_LOC.get(in));
			}
			for(int schedulers : TRIP_SCHEDULE.get(in)){
				Bukkit.getScheduler().cancelTask(schedulers);
			}
			ACTIVE_HARBOR.get(in).setActive(false);
			ACTIVE_TRIP.get(in).setActive(false);
		}
	}
}
