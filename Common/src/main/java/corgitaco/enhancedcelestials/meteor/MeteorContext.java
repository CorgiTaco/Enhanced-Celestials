package corgitaco.enhancedcelestials.meteor;

import corgitaco.enhancedcelestials.entity.MeteorEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MeteorContext {

    public MeteorContext(Level level) {
    }


    public void tick(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.random.nextInt(1000) == 0) {
                MeteorEntity entity = new MeteorEntity(level);
                entity.setDeltaMovement(new Vec3(0.5, -0.5, 0.5));

                level.addFreshEntity(entity);
            }
        }
    }

}
