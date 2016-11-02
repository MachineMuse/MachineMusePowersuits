package net.machinemuse.general.gui.frame;

import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.DrawableMuseRect;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.powersuits.client.render.item.ArmorModel$;
import net.machinemuse.powersuits.item.ItemPowerArmor;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import scala.None$;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:25 PM, 5/2/13
 *
 * Ported to Java by lehjr on 11/2/16.
 */
public class ItemModelViewFrame implements IGuiFrame
{
    private final ItemSelectionFrame itemSelector;
    private final DrawableMuseRect border;
    private double anchorx;
    private double anchory;
    private int dragging;
    private double lastdWheel;
    private double rotx;
    private double roty;
    private double offsetx;
    private double offsety;
    private double zoom;

    public ItemModelViewFrame(final ItemSelectionFrame itemSelector, final MusePoint2D topleft, final MusePoint2D bottomright, final Colour borderColour, final Colour insideColour) {
        this.itemSelector = itemSelector;
        this.border = new DrawableMuseRect(topleft, bottomright, borderColour, insideColour);
        this.anchorx = 0.0;
        this.anchory = 0.0;
        this.dragging = -1;
        this.lastdWheel = 0.0;
        this.rotx = 0.0;
        this.roty = 0.0;
        this.offsetx = 0.0;
        this.offsety = 0.0;
        this.zoom = 64.0;
    }

    public int getArmorSlot() {
        return ((ItemPowerArmor)this.getSelectedItem().getItem().getItem()).armorType;
    }

    public ClickableItem getSelectedItem() {
        return this.itemSelector.getSelectedItem();
    }

    public NBTTagCompound getRenderTag() {
        return MuseItemUtils.getMuseRenderTag(this.getSelectedItem().getItem(), this.getArmorSlot());
    }

    @Override
    public void onMouseDown(final double x, final double y, final int button) {
        if (this.border.containsPoint(x, y)) {
            this.dragging = button;
            this.anchorx = x;
            this.anchory = y;
        }
    }

    @Override
    public void onMouseUp(final double x, final double y, final int button) {
        this.dragging = -1;
    }

    @Override
    public void update(final double mousex, final double mousey) {
        if (this.border.containsPoint(mousex, mousey)) {
            final double dscroll = (this.lastdWheel - Mouse.getDWheel()) / 120;
            this.zoom = (this.zoom * Math.pow(1.1, dscroll));
            this.lastdWheel = Mouse.getDWheel();
        }
        final double dx = mousex - this.anchorx;
        final double dy = mousey - this.anchory;
        switch (this.dragging) {
            default: {
                final None$ module$ = None$.MODULE$;
                break;
            }
            case 1: {
                this.offsetx = this.offsetx + dx;
                this.offsety = this.offsety + dy;
                this.anchorx = mousex;
                this.anchory = mousey;
                break;
            }
            case 0: {
                this.rotx = MuseMathUtils.clampDouble(this.rotx + dy, -90.0, 90.0);
                this.roty = this.roty - dx;
                this.anchorx = mousex;
                this.anchory = mousey;
                break;
            }
            case -1: {
                final None$ module$2 = None$.MODULE$;
                break;
            }
        }
    }

    @Override
    public void draw() {
        final Minecraft mc = Minecraft.getMinecraft();
        this.border.draw();
        if (this.itemSelector.getSelectedItem() != null && this.getSelectedItem().getItem().getItem() instanceof ItemPowerArmor) {
            GL11.glPushMatrix();
            ArmorModel$.MODULE$.instance().renderSpec_$eq(MuseItemUtils.getMuseRenderTag(this.getSelectedItem().getItem(), this.getArmorSlot()));
            ArmorModel$.MODULE$.instance().visibleSection_$eq(this.getArmorSlot());
            GL11.glTranslated(this.border.centerx() + this.offsetx, this.border.centery() + this.offsety, 0.0);
            GL11.glScaled(this.zoom, this.zoom, this.zoom);
            GL11.glClear(256);
            GL11.glDisable(2884);
            GL11.glRotatef((float)this.rotx, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef((float)this.roty, 0.0f, 1.0f, 0.0f);
            GL11.glTranslated(0.0, -this.getArmorSlot() / 2.0, 0.0);
            ((ModelBiped)ArmorModel$.MODULE$.instance()).render((Entity)mc.thePlayer, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
            GL11.glPopMatrix();
        }
    }

    @Override
    public List<String> getToolTip(final int x, final int y) {
        return null;
    }
}