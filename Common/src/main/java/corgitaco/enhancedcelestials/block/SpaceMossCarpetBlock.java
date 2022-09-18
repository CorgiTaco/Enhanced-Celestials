package corgitaco.enhancedcelestials.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class SpaceMossCarpetBlock extends CarpetBlock implements SpaceMossGrowthBlock {

    public SpaceMossCarpetBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        collideWith($$3, $$1.random);
    }

    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        return survives($$0, $$1, $$2);
    }
}
