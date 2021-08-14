package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.mixin.access.DimensionTypeAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.ISpecialSpawner;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.SaveFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class MixinServerWorld implements EnhancedCelestialsWorldData {

    @Nullable
    private LunarContext lunarContext;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectLunarContext(MinecraftServer server, Executor executor, SaveFormat.LevelSave save, IServerWorldInfo worldInfo, RegistryKey<World> key, DimensionType dimensionType, IChunkStatusListener statusListener, ChunkGenerator generator, boolean b, long seed, List<ISpecialSpawner> specialSpawners, boolean b1, CallbackInfo ci) {
        if (((DimensionTypeAccess) dimensionType).getEffectsServerSafe().equals(DimensionType.OVERWORLD_ID)) {
            lunarContext = new LunarContext((ServerWorld) (Object) this);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void attachLunarTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (lunarContext != null) {
            lunarContext.tick((ServerWorld) (Object) this);
        }
    }

    @Nullable
    @Override
    public LunarContext getLunarContext() {
        return this.lunarContext;
    }

    @Override
    public LunarContext setLunarContext(LunarContext lunarContext) {
        this.lunarContext = lunarContext;
        return this.lunarContext;
    }
}
