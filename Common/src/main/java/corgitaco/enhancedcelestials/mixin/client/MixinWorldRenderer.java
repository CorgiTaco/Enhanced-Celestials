package corgitaco.enhancedcelestials.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinWorldRenderer {
    @Shadow
    private ClientLevel level;


    @Shadow @Final private static ResourceLocation MOON_LOCATION;

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I"))
    private void changeMoonColor(PoseStack $$0, Matrix4f $$1, float partialTicks, Camera $$3, boolean $$4, Runnable $$5, CallbackInfo ci) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            Vector3f glColor = lunarContext.getCurrentEvent().getClientSettings().getColorSettings().getGLMoonColor();
            RenderSystem.setShaderColor(glColor.x(), glColor.y(), glColor.z(), 1.0F - this.level.getRainLevel(partialTicks));
        }
    }

    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V", ordinal = 1))
    private void bindCustomMoonTexture(int i, ResourceLocation resourceLocation) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            RenderSystem.setShaderTexture(i, lunarContext.getCurrentEvent().getClient().getMoonTextureLocation());
        } else {
            RenderSystem.setShaderTexture(i, MOON_LOCATION);
        }
    }

    @ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0F))
    private float getSuperMoonSize(float arg0) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
        if (lunarContext != null) {
            return lunarContext.getCurrentEvent().getClient().getMoonSize();
        }
        return arg0;
    }
}