package net.machinemuse.powersuits.common.gui.tinker;

import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.math.geometry.MuseRect;
import net.machinemuse.powersuits.common.gui.MuseGui;
import net.machinemuse.powersuits.common.gui.tinker.frame.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:32 PM, 29/04/13
 *
 * Ported to Java by lehjr on 10/19/16.
 */
public class CosmeticGui extends MuseGui {
    EntityPlayer player;
    int worldx;
    int worldy;
    int worldz;
    ItemSelectionFrame itemSelect;
//    ItemStack lastSelectedItem;

    public CosmeticGui(EntityPlayer player, int worldx, int worldy, int worldz) {
        this.player = player;
        this.worldx = worldx;
        this.worldy = worldy;
        this.worldz = worldz;
//        this.xSize = 256;
//        this.ySize = 200;
//        this.xSize = 400;
//        this.ySize = 244;

        ScaledResolution screen = new ScaledResolution(Minecraft.getMinecraft());
        this.xSize = Math.min(screen.getScaledWidth() - 50, 500);
        this.ySize = Math.min(screen.getScaledHeight() - 50, 300);
    }

    /**
     * Add the buttons (and other controls) to the screen.
     */
    @Override
    public void initGui() {
        super.initGui();
        itemSelect = new ItemSelectionFrame(
                new MusePoint2D(absX(-0.95F), absY(-0.95F)),
                new MusePoint2D(absX(-0.78F), absY(-0.025F)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F), player);
        frames.add(itemSelect);

        PlayerModelViewFrame renderframe = new PlayerModelViewFrame(
                itemSelect,
                new MusePoint2D(absX(-0.75F), absY(-0.95f)),
                new MusePoint2D(absX(0.15F), absY(-0.025f)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F));
        frames.add(renderframe);

        ColourPickerFrame colourpicker = new ColourPickerFrame(
                new MuseRect(absX(0.18f), absY(-0.95f),
                        absX(0.95f), absY(-0.025f)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F),
                itemSelect);
        frames.add(colourpicker);

        PartManipContainer partframe = new PartManipContainer(
                itemSelect, colourpicker,
                new MusePoint2D(absX(-0.95F), absY(0.025f)),
                new MusePoint2D(absX(+0.95F), absY(0.95f)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F));
        frames.add(partframe);

        TabSelectFrame tabFrame = new TabSelectFrame(
                player,
                new MusePoint2D(absX(-0.95F), absY(-1.05f)),
                new MusePoint2D(absX(0.95F), absY(-0.95f)),
                worldx, worldy, worldz);
        frames.add(tabFrame);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        super.drawScreen(x, y, z);
    }
}
