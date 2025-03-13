package dev.corgitaco.enhancedcelestials.datagen.providers;

import dev.corgitaco.enhancedcelestials.api.ECItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ECItemTagsProvider extends ItemTagsProvider {

    public ECItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    public ECItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Item>> parentProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, parentProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        getOrCreateRawBuilder(ECItemTags.HARVEST_MOON_CROPS)
                .addOptionalTag(new ResourceLocation("c", "crop"))
                .addOptionalTag(new ResourceLocation("forge", "crop"))
                .addOptionalTag(new ResourceLocation("minecraft", "crop"))
                .addOptionalTag(new ResourceLocation("c", "crops"))
                .addOptionalTag(new ResourceLocation("forge", "crops"))
                .addOptionalTag(new ResourceLocation("minecraft", "crops"));
    }
}
