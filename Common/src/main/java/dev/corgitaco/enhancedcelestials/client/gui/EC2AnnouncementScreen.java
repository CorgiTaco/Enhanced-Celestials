package dev.corgitaco.enhancedcelestials.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class EC2AnnouncementScreen extends Screen {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;

    private MultiLineLabel message = MultiLineLabel.EMPTY;

    public EC2AnnouncementScreen() {
        super(Component.translatable("enhancedcelestials.ec2_announcement.title"));
    }

    @Override
    protected void init() {
        this.message = MultiLineLabel.create(this.font, Component.translatable("enhancedcelestials.ec2_announcement.message"), 280);

        int centerX = this.width / 2;
        int y = this.height / 2;

        this.addRenderableWidget(Button.builder(Component.translatable("enhancedcelestials.ec2_announcement.modrinth"), button -> this.minecraft.setScreen(new EC2LinksScreen(this, EC2LinksScreen.Platform.MODRINTH)))
                .tooltip(Tooltip.create(Component.translatable("enhancedcelestials.ec2_announcement.modrinth.tooltip")))
                .bounds(centerX - BUTTON_WIDTH / 2, y - 30, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        this.addRenderableWidget(Button.builder(Component.translatable("enhancedcelestials.ec2_announcement.curseforge"), button -> this.minecraft.setScreen(new EC2LinksScreen(this, EC2LinksScreen.Platform.CURSEFORGE)))
                .tooltip(Tooltip.create(Component.translatable("enhancedcelestials.ec2_announcement.curseforge.tooltip")))
                .bounds(centerX - BUTTON_WIDTH / 2, y - 5, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        this.addRenderableWidget(Button.builder(Component.translatable("enhancedcelestials.ec2_announcement.dismiss"), button -> this.onClose())
                .tooltip(Tooltip.create(Component.translatable("enhancedcelestials.ec2_announcement.dismiss.tooltip")))
                .bounds(centerX - BUTTON_WIDTH / 2, y + 25, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 90, 0xFFFFFF);
        this.message.renderCentered(graphics, this.width / 2, this.height / 2 - 65, 10, 0xA0A0A0);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
