package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ConfiguredFeature.class)
public class MixinConfiguredFeature {

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void cancelPlacementNearCraters(WorldGenLevel level, ChunkGenerator generator, RandomSource source, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (EnhancedCelestials.NEW_CONTENT) {
            Structure structure = level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ECStructures.CRATER);
            ChunkAccess chunk = level.getChunk(pos);
            boolean hasCrater = chunk.getAllStarts().containsKey(structure) || chunk.getAllReferences().containsKey(structure);
            if (hasCrater) {
                cir.setReturnValue(false);
            }
        }
    }
}
