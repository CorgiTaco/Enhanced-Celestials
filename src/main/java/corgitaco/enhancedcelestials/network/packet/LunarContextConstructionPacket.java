package corgitaco.enhancedcelestials.network.packet;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;

import java.io.IOException;

public class LunarContextConstructionPacket {

    private final LunarContext lunarContext;

    public LunarContextConstructionPacket(LunarContext lunarContext) {
        this.lunarContext = lunarContext;
    }

    public static void writeToPacket(LunarContextConstructionPacket packet, FriendlyByteBuf buf) {
        try {
            buf.writeWithCodec(LunarContext.PACKET_CODEC, packet.lunarContext);
        } catch (Exception e) {
            throw new IllegalStateException("Lunar Context packet could not be written to. This is really really bad...\n\n" + e.getMessage());

        }
    }

    public static LunarContextConstructionPacket readFromPacket(FriendlyByteBuf buf) {
        try {
            return new LunarContextConstructionPacket(buf.readWithCodec(LunarContext.PACKET_CODEC));
        } catch (Exception e) {
            throw new IllegalStateException("Lunar Context packet could not be read. This is really really bad...\n\n" + e.getMessage());
        }
    }

    public static void handle(LunarContextConstructionPacket message) {
        Minecraft minecraft = Minecraft.getInstance();

        ClientLevel world = minecraft.level;
        if (world != null && minecraft.player != null) {
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
            if (lunarContext == null) {
                ((EnhancedCelestialsWorldData) world).setLunarContext(new LunarContext(message.lunarContext.getLunarForecast(), message.lunarContext.getLunarTimeSettings(),
                        world.dimension().location(), message.lunarContext.getLunarEvents(), true));
            }
        }
    }
}