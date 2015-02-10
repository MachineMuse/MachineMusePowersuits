package net.machinemuse.general.gui;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.general.gui.frame.*;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;

/**
 * The gui class for the TinkerTable block.
 *
 * @author MachineMuse
 */
public class GuiTinkerTable extends MuseGui {
    protected EntityClientPlayerMP player;

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
        this.player = (EntityClientPlayerMP) player;
        this.xSize = 256;
        this.ySize = 200;
    }

    public GuiTinkerTable(EntityPlayer player, int x, int y, int z) {
        this.player = (EntityClientPlayerMP) player;
        this.xSize = 256;
        this.ySize = 200;
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

        ItemInfoFrame statsFrame = new ItemInfoFrame(player, new MusePoint2D(absX(0f), absY(-0.9f)), new MusePoint2D(absX(0.9f), absY(-0.05f)),
                Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame);
        frames.add(statsFrame);

        ModuleSelectionFrame moduleSelectFrame = new ModuleSelectionFrame(new MusePoint2D(absX(-0.75F), absY(-0.95f)), new MusePoint2D(absX(-0.05F),
                absY(0.55f)), Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame);
        frames.add(moduleSelectFrame);

        InstallSalvageFrame installFrame = new InstallSalvageFrame(player, new MusePoint2D(absX(-0.75F), absY(0.6f)), new MusePoint2D(absX(-0.05F),
                absY(0.95f)), Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame, moduleSelectFrame);
        frames.add(installFrame);

        ModuleTweakFrame tweakFrame = new ModuleTweakFrame(player, new MusePoint2D(absX(0f), absY(0f)), new MusePoint2D(absX(0.9f), absY(0.9f)),
                Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame, moduleSelectFrame);
        frames.add(tweakFrame);

        TabSelectFrame tabFrame = new TabSelectFrame(player, new MusePoint2D(absX(-0.95F), absY(-1.05f)),new MusePoint2D(absX(0.95F), absY(-0.95f)), worldx, worldy, worldz);
        frames.add(tabFrame);
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        super.drawScreen(x, y, z);
        if (itemSelectFrame.hasNoItems()) {
            double centerx = absX(0);
            double centery = absY(0);
            String noItems = StatCollector.translateToLocal("tile.tinkerTable.ui.noItemsFound");
            int indexCenter = noItems.lastIndexOf(' ', ( noItems.length() / 2 ));
            MuseRenderer.drawCenteredString(noItems.substring(0, indexCenter), centerx, centery - 5);
            MuseRenderer.drawCenteredString(noItems.substring(indexCenter + 1, noItems.length()), centerx, centery + 5);
        }
    }
}
