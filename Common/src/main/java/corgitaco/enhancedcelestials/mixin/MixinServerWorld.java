package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class MixinServerWorld {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectLunarContext(MinecraftServer $$0, Executor $$1, LevelStorageSource.LevelStorageAccess $$2, ServerLevelData $$3, ResourceKey<Level> $$4, LevelStem stem, ChunkProgressListener $$6, boolean $$7, long $$8, List $$9, boolean $$10, CallbackInfo ci) {
        ((EnhancedCelestialsWorldData) this).setLunarContext(LunarContext.forLevel((ServerLevel) (Object) this, Optional.empty()));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void attachLunarTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this).getLunarContext();
        if (lunarContext != null) {
            lunarContext.tick((ServerLevel) (Object) this);
        }
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void saveLunarContext(ProgressListener $$0, boolean $$1, boolean $$2, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this).getLunarContext();
        if (lunarContext != null) {
            lunarContext.save((ServerLevel) (Object) this);
        }
    }
}
