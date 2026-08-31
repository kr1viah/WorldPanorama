package net.kr1v.worldpanorama.client.interfaces;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

public interface SingleplayerStarter {
	@Unique
	void world_panorama$startSingleplayer(@Nullable String name);
}
