package corgitaco.enhancedcelestials.network.packet;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarForecast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.function.Supplier;

public class LunarForecastChangedPacket {

    private final LunarForecast lunarForecast;

    public LunarForecastChangedPacket(LunarForecast lunarForecast) {
        this.lunarForecast = lunarForecast;
    }

    public static void writeToPacket(LunarForecastChangedPacket packet, PacketBuffer buf) {
        try {
            buf.func_240629_a_(LunarForecast.CODEC, packet.lunarForecast);
        } catch (IOException e) {
            throw new IllegalStateException("Lunar Forecast packet could not be written to. This is really really bad...\n\n" + e.getMessage());

        }
    }

    public static LunarForecastChangedPacket readFromPacket(PacketBuffer buf) {
        try {
            return new LunarForecastChangedPacket(buf.func_240628_a_(LunarForecast.CODEC));
        } catch (IOException e) {
            throw new IllegalStateException("Lunar Forecast packet could not be read. This is really really bad...\n\n" + e.getMessage());
        }
    }

    public static void handle(LunarForecastChangedPacket message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();

                ClientWorld world = minecraft.world;
                if (world != null && minecraft.player != null) {
                    LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
                    if (lunarContext == null) {
                        lunarContext.getLunarForecast().getForecast().clear();
                        lunarContext.getLunarForecast().getForecast().addAll(message.lunarForecast.getForecast());
                        lunarContext.getLunarForecast().setLastCheckedGameTime(message.lunarForecast.getLastCheckedGameTime());
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}