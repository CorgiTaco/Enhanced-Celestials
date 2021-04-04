package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock {
    @Inject(method = "spawnAsEntity", at = @At("HEAD"))
    private static void modifyDrops(World world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (world instanceof ServerWorld) {
            if (EnhancedCelestialsUtils.isOverworld(world.getDimensionKey())) {
                EnhancedCelestials.currentLunarEvent.multiplyDrops((ServerWorld) world, stack);
            }
        }
    }
}