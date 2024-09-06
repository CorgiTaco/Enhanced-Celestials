package dev.corgitaco.enhancedcelestials.fabric.platform;

import com.google.auto.service.AutoService;
import dev.corgitaco.enhancedcelestials.network.ECPacket;
import dev.corgitaco.enhancedcelestials.platform.services.IPlatformHelper;
import dev.corgitaco.enhancedcelestials.fabric.network.FabricNetworkHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

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
    public <P extends ECPacket> void sendToClient(ServerPlayer player, P packet) {
        FabricNetworkHandler.sendToPlayer(player, packet);
    }

    @Override
    public Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
