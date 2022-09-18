package corgitaco.enhancedcelestials.block;

import corgitaco.enhancedcelestials.core.EnhancedCelestialsBlockTags;
import corgitaco.enhancedcelestials.entity.SpaceMossBugEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface SpaceMossGrowthBlock {

    default boolean survives(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos.below()).is(EnhancedCelestialsBlockTags.SPACE_MOSS_GROWS_ON);
    }

    default void collideWith(Entity entity, RandomSource randomSource) {
        if (entity instanceof SpaceMossBugEntity spaceMossBug && spaceMossBug.getSporeDelay() == 0 && !spaceMossBug.isCoveredInSpores() && randomSource.nextInt(48) == 0) {
            spaceMossBug.setCoveredInSpores(true);
        }
    }
}
