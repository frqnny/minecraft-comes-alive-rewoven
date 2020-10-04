package io.github.franiscoder.mca.components;

import io.github.franiscoder.mca.components.data.PlayerData;
import io.github.franiscoder.mca.components.interfaces.IPlayerDataComponent;
import io.github.franiscoder.mca.init.MCAComponents;
import io.github.franiscoder.mca.util.enums.SpouseType;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class PlayerDataComponent implements IPlayerDataComponent, EntitySyncedComponent {
    public final ServerPlayerEntity playerEntity;
    private final PlayerData playerData = new PlayerData();


    public PlayerDataComponent(PlayerEntity entity) {
        this.playerEntity = (ServerPlayerEntity) entity;
    }

    @Override
    public PlayerData get() {
        return this.playerData;
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        if (compoundTag.getBoolean("isMarried")) {
            if (compoundTag.getByte("SpouseType") == SpouseType.PLAYER.typeId) {
                this.playerData.spouseType = SpouseType.PLAYER;
                this.playerData.spousePlayer = compoundTag.getUuid("SpousePlayer");
            } else {
                this.playerData.spouseType = SpouseType.VILLAGER;
                this.playerData.spouseName = compoundTag.getString("SpouseName");
            }

        } else {
            if (compoundTag.contains("mR")) {
                ListTag mRList = compoundTag.getList("mR", 11);
                for (Tag tag : mRList) {
                    this.playerData.marriageRequests.add(NbtHelper.toUuid(tag));
                }
            }
            ListTag oRList = compoundTag.getList("oR", 11);
            if (compoundTag.contains("oR")) {
                for (Tag tag : oRList) {
                    this.playerData.marriageRequests.add(NbtHelper.toUuid(tag));
                }
            }
        }


    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        compoundTag.putBoolean("isMarried", playerData.isMarried);
        if (playerData.isMarried) {
            if (playerData.spouseType == SpouseType.PLAYER) {
                compoundTag.putByte("SpouseType", SpouseType.PLAYER.typeId);
                compoundTag.putUuid("SpousePlayer", this.playerData.spousePlayer);
            } else {
                compoundTag.putByte("SpouseType", SpouseType.VILLAGER.typeId);
                compoundTag.putString("SpouseName", this.playerData.spouseName);
            }
        } else {
            if (!playerData.marriageRequests.isEmpty()) {
                ListTag marriageRequests = new ListTag();
                for (UUID uuid : playerData.marriageRequests) {
                    marriageRequests.add(NbtHelper.fromUuid(uuid));
                }
                compoundTag.put("mR", marriageRequests);
            }
            if (!playerData.outgoingRequests.isEmpty()) {
                ListTag outgoingRequests = new ListTag();
                for (UUID uuid : playerData.outgoingRequests) {
                    outgoingRequests.add(NbtHelper.fromUuid(uuid));
                }
                compoundTag.put("oR", outgoingRequests);
            }
        }


        return compoundTag;
    }

    @Override
    public Entity getEntity() {
        return this.playerEntity;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return MCAComponents.PLAYER_DATA_COMPONENT;
    }
}
