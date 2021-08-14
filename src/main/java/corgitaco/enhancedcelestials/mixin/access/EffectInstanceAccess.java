package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EffectInstance.class)
public interface EffectInstanceAccess {


    @Accessor
    Effect getPotion();
}
