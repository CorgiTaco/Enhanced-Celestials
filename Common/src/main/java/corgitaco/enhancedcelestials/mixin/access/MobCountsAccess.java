package corgitaco.enhancedcelestials.mixin.access;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LocalMobCapCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalMobCapCalculator.MobCounts.class)
public interface MobCountsAccess {

    @Accessor
    Object2IntMap<MobCategory> getCounts();
}
