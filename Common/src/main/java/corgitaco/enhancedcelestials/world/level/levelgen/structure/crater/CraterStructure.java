package corgitaco.enhancedcelestials.world.level.levelgen.structure.crater;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BiomeTags;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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

        ChunkGenerator chunkGenerator = context.chunkGenerator();
        RandomState randomState = context.randomState();
        int baseHeight = chunkGenerator.getBaseHeight(blockX, blockZ, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), randomState);

        BlockPos origin = new BlockPos(blockX, baseHeight, blockZ);

        double baseRadiusX = 128;
        double baseRadiusZ = baseRadiusX; //random.nextInt(32, 128);
        double threshold = 1;

        int coneSizePackedX = SectionPos.blockToSectionCoord(baseRadiusX) + 1;
        int coneSizePackedZ = SectionPos.blockToSectionCoord(baseRadiusZ) + 1;


        List<CraterPiece> craterPieceList = new ArrayList<>();

        for (int x = -coneSizePackedX; x <= coneSizePackedX; x++) {
            for (int z = -coneSizePackedZ; z <= coneSizePackedZ; z++) {
                int chunkX = SectionPos.blockToSectionCoord(blockX) + x;
                int chunkZ = SectionPos.blockToSectionCoord(blockZ) + z;
                long chunk = ChunkPos.asLong(chunkX, chunkZ);
                if (!isCraterSafe(chunkX, chunkZ, chunkGenerator, biomeHolder -> !biomeHolder.is(BiomeTags.IS_OCEAN) && !biomeHolder.is(BiomeTags.IS_RIVER), randomState)) {
                    return;
                }

                craterPieceList.add(new CraterPiece(new PieceStructureInfo(origin, seed, baseRadiusX, baseRadiusZ, threshold), 0, getWritableArea(new ChunkPos(chunk), context.heightAccessor())));
            }
        }

        craterPieceList.forEach(piecesBuilder::addPiece);
    }


    public static BoundingBox getWritableArea(ChunkPos chunkPos, LevelHeightAccessor accessor) {
        int i = chunkPos.getMinBlockX();
        int j = chunkPos.getMinBlockZ();
        int k = accessor.getMinBuildHeight() + 1;
        int l = accessor.getMaxBuildHeight() - 1;
        return new BoundingBox(i, k, j, i + 15, l, j + 15);
    }


    public static boolean isCraterSafe(int sectionX, int sectionZ, ChunkGenerator generator, Predicate<Holder<Biome>> isBiome, RandomState randomState) {

        for (int xOffset = 0; xOffset < 4; xOffset++) {
            for (int zOffset = 0; zOffset < 4; zOffset++) {
                int quartX = QuartPos.fromBlock(SectionPos.sectionToBlockCoord(sectionX)) + xOffset;
                int quartY = QuartPos.fromBlock(generator.getSeaLevel());
                int quartZ = QuartPos.fromBlock(SectionPos.sectionToBlockCoord(sectionZ)) + zOffset;
                if(!isBiome.test(generator.getBiomeSource().getNoiseBiome(quartX, quartY, quartZ, randomState.sampler())))  {
                    return false;
                };
            }
        }

        return true;
    }

    private static boolean matchesBiome(BlockPos pos, ChunkGenerator generator, Predicate<Holder<Biome>> isBiome, RandomState randomState) {
        return isBiome.test(generator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ()), randomState.sampler()));
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


    static {
        String s = "";
    }
}
