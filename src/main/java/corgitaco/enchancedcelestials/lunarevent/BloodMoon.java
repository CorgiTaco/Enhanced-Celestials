package corgitaco.enchancedcelestials.lunarevent;

import corgitaco.enchancedcelestials.config.EnhancedCelestialsConfig;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.Color;

public class BloodMoon extends LunarEvent {
    public static final Color COLOR = new Color(166, 16, 30);

    private static boolean redClouds = EnhancedCelestialsConfig.redClouds.get();

    public BloodMoon() {
        super(LunarEventSystem.BLOOD_MOON_EVENT_ID, "enhancedcelestials.blood_moon", EnhancedCelestialsConfig.bloodMoonChance.get());
    }

    @Override
    public boolean modifySkyLightMapColor(Vector3f originalMoonColor) {
        if (originalMoonColor != null) {
            originalMoonColor.lerp(new Vector3f(2.0F, 0, 0), 1.0F);
        }
        return true;
    }

    @Override
    public Color modifyMoonColor() {
        return new Color(166, 16, 30, 255);
    }

    @Override
    public Color modifySkyColor(Color originalSkyColor) {
        return COLOR;
    }

    @Override
    public Color modifyFogColor(Color originalSkyColor) {
        return COLOR;
    }

    @Override
    public Color modifyWaterColor(Color originalWaterColor) {
        return COLOR;
    }

    @Override
    public Color modifyWaterFogColor(Color originalWaterFogColor) {
        return new Color(206, 56, 70);
    }

    @Override
    public Color modifyCloudColor(Color originalCloudColor) {
        return redClouds ? COLOR : super.modifyCloudColor(originalCloudColor);
    }

    @Override
    public double getSpawnCapacityMultiplier() {
        return EnhancedCelestialsConfig.spawnCapacityMultiplier.get();
    }

    @Override
    public void sendRisingNotification(PlayerEntity player) {
        if (displayNotifications()) {
            EnhancedCelestialsUtils.sendNotification(player, new TranslationTextComponent("enhancedcelestials.notification.rise", new TranslationTextComponent(getName())), TextFormatting.RED);
        }
    }

    @Override
    public void sendSettingNotification(PlayerEntity player) {
        if (displayNotifications()) {
            EnhancedCelestialsUtils.sendNotification(player, new TranslationTextComponent("enhancedcelestials.notification.set", new TranslationTextComponent(getName())), TextFormatting.RED);
        }
    }
}