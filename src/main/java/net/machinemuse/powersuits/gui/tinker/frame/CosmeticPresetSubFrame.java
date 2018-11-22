package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.math.geometry.MuseRelativeRect;
import net.machinemuse.powersuits.client.render.modelspec.SpecBase;
import net.machinemuse.powersuits.gui.tinker.scrollable.ScrollableFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CosmeticPresetSubFrame extends ScrollableFrame {
    public ItemSelectionFrame itemSelect;
    public ColourPickerFrame colourSelect;
    public MusePoint2D topleft;
    public MusePoint2D bottomright;
    public Integer lastItemSlot;
    public List<CosmeticPresetSelectionSubframe> presetFrames;

    public CosmeticPresetSubFrame(ItemSelectionFrame itemSelect,
                                  ColourPickerFrame colourSelect,
                                  MusePoint2D topleft,
                                  MusePoint2D bottomright,
                                  Colour borderColour,
                                  Colour insideColour) {
        super(topleft, bottomright, borderColour, insideColour);
        this.itemSelect = itemSelect;
        this.colourSelect = colourSelect;
        this.topleft = topleft;
        this.bottomright = bottomright;
        this.lastItemSlot = null;
        this.presetFrames = getPresetFrames();
    }


    @Nonnull
    public ItemStack getItem() {
        return (itemSelect.getSelectedItem() != null) ? itemSelect.getSelectedItem().getItem() : ItemStack.EMPTY;
    }

    @Nullable
    public Integer getItemSlot() {
        return (itemSelect.getSelectedItem() != null) ? itemSelect.getSelectedItem().inventorySlot : null;
    }

    public int getColourIndex() {
        return this.colourSelect.selectedColour;
    }

    public List<CosmeticPresetSelectionSubframe> getPresetFrames() {
        List<CosmeticPresetSelectionSubframe> cosmeticFrameList = new ArrayList<>();
        ItemStack itemStack = getItem();




//        Iterable<SpecBase> specCollection = ModelRegistry.getInstance().getSpecs();
//        PartSpecManipSubFrame prev = null;
//        PartSpecManipSubFrame newframe;
//        for (SpecBase modelspec : specCollection) {
//            newframe = createNewFrame(modelspec, prev);
//            prev = newframe;
//            modelframesList.add(newframe);
//        }

        // TODO: populate from config using selected item as key...




        return cosmeticFrameList;
    }

    public CosmeticPresetSelectionSubframe createNewFrame(String label, NBTTagCompound nbt, CosmeticPresetSelectionSubframe prev) {
        MuseRelativeRect newborder = new MuseRelativeRect(this.topleft.getX() + 4, this.topleft.getY() + 4, this.bottomright.getX(), this.topleft.getY() + 10);
        newborder.setBelow((prev != null) ? prev.border : null);





        return new CosmeticPresetSelectionSubframe(label, nbt, new MusePoint2D(newborder.centerx(), newborder.centery()),  this.itemSelect, newborder);
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        if (button == 0) {
            for (CosmeticPresetSelectionSubframe frame : presetFrames) {
                if (frame.hitbox(x, y))
                    return;
            }
        }
    }

//    @Override
//    public void update(double mousex, double mousey) {
//        super.update(mousex, mousey);
//        if (!Objects.equals(lastItemSlot, getItemSlot())) {
//            lastItemSlot = getItemSlot();
//            colourSelect.refreshColours(); // this does nothing
//
//            double x = 0;
//            for (CosmeticPresetSelectionSubframe subframe : presetFrames) {
//                subframe.updateItems();
//                x += subframe.border.bottom();
//            }
//            this.totalsize = (int) x;
//        }
//        if (colourSelect.decrAbove > -1) {
//            decrAbove(colourSelect.decrAbove);
//            colourSelect.decrAbove = -1;
//        }
//    }
//
//    public void decrAbove(int index) {
//        for (CosmeticPresetSelectionSubframe frame : presetFrames) frame.decrAbove(index);
//    }

    @Override
    public void draw() {
        super.preDraw();
        GL11.glPushMatrix();
        GL11.glTranslated(0.0, (double) (-this.currentscrollpixels), 0.0);
        for (CosmeticPresetSelectionSubframe f : presetFrames) {
            f.draw();
        }
        GL11.glPopMatrix();
        super.postDraw();
    }
}
