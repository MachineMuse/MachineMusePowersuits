package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.client.gui.scrollable.ScrollableFrame;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.constants.NuminaNBTConstants;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.string.MuseStringUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class DetailedSummaryFrame extends ScrollableFrame {
    public static final double SCALEFACTOR = 1;
    protected EntityPlayer player;
    protected int slotPoints;
    protected int energy;
    protected double armor;
    protected ItemSelectionFrame itemSelectionFrame;

    public DetailedSummaryFrame(EntityPlayer player,
                                MusePoint2D topleft,
                                MusePoint2D bottomright,
                                Colour borderColour,
                                Colour insideColour,
                                ItemSelectionFrame itemSelectionFrame) {
        super(topleft.times(1.0 / SCALEFACTOR), bottomright.times(1.0 / SCALEFACTOR), borderColour, insideColour);
        this.player = player;
        this.itemSelectionFrame = itemSelectionFrame;
    }

    @Override
    public void update(double mousex, double mousey) {
        energy = 0;
        armor = 0;
        slotPoints = 0;

        if (itemSelectionFrame.getSelectedItem() != null) {
            slotPoints += (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemSelectionFrame.getSelectedItem().getItem(), MPSModuleConstants.SLOT_POINTS);
        }

        for (ItemStack stack : MuseItemUtils.getModularItemsEquipped(player)) {
            energy += (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, NuminaNBTConstants.MAXIMUM_ENERGY);
//            slotPoints += (int)ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.SLOT_POINTS);
            armor += ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.ARMOR_VALUE_PHYSICAL);
            armor += ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.ARMOR_VALUE_ENERGY);
        }
    }

    @Override
    public void draw() {
        if (player != null) {
            GL11.glPushMatrix();
            GL11.glScaled(SCALEFACTOR, SCALEFACTOR, SCALEFACTOR);
            super.draw();
            int margin = 4;
            int nexty = (int) border.top() + margin;
            MuseRenderer.drawCenteredString(I18n.format("gui.powersuits.equippedTotals"), (border.left() + border.right()) / 2, nexty);
            nexty += 10;

            // Max Energy
            String formattedValue = MuseStringUtils.formatNumberFromUnits(energy, PowerModuleBase.getUnit(NuminaNBTConstants.MAXIMUM_ENERGY));
            String name = I18n.format("gui.powersuits.energyStorage");
            double valueWidth = MuseRenderer.getStringWidth(formattedValue);
            double allowedNameWidth = border.width() - valueWidth - margin * 2;
            List<String> namesList = MuseStringUtils.wrapStringToVisualLength(name, allowedNameWidth);
            for (int i = 0; i < namesList.size(); i++) {
                MuseRenderer.drawString(namesList.get(i), border.left() + margin, nexty + 9 * i);
            }
            MuseRenderer.drawRightAlignedString(formattedValue, border.right() - margin, nexty + 9 * (namesList.size() - 1) / 2);
            nexty += 10 * namesList.size() + 1;

            // Slot points
            if (slotPoints > 0) {
                formattedValue = MuseStringUtils.wrapFormatTags(MuseStringUtils.formatNumberFromUnits(slotPoints, "pts"), MuseStringUtils.FormatCodes.BrightGreen);
                name = I18n.format("gui.powersuits.slotpoints");
                valueWidth = MuseRenderer.getStringWidth(formattedValue);
                allowedNameWidth = border.width() - valueWidth - margin * 2;
                namesList = MuseStringUtils.wrapStringToVisualLength(name, allowedNameWidth);
                assert namesList != null;
                for (int i = 0; i < namesList.size(); i++) {
                    MuseRenderer.drawString(namesList.get(i), border.left() + margin, nexty + 9 * i);
                }
                MuseRenderer.drawRightAlignedString(formattedValue, border.right() - margin, nexty + 9 * (namesList.size() - 1) / 2);
                nexty += 10 * namesList.size() + 1;
            }

            // Armor points
            formattedValue = MuseStringUtils.formatNumberFromUnits(armor, "pts");
            name = I18n.format("gui.powersuits.armor");
            valueWidth = MuseRenderer.getStringWidth(formattedValue);
            allowedNameWidth = border.width() - valueWidth - margin * 2;
            namesList = MuseStringUtils.wrapStringToVisualLength(name, allowedNameWidth);
            assert namesList != null;
            for (int i = 0; i < namesList.size(); i++) {
                MuseRenderer.drawString(namesList.get(i), border.left() + margin, nexty + 9 * i);
            }
            MuseRenderer.drawRightAlignedString(formattedValue, border.right() - margin, nexty + 9 * (namesList.size() - 1) / 2);

            GL11.glPopMatrix();
        }
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
    }
}
