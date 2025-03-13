package dev.corgitaco.enhancedcelestials.world.level.levelgen.structure;

import dev.corgitaco.enhancedcelestials.platform.services.RegistrationService;
import dev.corgitaco.enhancedcelestials.world.level.levelgen.structure.crater.CraterPiece;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Locale;
import java.util.function.Supplier;

public class ECStructurePieceTypes {
    public static final Supplier<StructurePieceType> CRATER_PIECE = setFullContextPieceId(() -> CraterPiece::new, "CraterPiece");

    private static Supplier<StructurePieceType> setFullContextPieceId(Supplier<StructurePieceType> type, String id) {
        return RegistrationService.INSTANCE.register(BuiltInRegistries.STRUCTURE_PIECE, id.toLowerCase(Locale.ROOT), type);
    }

    public static void bootStrap() {
    }
}
