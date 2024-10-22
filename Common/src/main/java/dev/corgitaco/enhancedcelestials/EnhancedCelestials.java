package dev.corgitaco.enhancedcelestials;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class EnhancedCelestials {
    public static final String MOD_ID = "enhancedcelestials";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean NEW_CONTENT = false;

    public EnhancedCelestials() {
    }

    public static void commonSetup() {
    }


    public static ResourceLocation createLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
