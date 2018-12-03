package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.math.geometry.MuseRelativeRect;
import net.machinemuse.powersuits.client.render.modelspec.SpecBase;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.gui.tinker.scrollable.ScrollableFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CosmeticPresetContainer extends ScrollableFrame {
    public ItemSelectionFrame itemSelect;
    public ColourPickerFrame colourSelect;
    public MusePoint2D topleft;
    public MusePoint2D bottomright;
    public Integer lastItemSlot;
    public List<CosmeticPresetSelectionSubframe> presetFrames;
    protected boolean enabled;
    protected boolean visibile;

    public CosmeticPresetContainer(ItemSelectionFrame itemSelect,
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
        this.visibile = true;
        this.enabled = true;
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
        Map<String, NBTTagCompound> presets = MPSConfig.INSTANCE.getCosmeticPresets(getItem());
        CosmeticPresetSelectionSubframe newFrame;
        CosmeticPresetSelectionSubframe prev = null;
        for (Map.Entry entry :  MPSConfig.INSTANCE.getCosmeticPresets(getItem()).entrySet()) {
            newFrame = createNewFrame((String)entry.getKey(), (NBTTagCompound) entry.getValue(), prev);
            prev = newFrame;
//            newFrame.setMode(0);
            cosmeticFrameList.add(newFrame);
        }
        return cosmeticFrameList;
    }

    public CosmeticPresetSelectionSubframe createNewFrame(String label, NBTTagCompound nbt, CosmeticPresetSelectionSubframe prev) {
        MuseRelativeRect newborder = new MuseRelativeRect(this.border.left() + 8, this.border.top() + 10, this.border.right(), this.border.top() + 24);
        newborder.setBelow((prev != null) ? prev.border : null);





        return new CosmeticPresetSelectionSubframe(label, nbt, new MusePoint2D(newborder.left(), newborder.centery()),  this.itemSelect, newborder);
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        if (enabled) {

            System.out.println(" mouse click: " + x + ", " + y);


            if (button == 0) {
                for (CosmeticPresetSelectionSubframe frame : presetFrames) {
                    if (frame.hitbox(x, y))
                        return;
                }
            }
        }
    }

    @Override
    public void update(double mouseX, double mouseY) {
        super.update(mouseX, mouseY);

        if (enabled) {
            if (!Objects.equals(lastItemSlot, getItemSlot())) {
                lastItemSlot = getItemSlot();

                presetFrames = getPresetFrames();
                double x = 0;
                for (CosmeticPresetSelectionSubframe subframe : presetFrames) {
//                subframe.updateItems();
                    x += subframe.border.bottom();
                }
                this.totalsize = (int) x;
//        }
                if (colourSelect.decrAbove > -1) {
//            decrAbove(colourSelect.decrAbove);
                    colourSelect.decrAbove = -1;
                }
            }
        }
    }

    public void hide () {
        visibile = false;
    }

    public void show() {
        visibile = true;
    }

    public boolean isVisibile() {
        return visibile;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void draw() {
        if (visibile) {
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
}
