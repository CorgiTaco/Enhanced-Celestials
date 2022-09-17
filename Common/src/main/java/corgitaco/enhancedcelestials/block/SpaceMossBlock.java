package corgitaco.enhancedcelestials.block;

import corgitaco.enhancedcelestials.core.ECBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class SpaceMossBlock extends Block {

    public SpaceMossBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public void randomTick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        var x = $$3.nextInt(5) - 2;
        var y = $$3.nextInt(5) - 2;
        var z = $$3.nextInt(5) - 2;

        var blockPos = $$2.offset(x, y, z);

        var blockState = ($$3.nextBoolean() ? ECBlocks.SPACE_MOSS_CARPET : ECBlocks.SPACE_MOSS_GRASS).get().defaultBlockState();

        if ($$1.isEmptyBlock(blockPos) && blockState.canSurvive($$1, blockPos)) {
            $$1.setBlock(blockPos, blockState, Block.UPDATE_CLIENTS);
        }

        super.randomTick($$0, $$1, $$2, $$3);
    }
}
