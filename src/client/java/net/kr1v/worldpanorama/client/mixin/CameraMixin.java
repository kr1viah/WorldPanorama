package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.WorldPanoramaConfig;
import net.kr1v.worldpanorama.client.util.Tweener;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public class CameraMixin {
	@Unique
	private float targetFov = WorldPanoramaConfig.PANORAMA_FOV;
	@Unique
	private final Tweener fovTweener = new Tweener(() -> targetFov, () -> WorldPanoramaConfig.ANIMATION_SPEED);

	@WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;calculateFov(F)F"))
	float yeah(Camera instance, float partialTicks, Operation<Float> original){
		if (WorldPanoramaConfig.ENABLED && WorldPanoramaClient.isInTitleScreen) {
			if (WorldPanoramaClient.hasTitleScreenOpen) {
				targetFov = WorldPanoramaConfig.PANORAMA_FOV;
			} else {
				targetFov = original.call(instance, partialTicks);
			}
			return (float) fovTweener.updateThenGet();
		}
		return original.call(instance, partialTicks);
	}
}