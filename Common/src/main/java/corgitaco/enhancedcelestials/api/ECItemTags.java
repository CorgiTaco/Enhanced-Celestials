package corgitaco.enhancedcelestials.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ECItemTags {

    public static final TagKey<Item> HARVEST_MOON_CROPS = create("harvest_moon_crops");


    public static TagKey<Item> create(String id) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(EnhancedCelestialsRegistry.MOD_ID, id));
    }

    public static void loadClass() {
    }
}
