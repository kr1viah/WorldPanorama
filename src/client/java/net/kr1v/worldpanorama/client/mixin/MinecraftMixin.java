package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.WorldPanoramaConfig;
import net.kr1v.worldpanorama.client.util.Tweener;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Unique
	private float spin;

	@Shadow
	@Nullable
	public LocalPlayer player;

	@Shadow
	@Final
	public GameRenderer gameRenderer;

	@Shadow
	public abstract DeltaTracker getDeltaTracker();
	
	@Shadow
	@Final
	public Gui gui;
	
	@WrapOperation(method = "doWorldLoad", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop(Gui instance, Screen screen, Operation<Void> original) {
		if (!WorldPanoramaClient.isLoadingPanoramaWorld)
			original.call(instance, screen);
	}

	@WrapOperation(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreenAndShow(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void inject(Minecraft instance, Screen screen, Operation<Void> original) {
		if (WorldPanoramaClient.isInTitleScreen && !WorldPanoramaClient.isLoadingPanoramaWorld) {
			WorldPanoramaClient.isInTitleScreen = false;
			gui.setScreen(new ProgressScreen(true));
			return;
		}
		if (WorldPanoramaClient.isLoadingPanoramaWorld) {
			return;
		}

		original.call(instance, screen);
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop2(Gui instance, Screen screen, Operation<Void> original) {
		if (WorldPanoramaConfig.ENABLED) {
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
	private Boolean prevHideGui = null;
	
	@Unique
	private Tweener pitchTweener = new Tweener(() -> WorldPanoramaConfig.PANORAMA_PITCH, () -> WorldPanoramaConfig.ANIMATION_SPEED);

	@Inject(method = "renderFrame", at = @At("HEAD"))
	private void doThing(boolean advanceGameTime, CallbackInfo ci) {
		if (WorldPanoramaConfig.ENABLED && WorldPanoramaClient.isInTitleScreen && gui.screen() == null) {
			WorldPanoramaClient.hasTitleScreenOpen = false;
		}
		if (WorldPanoramaConfig.ENABLED && WorldPanoramaClient.isInTitleScreen && WorldPanoramaClient.hasTitleScreenOpen) {
			if (player != null) {
				pitchTweener.update();
				player.setXRot((float) pitchTweener.get());
				if (WorldPanoramaConfig.ROTATE_PANORAMA) {
					float a = getDeltaTracker().getRealtimeDeltaTicks();
					float delta = (float) (a * gameRenderer.gameRenderState().optionsRenderState.panoramaSpeed);
					this.spin = Mth.wrapDegrees(this.spin + delta * 0.1f);
					player.setYRot(spin);
				} else {
					player.setYRot(WorldPanoramaConfig.PANORAMA_YAW);
				}
			}
			if (prevHideGui == null) {
				prevHideGui = gui.hud.isHidden();
			}

			if (gui.hud.isHidden() != WorldPanoramaConfig.HIDE_HUD) gui.hud.toggle();
		} else {
			if (prevHideGui != null) {
				if (gui.hud.isHidden() != prevHideGui) gui.hud.toggle();
				prevHideGui = null;
			}
			if (player != null) {
				spin = player.getYRot();
				pitchTweener.snapToValue(player.getXRot());
			}
		}
	}
}
