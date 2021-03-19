package io.github.franiscoder.mca.util;

import io.github.franiscoder.mca.MinecraftComesAlive;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class IdentifierCache {
	private static final Map<String, Identifier> cache = new HashMap<>(40);
	
	public static Identifier getIdentifierFor(String location) {
		if (!cache.containsKey(location)) {
			Identifier id = MinecraftComesAlive.id(location);
			cache.put(location, id);
		}
		return cache.get(location);
	}
}
