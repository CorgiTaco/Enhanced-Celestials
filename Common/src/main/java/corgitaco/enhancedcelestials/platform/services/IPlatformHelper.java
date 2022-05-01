package corgitaco.enhancedcelestials.platform.services;

import corgitaco.enhancedcelestials.network.S2CPacket;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;
import java.util.List;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    <P extends S2CPacket> void sendToClient(ServerPlayer player, P packet);

    default <P extends S2CPacket> void sendToAllClients(List<ServerPlayer> players, P packet) {
        for (ServerPlayer player : players) {
            sendToClient(player, packet);
        }
    }

    Path configDir();
}
