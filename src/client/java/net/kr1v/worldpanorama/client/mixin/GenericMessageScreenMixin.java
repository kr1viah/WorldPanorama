package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.config.Main;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenericMessageScreen.class)
public abstract class GenericMessageScreenMixin extends Screen {
	protected GenericMessageScreenMixin(Component title) {
		super(title);
	}
//TODO: i have no idea why it fucking breaks
//	@WrapOperation(method = "extractBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/GenericMessageScreen;extractPanorama(Lnet/minecraft/client/gui/GuiGraphicsExtractor;F)V"))
//	void yeah(GenericMessageScreen instance, GuiGraphicsExtractor guiGraphicsExtractor, float v, Operation<Void> original){
//		if(!Main.ENABLED.getBooleanValue()){
//			original.call(instance, guiGraphicsExtractor, v);
//		}
//	}

}
