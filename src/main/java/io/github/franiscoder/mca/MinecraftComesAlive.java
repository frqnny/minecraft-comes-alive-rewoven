package io.github.franiscoder.mca;

import io.github.franiscoder.mca.init.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class MinecraftComesAlive implements ModInitializer {
	public static final String MODID = "mca";
	
	public static Identifier id(String namespace) {
		return new Identifier(MODID, namespace);
	}
	
	@Override
	public void onInitialize() {
		MCAItems.init();
		MCACommands.init();
		MCAEvents.init();
		MCAVillagerProfessions.init();
		MCAEntities.init();
	}
}
