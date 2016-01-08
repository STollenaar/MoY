package moy.tollenaar.stephen.Speech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import moy.tollenaar.stephen.MistsOfYsir.MoY;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SpeechNode {
	private static int UNIQUE_ID = 0;
	private static HashMap<Integer, SpeechNode> nodes = new HashMap<>();
	private MoY plugin;

	private final int id;
	private Set<Integer> traits = new HashSet<>();
	private String npcResponse;

	public SpeechNode(MoY instance) {
		this(UNIQUE_ID, instance);
	}

	public SpeechNode(int id, MoY instance) {
		this.id = id;
		if (id > UNIQUE_ID) {
			UNIQUE_ID = id;
		}
		nodes.put(id, this);
		while (nodes.get(UNIQUE_ID) != null) {
			UNIQUE_ID++;
		}
		this.npcResponse = "Hmp";
		this.plugin = instance;
	}

	public void addTrait(SpeechTrait trait) {
		traits.add(trait.getId());
	}

	public Set<Integer> getTraits() {
		return traits;
	}

	public void removeTrait(int node) {
		SpeechTrait.removeTrait(node);
		traits.remove(node);
	}

	public static void removeNode(int node) {
		SpeechNode n = nodes.get(node);
		for(int t : n.getTraits()){
			SpeechTrait.removeTrait(t);
		}
		n.getTraits().clear();
		nodes.remove(node);
	}

	public void constructNode(Player player) {
			@SuppressWarnings("unchecked")
			List<String> meta = (List<String>) player.getMetadata("Speech").get(0).value();
			UUID npcuuid = UUID.fromString(meta.get(0));
			player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD
				+ plugin.getNPCHandler().getNPCByUUID(npcuuid).getName()
				+ ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA + npcResponse);
		List<String> messages = new ArrayList<String>();
		List<String> commands = new ArrayList<String>();
		for (int ids : traits) {
			SpeechTrait in = SpeechTrait.getSpeech(ids);
			messages.add(in.getMessage());
			String command = in.getCommand();
			if(command.contains("-1") || command.contains("-2")){
				if(command.contains("-1")){
					command = command.replace("node", "accept");
					command = command.replace("-1", Integer.toString(in.getQuestnumber()));
				}else{
					command = command.replace("node", "decline");
					
				}
			}
			command += npcuuid.toString() + " " + in.getQuestType();
			commands.add(command);
		}
		SpeechType.depthSpeech(player, commands, messages, id);
	}

	public static SpeechNode getNode(int id) {
		return nodes.get(id);
	}

	public void setResponse(String response) {
		this.npcResponse = response;
	}

	public String getResponse() {
		return npcResponse;
	}

	public int getId() {
		return id;
	}
}
