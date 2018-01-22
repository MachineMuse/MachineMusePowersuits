package net.machinemuse.general.gui.frame;


import net.machinemuse.api.item.IModularItemBase;
import net.machinemuse.general.gui.clickable.ClickableSlider;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.DrawableMuseRect;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.numina.geometry.MuseRect;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.machinemuse.powersuits.network.MusePacketColourInfo;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.render.GuiIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:19 AM, 03/05/13
 *
 * Ported to Java by lehjr on 11/2/16.
 */
@SideOnly(Side.CLIENT)
public class ColourPickerFrame implements IGuiFrame {
    public ItemSelectionFrame itemSelector;
    public DrawableMuseRect border;
    public ClickableSlider rslider;
    public ClickableSlider gslider;
    public ClickableSlider bslider;
    public ClickableSlider selectedSlider;
    public int selectedColour;
    public int decrAbove;

    public ColourPickerFrame(MuseRect borderRef, Colour insideColour, Colour borderColour, ItemSelectionFrame itemSelector) {
        this.itemSelector = itemSelector;
        this.border = new DrawableMuseRect(borderRef, insideColour, borderColour);

        this.rslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + 8), this.border.width() - 10, I18n.format("gui.red"));
        this.gslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + 24), this.border.width() - 10, I18n.format("gui.green"));
        this.bslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + 40), this.border.width() - 10, I18n.format("gui.blue"));

        this.selectedSlider = null;
        this.selectedColour = 0;
        this.decrAbove = -1;
    }

    /**
     * gets the byte array of EnumColour index values
     */
    public byte[] colours() {
        return (getOrCreateColourTag() != null) ? getOrCreateColourTag().getByteArray() : new byte[0];
    }

    public NBTTagByteArray getOrCreateColourTag() {
        if (this.itemSelector.getSelectedItem() == null)
            return null;

        NBTTagCompound renderSpec = MuseItemUtils.getMuseRenderTag(this.itemSelector.getSelectedItem().getItem());

        if (renderSpec.hasKey("colours") && renderSpec.getTag("colours") instanceof NBTTagByteArray) {
            return (NBTTagByteArray) renderSpec.getTag("colours");
        } else {
            Item item = this.itemSelector.getSelectedItem().getItem().getItem();
            byte[] byteArray;
            if (item instanceof IModularItemBase) { // armor, powerfist, w/e
                IModularItemBase modularItemBase = (IModularItemBase)item;
                byteArray = new byte[]{
                        (byte)modularItemBase.getColorFromItemStack(this.itemSelector.getSelectedItem().getItem()).getIndex()};
            } else {
                byteArray = new byte[0];
                }
            renderSpec.setByteArray("colours", byteArray);
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player.world.isRemote) {
                PacketSender.sendToServer(new MusePacketColourInfo(player, this.itemSelector.getSelectedItem().inventorySlot, this.colours()));
            }
            return (NBTTagByteArray) renderSpec.getTag("colours");
        }
    }

    public NBTTagByteArray setColourTagMaybe(byte[] newarray) {
        if (this.itemSelector.getSelectedItem() == null) {
            return null;
        }
        NBTTagCompound renderSpec = MuseItemUtils.getMuseRenderTag(this.itemSelector.getSelectedItem().getItem());
        renderSpec.setTag("colours", new NBTTagByteArray(newarray));
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player.world.isRemote) {
            PacketSender.sendToServer(new MusePacketColourInfo(player, this.itemSelector.getSelectedItem().inventorySlot, this.colours()));
        }
        return (NBTTagByteArray) renderSpec.getTag("colours");
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
        this.selectedSlider = null;
    }


    @Override
    public void update(double mousex, double mousey) {
        if (this.selectedSlider != null) {
            this.selectedSlider.setValueByX(mousex);
            if (colours().length > selectedColour) {
                colours()[selectedColour] = (byte)
                        EnumColour.findClosestEnumColour(
                                new Colour(rslider.value(), gslider.value(), bslider.value(), 1.0)).getIndex();
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                if (player.world.isRemote)
                    PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem().inventorySlot, colours()));
            }
        }
    }

    @Override
    public void draw() {
        this.border.draw();
        this.rslider.draw();
        this.gslider.draw();
        this.bslider.draw();
        for (int i = 0; i <  colours().length; i++) {
            new GuiIcons.ArmourColourPatch(border.left() + 8 + i * 8, border.bottom() - 16,
                    EnumColour.getColourEnumFromIndex(Byte.toUnsignedInt(colours()[i])).getColour(), null, null, null, null);
        }
        new GuiIcons.ArmourColourPatch(this.border.left() + 8 + this.colours().length * 8, this.border.bottom() - 16, Colour.WHITE, null, null, null, null);
        new GuiIcons.SelectedArmorOverlay(this.border.left() + 8 + this.selectedColour * 8, this.border.bottom() - 16, Colour.WHITE, null, null, null, null);
        new GuiIcons.MinusSign(this.border.left() + 8 + this.selectedColour * 8, this.border.bottom() - 24, Colour.RED, null, null, null, null);
        new GuiIcons.PlusSign(this.border.left() + 8 + this.colours().length * 8, this.border.bottom() - 16, Colour.GREEN, null, null, null, null);
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
    }

    public void onSelectColour(int i) {
        Colour c = new Colour(this.colours()[i]);
        this.rslider.setValue(c.r);
        this.gslider.setValue(c.g);
        this.bslider.setValue(c.b);
        this.selectedColour = i;
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        if (this.rslider.hitBox(x, y))
            this.selectedSlider = this.rslider;
        else if (this.gslider.hitBox(x, y))
            this.selectedSlider = this.gslider;
        else if (this.bslider.hitBox(x, y))
            this.selectedSlider = this.bslider;
        else
            this.selectedSlider = null;

        // add
        if (y > this.border.bottom() - 16 && y < this.border.bottom() - 8) {
            int colourCol = (int)(x - this.border.left() - 8.0) / 8;
            if (colourCol >= 0 && colourCol < colours().length) {
                this.onSelectColour(colourCol);
            } else if (colourCol == this.colours().length) {
                MuseLogger.logDebug("Adding");
                setColourTagMaybe(ArrayUtils.add(getByteArray(getOrCreateColourTag()), (byte)EnumColour.WHITE.getIndex()));
            }
        }

        // remove
        if (y > border.bottom() - 24 && y < border.bottom() - 16 && x > border.left() + 8 + selectedColour * 8 && x < border.left() + 16 + selectedColour * 8) {
            NBTTagByteArray nbtTagByteArray = getOrCreateColourTag();

            byte[] byteArray = getByteArray(nbtTagByteArray);
            if (byteArray.length > 1) {
                byte[] newByteArray = ArrayUtils.remove(byteArray, selectedColour);
                setColourTagMaybe(newByteArray);

                decrAbove = selectedColour;
                if (selectedColour == getByteArray(nbtTagByteArray).length) {
                    selectedColour = selectedColour -1;
                }

                EntityPlayerSP player = Minecraft.getMinecraft().player;
                if (player.world.isRemote)
                    PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem().inventorySlot, nbtTagByteArray.getByteArray()));
            }
        }
    }

    /**
     * This returns a byte array which represent index values corresponding to an EnumColour (convert back to unsigned int before getting value)
     * If the NBT Array is null, an empty byte array is returned
     */
    public byte[] getByteArray(NBTTagByteArray e) {
        if (e == null) // null when no armor item selected
            return new byte[0];
        return e.getByteArray();
    }
}