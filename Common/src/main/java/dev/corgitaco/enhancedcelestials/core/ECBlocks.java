package dev.corgitaco.enhancedcelestials.core;

import dev.corgitaco.enhancedcelestials.block.SpaceMossBlock;
import dev.corgitaco.enhancedcelestials.block.SpaceMossCarpetBlock;
import dev.corgitaco.enhancedcelestials.block.SpaceMossGrassBlock;
import dev.corgitaco.enhancedcelestials.platform.services.RegistrationService;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public final class ECBlocks {

    public static final Supplier<Block> METEOR = register("meteor", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final Supplier<Block> SPACE_MOSS_CARPET = register("space_moss_carpet", () -> new SpaceMossCarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_CARPET)));
    public static final Supplier<Block> SPACE_MOSS_BLOCK = register("space_moss_block", () -> new SpaceMossBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).randomTicks()));
    public static final Supplier<Block> SPACE_MOSS_GRASS = register("space_moss_grass", () -> new SpaceMossGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));

    private ECBlocks() {
    }

    public static <B extends Block> Supplier<B> register(String id, Supplier<B> block) {
        return RegistrationService.INSTANCE.register((net.minecraft.core.Registry<B>) BuiltInRegistries.BLOCK, id, block);
    }

    public static void classLoad() {
    }
}
