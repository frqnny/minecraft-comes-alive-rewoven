package io.github.franiscoder.mca.init;

import io.github.franiscoder.mca.MinecraftComesAlive;
import io.github.franiscoder.mca.components.PlayerDataComponent;
import io.github.franiscoder.mca.components.VillagerDataComponent;
import io.github.franiscoder.mca.components.interfaces.IPlayerDataComponent;
import io.github.franiscoder.mca.components.interfaces.IVillagerDataComponent;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.minecraft.entity.player.PlayerEntity;

public class MCAComponents {
    public static final ComponentType<IPlayerDataComponent> PLAYER_DATA_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(MinecraftComesAlive.id("playerdata"), IPlayerDataComponent.class);
    public static final ComponentType<IVillagerDataComponent> VILLAGE_DATE_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(MinecraftComesAlive.id("villagerdata"), IVillagerDataComponent.class);

    public static void init() {
        EntityComponentCallback.event(PlayerEntity.class).register((player, components) -> components.put(PLAYER_DATA_COMPONENT, new PlayerDataComponent(player)));
        EntityComponentCallback.event(MCAVillagerEntity.class).register((villager, components) -> components.put(VILLAGE_DATE_COMPONENT, new VillagerDataComponent(villager)));
    }
}
