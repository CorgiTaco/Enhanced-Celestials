package dev.corgitaco.enhancedcelestials;

import dev.corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;

import org.jetbrains.annotations.Nullable;

public interface EnhancedCelestialsWorldData {

    @Nullable
    EnhancedCelestialsContext getLunarContext();

    EnhancedCelestialsContext setLunarContext(EnhancedCelestialsContext enhancedCelestialsContext);
}
