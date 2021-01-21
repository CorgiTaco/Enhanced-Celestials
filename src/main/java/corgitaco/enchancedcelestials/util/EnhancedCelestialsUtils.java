package corgitaco.enchancedcelestials.util;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

public class EnhancedCelestialsUtils {

    public static final int CHUNK_AREA = (int)Math.pow(17.0D, 2.0D);

    public static void modifySpawnCap(EntityClassification mobCategory, int spawningChunkCount, Object2IntOpenHashMap<EntityClassification> currentMobCategoryCounts, CallbackInfoReturnable<Boolean> cir) {
        EnhancedCelestials.currentLunarEvent.multiplySpawnCap(mobCategory, spawningChunkCount, currentMobCategoryCounts, cir);
    }

    public static boolean isOverworld(RegistryKey<World> worldKey) {
        return worldKey == World.OVERWORLD;
    }

    public static Color transformFloatColor(Vector3d floatColor) {
        return new Color((int) (floatColor.getX() * 255), (int) (floatColor.getY() * 255), (int) (floatColor.getZ() * 255));
    }

    public static final Tags.IOptionalNamedTag<Item> FRUITS = tag("fruits");
    public static final Tags.IOptionalNamedTag<Item> VEGETABLES = tag("vegetable");

    private static Tags.IOptionalNamedTag<Item> tag(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }

    public static boolean filterRegistryID(ResourceLocation id, Registry<?> registry, String registryTypeName) {
        if (registry.keySet().contains(id))
            return true;
        else {
            EnhancedCelestials.LOGGER.error("\"" + id.toString() + "\" was not a registryID in the " + registryTypeName + "! Skipping entry...");
            return false;
        }
    }
}
