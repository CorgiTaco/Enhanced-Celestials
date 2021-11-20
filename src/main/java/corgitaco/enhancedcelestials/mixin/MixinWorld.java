package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(World.class)
public abstract class MixinWorld implements EnhancedCelestialsWorldData {


    @Nullable
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
}
