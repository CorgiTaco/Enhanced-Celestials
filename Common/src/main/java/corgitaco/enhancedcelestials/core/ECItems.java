package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.item.MeteorStaffItem;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import corgitaco.enhancedcelestials.reg.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public final class ECItems {
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM, EnhancedCelestials.MOD_ID);

    public static final RegistryObject<Item> METEOR = ITEMS.register("meteor", () -> new BlockItem(ECBlocks.METEOR.get(), createProperties()));
    public static final RegistryObject<Item> METEOR_STAFF = ITEMS.register("meteor_staff", () -> new MeteorStaffItem( new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

    private ECItems() {
    }

    public static void classLoad() {
    }

    private static Item.Properties createProperties() {
        return new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS);
    }
}
