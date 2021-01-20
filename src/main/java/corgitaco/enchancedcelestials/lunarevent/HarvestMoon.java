package corgitaco.enchancedcelestials.lunarevent;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.config.EnhancedCelestialsConfig;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsClientUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

public class HarvestMoon extends LunarEvent {


    static double cropGrowthMultiplier = EnhancedCelestialsConfig.harvestMoonCropGrowthChanceMultiplier.get();

    public HarvestMoon() {
        super(LunarEventSystem.HARVEST_MOON_EVENT_ID, 0.025);
    }

    @Override
    public boolean modifySkyLightMapColor(Vector3f originalLightmapColor) {
        originalLightmapColor.lerp(EnhancedCelestialsClientUtils.transformVectorColor(new Color(255, 219, 99)), 1.0F);
        return true;
    }

    @Override
    public Color modifyMoonColor() {
        return new Color(255, 219, 99, 255);
    }


    @Override
    public void blockTick(ServerWorld world, BlockPos pos, Block block, BlockState blockState, CallbackInfo ci) {
        if (BlockTags.CROPS.contains(block) || BlockTags.BEE_GROWABLES.contains(block) || BlockTags.SAPLINGS.contains(block)) {
            for (int i = 0; i < cropGrowthMultiplier; i++) {
                block.randomTick(blockState,  world, pos, world.rand);
            }
        }
    }
}
