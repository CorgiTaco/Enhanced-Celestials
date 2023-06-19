package corgitaco.enhancedcelestials.api.lunarevent;

import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Supplier;

public class DefaultLunarDimensionSettings {
    public static final Map<ResourceKey<LunarDimensionSettings>, LunarDimensionSettingsFactory> LUNAR_DIMENSION_SETTINGS_FACTORIES = new Reference2ObjectOpenHashMap<>();

    public static final ResourceKey<LunarDimensionSettings> OVERWORLD_LUNAR_SETTINGS = createEvent( new ResourceLocation("overworld"), () ->
            new LunarDimensionSettings(
                    DefaultLunarEvents.DEFAULT,
                    100,
                    24000L,
                    100,
                    2,
                    true
            )
    );


    public static ResourceKey<LunarDimensionSettings> createEvent(ResourceLocation location, Supplier<LunarDimensionSettings> event) {
        ResourceKey<LunarDimensionSettings> lunarEventResourceKey = ResourceKey.create(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, location);
        LUNAR_DIMENSION_SETTINGS_FACTORIES.put(lunarEventResourceKey, placedFeatureHolderGetter -> event.get());
        return lunarEventResourceKey;
    }

    public static void loadClass() {
    }

    @FunctionalInterface
    public interface LunarDimensionSettingsFactory {

        LunarDimensionSettings generate(BootstapContext<LunarDimensionSettings> placedFeatureHolderGetter);
    }
}
