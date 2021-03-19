package io.github.franiscoder.mca.util.enums;

public enum Gender {
	MALE((byte) 0),
	FEMALE((byte) 1);
	
	
	final byte id;
	
	Gender(byte b) {
		this.id = b;
	}
	
	public static Gender fromId(byte id) {
		for (Gender type : values()) {
			if (type.id == id) {
				return type;
			}
		}
		throw (new ArrayIndexOutOfBoundsException("Tried getting a non existant Gender from byte id: " + id + "!"));
	}
	
	public byte getId() {
		return this.id;
	}
}
