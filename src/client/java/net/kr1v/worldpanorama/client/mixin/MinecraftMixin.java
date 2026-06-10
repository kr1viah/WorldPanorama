package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.Main;
import net.kr1v.worldpanorama.client.interfaces.Tweened;
import net.kr1v.worldpanorama.client.util.Tweener;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import static net.minecraft.world.level.levelgen.WorldOptions.randomSeed;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Unique
	private float spin;

	@Shadow
	public abstract void setScreen(@Nullable Screen screen);

	@Shadow
	public abstract WorldOpenFlows createWorldOpenFlows();

	@Shadow
	public abstract LevelStorageSource getLevelSource();

	@Shadow
	@Final
	public Options options;

	@Shadow
	@Nullable
	public LocalPlayer player;

	@Shadow
	public abstract DeltaTracker getDeltaTracker();

	@Shadow
	@Nullable
	public Screen screen;

	@WrapOperation(method = "doWorldLoad", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop(Minecraft instance, Screen screen, Operation<Void> original) {
		if (!WorldPanoramaClient.isLoadingPanoramaWorld)
			original.call(instance, screen);
	}

	@WrapOperation(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreenAndShow(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void inject(Minecraft instance, Screen screen, Operation<Void> original) {
		if (WorldPanoramaClient.isInTitleScreen && !WorldPanoramaClient.isLoadingPanoramaWorld) {
			WorldPanoramaClient.isInTitleScreen = false;
			setScreen(new ProgressScreen(true));
			return;
		}
		if (WorldPanoramaClient.isLoadingPanoramaWorld) {
			return;
		}

		original.call(instance, screen);
	}

	@Unique
	private TitleScreen theTitleScreen; // prevent splash from resetting

	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	private void inject2(Screen screen, CallbackInfo ci) {
		if ((screen instanceof TitleScreen titleScreen) && Main.ENABLED.getBooleanValue() && !WorldPanoramaClient.isInTitleScreen) {
			WorldPanoramaClient.isInTitleScreen = true;
			startSingleplayer(Main.WORLD_NAME.getStringValue());
			theTitleScreen = titleScreen;
		}
		if (screen instanceof PauseScreen && Main.ENABLED.getBooleanValue() && WorldPanoramaClient.isInTitleScreen) {
			ci.cancel();
			setScreen(theTitleScreen);
			((Tweened) theTitleScreen).world_panorama$getTweener().snapToValue(0);
		}
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop2(Minecraft instance, Screen screen, Operation<Void> original) {
		if (Main.ENABLED.getBooleanValue()) {
			original.call(instance, new Screen(Component.empty()) {
				//? if >=26.1 {
				/*@Override
				public void extractBackground(@NotNull net.minecraft.client.gui.GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
				}
				*///? } else {
				@Override
				public void renderBackground(@NotNull net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float a) {
				}
				//? }
			});
			return;
		}
		original.call(instance, screen);
	}


	@Unique
	private Boolean prevHideGui = null;

	@Unique
	private final Tweener pitchTweener = new Tweener(Main.PANORAMA_PITCH::getFloatValue, Main.ANIMATION_SPEED::getFloatValue);

	//? if <=1.21.11 {
	@Inject(method = "runTick", at = @At("HEAD"))
	//? } else
	//@Inject(method = "renderFrame", at = @At("HEAD"))
	private void doThing(boolean advanceGameTime, CallbackInfo ci) {
		if (Main.ENABLED.getBooleanValue() && WorldPanoramaClient.isInTitleScreen && screen == null) {
			WorldPanoramaClient.hasTitleScreenOpen = false;
		}
		if (Main.ENABLED.getBooleanValue() && WorldPanoramaClient.isInTitleScreen && WorldPanoramaClient.hasTitleScreenOpen) {
			if (player != null) {
				pitchTweener.update();
				player.setXRot(((float) pitchTweener.get()));
				if (Main.ROTATE_PANORAMA.getBooleanValue()) {
					float a = getDeltaTracker().getRealtimeDeltaTicks();
					float delta = (float) (a * options.panoramaSpeed().get());
					this.spin = Mth.wrapDegrees(this.spin + delta * 0.1f);
					player.setYRot(spin);
				} else {
					player.setYRot(Main.PANORAMA_YAW.getFloatValue());
				}
			}
			if (prevHideGui == null) {
				prevHideGui = options.hideGui;
			}

			options.hideGui = Main.HIDE_HUD.getBooleanValue();
		} else {
			if (prevHideGui != null) {
				options.hideGui = prevHideGui;
				prevHideGui = null;
			}
			if (player != null) {
				spin = player.getYRot();
				pitchTweener.snapToValue(player.getXRot());
			}
		}
	}

	@Unique
	private void startSingleplayer(@Nullable String name) {
		if (isWorldNameValid(name)) {
			WorldPanoramaClient.isLoadingPanoramaWorld = true;
			if (Main.GENERATE_NEW_EVERY_TIME.getBooleanValue()) {
				try (var access = getLevelSource().createAccess(name)) {
					access.deleteLevel();
				} catch (IOException ignored) {
				}
				createWorldOpenFlows().createFreshLevel(
						name,
						//? if >=26.1 {
						/*new LevelSettings(name, GameType.CREATIVE, LevelSettings.DifficultySettings.DEFAULT, true, WorldDataConfiguration.DEFAULT),
						*///? } else
						new LevelSettings(name, GameType.CREATIVE, false, net.minecraft.world.Difficulty.NORMAL, true, new net.minecraft.world.level.gamerules.GameRules(WorldDataConfiguration.DEFAULT.enabledFeatures()), WorldDataConfiguration.DEFAULT),
						new WorldOptions(randomSeed(), true, false),
						WorldPresets::createNormalWorldDimensions,
						theTitleScreen
				);
			} else {
				if (getLevelSource().levelExists(name)) {
					createWorldOpenFlows().openWorld(name, () -> setScreen(new TitleScreen()));
				} else {
					createWorldOpenFlows().createFreshLevel(
							name,
							//? if >=26.1 {
							/*new LevelSettings(name, GameType.CREATIVE, LevelSettings.DifficultySettings.DEFAULT, true, WorldDataConfiguration.DEFAULT),
							 *///? } else
							new LevelSettings(name, GameType.CREATIVE, false, net.minecraft.world.Difficulty.NORMAL, true, new net.minecraft.world.level.gamerules.GameRules(WorldDataConfiguration.DEFAULT.enabledFeatures()), WorldDataConfiguration.DEFAULT),
							new WorldOptions(randomSeed(), true, false),
							WorldPresets::createNormalWorldDimensions,
							theTitleScreen
					);
				}
			}
		}
	}

	@Unique
	private boolean isWorldNameValid(@Nullable String name) {
		return name != null && !name.isBlank();
	}
}
