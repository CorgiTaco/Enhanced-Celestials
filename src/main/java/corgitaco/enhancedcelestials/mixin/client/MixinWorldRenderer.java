package corgitaco.enhancedcelestials.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Shadow
    private ClientWorld level;

    @Shadow @Final private static ResourceLocation MOON_LOCATION;

    @Inject(method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"))
    private void changeMoonColor(MatrixStack matrixStackIn, float partialTicks, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            Vector3f glColor = lunarContext.getCurrentEvent().getClientSettings().getColorSettings().getGLMoonColor();
            RenderSystem.color4f(glColor.x(), glColor.y(), glColor.z(), 1.0F - this.level.getRainLevel(partialTicks));
        }
    }

    @Redirect(method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bind(Lnet/minecraft/util/ResourceLocation;)V", ordinal = 1))
    private void bindCustomMoonTexture(TextureManager textureManager, ResourceLocation resource) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            textureManager.bind(lunarContext.getCurrentEvent().getClient().getMoonTextureLocation());
        } else {
            textureManager.bind(MOON_LOCATION);
        }
    }

    @ModifyConstant(method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", constant = @Constant(floatValue = 20.0F), require = 0)
    private float getSuperMoonSize(float arg0) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            return lunarContext.getCurrentEvent().getClient().getMoonSize();
        }
        return arg0;
    }
}