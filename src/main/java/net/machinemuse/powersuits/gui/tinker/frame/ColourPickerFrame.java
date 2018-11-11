package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.DrawableMuseRect;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.math.geometry.MuseRect;
import net.machinemuse.powersuits.gui.GuiIcons;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableSlider;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.machinemuse.powersuits.network.packets.MusePacketColourInfo;
import net.machinemuse.powersuits.utils.nbt.MPSNBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:19 AM, 03/05/13
 * <p>
 * Ported to Java by lehjr on 11/2/16.
 */
public class ColourPickerFrame implements IGuiFrame {
    public ItemSelectionFrame itemSelector;
    public DrawableMuseRect border;
    public ClickableSlider rslider;
    public ClickableSlider gslider;
    public ClickableSlider bslider;
    public ClickableSlider aslider;

    public ClickableSlider selectedSlider;
    public int selectedColour;
    public int decrAbove;

    public ColourPickerFrame(MuseRect borderRef, Colour insideColour, Colour borderColour, ItemSelectionFrame itemSelector) {
        this.itemSelector = itemSelector;
        this.border = new DrawableMuseRect(borderRef, insideColour, borderColour);
        this.rslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + 4), this.border.width() - 10, "red");
        this.gslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + 20), this.border.width() - 10, "green");
        this.bslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + 36), this.border.width() - 10, "blue");
        this.aslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + 52), this.border.width() - 10, "alpha");
        this.selectedSlider = null;
        this.selectedColour = 0;
        this.decrAbove = -1;
    }

    public int[] colours() {
        return (getOrCreateColourTag() != null) ? getOrCreateColourTag().getIntArray() : new int[0];
    }

    public NBTTagIntArray getOrCreateColourTag() {
        if (this.itemSelector.getSelectedItem() == null) {
            return null;
        }

        NBTTagCompound renderSpec = MPSNBTUtils.getMuseRenderTag(this.itemSelector.getSelectedItem().getItem());
        if (renderSpec.hasKey(NuminaNBTConstants.TAG_COLOURS) && renderSpec.getTag(NuminaNBTConstants.TAG_COLOURS) instanceof NBTTagIntArray) {
            return (NBTTagIntArray) renderSpec.getTag(NuminaNBTConstants.TAG_COLOURS);
        } else {
            Item item = this.itemSelector.getSelectedItem().getItem().getItem();
            if (item instanceof ItemPowerArmor) {
                ItemPowerArmor itemPowerArmor = (ItemPowerArmor) item;
                int[] intArray = {itemPowerArmor.getColorFromItemStack(this.itemSelector.getSelectedItem().getItem()).getInt()};
                renderSpec.setIntArray(NuminaNBTConstants.TAG_COLOURS, intArray);
            } else {
                int[] intArray2 = new int[]{Colour.WHITE.getInt()};
                renderSpec.setIntArray(NuminaNBTConstants.TAG_COLOURS, intArray2);
            }
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player.world.isRemote) {
                PacketSender.sendToServer(new MusePacketColourInfo((EntityPlayer) player, this.itemSelector.getSelectedItem().inventorySlot, this.colours()));
            }
            return (NBTTagIntArray) renderSpec.getTag(NuminaNBTConstants.TAG_COLOURS);
        }
    }

    public NBTTagIntArray setColourTagMaybe(List<Integer> intList) {
        if (this.itemSelector.getSelectedItem() == null) {
            return null;
        }
        NBTTagCompound renderSpec = MPSNBTUtils.getMuseRenderTag(this.itemSelector.getSelectedItem().getItem());
        renderSpec.setTag(NuminaNBTConstants.TAG_COLOURS, new NBTTagIntArray(intList));
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player.world.isRemote) {
            PacketSender.sendToServer(new MusePacketColourInfo((EntityPlayer) player, this.itemSelector.getSelectedItem().inventorySlot, this.colours()));
        }
        return (NBTTagIntArray) renderSpec.getTag(NuminaNBTConstants.TAG_COLOURS);
    }

    public ArrayList<Integer> importColours() {
        return new ArrayList<Integer>();
    }

    public void refreshColours() {
        //    getOrCreateColourTag.map(coloursTag => {
        //      val colourints: Array[Int] = coloursTag.intArray
        //      val colourset: HashSet[Int] = HashSet.empty ++ colours ++ colourints
        //      val colourarray = colourset.toArray
        //      coloursTag.intArray = colourarray
        //    })
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
                colours()[selectedColour] = Colour.getInt(rslider.getValue(), gslider.getValue(), bslider.getValue(), aslider.getValue());

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
        this.aslider.draw();

        for (int i = 0; i < colours().length; i++) {
            new GuiIcons.ArmourColourPatch(border.left() + 8 + i * 8, border.bottom() - 16, new Colour(colours()[i]), null, null, null, null);
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
        this.aslider.setValue(c.a);
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
        else if (this.aslider.hitBox(x, y))
            this.selectedSlider = this.aslider;
        else
            this.selectedSlider = null;

        // add color
        if (y > this.border.bottom() - 16 && y < this.border.bottom() - 8) {
            int colourCol = (int) (x - this.border.left() - 8.0) / 8;
            if (colourCol >= 0 && colourCol < colours().length) {
                this.onSelectColour(colourCol);
            } else if (colourCol == this.colours().length) {
                MuseLogger.logDebug("Adding");

                List<Integer> intList = Arrays.stream(getIntArray(getOrCreateColourTag())).boxed().collect(Collectors.toList());
                intList.add(Colour.WHITE.getInt());
                setColourTagMaybe(intList);
            }
        }

        // remove color
        if (y > border.bottom() - 24 && y < border.bottom() - 16 && x > border.left() + 8 + selectedColour * 8 && x < border.left() + 16 + selectedColour * 8) {
            NBTTagIntArray nbtTagIntArray = getOrCreateColourTag();
            List<Integer> intList = Arrays.stream(getIntArray(nbtTagIntArray)).boxed().collect(Collectors.toList());

            if (intList.size() > 1) {
                intList.remove(selectedColour); // with integer list, will default to index rather than getValue

                setColourTagMaybe(intList);

                decrAbove = selectedColour;
                if (selectedColour == getIntArray(nbtTagIntArray).length) {
                    selectedColour = selectedColour - 1;
                }

                EntityPlayerSP player = Minecraft.getMinecraft().player;
                if (player.world.isRemote)
                    PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem().inventorySlot, nbtTagIntArray.getIntArray()));
            }
        }
    }

    public int[] getIntArray(NBTTagIntArray e) {
        if (e == null) // null when no armor item selected
            return new int[0];
        return e.getIntArray();
    }
}