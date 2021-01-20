package corgitaco.enchancedcelestials.mixin.client;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Inject(method = "getCloudColor", at = @At("RETURN"), cancellable = true)
    private void modifyCloudColor(float partialTicks, CallbackInfoReturnable<Vector3d> cir) {
        if (EnhancedCelestialsUtils.isOverworld(((ClientWorld) (Object) this).getDimensionKey())) {
            int rgbColor = EnhancedCelestials.currentLunarEvent.modifyCloudColor(EnhancedCelestialsUtils.transformFloatColor(cir.getReturnValue())).getRGB();
            float r = (float) (rgbColor >> 16 & 255) / 255.0F;
            float g = (float) (rgbColor >> 8 & 255) / 255.0F;
            float b = (float) (rgbColor & 255) / 255.0F;
            cir.setReturnValue(new Vector3d(r, g, b));
        }
    }
}
