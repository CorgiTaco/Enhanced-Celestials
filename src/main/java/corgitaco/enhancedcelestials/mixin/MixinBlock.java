package corgitaco.enhancedcelestials.mixin;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class MixinBlock {
//    @Inject(method = "spawnAsEntity", at = @At("HEAD"))
//    private static void modifyDrops(World world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
//        if (world instanceof ServerWorld) {
//            if (EnhancedCelestialsUtils.isOverworld(world.getDimensionKey())) {
//                EnhancedCelestials.currentLunarEvent.multiplyDrops((ServerWorld) world, stack);
//            }
//        }
//    }
}