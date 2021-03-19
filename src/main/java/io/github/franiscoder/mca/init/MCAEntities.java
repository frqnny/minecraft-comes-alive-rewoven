package io.github.franiscoder.mca.init;

import io.github.franiscoder.mca.MinecraftComesAlive;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MCAEntities {
	public static EntityType<MCAVillagerEntity> MCA_VILLAGER;
	
	public static void init() {
		MCA_VILLAGER = register("villager",
				FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, MCAVillagerEntity::new)
						.dimensions(EntityDimensions.fixed(1.4F, 2.7F)).build()
		);
	}
	
	private static <T extends Entity> EntityType<T> register(String name, EntityType<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, MinecraftComesAlive.id(name), builder);
	}
}
