package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.lunarevent.LunarContext;

import javax.annotation.Nullable;

public interface EnhancedCelestialsWorldData {

    @Nullable
    LunarContext getLunarContext();

    LunarContext setLunarContext(LunarContext lunarContext);
}
