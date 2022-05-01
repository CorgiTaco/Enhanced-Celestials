package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.LunarTextComponents;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class HarvestMoon extends LunarEvent {

    public static final Codec<HarvestMoon> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(LunarEventClientSettings.CODEC.fieldOf("clientSettings").forGetter((clientSettings) -> {
            return clientSettings.getClientSettings();
        }), Codec.INT.fieldOf("minNumberOfNightsBetween").forGetter((clientSettings) -> {
            return clientSettings.getMinNumberOfNightsBetween();
        }), Codec.DOUBLE.fieldOf("chance").forGetter((clientSettings) -> {
            return clientSettings.getChance();
        }), Codec.list(Codec.INT).fieldOf("validMoonPhases").forGetter((clientSettings) -> {
            return new ArrayList<>(clientSettings.getValidMoonPhases());
        }), LunarTextComponents.CODEC.fieldOf("textComponents").forGetter((blueMoon) -> {
            return blueMoon.getTextComponents();
        }), Codec.BOOL.fieldOf("blockSleeping").forGetter((clientSettings) -> {
            return clientSettings.blockSleeping();
        }), Codec.list(ResourceLocation.CODEC).fieldOf("enhancedCrops").forGetter((clientSettings) -> {
            return new ArrayList<>(clientSettings.cropTags);
        }), Codec.DOUBLE.fieldOf("cropDropMultiplier").orElse(2.5).forGetter((clientSettings) -> {
            return clientSettings.cropDropMultiplier;
        })).apply(builder, HarvestMoon::new);
    });

    private final List<Object> enhancedCrops = new ArrayList<>();
    private final Collection<ResourceLocation> cropTags;
    private final double cropDropMultiplier;

    public HarvestMoon(LunarEventClientSettings clientSettings, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases, LunarTextComponents lunarTextComponents, boolean blockSleeping, Collection<ResourceLocation> cropTags, double cropDropMultiplier) {
        this(clientSettings, minNumberOfNightsBetween, chance, validMoonPhases, lunarTextComponents, blockSleeping, cropTags, cropDropMultiplier, true);
    }

    public HarvestMoon(LunarEventClientSettings clientSettings, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases, LunarTextComponents lunarTextComponents, boolean blockSleeping, Collection<ResourceLocation> cropTags, double cropDropMultiplier, boolean serializeCrops) {
        super(clientSettings, minNumberOfNightsBetween, chance, validMoonPhases, lunarTextComponents, blockSleeping);
        this.cropTags = cropTags;
        this.cropDropMultiplier = cropDropMultiplier;

        if (serializeCrops) {
            for (ResourceLocation tagID : cropTags) {
                if (tagID.getPath().contains("item_tag_")) {
                    tagID = new ResourceLocation(tagID.getNamespace(), tagID.getPath().replace("item_tag_", ""));
                   enhancedCrops.add(TagKey.create(Registry.ITEM_REGISTRY, tagID));

                } else {
                    tagID = new ResourceLocation(tagID.getNamespace(), tagID.getPath());
                    Optional<Item> optional = Registry.ITEM.getOptional(tagID);
                    if (optional.isPresent()) {
                        enhancedCrops.add(optional.get());
                    } else {
                        EnhancedCelestials.LOGGER.error("\"" + tagID + "\" is not a valid item ID!");
                    }
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void onBlockItemDrop(ServerLevel world, ItemStack itemStack) {
        Item item = itemStack.getItem();
        for (Object enhancedCrop : this.enhancedCrops) {
            if (enhancedCrop instanceof TagKey tagKey) {
                if (Registry.ITEM.getOrCreateTag(tagKey).contains(itemStack.getItem().builtInRegistryHolder())) {
                    itemStack.setCount((int) (itemStack.getCount() * this.cropDropMultiplier));
                    break;
                }
            } else if (enhancedCrop instanceof Item) {
                if (enhancedCrop == item) {
                    itemStack.setCount((int) (itemStack.getCount() * this.cropDropMultiplier));
                    break;
                }
            }
        }
        super.onBlockItemDrop(world, itemStack);
    }

    @Override
    public Codec<? extends LunarEvent> codec() {
        return CODEC;
    }
}
