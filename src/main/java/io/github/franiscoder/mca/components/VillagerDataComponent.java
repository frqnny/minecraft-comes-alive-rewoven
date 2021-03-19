package io.github.franiscoder.mca.components;

import io.github.franiscoder.mca.components.data.MCAVillagerData;
import io.github.franiscoder.mca.components.interfaces.IVillagerDataComponent;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import io.github.franiscoder.mca.util.enums.*;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class VillagerDataComponent implements IVillagerDataComponent, EntitySyncedComponent {
	public final MCAVillagerEntity villager;
	private final MCAVillagerData villagerData = new MCAVillagerData();
	
	public VillagerDataComponent(MCAVillagerEntity v) {
		this.villager = v;
	}
	
	@Override
	public MCAVillagerData get() {
		return villagerData;
	}
	
	@Override
	public @NotNull Entity getEntity() {
		return villager;
	}
	
	@Override
	public void fromTag(CompoundTag compoundTag) {
		villagerData.setParentType(ParentType.fromId(compoundTag.getByte("ParentType")));
		switch (villagerData.getParentType()) {
			case VILLAGER:
				villagerData.setParentName1(compoundTag.getString("Parent1"));
				villagerData.setParentName2(compoundTag.getString("Parent2"));
				break;
			case PLAYER:
				villagerData.setPlayerParent1(compoundTag.getUuid("PlayerParent1"));
				villagerData.setPlayerParent2(compoundTag.getUuid("PlayerParent2"));
				break;
			case BOTH:
				villagerData.setBoth_player_name(compoundTag.getUuid("PlayerParent"));
				villagerData.setBoth_villager_name(compoundTag.getString("VillagerParent"));
		}
		villagerData.setName(compoundTag.getString("Name"));
		villagerData.setTexture(compoundTag.getString("Texture"));
		villagerData.setGender(Gender.fromId(compoundTag.getByte("Gender")));
		villagerData.setAge(Age.fromId(compoundTag.getByte("Age")));
		villagerData.setChore(Chore.fromId(compoundTag.getByte("Chore")));
		villagerData.setMentality(Mentality.fromId(compoundTag.getByte("Mentality")));
		villagerData.setPersonality(Personality.fromId(compoundTag.getByte("Personality")));
		villagerData.setMood(Mood.fromId(compoundTag.getByte("Mood")));
		
		if (compoundTag.contains("PlayerHistory")) {
			ListTag playerHistory = compoundTag.getList("PlayerHistory", 10);
			for (Tag tag : playerHistory) {
				villagerData.getPlayerHistoryMap().add((CompoundTag) tag);
			}
		}
		villagerData.setBlockPos(compoundTag.getCompound("BlockPosMemory"));
		
		villagerData.setSleeping(compoundTag.getBoolean("Sleeping"));
		villagerData.setProfession(Registry.VILLAGER_PROFESSION.get(new Identifier(compoundTag.getString("ProfessionId"))));
		villagerData.setLevel(compoundTag.getInt("Level"));
	}
	
	@Override
	public @NotNull CompoundTag toTag(CompoundTag compoundTag) {
		compoundTag.putByte("ParentType", villagerData.getParentType().getId());
		switch (villagerData.getParentType()) {
			case VILLAGER:
				compoundTag.putString("Parent1", villagerData.getParentName1());
				compoundTag.putString("parent2", villagerData.getParentName2());
				break;
			case PLAYER:
				compoundTag.putUuid("PlayerParent1", villagerData.getPlayerParent1());
				compoundTag.putUuid("PlayerParent2", villagerData.getPlayerParent2());
				break;
			case BOTH:
				compoundTag.putUuid("PlayerParent", villagerData.getBoth_player_name());
				compoundTag.putString("VillagerParent", villagerData.getBoth_villager_name());
				break;
			default:
				break;
		}
		
		compoundTag.putString("Name", villagerData.getName());
		compoundTag.putString("Texture", villagerData.getTexture());
		compoundTag.putByte("Gender", villagerData.getGender().getId());
		compoundTag.putByte("Age", villagerData.getAge().getId());
		compoundTag.putByte("Chore", villagerData.getChore().getId());
		compoundTag.putByte("Mentality", villagerData.getMentality().getId());
		compoundTag.putByte("Personality", villagerData.getPersonality().getId());
		compoundTag.putByte("Mood", villagerData.getMood().getId());
		
		
		ListTag playerHistory = new ListTag();
		playerHistory.addAll(villagerData.getPlayerHistoryMap());
		compoundTag.put("PlayerHistory", playerHistory);
		
		compoundTag.put("BlockPosMemory", villagerData.getBlockPos());
		
		compoundTag.putBoolean("Sleeping", villagerData.isSleeping());
		compoundTag.putString("ProfessionId", Registry.VILLAGER_PROFESSION.getId(villagerData.getProfession()).toString());
		compoundTag.putInt("Level", villagerData.getLevel());
		return compoundTag;
	}
}
