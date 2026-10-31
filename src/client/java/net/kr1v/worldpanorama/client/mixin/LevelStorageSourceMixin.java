package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.kr1v.worldpanorama.client.WorldPanoramaClient;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelStorageSource.class)
public class LevelStorageSourceMixin {
	//~ if 1.21.11 'lambda$loadLevelSummaries$0' -> 'method_43418'
	@WrapOperation(method = "lambda$loadLevelSummaries$0", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
	private void wrap(Logger instance, String s, Object o, Object object, Operation<Void> original, @Local(argsOnly = true) LevelStorageSource.LevelDirectory level) {
		if (level.directoryName().equals(WorldPanoramaClient.getWorldName())) {
			return;
		}
		original.call(instance, s, o, object);
	}
}
