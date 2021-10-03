package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.helper.LevelGetter;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkCache.class)
public class MixinServerChunkProvider {

    @Shadow @Final
    private ServerLevel level;

    @Shadow @Nullable private NaturalSpawner.SpawnState lastSpawnState;

    @Inject(method = "tickChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", ordinal = 0))
    private void setDensityManagerLevel(CallbackInfo ci) {
        ((LevelGetter) this.lastSpawnState).setLevel(this.level);
    }
}
