package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.DrawableMuseRect;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.math.geometry.MuseRect;
import net.machinemuse.numina.utils.math.geometry.MuseRelativeRect;
import net.machinemuse.numina.utils.render.MuseRenderer;
import net.machinemuse.powersuits.client.render.modelspec.DefaultModelSpec;
import net.machinemuse.powersuits.common.config.CosmeticPresetSaveLoad;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableButton;
import net.machinemuse.powersuits.gui.tinker.scrollable.ScrollableLabel;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.machinemuse.powersuits.network.packets.MusePacketCosmeticInfo;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class LoadSaveResetSubFrame implements IGuiFrame {
    EntityPlayer player;
    public ItemSelectionFrame itemSelector;
    public DrawableMuseRect border;
    protected ClickableButton loadButton;
    protected ClickableButton saveButton;
    protected ClickableButton resetButton;
    ColourPickerFrame colourpicker;
    ScrollableLabel saveAsLabel;
    final boolean usingCosmeticPresets;
    final boolean allowCosmeticPresetCreation;

    final double originalBottom;
    final double originalTop;
    final double originalHeight;
    final double newHeight;
    protected PartManipContainer partframe;
    protected CosmeticPresetContainer cosmeticFrame;
    protected boolean isEditing;

    GuiTextField presetNameInputBox;


    public LoadSaveResetSubFrame(ColourPickerFrame colourpicker, EntityPlayer player, MuseRect borderRef, Colour insideColour, Colour borderColour, ItemSelectionFrame itemSelector, boolean usingCosmeticPresetsIn, boolean allowCosmeticPresetCreationIn, PartManipContainer partframe, CosmeticPresetContainer cosmeticFrame) {
        this.border = new DrawableMuseRect(borderRef, insideColour, borderColour);
        this.originalTop = border.top();
        this.originalHeight = border.height();
        this.originalBottom = border.bottom();
        this.newHeight = (Math.abs(colourpicker.getBorder().top() - originalBottom));
        double sizex = border.right() - border.left();
        double sizey = border.bottom() - border.top();
        this.itemSelector = itemSelector;
        this.colourpicker = colourpicker;
        this.saveAsLabel = new ScrollableLabel(I18n.format("gui.powersuits.saveAs"),  new MuseRelativeRect(border.left(), colourpicker.getBorder().top() + 20, border.right(), colourpicker.getBorder().top() + 30)).setEnabled(false);
        presetNameInputBox = new GuiTextField(0, MuseRenderer.getFontRenderer(), (int) (border.left()) + 2, (int) (saveAsLabel.bottom() + 10), (int) border.width() - 4, 20);

        this.loadButton = new ClickableButton(
                I18n.format("gui.powersuits.load"),
                new MusePoint2D(border.left() + sizex * 2.5 / 12.0, border.bottom() - sizey / 2.0), true);
        this.saveButton = new ClickableButton(
                I18n.format("gui.powersuits.save"),
                new MusePoint2D(border.right() - sizex * 2.5 / 12.0, border.bottom() - sizey / 2.0), true);

        this.resetButton = new ClickableButton(
                I18n.format("gui.powersuits.reset"),
                new MusePoint2D(border.left() + sizex / 2.0, border.bottom() - sizey / 2.0), true);

        presetNameInputBox.setEnabled(false);
        presetNameInputBox.setVisible(false);
        presetNameInputBox.setFocused(false);
        presetNameInputBox.setMaxStringLength((int) border.width());
        presetNameInputBox.setText("");

        this.usingCosmeticPresets = usingCosmeticPresetsIn;
        this.allowCosmeticPresetCreation = allowCosmeticPresetCreationIn;

        this.partframe = partframe;
        this.cosmeticFrame = cosmeticFrame;
        this.isEditing = false;

        if (usingCosmeticPresets)
            showPresetFrame();
        else
            showPartManipFrame();
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        if (itemSelector.getSelectedItem() == null || itemSelector.getSelectedItem().getItem().isEmpty())
            return;

        if (allowCosmeticPresetCreation) {
            /**
             * if colour picker frame enabled/visible
             - hide colour picker frame
             - resize this frame
             - show "Save As" label
             - show text input box
             - change loadButton button text to cancel -> use this to exit
             - change resetButton button text to clear -> use this to clear text input
             - show preset subframe
             */
            if (saveButton.hitBox(x, y)) {
                if (colourpicker.isEnabled) {
                    openSaveGUI();
                    /**
                     * if colour picker frame disabled/invisible
                     - loadButton/cancel unchanged
                     - check text input box for text
                     - if saveButton successful then resetButton to previous state but do not clear text in case user wants to saveButton other armor piece customization under same name



                     hide colour picker frame
                     - resize this frame
                     - show "Save As" label
                     - show text input box
                     - change loadButton button text to cancel
                     - change resetButton button text to clear
                     - show preset subframe
                     */
                } else if (!presetNameInputBox.getText().isEmpty()) {
                    boolean save = CosmeticPresetSaveLoad.savePreset(presetNameInputBox.getText(), itemSelector.getSelectedItem().getItem());
                    if (save) {
                        closeSaveGUI();
                    }
                }
            }


            if (loadButton.hitBox(x, y)) {
                // cancel
                if (!colourpicker.isEnabled)
                    closeSaveGUI();
                // TODO: loading screen

            }
        } else {
            saveButton.setEnabled(false);
            saveButton.hide();
            loadButton.setEnabled(false);
            loadButton.hide();
        }

        if (!itemSelector.getSelectedItem().getItem().isEmpty()) {


            if (resetButton.hitBox(x, y)) {
                // reset cosmetic tag to defaults
                if (colourpicker.isEnabled) {
                    NBTTagCompound nbt = DefaultModelSpec.makeModelPrefs(itemSelector.getSelectedItem().getItem());
                    PacketSender.sendToServer(new MusePacketCosmeticInfo(player, itemSelector.getSelectedItem().inventorySlot, NuminaNBTConstants.TAG_RENDER, nbt).getPacket131());
                } else {
                    // reset text in text input box
                    presetNameInputBox.setText("");
                }
            }
        }
    }

    void closeSaveGUI() {
        boolean boolVal = false;
        presetNameInputBox.setEnabled(boolVal);
        presetNameInputBox.setVisible(boolVal);
        presetNameInputBox.setFocused(boolVal);
        colourpicker.setIsEnabled(!boolVal);
        this.border.setTop(originalTop).setHeight(originalHeight);
        saveAsLabel.setEnabled(boolVal);
        loadButton.setLable(I18n.format("gui.powersuits.load"));
        partframe.hide();
        partframe.disable();
        cosmeticFrame.enable();
        cosmeticFrame.show();






        // fixme: hide cosmetic manip subframe
        // fixme: show cosmeitc preset selection subframe
    }

    void openSaveGUI () {
        boolean boolVal = true;
        presetNameInputBox.setEnabled(boolVal);
        presetNameInputBox.setVisible(boolVal);
        presetNameInputBox.setFocused(boolVal);
        colourpicker.setIsEnabled(!boolVal);
        this.border.setTop(colourpicker.getBorder().top()).setHeight(newHeight);
        saveAsLabel.setEnabled(boolVal);
        loadButton.setLable(I18n.format("gui.powersuits.cancel"));

        partframe.show();
        partframe.enable();
        cosmeticFrame.disable();
        cosmeticFrame.hide();


        // fixme: hide cosmeitc preset selection subframe
        // fixme: show cosmetic manip subframe
    }

    void showPresetFrame() {
        cosmeticFrame.enable();
        cosmeticFrame.show();
        partframe.disable();
        partframe.hide();
    }

    void showPartManipFrame() {
        cosmeticFrame.disable();
        cosmeticFrame.hide();
        partframe.enable();
        partframe.show();
    }


    @Override
    public void onMouseUp(double x, double y, int button) {

    }

    @Override
    public void update(double mousex, double mousey) {
        resetButton.setEnabled(true);
        resetButton.show();

        // cosmetic preset creation mode
        if (isEditing) {
            showPartManipFrame();
            loadButton.setEnabled(true);
            loadButton.show();

            saveButton.setEnabled(true);
            saveButton.show();

        } else if (usingCosmeticPresets) {

            // allowed to create cosmetic presets but not yet doing so
            if (allowCosmeticPresetCreation && !isEditing) {
                saveButton.setEnabled(true);
                saveButton.show();
                saveButton.setLable("gui.powersuits.new");

            } else {
                showPresetFrame();
                loadButton.setEnabled(false);
                loadButton.hide();

                saveButton.setEnabled(false);
                saveButton.hide();

                colourpicker.setIsEnabled(false);
                presetNameInputBox.setEnabled(false);
                presetNameInputBox.setVisible(false);
                presetNameInputBox.setFocused(false);
            }
        } else {
            if (!MPSConfig.INSTANCE.allowPowerFistCustomization() &&
                    itemSelector.getSelectedItem() != null && itemSelector.getSelectedItem().getItem().getItem() instanceof ItemPowerFist) {
                showPresetFrame();

                loadButton.setEnabled(false);
                loadButton.hide();

                saveButton.setEnabled(false);
                saveButton.hide();

                colourpicker.setIsEnabled(false);
                presetNameInputBox.setEnabled(false);
                presetNameInputBox.setVisible(false);
                presetNameInputBox.setFocused(false);
            } else {
                showPartManipFrame();
                loadButton.setEnabled(false);
                loadButton.hide();

                saveButton.setEnabled(false);
                saveButton.hide();

                colourpicker.setIsEnabled(true);
                presetNameInputBox.setEnabled(false);
                presetNameInputBox.setVisible(false);
                presetNameInputBox.setFocused(false);
            }
        }




//        loadButton.setEnabled(true);
//        saveButton.setEnabled(true);
//        resetButton.setEnabled(true);
    }

    @Override
    public void draw() {
        border.draw();
        loadButton.draw();
        saveButton.draw();
        resetButton.draw();
        saveAsLabel.draw();
        presetNameInputBox.drawTextBox();
    }

    private static boolean isValidCharacterForName(char typedChar, int keyCode) {
//        System.out.println("character: " + typedChar + "; keycode: " + keyCode);
        if (keyCode == 14 || // backspace;
                keyCode == 12 || // - ; 147 is _; 52 is .
                keyCode == 147 || //
                Character.isDigit(typedChar) || Character.isLetter(typedChar))
            return true;
        return false;
    }


    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.presetNameInputBox.getVisible() && isValidCharacterForName(typedChar, keyCode)) {
            this.presetNameInputBox.textboxKeyTyped(typedChar, keyCode);
        }
    }
}
