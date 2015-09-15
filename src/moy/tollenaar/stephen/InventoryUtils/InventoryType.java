package moy.tollenaar.stephen.InventoryUtils;

public enum InventoryType {
		ALLKILL("AllKill"),
		ALLHARVEST("AllHarvest"),
		ALLTALKTO("AllTalkTo"),
		WARPLIST("WarpList"),
		ALLWARPS("AllWarps"),
		TALKTOQUEST("TalktoQuest"),
		KILLQUEST("KillQuest"),
		HARVESTQUEST("HarvestQuest"),
		MAINSETTING("Main settings"),
		EVENTQUEST("EventQuest"),
		ALLEVENTS("AllEvents"),
	;

	private final String name;

	InventoryType(String name) {
		this.name = name;
	}

	public static boolean contains(String type){
		for(InventoryType in : values()){
			if(in.getname().equals(type)){
				return true;
			}
		}
		return false;
	}

	private String getname() {
		return name;
	}
}
