package de.hysky.ravengarder;

import de.hysky.ravengarder.utils.RavengardInfo;
import de.hysky.ravengarder.utils.container.RavengarderContainerSolverManager;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;

public class RavengarderMod implements ClientModInitializer {
	public static final String MOD_ID = "ravengarder";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitializeClient() {
		RavengardInfo.init();
		RavengarderContainerSolverManager.init();
	}
}
