package corgitaco.enhancedcelestials.network;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class LunarEventChangedPacket implements S2CPacket {

    private final String eventKey;

    public LunarEventChangedPacket(String eventKey) {
        this.eventKey = eventKey;
    }

    public static LunarEventChangedPacket readFromPacket(FriendlyByteBuf buf) {
        return new LunarEventChangedPacket(buf.readUtf());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.eventKey);
    }

    @Override
    public void handle(Level level) {
        if (level != null) {
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
            if (lunarContext != null) {
                lunarContext.setLastEvent(lunarContext.getCurrentEvent());
                lunarContext.setCurrentEvent(this.eventKey);
                lunarContext.setStrength(0);
            }
        }
    }
}
