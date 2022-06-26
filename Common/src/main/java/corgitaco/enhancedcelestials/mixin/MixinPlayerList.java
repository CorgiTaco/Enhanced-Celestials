package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarForecast;
import corgitaco.enhancedcelestials.network.LunarContextConstructionPacket;
import corgitaco.enhancedcelestials.network.LunarForecastChangedPacket;
import corgitaco.enhancedcelestials.platform.Services;
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
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) worldIn).getLunarContext();
        if (lunarContext != null) {
            LunarForecast.SaveData saveData = lunarContext.getLunarForecast().saveData();
            Services.PLATFORM.sendToClient(playerIn, new LunarContextConstructionPacket(saveData));
            Services.PLATFORM.sendToClient(playerIn, new LunarForecastChangedPacket(saveData));
        }
    }
}