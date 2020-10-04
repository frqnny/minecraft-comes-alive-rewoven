package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;

import java.util.Optional;

public class GoToWorkTask extends Task<MCAVillagerEntity> {
    public GoToWorkTask() {
        super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleState.VALUE_PRESENT));
    }

    protected boolean shouldRun(ServerWorld serverWorld, MCAVillagerEntity villagerEntity) {
        BlockPos blockPos = villagerEntity.getBrain().getOptionalMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().getPos();
        return blockPos.isWithinDistance(villagerEntity.getPos(), 2.0D) || villagerEntity.isNatural();
    }

    protected void run(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        GlobalPos globalPos = villagerEntity.getBrain().getOptionalMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get();
        villagerEntity.getBrain().forget(MemoryModuleType.POTENTIAL_JOB_SITE);
        villagerEntity.getBrain().remember(MemoryModuleType.JOB_SITE, globalPos);
        serverWorld.sendEntityStatus(villagerEntity, (byte) 14);
        if (villagerEntity.getVillagerData().getProfession() == VillagerProfession.NONE) {
            MinecraftServer minecraftServer = serverWorld.getServer();
            Optional.ofNullable(minecraftServer.getWorld(globalPos.getDimension())).flatMap((serverWorldx) -> serverWorldx.getPointOfInterestStorage().getType(globalPos.getPos())).flatMap((pointOfInterestType) -> Registry.VILLAGER_PROFESSION.stream().filter((villagerProfession) -> villagerProfession.getWorkStation() == pointOfInterestType).findFirst()).ifPresent((villagerProfession) -> {
                villagerEntity.getVillagerData().setProfession(villagerProfession);
                villagerEntity.reinitializeBrain(serverWorld);
            });
        }
    }
}