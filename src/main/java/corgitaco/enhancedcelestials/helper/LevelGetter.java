package corgitaco.enhancedcelestials.helper;

import net.minecraft.world.level.Level;

public interface LevelGetter {
    void setLevel(Level world);

    Level getLevel();
}
