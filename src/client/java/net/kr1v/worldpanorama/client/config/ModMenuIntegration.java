package net.kr1v.worldpanorama.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.WorldOptions;

import static net.kr1v.worldpanorama.client.config.WorldPanoramaConfig.*;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parentScreen -> YetAnotherConfigLib.createBuilder()
				.title(Component.literal("World Panorama Options"))
				.category(ConfigCategory.createBuilder()
						.name(Component.literal("Panorama Options"))
						.option(Option.<Boolean>createBuilder()
								.name(Component.literal("Mod Enabled"))
								.description(OptionDescription.of(Component.literal("Fully enables or disables this mod")))
								.binding(true, () -> ENABLED, val -> ENABLED = val)
								.controller(BooleanControllerBuilder::create).build())
						.option(Option.<Boolean>createBuilder()
								.name(Component.literal("Generate on Start"))
								.description(OptionDescription.of(Component.literal("Generate a new world for the panorama every time the game is launched")))
								.binding(true, () -> GENERATE_NEW_EVERY_TIME, val -> GENERATE_NEW_EVERY_TIME = val)
								.controller(BooleanControllerBuilder::create).build())
						.option(Option.<String>createBuilder()
								.name(Component.literal("Panorama World Name"))
								.description(OptionDescription.of(Component.literal("The name of the world to load on startup")))
								.binding("Panorama World", () -> WORLD_NAME, val -> WORLD_NAME = val)
								.controller(StringControllerBuilder::create).build())
						.option(Option.<Long>createBuilder()
								.name(Component.literal("Panorama World Seed"))
								.description(OptionDescription.of(Component.literal("Seed used for the panorama world")))
								.binding(WorldOptions.randomSeed(), () -> WORLD_SEED, val -> WORLD_SEED = val)
								.controller(LongFieldControllerBuilder::create).build())
//						.option(Option.<Double>createBuilder()
//								.name(Component.literal("Player Starting X"))
//								.description(OptionDescription.of(Component.literal("Starting X position of the player, NaN for random")))
//								.binding(Double.NaN, () -> PLAYER_STARTING_X, val -> PLAYER_STARTING_X = val)
//								.controller(DoubleFieldControllerBuilder::create).build())
//						.option(Option.<Double>createBuilder()
//								.name(Component.literal("Player Starting y"))
//								.description(OptionDescription.of(Component.literal("Starting Y position of the player, NaN for random")))
//								.binding(Double.NaN, () -> PLAYER_STARTING_Y, val -> PLAYER_STARTING_Y = val)
//								.controller(DoubleFieldControllerBuilder::create).build())
//						.option(Option.<Double>createBuilder()
//								.name(Component.literal("Player Starting Z"))
//								.description(OptionDescription.of(Component.literal("Starting Z position of the player, NaN for random")))
//								.binding(Double.NaN, () -> PLAYER_STARTING_Z, val -> PLAYER_STARTING_Z = val)
//								.controller(DoubleFieldControllerBuilder::create).build())
						.option(Option.<Boolean>createBuilder()
								.name(Component.literal("Rotate Panorama"))
								.description(OptionDescription.of(Component.literal("Applies to world panoramas, not \"real\" panoramas")))
								.binding(true, () -> ROTATE_PANORAMA, val -> ROTATE_PANORAMA = val)
								.controller(BooleanControllerBuilder::create).build())
						.option(Option.<Float>createBuilder()
								.name(Component.literal("Panorama Yaw"))
								.description(OptionDescription.of(Component.literal("Only applies if \"Rotate Panorama\" is set to false")))
								.binding(0F, () -> PANORAMA_YAW, val -> PANORAMA_YAW = val)
								.controller(option -> FloatSliderControllerBuilder.create(option).range(0F, 360F).step(1F)).build())
						.option(Option.<Float>createBuilder()
								.name(Component.literal("Panorama Pitch"))
								.description(OptionDescription.of(Component.literal("Vertical rotation when in the title screen")))
								.binding(10F, () -> PANORAMA_PITCH, val -> PANORAMA_PITCH = val)
								.controller(option -> FloatSliderControllerBuilder.create(option).range(-90F, 90F).step(1F)).build())
						.option(Option.<Boolean>createBuilder()
								.name(Component.literal("Hide HUD"))
								.description(OptionDescription.of(Component.literal("Hides all GUI elements when in the title screen")))
								.binding(true, () -> HIDE_HUD, val -> HIDE_HUD = val)
								.controller(BooleanControllerBuilder::create).build())
						.option(Option.<Integer>createBuilder()
								.name(Component.literal("FOV"))
								.description(OptionDescription.of(Component.literal("FOV used only when in a title screen")))
								.binding(30, () -> PANORAMA_FOV, val -> PANORAMA_FOV = val)
								.controller(option -> IntegerSliderControllerBuilder.create(option).range(30, 110).step(1)).build())
						.option(Option.<Boolean>createBuilder()
								.name(Component.literal("Pause World in Title Screen"))
								.description(OptionDescription.of(Component.literal("Freezes the world if enabled. Good for survival worlds")))
								.binding(false, () -> SHOULD_PAUSE, val -> SHOULD_PAUSE = val)
								.controller(BooleanControllerBuilder::create).build())
						.option(Option.<Float>createBuilder()
								.name(Component.literal("Animation Speed"))
								.description(OptionDescription.of(Component.literal("How fast the the FOV, pitch, and screen scale transitions will be")))
								.binding(20F, () -> ANIMATION_SPEED, val -> ANIMATION_SPEED = val)
								.controller(option -> FloatSliderControllerBuilder.create(option).range(1F, 30F).step(1F)).build())
						.build())
//				.category(ConfigCategory.createBuilder()
//						.name(Component.literal("Presets"))
//						.option(ButtonOption.createBuilder()
//								.name(Component.literal("26.1 aka Tiny Takeover"))
//								.description(OptionDescription.of(Component.literal("The panorama for the Tiny Takeover 26.1 drop")))
//								.action((this::setTinyTakeover))
//								.build())
//						.build())
				.save(() -> HANDLER.save())
				.build().generateScreen(parentScreen);
	}
	
	public void setTinyTakeover(YACLScreen screen, ButtonOption option) {
		// -250.8575763589899 126.74146484799883 -2012.82253427 39.080353 0
		// cloud tick 24215
		//todo
	}
}
