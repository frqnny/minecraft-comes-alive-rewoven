package io.github.franiscoder.mca.util;

import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class TaskHelper {
    public static Stream<MCAVillagerEntity> streamSeenVillagers(MCAVillagerEntity villager, Predicate<MCAVillagerEntity> filter) {
        return villager.getBrain().getOptionalMemory(MemoryModuleType.MOBS).map((list) -> list.stream().filter((livingEntity) -> livingEntity instanceof MCAVillagerEntity && livingEntity != villager).map((livingEntity) -> (MCAVillagerEntity) livingEntity).filter(LivingEntity::isAlive).filter(filter)).orElseGet(Stream::empty);
    }
}
