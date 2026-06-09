package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.kr1v.worldpanorama.client.config.WorldPanoramaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.BlockPos;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldLoader.ResultFactory;
import net.minecraft.server.WorldStem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.LevelDataAndDimensions;
import net.minecraft.world.level.storage.LevelDataAndDimensions.WorldDataAndGenSettings;
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

	@WrapOperation(method = "openWorldLoadLevelStem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void wrapop3(Gui instance, Screen screen, Operation<Void> original) {
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
	
//	@WrapOperation(method = "createFreshLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows;loadWorldDataBlocking(Lnet/minecraft/server/WorldLoader$PackConfig;Lnet/minecraft/server/WorldLoader$WorldDataSupplier;Lnet/minecraft/server/WorldLoader$ResultFactory;)Ljava/lang/Object;"))
//	<D, R> R a(WorldOpenFlows instance, WorldLoader.PackConfig packConfig, WorldLoader.WorldDataSupplier<D> worldDataGetter,
//		ResultFactory<D, R> worldDataSupplier, Operation<R> original) {
//		if (WorldPanoramaClient.isLoadingPanoramaWorld)
//			if (!Double.isNaN(WorldPanoramaConfig.PLAYER_STARTING_X)) { return original.call(instance, packConfig, worldDataGetter,
//					((ResultFactory<WorldDataAndGenSettings, WorldStem>)
//							(resourceManager, dataPackResources, registries, worldDataAndGenSettings) -> {
//								worldDataAndGenSettings.data().overworldData().setSpawn(LevelData.RespawnData.of(
//										Level.OVERWORLD, new BlockPos((int) WorldPanoramaConfig.PLAYER_STARTING_X, (int) WorldPanoramaConfig.PLAYER_STARTING_Y,
//												(int) WorldPanoramaConfig.PLAYER_STARTING_Z),
//										WorldPanoramaConfig.PANORAMA_YAW, WorldPanoramaConfig.PANORAMA_PITCH));
//								return new WorldStem(resourceManager, dataPackResources, registries, worldDataAndGenSettings);
//							}));
//		}
//		return original.call(instance, packConfig, worldDataGetter, worldDataSupplier);
//	}
}
