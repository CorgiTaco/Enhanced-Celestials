package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.network.NetworkHandler;
import corgitaco.enhancedcelestials.network.packet.LunarContextConstructionPacket;
import corgitaco.enhancedcelestials.network.packet.LunarEventChangedPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList {

    @Inject(method = "sendWorldInfo", at = @At(value = "HEAD"))
    private void sendContext(ServerPlayerEntity playerIn, ServerWorld worldIn, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) worldIn).getLunarContext();
        if (lunarContext != null) {
            NetworkHandler.sendToPlayer(playerIn, new LunarContextConstructionPacket(lunarContext));
            NetworkHandler.sendToPlayer(playerIn, new LunarEventChangedPacket(lunarContext.getCurrentEvent().getKey()));
        }
    }
}