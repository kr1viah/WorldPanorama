package net.kr1v.worldpanorama.client.mixin.mc1_21_11;

//? if <=1.21.11 {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.Main;
import net.kr1v.worldpanorama.client.util.Tweener;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Unique;
//? }
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	//? if <=1.21.11 {
	@Unique
	private float targetFov = Main.PANORAMA_FOV.get();
	@Unique
	private final Tweener fovTweener = new Tweener(() -> targetFov, Main.ANIMATION_SPEED::getFloatValue);

	@WrapMethod(method = "getFov")
	private float wrap(Camera camera, float f, boolean bl, Operation<Float> original) {
		if (Main.ENABLED.get() && WorldPanoramaClient.isInTitleScreen) {
			if (WorldPanoramaClient.hasTitleScreenOpen) {
				targetFov = Main.PANORAMA_FOV.get();
			} else {
				targetFov = original.call(camera, f, bl);
			}
			return (float) fovTweener.updateThenGet();
		}
		return original.call(camera, f, bl);
	}
	//? }
}
