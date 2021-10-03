package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.network.NetworkHandler;
import corgitaco.enhancedcelestials.network.packet.LunarContextConstructionPacket;
import corgitaco.enhancedcelestials.network.packet.LunarEventChangedPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList {

    @Inject(method = "sendLevelInfo", at = @At(value = "HEAD"))
    private void sendContext(ServerPlayer playerIn, ServerLevel worldIn, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) worldIn).getLunarContext();
        if (lunarContext != null) {
            NetworkHandler.sendToPlayer(playerIn, new LunarContextConstructionPacket(lunarContext));
            NetworkHandler.sendToPlayer(playerIn, new LunarEventChangedPacket(lunarContext.getCurrentEvent().getKey()));
        }
    }
}