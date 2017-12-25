package net.machinemuse.numina.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Ported to Java by lehjr on 10/10/16.
 *
 * FIXME: this is currently broken. There is something missing in all of this as player inventory is lost and player is still somehow still marked as dead
 * FIXME: the above issue also causes this GUI to open again after clicking a button
 */
@SideOnly(Side.CLIENT)
public class GuiGameOverPlus extends GuiScreen {
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        System.out.println("init GameOverPlus");


        this.buttonList.clear();
        if (this.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
            if (this.mc.isIntegratedServerRunning()) {
                addButton(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld")));
            } else {
                addButton(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer")));
            }
        } else {
            addButton(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn")));
            addButton(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen")));
            addButton(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120, "Revive"));
            if (this.mc.getSession() == null) {
                ((GuiButton) this.buttonList.get(1)).enabled = false;
            }
        }
        for (GuiButton aButtonList : (Iterable<GuiButton>) this.buttonList) {
            aButtonList.enabled = false;
        }
    }

//    public void addButton(GuiButton b) {
//        this.buttonList.add(b);
//    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
    }


    @Override
    public void confirmClicked(boolean result, int id) {
        System.out.println("result: " + result);
        System.out.println("id: " + id);


        if (result) {
            if (this.mc.world != null) {
                this.mc.world.sendQuittingDisconnectingPacket();
            }

            this.mc.loadWorld((WorldClient) null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        } else {


            this.mc.player.respawnPlayer();
            this.mc.displayGuiScreen((GuiScreen) null);
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton guiButton) {
        switch (guiButton.id) {
            case 0:
                this.mc.player.respawnPlayer();
                this.mc.displayGuiScreen(null);
                break;
            case 1:
                this.mc.world.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
            case 2:
                this.mc.player.setHealth(10f);
                this.mc.player.isDead = false;
                this.mc.player.extinguish();
                this.mc.displayGuiScreen(null);
                break;
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean flag = this.mc.world.getWorldInfo().isHardcoreModeEnabled();
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRenderer, I18n.format(flag ? "deathScreen.title.hardcore" : "deathScreen.title"), this.width / 2 / 2, 30, 16777215);
        GlStateManager.popMatrix();
        if (flag) {
            this.drawCenteredString(this.fontRenderer, I18n.format("deathScreen.hardcoreInfo"), this.width / 2, 144, 16777215);
        }
        this.drawCenteredString(this.fontRenderer, I18n.format("deathScreen.score") + ": " + TextFormatting.YELLOW + this.mc.player.getScore(), this.width / 2, 100, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.cooldownTimer += 1;
        if (this.cooldownTimer == 20) {
            for (GuiButton aButtonList : (Iterable<GuiButton>) this.buttonList) {
                aButtonList.enabled = true;
            }
        }
    }

    /**
     * The cooldown timer for the buttons, increases every tick and enables all buttons when reaching 20.
     */
    private int cooldownTimer = 0;
}
