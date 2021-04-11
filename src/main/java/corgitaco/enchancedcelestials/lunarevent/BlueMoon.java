package corgitaco.enchancedcelestials.lunarevent;

import corgitaco.enchancedcelestials.config.EnhancedCelestialsConfig;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsClientUtils;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.Color;

public class BlueMoon extends LunarEvent {
    public static final Color COLOR = new Color(63, 63, 192);

    public BlueMoon() {
        super(LunarEventSystem.BLUE_MOON_EVENT_ID, "enhancedcelestials.blue_moon", EnhancedCelestialsConfig.blueMoonChance.get());
    }

    @Override
    public boolean modifySkyLightMapColor(Vector3f originalMoonColor) {
        if (originalMoonColor != null) {
            originalMoonColor.lerp(EnhancedCelestialsClientUtils.transformToVectorColor(COLOR), 1.0F);
        }
        return true;
    }

    @Override
    public Color modifyMoonColor() {
        return COLOR;
    }

    @Override
    public void sendRisingNotification(PlayerEntity player) {
        if (displayNotifications()) {
            EnhancedCelestialsUtils.sendNotification(player, new TranslationTextComponent("enhancedcelestials.notification.rise", new TranslationTextComponent(getName())), TextFormatting.BLUE);
        }
    }

    @Override
    public void sendSettingNotification(PlayerEntity player) {
        if (displayNotifications()) {
            EnhancedCelestialsUtils.sendNotification(player, new TranslationTextComponent("enhancedcelestials.notification.set", new TranslationTextComponent(getName())), TextFormatting.BLUE);
        }
    }
}