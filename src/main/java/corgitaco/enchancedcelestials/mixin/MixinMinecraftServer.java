package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.data.world.LunarData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Shadow @Nullable public abstract ServerWorld getWorld(RegistryKey<World> dimension);

    @Inject(method = "func_240787_a_", at = @At("RETURN"))
    private void assignWorldData(IChunkStatusListener statusListener, CallbackInfo ci) {
        EnhancedCelestials.lunarData = LunarData.get(getWorld(World.OVERWORLD));
    }
}
