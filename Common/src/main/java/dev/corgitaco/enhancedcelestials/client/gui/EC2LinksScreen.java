package dev.corgitaco.enhancedcelestials.client.gui;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class EC2LinksScreen extends Screen {

    private static final int BUTTON_WIDTH = 220;
    private static final int BUTTON_HEIGHT = 20;

    private final Screen previousScreen;
    private final Platform platform;

    public EC2LinksScreen(Screen previousScreen, Platform platform) {
        super(Component.translatable("enhancedcelestials.ec2_links.title"));
        this.previousScreen = previousScreen;
        this.platform = platform;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = this.height / 2 - 40;

        this.addRenderableWidget(this.linkButton(Component.translatable("enhancedcelestials.ec2_links.core"), Component.translatable("enhancedcelestials.ec2_links.core.tooltip"), this.platform.coreUrl, centerX - BUTTON_WIDTH / 2, y));
        this.addRenderableWidget(this.linkButton(Component.translatable("enhancedcelestials.ec2_links.lunar_events"), Component.translatable("enhancedcelestials.ec2_links.lunar_events.tooltip"), this.platform.lunarEventsUrl, centerX - BUTTON_WIDTH / 2, y + 25));
        this.addRenderableWidget(this.linkButton(Component.translatable("enhancedcelestials.ec2_links.shader_support"), Component.translatable("enhancedcelestials.ec2_links.shader_support.tooltip"), this.platform.shaderSupportUrl, centerX - BUTTON_WIDTH / 2, y + 50));

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds(centerX - BUTTON_WIDTH / 2, y + 85, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());
    }

    private Button linkButton(Component label, Component tooltip, String url, int x, int y) {
        return Button.builder(label, button -> this.minecraft.setScreen(new ConfirmLinkScreen(status -> {
                    if (status) {
                        Util.getPlatform().openUri(url);
                    }
                    this.minecraft.setScreen(this);
                }, url, true)))
                .tooltip(Tooltip.create(tooltip))
                .bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBlurredBackground(partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 70, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.previousScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public enum Platform {
        MODRINTH(
                "https://modrinth.com/mod/enhanced-celestials-2-core",
                "https://modrinth.com/mod/enhanced-celestials-2-default-lunar-events",
                "https://modrinth.com/mod/enhanced-celestials-2-shader-support"
        ),
        CURSEFORGE(
                "https://www.curseforge.com/minecraft/mc-mods/enhanced-celestials-2-core",
                "https://www.curseforge.com/minecraft/mc-mods/enhanced-celestials-2-default-lunar-events",
                "https://www.curseforge.com/minecraft/mc-mods/enhanced-celestials-2-shader-support"
        );

        private final String coreUrl;
        private final String lunarEventsUrl;
        private final String shaderSupportUrl;

        Platform(String coreUrl, String lunarEventsUrl, String shaderSupportUrl) {
            this.coreUrl = coreUrl;
            this.lunarEventsUrl = lunarEventsUrl;
            this.shaderSupportUrl = shaderSupportUrl;
        }
    }
}
