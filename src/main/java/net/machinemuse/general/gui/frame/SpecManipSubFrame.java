package net.machinemuse.general.gui.frame;

import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.geometry.MuseRect;
import net.machinemuse.numina.geometry.MuseRelativeRect;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.utils.NBTTagListUtils;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.machinemuse.powersuits.client.modelspec.ModelSpec;
import net.machinemuse.powersuits.client.modelspec.PartSpec;
import net.machinemuse.powersuits.client.modelspec.Spec;
import net.machinemuse.powersuits.client.modelspec.TextureSpec;
import net.machinemuse.powersuits.common.items.old.ItemPowerArmor;
import net.machinemuse.powersuits.common.items.old.ItemPowerFist;
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
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;

import static net.machinemuse.powersuits.common.MPSConstants.NBT_SPECLIST_TAG;
import static net.machinemuse.powersuits.common.MPSConstants.NBT_TEXTURESPEC_TAG;

/**
 * TextureSpec/ModelSpec version of PartSpecManipSubFrame
 */
@SideOnly(Side.CLIENT)
public class SpecManipSubFrame {
    public Spec model;
    public ColourPickerFrame colourframe;
    public ItemSelectionFrame itemSelector;
    public MuseRelativeRect border;
    final String tagname;

    public SpecManipSubFrame(Spec model, ColourPickerFrame colourframe, ItemSelectionFrame itemSelector, MuseRelativeRect border) {
        this.model = model;
        this.tagname = model instanceof TextureSpec ? NBT_TEXTURESPEC_TAG : NBT_SPECLIST_TAG;
        this.colourframe = colourframe;
        this.itemSelector = itemSelector;
        this.border = border;
    }

    /**
     * checks all the parts of the model to see if any of them are valid for the current item
     */
    private boolean isSpecValid() {
        Iterator<PartSpec> specIt = model.apply().values().iterator();
        PartSpec spec;
        while (specIt.hasNext()) {
            spec = specIt.next();
            // some models span multiple slots so only looking for any valid part for that slot
            if (isValidItem(getSelectedItem(), spec.getBinding().getSlot()))
                return true;
        }
        return false;
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

    /**
     * Get your render tags here while they're hot. Sorry, no discounts.
     */
    public NBTTagCompound getRenderTag() {
        return MuseItemUtils.getMuseRenderTag(this.getSelectedItem().getItem(), this.getEquipmentSlot());
    }

    /**
     * sorry, no help given for this one, figure it out.
     */
    public boolean isValidItem(ClickableItem clickie, EntityEquipmentSlot slot) {
        if (clickie != null) {
            if (clickie.getItem().getItem() instanceof ItemPowerArmor)
                return clickie.getItem().getItem().isValidArmor(clickie.getItem(), slot, Minecraft.getMinecraft().player);
            else if (clickie.getItem().getItem() instanceof ItemPowerFist && slot.getSlotType().equals(EntityEquipmentSlot.Type.HAND))
                return true;
        }
        return false;
    }

    /**
     * Returns null if no item detected or no TextureSpec or ModelSpec elements exist,
     * which is hard to get here without them
     */
    public NBTTagCompound getOrDontGetSpecTag(Spec spec) {
        NBTTagCompound renderTag = getRenderTag();
        if (renderTag.hasNoTags()) return null;

        // there can be many ModelPartSpecs
        if (spec instanceof ModelSpec && this.getRenderTag().hasKey(tagname)) {
            NBTTagList specList = renderTag.getTagList(tagname, Constants.NBT.TAG_COMPOUND);
            return NBTTagListUtils.getSpecNBTorNull(specList, spec.getOwnName());
        } else if (spec instanceof TextureSpec && this.getRenderTag().hasKey(tagname)) {
            if (spec.getOwnName().equals(this.getRenderTag().getCompoundTag(tagname).getString("model")))
                return getRenderTag().getCompoundTag(tagname);
        }
        return null;
    }

    public NBTTagCompound getSpecTag(Spec spec) {
        NBTTagCompound nbt = getOrDontGetSpecTag(spec);
        return nbt != null ? nbt : new NBTTagCompound();
    }

    /**
     * Gets the render tag for the whole model or makes one if needed
     */
    public NBTTagCompound getOrMakeSpecTag(Spec spec) {
        NBTTagCompound nbt = getSpecTag(spec);
        if(nbt.hasNoTags()) {
            if (spec instanceof ModelSpec) {
                NBTTagList specList = this.getRenderTag().getTagList(tagname, Constants.NBT.TAG_COMPOUND);
                nbt.setString("model", spec.getOwnName());
                NBTTagListUtils.replaceOrAddModelModelSpecNBT(specList, nbt);
                specList.appendTag(nbt);
                this.getRenderTag().setTag(tagname, specList);
            } else if (spec instanceof TextureSpec) {
                spec.get(getEquipmentSlot().getName()).multiSet(nbt, null);
                this.getRenderTag().setTag(tagname, nbt);
            }
        }
        return nbt;
    }

    /**
     * This just sets the spacing between lines
     */
    public void updateItems() {
        this.border.setHeight((isSpecValid()) ? 8 : 0);
    }

    /**
     * This draws the line of text
     */
    public void drawPartial(double min, double max) {
        if (isSpecValid())
            drawSpecPartial(border.left(), border.top(), model, min, max);
    }

    /**
     * Just changes color selection of the model
     */
    public void decrAbove(int index) {
        NBTTagCompound tagdata = getOrDontGetSpecTag(model);
        if (tagdata != null) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            int oldindex;
            if (model instanceof ModelSpec) {
                oldindex = ((ModelSpec) model).getColourIndex(tagdata);
                if (oldindex >= index && oldindex > 0) {
                    ((ModelSpec) model).setColourIndex(tagdata, oldindex - 1);
                    NBTTagCompound nbt = getRenderTag();
                    NBTTagList tagList = nbt.getTagList(tagname, Constants.NBT.TAG_COMPOUND);
                    NBTTagListUtils.replaceOrAddModelModelSpecNBT(tagList, tagdata);
                    NBTTagCompound nbt2 = new NBTTagCompound();
                    nbt2.setTag(tagname, tagList);
                    if (player.world.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem().inventorySlot, "tagList", nbt2).getPacket131());
                    }
                }
            } else if (model instanceof TextureSpec) {
                oldindex = model.getPart(getEquipmentSlot().getName()).getColourIndex(tagdata);
                if (oldindex >= index && oldindex > 0) {
                    model.getPart(getEquipmentSlot().getName()).setColourIndex(tagdata, oldindex -1);
                    if (player.world.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem().inventorySlot, "textureSpec", tagdata).getPacket131());
                    }
                }
            }
        }
    }

    /**
     * This just draws the model name and the icons on that line
     */
    public void drawSpecPartial(double x, double y, Spec spec, double ymino, double ymaxo) {
        NBTTagCompound tag = this.getSpecTag(spec);
        int selcomp = tag.hasNoTags() ? 0 :  1;
        int selcolour = spec instanceof TextureSpec ? spec.getPart(getEquipmentSlot().getName()).getColourIndex(tag) :
                ((ModelSpec)spec).getColourIndex(tag);
        new GuiIcons.TransparentArmor(x, y, null, null, ymino, null, ymaxo);
        new GuiIcons.NormalArmor(x + 8, y, null, null, ymino, null, ymaxo);
        new GuiIcons.SelectedArmorOverlay(x + selcomp * 8, y, null, null, ymino, null, ymaxo);

        double acc = (x + 28);
        for (byte colour : colourframe.colours()) {
            new GuiIcons.ArmourColourPatch(acc, y, EnumColour.getColourEnumFromIndex(Byte.toUnsignedInt(colour)).getColour(), null, ymino, null, ymaxo);
            acc += 8;
        }

        double textstartx = acc;

        if (selcomp > 0)
            new GuiIcons.SelectedArmorOverlay(x + 28 + selcolour * 8, y, null, null, ymino, null, ymaxo);
        MuseRenderer.drawString(spec.getDisaplayName(), textstartx + 4, y);
    }

    public MuseRect getBorder() {
        if (isSpecValid())
            border.setHeight(9 + 9);
        else
            this.border.setHeight(0);
        return this.border;
    }

    public boolean tryMouseClick(double x, double y) {
        if (!isSpecValid())
            return false;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        NBTTagCompound tagdata;

        // checks if click is within a certain area
        if (x < this.border.left() || x > this.border.right() || y < this.border.top() || y > this.border.bottom())
            return false;
        else if (x < this.border.left() + 24 && y > this.border.top()) {

            int lineNumber = (int)((y - this.border.top() - 8) / 8);
            int columnNumber = (int)((x - this.border.left()) / 8);
            Spec spec = model;
            MuseLogger.logDebug("Line " + lineNumber + " Column " + columnNumber);
            switch (columnNumber) {
                // removes the associated tag from the render tag making the model not visible
                case 0: {
                    NBTTagCompound nbt = new NBTTagCompound();
                    if (spec instanceof ModelSpec) {
                        NBTTagList nbtTagList = getRenderTag().getTagList(tagname, Constants.NBT.TAG_COMPOUND);
                        NBTTagListUtils.removeModelSpecNBT(nbtTagList, model.getOwnName());
                        nbt.setTag(tagname, nbtTagList);
                    }
                    if (player.world.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, nbt).getPacket131());
                    }
                    this.updateItems();
                    return true;
                }

                // set part to visible
                case 1: {
                    NBTTagCompound nbt;
                    if (spec instanceof TextureSpec) {
                        nbt = this.getOrMakeSpecTag(spec);
                    } else {
                        NBTTagList nbtTagList = getRenderTag().getTagList(tagname, Constants.NBT.TAG_COMPOUND);
                        tagdata = getOrMakeSpecTag(spec);
                        NBTTagListUtils.replaceOrAddModelModelSpecNBT(nbtTagList, tagdata);
                        nbt = new NBTTagCompound();
                        nbt.setTag(tagname, nbtTagList);
                   }
                    if (player.world.isRemote) {
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, nbt).getPacket131());
                    }
                    this.updateItems();
                    return true;
                }
                default:
                    return false;
            }
        }
        else if (x > this.border.left() + 28 && x < this.border.left() + 28 + this.colourframe.colours().length * 8) {
            int columnNumber = (int)((x - this.border.left() - 28) / 8);
            Spec spec = model;
            NBTTagCompound nbt;
            tagdata = this.getOrMakeSpecTag(spec);
            if (spec instanceof TextureSpec) {
                spec.get(getEquipmentSlot().getName()).setColourIndex(tagdata, columnNumber);
                nbt = tagdata;
            } else {
                NBTTagList nbtTagList = getRenderTag().getTagList(tagname, Constants.NBT.TAG_COMPOUND);
                ((ModelSpec)spec).setColourIndex(tagdata, columnNumber);
                NBTTagListUtils.replaceOrAddModelModelSpecNBT(nbtTagList, tagdata);
                nbt = new NBTTagCompound();
                nbt.setTag(tagname, nbtTagList);
            }

            if (player.world.isRemote) {
                PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, nbt).getPacket131());
            }
            return true;
        }
        return false;
    }
}