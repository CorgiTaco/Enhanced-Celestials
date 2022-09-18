package corgitaco.enhancedcelestials.world.level.levelgen.structure.crater;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class CraterStructure extends Structure {

    public static final Codec<CraterStructure> CODEC = RecordCodecBuilder.<CraterStructure>mapCodec(
            archStructureInstance ->
                    archStructureInstance.group(
                            settingsCodec(archStructureInstance)
                    ).apply(archStructureInstance, CraterStructure::new)
    ).codec();

    public CraterStructure(StructureSettings structureSettings) {
        super(structureSettings);
    }


    private static void generatePieces(StructurePiecesBuilder piecesBuilder, GenerationContext context) {
        WorldgenRandom random = context.random();

        int seed = random.nextInt();

        ChunkPos chunkPos = context.chunkPos();
        int blockX = chunkPos.getBlockX(random.nextInt(16));
        int blockZ = chunkPos.getBlockZ(random.nextInt(16));

        int baseHeight = context.chunkGenerator().getBaseHeight(blockX, blockZ, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());

        BlockPos origin = new BlockPos(blockX, baseHeight, blockZ);

        double baseRadiusX = random.nextInt(100, 150);
        double baseRadiusZ = random.nextInt(100, 150);
        double threshold = 1;

        int coneSizePackedX = SectionPos.blockToSectionCoord(baseRadiusX);
        int coneSizePackedZ = SectionPos.blockToSectionCoord(baseRadiusX);

        for (int x = -coneSizePackedX - 1; x <= coneSizePackedX + 1; x++) {
            for (int z = -coneSizePackedZ - 1; z <= coneSizePackedZ + 1; z++) {
                long chunk = ChunkPos.asLong(SectionPos.blockToSectionCoord(blockX) + x, SectionPos.blockToSectionCoord(blockZ) + z);
                piecesBuilder.addPiece(new CraterPiece(new PieceStructureInfo(origin, seed, baseRadiusX, baseRadiusZ, threshold), 0, getWritableArea(new ChunkPos(chunk), context.heightAccessor())));
            }
        }
    }


    public static BoundingBox getWritableArea(ChunkPos chunkPos, LevelHeightAccessor accessor) {
        int i = chunkPos.getMinBlockX();
        int j = chunkPos.getMinBlockZ();
        int k = accessor.getMinBuildHeight() + 1;
        int l = accessor.getMaxBuildHeight() - 1;
        return new BoundingBox(i, k, j, i + 15, l, j + 15);
    }

    private static boolean matchesBiome(BlockPos pos, ChunkGenerator generator, TagKey<Biome> biomeTagKey, RandomState randomState) {
        return generator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ()), randomState.sampler()).is(biomeTagKey);
    }


    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (piecesBuilder) -> {
            generatePieces(piecesBuilder, generationContext);
        });
    }

    @Override
    public StructureType<?> type() {
        return ECStructureTypes.CRATER.get();
    }


    public record PieceStructureInfo(BlockPos origin, int noiseSeed, double baseRadiusX, double baseRadiusZ,
                                     double threshold) {
        public static final Codec<PieceStructureInfo> CODEC = RecordCodecBuilder.create(
                builder ->
                        builder.group(
                                BlockPos.CODEC.fieldOf("origin").forGetter(PieceStructureInfo::origin),
                                Codec.INT.fieldOf("noiseSeed").forGetter(PieceStructureInfo::noiseSeed),
                                Codec.DOUBLE.fieldOf("baseRadiusX").forGetter(PieceStructureInfo::baseRadiusX),
                                Codec.DOUBLE.fieldOf("baseRadiusZ").forGetter(PieceStructureInfo::baseRadiusZ),
                                Codec.DOUBLE.fieldOf("threshold").forGetter(PieceStructureInfo::threshold)
                        ).apply(builder, PieceStructureInfo::new)
        );
    }
}
