package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEffectInstance.class)
public interface EffectInstanceAccess {


    @Accessor
    MobEffect getEffect();
}
