package net.machinemuse.powersuits.client.gui.tinker;

import net.machinemuse.numina.client.gui.MuseGui;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.powersuits.client.control.KeybindManager;
import net.machinemuse.powersuits.client.gui.tinker.frame.KeybindConfigFrame;
import net.machinemuse.powersuits.client.gui.tinker.frame.TabSelectFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class KeyConfigGui extends MuseGui {
    protected KeybindConfigFrame frame;
    protected int worldx;
    protected int worldy;
    protected int worldz;
    private EntityPlayer player;

    public KeyConfigGui(EntityPlayer player) {
        super();
        KeybindManager.readInKeybinds();
        this.player = player;
//        this.xSize = 256;
//        this.ySize = 226;
//        this.xSize = 400;
//        this.ySize = 244;
        this.xSize = mc.mainWindow.getScaledWidth() - 50;
        this.ySize = mc.mainWindow.getScaledHeight() - 50;

        BlockPos pos = player.getPosition();
        this.worldx = pos.getX();
        this.worldy = pos.getY();
        this.worldz = pos.getZ();
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
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
        return false;
    }

    /*

     int key, int scanCode, int action, int modifiers) /// no idea what these are or do

    IGuiEventListener


        default boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        return false;
    }

    default boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        return false;
    }

    default boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        return false;
    }

    default boolean mouseScrolled(double p_mouseScrolled_1_) {
        return false;
    }

    default boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return false;
    }

    default boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
        return false;
    }

    default boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        return false;
    }

    default void focusChanged(boolean p_205700_1_) {
    }

    default boolean canFocus() {
        return false;
    }
     */









//    @Override
//    public void handleKeyboardInput() {
//        try {
//            super.handleKeyboardInput();
//            frame.handleKeyboard();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        KeybindManager.writeOutKeybinds();
    }
}