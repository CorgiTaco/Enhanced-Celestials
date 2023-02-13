package corgitaco.enhancedcelestials.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ECWorldRenderer {

    public static void changeMoonColor(float partialTicks) {
        ClientLevel level = Minecraft.getInstance().level;
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
        if (enhancedCelestialsContext != null) {
            LunarForecast lunarForecast = enhancedCelestialsContext.getLunarForecast();

            ColorSettings lastColorSettings = lunarForecast.getMostRecentEvent().value().getClientSettings().colorSettings();
            ColorSettings currentColorSettings = lunarForecast.getCurrentEvent(level.getRainLevel(1) < 1).value().getClientSettings().colorSettings();

            Vector3f lastGLColor = lastColorSettings.getGLMoonColor();
            Vector3f currentGLColor = currentColorSettings.getGLMoonColor();

            float blend = lunarForecast.getBlend();

            float r = Mth.clampedLerp(lastGLColor.x(), currentGLColor.x(), blend);
            float g = Mth.clampedLerp(lastGLColor.y(), currentGLColor.y(), blend);
            float b = Mth.clampedLerp(lastGLColor.z(), currentGLColor.z(), blend);
            RenderSystem.setShaderColor(r, g, b, 1.0F - level.getRainLevel(partialTicks));
        }
    }

    public static void bindMoonTexture(int moonTextureId, ResourceLocation moonLocation) {
        ClientLevel level = Minecraft.getInstance().level;
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
        if (enhancedCelestialsContext != null) {
            LunarForecast lunarForecast = enhancedCelestialsContext.getLunarForecast();
            RenderSystem.setShaderTexture(moonTextureId, lunarForecast.getCurrentEvent(level.getRainLevel(1) < 1).value().getClientSettings().moonTextureLocation());
        } else {
            RenderSystem.setShaderTexture(moonTextureId, moonLocation);
        }
    }

    public static float getMoonSize(float arg0) {
        ClientLevel level = Minecraft.getInstance().level;
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
        if (enhancedCelestialsContext != null) {
            LunarForecast lunarForecast = enhancedCelestialsContext.getLunarForecast();
            return Mth.clampedLerp(lunarForecast.getMostRecentEvent().value().getClientSettings().moonSize(), lunarForecast.getCurrentEvent(level.getRainLevel(1) < 1).value().getClientSettings().moonSize(), lunarForecast.getBlend());
        }
        return arg0;
    }

    public static void eventLightMap(Vector3f skyVector, float partialTicks) {
        ClientLevel level = Minecraft.getInstance().level;
        EnhancedCelestialsWorldData enhancedCelestialsWorldData = (EnhancedCelestialsWorldData) level;
        if (enhancedCelestialsWorldData != null) {
            EnhancedCelestialsContext enhancedCelestialsContext = enhancedCelestialsWorldData.getLunarContext();
            if (enhancedCelestialsContext != null) {
                LunarForecast lunarForecast = enhancedCelestialsContext.getLunarForecast();
                LunarEvent lastEvent = lunarForecast.getMostRecentEvent().value();
                LunarEvent currentEvent = lunarForecast.getCurrentEvent(level.getRainLevel(1) < 1).value();

                ColorSettings colorSettings = currentEvent.getClientSettings().colorSettings();
                ColorSettings lastColorSettings = lastEvent.getClientSettings().colorSettings();

                Vector3f targetColor = lastColorSettings.getGLSkyLightColor().copy();

                float skyDarken = (level.getSkyDarken(1.0F) - 0.2F) / 0.8F;
                float eventBlend = lunarForecast.getBlend() - skyDarken;
                targetColor.lerp(colorSettings.getGLSkyLightColor(), eventBlend);

                float skyBlend = (1 - skyDarken) - level.getRainLevel(partialTicks);
                skyVector.lerp(targetColor, skyBlend);
            }
        }
    }
}
