package corgitaco.enhancedcelestials.block;

import corgitaco.enhancedcelestials.core.EnhancedCelestialsBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class SpaceMossCarpetBlock extends CarpetBlock {

    public SpaceMossCarpetBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        return doesSurvive($$0, $$1, $$2);
    }

    public static boolean doesSurvive(BlockState blockState, LevelReader reader, BlockPos blockPos) {
        return reader.getBlockState(blockPos.below()).is(EnhancedCelestialsBlockTags.SPACE_MOSS_GROWS_ON);
    }
}
