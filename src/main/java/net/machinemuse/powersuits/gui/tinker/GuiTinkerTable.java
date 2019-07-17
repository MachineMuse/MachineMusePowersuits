package net.machinemuse.powersuits.gui.tinker;

import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.powersuits.gui.MuseGui;
import net.machinemuse.powersuits.gui.tinker.frame.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;


/**
 * The gui class for the TinkerTable block.
 *
 * @author MachineMuse
 */
public class GuiTinkerTable extends MuseGui {
    protected final EntityPlayerSP player;
    protected ItemSelectionFrame itemSelectFrame;
    protected int worldx;
    protected int worldy;
    protected int worldz;

    /**
     * Constructor. Takes a player as an argument.
     *
     * @param player
     */
    public GuiTinkerTable(EntityPlayer player) {
        this.player = (EntityPlayerSP) player;
        ScaledResolution screen = new ScaledResolution(Minecraft.getMinecraft());
        this.xSize = Math.min(screen.getScaledWidth() - 50, 500);
        this.ySize = Math.min(screen.getScaledHeight() - 50, 300);
    }

    public GuiTinkerTable(EntityPlayer player, int x, int y, int z) {
        this.player = (EntityPlayerSP) player;
        ScaledResolution screen = new ScaledResolution(Minecraft.getMinecraft());
        this.xSize = Math.min(screen.getScaledWidth() - 50, 500);
        this.ySize = Math.min(screen.getScaledHeight() - 50, 300);

        this.worldx = x;
        this.worldy = y;
        this.worldz = z;
    }

    /**
     * Add the buttons (and other controls) to the screen.
     */
    @Override
    public void initGui() {
        super.initGui();
        itemSelectFrame = new ItemSelectionFrame(new MusePoint2D(absX(-0.95F), absY(-0.95F)), new MusePoint2D(absX(-0.78F), absY(0.95F)),
                Colour.LIGHTBLUE.withAlpha(0.8F), Colour.DARKBLUE.withAlpha(0.8F), player);
        frames.add(itemSelectFrame);

        DetailedSummaryFrame statsFrame = new DetailedSummaryFrame(player,
                new MusePoint2D(absX(0f), absY(-0.9f)),
                new MusePoint2D(absX(0.95f), absY(-0.3f)),
                Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame);
        frames.add(statsFrame);

        ModuleSelectionFrame moduleSelectFrame = new ModuleSelectionFrame(new MusePoint2D(absX(-0.75F), absY(-0.95f)), new MusePoint2D(absX(-0.05F),
                absY(0.55f)), Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame);
        frames.add(moduleSelectFrame);

        InstallSalvageFrame installFrame = new InstallSalvageFrame(player, new MusePoint2D(absX(-0.75F), absY(0.6f)), new MusePoint2D(absX(-0.05F),
                absY(0.95f)), Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame, moduleSelectFrame);
        frames.add(installFrame);

        ModuleTweakFrame tweakFrame = new ModuleTweakFrame(player,
                new MusePoint2D(absX(0f), absY(-0.25f)),
                new MusePoint2D(absX(0.95f), absY(0.95f)),
                Colour.LIGHTBLUE.withAlpha(0.8),
                Colour.DARKBLUE.withAlpha(0.8),
                itemSelectFrame,
                moduleSelectFrame);
        frames.add(tweakFrame);

        TabSelectFrame tabFrame = new TabSelectFrame(player, new MusePoint2D(absX(-0.95F), absY(-1.05f)), new MusePoint2D(absX(0.95F), absY(-0.95f)), worldx, worldy, worldz);
        frames.add(tabFrame);
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        super.drawScreen(x, y, z);
        if (itemSelectFrame.hasNoItems()) {
            double centerx = absX(0);
            double centery = absY(0);
            MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.noModulesFound.line1"), centerx, centery - 5);
            MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.noModulesFound.line2"), centerx, centery + 5);
        }
    }
}