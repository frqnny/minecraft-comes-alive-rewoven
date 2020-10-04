package io.github.franiscoder.mca;

import io.github.franiscoder.mca.client.render.MCAVillagerEntityRenderer;
import io.github.franiscoder.mca.init.MCAEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class MinecraftComesAliveClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(MCAEntities.MCA_VILLAGER, (dispatcher, context) -> new MCAVillagerEntityRenderer(dispatcher));
    }
}
