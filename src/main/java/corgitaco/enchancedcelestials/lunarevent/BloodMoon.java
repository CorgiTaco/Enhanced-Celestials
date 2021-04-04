package corgitaco.enchancedcelestials.lunarevent;

import corgitaco.enchancedcelestials.config.EnhancedCelestialsConfig;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

public class BloodMoon extends LunarEvent {
    public static final Color COLOR = new Color(166, 16, 30);

    private static double spawnCapMultiplier = EnhancedCelestialsConfig.spawnCapMultiplier.get();
    private static boolean redClouds = EnhancedCelestialsConfig.redClouds.get();
    private static boolean displayNotification = EnhancedCelestialsConfig.bloodMoonNotification.get();

    public BloodMoon() {
        super(LunarEventSystem.BLOOD_MOON_EVENT_ID, EnhancedCelestialsConfig.bloodMoonChance.get());
    }

    @Override
    public boolean modifySkyLightMapColor(Vector3f lightMapSkyColor) {
        if (lightMapSkyColor != null) {
            lightMapSkyColor.lerp(new Vector3f(2.0F, 0, 0), 1.0F);
        }
        return true;
    }

    @Override
    public void multiplySpawnCap(EntityClassification mobCategory, int spawningChunkCount, Object2IntOpenHashMap<EntityClassification> currentMobCategoryCounts, CallbackInfoReturnable<Boolean> cir) {
        if (mobCategory == EntityClassification.MONSTER) {
            int spawnCap = (int) (mobCategory.getMaxNumberOfCreature() * (spawningChunkCount * spawnCapMultiplier) / EnhancedCelestialsUtils.CHUNK_AREA);
            cir.setReturnValue(currentMobCategoryCounts.getInt(mobCategory) < spawnCap);
        }
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
    public boolean stopSleeping(PlayerEntity player) {
        player.sendStatusMessage(new TranslationTextComponent("enhancedcelestials.sleep.fail.blood_moon"), true);
        return true;
    }

    @Override
    public void sendRisingNotification(PlayerEntity player) {
        if (displayNotification) {
            TranslationTextComponent component = new TranslationTextComponent("enhancedcelestials.notification.blood_moon.rise");
            component.setStyle(component.getStyle().applyFormatting(TextFormatting.RED));
            player.sendMessage(component, Util.DUMMY_UUID);
        }
    }

    @Override
    public void sendSettingNotification(PlayerEntity player) {
        if (displayNotification) {
            TranslationTextComponent component = new TranslationTextComponent("enhancedcelestials.notification.blood_moon.set");
            component.setStyle(component.getStyle().applyFormatting(TextFormatting.RED));
            player.sendMessage(component, Util.DUMMY_UUID);
        }
    }
}