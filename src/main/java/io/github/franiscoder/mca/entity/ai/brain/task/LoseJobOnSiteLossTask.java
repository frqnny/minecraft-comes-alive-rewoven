package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import io.github.franiscoder.mca.components.data.MCAVillagerData;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerProfession;

public class LoseJobOnSiteLossTask extends Task<MCAVillagerEntity> {
    public LoseJobOnSiteLossTask() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_ABSENT));
    }

    protected boolean shouldRun(ServerWorld serverWorld, MCAVillagerEntity villagerEntity) {
        MCAVillagerData villagerData = villagerEntity.getVillagerData();
        return villagerData.getProfession() != VillagerProfession.NONE && villagerData.getProfession() != VillagerProfession.NITWIT && villagerEntity.getExperience() == 0 && villagerData.getLevel() <= 1;
    }

    protected void run(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        villagerEntity.getVillagerData().setProfession(VillagerProfession.NONE);
        villagerEntity.reinitializeBrain(serverWorld);
    }
}
