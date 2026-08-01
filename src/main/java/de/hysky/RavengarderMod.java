package de.hysky;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;

public class RavengarderMod implements ClientModInitializer {
	public static final String MOD_ID = "ravengarder";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitializeClient() {}
}
