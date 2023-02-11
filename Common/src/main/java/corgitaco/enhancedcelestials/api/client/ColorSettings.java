package corgitaco.enhancedcelestials.api.client;

import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.util.ColorUtil;

public class ColorSettings {

    public static final Codec<ColorSettings> CODEC = RecordCodecBuilder.create(colorSettingsInstance -> {
        return colorSettingsInstance.group(Codec.STRING.fieldOf("sky_light_color").forGetter((colorSettings) -> {
            return colorSettings.skyLightColor == Integer.MAX_VALUE ? "ffffff" : Integer.toHexString(colorSettings.skyLightColor);
        }), Codec.STRING.fieldOf("moon_texture_color").forGetter((colorSettings) -> {
            return colorSettings.moonTextureColor == Integer.MAX_VALUE ? "ffffff" : Integer.toHexString(colorSettings.moonTextureColor);
        })).apply(colorSettingsInstance, ColorSettings::new);
    });

    private final int skyLightColor;
    private final int moonTextureColor;

    public ColorSettings(String skyLightHexColor, String moonTextureHexColor) {
        this(tryParseColor(skyLightHexColor), tryParseColor(moonTextureHexColor));
    }

    public ColorSettings(int skyLightColor, int moonTextureColor) {
        this.skyLightColor = skyLightColor;
        this.moonTextureColor = moonTextureColor;
    }

    public static int tryParseColor(String input) {
        int result = Integer.MAX_VALUE;

        if (input.isEmpty()) {
            return result;
        }

        try {
            result = (int) Long.parseLong(input.replace("#", "").replace("0x", ""), 16);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int getMoonTextureColor() {
        return moonTextureColor;
    }

    public Vector3f getGLMoonColor() {
        return ColorUtil.glColor(ColorUtil.unpack(this.moonTextureColor));
    }

    public int getSkyLightColor() {
        return skyLightColor;
    }

    public Vector3f getGLSkyLightColor() {
        return ColorUtil.glColor(ColorUtil.unpack(this.skyLightColor));
    }
}
