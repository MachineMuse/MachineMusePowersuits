package net.machinemuse.powersuits.client.gui.tinker;

import net.machinemuse.general.gui.frame.ItemSelectionFrame;
import net.minecraft.entity.player.EntityPlayer;

public class CosmeticGUI2 extends MuseGui {
    EntityPlayer player;
    int worldx;
    int worldy;
    int worldz;
    ItemSelectionFrame itemSelect;

    public CosmeticGUI2(EntityPlayer player, int worldx, int worldy, int worldz) {
        this.player = player;
        this.worldx = worldx;
        this.worldy = worldy;
        this.worldz = worldz;
        this.xSize = 256;
        this.ySize = 200;
    }

}
