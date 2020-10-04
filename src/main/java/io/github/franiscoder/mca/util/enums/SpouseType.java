package io.github.franiscoder.mca.util.enums;

public enum SpouseType {
    PLAYER((byte) 0),
    VILLAGER((byte) 1);

    public final byte typeId;

    SpouseType(byte typeId) {
        this.typeId = typeId;
    }
}
