package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.Main;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;

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
        }), CustomTranslationTextComponent.CODEC.optionalFieldOf("startNotification", CustomTranslationTextComponent.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.startNotification();
        }), CustomTranslationTextComponent.CODEC.optionalFieldOf("endNotification", CustomTranslationTextComponent.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.endNotification();
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

    public HarvestMoon(LunarEventClientSettings clientSettings, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases, CustomTranslationTextComponent startNotificationLangKey, CustomTranslationTextComponent endNotificationLangKey, boolean blockSleeping, Collection<ResourceLocation> cropTags, double cropDropMultiplier) {
        super(clientSettings, minNumberOfNightsBetween, chance, validMoonPhases, startNotificationLangKey, endNotificationLangKey, blockSleeping);
        this.cropTags = cropTags;
        this.cropDropMultiplier = cropDropMultiplier;

        for (ResourceLocation tagID : cropTags) {
            if (tagID.getPath().contains("item_tag_")) {
                tagID = new ResourceLocation(tagID.getNamespace(), tagID.getPath().replace("item_tag_", ""));
                if (ItemTags.getCollection().getRegisteredTags().contains(tagID)) {
                    enhancedCrops.add(ItemTags.makeWrapperTag(tagID.toString()));
                } else {
                    Main.LOGGER.error("\"" + tagID + "\" is not a valid item tag!");
                }
            } else {
                tagID = new ResourceLocation(tagID.getNamespace(), tagID.getPath());
                Optional<Item> optional = Registry.ITEM.getOptional(tagID);
                if (optional.isPresent()) {
                    enhancedCrops.add(optional.get());
                } else {
                    Main.LOGGER.error("\"" + tagID + "\" is not a valid item ID!");
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void onBlockItemDrop(ServerWorld world, ItemStack itemStack) {
        Item item = itemStack.getItem();
        for (Object enhancedCrop : this.enhancedCrops) {
            if (enhancedCrop instanceof ITag.INamedTag) {
                if (((ITag.INamedTag) enhancedCrop).contains(item)) {
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
