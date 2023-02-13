package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock {
    @Inject(method = "popResource*", at = @At("HEAD"))
    private static void modifyDrops(Level world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (!world.isClientSide) {
            EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
            if (enhancedCelestialsContext != null) {
                enhancedCelestialsContext.getLunarForecast().getCurrentEvent(world.getRainLevel(1) < 1).value().onBlockItemDrop((ServerLevel) world, stack);
            }
        }
    }
}