package io.github.franiscoder.mca;

import io.github.franiscoder.mca.init.MCAComponents;
import net.fabricmc.api.DedicatedServerModInitializer;

public class MinecraftComesAliveServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		MCAComponents.init();
	}
}
