package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.misc.AdditionalEntityDensityManagerData;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldEntitySpawner.class)
public class MixinEntityWorldSpawner {
    @Inject(method = "func_234979_a_", at = @At("HEAD"))
    private static void bindData(ServerWorld world, Chunk chunk, WorldEntitySpawner.EntityDensityManager densityManager, boolean p_234979_3_, boolean p_234979_4_, boolean p_234979_5_, CallbackInfo ci) {
        ((AdditionalEntityDensityManagerData) densityManager).setIsOverworld(world.getDimensionKey() == World.OVERWORLD);
    }
}