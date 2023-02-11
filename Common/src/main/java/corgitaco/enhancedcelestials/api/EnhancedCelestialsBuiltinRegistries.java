package corgitaco.enhancedcelestials.api;

import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.mixin.access.BuiltinRegistriesAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;

public class EnhancedCelestialsBuiltinRegistries {

    public static final Registry<LunarEvent> LUNAR_EVENT = BuiltinRegistriesAccess.ec_invokeRegisterSimple(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, registry -> DefaultLunarEvents.DEFAULT.asHolder());
    public static final Registry<LunarDimensionSettings> LUNAR_EVENT_DIMENSION_SETTING = BuiltinRegistriesAccess.ec_invokeRegisterSimple(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, registry -> DefaultLunarDimensionSettings.OVERWORLD_LUNAR_SETTINGS.asHolder());

    public static void init(){}

    static {
        Registry.checkRegistry(BuiltinRegistriesAccess.ec_getWRITABLE_REGISTRY());
        BuiltinRegistriesAccess.ec_setRegistryAccess(RegistryAccess.fromRegistryOfRegistries(BuiltinRegistries.REGISTRY));
    }
}