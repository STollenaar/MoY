package moy.tollenaar.stephen.Speech;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import lib.fanciful.main.FancyMessage;
import moy.tollenaar.stephen.Commands.SpeechCommand;

public enum SpeechType {
	ACCEPT("I will help %npcname%. ", "accept", "/speechtrait accept "), DECLINE(
			"Sorry %npcname%. I'll look into it another time.", "decline",
			"/speechtrait decline "), NODE("none", "node", "/speechtrait node");

	private final String message;

	public String getMessage() {
		return message;
	}

	public String getName() {
		return name;
	}

	public String getCommand() {
		return command;
	}

	private final String name;
	private final String command;

	private SpeechType(String message, String name, String command) {
		this.message = message;
		this.name = name;
		this.command = command;
	}

	public static SpeechType getType(String search) {
		for (SpeechType type : values()) {
			if (type.getName().equals(search)) {
				return type;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param player
	 *            where the message is send to
	 * @param npcid
	 *            the number of the npc
	 * @param questtype
	 *            the type of quest
	 * @param questid
	 *            the id of the quest
	 * @throws CloneNotSupportedException
	 */
	public static void constructSpeech(Player player, String questtype,
			int questid, String npcname, UUID npcuuid) throws CloneNotSupportedException {
		FancyMessage accept = new FancyMessage(ACCEPT.getMessage().replace(
				"%npcname%", npcname));
		accept.color(ChatColor.AQUA);
		accept.formattedTooltip(new FancyMessage(
				"You will accept the given request.").color(ChatColor.GREEN));
		accept.command(ACCEPT.getCommand() + " " + player.getName() + " "
				+ questtype + " " + questid + " " + npcname);

		FancyMessage decline = new FancyMessage(DECLINE.getMessage().replace(
				"%npcname%", npcname));
		decline.color(ChatColor.AQUA);
		decline.formattedTooltip(new FancyMessage(
				"You will decline the given request.").color(ChatColor.RED));
		decline.command(DECLINE.getCommand() + " " + player.getName() + " "
				+ questtype + " " + questid + " " + npcuuid.toString());
		SpeechCommand.addCache(player.getUniqueId(), questid);
		accept.send(player);
		decline.send(player);
	
	}
	protected static void depthSpeech(Player player, List<String> command, List<String> message, int node){
		List<FancyMessage> cases = new ArrayList<>();
		for(int i =0; i < command.size(); i++){
			FancyMessage temp =new FancyMessage(message.get(i));
			temp.color(ChatColor.GRAY);
			String cmd = command.get(i);
			cmd = cmd.replace("%player%", player.getName());
			cmd += " " + node;
			temp.command(cmd);
			cases.add(temp);
		}
		SpeechCommand.addCache(player.getUniqueId(), node);
		for(FancyMessage in : cases){
			in.send(player);
		}
	}
}
