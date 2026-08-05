package com.anatolyonhardmode.russiansurvival.client;

import com.anatolyonhardmode.russiansurvival.config.ClientConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class ColdHud {
    public static void render(GuiGraphics graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        if (!ClientConfig.values.showColdHud || client.player == null || client.options.hideGui) return;
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        int barWidth = 96;
        int x = width / 2 + ClientConfig.values.hudOffsetX + 8;
        int y = height - 65 + ClientConfig.values.hudOffsetY;
        float cold = Math.max(0, Math.min(100, ClientColdState.cold));
        int filled = Math.round(barWidth * cold / 100.0F);
        boolean pulse = cold >= 90 && !ClientConfig.values.reducedNausea && (client.player.tickCount / 5) % 2 == 0;
        int color = pulse ? 0xFFFFFFFF : interpolateColor(cold / 100.0F);

        graphics.fill(x - 14, y - 3, x + barWidth + 5, y + 17, 0xA0142533);
        graphics.fill(x - 13, y - 2, x + barWidth + 4, y + 16, 0x703F7893);
        graphics.fill(x, y + 7, x + barWidth + 2, y + 14, 0xE008121A);
        graphics.fill(x + 1, y + 8, x + 1 + filled, y + 13, color);
        for (int mark = 1; mark < 10; mark++) {
            int markX = x + 1 + mark * barWidth / 10;
            graphics.fill(markX, y + 8, markX + 1, y + 13, 0x70081722);
        }
        graphics.drawString(client.font, "\u2744", x - 11, y + 4, 0xE8F9FF, true);
        Component label = Component.translatable("hud.russian_survival.cold", Math.round(cold));
        graphics.drawString(client.font, label, x, y - 2, 0xE5F7FF, true);
        if (cold >= 40 && ClientConfig.values.coldVignetteIntensity > 0) {
            renderVignette(graphics, width, height, cold, pulse);
        }
    }

    private static int interpolateColor(float t) {
        int r = (int) (54 + (225 - 54) * t);
        int g = (int) (151 + (248 - 151) * t);
        int b = (int) (210 + (255 - 210) * t);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    private static void renderVignette(GuiGraphics graphics, int width, int height, float cold, boolean pulse) {
        float severity = Math.min(1.0F, (cold - 40.0F) / 60.0F);
        float configured = Math.max(0.0F, Math.min(1.5F, ClientConfig.values.coldVignetteIntensity));
        int depth = 8 + Math.round(22 * severity);
        for (int i = 0; i < depth; i++) {
            float outer = 1.0F - i / (float) depth;
            int alpha = Math.min(150, Math.round((10 + 92 * severity) * outer * outer * configured));
            if (pulse) alpha = Math.min(175, alpha + 16);
            int blue = (alpha << 24) | 0x8EDBFF;
            graphics.fill(i, i, width - i, i + 1, blue);
            graphics.fill(i, height - i - 1, width - i, height - i, blue);
            graphics.fill(i, i, i + 1, height - i, blue);
            graphics.fill(width - i - 1, i, width - i, height - i, blue);
        }
        int cornerAlpha = Math.min(110, Math.round(70 * severity * configured));
        int frost = (cornerAlpha << 24) | 0xD9F5FF;
        int corner = 5 + Math.round(12 * severity);
        graphics.fill(0, 0, corner, 3, frost);
        graphics.fill(0, 0, 3, corner, frost);
        graphics.fill(width - corner, 0, width, 3, frost);
        graphics.fill(width - 3, 0, width, corner, frost);
        graphics.fill(0, height - 3, corner, height, frost);
        graphics.fill(0, height - corner, 3, height, frost);
        graphics.fill(width - corner, height - 3, width, height, frost);
        graphics.fill(width - 3, height - corner, width, height, frost);
    }
    private ColdHud() {}
}
