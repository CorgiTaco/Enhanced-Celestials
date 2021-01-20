package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class MixinBedBlock {

    @Inject(method = "onBlockActivated", at = @At("HEAD"), cancellable = true)
    private void cancelSleeping(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit, CallbackInfoReturnable<ActionResultType> cir) {
        if (EnhancedCelestials.currentLunarEvent.stopSleeping(player)) {
            cir.setReturnValue(ActionResultType.FAIL);
        }
    }
}
