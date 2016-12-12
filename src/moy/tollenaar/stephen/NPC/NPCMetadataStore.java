package moy.tollenaar.stephen.NPC;

import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_11_R1.metadata.PlayerMetadataStore;

public class NPCMetadataStore extends PlayerMetadataStore{

	private final String owner;
	private final String shop;
	private final NPCSpawnReason reason;
	private final int id;

	
	public String getShop() {
		return shop;
	}
	public String getOwner() {
		return owner;
	}

	public NPCSpawnReason getReason() {
		return reason;
	}
	
	public NPCMetadataStore(String owner, String shop, int id){
		this.owner = owner;
		this.shop = shop;
		this.reason = NPCSpawnReason.SHOP_SPAWN;
		this.id = id;
	}
	
    @Override
    protected String disambiguate(OfflinePlayer player, String metadataKey) {
        return player.getUniqueId().toString() + ":" + metadataKey;
    }
	public int getId() {
		return id;
	}
}
