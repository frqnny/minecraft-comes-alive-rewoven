package io.github.franiscoder.mca.util.enums;

public enum Age {
	Baby((byte) 0),
	Toddler((byte) 1),
	Kid((byte) 2),
	Teenager((byte) 3),
	Adult((byte) 4);
	
	final byte id;
	
	Age(byte id) {
		this.id = id;
	}
	
	public static Age fromId(byte id) {
		for (Age type : values()) {
			if (type.id == id) {
				return type;
			}
		}
		throw (new ArrayIndexOutOfBoundsException("Tried getting a non existant Age from byte id: " + id + "!"));
	}
	
	public byte getId() {
		return this.id;
	}
}
