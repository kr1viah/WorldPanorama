package net.kr1v.worldpanorama.client.mixin;

import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.WorldPanoramaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Gui.class)
public abstract class GuiMixin {
	
	@Shadow
	public abstract void setScreen(@Nullable Screen screen);
	
	@Shadow
	@Final
	private Minecraft minecraft;
	@Unique
	private TitleScreen theTitleScreen; // prevent splash from resetting
	
	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	private void inject2(Screen screen, CallbackInfo ci) {
		if ((screen instanceof TitleScreen titleScreen) && WorldPanoramaConfig.ENABLED && !WorldPanoramaClient.isInTitleScreen) {
			WorldPanoramaClient.isInTitleScreen = true;
			startSingleplayer(WorldPanoramaConfig.WORLD_NAME);
			theTitleScreen = titleScreen;
		}
		if (screen instanceof PauseScreen && WorldPanoramaConfig.ENABLED && WorldPanoramaClient.isInTitleScreen) {
			ci.cancel();
			setScreen(theTitleScreen);
		}
	}
	
	@Unique
	private void startSingleplayer(@Nullable String name) {
		if (isWorldNameValid(name)) {
			WorldPanoramaClient.isLoadingPanoramaWorld = true;
			if (WorldPanoramaConfig.GENERATE_NEW_EVERY_TIME) {
				try (var access = minecraft.getLevelSource().createAccess(name)) {
					access.deleteLevel();
				} catch (IOException _) {}
				minecraft.createWorldOpenFlows().createFreshLevel(
						name,
						new LevelSettings(name, GameType.CREATIVE, LevelSettings.DifficultySettings.DEFAULT, true, WorldDataConfiguration.DEFAULT),
						new WorldOptions(WorldPanoramaConfig.WORLD_SEED, true, false),
						WorldPresets::createNormalWorldDimensions,
						theTitleScreen);
			} else {
				if (minecraft.getLevelSource().levelExists(name)) {
					minecraft.createWorldOpenFlows().openWorld(name, () -> setScreen(new TitleScreen()));
				} else {
					minecraft.createWorldOpenFlows().createFreshLevel(
							name,
							new LevelSettings(name, GameType.CREATIVE, LevelSettings.DifficultySettings.DEFAULT, true, WorldDataConfiguration.DEFAULT),
							new WorldOptions(WorldPanoramaConfig.WORLD_SEED, true, false),
							WorldPresets::createNormalWorldDimensions,
							theTitleScreen);
				}
			}
		}
	}
	
	@Unique
	private boolean isWorldNameValid(@Nullable String name) {
		return name != null && !name.isBlank();
	}
}
