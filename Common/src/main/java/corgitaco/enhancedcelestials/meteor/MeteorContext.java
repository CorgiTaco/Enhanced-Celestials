package corgitaco.enhancedcelestials.meteor;

import corgitaco.enhancedcelestials.entity.MeteorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;

public class MeteorContext {

    public MeteorContext() {
    }


    public void tick(Level level) {

    }

    public void chunkTick(Level level, ChunkAccess chunkAccess) {
        // if (level instanceof ServerLevel serverLevel) {
        //     RandomSource random = serverLevel.random;
        //     if (random.nextInt(1000) == 0) {
        //         MeteorEntity entity = new MeteorEntity(level);
        //         entity.setDeltaMovement(new Vec3(random.nextGaussian(), -0.5, random.nextGaussian() ));
        //
        //         BlockPos worldPosition = chunkAccess.getPos().getWorldPosition();
        //         entity.setPos(worldPosition.getX() + random.nextInt(16), level.getMaxBuildHeight() + 2000, worldPosition.getZ() + random.nextInt(16));
        //         entity.setSize((byte) random.nextInt(2, 5));
        //         level.addFreshEntity(entity);
        //     }
        // }
    }
}
