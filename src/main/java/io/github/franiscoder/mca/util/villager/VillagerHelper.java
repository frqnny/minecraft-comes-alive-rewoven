package io.github.franiscoder.mca.util.villager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import io.github.franiscoder.mca.init.MCAEntityAIs;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

public class VillagerHelper {

    //shoving this static shit here to clean up the entity class, then make these things clearer
    public static final Map<Item, Integer> ITEM_FOOD_VALUES = ImmutableMap.of(
            Items.BREAD, 4,
            Items.POTATO, 1,
            Items.CARROT, 1,
            Items.BEETROOT, 1
    );
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<MCAVillagerEntity, PointOfInterestType>> POINTS_OF_INTEREST = ImmutableMap.of(
            MemoryModuleType.HOME, (villagerEntity, pointOfInterestType) -> pointOfInterestType == PointOfInterestType.HOME,
            MemoryModuleType.JOB_SITE, (villagerEntity, pointOfInterestType) -> villagerEntity.getVillagerData().getProfession().getWorkStation() == pointOfInterestType,
            MemoryModuleType.POTENTIAL_JOB_SITE, (villagerEntity, pointOfInterestType) -> PointOfInterestType.IS_USED_BY_PROFESSION.test(pointOfInterestType),
            MemoryModuleType.MEETING_POINT, (villagerEntity, pointOfInterestType) -> pointOfInterestType == PointOfInterestType.MEETING
    );
    public static final Set<Item> GATHERABLE_ITEMS = ImmutableSet.of(
            Items.BREAD,
            Items.POTATO,
            Items.CARROT,
            Items.WHEAT,
            Items.WHEAT_SEEDS,
            Items.BEETROOT,
            Items.BEETROOT_SEEDS
    );
    public static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(
            MemoryModuleType.HOME,
            MemoryModuleType.JOB_SITE,
            MemoryModuleType.POTENTIAL_JOB_SITE,
            MemoryModuleType.MEETING_POINT,
            MemoryModuleType.MOBS,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.VISIBLE_VILLAGER_BABIES,
            MemoryModuleType.NEAREST_PLAYERS,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.PATH,
            MemoryModuleType.DOORS_TO_CLOSE,
            MemoryModuleType.NEAREST_BED,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.NEAREST_HOSTILE,
            MemoryModuleType.SECONDARY_JOB_SITE,
            MemoryModuleType.HIDING_PLACE,
            MemoryModuleType.HEARD_BELL_TIME,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.LAST_SLEPT,
            MemoryModuleType.LAST_WOKEN,
            MemoryModuleType.LAST_WORKED_AT_POI,
            MemoryModuleType.GOLEM_DETECTED_RECENTLY
    );
    public static final ImmutableList<SensorType<? extends Sensor<? super MCAVillagerEntity>>> SENSORS = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.VILLAGER_HOSTILES,
            SensorType.NEAREST_PLAYERS,
            SensorType.VILLAGER_BABIES,
            MCAEntityAIs.SECONDARY_POIS,
            SensorType.NEAREST_ITEMS,
            SensorType.NEAREST_BED,
            SensorType.HURT_BY,
            SensorType.GOLEM_DETECTED
    );

}
