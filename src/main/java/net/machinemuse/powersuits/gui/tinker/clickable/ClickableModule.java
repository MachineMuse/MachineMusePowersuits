package net.machinemuse.powersuits.gui.tinker.clickable;

import net.machinemuse.numina.client.gui.clickable.Clickable;
import net.machinemuse.numina.client.render.MuseIconUtils;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.client.render.MuseTextureUtils;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.numina.string.MuseStringUtils;
import net.machinemuse.powersuits.gui.GuiIcons;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 *
 * @author MachineMuse
 * <p>
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableModule extends Clickable {
    final Colour checkmarkcolour = new Colour(0.0F, 0.667F, 0.0F, 1.0F);
    boolean allowed = true;
    boolean installed = false;
    IPowerModule module;

    public ClickableModule(IPowerModule module, MusePoint2D position) {
        super(position);
        this.module = module;
    }

    @Override
    public List<String> getToolTip() {
        List<String> toolTipText = new ArrayList<>();
        toolTipText.add(getLocalizedName(getModule()));
        toolTipText.addAll(MuseStringUtils.wrapStringToLength(getLocalizedDescription(getModule()), 30));
        return toolTipText;
    }

    public String getLocalizedName(IPowerModule m) {
        if (m == null)
            return "";

        return (m.getDataName() != null && !m.getDataName().isEmpty()) ? I18n.format(m.getUnlocalizedName() + ".name") : "broken translation for module name";
    }

    public String getLocalizedDescription(IPowerModule m) {
        if (m == null)
            return "";
        return (m.getDataName() != null && !m.getDataName().isEmpty()) ? I18n.format(m.getUnlocalizedName() + ".desc") : "broken translation for module description";
    }

    @Override
    public void draw() {
        double k = Integer.MAX_VALUE;
        double left = getPosition().getX() - 8;
        double top = getPosition().getY() - 8;
        drawPartial(left, top, left + 16, top + 16);
    }

    // TODO: switch to models in order to handle icons overrriden with resource packs or items with no icons

    public void drawPartial(double xmino, double ymino, double xmaxo, double ymaxo) {
//         IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);

        double left = getPosition().getX() - 8;
        double top = getPosition().getY() - 8;

        MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
        MuseIconUtils.drawIconAt(left, top, getModule().getIcon(null), Colour.WHITE);
        MuseTextureUtils.popTexture();

        if (!allowed) {
            String string = MuseStringUtils.wrapFormatTags("x", MuseStringUtils.FormatCodes.DarkRed);
            MuseRenderer.drawString(string, getPosition().getX() + 3, getPosition().getY() + 1);
        } else if (installed) {
            new GuiIcons.Checkmark(getPosition().getX() - 8 + 1, getPosition().getY() - 8 + 1, checkmarkcolour, null, null, null, null);
        }
    }

    @Override
    public boolean hitBox(double x, double y) {
        boolean hitx = Math.abs(x - getPosition().getX()) < 8;
        boolean hity = Math.abs(y - getPosition().getY()) < 8;
        return hitx && hity;
    }

    public IPowerModule getModule() {
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
