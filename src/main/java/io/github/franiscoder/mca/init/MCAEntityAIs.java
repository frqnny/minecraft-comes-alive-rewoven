package io.github.franiscoder.mca.init;

import io.github.franiscoder.mca.MinecraftComesAlive;
import io.github.franiscoder.mca.entity.ai.brain.sensor.SecondaryPOIS;
import io.github.franiscoder.mca.mixin.MixinMemoryModuleType;
import io.github.franiscoder.mca.mixin.MixinSensorType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.util.dynamic.GlobalPos;

public class MCAEntityAIs {
    public static MemoryModuleType<GlobalPos> test;
    public static SensorType<SecondaryPOIS> SECONDARY_POIS;


    public static void init() {
        test = MixinMemoryModuleType.invokeRegister("mca:test");
        SECONDARY_POIS = MixinSensorType.invokeRegister(MinecraftComesAlive.MODID + "secondary_pois", SecondaryPOIS::new);

    }
}
