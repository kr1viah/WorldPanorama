package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldOpenFlows.class)
public class WorldOpenFlowsMixin {
	@WrapOperation(method = "openWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreenAndShow(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop(Minecraft instance, Screen screen, Operation<Void> original) {
		if (!WorldPanoramaClient.isLoadingPanoramaWorld)
			original.call(instance, screen);
	}

	@WrapOperation(method = "openWorldLoadLevelData", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreenAndShow(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop2(Minecraft instance, Screen screen, Operation<Void> original) {
		if (!WorldPanoramaClient.isLoadingPanoramaWorld)
			original.call(instance, screen);
	}

	@WrapOperation(method = "openWorldLoadLevelStem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop3(Minecraft instance, Screen screen, Operation<Void> original) {
		if (!WorldPanoramaClient.isLoadingPanoramaWorld)
			original.call(instance, screen);
	}

	@WrapOperation(method = "openWorldLoadLevelStem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreenAndShow(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop4(Minecraft instance, Screen screen, Operation<Void> original) {
		if (!WorldPanoramaClient.isLoadingPanoramaWorld)
			original.call(instance, screen);
	}

	@WrapOperation(method = "createFreshLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreenAndShow(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop5(Minecraft instance, Screen screen, Operation<Void> original) {
		if (!WorldPanoramaClient.isLoadingPanoramaWorld)
			original.call(instance, screen);
	}
}
