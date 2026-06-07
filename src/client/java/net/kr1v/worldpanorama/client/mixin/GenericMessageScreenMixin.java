package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.config.Main;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GenericMessageScreen.class)
public class GenericMessageScreenMixin {
	@WrapOperation(method = "extractBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/GenericMessageScreen;extractPanorama(Lnet/minecraft/client/gui/GuiGraphicsExtractor;F)V"))
	private void preventPanorama(GenericMessageScreen instance, GuiGraphicsExtractor graphicsExtractor, float v, Operation<Void> original) {
		if (Main.ENABLED.getBooleanValue()) return;
		original.call(instance, graphicsExtractor, v);
	}
}
