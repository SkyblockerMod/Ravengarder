package de.hysky.ravengarder.utils;

import java.util.Arrays;

public enum RavengardLocation {
	HUB("RAVENGARD_HUB"),
	DUNGEON("RAVENGARD_DUNGEON_TRIO"),
	UNKNOWN("");

	private final String id;

	RavengardLocation(String id) {
		this.id = id;
	}

	public String id() {
		return id;
	}

	public static RavengardLocation from(String id) {
		return Arrays.stream(values())
				.filter(location -> location.id.equals(id))
				.findFirst()
				.orElse(UNKNOWN);
	}
}
