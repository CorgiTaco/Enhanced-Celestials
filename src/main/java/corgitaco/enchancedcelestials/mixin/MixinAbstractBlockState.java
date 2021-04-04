package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class MixinAbstractBlockState {
    @Shadow public abstract Block getBlock();
    @Shadow protected abstract BlockState getSelf();

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void cropGrowthModifier(ServerWorld world, BlockPos pos, Random randomIn, CallbackInfo ci) {
        if (EnhancedCelestialsUtils.isOverworld(world.getDimensionKey())) {
            if (EnhancedCelestials.currentLunarEvent != null) {
                EnhancedCelestials.currentLunarEvent.blockTick(world, pos, this.getBlock(), this.getSelf());
            }
        }
    }
}