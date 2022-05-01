package corgitaco.enhancedcelestials.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public interface S2CPacket {

    void write(FriendlyByteBuf buf);

    void handle(Level level);
}
