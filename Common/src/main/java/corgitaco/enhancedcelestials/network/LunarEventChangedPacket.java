package corgitaco.enhancedcelestials.network;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class LunarEventChangedPacket implements S2CPacket {

    private final ResourceKey<LunarEvent> eventKey;

    public LunarEventChangedPacket(ResourceKey<LunarEvent> eventKey) {
        this.eventKey = eventKey;
    }

    public static LunarEventChangedPacket readFromPacket(FriendlyByteBuf buf) {
        return new LunarEventChangedPacket(buf.readResourceKey(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceKey(this.eventKey);
    }

    @Override
    public void handle(Level level) {
        if (level != null) {
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
            if (lunarContext != null) {
                lunarContext.getLunarForecast().setCurrentEvent(this.eventKey);
            }
        }
    }
}
