package corgitaco.enhancedcelestials;

import javax.annotation.Nullable;

public interface EnhancedCelestialsWorldData {

    @Nullable
    LunarContext getLunarContext();

    LunarContext setLunarContext(LunarContext lunarContext);
}
