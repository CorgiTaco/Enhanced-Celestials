package dev.corgitaco.enhancedcelestials.core;

import dev.corgitaco.enhancedcelestials.item.MeteorStaffItem;
import dev.corgitaco.enhancedcelestials.platform.services.RegistrationService;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public final class ECItems {

    public static final Supplier<Item> METEOR = register("meteor", () -> new BlockItem(ECBlocks.METEOR.get(), createProperties()));
    public static final Supplier<Item> SPACE_MOSS_CARPET = register("space_moss_carpet", () -> new BlockItem(ECBlocks.SPACE_MOSS_CARPET.get(), createProperties()));
    public static final Supplier<Item> SPACE_MOSS_BLOCK = register("space_moss_block", () -> new BlockItem(ECBlocks.SPACE_MOSS_BLOCK.get(), createProperties()));
    public static final Supplier<Item> SPACE_MOSS_GRASS = register("space_moss_grass", () -> new BlockItem(ECBlocks.SPACE_MOSS_GRASS.get(), createProperties()));

    public static final Supplier<Item> METEOR_STAFF = register("meteor_staff", () -> new MeteorStaffItem(new Item.Properties()));

    public static final Supplier<Item> SPACE_MOSS_BUG_SPAWN_EGG = register("space_moss_bug_spawn_egg", () -> new SpawnEggItem(ECEntities.SPACE_MOSS_BUG.get(), 0x626361, 0x564945, createProperties()));

    private ECItems() {
    }

    public static void classLoad() {
    }

    public static <B extends Item> Supplier<B> register(String id, Supplier<B> item) {
        return RegistrationService.INSTANCE.register((net.minecraft.core.Registry<B>) BuiltInRegistries.ITEM, id, item);
    }

    private static Item.Properties createProperties() {
        return new Item.Properties();
    }
}
