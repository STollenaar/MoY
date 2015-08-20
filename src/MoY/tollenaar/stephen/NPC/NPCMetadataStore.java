package MoY.tollenaar.stephen.NPC;

import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.metadata.PlayerMetadataStore;

public class NPCMetadataStore extends PlayerMetadataStore{

    @Override
    protected String disambiguate(OfflinePlayer player, String metadataKey) {
        return player.getUniqueId().toString() + ":" + metadataKey;
    }
}
