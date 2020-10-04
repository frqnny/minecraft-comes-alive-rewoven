package io.github.franiscoder.mca.util.enums;

public enum DialogueType {
    PLAYER_CHILD((byte) 0),
    CHILD((byte) 1),
    NORMAL((byte) 2),
    SPOUSE((byte) 3);

    public final byte id;

    DialogueType(byte id) {
        this.id = id;
    }

    public byte getId() {
        return this.id;
    }
}
