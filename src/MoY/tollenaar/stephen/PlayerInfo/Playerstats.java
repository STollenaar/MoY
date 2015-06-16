package MoY.tollenaar.stephen.PlayerInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Playerstats {

	private UUID playeruuid;
	private int ability;
	private int strength;
	private int dex;
	private int level;

	private int woodcutting;
	private int mining;
	private int smelting;
	private int cooking;
	private int fishing;

	private int woodcuttingprog;
	private int miningprog;
	private int smeltingprog;
	private int cookingprog;
	private int fishingprog;
	private int levelprog;

	public Playerstats(UUID playeruuid) {
		this.playeruuid = playeruuid;
		this.ability = 0;
		this.strength = 1;
		this.dex = 1;
		this.level = 1;

		this.woodcutting = 1;
		this.mining = 1;
		this.smelting = 1;
		this.cooking = 1;
		this.fishing = 1;

		this.levelprog = 0;
		this.woodcuttingprog = 0;
		this.miningprog = 0;
		this.smeltingprog = 0;
		this.cookingprog = 0;
		this.fishingprog = 0;
		
		this.activequests = new HashMap<String, Set<Integer>>();
		this.completedquests = new HashMap<String, Set<Integer>>();
		this.rewardedlist = new HashMap<String, HashMap<Integer, Long>>();
	}
	
	private HashMap<String, Set<Integer>> activequests;
	private HashMap<String, Set<Integer>> completedquests;
	private HashMap<String, HashMap<Integer, Long>> rewardedlist;
	
	
	public void addactive(String type, int number){
		if(activequests.get(type) != null){
			activequests.get(type).add(number);
		}else{
			Set<Integer> temp = new HashSet<Integer>();
			temp.add(number);
			activequests.put(type, temp);
		}
	}
	
	public void addcompleted(String type, int number){
		if(completedquests.get(type) != null){
			completedquests.get(type).add(number);
		}else{
			Set<Integer> temp = new HashSet<Integer>();
			temp.add(number);
			completedquests.put(type, temp);
		}
	}
	
	public void addrewarded(String type, int number, long time){
		if(rewardedlist.get(type) != null){
			HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
			temp.put(number, time);
			rewardedlist.get(type).put(number, time);
		}else{
			HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
			temp.put(number, time);
			rewardedlist.put(type, temp);
		}
	}
	public Set<String> getactivetype(){
		return activequests.keySet();
	}
	
	public Set<Integer> getactives(String type){
		if(activequests.get(type) != null){
			return activequests.get(type);
		}else{
			return null;
		}
	}
	public Set<String> getcompletedtype(){
		return completedquests.keySet();
	}
	
	public Set<Integer> getcompleted(String type){
		if(completedquests.get(type) != null){
			return completedquests.get(type);
		}else{
			return null;
		}
	}
	
	public Set<String> getrewardedtype(){
		return rewardedlist.keySet();
	}
	
	public Set<Integer> getrewardednumber(String type){
		if(rewardedlist.get(type) != null){
			return rewardedlist.get(type).keySet();
		}else{
			return null;
		}
	}
	
	public long getrewardedtime(String type, int number){
		if(rewardedlist.get(type) != null && rewardedlist.get(type).get(number) != null){
			return rewardedlist.get(type).get(number);
		}else{
			return 0;
		}
	}
	public void deleteactive(String type, int number){
		if(activequests.get(type) != null){
			if(activequests.get(type).contains(number)){
				activequests.get(type).remove(number);
			}
		}
	}
	public void deletecompleted(String type, int number){
		if(completedquests.get(type) != null){
			if(completedquests.get(type).contains(number)){
				completedquests.get(type).remove(number);
			}
		}
	}
	
	public void deleterewarded(String type, int number){
		if(rewardedlist.get(type) != null){
			if(rewardedlist.get(type).containsKey(number)){
				rewardedlist.get(type).remove(number);
			}
		}
	}
	
	
	public int getAbility() {
		return ability;
	}
	public void setAbility(int ability) {
		this.ability = ability;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public int getDex() {
		return dex;
	}
	public void setDex(int dex) {
		this.dex = dex;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getWoodcutting() {
		return woodcutting;
	}
	public void setWoodcutting(int woodcutting) {
		this.woodcutting = woodcutting;
	}
	public int getMining() {
		return mining;
	}
	public void setMining(int mining) {
		this.mining = mining;
	}
	public int getSmelting() {
		return smelting;
	}
	public void setSmelting(int smelting) {
		this.smelting = smelting;
	}
	public int getCooking() {
		return cooking;
	}
	public void setCooking(int cooking) {
		this.cooking = cooking;
	}
	public int getFishing() {
		return fishing;
	}
	public void setFishing(int fishing) {
		this.fishing = fishing;
	}
	public int getWoodcuttingprog() {
		return woodcuttingprog;
	}
	public void setWoodcuttingprog(int woodcuttingprog) {
		this.woodcuttingprog = woodcuttingprog;
	}
	public int getMiningprog() {
		return miningprog;
	}
	public void setMiningprog(int miningprog) {
		this.miningprog = miningprog;
	}
	public int getSmeltingprog() {
		return smeltingprog;
	}
	public void setSmeltingprog(int smeltingprog) {
		this.smeltingprog = smeltingprog;
	}
	public int getCookingprog() {
		return cookingprog;
	}
	public void setCookingprog(int cookingprog) {
		this.cookingprog = cookingprog;
	}
	public int getFishingprog() {
		return fishingprog;
	}
	public void setFishingprog(int fishingprog) {
		this.fishingprog = fishingprog;
	}
	public int getLevelprog() {
		return levelprog;
	}
	public void setLevelprog(int levelprog) {
		this.levelprog = levelprog;
	}
	public UUID getPlayeruuid() {
		return playeruuid;
	}
	



	// // general stats
	// public static HashMap<UUID, Integer> abilitiescore = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> Strengthscore = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> Dexscore = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> level = new HashMap<UUID,
	// Integer>();
	// // skills
	// public static HashMap<UUID, Integer> woodcutting = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> mining = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> smelting = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> cooking = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> fishing = new HashMap<UUID,
	// Integer>();
	// //skill progress
	// public static HashMap<UUID, Integer> levelprog = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> woodcuttingprog = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> miningprog = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> smeltingprog = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> cookingprog = new HashMap<UUID,
	// Integer>();
	// public static HashMap<UUID, Integer> fishingprog = new HashMap<UUID,
	// Integer>();
	//
	//
	// quests playeruuid type questnumber
//	public static HashMap<UUID, HashMap<String, HashSet<Integer>>> activequests = new HashMap<UUID, HashMap<String, HashSet<Integer>>>();
//	public static HashMap<UUID, HashMap<String, HashSet<Integer>>> completedquests = new HashMap<UUID, HashMap<String, HashSet<Integer>>>();
//	public static HashMap<UUID, HashMap<String, HashMap<Integer, Long>>> rewardedlist = new HashMap<UUID, HashMap<String, HashMap<Integer, Long>>>();
}
