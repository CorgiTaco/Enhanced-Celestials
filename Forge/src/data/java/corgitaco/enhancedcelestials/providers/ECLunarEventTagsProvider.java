package corgitaco.enhancedcelestials.providers;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents.*;

public class ECLunarEventTagsProvider extends TagsProvider<LunarEvent> {
    private final boolean useMinecraftNameSpace;

    public ECLunarEventTagsProvider(PackOutput pOutput, boolean useMinecraftNameSpace, ResourceKey<? extends Registry<LunarEvent>> pRegistryKey, CompletableFuture<HolderLookup.Provider> pLookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pRegistryKey, pLookupProvider, modId, existingFileHelper);
        this.useMinecraftNameSpace = useMinecraftNameSpace;
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(createTagKey("blood_moon")).add(BLOOD_MOON, SUPER_BLOOD_MOON);
        tag(createTagKey("blue_moon")).add(BLUE_MOON, SUPER_BLUE_MOON);
        tag(createTagKey("harvest_moon")).add(HARVEST_MOON, SUPER_HARVEST_MOON);
        tag(createTagKey("super_moon")).add(SUPER_BLOOD_MOON, SUPER_BLUE_MOON, SUPER_HARVEST_MOON, SUPER_MOON);
        tag(createTagKey("moon")).add(BLOOD_MOON, BLUE_MOON, HARVEST_MOON, DEFAULT);
    }

    public TagKey<LunarEvent> createTagKey(String path) {
        ResourceKey<Registry<LunarEvent>> lunarEventKey = useMinecraftNameSpace ? ResourceKey.createRegistryKey(new ResourceLocation("lunar/event")) : EnhancedCelestialsRegistry.LUNAR_EVENT_KEY;

        return TagKey.create(lunarEventKey, EnhancedCelestials.createLocation(path));
    }

    @Override
    public String getName() {
        return this.useMinecraftNameSpace ? "Fabric " + super.getName() : "Forge " + super.getName();
    }
}
