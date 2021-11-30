package corgitaco.enhancedcelestials.network.packet;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LunarEventChangedPacket {

    private final String eventKey;

    public LunarEventChangedPacket(String eventKey) {
        this.eventKey = eventKey;
    }

    public static void writeToPacket(LunarEventChangedPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.eventKey);
    }

    public static LunarEventChangedPacket readFromPacket(FriendlyByteBuf buf) {
        return new LunarEventChangedPacket(buf.readUtf());
    }

    public static void handle(LunarEventChangedPacket message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();

                ClientLevel world = minecraft.level;
                if (world != null && minecraft.player != null) {
                    LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
                    if (lunarContext != null) {
                        lunarContext.setLastEvent(lunarContext.getCurrentEvent());
                        lunarContext.setCurrentEvent(message.eventKey);
                        lunarContext.setStrength(0);
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
