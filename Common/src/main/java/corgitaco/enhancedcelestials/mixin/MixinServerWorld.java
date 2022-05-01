package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.mixin.access.DimensionTypeAccess;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class MixinServerWorld {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectLunarContext(MinecraftServer $$0, Executor $$1, LevelStorageSource.LevelStorageAccess $$2, ServerLevelData $$3, ResourceKey $$4, Holder<DimensionType> dimensionType, ChunkProgressListener $$6, ChunkGenerator $$7, boolean $$8, long $$9, List $$10, boolean $$11, CallbackInfo ci) {
        if (((DimensionTypeAccess) dimensionType.value()).getEffectsServerSafe().equals(DimensionType.OVERWORLD_EFFECTS)) {
            ((EnhancedCelestialsWorldData) this).setLunarContext(new LunarContext((ServerLevel) (Object) this));
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void attachLunarTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this).getLunarContext();
        if (lunarContext != null) {
            lunarContext.tick((ServerLevel) (Object) this);
        }
    }
}
