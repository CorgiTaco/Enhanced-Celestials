package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Chunk.class)
public interface ChunkAccess {


    @Accessor
    World getWorld();
}
