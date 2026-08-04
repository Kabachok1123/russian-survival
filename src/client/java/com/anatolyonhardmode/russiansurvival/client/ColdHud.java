package com.anatolyonhardmode.russiansurvival.client;

import com.anatolyonhardmode.russiansurvival.config.ClientConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class ColdHud {
    public static void render(GuiGraphics graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        if (!ClientConfig.values.showColdHud || client.player == null || client.options.hideGui) return;
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        int x = width / 2 + ClientConfig.values.hudOffsetX + 10;
        int y = height - 60 + ClientConfig.values.hudOffsetY;
        int barWidth = 81;
        float cold = Math.max(0, Math.min(100, ClientColdState.cold));
        int filled = Math.round(barWidth * cold / 100.0F);
        boolean pulse = cold >= 90 && !ClientConfig.values.reducedNausea && (client.player.tickCount / 5) % 2 == 0;
        int color = pulse ? 0xFFFFFFFF : interpolateColor(cold / 100.0F);
        graphics.fill(x, y, x + barWidth + 2, y + 7, 0xB0000000);
        graphics.fill(x + 1, y + 1, x + 1 + filled, y + 6, color);
        graphics.drawString(client.font, "\u2744", x - 10, y - 1, 0xD9F6FF, true);
        if (cold >= 40 && ClientConfig.values.coldVignetteIntensity > 0) {
            int alpha = (int) (Math.min(0.28F, (cold - 40) / 215.0F) * ClientConfig.values.coldVignetteIntensity * 255);
            int edge = (alpha << 24) | 0xB5E8FF;
            graphics.fill(0, 0, width, 5, edge);
            graphics.fill(0, height - 5, width, height, edge);
            graphics.fill(0, 0, 5, height, edge);
            graphics.fill(width - 5, 0, width, height, edge);
        }
    }

    private static int interpolateColor(float t) {
        int r = (int) (146 + (235 - 146) * t);
        int g = (int) (220 + (250 - 220) * t);
        int b = 255;
        return 0xFF000000 | r << 16 | g << 8 | b;
    }
    private ColdHud() {}
}
