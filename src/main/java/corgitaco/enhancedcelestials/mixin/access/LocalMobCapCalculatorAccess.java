package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.LocalMobCapCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalMobCapCalculator.class)
public interface LocalMobCapCalculatorAccess {

    @Accessor
    ChunkMap getChunkMap();
}
