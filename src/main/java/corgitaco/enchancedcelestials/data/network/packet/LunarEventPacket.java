package corgitaco.enchancedcelestials.data.network.packet;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class LunarEventPacket {
    private final String event;

    public LunarEventPacket(String event) {
        this.event = event;
    }

    public static void writeToPacket(LunarEventPacket packet, PacketBuffer buf) {
        buf.writeString(packet.event);
    }

    public static LunarEventPacket readFromPacket(PacketBuffer buf) {
        return new LunarEventPacket(buf.readString());
    }

    public static void handle(LunarEventPacket message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.world != null && minecraft.player != null) {
                    EnhancedCelestials.getLunarData(minecraft.world).setEvent(message.event);
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}