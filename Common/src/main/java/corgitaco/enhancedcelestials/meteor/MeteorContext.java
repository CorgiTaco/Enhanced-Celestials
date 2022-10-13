package corgitaco.enhancedcelestials.meteor;

import corgitaco.enhancedcelestials.entity.MeteorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
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
         if (level instanceof ServerLevel serverLevel) {
             RandomSource random = serverLevel.random;
             if (random.nextInt(25000) == 0) {
                 MeteorEntity entity = new MeteorEntity(level);
                 entity.setDeltaMovement(new Vec3(Mth.randomBetween(random, -2F, 2F), -0.35, Mth.randomBetween(random, -2F, 2F)));
                 BlockPos worldPosition = chunkAccess.getPos().getWorldPosition();
                 entity.setPos(worldPosition.getX() + random.nextInt(16), level.getMaxBuildHeight() + 700, worldPosition.getZ() + random.nextInt(16));
                 entity.setSize(Mth.nextFloat(random, 1F, 8F));
                 level.addFreshEntity(entity);
             }
         }
    }
}
