package de.teamlapen.vampirism.client.gui.components;

import de.teamlapen.vampirism.client.ClientBloodDrinkData;
import de.teamlapen.vampirism.config.VampirismConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Widget to display blood drink progression in the vampire menu
 */
public class BloodDrinkProgressWidget implements Renderable {
    private final Font font;
    private final int x;
    private final int y;
    private final int width;

    public BloodDrinkProgressWidget(int x, int y, int width) {
        this.font = Minecraft.getInstance().font;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int baseThreshold = VampirismConfig.BALANCE.vpHostileDrinkBaseThreshold.get();
        int increment = VampirismConfig.BALANCE.vpHostileDrinkThresholdIncrement.get();

        // Title
        graphics.drawString(font, Component.literal("§6Blood Mastery"), x, y, 0xFFFFFF, false);

        // Zombie progress
        int zombieTier = ClientBloodDrinkData.getZombieTier();
        int zombieCount = ClientBloodDrinkData.getZombieCount();
        int zombieRequired = baseThreshold + (zombieTier * increment);
        drawProgressLine(graphics, y + 12, "§aZombie", zombieTier, zombieCount, zombieRequired);

        // Enderman progress
        int endermanTier = ClientBloodDrinkData.getEndermanTier();
        int endermanCount = ClientBloodDrinkData.getEndermanCount();
        int endermanRequired = baseThreshold + (endermanTier * increment);
        drawProgressLine(graphics, y + 22, "§5Enderman", endermanTier, endermanCount, endermanRequired);

        // Creeper progress
        int creeperTier = ClientBloodDrinkData.getCreeperTier();
        int creeperCount = ClientBloodDrinkData.getCreeperCount();
        int creeperRequired = baseThreshold + (creeperTier * increment);
        drawProgressLine(graphics, y + 32, "§2Creeper", creeperTier, creeperCount, creeperRequired);
    }

    private void drawProgressLine(@NotNull GuiGraphics graphics, int yPos, String mobName, int tier, int count, int required) {
        // Format: "Zombie T5: 73/600"
        String text = String.format("%s §7T%d: §f%d/%d", mobName, tier, count, required);
        graphics.drawString(font, Component.literal(text), x + 2, yPos, 0xFFFFFF, false);

        // Draw progress bar
        int barX = x;
        int barY = yPos + 9;
        int barWidth = width;
        int barHeight = 3;

        // Background
        graphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF333333);

        // Progress fill
        float progress = Math.min(1.0f, (float) count / required);
        int fillWidth = (int) (barWidth * progress);
        int fillColor = progress >= 1.0f ? 0xFF00FF00 : 0xFFFFAA00; // Green when complete, orange otherwise
        graphics.fill(barX, barY, barX + fillWidth, barY + barHeight, fillColor);

        // Border
        graphics.fill(barX, barY, barX + barWidth, barY + 1, 0xFF000000); // Top
        graphics.fill(barX, barY + barHeight - 1, barX + barWidth, barY + barHeight, 0xFF000000); // Bottom
        graphics.fill(barX, barY, barX + 1, barY + barHeight, 0xFF000000); // Left
        graphics.fill(barX + barWidth - 1, barY, barX + barWidth, barY + barHeight, 0xFF000000); // Right
    }
}
