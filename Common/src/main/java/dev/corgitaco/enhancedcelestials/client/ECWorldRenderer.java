package dev.corgitaco.enhancedcelestials.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.client.ColorSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class ECWorldRenderer {

    public static void changeMoonColor(float partialTicks) {
        ClientLevel level = Minecraft.getInstance().level;

        EnhancedCelestials.lunarForecastWorldData(level).ifPresent(data -> {
            ColorSettings lastColorSettings = data.lastLunarEventHolder().value().getClientSettings().colorSettings();
            ColorSettings currentColorSettings = data.currentLunarEventHolder().value().getClientSettings().colorSettings();

            Vector3f lastGLColor = lastColorSettings.getGLMoonColor();
            Vector3f currentGLColor = currentColorSettings.getGLMoonColor();

            float blend = data.getBlend();

            float r = Mth.clampedLerp(lastGLColor.x(), currentGLColor.x(), blend);
            float g = Mth.clampedLerp(lastGLColor.y(), currentGLColor.y(), blend);
            float b = Mth.clampedLerp(lastGLColor.z(), currentGLColor.z(), blend);
            RenderSystem.setShaderColor(r, g, b, 1.0F - level.getRainLevel(partialTicks));
        });
    }

    public static void eventLightMap(Vector3f skyVector, float partialTicks) {
        ClientLevel level = Minecraft.getInstance().level;
        EnhancedCelestials.lunarForecastWorldData(level).ifPresent(data -> {
            LunarEvent lastEvent = data.lastLunarEvent();
            LunarEvent currentEvent = data.currentLunarEvent();

            ColorSettings colorSettings = currentEvent.getClientSettings().colorSettings();
            ColorSettings lastColorSettings = lastEvent.getClientSettings().colorSettings();

            Vector3f glSkyLightColor = lastColorSettings.getGLSkyLightColor();
            Vector3f targetColor = new Vector3f(glSkyLightColor.x(), glSkyLightColor.y(), glSkyLightColor.z());

            float skyDarken = (level.getSkyDarken(1.0F) - 0.2F) / 0.8F;
            float eventBlend = data.getBlend() - skyDarken;
            targetColor.lerp(colorSettings.getGLSkyLightColor(), eventBlend);

            float skyBlend = (1 - skyDarken) - level.getRainLevel(partialTicks);
            skyVector.lerp(targetColor, skyBlend);
        });
    }
}
