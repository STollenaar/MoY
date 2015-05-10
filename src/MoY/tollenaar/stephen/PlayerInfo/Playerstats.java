package MoY.tollenaar.stephen.PlayerInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;


public class Playerstats {
 
	// general stats
	public static HashMap<UUID, Integer> abilitiescore = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> Strengthscore = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> Dexscore = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> level = new HashMap<UUID, Integer>();
// skills
	public static HashMap<UUID, Integer> woodcutting = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> mining = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> smelting = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> cooking = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> fishing = new HashMap<UUID, Integer>();
//skill progress
	public static HashMap<UUID, Integer> levelprog = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> woodcuttingprog = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> miningprog = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> smeltingprog = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> cookingprog = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> fishingprog = new HashMap<UUID, Integer>();

	
	//quests			playeruuid		type		questnumber			
	public static HashMap<UUID, HashMap<String, HashSet<Integer>>> activequests = new HashMap<UUID, HashMap<String, HashSet<Integer>>>();
	public static HashMap<UUID, HashMap<String, HashSet<Integer>>> completedquests = new HashMap<UUID, HashMap<String, HashSet<Integer>>>();
	public static HashMap<UUID, HashMap<String, HashMap<Integer, Long>>> rewardedlist = new HashMap<UUID, HashMap<String, HashMap<Integer, Long>>>();
}
