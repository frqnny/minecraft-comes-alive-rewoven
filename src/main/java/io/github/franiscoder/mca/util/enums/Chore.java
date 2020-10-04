package io.github.franiscoder.mca.util.enums;

import lombok.Getter;

public enum Chore {
    Farming((byte) 0),
    TreeCutting((byte) 1),
    Mining((byte) 2),
    Fishing((byte) 3),
    None((byte) 4);

    @Getter
    public final byte id;

    Chore(byte id) {
        this.id = id;
    }

    public static Chore fromId(byte id) {
        for (Chore type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw (new ArrayIndexOutOfBoundsException("Tried getting a non existant Chore from byte id: " + id + "!"));
    }
}
