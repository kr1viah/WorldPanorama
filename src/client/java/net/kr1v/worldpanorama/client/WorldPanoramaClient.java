package net.kr1v.worldpanorama.client;

import net.fabricmc.api.ClientModInitializer;

public class WorldPanoramaClient implements ClientModInitializer {
	public static boolean isLoadingPanoramaWorld;
	public static boolean isInTitleScreen;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}