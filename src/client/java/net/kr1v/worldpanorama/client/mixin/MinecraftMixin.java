package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Unique
	private boolean hasLaunched;
	private float spin;

	@Shadow
	public abstract void setScreen(@Nullable Screen screen);

	@Shadow
	public abstract WorldOpenFlows createWorldOpenFlows();

	@Shadow
	public abstract LevelStorageSource getLevelSource();

	@Shadow
	@Nullable
	public ClientLevel level;

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

	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	private void inject2(Screen screen, CallbackInfo ci) {
		if ((screen instanceof TitleScreen) && Main.ENABLED.getBooleanValue() && !WorldPanoramaClient.isInTitleScreen) {
			WorldPanoramaClient.isInTitleScreen = true;
			startSingleplayer(Main.WORLD_NAME.getStringValue());
			hasLaunched = true;
		}
		if (screen instanceof PauseScreen && Main.ENABLED.getBooleanValue() && WorldPanoramaClient.isInTitleScreen) {
			ci.cancel();
			setScreen(new TitleScreen());
		}
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop2(Minecraft instance, Screen screen, Operation<Void> original) {
		if (Main.ENABLED.getBooleanValue()) {
			original.call(instance, new Screen(Component.empty()) {
				@Override
				public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
				}
			});
			return;
		}
		original.call(instance, screen);
	}

	@Unique
	private int prevFov = -1;

	@Unique
	private Boolean prevHideGui = null;

	@Inject(method = "renderFrame", at = @At("HEAD"))
	private void doThing(boolean advanceGameTime, CallbackInfo ci) {
		if (Main.ENABLED.getBooleanValue() && WorldPanoramaClient.isInTitleScreen && screen != null) {
			var player = Minecraft.getInstance().player;
			if (player != null) {
				player.setXRot(Main.PANORAMA_PITCH.getFloatValue());
				if (Main.ROTATE_PANORAMA.getBooleanValue()) {
					float a = Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks();
					float delta = (float) (a * Minecraft.getInstance().gameRenderer.getGameRenderState().optionsRenderState.panoramaSpeed);
					this.spin = Mth.wrapDegrees(this.spin + delta * 0.1f);
					player.setYRot(spin);
				} else {
					player.setYRot(Main.PANORAMA_YAW.getFloatValue());
				}
			}

			if (prevFov == -1) {
				prevFov = Minecraft.getInstance().options.fov().get();
			}
			if (prevHideGui == null) {
				prevHideGui = Minecraft.getInstance().options.hideGui;
			}

			Minecraft.getInstance().options.hideGui = Main.HIDE_HUD.getBooleanValue();
			Minecraft.getInstance().options.fov().set(Main.PANORAMA_FOV.getIntegerValue());

		} else {
			if (prevHideGui != null) {
				Minecraft.getInstance().options.hideGui = prevHideGui;
				prevHideGui = null;
			}
			if (prevFov != -1) {
				Minecraft.getInstance().options.fov().set(prevFov);
				prevFov = -1;
			}
		}
	}

	@Unique
	private void startSingleplayer(@Nullable String name) {
		if (name != null && !name.isBlank() && getLevelSource().levelExists(name)) {
			WorldPanoramaClient.isLoadingPanoramaWorld = true;
			createWorldOpenFlows().openWorld(name, () -> setScreen(new TitleScreen()));
		}
	}
}
