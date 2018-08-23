package net.machinemuse.powersuits.client.gui.tinker;

import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.powersuits.client.gui.MuseGui;
import net.machinemuse.powersuits.client.gui.tinker.frame.KeybindConfigFrame;
import net.machinemuse.powersuits.client.gui.tinker.frame.TabSelectFrame;
import net.machinemuse.powersuits.control.KeybindManager;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

public class KeyConfigGui extends MuseGui {
    private EntityPlayer player;
    protected KeybindConfigFrame frame;
    protected int worldx;
    protected int worldy;
    protected int worldz;

    public KeyConfigGui(EntityPlayer player, int x, int y, int z) {
        super();
        KeybindManager.readInKeybinds();
        this.player = player;
        this.xSize = 256;
        this.ySize = 226;
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
        frame = new KeybindConfigFrame(this,
                new MusePoint2D(absX(-0.95), absY(-0.95)),
                new MusePoint2D(absX(0.95), absY(0.95)), player);
        frames.add(frame);

        TabSelectFrame tabFrame = new TabSelectFrame(player, new MusePoint2D(absX(-0.95F), absY(-1.05f)), new MusePoint2D(absX(0.95F), absY(-0.95f)), worldx, worldy, worldz);
        frames.add(tabFrame);
    }

    @Override
    public void handleKeyboardInput() {
        try {
            super.handleKeyboardInput();
            frame.handleKeyboard();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        KeybindManager.writeOutKeybinds();
    }
}