package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.PanicTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

public class StopPanickingTask extends Task<MCAVillagerEntity> {
    public StopPanickingTask() {
        super(ImmutableMap.of());
    }

    private static boolean wasHurtByNearbyEntity(MCAVillagerEntity entity) {
        return entity.getBrain().getOptionalMemory(MemoryModuleType.HURT_BY_ENTITY).filter((livingEntity) -> livingEntity.squaredDistanceTo(entity) <= 36.0D).isPresent();
    }

    protected void run(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        boolean bl = PanicTask.wasHurt(villagerEntity) || PanicTask.isHostileNearby(villagerEntity) || wasHurtByNearbyEntity(villagerEntity);
        if (!bl) {
            villagerEntity.getBrain().forget(MemoryModuleType.HURT_BY);
            villagerEntity.getBrain().forget(MemoryModuleType.HURT_BY_ENTITY);
            villagerEntity.getBrain().refreshActivities(serverWorld.getTimeOfDay(), serverWorld.getTime());
        }

    }
}
