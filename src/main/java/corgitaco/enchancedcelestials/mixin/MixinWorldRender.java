package corgitaco.enchancedcelestials.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsClientUtils;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRender {

    @Inject(method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"))
    private void changeMoonColor(MatrixStack matrixStackIn, float partialTicks, CallbackInfo ci) {
        Color color = EnhancedCelestials.currentLunarEvent.modifyMoonColor();
        Vector3f glColor = EnhancedCelestialsClientUtils.transformToVectorColor(color);
        RenderSystem.color4f(glColor.getX(), glColor.getY(), glColor.getZ(), color.getAlpha() / 255.0F);
    }
}
