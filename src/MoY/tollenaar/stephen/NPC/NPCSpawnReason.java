package MoY.tollenaar.stephen.NPC;

public enum NPCSpawnReason {
	NORMAL_SPAWN("normal"),
	RESPAWN(null),
	DESPAWN(null),
	SHOP_SPAWN("shop");
	
	private String type;
	
	 NPCSpawnReason(String type) {
		this.type = type;
	}
	 
	 public void setType(NPCSpawnReason reason){
		 if(reason.getType() != null){
			setType(reason.getType());
		 }else{
			 setType("normal");
		 }
	 }
	 
	 public void setType(String type){
		this.type = type;
	 }
	 
	 public String getType(){
		 return type;
	 }
	 
	 
}
