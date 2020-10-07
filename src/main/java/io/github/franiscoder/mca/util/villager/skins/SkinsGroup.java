package io.github.franiscoder.mca.util.villager.skins;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SkinsGroup {
	@Getter
	private final String gender;
	@Getter
	private final String profession;
	@Getter
	private final String[] paths;
	
	public static SkinsGroup[] getAllSkins() {
		//what
		Gson gson = new Gson();
		return gson.fromJson("api/skins.json", SkinsGroup[].class);
	}
}
