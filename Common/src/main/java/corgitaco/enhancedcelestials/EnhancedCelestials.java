package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.platform.Services;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class EnhancedCelestials {
    public static final String MOD_ID = "enhancedcelestials";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Path CONFIG_PATH = Services.PLATFORM.configDir().resolve(MOD_ID);

    public EnhancedCelestials() {
    }

    public static void commonSetup() {
    }


    public static ResourceLocation createLocation(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
