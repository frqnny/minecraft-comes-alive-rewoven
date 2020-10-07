package io.github.franiscoder.mca.components.data;

import io.github.franiscoder.mca.util.enums.*;
import lombok.Data;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.village.VillagerProfession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Contains Parent data, basic data,
 */
public @Data
class MCAVillagerData {
	private static final int[] LEVEL_BASE_EXPERIENCE = new int[]{0, 10, 70, 150, 250};
	
	private final ParentType parentType = ParentType.NONE;
	
	private final String parentName1 = "";
	private final String parentName2 = "";
	
	private UUID playerParent1;
	private UUID playerParent2;
	
	private final String both_villager_name = "";
	private UUID both_player_name;
	
	private final String name = "";
	private final String texture = "";
	private Gender gender;
	private Age age;
	private final Chore chore = Chore.None;
	private Mentality mentality;
	private Personality personality;
	private Mood mood;
	private final List<CompoundTag> playerHistoryMap = new ArrayList<>(5);
	private final CompoundTag blockPos = new CompoundTag();
	
	private final boolean sleeping = false;
	private VillagerProfession profession;
	private final int level = 0;
	
	
}
