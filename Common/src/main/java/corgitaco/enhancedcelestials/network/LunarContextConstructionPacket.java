package corgitaco.enhancedcelestials.network;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class LunarContextConstructionPacket implements S2CPacket {

    private final LunarContext lunarContext;

    public LunarContextConstructionPacket(LunarContext lunarContext) {
        this.lunarContext = lunarContext;
    }

    public static LunarContextConstructionPacket readFromPacket(FriendlyByteBuf buf) {
        try {
            return new LunarContextConstructionPacket(buf.readWithCodec(LunarContext.PACKET_CODEC));
        } catch (Exception e) {
            throw new IllegalStateException("Lunar Context packet could not be read. This is really really bad...\n\n" + e.getMessage());
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        try {
            buf.writeWithCodec(LunarContext.PACKET_CODEC, this.lunarContext);
        } catch (Exception e) {
            throw new IllegalStateException("Lunar Context packet could not be written to. This is really really bad...\n\n" + e.getMessage());

        }
    }

    @Override
    public void handle(Level level) {
        if (level != null) {
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
            if (lunarContext == null) {
                ((EnhancedCelestialsWorldData) level).setLunarContext(new LunarContext(this.lunarContext.getLunarForecast(), this.lunarContext.getLunarTimeSettings(),
                    level.dimension().location(), this.lunarContext.getLunarEvents(), true));
            }
        }
    }
}