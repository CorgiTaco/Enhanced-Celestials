package corgitaco.enhancedcelestials.mixin;

import com.google.gson.GsonBuilder;
import corgitaco.enhancedcelestials.util.loot.Probability;
import net.minecraft.loot.LootSerializers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootSerializers.class)
public class MixinLootSerializers {

    @Inject(method = "func_237388_c_", at = @At("RETURN"))
    private static void registerProbability(CallbackInfoReturnable<GsonBuilder> cir) {
        cir.getReturnValue().registerTypeAdapter(Probability.class, new Probability.Serializer());
    }
}
