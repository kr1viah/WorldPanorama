package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.Main;
import net.kr1v.worldpanorama.client.interfaces.Tweened;
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
public abstract class TitleScreenMixin extends Screen implements Tweened {
	@Unique
	private final Tweener scaleTweener = new Tweener(() -> 1, Main.ANIMATION_SPEED::getFloatValue);

	@Override
	public Tweener world_panorama$getTweener() {
		return scaleTweener;
	}

	protected TitleScreenMixin(Component title) {
		super(title);
	}

	@Inject(method = "init", at = @At("RETURN"))
	private void hasTitleScreenOpen(CallbackInfo ci) {
		WorldPanoramaClient.hasTitleScreenOpen = true;
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

	@WrapMethod(method = "extractRenderState")
	private void wrap(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, Operation<Void> original) {
		scaleTweener.update();
		graphics.pose().pushMatrix();
		graphics.pose().scaleAround(((float) scaleTweener.get()), width / 2F, height / 2F);
		original.call(graphics, mouseX, mouseY, a);
		graphics.pose().popMatrix();
	}
}
