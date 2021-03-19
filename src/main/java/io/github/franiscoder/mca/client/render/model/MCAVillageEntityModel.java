package io.github.franiscoder.mca.client.render.model;

import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class MCAVillageEntityModel<T extends MCAVillagerEntity> extends PlayerEntityModel<T> {
	public final ModelPart breasts;
	
	public MCAVillageEntityModel(float scale, boolean thinArms) {
		super(scale, thinArms);
		breasts = new ModelPart(this, 18, 21);
		breasts.addCuboid(-3F, 0F, -1F, 6, 3, 3);
		breasts.setPivot(0F, 3.5F, -3F);
		breasts.setTextureSize(64, 64);
		breasts.mirror = true;
	}
	
	@Override
	public void animateModel(T villager, float f, float g, float h) {
		super.animateModel(villager, f, g, h);
		
		//TODO check for gender &
	}
	
	@Override
	public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
		super.setAngles(livingEntity, f, g, h, i, j);
		
	}
	
}
