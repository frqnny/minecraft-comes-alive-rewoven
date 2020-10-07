package io.github.franiscoder.mca.components.data;

import io.github.franiscoder.mca.util.enums.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.village.VillagerProfession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Contains Parent data, basic data,
 */
public class MCAVillagerData {
	private static final int[] LEVEL_BASE_EXPERIENCE = new int[]{0, 10, 70, 150, 250};
	
	private ParentType parentType = ParentType.NONE;
	
	private String parentName1 = "";
	private String parentName2 = "";
	private String both_villager_name = "";
	private String name = "";
	private String texture = "";
	private Chore chore = Chore.None;
	private List<CompoundTag> playerHistoryMap = new ArrayList<>(5);
	private CompoundTag blockPos = new CompoundTag();
	private boolean sleeping = false;
	private UUID playerParent1;
	private UUID playerParent2;
	private UUID both_player_name;
	private Gender gender;
	private Age age;
	private Mentality mentality;
	private Personality personality;
	private Mood mood;
	private VillagerProfession profession;
	private int level = 0;
	
	
	public VillagerProfession getProfession() {
		return this.profession;
	}
	
	public void setProfession(VillagerProfession profession) {
		this.profession = profession;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTexture() {
		return this.texture;
	}
	
	public void setTexture(String texture) {
		this.texture = texture;
	}
	
	public Gender getGender() {
		return this.gender;
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public Mentality getMentality() {
		return this.mentality;
	}
	
	public void setMentality(Mentality mentality) {
		this.mentality = mentality;
	}
	
	public Personality getPersonality() {
		return this.personality;
	}
	
	public void setPersonality(Personality personality) {
		this.personality = personality;
	}
	
	public ParentType getParentType() {
		return parentType;
	}
	
	public void setParentType(ParentType parentType) {
		this.parentType = parentType;
	}
	
	public String getParentName1() {
		return this.parentName1;
	}
	
	public void setParentName1(String parentName1) {
		this.parentName1 = parentName1;
	}
	
	public String getParentName2() {
		return this.parentName2;
	}
	
	public void setParentName2(String parentName2) {
		this.parentName2 = parentName2;
	}
	
	public Chore getChore() {
		return this.chore;
	}
	
	public void setChore(Chore chore) {
		this.chore = chore;
	}
	
	public Mood getMood() {
		return this.mood;
	}
	
	public void setMood(Mood mood) {
		this.mood = mood;
	}
	
	public List<CompoundTag> getPlayerHistoryMap() {
		return this.playerHistoryMap;
	}
	
	public void setPlayerHistoryMap(List<CompoundTag> playerHistoryMap) {
		this.playerHistoryMap = playerHistoryMap;
	}
	
	public CompoundTag getBlockPos() {
		return this.blockPos;
	}
	
	public void setBlockPos(CompoundTag blockPos) {
		this.blockPos = blockPos;
	}
	
	public boolean isSleeping() {
		return this.sleeping;
	}
	
	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}
	
	public String getBoth_villager_name() {
		return both_villager_name;
	}
	
	public void setBoth_villager_name(String both_villager_name) {
		this.both_villager_name = both_villager_name;
	}
	
	public UUID getPlayerParent1() {
		return playerParent1;
	}
	
	public void setPlayerParent1(UUID playerParent1) {
		this.playerParent1 = playerParent1;
	}
	
	public UUID getPlayerParent2() {
		return playerParent2;
	}
	
	public void setPlayerParent2(UUID playerParent2) {
		this.playerParent2 = playerParent2;
	}
	
	public UUID getBoth_player_name() {
		return both_player_name;
	}
	
	public void setBoth_player_name(UUID both_player_name) {
		this.both_player_name = both_player_name;
	}
	
	public Age getAge() {
		return this.age;
	}
	
	public void setAge(Age age) {
		this.age = age;
	}
}
