package corgitaco.enhancedcelestials.api.lunarevent;

import corgitaco.enhancedcelestials.api.EnhancedCelestialsBuiltinRegistries;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import net.minecraft.core.Holder;

import java.util.function.Supplier;

public class DefaultLunarDimensionSettings {
    public static final RegistrationProvider<LunarDimensionSettings> LUNAR_EVENT_DIMENSION_SETTINGS = RegistrationProvider.get(EnhancedCelestialsBuiltinRegistries.LUNAR_EVENT_DIMENSION_SETTING, "minecraft");

    public static final Holder<LunarDimensionSettings> OVERWORLD_LUNAR_SETTINGS = createEvent("overworld", () ->
            new LunarDimensionSettings(
                    DefaultLunarEvents.DEFAULT,
                    100,
                    24000L,
                    100,
                    2
            )
    );


    public static Holder<LunarDimensionSettings> createEvent(String id, Supplier<LunarDimensionSettings> event) {
        return LUNAR_EVENT_DIMENSION_SETTINGS.register(id, event).asHolder();
    }

    public static void loadClass() {
    }
}
