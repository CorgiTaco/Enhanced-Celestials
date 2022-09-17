package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class EnhancedCelestialsBlockTags {
    public static final TagKey<Block> SPACE_MOSS_GROWS_ON = create("space_moss_grows_on");

    private EnhancedCelestialsBlockTags() {
    }

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registry.BLOCK_REGISTRY, EnhancedCelestials.createLocation(name));
    }
}
