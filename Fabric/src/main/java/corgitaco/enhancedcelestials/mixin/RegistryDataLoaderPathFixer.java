package corgitaco.enhancedcelestials.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderPathFixer {
    @ModifyExpressionValue(method="loadRegistryContents", at= @At(value = "INVOKE", target = "Lnet/minecraft/resources/RegistryDataLoader;registryDirPath(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/String;"))
    private static String removeNamespaceForECDynamicRegistry(String original, @Local(argsOnly = true) ResourceKey key) {
        if (key.location().getNamespace().equals(EnhancedCelestials.MOD_ID)) return key.location().getPath();
        return original;
    }
}
