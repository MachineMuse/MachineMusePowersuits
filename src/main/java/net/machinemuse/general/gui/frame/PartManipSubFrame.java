package net.machinemuse.general.gui.frame;

import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.MuseRect;
import net.machinemuse.numina.geometry.MuseRelativeRect;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.render.RenderState;
import net.machinemuse.powersuits.client.render.modelspec.ModelPartSpec;
import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpec;
import net.machinemuse.powersuits.network.packets.MusePacketCosmeticInfo;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.render.GuiIcons;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:46 AM, 30/04/13
 *
 * Ported to Java by lehjr on 11/2/16.
 */

public class PartManipSubFrame {
    public ModelSpec model;
    public ColourPickerFrame colourframe;
    public ItemSelectionFrame itemSelector;
    public final MuseRelativeRect border;
    public List<ModelPartSpec> specs;
    public boolean open;

    public PartManipSubFrame(ModelSpec model, ColourPickerFrame colourframe, ItemSelectionFrame itemSelector, MuseRelativeRect border) {
        this.model = model;
        this.colourframe = colourframe;
        this.itemSelector = itemSelector;
        this.border = border;

        /* TODO: cleanup and simplify in 1.10.2
         * Scala method of filtering the list and building a new array based on boolean values.

        var specs: Array[ModelPartSpec] = model.apply.values.filter(spec => isValidArmor(getSelectedItem, spec.slot)).toArray
        model.apply().values().filter(Function1<ModelPartSpec, Object> p) */
        this.specs = getSpecs();
        this.open = true;
    }

    private List<ModelPartSpec> getSpecs() {
        List<ModelPartSpec> specsArray = new ArrayList<>();
        Iterator<ModelPartSpec> specIt = model.apply().values().iterator();
        ModelPartSpec spec;
        while (specIt.hasNext()) {
            spec = specIt.next();
            if (isValidArmor(getSelectedItem(), spec.slot))
                specsArray.add(spec);
        }
        return specsArray;
    }

    public int getArmorSlot() {
        return ((ItemArmor)this.getSelectedItem().getItem().getItem()).armorType;
    }

    public ClickableItem getSelectedItem() {
        return this.itemSelector.getSelectedItem();
    }

    public NBTTagCompound getRenderTag() {
        return MuseItemUtils.getMuseRenderTag(this.getSelectedItem().getItem(), this.getArmorSlot());
    }

    public NBTTagCompound getItemTag() {
        return MuseItemUtils.getMuseItemTag(this.getSelectedItem().getItem());
    }

    public boolean isValidArmor(final ClickableItem clickie, final int slot) {
        return clickie != null && clickie.getItem().getItem().isValidArmor(clickie.getItem(), slot, (Entity)Minecraft.getMinecraft().thePlayer);
    }

    public NBTTagCompound getSpecTag(final ModelPartSpec spec) {
        return this.getRenderTag().getCompoundTag(ModelRegistry.getInstance().makeName(spec));
    }

    public NBTTagCompound getOrDontGetSpecTag(final ModelPartSpec spec) {
        final String name = ModelRegistry.getInstance().makeName(spec);
        return this.getRenderTag().hasKey(name) ? this.getRenderTag().getCompoundTag(name) : null;
    }

    public NBTTagCompound getOrMakeSpecTag(final ModelPartSpec spec) {
        final String name = ModelRegistry.getInstance().makeName(spec);
        NBTTagCompound compoundTag;
        if (this.getRenderTag().hasKey(name)) {
            compoundTag = this.getRenderTag().getCompoundTag(name);
        }
        else {
            final NBTTagCompound k = new NBTTagCompound();
            spec.multiSet(k, null, null, null);
            this.getRenderTag().setTag(name, (NBTBase)k);
            compoundTag = k;
        }
        return compoundTag;
    }

    public void updateItems() {
        this.specs = getSpecs();
        this.border.setHeight((specs.size() > 0) ? (specs.size() * 8 + 10) : 0);
    }

    public void drawPartial(final double min, final double max) {
        if (specs.size() > 0) {
            MuseRenderer.drawString(ModelRegistry.getInstance().getName(model), border.left() + 8, border.top());
            drawOpenArrow(min, max);
            if (open) {
                int y = (int) (border.top() + 8);
                for (ModelPartSpec spec : specs) {
                    drawSpecPartial(border.left(), y, spec, min, max);
                    y+=8;
                }
            }
        }
    }

    public void decrAbove(int index) {
        for (ModelPartSpec spec : specs) {
            String tagname = ModelRegistry.getInstance().makeName(spec);
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            NBTTagCompound tagdata = getOrDontGetSpecTag(spec);

            if (tagdata != null) {
                int oldindex = spec.getColourIndex(tagdata);
                if (oldindex >= index && oldindex > 0) {
                    spec.setColourIndex(tagdata, oldindex - 1);
                    if (player.worldObj.isRemote)
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
                }
            }
        }
    }

    public void drawSpecPartial(final double x, final double y, final ModelPartSpec spec, final double ymino, final double ymaxo) {
        final NBTTagCompound tag = this.getSpecTag(spec);
        final int selcomp = tag.hasNoTags() ? 0 : (spec.getGlow(tag) ? 2 : 1);
        final int selcolour = spec.getColourIndex(tag);
        new GuiIcons.TransparentArmor(x, y, null, null, ymino, null, ymaxo);
        new GuiIcons.NormalArmor(x + 8, y, null, null, ymino, null, ymaxo);
        new GuiIcons.GlowArmor(x + 16, y, null, null, ymino, null, ymaxo);
        new GuiIcons.SelectedArmorOverlay(x + selcomp * 8, y, null, null, ymino, null, ymaxo);

        double acc = (x + 28);
        for (int colour: colourframe.colours()) {
            new GuiIcons.ArmourColourPatch(acc, y, new Colour(colour), null, ymino, null, ymaxo);
            acc += 8;
        }
        double textstartx = acc;

        if (selcomp > 0) {
            new GuiIcons.SelectedArmorOverlay(x + 28 + selcolour * 8, y, null, null, ymino, null, ymaxo);
        }
        MuseRenderer.drawString(spec.displayName, textstartx + 4, y);
    }

    public void drawOpenArrow(final double min, final double max) {
        RenderState.texturelessOn();
        Colour.LIGHTBLUE.doGL();
        GL11.glBegin(4);
        if (this.open) {
            GL11.glVertex2d(this.border.left() + 3, MuseMathUtils.clampDouble(this.border.top() + 3, min, max));
            GL11.glVertex2d(this.border.left() + 5, MuseMathUtils.clampDouble(this.border.top() + 7, min, max));
            GL11.glVertex2d(this.border.left() + 7, MuseMathUtils.clampDouble(this.border.top() + 3, min, max));
        }
        else {
            GL11.glVertex2d(this.border.left() + 3, MuseMathUtils.clampDouble(this.border.top() + 3, min, max));
            GL11.glVertex2d(this.border.left() + 3, MuseMathUtils.clampDouble(this.border.top() + 7, min, max));
            GL11.glVertex2d(this.border.left() + 7, MuseMathUtils.clampDouble(this.border.top() + 5, min, max));
        }
        GL11.glEnd();
        Colour.WHITE.doGL();
        RenderState.texturelessOff();
    }

    public MuseRect getBorder() {
        if (this.open) {
            border.setHeight(9 + 9 * specs.size());
        }
        else {
            this.border.setHeight(9.0);
        }
        return this.border;
    }

    public boolean tryMouseClick(final double x, final double y) {
        if (x < this.border.left() || x > this.border.right() || y < this.border.top() || y > this.border.bottom()) {
            return false;
        }
        else if (x > this.border.left() + 2 && x < this.border.left() + 8 && y > this.border.top() + 2 && y < this.border.top() + 8) {
            this.open = !this.open;
            this.getBorder();
            return true;
        }
        else if (x < this.border.left() + 24 && y > this.border.top() + 8) {
            final int lineNumber = (int)((y - this.border.top() - 8) / 8);
            final int columnNumber = (int)((x - this.border.left()) / 8);
            final ModelPartSpec spec = specs.get(Math.max(Math.min(lineNumber, specs.size() - 1), 0));
            MuseLogger.logDebug("Line " + lineNumber + " Column " + columnNumber);
            switch (columnNumber) {
                case 0: {
                    final NBTTagCompound renderTag = this.getRenderTag();
                    final String tagname3 = ModelRegistry.getInstance().makeName(spec);
                    final EntityClientPlayerMP player3 = Minecraft.getMinecraft().thePlayer;
                    renderTag.removeTag(ModelRegistry.getInstance().makeName(spec));
                    if (player3.worldObj.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo((EntityPlayer) player3, this.getSelectedItem().inventorySlot, tagname3, new NBTTagCompound()).getPacket131());
                    }
                    this.updateItems();
                    return true;
                }

                case 1: {
                    final String tagname2 = ModelRegistry.getInstance().makeName(spec);
                    final EntityClientPlayerMP player2 = Minecraft.getMinecraft().thePlayer;
                    final NBTTagCompound tagdata2 = this.getOrMakeSpecTag(spec);
                    spec.setGlow(tagdata2, false);
                    if (player2.worldObj.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo((EntityPlayer)player2, this.getSelectedItem().inventorySlot, tagname2, tagdata2).getPacket131());
                    }
                    this.updateItems();
                    return true;
                }

                case 2: {
                    final String tagname = ModelRegistry.getInstance().makeName(spec);
                    final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
                    final NBTTagCompound tagdata = this.getOrMakeSpecTag(spec);
                    spec.setGlow(tagdata, true);
                    if (player.worldObj.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo((EntityPlayer) player, this.getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
                    }
                    this.updateItems();
                    return true;
                }

                default:
                    return false;
            }
        }
        else if (x > this.border.left() + 28 && x < this.border.left() + 28 + this.colourframe.colours().length * 8) {
            final int lineNumber2 = (int)((y - this.border.top() - 8) / 8);
            final int columnNumber2 = (int)((x - this.border.left() - 28) / 8);
            final ModelPartSpec spec2 = specs.get(Math.max(Math.min(lineNumber2, specs.size() - 1), 0));
            final String tagname4 = ModelRegistry.getInstance().makeName(spec2);
            final EntityClientPlayerMP player4 = Minecraft.getMinecraft().thePlayer;
            final NBTTagCompound tagdata3 = this.getOrMakeSpecTag(spec2);
            spec2.setColourIndex(tagdata3, columnNumber2);
            if (player4.worldObj.isRemote) {
                PacketSender.sendToServer(new MusePacketCosmeticInfo((EntityPlayer)player4, this.getSelectedItem().inventorySlot, tagname4, tagdata3).getPacket131());
            }
            return true;
        }
        return false;
    }
}
