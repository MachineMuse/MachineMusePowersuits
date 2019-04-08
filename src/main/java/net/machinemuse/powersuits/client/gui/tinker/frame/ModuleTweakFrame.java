package net.machinemuse.powersuits.client.gui.tinker.frame;

import net.machinemuse.numina.client.gui.clickable.ClickableItem;
import net.machinemuse.numina.client.gui.clickable.ClickableTinkerSlider;
import net.machinemuse.numina.client.gui.scrollable.ScrollableFrame;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.machinemuse.numina.nbt.propertymodifier.IPropertyModifier;
import net.machinemuse.numina.nbt.propertymodifier.PropertyModifierLinearAdditiveDouble;
import net.machinemuse.numina.string.MuseStringUtils;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.machinemuse.powersuits.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.machinemuse.powersuits.network.MPSPackets;
import net.machinemuse.powersuits.network.packets.MusePacketTweakRequestDouble;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class ModuleTweakFrame extends ScrollableFrame {
    protected static double SCALERATIO = 0.75;
    protected static int margin = 4;
    protected ItemSelectionFrame itemTarget;
    protected ModuleSelectionFrame moduleTarget;
    protected List<ClickableTinkerSlider> sliders;
    protected Map<String, Double> propertyStrings;
    protected ClickableTinkerSlider selectedSlider;
    protected EntityPlayerSP player;

    public ModuleTweakFrame(
            EntityPlayerSP player,
            MusePoint2D topleft,
            MusePoint2D bottomright,
            Colour borderColour,
            Colour insideColour,
            ItemSelectionFrame itemTarget,
            ModuleSelectionFrame moduleTarget) {
        super(topleft.times(1 / SCALERATIO), bottomright.times(1 / SCALERATIO), borderColour, insideColour);
        this.itemTarget = itemTarget;
        this.moduleTarget = moduleTarget;
        this.player = player;
    }

    @Override
    public void update(double mousex, double mousey) {
        mousex /= SCALERATIO;
        if (itemTarget.getSelectedItem() != null && moduleTarget.getSelectedModule() != null) {
            ItemStack stack = itemTarget.getSelectedItem().getItem();
            ItemStack module = moduleTarget.getSelectedModule().getModule();
            if (ModuleManager.INSTANCE.itemHasModule(itemTarget.getSelectedItem().getItem(), moduleTarget.getSelectedModule().getModule().getItem().getRegistryName())) {
                loadTweaks(stack, module);
            } else {
                sliders = null;
                propertyStrings = null;
            }
        } else {
            sliders = null;
            propertyStrings = null;
        }
        if (selectedSlider != null) {
            selectedSlider.setValueByX(mousex);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (sliders != null) {
            GL11.glPushMatrix();
            GL11.glScaled(SCALERATIO, SCALERATIO, SCALERATIO);
            super.render(mouseX, mouseY, partialTicks);
            MuseRenderer.drawCenteredString("Tinker", (border.left() + border.right()) / 2, border.top() + 2);
            for (ClickableTinkerSlider slider : sliders) {
                slider.render(mouseX, mouseY, partialTicks);
            }
            int nexty = (int) (sliders.size() * 20 + border.top() + 23);
            for (Map.Entry<String, Double> property : propertyStrings.entrySet()) {
                String formattedValue = MuseStringUtils.formatNumberFromUnits(property.getValue(), ItemAbstractPowerModule.getUnit(property.getKey()));
                String name = property.getKey();
                double valueWidth = MuseRenderer.getStringWidth(formattedValue);
                double allowedNameWidth = border.width() - valueWidth - margin * 2;

                List<String> namesList = MuseStringUtils.wrapStringToVisualLength(
                        I18n.format(MPSModuleConstants.MODULE_TRADEOFF_PREFIX + name), allowedNameWidth);
                for (int i = 0; i < namesList.size(); i++) {
                    MuseRenderer.drawString(namesList.get(i), border.left() + margin, nexty + 9 * i);
                }
                MuseRenderer.drawRightAlignedString(formattedValue, border.right() - margin, nexty + 9 * (namesList.size() - 1) / 2);
                nexty += 9 * namesList.size() + 1;

            }
            GL11.glPopMatrix();
        }
    }

    private void loadTweaks(ItemStack stack, ItemStack module) {
//        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
        NBTTagCompound moduleTag = MuseNBTUtils.getMuseModuleTag(module);

        propertyStrings = new HashMap();
        Set<String> tweaks = new HashSet<String>();

        Map<String, List<IPropertyModifier>> propertyModifiers = ModuleManager.INSTANCE.getPropertyModifiers(module);
        for (Map.Entry<String, List<IPropertyModifier>> property : propertyModifiers.entrySet()) {
            double currValue = 0;
            for (IPropertyModifier modifier : property.getValue()) {
                currValue = (double) modifier.applyModifier(moduleTag, currValue);
                if (modifier instanceof PropertyModifierLinearAdditiveDouble) {
                    tweaks.add(((PropertyModifierLinearAdditiveDouble) modifier).getTradeoffName());
                }
            }
            propertyStrings.put(property.getKey(), currValue);
        }

        sliders = new LinkedList();
        int y = 0;
        for (String tweak : tweaks) {
            y += 20;
            MusePoint2D center = new MusePoint2D((border.left() + border.right()) / 2, border.top() + y);
            ClickableTinkerSlider slider = new ClickableTinkerSlider(
                    center,
                    border.right() - border.left() - 8,
                    moduleTag,
                    tweak, I18n.format(MPSModuleConstants.MODULE_TRADEOFF_PREFIX + tweak));
            sliders.add(slider);
            if (selectedSlider != null && slider.hitBox(center.getX(), center.getY())) {
                selectedSlider = slider;
            }
        }
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        x /= SCALERATIO;
        y /= SCALERATIO;
        if (button == 0) {
            if (sliders != null) {
                for (ClickableTinkerSlider slider : sliders) {
                    if (slider.hitBox(x, y)) {
                        selectedSlider = slider;
                    }
                }
            }
        }
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
        if (selectedSlider != null && itemTarget.getSelectedItem() != null && moduleTarget.getSelectedModule() != null) {
            ClickableItem item = itemTarget.getSelectedItem();
            ItemStack module = moduleTarget.getSelectedModule().getModule();
            if (!module.isEmpty())
                MPSPackets.sendToServer(
                        new MusePacketTweakRequestDouble(player.getEntityId(), item.inventorySlot, module.getItem().getRegistryName().toString(), selectedSlider.id(), selectedSlider.getValue()));
        }
        if (button == 0) {
            selectedSlider = null;
        }
    }
}