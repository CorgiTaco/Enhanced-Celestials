package corgitaco.enchancedcelestials.util;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

public class EnhancedCelestialsUtils {

    public static final int CHUNK_AREA = (int)Math.pow(17.0D, 2.0D);

    public static boolean bloodMoonSpawnCap(EntityClassification mobCategory, int spawningChunkCount,  Object2IntOpenHashMap<EntityClassification> currentMobCategoryCounts, boolean originalReturn) {
        if (mobCategory == EntityClassification.MONSTER) {
            int spawnCap = mobCategory.getMaxNumberOfCreature() * (spawningChunkCount * 25) / CHUNK_AREA;
            return currentMobCategoryCounts.getInt(mobCategory) < spawnCap;
        }
        return originalReturn;
    }


    public static void modifySpawnCap(EntityClassification mobCategory, int spawningChunkCount, Object2IntOpenHashMap<EntityClassification> currentMobCategoryCounts, CallbackInfoReturnable<Boolean> cir) {
        EnhancedCelestials.currentLunarEvent.multiplySpawnCap(mobCategory, spawningChunkCount, currentMobCategoryCounts, cir);
    }

    public static boolean isOverworld(RegistryKey<World> worldKey) {
        return worldKey == World.OVERWORLD;
    }

    public static Color transformFloatColor(Vector3d floatColor) {
        return new Color((int) (floatColor.getX() * 255), (int) (floatColor.getY() * 255), (int) (floatColor.getZ() * 255));
    }

    public static Vector3f transformVectorColor(Color color) {
        int rgbColor = color.getRGB();
        float r = (float) (rgbColor >> 16 & 255) / 255.0F;
        float g = (float) (rgbColor >> 8 & 255) / 255.0F;
        float b = (float) (rgbColor & 255) / 255.0F;
        return new Vector3f(r, g, b);
    }
}
