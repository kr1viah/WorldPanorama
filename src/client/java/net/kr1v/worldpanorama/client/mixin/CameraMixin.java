package net.kr1v.worldpanorama.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kr1v.worldpanorama.client.config.Main;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public class CameraMixin {
	@WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;calculateFov(F)F"))
	float yeah(Camera instance, float partialTicks, Operation<Float> original){
		if(Minecraft.getInstance().screen != null && Main.ENABLED.get()){
			return Main.PANORAMA_FOV.get();
		}
		else {
			return original.call(instance, partialTicks);
		}
	}
}