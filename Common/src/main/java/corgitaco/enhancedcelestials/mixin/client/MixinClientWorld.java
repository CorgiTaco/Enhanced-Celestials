package corgitaco.enhancedcelestials.mixin.client;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.lunarevent.LunarContext;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public abstract class MixinClientWorld {

    @Inject(method = "tick", at = @At("HEAD"))
    private void attachLunarTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this).getLunarContext();
        if (lunarContext != null) {
            lunarContext.tick((ClientLevel) (Object) this);
        }
    }
}
