package moy.tollenaar.stephen.Util;

import java.util.Set;
import java.util.UUID;

import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.PlayerInfo.Party;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class ChatController implements Listener{
	MoY plugin;
	Party party;
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event){
		Player player  = event.getPlayer();
		String message = event.getMessage();
		if(party.Partychat.contains(player.getUniqueId())){
			event.setCancelled(true);
			Set<UUID> members = party.Partyswithmembers.get(party.Partymembers.get(player.getUniqueId()));
			for(UUID member: members){
				for(Player players : Bukkit.getOnlinePlayers()){
					if(member.compareTo(players.getUniqueId()) == 0){
						players.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + ChatColor.RED + player.getName() + ": " + ChatColor.BLUE + message);
					}
				}
			}
		}
	}
	
	public ChatController(MoY instance){
		this.plugin = instance;
		this.party = instance.party;
	}
}
