package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;

import javax.annotation.Nullable;

public interface EnhancedCelestialsWorldData {

    @Nullable
    EnhancedCelestialsContext getLunarContext();

    EnhancedCelestialsContext setLunarContext(EnhancedCelestialsContext enhancedCelestialsContext);
}
