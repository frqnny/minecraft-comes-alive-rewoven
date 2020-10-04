package io.github.franiscoder.mca.init;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;

public class MCAEvents {
    public static void init() {
        ServerEntityEvents.ENTITY_LOAD.register(((entity, serverWorld) -> {
            if (entity instanceof VillagerEntity) {
                //we need to make the brain think it ticks so it is able to claim a workspace in the FindPOintOfInterestTask
                ((VillagerEntity) entity).getBrain().tick(serverWorld, (VillagerEntity) entity);
                //ok now we go get its data
                VillagerData data = ((VillagerEntity) entity).getVillagerData();
                VillagerProfession profession = data.getProfession();
                //and now we figure out how to spawn one in
                //serverWorld.pointof

                entity.remove();
                //VillagerFactory to help us get a new random villager
            }
        }));
    }
}
