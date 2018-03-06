package net.machinemuse.powersuits.client.gui.tinker.clickable;

import net.machinemuse.numina.api.module.IModule;
import net.machinemuse.numina.client.render.MuseIconUtils;
import net.machinemuse.numina.client.render.MuseTextureUtils;
import net.machinemuse.numina.math.geometry.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.render.MuseRenderer;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.machinemuse.powersuits.client.gui.tinker.GuiIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableModule extends Clickable {
    boolean allowed = true;
    boolean installed = false;
    final Colour checkmarkcolour = new Colour(0.0F, 0.667F, 0.0F, 1.0F);
    ItemStack module;

    public ClickableModule(ItemStack module, MusePoint2D position){
        super(position);
        this.module = module;
    }

    @Override
    public List<String> getToolTip() {
        List<String> toolTipText = new ArrayList<String>();
        toolTipText.add(getLocalizedName(getModule()));
        toolTipText.addAll(MuseStringUtils.wrapStringToLength(getLocalizedDescription(getModule()), 30));
        return toolTipText;
    }

    public String getLocalizedName(ItemStack m) {
            return I18n.format( m.getUnlocalizedName() + ".name");
    }

    public String getLocalizedDescription(ItemStack m) {
            return I18n.format( m.getUnlocalizedName() + ".desc");
    }

    @Override
    public void draw() {
        double k = Integer.MAX_VALUE;
        double left = getPosition().x() - 8;
        double top = getPosition().y() - 8;
        drawPartial(left, top, left + 16, top + 16);
    }

    public void drawPartial(double xmino, double ymino, double xmaxo, double ymaxo) {
        double left = getPosition().x() - 8;
        double top = getPosition().y() - 8;

        MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
        MuseIconUtils.drawIconAt(left, top,
//                getModule().getIcon(null),
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(getModule()).getParticleTexture(),
                Colour.WHITE);
        MuseTextureUtils.popTexture();

        if (!allowed) {
            String string = MuseStringUtils.wrapFormatTags("x", MuseStringUtils.FormatCodes.DarkRed);
            MuseRenderer.drawString(string, getPosition().x() + 3, getPosition().y() + 1);
        }
        else if (installed) {
            new GuiIcons.Checkmark(getPosition().x() - 8 + 1, getPosition().y() - 8 + 1, checkmarkcolour, null, null, null, null);
        }
    }

    @Override
    public boolean hitBox(double x, double y) {
        boolean hitx = Math.abs(x - getPosition().x()) < 8;
        boolean hity = Math.abs(y - getPosition().y()) < 8;
        return hitx && hity;
    }

    public ItemStack getModule() {
        return module;
    }

    public boolean equals(ClickableModule other) {
        return this.module == other.getModule();
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }
}