package net.kr1v.worldpanorama.client;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.kr1v.worldpanorama.client.config.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.FileUtil;

import java.io.IOException;

public class WorldPanoramaClient {
	public static boolean isLoadingPanoramaWorld;
	public static boolean isInTitleScreen;
	public static boolean hasTitleScreenOpen;

	public static Pair<String, String> name2directory = Pair.of(Main.WORLD_NAME.getStringValue(), getName(Main.WORLD_NAME.getStringValue()));

	public static String getWorldName() {
		if (!Main.WORLD_NAME.getStringValue().equals(name2directory.getFirst())) {
			name2directory = Pair.of(Main.WORLD_NAME.getStringValue(), getName(Main.WORLD_NAME.getStringValue()));
		}
		return name2directory.getSecond();
	}

	public static String getName(String name) {
		try {
			name = FileUtil.findAvailableName(Minecraft.getInstance().getLevelSource().getBaseDir(), name, "");
		} catch (IOException e) {
			LogUtils.getLogger().error("error while trying to find available world name for {}", name, e);
		}
		return name;
	}
}