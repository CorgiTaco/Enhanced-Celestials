package corgitaco.enhancedcelestials.util;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ColorUtil {
    private static final long BIT_MASK = 0xFF; // 255

    private static int clamp(int target, int fallback) {
        return target == Integer.MAX_VALUE ? fallback : target;
    }

    // Mix two colour arrays together. Similar to additive color mixing.
    public static int mix(int[] start, int end[], double blend) {
        return pack(
                lerp(start[0], end[0], blend), // Alpha.
                lerp(start[1], end[1], blend), // Red.
                lerp(start[2], end[2], blend), // Green.
                lerp(start[3], end[3], blend)  // Blue.
        );
    }

    public static int[] transformFloatColor(Vec3 floatColor) {
        return new int[]{255, (int) (floatColor.x() * 255), (int) (floatColor.y() * 255), (int) (floatColor.z() * 255)};
    }

    public static Vector3f glColor(int[] packedColor) {
        float r = (float) packedColor[1] / 255.0F;
        float g = (float) packedColor[2] / 255.0F;
        float b = (float) packedColor[3] / 255.0F;
        return new Vector3f(r, g, b);
    }

    // Interpolate between color channels.
    private static int lerp(int start, int end, double blend) {
        return (int) (start + ((end - start) * blend));
    }

    // Packs ARGB into a decimal.
    public static int pack(int a, int r, int g, int b) {
        return (int) (
                ((a & BIT_MASK) << 24) |
                        ((r & BIT_MASK) << 16) |
                        ((g & BIT_MASK)) << 8 |
                        (b & BIT_MASK)
        );
    }

    // Packs ARGB decimal, with an alpha of 255.
    public static int pack(int r, int g, int b) {
        return pack(255, r, g, b);
    }

    // Unpacks ARGB channels from a decimal.
    public static int[] unpack(int decimal) {
        return new int[]{
                (int) ((decimal >> 24) & BIT_MASK), // Alpha.
                (int) ((decimal >> 16) & BIT_MASK), // Red.
                (int) ((decimal >> 8) & BIT_MASK),  // Green.
                (int) (decimal & BIT_MASK)          // Blue.
        };
    }
}
