package net.kr1v.worldpanorama.client.mixin;

import net.kr1v.worldpanorama.client.config.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <=1.21.11 {
@Mixin(net.minecraft.client.renderer.PanoramaRenderer.class)
//? } else
//@Mixin(net.minecraft.client.renderer.Panorama.class)
public class PanoramaMixin {
	@Inject(method = /*? if >=26.1 {*//*"extractRenderState"*//*? } else {*/"render"/*? }*/, at = @At("HEAD"), cancellable = true)
	private void prevent(@Coerce Object graphics, int width, int height, boolean shouldSpin, CallbackInfo ci) {
		if (Main.ENABLED.getBooleanValue()) {
			ci.cancel();
		}
	}
}
