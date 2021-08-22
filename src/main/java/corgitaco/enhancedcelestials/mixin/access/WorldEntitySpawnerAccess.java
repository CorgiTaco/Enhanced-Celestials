package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldEntitySpawner.class)
public interface WorldEntitySpawnerAccess {

    @Accessor("MAGIC_NUMBER")
    static int getMagicNumber() {
        throw new Error("Mixin did not apply!");
    }
}
