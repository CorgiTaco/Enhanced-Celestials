package corgitaco.enhancedcelestials.api.client;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface SkyRenderable {

    void renderSky(MatrixStack matrixStackIn, float partialTicks);
}
