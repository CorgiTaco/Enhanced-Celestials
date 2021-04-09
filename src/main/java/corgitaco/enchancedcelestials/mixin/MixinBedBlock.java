package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.config.EnhancedCelestialsConfig;
import corgitaco.enchancedcelestials.lunarevent.BloodMoon;
import corgitaco.enchancedcelestials.lunarevent.LunarEventSystem;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class MixinBedBlock {
    @Inject(method = "onBlockActivated", at = @At("HEAD"), cancellable = true)
    private void cancelSleeping(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit, CallbackInfoReturnable<ActionResultType> cir) {
        if (!EnhancedCelestialsConfig.bloodMoonCanSleep.get() && EnhancedCelestials.currentLunarEvent instanceof BloodMoon && !state.get(BedBlock.OCCUPIED)) {
            cir.setReturnValue(ActionResultType.FAIL);
            player.sendStatusMessage(new TranslationTextComponent("enhancedcelestials.sleep.fail.lunar_event", new TranslationTextComponent(LunarEventSystem.BLOOD_MOON_EVENT.getName())), true);
        }
    }
}