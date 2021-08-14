package corgitaco.enhancedcelestials.network.packet;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.function.Supplier;

public class LunarContextConstructionPacket {

    private final LunarContext lunarContext;

    public LunarContextConstructionPacket(LunarContext lunarContext) {
        this.lunarContext = lunarContext;
    }

    public static void writeToPacket(LunarContextConstructionPacket packet, PacketBuffer buf) {
        try {
            buf.func_240629_a_(LunarContext.PACKET_CODEC, packet.lunarContext);
        } catch (IOException e) {
            throw new IllegalStateException("Lunar Context packet could not be written to. This is really really bad...\n\n" + e.getMessage());

        }
    }

    public static LunarContextConstructionPacket readFromPacket(PacketBuffer buf) {
        try {
            return new LunarContextConstructionPacket(buf.func_240628_a_(LunarContext.PACKET_CODEC));
        } catch (IOException e) {
            throw new IllegalStateException("Lunar Context packet could not be read. This is really really bad...\n\n" + e.getMessage());
        }
    }

    public static void handle(LunarContextConstructionPacket message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();

                ClientWorld world = minecraft.world;
                if (world != null && minecraft.player != null) {
                    LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
                    if (lunarContext == null) {
                        ((EnhancedCelestialsWorldData) world).setLunarContext(new LunarContext(message.lunarContext.getLunarForecast(),
                                world.getDimensionKey().getLocation(), message.lunarContext.getLunarEvents()));
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}