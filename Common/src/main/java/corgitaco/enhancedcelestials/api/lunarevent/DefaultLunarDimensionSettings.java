package corgitaco.enhancedcelestials.api.lunarevent;

import corgitaco.enhancedcelestials.api.EnhancedCelestialsBuiltinRegistries;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import net.minecraft.Util;
import net.minecraft.core.Holder;

import java.util.IdentityHashMap;
import java.util.function.Supplier;

public class DefaultLunarDimensionSettings {
    public static final RegistrationProvider<LunarDimensionSettings> LUNAR_EVENT_DIMENSION_SETTINGS = RegistrationProvider.get(EnhancedCelestialsBuiltinRegistries.LUNAR_EVENT_DIMENSION_SETTING, "minecraft");

    public static final Holder<LunarDimensionSettings> OVERWORLD_LUNAR_SETTINGS = createEvent("overworld", () -> new LunarDimensionSettings(Util.make(new IdentityHashMap<>(), map -> {
        map.put(DefaultLunarEvents.BLOOD_MOON, new LunarDimensionSettings.Entry(0.07, 4));
        map.put(DefaultLunarEvents.SUPER_BLOOD_MOON, new LunarDimensionSettings.Entry(0.035, 20));
        map.put(DefaultLunarEvents.HARVEST_MOON, new LunarDimensionSettings.Entry(0.035, 4));
        map.put(DefaultLunarEvents.SUPER_HARVEST_MOON, new LunarDimensionSettings.Entry(0.0175, 20));
        map.put(DefaultLunarEvents.BLUE_MOON, new LunarDimensionSettings.Entry(0.02, 4));
        map.put(DefaultLunarEvents.SUPER_BLUE_MOON, new LunarDimensionSettings.Entry(0.01, 20));
    }), DefaultLunarEvents.DEFAULT, 24000L, 100, 10));


    public static Holder<LunarDimensionSettings> createEvent(String id, Supplier<LunarDimensionSettings> event) {
        return LUNAR_EVENT_DIMENSION_SETTINGS.register(id, event).asHolder();
    }

    public static void loadClass(){}
}
