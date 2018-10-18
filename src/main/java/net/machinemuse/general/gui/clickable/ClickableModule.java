package net.machinemuse.general.gui.clickable;

import net.machinemuse.api.ILocalizeableModule;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.numina.render.MuseIconUtils;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.utils.MuseStringUtils;
import net.machinemuse.utils.render.GuiIcons;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.util.StatCollector;

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
    IPowerModule module;

    public ClickableModule(IPowerModule module, MusePoint2D position){
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

    public String getLocalizedName(IPowerModule m) {
        if (m instanceof ILocalizeableModule)
            return StatCollector.translateToLocal("module." + ((ILocalizeableModule) m).getUnlocalizedName() + ".name");
        return "module has localization issue";
    }

    public String getLocalizedDescription(IPowerModule m) {
        if (m instanceof ILocalizeableModule)
            return StatCollector.translateToLocal("module." + ((ILocalizeableModule) m).getUnlocalizedName() + ".desc");
        return "module has localization issue";
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
        MuseTextureUtils.pushTexture(getModule().getStitchedTexture(null));
        MuseIconUtils.drawIconAt(left, top, getModule().getIcon(null), Colour.WHITE);
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
