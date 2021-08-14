package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DimensionType.class)
public interface DimensionTypeAccess {


    @Accessor("effects")
    ResourceLocation getEffectsServerSafe();
}
