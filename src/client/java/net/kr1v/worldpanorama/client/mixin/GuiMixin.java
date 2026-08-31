package net.kr1v.worldpanorama.client.mixin;

import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.Main;
import net.kr1v.worldpanorama.client.interfaces.SingleplayerStarter;
import net.kr1v.worldpanorama.client.interfaces.TitleScreenHaver;
import net.kr1v.worldpanorama.client.interfaces.Tweened;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
	//? >=26.2 {
	@Shadow
	public abstract void setScreen(Screen screen);

	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	private void inject2(Screen screen, CallbackInfo ci) {
		if ((screen instanceof TitleScreen titleScreen) && Main.ENABLED.getBooleanValue() && !WorldPanoramaClient.isInTitleScreen) {
			WorldPanoramaClient.isInTitleScreen = true;
			((SingleplayerStarter) Minecraft.getInstance()).world_panorama$startSingleplayer(Main.WORLD_NAME.getStringValue());
			((TitleScreenHaver) Minecraft.getInstance()).world_panorama$setTitleScreen(titleScreen);
		}
		if (screen instanceof PauseScreen && Main.ENABLED.getBooleanValue() && WorldPanoramaClient.isInTitleScreen) {
			ci.cancel();
			setScreen(((TitleScreenHaver) Minecraft.getInstance()).world_panorama$getTitleScreen());
			((Tweened) ((TitleScreenHaver) Minecraft.getInstance()).world_panorama$getTitleScreen()).world_panorama$getTweener().snapToValue(0);
		}
	}
	//? }
}
