package corgitaco.enhancedcelestials.api.client;

import com.mojang.blaze3d.vertex.PoseStack;

public interface SkyRenderable {

    void renderSky(PoseStack matrixStackIn, float partialTicks);
}
