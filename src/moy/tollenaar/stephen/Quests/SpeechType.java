package moy.tollenaar.stephen.Quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import lib.fanciful.main.FancyMessage;

public enum SpeechType {
	ACCEPT("I will help %npcname%. ", "accept", "/speechtrait accept "), DECLINE(
			"Sorry %npcname%. I'll look into it another time.", "decline",
			"/speechtrait decline ");

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
			int questid, String npcname) throws CloneNotSupportedException {
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
				+ questtype + " " + questid + " " + npcname);

		accept.send(player);
		decline.send(player);

	}
}
