package moy.tollenaar.stephen.Speech;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import moy.tollenaar.stephen.Commands.SpeechCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
	public static void constructSpeech(Player player, String questtype, int questid, String npcname, UUID npcuuid)
			throws CloneNotSupportedException {
		TextComponent accept = new TextComponent(ACCEPT.getMessage().replace("%npcname%", npcname));
		accept.setColor(ChatColor.AQUA);

		TextComponent t1 = new TextComponent("You will accept the given request.");
		t1.setColor(ChatColor.GREEN);
		accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { t1 }));
		accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
				ACCEPT.getCommand() + " " + player.getName() + " " + questtype + " " + questid + " " + npcname));

		TextComponent decline = new TextComponent(DECLINE.getMessage().replace("%npcname%", npcname));
		decline.setColor(ChatColor.AQUA);

		TextComponent t2 = new TextComponent("You will decline the given request.");
		t2.setColor(ChatColor.RED);
		decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { t2 }));

		decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, DECLINE.getCommand() + " "
				+ player.getName() + " " + questtype + " " + questid + " " + npcuuid.toString()));

		SpeechCommand.addCache(player.getUniqueId(), questid);
		player.spigot().sendMessage(accept);
		player.spigot().sendMessage(decline);

	}

	protected static void depthSpeech(Player player, List<String> command, List<String> message, int node) {
		List<TextComponent> cases = new ArrayList<>();
		for (int i = 0; i < command.size(); i++) {
			TextComponent temp = new TextComponent(message.get(i));
			temp.setColor(ChatColor.GRAY);
			String cmd = command.get(i);
			cmd = cmd.replace("%player%", player.getName());
			cmd += " " + node;
			temp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			cases.add(temp);
		}
		SpeechCommand.addCache(player.getUniqueId(), node);
		for (TextComponent in : cases) {
			player.spigot().sendMessage(in);
		}
	}
}
