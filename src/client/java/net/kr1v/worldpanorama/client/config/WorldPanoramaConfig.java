package net.kr1v.worldpanorama.client.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.WorldOptions;

public class WorldPanoramaConfig {
	public static ConfigClassHandler<WorldPanoramaConfig> HANDLER = ConfigClassHandler.createBuilder(WorldPanoramaConfig.class)
			.id(Identifier.parse("world-panorama:config"))
			.serializer(config -> GsonConfigSerializerBuilder.create(config)
					.setPath(FabricLoader.getInstance().getConfigDir().resolve("panorama_config.json5"))
					.setJson5(true)
					.build())
			.build();
	
	@SerialEntry
	public static boolean ENABLED = true;
	@SerialEntry
	public static boolean GENERATE_NEW_EVERY_TIME = true;
	@SerialEntry
	public static String WORLD_NAME = "Panorama World";
	@SerialEntry
	public static long WORLD_SEED = WorldOptions.randomSeed();
//	@SerialEntry
//	public static double PLAYER_STARTING_X = Double.NaN;
//	@SerialEntry
//	public static double PLAYER_STARTING_Y = Double.NaN;
//	@SerialEntry
//	public static double PLAYER_STARTING_Z = Double.NaN;
	@SerialEntry
	public static boolean ROTATE_PANORAMA = true;
	@SerialEntry
	public static float PANORAMA_YAW = 0;
	@SerialEntry
	public static float PANORAMA_PITCH = 10;
	@SerialEntry
	public static boolean HIDE_HUD = true;
	@SerialEntry
	public static int PANORAMA_FOV = 30;
	@SerialEntry
	public static boolean SHOULD_PAUSE = false;
	@SerialEntry
	public static float ANIMATION_SPEED = 20;
	
	static {
		HANDLER.load();
	}
}
