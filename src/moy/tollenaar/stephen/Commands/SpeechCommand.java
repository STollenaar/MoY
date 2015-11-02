package moy.tollenaar.stephen.Commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import moy.tollenaar.stephen.CEvents.QuestProcessEvent;
import moy.tollenaar.stephen.Quests.SpeechType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeechCommand implements CommandExecutor {

	private static HashMap<UUID, HashMap<String, Set<Integer>>> cachedrespone = new HashMap<UUID, HashMap<String, Set<Integer>>>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (args.length == 5 && args[1].equals(sender.getName())
				&& sender instanceof Player) {
			// replace type for enum
			SpeechType type = SpeechType.getType(args[0]);
			String questtype = args[2];
			String questnumber = args[3];
			String npcname = args[4];
			Player player = (Player) sender;
			if (!isCached(player.getUniqueId(), questtype,
					Integer.parseInt(questnumber))) {
				addCach(player.getUniqueId(), questtype, Integer.parseInt(questnumber));
				if (type == SpeechType.ACCEPT) {
					QuestProcessEvent event = new QuestProcessEvent(
							(Player) sender, Integer.parseInt(questnumber),
							questtype);
					Bukkit.getPluginManager().callEvent(event);
					sender.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npcname + ChatColor.DARK_PURPLE
							+ "]" + ChatColor.AQUA
							+ " Thanks. Come back when you are finished.");
				} else {
					sender.sendMessage(ChatColor.DARK_PURPLE + "["
							+ ChatColor.GOLD + npcname + ChatColor.DARK_PURPLE
							+ "]" + ChatColor.AQUA + " How pitiful.");
				}

				return true;
			}
		}
		return true;
	}

	private void addCach(UUID player, String type, int number) {
		if(cachedrespone.get(player) != null){
			if(cachedrespone.get(player).get(type) != null){
				cachedrespone.get(player).get(type).add(number);
			}else{
				Set<Integer> t = new HashSet<Integer>();
				t.add(number);
				cachedrespone.get(player).put(type, t);
			}
		}else{
			Set<Integer> t = new HashSet<Integer>();
			t.add(number);
			HashMap<String, Set<Integer>> tt = new HashMap<String, Set<Integer>>();
			tt.put(type, t);
			cachedrespone.put(player, tt);
		}
	}
	
	public void removeCache(UUID player, String type, int number){
		try{
		cachedrespone.get(player).get(type).remove(number);
		}catch(NullPointerException ex){
			return;
		}
	}

	private boolean isCached(UUID player, String type, int number) {
		if (cachedrespone.get(player) != null) {
			if (cachedrespone.get(player).get(type) != null) {
				if (cachedrespone.get(player).get(type).contains(number)) {
					return true;
				}
			}
		}

		return false;
	}

}
