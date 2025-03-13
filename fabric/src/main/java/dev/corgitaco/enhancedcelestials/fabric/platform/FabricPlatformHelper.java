package dev.corgitaco.enhancedcelestials.fabric.platform;

import com.google.auto.service.AutoService;
import dev.corgitaco.enhancedcelestials.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

@AutoService(IPlatformHelper.class)
public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }


    @Override
    public Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
