package io.github.franiscoder.mca.init;

import io.github.franiscoder.mca.MinecraftComesAlive;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class MCAItems {
	public static final Item BABY_BOY = new Item(new Item.Settings().maxCount(1).rarity(Rarity.EPIC));
	public static final Item BABY_GIRL = new Item(new Item.Settings().maxCount(1).rarity(Rarity.EPIC));
	public static final Item ENGAGEMENT_RING = new Item(new Item.Settings().maxCount(1));
	public static final Item MATCHMAKERS_RING = new Item(new Item.Settings().maxCount(2));
	public static final Item WEDDING_RING = new Item(new Item.Settings().maxCount(1));
	
	public static void init() {
		Registry.register(Registry.ITEM, MinecraftComesAlive.id("baby_boy"), BABY_BOY);
		Registry.register(Registry.ITEM, MinecraftComesAlive.id("baby_girl"), BABY_GIRL);
		Registry.register(Registry.ITEM, MinecraftComesAlive.id("engagement_ring"), ENGAGEMENT_RING);
		Registry.register(Registry.ITEM, MinecraftComesAlive.id("matchmakers_ring"), MATCHMAKERS_RING);
		Registry.register(Registry.ITEM, MinecraftComesAlive.id("wedding_ring"), WEDDING_RING);
		
	}
}
