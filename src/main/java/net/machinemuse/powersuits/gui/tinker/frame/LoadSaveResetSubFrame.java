package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.common.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.item.MuseItemUtils;
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
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableItem;
import net.machinemuse.powersuits.gui.tinker.scrollable.ScrollableLabel;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.machinemuse.powersuits.network.MPSPackets;
import net.machinemuse.powersuits.network.packets.MusePacketCosmeticInfo;
import net.machinemuse.powersuits.network.packets.MusePacketCosmeticPresetUpdate;
import net.machinemuse.powersuits.utils.nbt.MPSNBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
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
    protected int lastCosmeticItem = -1;

    GuiTextField presetNameInputBox;

    public LoadSaveResetSubFrame(ColourPickerFrame colourpicker, EntityPlayer player, MuseRect borderRef, Colour insideColour, Colour borderColour, ItemSelectionFrame itemSelector, boolean usingCosmeticPresetsIn, boolean allowCosmeticPresetCreationIn, PartManipContainer partframe, CosmeticPresetContainer cosmeticFrame) {
        this.player = player;
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

        textInputOff();
        presetNameInputBox.setMaxStringLength((int) border.width());
        presetNameInputBox.setText("");

        this.usingCosmeticPresets = usingCosmeticPresetsIn;
        this.allowCosmeticPresetCreation = allowCosmeticPresetCreationIn;

        this.partframe = partframe;
        this.cosmeticFrame = cosmeticFrame;
        this.isEditing = false;

        if (usingCosmeticPresets) {
            if (allowCosmeticPresetCreation)
                cosmeticPresetCreator ();
            else
                cosmeticPresetsNormal();
        } else
            setLegacyMode();
    }

    /**
     * settings for the classic style cosmetic configuration
     */
    void setLegacyMode() {
        saveButton.buttonOff();
        loadButton.buttonOff();
        cosmeticFrame.frameOff();
        partframe.frameOn();
        colourPickerSetOpen();
    }

    /**
     * settings for cosmetic preset mode (normal user)
     */
    void cosmeticPresetsNormal() {
        saveButton.buttonOff();
        loadButton.buttonOff();
        colourpickerSetClosed();
        cosmeticFrame.frameOn();
        partframe.frameOff();
    }

    /**
     * settings for cosmetic preset mode (creator)
     */
    void cosmeticPresetCreator () {
        if (isEditing) {
            loadButton.buttonOn();
            loadButton.setLable(I18n.format("gui.powersuits.cancel"));
            saveButton.buttonOn();
            saveButton.setLable(I18n.format("gui.powersuits.save"));
            showPartManipFrame();
            // save as dialog
            if (presetNameInputBox.getVisible()) {
                colourpickerSetClosed();
            } else {
                colourPickerSetOpen();
            }
        } else {
            textInputOff();
            showPresetFrame();
            colourpickerSetClosed();
            loadButton.buttonOff();
            saveButton.buttonOn();
            saveButton.setLable(I18n.format("gui.powersuits.new"));
        }
    }

    void colourPickerSetOpen() {
        this.border.setTop(originalTop).setHeight(originalHeight);
        colourpicker.frameOn();
        saveAsLabel.setEnabled(false);
    }

    void colourpickerSetClosed() {
        this.border.setTop(colourpicker.getBorder().top()).setHeight(newHeight);
        colourpicker.frameOff();
    }

    void textInputOn () {
        presetNameInputBox.setEnabled(true);
        presetNameInputBox.setVisible(true);
        presetNameInputBox.setFocused(true);
        saveAsLabel.setEnabled(true);
    }

    void textInputOff() {
        presetNameInputBox.setEnabled(false);
        presetNameInputBox.setVisible(false);
        presetNameInputBox.setFocused(false);
        saveAsLabel.setEnabled(false);
    }

    void closeSaveGUI() {
        boolean boolVal = false;
        presetNameInputBox.setEnabled(boolVal);
        presetNameInputBox.setVisible(boolVal);
        presetNameInputBox.setFocused(boolVal);
        colourpicker.enable();
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

    /**
     * Get's the equipment slot the item is for.
     */
    public EntityEquipmentSlot getEquipmentSlot() {
        ItemStack selectedItem = getSelectedItem().getItem();
        if (!selectedItem.isEmpty() && selectedItem.getItem() instanceof ItemPowerArmor)
            return ((ItemPowerArmor) selectedItem.getItem()).armorType;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        ItemStack heldItem = player.getHeldItemOffhand();

        if (!heldItem.isEmpty() && MuseItemUtils.stackEqualExact(selectedItem, heldItem))
            return EntityEquipmentSlot.OFFHAND;
        return EntityEquipmentSlot.MAINHAND;
    }

    /**
     * This is just for resetting the cosmetic tag to a preset and is called when either
     * switching to a new tab or when exiting the GUI altogether
     */
    public void onGuiClosed() {
        if (allowCosmeticPresetCreation) {
            // FIXME: update tag to cosmetic preset


            System.out.println("creator gui closed and was editing: " + isEditing);


        }


//        if (itemSelector.getSelectedItem() != null && itemSelector.getSelectedItem().getItem().getItem() instanceof ItemPowerFist) {
//
//        }



        // FIXME: use this for updating cosmetic tag on exit of GUI
    }

    @Override
    public void update(double mousex, double mousey) {
        if (usingCosmeticPresets ||
                (!MPSConfig.INSTANCE.allowPowerFistCustomization() &&
                        itemSelector.getSelectedItem() != null && getSelectedItem().getItem().getItem() instanceof ItemPowerFist)) {

            //
            if (allowCosmeticPresetCreation) {
                cosmeticPresetCreator();
                // normal preset user
            } else
                cosmeticPresetsNormal();
        } else
            setLegacyMode();
    }

    NBTTagCompound getDefaultPreset(@Nonnull ItemStack itemStack) {
        return MPSConfig.INSTANCE.getPresetNBTFor(itemStack, "Default");
    }

    public boolean isValidItem(ClickableItem clickie, EntityEquipmentSlot slot) {
        if (clickie != null) {
            if (clickie.getItem().getItem() instanceof ItemPowerArmor)
                return clickie.getItem().getItem().isValidArmor(clickie.getItem(), slot, Minecraft.getMinecraft().player);
            else if (clickie.getItem().getItem() instanceof ItemPowerFist && slot.getSlotType().equals(EntityEquipmentSlot.Type.HAND))
                return true;
        }
        return false;
    }

    public ClickableItem getSelectedItem() {
        return this.itemSelector.getSelectedItem();
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        if (itemSelector.getSelectedItem() == null || itemSelector.getSelectedItem().getItem().isEmpty())
            return;

        if (usingCosmeticPresets ||
                (!MPSConfig.INSTANCE.allowPowerFistCustomization() &&
                        getSelectedItem() != null && getSelectedItem().getItem().getItem() instanceof ItemPowerFist)) {
            if (allowCosmeticPresetCreation) {
                if (isEditing) {
                    // todo: insert check for new item selected and save tag for previous item selected

                    if (itemSelector.getLastItemSlot() != -1 && itemSelector.selectedItemStack != itemSelector.getLastItemSlot()) {
                        System.out.println("previous item index: " + itemSelector.getSelectedItemSlot());
                        System.out.println("current item index: " + itemSelector.getSelectedItemSlot());

                        System.out.println("this is where we would save the cosmetic preset tag for the previous item:");

                    }


//                        ClickableItem lastItemClickie = itemSelector.getPreviousSelectedItem();
//                        if (lastItemClickie != null) {
//                            ItemStack lastItemStack = lastItemClickie.getItem();
//
//
//
////                        NBTTagCompound cosmeticTag = MPSNBTUtils.getMuseRenderTag()
////
////
////
////                                .getCosmeticTag()
////
////
////                        if ()
//
//
//
//
//
//                        }
//                    }







                    if (saveButton.hitBox(x, y)) {
                        // save as dialog is open
                        if (presetNameInputBox.getVisible()) {
                            // fixme: insert save code here... save and revert isEditing to false if save sucessful. also sync to server and change cosmetic tag to preset
                            System.out.println("save tag to file, sync to server, change tag to preset close creation dialog");

                            String name = presetNameInputBox.getText();
                            ItemStack itemStack = getSelectedItem().getItem();

                            boolean save = CosmeticPresetSaveLoad.savePreset(name, itemStack);
                            if (save) {
                                if (isValidItem(getSelectedItem(), getEquipmentSlot())) {
                                    // get the render tag for the item
                                    NBTTagCompound nbt = MPSNBTUtils.getMuseRenderTag(itemStack).copy();
                                    MPSPackets.sendToServer(new MusePacketCosmeticPresetUpdate(itemStack.getItem().getRegistryName(), name, nbt));

                                    // Fixeme: this should not update until GUI closed or until next item is selected
//                                    NBTTagCompound nbtUpdate = new NBTTagCompound();
//                                    nbtUpdate.setString(NuminaNBTConstants.TAG_COSMETIC_PRESET, name);
//                                    PacketSender.sendToServer(new MusePacketCosmeticInfo(Minecraft.getMinecraft().player, this.getSelectedItem().inventorySlot, NuminaNBTConstants.TAG_COSMETIC, nbtUpdate));
                                }


//                                MusePacketCosmeticPresetUpdate()



                                // sync



//                                closeSaveGUI();
                            }




                        } else {
                            // enabling here opens save dialog in update loop
                            textInputOn();
                        }

                        // reset tag to cosmetic copy of cosmetic preset default as legacy tag for editing.
                    } else if (resetButton.hitBox(x, y)) {
                        NBTTagCompound nbt = getDefaultPreset(itemSelector.getSelectedItem().getItem());
                        if (isValidItem(getSelectedItem(), getEquipmentSlot())) {
                            MPSPackets.sendToServer(new MusePacketCosmeticInfo(player, itemSelector.getSelectedItem().inventorySlot, NuminaNBTConstants.TAG_RENDER, nbt));
                        }
                        // cancel creation
                    } else if (loadButton.hitBox(x, y)) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setString(NuminaNBTConstants.TAG_COSMETIC_PRESET, "Default");
                        if (isValidItem(getSelectedItem(), getEquipmentSlot())) {
                            MPSPackets.sendToServer(new MusePacketCosmeticInfo(Minecraft.getMinecraft().player, this.getSelectedItem().inventorySlot, NuminaNBTConstants.TAG_COSMETIC, nbt));
                        }
                        isEditing = false;
                    }
                } else {
                    if (saveButton.hitBox(x, y)) {
                        isEditing = true;
                    } else if (resetButton.hitBox(x, y)) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setString(NuminaNBTConstants.TAG_COSMETIC_PRESET, "Default");
                        if (isValidItem(getSelectedItem(), getEquipmentSlot())) {
                            MPSPackets.sendToServer(new MusePacketCosmeticInfo(Minecraft.getMinecraft().player, this.getSelectedItem().inventorySlot, NuminaNBTConstants.TAG_COSMETIC, nbt));
                        }
                    }
                }
            } else {
                if (resetButton.hitBox(x, y)) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setString(NuminaNBTConstants.TAG_COSMETIC_PRESET, "Default");
                    if (isValidItem(getSelectedItem(), getEquipmentSlot())) {
                        MPSPackets.sendToServer(new MusePacketCosmeticInfo(Minecraft.getMinecraft().player, this.getSelectedItem().inventorySlot, NuminaNBTConstants.TAG_COSMETIC, nbt));
                    }
                }
            }
            // legacy mode
        } else {
            if (resetButton.hitBox(x, y)) {
                if (isValidItem(getSelectedItem(), getEquipmentSlot())) {
                    NBTTagCompound nbt = DefaultModelSpec.makeModelPrefs(itemSelector.getSelectedItem().getItem());
                    MPSPackets.sendToServer(new MusePacketCosmeticInfo(player, itemSelector.getSelectedItem().inventorySlot, NuminaNBTConstants.TAG_RENDER, nbt));
                }
            }
        }







//        if (usingCosmeticPresets && allowCosmeticPresetCreation) {
//            if (!isEditing) {
//                buttonOn(saveButton);
//                saveButton.setLable(I18n.format("gui.powersuits.new"));
//
//
//
//            } else {
//
//            }
//
//
//
//
//        }








//        if (loadButton.isVisible()) {
//            loadButton.hide();
//            loadButton.disable();
//            System.out.println("disable load button");
//        } else {
//            loadButton.show();
//            loadButton.enable();
//            System.out.println("enable load button");
//        }






//        // fixme: rewrite all of this
//
//
//        //
//        if (allowCosmeticPresetCreation) {
//
//
//
//
//
//
//
//
//
//            /**
//             * if colour picker frame enabled/visible
//             - hide colour picker frame
//             - resize this frame
//             - show "Save As" label
//             - show text input box
//             - change loadButton button text to cancel -> use this to exit
//             - change resetButton button text to clear -> use this to clear text input
//             - show preset subframe
//             */
//            if (saveButton.hitBox(x, y)) {
//                if (colourpicker.isEnabled()) {
//                    openSaveGUI();
//                    /**
//                     * if colour picker frame disabled/invisible
//                     - loadButton/cancel unchanged
//                     - check text input box for text
//                     - if saveButton successful then resetButton to previous state but do not clear text in case user wants to saveButton other armor piece customization under same name
//
//
//
//                     hide colour picker frame
//                     - resize this frame
//                     - show "Save As" label
//                     - show text input box
//                     - change loadButton button text to cancel
//                     - change resetButton button text to clear
//                     - show preset subframe
//                     */
//                } else if (!presetNameInputBox.getText().isEmpty()) {
//                    boolean save = CosmeticPresetSaveLoad.savePreset(presetNameInputBox.getText(), itemSelector.getSelectedItem().getItem());
//                    if (save) {
//                        closeSaveGUI();
//                    }
//                }
//            }
//
//
//            if (loadButton.hitBox(x, y)) {
//                // cancel
//                if (!colourpicker.isEnabled())
//                    closeSaveGUI();
//                // TODO: loading screen
//
//            }
//        } else {
//            saveButton.setEnabled(false);
//            saveButton.hide();
//            loadButton.setEnabled(false);
//            loadButton.hide();
//        }
//
//        if (!itemSelector.getSelectedItem().getItem().isEmpty()) {
//
//
//            if (resetButton.hitBox(x, y)) {
//                // reset cosmetic tag to defaults
//                if (colourpicker.isEnabled()) {
//                    NBTTagCompound nbt = DefaultModelSpec.makeModelPrefs(itemSelector.getSelectedItem().getItem());
//                    PacketSender.sendToServer(new MusePacketCosmeticInfo(player, itemSelector.getSelectedItem().inventorySlot, NuminaNBTConstants.TAG_RENDER, nbt));
//                } else {
//                    // reset text in text input box
//                    presetNameInputBox.setText("");
//                }
//            }
//        }
    }



    void openSaveGUI () {
        boolean boolVal = true;
        presetNameInputBox.setEnabled(boolVal);
        presetNameInputBox.setVisible(boolVal);
        presetNameInputBox.setFocused(boolVal);
        colourpicker.disable();
        this.border.setTop(colourpicker.getBorder().top()).setHeight(newHeight);
        saveAsLabel.setEnabled(boolVal);
        loadButton.setLable(I18n.format("gui.powersuits.cancel"));

        showPartManipFrame();


        // fixme: hide cosmeitc preset selection subframe
        // fixme: show cosmetic manip subframe
    }

    void showPresetFrame() {
        cosmeticFrame.frameOn();
        partframe.frameOff();
    }

    void showPartManipFrame() {
        cosmeticFrame.frameOff();
        partframe.frameOn();
    }


    @Override
    public void onMouseUp(double x, double y, int button) {

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