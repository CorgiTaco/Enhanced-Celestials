package corgitaco.enhancedcelestials.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Shadow
    private ClientWorld world;

    @Inject(method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"))
    private void changeMoonColor(MatrixStack matrixStackIn, float partialTicks, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.world).getLunarContext();
        if (lunarContext != null) {
            Vector3f glColor = lunarContext.getCurrentEvent().getClientSettings().getColorSettings().getGLMoonColor();
            RenderSystem.color4f(glColor.getX(), glColor.getY(), glColor.getZ(), 1.0F - this.world.getRainStrength(partialTicks));
        }
    }

    @ModifyConstant(method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", constant = @Constant(floatValue = 20.0F))
    private float getSuperMoonSize(float arg0) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.world).getLunarContext();
        if (lunarContext != null) {
            return lunarContext.getCurrentEvent().getClient().getMoonSize();
        }
        return arg0;
    }
}