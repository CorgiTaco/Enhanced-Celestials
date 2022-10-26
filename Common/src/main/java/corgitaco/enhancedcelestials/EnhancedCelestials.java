package corgitaco.enhancedcelestials;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnhancedCelestials {
    public static final String MOD_ID = "enhancedcelestials";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final boolean NEW_CONTENT = false;

    public EnhancedCelestials() {
    }

    public static void commonSetup() {
    }


    public static ResourceLocation createLocation(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
