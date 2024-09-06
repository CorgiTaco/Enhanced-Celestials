package dev.corgitaco.enhancedcelestials.neoforge.datagen.providers;

import dev.corgitaco.enhancedcelestials.api.ECItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ECItemTagsProvider extends ItemTagsProvider {
    public ECItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> p_256467_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, p_256467_, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ECItemTags.HARVEST_MOON_CROPS)
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "crop"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("forge", "crop"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("minecraft", "crop"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "crops"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("forge", "crops"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("minecraft", "crops"));
    }
}
