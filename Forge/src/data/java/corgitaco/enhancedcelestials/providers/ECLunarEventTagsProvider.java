package corgitaco.enhancedcelestials.providers;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents.*;

public class ECLunarEventTagsProvider extends TagsProvider<LunarEvent> {
    public ECLunarEventTagsProvider(PackOutput pOutput, ResourceKey<? extends Registry<LunarEvent>> pRegistryKey, CompletableFuture<HolderLookup.Provider> pLookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pRegistryKey, pLookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(TagKey.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, EnhancedCelestials.createLocation("blood_moon"))).add(BLOOD_MOON, SUPER_BLOOD_MOON);
        tag(TagKey.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, EnhancedCelestials.createLocation("blue_moon"))).add(BLUE_MOON, SUPER_BLUE_MOON);
        tag(TagKey.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, EnhancedCelestials.createLocation("harvest_moon"))).add(HARVEST_MOON, SUPER_HARVEST_MOON);
        tag(TagKey.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, EnhancedCelestials.createLocation("super_moon"))).add(SUPER_BLOOD_MOON, SUPER_BLUE_MOON, SUPER_HARVEST_MOON, SUPER_MOON);
        tag(TagKey.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, EnhancedCelestials.createLocation("moon"))).add(BLOOD_MOON, BLUE_MOON, HARVEST_MOON, DEFAULT);
    }
}
