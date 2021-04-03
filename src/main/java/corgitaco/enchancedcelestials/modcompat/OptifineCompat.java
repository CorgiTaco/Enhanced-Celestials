package corgitaco.enchancedcelestials.modcompat;

import net.minecraft.util.LazyValue;

public class OptifineCompat {
    /**
     * @author Darkhax
     * Tracks whether or not Optifine is installed.
     * Allows for better compatibility with Optifine.
     */
    public static final LazyValue<Boolean> IS_OPTIFINE_PRESENT = new LazyValue<>(() -> {
        try {
            final Class<?> clazz = Class.forName("net.optifine.Config");
            return clazz != null;
        } catch (final Exception e) {
            return false;
        }
    });
}