package net.kr1v.worldpanorama.client.mixin;

import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.Main;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "init", at = @At("RETURN"))
	private void hasTitleScreenOpen(CallbackInfo ci) {
		WorldPanoramaClient.hasTitleScreenOpen = true;
	}

	@Inject(method = "shouldCloseOnEsc", at = @At("HEAD"), cancellable = true)
	private void shouldCloseOnEsc(CallbackInfoReturnable<Boolean> cir) {
		if (Main.ENABLED.getBooleanValue()) {
			cir.setReturnValue(true);
			WorldPanoramaClient.hasTitleScreenOpen = false;
		}
	}
}
