package io.github.franiscoder.mca.components.data;

import io.github.franiscoder.mca.util.enums.SpouseType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {
	//requests to the player
	public final List<UUID> marriageRequests = new ArrayList<>(5);
	//requests the player sent
	public final List<UUID> outgoingRequests = new ArrayList<>(5);
	//done to optimize from and to tags
	public boolean isMarried = false;
	//married stuff
	public SpouseType spouseType;
	public UUID spousePlayer;
	public String spouseName;
}
