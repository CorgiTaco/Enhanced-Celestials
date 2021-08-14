package corgitaco.enhancedcelestials.network.packet;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class LunarEventChangedPacket {

    private final String eventKey;

    public LunarEventChangedPacket(String eventKey) {
        this.eventKey = eventKey;
    }

    public static void writeToPacket(LunarEventChangedPacket packet, PacketBuffer buf) {
        buf.writeString(packet.eventKey);
    }

    public static LunarEventChangedPacket readFromPacket(PacketBuffer buf) {
        return new LunarEventChangedPacket(buf.readString());
    }

    public static void handle(LunarEventChangedPacket message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();

                ClientWorld world = minecraft.world;
                if (world != null && minecraft.player != null) {
                    LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
                    if (lunarContext != null) {
                        lunarContext.setCurrentEvent(message.eventKey);
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
