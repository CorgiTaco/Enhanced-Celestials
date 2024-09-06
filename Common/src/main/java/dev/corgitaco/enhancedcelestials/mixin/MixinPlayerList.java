package dev.corgitaco.enhancedcelestials.mixin;

import dev.corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import dev.corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import dev.corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import dev.corgitaco.enhancedcelestials.network.LunarContextConstructionPacket;
import dev.corgitaco.enhancedcelestials.network.LunarForecastChangedPacket;
import dev.corgitaco.enhancedcelestials.platform.services.IPlatformHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList {

    @Inject(method = "sendLevelInfo", at = @At(value = "RETURN"))
    private void sendContext(ServerPlayer playerIn, ServerLevel worldIn, CallbackInfo ci) {
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) worldIn).getLunarContext();
        if (enhancedCelestialsContext != null) {
            LunarForecast.Data data = enhancedCelestialsContext.getLunarForecast().data();
            IPlatformHelper.PLATFORM.sendToClient(playerIn, new LunarContextConstructionPacket(data));
            IPlatformHelper.PLATFORM.sendToClient(playerIn, new LunarForecastChangedPacket(data, worldIn.isNight()));
        }
    }
}