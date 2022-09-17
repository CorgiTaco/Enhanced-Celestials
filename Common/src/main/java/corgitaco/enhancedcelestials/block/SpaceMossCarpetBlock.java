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
        return $$1.getBlockState($$2.below()).is(EnhancedCelestialsBlockTags.SPACE_MOSS_GROWS_ON);
    }
}
