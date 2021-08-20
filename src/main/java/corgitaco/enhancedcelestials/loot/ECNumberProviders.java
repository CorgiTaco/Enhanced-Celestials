package corgitaco.enhancedcelestials.loot;

import corgitaco.enhancedcelestials.Main;
import corgitaco.enhancedcelestials.mixin.access.RandomRangesAccessor;
import corgitaco.enhancedcelestials.util.loot.Probability;
import net.minecraft.util.ResourceLocation;

public class ECNumberProviders {
    public static final ResourceLocation PROBABILITY = new ResourceLocation(Main.MOD_ID, "probability");

    public static void registerNumberProviders(){
        RandomRangesAccessor.getGENERATOR_MAP().put(PROBABILITY, Probability.class);
    }
}