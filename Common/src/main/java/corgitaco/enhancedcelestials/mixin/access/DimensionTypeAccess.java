package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DimensionType.class)
public interface DimensionTypeAccess {


    @Accessor("effectsLocation")
    ResourceLocation getEffectsServerSafe();
}
