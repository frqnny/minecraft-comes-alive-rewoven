package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import io.github.franiscoder.mca.util.TaskHelper;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class WorkStationCompetitionTask extends Task<MCAVillagerEntity> {
    final VillagerProfession profession;

    public WorkStationCompetitionTask(VillagerProfession profession) {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.MOBS, MemoryModuleState.VALUE_PRESENT));
        this.profession = profession;
    }

    private static MCAVillagerEntity keepJobSiteForMoreExperiencedVillager(MCAVillagerEntity first, MCAVillagerEntity second) {
        MCAVillagerEntity villagerEntity3;
        MCAVillagerEntity villagerEntity4;
        if (first.getExperience() > second.getExperience()) {
            villagerEntity3 = first;
            villagerEntity4 = second;
        } else {
            villagerEntity3 = second;
            villagerEntity4 = first;
        }

        villagerEntity4.getBrain().forget(MemoryModuleType.JOB_SITE);
        return villagerEntity3;
    }

    protected void run(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        GlobalPos globalPos = villagerEntity.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).get();
        serverWorld.getPointOfInterestStorage().getType(globalPos.getPos()).ifPresent((pointOfInterestType) -> TaskHelper.streamSeenVillagers(villagerEntity, (villagerEntityx) -> this.isUsingWorkStationAt(globalPos, pointOfInterestType, villagerEntityx)).reduce(villagerEntity, WorkStationCompetitionTask::keepJobSiteForMoreExperiencedVillager));
    }

    private boolean isUsingWorkStationAt(GlobalPos pos, PointOfInterestType poiType, MCAVillagerEntity villager) {
        return this.hasJobSite(villager) && pos.equals(villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).get()) && this.isCompletedWorkStation(poiType, villager.getVillagerData().getProfession());
    }

    private boolean isCompletedWorkStation(PointOfInterestType poiType, VillagerProfession profession) {
        return profession.getWorkStation().getCompletionCondition().test(poiType);
    }

    private boolean hasJobSite(MCAVillagerEntity villager) {
        return villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).isPresent();
    }
}