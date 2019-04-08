package net.machinemuse.powersuits.client.gui.tinker;

import net.machinemuse.numina.client.gui.MuseGui;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.powersuits.client.gui.tinker.frame.DetailedSummaryFrame;
import net.machinemuse.powersuits.client.gui.tinker.frame.ItemSelectionFrame;
import net.machinemuse.powersuits.client.gui.tinker.frame.ModuleSelectionFrame;
import net.machinemuse.powersuits.client.gui.tinker.frame.ModuleTweakFrame;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

/**
 * The gui class for the TinkerTable block.
 *
 * @author MachineMuse
 */
public class GuiFieldTinker extends MuseGui {
    protected final EntityPlayerSP player;
    protected ItemSelectionFrame itemSelectFrame;

    /**
     * Constructor. Takes a player as an argument.
     *
     * @param player
     */
    public GuiFieldTinker(EntityPlayer player) {
        this.player = (EntityPlayerSP) player;
//        this.xSize = 256;
//        this.ySize = 200;
//        this.xSize = 400;
//        this.ySize = 244;
        this.xSize = Math.min(mc.mainWindow.getScaledWidth() - 50, 500);
        this.ySize = Math.min(mc.mainWindow.getScaledHeight() - 50, 300);
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
                Colour.LIGHTBLUE.withAlpha(0.8),
                Colour.DARKBLUE.withAlpha(0.8),
                itemSelectFrame);
        frames.add(statsFrame);

        ModuleSelectionFrame moduleSelectFrame = new ModuleSelectionFrame(
                new MusePoint2D(absX(-0.75F), absY(-0.95f)),
                new MusePoint2D(absX(-0.05F), absY(0.95f)),
                Colour.LIGHTBLUE.withAlpha(0.8),
                Colour.DARKBLUE.withAlpha(0.8),
                itemSelectFrame);
        frames.add(moduleSelectFrame);

        ModuleTweakFrame tweakFrame = new ModuleTweakFrame(player,
                new MusePoint2D(absX(0f), absY(-0.25f)),
                new MusePoint2D(absX(0.95f), absY(0.95f)),
                Colour.LIGHTBLUE.withAlpha(0.8),
                Colour.DARKBLUE.withAlpha(0.8),
                itemSelectFrame,
                moduleSelectFrame);
        frames.add(tweakFrame);
    }

    @Override
    public void render(int x, int y, float z) {
        super.render(x, y, z);
        if (itemSelectFrame.hasNoItems()) {
            double centerx = absX(0);
            double centery = absY(0);
            MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.noModulesFound.line1"), centerx, centery - 5);
            MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.noModulesFound.line2"), centerx, centery + 5);
        }
    }
}
