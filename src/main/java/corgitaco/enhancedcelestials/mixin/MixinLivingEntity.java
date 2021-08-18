package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {


    public MixinLivingEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void lunarEntityTick(CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.world).getLunarContext();
        if (lunarContext != null) {
            lunarContext.getCurrentEvent().livingEntityTick((LivingEntity) (Object) this, this.world);
        }
    }

    @Inject(method = "isInValidBed", at = @At("HEAD"), cancellable = true)
    private void blockSleeping(CallbackInfoReturnable<Boolean> cir) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.world).getLunarContext();
        if (lunarContext != null) {
            if (lunarContext.getCurrentEvent().blockSleeping()) {
                if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) (Object) this).sendStatusMessage(new TranslationTextComponent("enhancedcelestials.sleep.fail").mergeStyle(TextFormatting.RED), true);
                }
                cir.setReturnValue(false);
            }
        }
    }
}
