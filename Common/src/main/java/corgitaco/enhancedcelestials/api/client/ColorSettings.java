package corgitaco.enhancedcelestials.api.client;

import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.util.ColorUtil;

public class ColorSettings {

    public static final Codec<ColorSettings> CODEC = RecordCodecBuilder.create(seasonClientSettingsInstance -> {
        return seasonClientSettingsInstance.group(Codec.STRING.fieldOf("sky_light_color").forGetter((colorSettings) -> {
            return colorSettings.skyLightColor == Integer.MAX_VALUE ? "" : Integer.toHexString(colorSettings.skyLightColor);
        }), Codec.FLOAT.fieldOf("sky_light_blend_strength").orElse(0.5F).forGetter((colorSettings) -> {
            return colorSettings.skyLightBlendStrength;
        }), Codec.STRING.fieldOf("moon_texture_color").forGetter((colorSettings) -> {
            return colorSettings.skyLightColor == Integer.MAX_VALUE ? "" : Integer.toHexString(colorSettings.skyLightColor);
        }), Codec.FLOAT.fieldOf("moon_texture_blend_strength").orElse(0.5F).forGetter((colorSettings) -> {
            return colorSettings.skyLightBlendStrength;
        })).apply(seasonClientSettingsInstance, ColorSettings::new);
    });

    private final int skyLightColor;
    private final float skyLightBlendStrength;
    private final int moonTextureColor;
    private final float moonTextureBlendStrength;

    public ColorSettings(String skyLightHexColor, float skyLightBlendStrength, String moonTextureHexColor, float moonTextureBlendStrength) {
        this(tryParseColor(skyLightHexColor), skyLightBlendStrength, tryParseColor(moonTextureHexColor), moonTextureBlendStrength);
    }

    public ColorSettings(int skyLightColor, float skyLightBlendStrength, int moonTextureColor, float moonTextureBlendStrength) {
        this.skyLightColor = skyLightColor;
        this.skyLightBlendStrength = skyLightBlendStrength;
        this.moonTextureColor = moonTextureColor;
        this.moonTextureBlendStrength = moonTextureBlendStrength;
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

    public float getMoonTextureBlendStrength() {
        return moonTextureBlendStrength;
    }

    public int getSkyLightColor() {
        return skyLightColor;
    }

    public Vector3f getGLSkyLightColor() {
        return ColorUtil.glColor(ColorUtil.unpack(this.skyLightColor));
    }

    public double getSkyLightBlendStrength() {
        return skyLightBlendStrength;
    }
}
