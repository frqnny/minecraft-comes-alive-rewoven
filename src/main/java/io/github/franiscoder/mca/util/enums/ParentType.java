package io.github.franiscoder.mca.util.enums;

public enum ParentType {
    NONE((byte) 0),
    VILLAGER((byte) 1),
    PLAYER((byte) 2),
    BOTH((byte) 3);

    final byte id;

    ParentType(byte id) {
        this.id = id;
    }

    public static ParentType fromId(byte id) {
        for (ParentType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw (new ArrayIndexOutOfBoundsException("Tried getting a non existant ParentType from byte id: " + id + "!"));
    }

    public byte getId() {
        return id;
    }
}
