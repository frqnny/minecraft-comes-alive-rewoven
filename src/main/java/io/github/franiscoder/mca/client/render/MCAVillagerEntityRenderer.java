package io.github.franiscoder.mca.client.render;

import io.github.franiscoder.mca.client.render.model.MCAVillageEntityModel;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import io.github.franiscoder.mca.util.IdentifierCache;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class MCAVillagerEntityRenderer extends MobEntityRenderer<MCAVillagerEntity, MCAVillageEntityModel<MCAVillagerEntity>> {
    protected final String texture;

    public MCAVillagerEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new MCAVillageEntityModel<>(0, false), 0.5F);
        if (entityRenderDispatcher.targetedEntity instanceof MCAVillagerEntity) {
            texture = ((MCAVillagerEntity) entityRenderDispatcher.targetedEntity).getTexture();
        } else {
            texture = "";
        }
    }

    @Override
    public Identifier getTexture(MCAVillagerEntity entity) {
        return IdentifierCache.getIdentifierFor(texture);
    }
}
