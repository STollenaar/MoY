package moy.tollenaar.stephen.Commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import moy.tollenaar.stephen.CEvents.QuestProcessEvent;
import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.Speech.SpeechCache;
import moy.tollenaar.stephen.Speech.SpeechNode;
import moy.tollenaar.stephen.Speech.SpeechTrait;
import moy.tollenaar.stephen.Speech.SpeechType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeechCommand implements CommandExecutor {
	private static HashMap<UUID, SpeechCache> cache = new HashMap<>();
	private MoY plugin;

	public SpeechCommand(MoY instance) {
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (args.length == 5 && sender instanceof Player
				&& sender.getName().equals(args[1])) {
				Player player = (Player) sender;
				SpeechType speechType = SpeechType.getType(args[0]);
				int node = Integer.parseInt(args[2]);
				UUID npcuuid = UUID.fromString(args[3]);
				String npcname = plugin.getNPCHandler().getNPCByUUID(npcuuid).getName();
				int questType =  Integer.parseInt(args[4]);
				if(speechType == SpeechType.ACCEPT){
					if(questType != -5){
						QuestProcessEvent event = new QuestProcessEvent(player, node, getQuestType(questType));
						Bukkit.getPluginManager().callEvent(event);
						sender.sendMessage(ChatColor.DARK_PURPLE + "["
								+ ChatColor.GOLD + npcname + ChatColor.DARK_PURPLE
								+ "]" + ChatColor.AQUA
								+ " Thanks. Come back when you are finished.");
					}
				}else if(speechType == SpeechType.DECLINE){
					sender.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npcname + ChatColor.DARK_PURPLE
							+ "]" + ChatColor.AQUA + " How pitiful.");
				}else if(speechType == SpeechType.NODE){
					SpeechNode next = SpeechNode.getNode(node);
					next.constructNode(player);
				}
		}
		return true;
	}

	public static void addCache(UUID player, int number) {
		cache.put(player, new SpeechCache(number));
	}

	public boolean isCache(UUID player, String type, int number) {
		if (cache.get(player) != null) {
			SpeechCache c = cache.get(player);
			if (c.getNumber() == number) {
				return true;
			}
		}
		return false;
	}
	
	private String getQuestType(int type){
		switch(type){
		case 1:
			return "kill";
		case 2:
			return "harvest";
		case 3:
			return "talkto";
		case 7:
			return "event";
		default:
			return null;
		}
	
	}
	
	public void removeCache(UUID player) {
		cache.remove(player);
	}

}
