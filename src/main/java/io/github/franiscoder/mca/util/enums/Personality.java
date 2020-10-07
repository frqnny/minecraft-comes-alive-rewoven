package io.github.franiscoder.mca.util.enums;

import lombok.Getter;

public enum Personality {
	ATHLETIC(Mentality.PLAYFUL, (byte) 0),
	CONFIDENT(Mentality.SERIOUS, (byte) 1),
	STRONG(Mentality.SERIOUS, (byte) 2),
	FRIENDLY(Mentality.STANDARD, (byte) 3),
	CURIOUS(Mentality.SERIOUS, (byte) 4),
	PEACEFUL(Mentality.STANDARD, (byte) 5),
	FLIRTY(Mentality.PLAYFUL, (byte) 6),
	WITTY(Mentality.PLAYFUL, (byte) 7),
	SENSITIVE(Mentality.STANDARD, (byte) 8),
	GREEDY(Mentality.SERIOUS, (byte) 9),
	STUBBORN(Mentality.SERIOUS, (byte) 10),
	Odd(Mentality.PLAYFUL, (byte) 11);
	
	
	@Getter
	public final Mentality group;
	@Getter
	public final byte id;
	
	Personality(Mentality group, byte id) {
		this.group = group;
		this.id = id;
	}
	
	public static Personality fromId(byte id) {
		for (Personality type : values()) {
			if (type.id == id) {
				return type;
			}
		}
		throw (new ArrayIndexOutOfBoundsException("Tried getting a non existant Personality from byte id: " + id + "!"));
	}
}
