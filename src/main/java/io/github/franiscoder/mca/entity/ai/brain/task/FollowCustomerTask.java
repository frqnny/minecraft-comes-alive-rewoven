package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class FollowCustomerTask extends Task<MCAVillagerEntity> {
    private final float speed;

    public FollowCustomerTask(float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED), 2147483647);
        this.speed = speed;
    }

    protected boolean shouldRun(ServerWorld serverWorld, MCAVillagerEntity villagerEntity) {
        PlayerEntity playerEntity = villagerEntity.getCurrentCustomer();
        return villagerEntity.isAlive() && playerEntity != null && !villagerEntity.isTouchingWater() && !villagerEntity.velocityModified && villagerEntity.squaredDistanceTo(playerEntity) <= 16.0D && playerEntity.currentScreenHandler != null;
    }

    protected boolean shouldKeepRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        return this.shouldRun(serverWorld, villagerEntity);
    }

    protected void run(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        this.update(villagerEntity);
    }

    protected void finishRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        Brain<?> brain = villagerEntity.getBrain();
        brain.forget(MemoryModuleType.WALK_TARGET);
        brain.forget(MemoryModuleType.LOOK_TARGET);
    }

    protected void keepRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        this.update(villagerEntity);
    }

    protected boolean isTimeLimitExceeded(long time) {
        return false;
    }

    private void update(MCAVillagerEntity villager) {
        Brain<?> brain = villager.getBrain();
        brain.remember(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityLookTarget(villager.getCurrentCustomer(), false), this.speed, 2));
        brain.remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(villager.getCurrentCustomer(), true));
    }
}