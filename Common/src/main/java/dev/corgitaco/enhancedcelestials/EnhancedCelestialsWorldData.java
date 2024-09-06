package dev.corgitaco.enhancedcelestials;

import dev.corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;

import javax.annotation.Nullable;

public interface EnhancedCelestialsWorldData {

    @Nullable
    EnhancedCelestialsContext getLunarContext();

    EnhancedCelestialsContext setLunarContext(EnhancedCelestialsContext enhancedCelestialsContext);
}
