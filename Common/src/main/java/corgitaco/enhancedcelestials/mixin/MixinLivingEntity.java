package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {


    public MixinLivingEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void lunarEntityTick(CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            lunarContext.getLunarForecast().getCurrentEvent().value().livingEntityTick((LivingEntity) (Object) this, this.level);
        }
    }

    @Inject(method = "checkBedExists", at = @At("HEAD"), cancellable = true)
    private void blockSleeping(CallbackInfoReturnable<Boolean> cir) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            if (lunarContext.getLunarForecast().getCurrentEvent().value().blockSleeping()) {
                if (((LivingEntity) (Object) this) instanceof ServerPlayer) {
                    ((ServerPlayer) (Object) this).displayClientMessage(Component.translatable("enhancedcelestials.sleep.fail").withStyle(ChatFormatting.RED), true);
                }
                cir.setReturnValue(false);
            }
        }
    }
}
