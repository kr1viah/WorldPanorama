package net.kr1v.worldpanorama.client.mixin;

import net.kr1v.worldpanorama.client.config.WorldPanoramaConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Panorama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Panorama.class)
public class PanoramaMixin {
	@Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
	private void prevent(GuiGraphicsExtractor graphics, int width, int height, CallbackInfo ci) {
		if (WorldPanoramaConfig.ENABLED) {
			ci.cancel();
		}
	}
}
