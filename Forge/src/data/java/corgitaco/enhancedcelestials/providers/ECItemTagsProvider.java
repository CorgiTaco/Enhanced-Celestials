package corgitaco.enhancedcelestials.providers;

import corgitaco.enhancedcelestials.api.ECItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ECItemTagsProvider extends ItemTagsProvider {
    public ECItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, TagsProvider<Block> p_256467_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, p_256467_, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ECItemTags.HARVEST_MOON_CROPS)
                .addOptionalTag(new ResourceLocation("c", "crop"))
                .addOptionalTag(new ResourceLocation("forge", "crop"))
                .addOptionalTag(new ResourceLocation("minecraft", "crop"))
                .addOptionalTag(new ResourceLocation("c", "crops"))
                .addOptionalTag(new ResourceLocation("forge", "crops"))
                .addOptionalTag(new ResourceLocation("minecraft", "crops"));
    }
}
