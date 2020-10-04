package io.github.franiscoder.mca.entity.ai.brain.sensor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class SecondaryPOIS extends Sensor<MCAVillagerEntity> {
    public SecondaryPOIS() {
        super(40);
    }

    protected void sense(ServerWorld serverWorld, MCAVillagerEntity villagerEntity) {
        RegistryKey<World> registryKey = serverWorld.getRegistryKey();
        BlockPos blockPos = villagerEntity.getBlockPos();
        List<GlobalPos> list = Lists.newArrayList();

        for (int j = -4; j <= 4; ++j) {
            for (int k = -2; k <= 2; ++k) {
                for (int l = -4; l <= 4; ++l) {
                    BlockPos blockPos2 = blockPos.add(j, k, l);
                    if (villagerEntity.getVillagerData().getProfession().getSecondaryJobSites().contains(serverWorld.getBlockState(blockPos2).getBlock())) {
                        list.add(GlobalPos.create(registryKey, blockPos2));
                    }
                }
            }
        }

        Brain<?> brain = villagerEntity.getBrain();
        if (!list.isEmpty()) {
            brain.remember(MemoryModuleType.SECONDARY_JOB_SITE, list);
        } else {
            brain.forget(MemoryModuleType.SECONDARY_JOB_SITE);
        }

    }

    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.SECONDARY_JOB_SITE);
    }
}