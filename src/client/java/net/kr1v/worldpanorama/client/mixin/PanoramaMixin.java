package net.kr1v.worldpanorama.client.mixin;

import net.kr1v.worldpanorama.client.config.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//~ if <=1.21.11 'Panorama' -> 'PanoramaRenderer'
@Mixin(net.minecraft.client.renderer.Panorama.class)
public class PanoramaMixin {
	//~ if >=26.1 'render' -> 'extractRenderState'
	@Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
	private void prevent(@Coerce Object graphics, int width, int height/*? if <26.2 {*//*, boolean shouldSpin*//*? }*/, CallbackInfo ci) {
		if (Main.ENABLED.getBooleanValue()) {
			ci.cancel();
		}
	}
}
