package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.block.SpaceMossBlock;
import corgitaco.enhancedcelestials.block.SpaceMossCarpetBlock;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import corgitaco.enhancedcelestials.reg.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ECBlocks {
    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registry.BLOCK, EnhancedCelestials.MOD_ID);

    public static final RegistryObject<Block> METEOR = BLOCKS.register("meteor", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> SPACE_MOSS_CARPET = BLOCKS.register("space_moss_carpet", () -> new SpaceMossCarpetBlock(BlockBehaviour.Properties.copy(Blocks.MOSS_CARPET)));
    public static final RegistryObject<Block> SPACE_MOSS_BLOCK = BLOCKS.register("space_moss_block", () -> new SpaceMossBlock(SPACE_MOSS_CARPET.get(), BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).randomTicks()));

    private ECBlocks() {
    }

    public static void classLoad() {
    }
}
