package io.github.franiscoder.mca.util.villager.skins;

import com.google.gson.Gson;

public class SkinsGroup {
	private String gender;
	private String profession;
	private String[] paths;
	
	public static SkinsGroup[] getAllSkins() {
		//what
		Gson gson = new Gson();
		return gson.fromJson("api/skins.json", SkinsGroup[].class);
	}
	
	public String getGender() {
		return gender;
	}
	
	public String getProfession() {
		return profession;
	}
	
	public String[] getPaths() {
		return paths;
	}
}
