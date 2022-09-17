package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import corgitaco.enhancedcelestials.reg.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class EnhancedCelestialsBlocks {
    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registry.BLOCK, EnhancedCelestials.MOD_ID);

    public static final RegistryObject<Block> METEOR = BLOCKS.register("meteor", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    private EnhancedCelestialsBlocks() {
    }

    public static void classLoad() {
    }
}
