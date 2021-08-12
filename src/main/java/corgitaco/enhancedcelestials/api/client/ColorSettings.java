package corgitaco.enhancedcelestials.api.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ColorSettings {

    public static final Codec<ColorSettings> CODEC = RecordCodecBuilder.create(seasonClientSettingsInstance -> {
        return seasonClientSettingsInstance.group(Codec.STRING.fieldOf("skyLightColor").forGetter((colorSettings) -> {
            return colorSettings.skyLightColor == Integer.MAX_VALUE ? "" : Integer.toHexString(colorSettings.skyLightColor);
        }), Codec.DOUBLE.fieldOf("skyLightBlendStrength").orElse(0.5).forGetter((colorSettings) -> {
            return colorSettings.skyLightBlendStrength;
        }), Codec.STRING.fieldOf("moonTextureColor").forGetter((colorSettings) -> {
            return colorSettings.skyLightColor == Integer.MAX_VALUE ? "" : Integer.toHexString(colorSettings.skyLightColor);
        }), Codec.DOUBLE.fieldOf("moonTextureBlendStrength").orElse(0.5).forGetter((colorSettings) -> {
            return colorSettings.skyLightBlendStrength;
        })).apply(seasonClientSettingsInstance, ColorSettings::new);
    });

    private final int skyLightColor;
    private final double skyLightBlendStrength;
    private final int moonTextureColor;
    private final double moonTextureBlendStrength;

    public ColorSettings(String skyLightHexColor, double skyLightBlendStrength, String moonTextureHexColor, double moonTextureBlendStrength) {
        this(tryParseColor(skyLightHexColor), skyLightBlendStrength, tryParseColor(moonTextureHexColor), moonTextureBlendStrength);
    }

    public ColorSettings(int skyLightColor, double skyLightBlendStrength, int moonTextureColor, double moonTextureBlendStrength) {
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

    public double getMoonTextureBlendStrength() {
        return moonTextureBlendStrength;
    }
}
