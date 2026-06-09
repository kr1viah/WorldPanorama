package net.kr1v.worldpanorama.client.config;

import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import kr1v.malilibApi.MalilibApi;
import kr1v.malilibApi.annotation.Config;
import kr1v.malilibApi.annotation.Label;
import kr1v.malilibApi.config._new.boring.ConfigFloat;
import kr1v.malilibApi.config.plus.ConfigBooleanPlus;
import kr1v.malilibApi.config.plus.ConfigHotkeyPlus;
import kr1v.malilibApi.config.plus.ConfigIntegerPlus;
import kr1v.malilibApi.config.plus.ConfigStringPlus;

@Config("world-panorama")
public class Main {
	public static final ConfigBooleanPlus ENABLED = new ConfigBooleanPlus("Enable world for panorama", true, "Fully enables or disables this mod");

	@Label
	@Label("Camera")
	public static final ConfigBooleanPlus ROTATE_PANORAMA = new ConfigBooleanPlus("Rotate panorama", true, "Applies to world panoramas, not \"real\" panoramas");
	public static final ConfigFloat PANORAMA_YAW = new ConfigFloat("Panorama yaw", 0, 0, 360, "Only applies if \"Rotate panorama\" is set to false");
	public static final ConfigFloat PANORAMA_PITCH = new ConfigFloat("Panorama pitch", 10, -90, 90, "Vertical rotation when in the title screen");
	public static final ConfigBooleanPlus HIDE_HUD = new ConfigBooleanPlus("Hide HUD", true, "Hides all GUI elements when in the title screen");
	public static final ConfigIntegerPlus PANORAMA_FOV = new ConfigIntegerPlus("FOV", 30, 30, 110, "FOV used only when in a title screen");
	public static final ConfigFloat ANIMATION_SPEED = new ConfigFloat("Animation Speed", 20, 1, 30, "How fast the the FOV, pitch, and screen scale transitions will be");

	@Label
	@Label("World")
	public static final ConfigStringPlus WORLD_NAME = new ConfigStringPlus("World name to use", "Panorama World", "The name of the world to load on startup");
	public static final ConfigBooleanPlus GENERATE_NEW_EVERY_TIME = new ConfigBooleanPlus("Generate on start", false, "Generate a new world for the panorama every time the game is launched");
	public static final ConfigBooleanPlus SHOULD_PAUSE = new ConfigBooleanPlus("Pause world", false, "Pauses the world if enabled. Good for survival worlds");

	@SuppressWarnings("unused")
	@Label
	@Label("Misc")
	public static final ConfigHotkeyPlus OPEN_MENU_HOTKEY = new ConfigHotkeyPlus("Open this gui", "J, C", "Keyboard combo to open this screen").setContext(KeybindSettings.Context.ANY).setPressCallback((_, _) -> {
		MalilibApi.openScreenFor("world-panorama");
		return true;
	});
}
