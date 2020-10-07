package io.github.franiscoder.mca.util.enums;

import lombok.Getter;

public enum Mood {
	DEPRESSED(-3, Mentality.STANDARD, (byte) 0),
	SAD(-2, Mentality.STANDARD, (byte) 1),
	UNHAPPY(-1, Mentality.STANDARD, (byte) 2),
	PASSIVE(0, Mentality.STANDARD, (byte) 3),
	FINE(1, Mentality.STANDARD, (byte) 4),
	HAPPY(2, Mentality.STANDARD, (byte) 5),
	OVERJOYED(3, Mentality.STANDARD, (byte) 6),
	
	BORED_TO_TEARS(-3, Mentality.PLAYFUL, (byte) 7),
	BORED(-2, Mentality.PLAYFUL, (byte) 8),
	UNINTERESTED(-1, Mentality.PLAYFUL, (byte) 9),
	SILLY(1, Mentality.PLAYFUL, (byte) 10),
	GIGGLY(2, Mentality.PLAYFUL, (byte) 11),
	ENTERTAINED(3, Mentality.PLAYFUL, (byte) 12),
	
	INFURIATED(-3, Mentality.SERIOUS, (byte) 13),
	ANGRY(-2, Mentality.SERIOUS, (byte) 14),
	ANNOYED(-1, Mentality.SERIOUS, (byte) 15),
	INTERESTED(1, Mentality.SERIOUS, (byte) 16),
	TALKATIVE(2, Mentality.SERIOUS, (byte) 17),
	PLEASED(3, Mentality.SERIOUS, (byte) 18);
	
	@Getter
	private final int level;
	@Getter
	private final Mentality mentality;
	@Getter
	private final byte id;
	
	Mood(int level, Mentality moodGroup, byte id) {
		this.level = level;
		this.mentality = moodGroup;
		this.id = id;
	}
	
	public static Mood fromId(byte id) {
		for (Mood type : values()) {
			if (type.id == id) {
				return type;
			}
		}
		throw (new ArrayIndexOutOfBoundsException("Tried getting a non existant Mood from byte id: " + id + "!"));
	}
	
}
