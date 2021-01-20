package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ISkyRenderHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.*;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRender {

    @Inject(method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void changeMoonColor(MatrixStack matrixStackIn, float partialTicks, CallbackInfo ci, ISkyRenderHandler renderHandler, Vector3d vector3d, float f, float f1, float f2, BufferBuilder bufferbuilder, float afloat[], float f11, Matrix4f matrix4f1, float f12) {
        Color color = EnhancedCelestials.currentLunarEvent.modifyMoonColor();
        Vector3f glColor = EnhancedCelestialsUtils.transformVectorColor(color);
        RenderSystem.color4f(glColor.getX(), glColor.getY(), glColor.getZ(), color.getAlpha() / 255.0F);
    }
}
