package corgitaco.enhancedcelestials.world.level.levelgen.structure;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import corgitaco.enhancedcelestials.reg.RegistryObject;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.crater.CraterPiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Locale;

public class ECStructurePieceTypes {
    public static final RegistrationProvider<StructurePieceType> PROVIDER = RegistrationProvider.get(Registries.STRUCTURE_PIECE, EnhancedCelestials.MOD_ID);
    public static final RegistryObject<StructurePieceType> CRATER_PIECE = setFullContextPieceId(CraterPiece::new, "CraterPiece");

    private static RegistryObject<StructurePieceType> setFullContextPieceId(StructurePieceType type, String id) {
        return PROVIDER.register(id.toLowerCase(Locale.ROOT), () -> type);
    }

    public static void bootStrap() {
    }
}
