package net.machinemuse.general.gui.frame;

import net.machinemuse.api.item.IModularItemBase;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.numina.client.render.RenderState;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.MuseRect;
import net.machinemuse.numina.geometry.MuseRelativeRect;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.machinemuse.powersuits.client.modelspec.*;
import net.machinemuse.powersuits.common.items.old.ItemPowerArmor;
import net.machinemuse.powersuits.network.MusePacketCosmeticInfo;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.render.GuiIcons;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:46 AM, 30/04/13
 *
 * Ported to Java by lehjr on 11/2/16.
 */
@SideOnly(Side.CLIENT)
public class PartManipSubFrame {
    public Spec model;
    public ColourPickerFrame colourframe;
    public ItemSelectionFrame itemSelector;
    public MuseRelativeRect border;
    public List<PartSpec> specs;
    public boolean open;

    public PartManipSubFrame(Spec model, ColourPickerFrame colourframe, ItemSelectionFrame itemSelector, MuseRelativeRect border) {
        this.model = model;
        this.colourframe = colourframe;
        this.itemSelector = itemSelector;
        this.border = border;
        // this is actually 3 to 8 times SLOWER than the loop in getSpecs()
//        this.specs = model.apply().values().stream().filter(spec-> isValidArmor(getSelectedItem(), spec.binding.getSlot())).collect(Collectors.toList());
        this.specs = this.getSpecs();
        this.open = true;
    }

    private List<PartSpec> getSpecs() {
        List<PartSpec> specsArray = new ArrayList<>();
        Iterator<PartSpec> specIt = model.apply().values().iterator();
        PartSpec spec;
        while (specIt.hasNext()) {
            spec = specIt.next();
            if (isValidArmor(getSelectedItem(), spec.binding.getSlot()))
                specsArray.add(spec);
        }
        return specsArray;
    }

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

    public ClickableItem getSelectedItem() {
        return this.itemSelector.getSelectedItem();
    }

    public NBTTagCompound getRenderTag() {
        return MuseItemUtils.getMuseRenderTag(this.getSelectedItem().getItem(), this.getEquipmentSlot());
    }

    public NBTTagCompound getItemTag() {
        return MuseItemUtils.getMuseItemTag(this.getSelectedItem().getItem());
    }

    public boolean isValidArmor(ClickableItem clickie, EntityEquipmentSlot slot) {
        return clickie != null && clickie.getItem().getItem().isValidArmor(clickie.getItem(), slot, Minecraft.getMinecraft().player);
    }

    public NBTTagCompound getSpecTag(PartSpec spec) {
        return this.getRenderTag().getCompoundTag(ModelRegistry.getInstance().makeName(spec));
    }

    public NBTTagCompound getOrDontGetSpecTag(PartSpec spec) {
        String name = ModelRegistry.getInstance().makeName(spec);
        return this.getRenderTag().hasKey(name) ? this.getRenderTag().getCompoundTag(name) : null;
    }

    public NBTTagCompound getOrMakeSpecTag(PartSpec spec) {
        String name = ModelRegistry.getInstance().makeName(spec);
        NBTTagCompound compoundTag;
        if (this.getRenderTag().hasKey(name)) {
            compoundTag = this.getRenderTag().getCompoundTag(name);
        }
        else {
            NBTTagCompound k = new NBTTagCompound();
            if (spec instanceof ModelPartSpec)
                ((ModelPartSpec) spec).multiSet(k, null, null);
            else if (spec instanceof TexturePartSpec)
                ((TexturePartSpec) spec).multiSet(k, null, null);
                // Fixme: What would this be?
            else
                spec.multiSet(k, null);
            this.getRenderTag().setTag(name, k);
            compoundTag = k;
        }
        return compoundTag;
    }

    public void updateItems() {
        this.specs = model.apply().values().stream().filter(spec-> isValidArmor(getSelectedItem(), spec.binding.getSlot())).collect(Collectors.toList());
        this.border.setHeight((specs.size() > 0) ? (specs.size() * 8 + 10) : 0);
    }

    public void drawPartial(double min, double max) {
        if (specs.size() > 0) {
            MuseRenderer.drawString(model.getDisaplayName(), border.left() + 8, border.top());
            drawOpenArrow(min, max);
            if (open) {
                int y = (int) (border.top() + 8);
                for (PartSpec spec : specs) {
                    drawSpecPartial(border.left(), y, spec, min, max);
                    y+=8;
                }
            }
        }
    }

    public void decrAbove(int index) {
        for (PartSpec spec : specs) {
            String tagname = ModelRegistry.getInstance().makeName(spec);
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            NBTTagCompound tagdata = getOrDontGetSpecTag(spec);

            if (tagdata != null) {
                int oldindex = spec.getColourIndex(tagdata);
                if (oldindex >= index && oldindex > 0) {
                    spec.setColourIndex(tagdata, oldindex - 1);
                    if (player.world.isRemote)
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
                }
            }
        }
    }

    public void drawSpecPartial(double x, double y, PartSpec spec, double ymino, double ymaxo) {
        NBTTagCompound tag = this.getSpecTag(spec);

        // FIXME: texture spec can have colour but not glow
        // FIXME: texture spec also needs a way to toggle between texture specs (turn one on turn the other off)
        // FIXME: active texture spec should set itemstack color


        int selcomp = tag.hasNoTags() ? 0 : (spec instanceof ModelPartSpec && ((ModelPartSpec) spec).getGlow(tag) ? 2 : 1);
        int selcolour = spec.getColourIndex(tag);
        new GuiIcons.TransparentArmor(x, y, null, null, ymino, null, ymaxo);
        new GuiIcons.NormalArmor(x + 8, y, null, null, ymino, null, ymaxo);

        if (spec instanceof ModelPartSpec)
            new GuiIcons.GlowArmor(x + 16, y, null, null, ymino, null, ymaxo);

        new GuiIcons.SelectedArmorOverlay(x + selcomp * 8, y, null, null, ymino, null, ymaxo);


        double acc = (x + 28);
        for (byte colour : colourframe.colours()) {
            new GuiIcons.ArmourColourPatch(acc, y, EnumColour.getColourEnumFromIndex(Byte.toUnsignedInt(colour)).getColour(), null, ymino, null, ymaxo);
            acc += 8;
        }

        double textstartx = acc;

        if (selcomp > 0) {
            new GuiIcons.SelectedArmorOverlay(x + 28 + selcolour * 8, y, null, null, ymino, null, ymaxo);
        }
        MuseRenderer.drawString(spec.getDisaplayName(), textstartx + 4, y);
    }

    public void drawOpenArrow(double min, double max) {
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

    public boolean tryMouseClick(double x, double y) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        NBTTagCompound tagdata;
        String tagname;

        if (x < this.border.left() || x > this.border.right() || y < this.border.top() || y > this.border.bottom()) {
            return false;
        }
        else if (x > this.border.left() + 2 && x < this.border.left() + 8 && y > this.border.top() + 2 && y < this.border.top() + 8) {
            this.open = !this.open;
            this.getBorder();
            return true;
        }
        else if (x < this.border.left() + 24 && y > this.border.top() + 8) {
            int lineNumber = (int)((y - this.border.top() - 8) / 8);
            int columnNumber = (int)((x - this.border.left()) / 8);
            PartSpec spec = specs.get(Math.max(Math.min(lineNumber, specs.size() - 1), 0));
            MuseLogger.logDebug("Line " + lineNumber + " Column " + columnNumber);

            switch (columnNumber) {
                case 0: {
                    NBTTagCompound renderTag = this.getRenderTag();
                    tagname = ModelRegistry.getInstance().makeName(spec);
                    if (player.world.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, new NBTTagCompound()).getPacket131());
                    }
                    this.updateItems();
                    return true;
                }

                case 1: {
                    tagname = ModelRegistry.getInstance().makeName(spec);
                    tagdata = this.getOrMakeSpecTag(spec);
                    if (spec instanceof ModelPartSpec)
                        ((ModelPartSpec) spec).setGlow(tagdata, false);
                    if (player.world.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
                    }
                    this.updateItems();
                    return true;
                }

                case 2: {
                    tagname = ModelRegistry.getInstance().makeName(spec);
                    tagdata = this.getOrMakeSpecTag(spec);
                    if (spec instanceof ModelPartSpec)
                        ((ModelPartSpec) spec).setGlow(tagdata, true);
                    if (player.world.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
                    }
                    this.updateItems();
                    return true;
                }

                default:
                    return false;
            }
        }
        else if (x > this.border.left() + 28 && x < this.border.left() + 28 + this.colourframe.colours().length * 8) {
            int lineNumber = (int)((y - this.border.top() - 8) / 8);
            int columnNumber = (int)((x - this.border.left() - 28) / 8);
            PartSpec spec = specs.get(Math.max(Math.min(lineNumber, specs.size() - 1), 0));
            tagname = ModelRegistry.getInstance().makeName(spec);
            tagdata = this.getOrMakeSpecTag(spec);
            spec.setColourIndex(tagdata, columnNumber);

            if (player.world.isRemote) {
                PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
            }
            return true;
        }
        return false;
    }
}
