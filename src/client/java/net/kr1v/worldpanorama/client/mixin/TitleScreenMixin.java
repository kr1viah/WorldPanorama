package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.Main;
import net.kr1v.worldpanorama.client.util.Tweener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	@Unique
	private final Tweener scaleTweener = new Tweener(() -> 1, Main.ANIMATION_SPEED::getFloatValue);

	protected TitleScreenMixin(Component title) {
		super(title);
	}

	@Inject(method = "init", at = @At("RETURN"))
	private void hasTitleScreenOpen(CallbackInfo ci) {
		WorldPanoramaClient.hasTitleScreenOpen = true;
		scaleTweener.snapToValue(0);
	}

	@Inject(method = "shouldCloseOnEsc", at = @At("HEAD"), cancellable = true)
	private void shouldCloseOnEsc(CallbackInfoReturnable<Boolean> cir) {
		if (Main.ENABLED.getBooleanValue() && !WorldPanoramaClient.isLoadingPanoramaWorld) {
			cir.setReturnValue(true);
			WorldPanoramaClient.hasTitleScreenOpen = false;
		}
	}
	@Inject(method = "isPauseScreen", at = @At("HEAD"), cancellable = true)
	void yeah(CallbackInfoReturnable<Boolean> cir) {
		if (Main.ENABLED.getBooleanValue() && Main.SHOULD_PAUSE.getBooleanValue() && Minecraft.getInstance().player != null) {
			cir.setReturnValue(true);
		}
	}
	//TODO: only make this happen when opening title screen from panorama world
	@Inject(method = "extractRenderState", at = @At(value = "HEAD"))
	void yeahhh(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci){
		scaleTweener.update();
		graphics.pose().pushMatrix();
		graphics.pose().scaleAround(((float) scaleTweener.get()), width / 2F, height / 2F);
	}
	@Inject(method = "extractRenderState", at = @At(value = "TAIL"))
	void yeahhhj(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci){
		graphics.pose().popMatrix();
	}
}
