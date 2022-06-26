package corgitaco.enhancedcelestials.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarForecast;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinWorldRenderer {
    @Shadow
    private ClientLevel level;


    @Shadow
    @Final
    private static ResourceLocation MOON_LOCATION;

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I"))
    private void changeMoonColor(PoseStack $$0, Matrix4f $$1, float partialTicks, Camera $$3, boolean $$4, Runnable $$5, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            LunarForecast lunarForecast = lunarContext.getLunarForecast();
            Vector3f lastGLColor = lunarForecast.getMostRecentEvent().value().getClientSettings().colorSettings().getGLMoonColor();
            Vector3f currentGLColor = lunarForecast.getCurrentEvent().value().getClientSettings().colorSettings().getGLMoonColor();

            float blend = lunarForecast.getBlend();

            float r = Mth.clampedLerp(lastGLColor.x(), currentGLColor.x(), blend);
            float g = Mth.clampedLerp(lastGLColor.y(), currentGLColor.y(), blend);
            float b = Mth.clampedLerp(lastGLColor.z(), currentGLColor.z(), blend);
            RenderSystem.setShaderColor(r, g, b, 1.0F - this.level.getRainLevel(partialTicks));
        }
    }

    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V", ordinal = 1))
    private void bindCustomMoonTexture(int i, ResourceLocation resourceLocation) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            LunarForecast lunarForecast = lunarContext.getLunarForecast();
            RenderSystem.setShaderTexture(i, lunarForecast.getCurrentEvent().value().getClientSettings().moonTextureLocation());
        } else {
            RenderSystem.setShaderTexture(i, MOON_LOCATION);
        }
    }

    @ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0F))
    private float getSuperMoonSize(float arg0) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            LunarForecast lunarForecast = lunarContext.getLunarForecast();
            return Mth.clampedLerp(lunarForecast.getMostRecentEvent().value().getClientSettings().moonSize(), lunarForecast.getCurrentEvent().value().getClientSettings().moonSize(), lunarForecast.getBlend());
        }
        return arg0;
    }
}