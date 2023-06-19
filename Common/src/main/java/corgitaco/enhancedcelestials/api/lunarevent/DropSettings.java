package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public record DropSettings(Map<TagKey<Item>, Double> dropEnhancer) {
    public static final DropSettings EMPTY = new DropSettings(new HashMap<>());

    public static Codec<DropSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.unboundedMap(TagKey.hashedCodec(Registries.ITEM), Codec.DOUBLE).fieldOf("drop_enhancer").forGetter(dropSettings -> dropSettings.dropEnhancer)
            ).apply(builder, DropSettings::new)
    );

}
