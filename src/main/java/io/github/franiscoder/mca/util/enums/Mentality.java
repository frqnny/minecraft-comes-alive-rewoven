package io.github.franiscoder.mca.util.enums;

import lombok.Getter;

public enum Mentality {
    PLAYFUL((byte) 0),
    SERIOUS((byte) 1),
    STANDARD((byte) 2);

    @Getter
    public final byte id;

    Mentality(byte id) {
        this.id = id;
    }

    public static Mentality fromId(byte id) {
        for (Mentality type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw (new ArrayIndexOutOfBoundsException("Tried getting a non existant Mentality from byte id: " + id + "!"));
    }
}
