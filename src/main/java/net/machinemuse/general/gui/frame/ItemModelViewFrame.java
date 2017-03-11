package net.machinemuse.general.gui.frame;

import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.DrawableMuseRect;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.powersuits.client.render.item.ArmorModelInstance;
import net.machinemuse.powersuits.client.render.item.IArmorModel;
import net.machinemuse.powersuits.item.ItemPowerArmor;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:25 PM, 5/2/13
 *
 * Ported to Java by lehjr on 11/2/16.
 */
public class ItemModelViewFrame implements IGuiFrame {
    ItemSelectionFrame itemSelector;
    DrawableMuseRect border;
    double anchorx = 0;
    double anchory = 0;
    int dragging = -1;
    double lastdWheel = 0;
    double rotx = 0;
    double roty = 0;
    double offsetx = 0;
    double offsety = 0;
    double zoom = 64;

    public ItemModelViewFrame(ItemSelectionFrame itemSelector, MusePoint2D topleft, MusePoint2D bottomright, Colour borderColour, Colour insideColour) {
        this.itemSelector = itemSelector;
        this.border = new DrawableMuseRect(topleft, bottomright, borderColour, insideColour);
    }

    int getArmorSlot() {
        return ((ItemPowerArmor)getSelectedItem().getItem().getItem()).armorType;
    }

    ClickableItem getSelectedItem() {
        return itemSelector.getSelectedItem();
    }

    NBTTagCompound getRenderTag() {
        return MuseItemUtils.getMuseRenderTag(getSelectedItem().getItem(), getArmorSlot());
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        if (border.containsPoint(x, y)) {
            dragging = button;
            anchorx = x;
            anchory = y;
        }
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
        dragging = -1;
    }

    @Override
    public void update(double mousex, double mousey) {
        if (border.containsPoint(mousex, mousey)) {
            double dscroll = (lastdWheel - Mouse.getDWheel()) / 120;
            zoom = zoom * Math.pow(1.1, dscroll);
            lastdWheel = Mouse.getDWheel();
        }
        double dx = mousex - anchorx;
        double dy = mousey - anchory;
        switch(dragging) {
            case 0: {
                rotx = MuseMathUtils.clampDouble(rotx + dy, -90, 90);
                roty = roty - dx;
                anchorx = mousex;
                anchory = mousey;
                break;
            }
            case 1: {
                offsetx = offsetx + dx;
                offsety = offsety + dy;
                anchorx = mousex;
                anchory = mousey;
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void draw() {
        Minecraft mc = Minecraft.getMinecraft();
        border.draw();
        if (itemSelector.getSelectedItem() == null || !(getSelectedItem().getItem().getItem() instanceof ItemPowerArmor)) return;
        GL11.glPushMatrix();
        ((IArmorModel)ArmorModelInstance.getInstance()).setRenderSpec(MuseItemUtils.getMuseRenderTag(getSelectedItem().getItem(), getArmorSlot()));
        ((IArmorModel)ArmorModelInstance.getInstance()).setVisibleSection(this.getArmorSlot());
        GL11.glTranslated(border.centerx() + offsetx, border.centery() + offsety, 0);
        GL11.glScaled(zoom, zoom, zoom);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glRotatef((float) rotx, 1, 0, 0);
        GL11.glRotatef((float) roty, 0, 1, 0);
        GL11.glTranslated(0, -getArmorSlot() / 2.0, 0);
        ((ModelBiped)ArmorModelInstance.getInstance()).render((Entity)mc.thePlayer, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
        GL11.glPopMatrix();
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
    }
}
