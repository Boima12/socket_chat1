package common;

import com.google.gson.Gson;

public class Message {
	public enum Type {
		TEXT, IMAGE, SYSTEM
	}

	private static final Gson gson = new Gson();
	public Type type;
	public String sender;
	public String content;

	// for text, or Base64 string for image 
	public Message(Type type, String sender, String content) {
		this.type = type;
		this.sender = sender;
		this.content = content;
	}

	/** Serialize to JSON */
	public String toJson() {
		return gson.toJson(this);
	}

	/** Deserialize from JSON */
	public static Message fromJson(String json) {
		return gson.fromJson(json, Message.class);
	}
}