package dev.corgitaco.enhancedcelestials.mixin;

import dev.corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import dev.corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(Level.class)
public abstract class MixinWorld implements EnhancedCelestialsWorldData {


    @Nullable
    private EnhancedCelestialsContext enhancedCelestialsContext;

    @Nullable
    @Override
    public EnhancedCelestialsContext getLunarContext() {
        return this.enhancedCelestialsContext;
    }

    @Override
    public EnhancedCelestialsContext setLunarContext(EnhancedCelestialsContext enhancedCelestialsContext) {
        this.enhancedCelestialsContext = enhancedCelestialsContext;
        return this.enhancedCelestialsContext;
    }
}
