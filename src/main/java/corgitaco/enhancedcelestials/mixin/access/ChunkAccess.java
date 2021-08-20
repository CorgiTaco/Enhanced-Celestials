package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelChunk.class)
public interface ChunkAccess {


    @Accessor
    Level getLevel();
}
