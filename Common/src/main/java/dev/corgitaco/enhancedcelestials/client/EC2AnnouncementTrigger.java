package dev.corgitaco.enhancedcelestials.client;

import dev.corgitaco.enhancedcelestials.client.gui.EC2AnnouncementScreen;
import net.minecraft.client.Minecraft;

public class EC2AnnouncementTrigger {

    private static boolean pending = false;

    public static void onLogin() {
        pending = true;
    }

    public static void onDisconnect() {
        pending = false;
    }

    public static void onClientTick() {
        if (!pending) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.screen == null) {
            pending = false;
            minecraft.setScreen(new EC2AnnouncementScreen());
        }
    }
}
