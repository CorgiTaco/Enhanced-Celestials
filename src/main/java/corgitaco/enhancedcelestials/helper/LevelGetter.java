package corgitaco.enhancedcelestials.helper;

import net.minecraft.world.World;

public interface LevelGetter {
    void setLevel(World world);

    World getLevel();
}
