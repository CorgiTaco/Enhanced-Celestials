package corgitaco.enhancedcelestials.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class SpaceMossGrassBlock extends BushBlock {

    public SpaceMossGrassBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        return SpaceMossCarpetBlock.doesSurvive($$0, $$1, $$2);
    }
}
