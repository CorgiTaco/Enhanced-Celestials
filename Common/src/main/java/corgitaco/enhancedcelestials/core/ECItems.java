package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.item.MeteorStaffItem;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import corgitaco.enhancedcelestials.reg.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class ECItems {
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM, EnhancedCelestials.MOD_ID);

    public static final RegistryObject<Item> METEOR = ITEMS.register("meteor", () -> new BlockItem(ECBlocks.METEOR.get(), createProperties()));
    public static final RegistryObject<Item> SPACE_MOSS_CARPET = ITEMS.register("space_moss_carpet", () -> new BlockItem(ECBlocks.SPACE_MOSS_CARPET.get(), createProperties()));
    public static final RegistryObject<Item> SPACE_MOSS_BLOCK = ITEMS.register("space_moss_block", () -> new BlockItem(ECBlocks.SPACE_MOSS_BLOCK.get(), createProperties()));
    public static final RegistryObject<Item> SPACE_MOSS_GRASS = ITEMS.register("space_moss_grass", () -> new BlockItem(ECBlocks.SPACE_MOSS_GRASS.get(), createProperties()));

    public static final RegistryObject<Item> METEOR_STAFF = ITEMS.register("meteor_staff", () -> new MeteorStaffItem( new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

    public static final RegistryObject<Item> SPACE_MOSS_BUG_SPAWN_EGG = ITEMS.register("space_moss_bug_spawn_egg", () -> new SpawnEggItem(ECEntities.SPACE_MOSS_BUG.get(), 0, 0, createProperties()));

    private ECItems() {
    }

    public static void classLoad() {
    }

    private static Item.Properties createProperties() {
        return new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS);
    }
}
