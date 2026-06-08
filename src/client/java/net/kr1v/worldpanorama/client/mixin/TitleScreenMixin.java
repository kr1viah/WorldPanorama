package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.config.Main;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;extractPanorama(Lnet/minecraft/client/gui/GuiGraphicsExtractor;F)V"))
	private void preventPanorama(TitleScreen instance, GuiGraphicsExtractor graphicsExtractor, float v, Operation<Void> original) {
		if (Main.ENABLED.getBooleanValue()) return;
		original.call(instance, graphicsExtractor, v);
	}

	@Inject(method = "shouldCloseOnEsc", at = @At("HEAD"), cancellable = true)
	private void shouldCloseOnEsc(CallbackInfoReturnable<Boolean> cir) {
		if (Main.ENABLED.getBooleanValue()) {
			//TODO: causes splash to change. evil. if no world is loaded pressing esc will just endlessly refresh the splash.
			cir.setReturnValue(true);
		}
	}
}
