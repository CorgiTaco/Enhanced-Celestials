package corgitaco.enhancedcelestials.network.packet;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarForecast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;

import java.io.IOException;

public class LunarForecastChangedPacket {

    private final LunarForecast lunarForecast;

    public LunarForecastChangedPacket(LunarForecast lunarForecast) {
        this.lunarForecast = lunarForecast;
    }

    public static void writeToPacket(LunarForecastChangedPacket packet, FriendlyByteBuf buf) {
        try {
            buf.writeWithCodec(LunarForecast.CODEC, packet.lunarForecast);
        } catch (IOException e) {
            throw new IllegalStateException("Lunar Forecast packet could not be written to. This is really really bad...\n\n" + e.getMessage());

        }
    }

    public static LunarForecastChangedPacket readFromPacket(FriendlyByteBuf buf) {
        try {
            return new LunarForecastChangedPacket(buf.readWithCodec(LunarForecast.CODEC));
        } catch (IOException e) {
            throw new IllegalStateException("Lunar Forecast packet could not be read. This is really really bad...\n\n" + e.getMessage());
        }
    }

    public static void handle(LunarForecastChangedPacket message) {
        Minecraft minecraft = Minecraft.getInstance();

        ClientLevel world = minecraft.level;
        if (world != null && minecraft.player != null) {
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
            if (lunarContext != null) {
                lunarContext.getLunarForecast().getForecast().clear();
                lunarContext.getLunarForecast().getForecast().addAll(message.lunarForecast.getForecast());
                lunarContext.getLunarForecast().setLastCheckedGameTime(message.lunarForecast.getLastCheckedGameTime());
            }
        }
    }
}