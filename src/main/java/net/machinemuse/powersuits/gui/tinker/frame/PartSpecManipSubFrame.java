package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.math.geometry.MuseRelativeRect;
import net.machinemuse.powersuits.client.render.modelspec.*;
import net.machinemuse.powersuits.network.packets.MusePacketCosmeticInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;

import static net.machinemuse.powersuits.api.constants.MPSNBTConstants.NBT_TEXTURESPEC_TAG;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:46 AM, 30/04/13
 * <p>
 * Ported to Java by lehjr on 11/2/16.
 */
public class PartSpecManipSubFrame extends PartSpecManipSubFrameBase {
    public PartSpecManipSubFrame(SpecBase model, ColourPickerFrame colourframe, ItemSelectionFrame itemSelector, MuseRelativeRect border) {
        super(model, colourframe, itemSelector, border);
    }

    public void decrAbove(int index) {
        for (PartSpecBase spec : partSpecs) {
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

    @Override
    public boolean tryMouseClick(double x, double y) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        NBTTagCompound tagdata;
        String tagname;

        // player clicked outside the area
        if (x < this.border.left() || x > this.border.right() || y < this.border.top() || y > this.border.bottom())
            return false;

            // opens the list of partSpecs
        else if (x > this.border.left() + 2 && x < this.border.left() + 8 && y > this.border.top() + 2 && y < this.border.top() + 8) {
            this.open = !this.open;
            this.getBorder();
            return true;

            // player clicked one of the icons for off/on/glowOn
        } else if (x < this.border.left() + 24 && y > this.border.top() + 8) {
            int lineNumber = (int) ((y - this.border.top() - 8) / 8);
            int columnNumber = (int) ((x - this.border.left()) / 8);
            PartSpecBase spec = partSpecs.get(Math.max(Math.min(lineNumber, partSpecs.size() - 1), 0));
            MuseLogger.logDebug("Line " + lineNumber + " Column " + columnNumber);

            switch (columnNumber) {
                // removes the associated tag from the render tag making the part not visible
                case 0: {
                    if (spec instanceof TexturePartSpec)
                        tagname = NBT_TEXTURESPEC_TAG;
                    else
                        tagname = ModelRegistry.getInstance().makeName(spec);
                    if (player.world.isRemote)
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, new NBTTagCompound()).getPacket131());
                    this.updateItems();
                    return true;
                }

                // set part to visible
                case 1: {
                    if (spec instanceof TexturePartSpec)
                        tagname = NBT_TEXTURESPEC_TAG;
                    else
                        tagname = ModelRegistry.getInstance().makeName(spec);
                    tagdata = this.getOrMakeSpecTag(spec);
                    if (spec instanceof ModelPartSpec)
                        ((ModelPartSpec) spec).setGlow(tagdata, false);
                    if (player.world.isRemote)
                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
                    this.updateItems();
                    return true;
                }

                // glow on
                case 2: {
                    if (spec instanceof ModelPartSpec) {
                        tagname = ModelRegistry.getInstance().makeName(spec);
                        tagdata = this.getOrMakeSpecTag(spec);
                        ((ModelPartSpec) spec).setGlow(tagdata, true);
                        if (player.world.isRemote)
                            PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
                        this.updateItems();
                        return true;
                    }
                    return false;
                }
                default:
                    return false;
            }
        }
        // player clicked a color icon
        else if (x > this.border.left() + 28 && x < this.border.left() + 28 + this.colourframe.colours().length * 8) {
            int lineNumber = (int) ((y - this.border.top() - 8) / 8);
            int columnNumber = (int) ((x - this.border.left() - 28) / 8);
            PartSpecBase spec = partSpecs.get(Math.max(Math.min(lineNumber, partSpecs.size() - 1), 0));

            if (spec instanceof TexturePartSpec)
                tagname = NBT_TEXTURESPEC_TAG;
            else
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
