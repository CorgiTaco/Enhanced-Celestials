package corgitaco.enhancedcelestials.mixin.client;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public class MixinClientWorld implements EnhancedCelestialsWorldData {

    private LunarContext lunarContext;

    @Nullable
    @Override
    public LunarContext getLunarContext() {
        return this.lunarContext;
    }

    @Override
    public LunarContext setLunarContext(LunarContext lunarContext) {
        this.lunarContext = lunarContext;
        return this.lunarContext;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void attachLunarTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (lunarContext != null) {
            lunarContext.tick((ClientLevel) (Object) this);
        }
    }
}
