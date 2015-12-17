package moy.tollenaar.stephen.Speech;

import java.util.HashMap;

public class SpeechTrait {
	private static int UNIQUE_ID = 0;
	private static HashMap<Integer, SpeechTrait> allSpeeches = new HashMap<>();

	private final int id;
	private String messages; // message player will receive
	private int depth; // node where the trait points to
	private int questType;
	private int questnumber;

	public SpeechTrait() {
		this(UNIQUE_ID);
	}

	public SpeechTrait(int number, int type) {
		this();
		this.questType = type;
		this.questnumber = number;
	}
	
	public SpeechTrait(int number, int type, int id){
		this(id);
		this.questnumber = number;
		this.questType = type;
	}

	public SpeechTrait(int id) {
		this.id = id;
		if (id > UNIQUE_ID) {
			UNIQUE_ID = id;
		}
		allSpeeches.put(id, this);
		while (allSpeeches.get(UNIQUE_ID) != null) {
			UNIQUE_ID++;
		}
		this.messages = "hmp";
		this.depth = -2;
		this.questnumber = -4;
		this.questType = -5;
	}

	public String getMessage() {
		return messages;
	}

	protected String getCommand() {
		return "/speechtrait node %player% " + depth + " ";
	}

	public static SpeechTrait getSpeech(int node) {
		return allSpeeches.get(node);
	}

	public int getDepth() {
		return depth;
	}

	public static void removeTrait(int node) {
		allSpeeches.remove(node);
	}

	public int getId() {
		return id;
	}

	public void setMessage(String message) {
		this.messages = message;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getQuestType() {
		return questType;
	}

	public void setQuestType(int questType) {
		this.questType = questType;
	}

	public int getQuestnumber() {
		return questnumber;
	}

	public void setQuestnumber(int questnumber) {
		this.questnumber = questnumber;
	}

}
