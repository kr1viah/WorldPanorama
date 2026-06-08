package net.kr1v.worldpanorama.client.config;

import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import kr1v.malilibApi.MalilibApi;
import kr1v.malilibApi.annotation.Config;
import kr1v.malilibApi.config._new.boring.ConfigFloat;
import kr1v.malilibApi.config.plus.ConfigBooleanPlus;
import kr1v.malilibApi.config.plus.ConfigHotkeyPlus;
import kr1v.malilibApi.config.plus.ConfigIntegerPlus;
import kr1v.malilibApi.config.plus.ConfigStringPlus;

@Config("world-panorama")
public class Main {
	public static final ConfigBooleanPlus ENABLED = new ConfigBooleanPlus("Enable world for panorama");
	public static final ConfigBooleanPlus GENERATE_NEW_EVERY_TIME = new ConfigBooleanPlus("Generate new world every time");
	public static final ConfigStringPlus WORLD_NAME = new ConfigStringPlus("World name to use", "Panorama World");
	public static final ConfigBooleanPlus ROTATE_PANORAMA = new ConfigBooleanPlus("Rotate panorama", true, "Applies to world panoramas, not \"real\" panoramas");
	public static final ConfigFloat PANORAMA_YAW = new ConfigFloat("Panorama yaw", 0, 0, 360, "Only applies if Rotate panorama is set to false");
	public static final ConfigFloat PANORAMA_PITCH = new ConfigFloat("Panorama pitch", 10, -90, 90, "");
	public static final ConfigBooleanPlus HIDE_HUD = new ConfigBooleanPlus("Hide hud");
	public static final ConfigIntegerPlus PANORAMA_FOV = new ConfigIntegerPlus("Fov", 30, 30, 110);

	public static final ConfigHotkeyPlus OPEN_MENU_HOTKEY = new ConfigHotkeyPlus("Open this gui", "J, C").setContext(KeybindSettings.Context.ANY).setPressCallback((_, _) -> {
		MalilibApi.openScreenFor("world-panorama");
		return true;
	});
}
