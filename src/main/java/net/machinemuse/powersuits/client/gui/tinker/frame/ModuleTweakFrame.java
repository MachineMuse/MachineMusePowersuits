package net.machinemuse.powersuits.client.gui.tinker.frame;

import net.machinemuse.numina.api.module.IPowerModule;
import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.machinemuse.numina.api.nbt.IPropertyModifierDouble;
import net.machinemuse.numina.api.nbt.PropertyModifierLinearAdditiveDouble;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.numina.utils.render.MuseRenderer;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.gui.tinker.clickable.ClickableItem;
import net.machinemuse.powersuits.client.gui.tinker.clickable.ClickableTinkerSlider;
import net.machinemuse.powersuits.network.packets.MusePacketTweakRequestDouble;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.MuseStringUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModuleTweakFrame extends ScrollableFrame {
    protected static double SCALERATIO = 0.75;
    protected static int margin = 4;
    protected ItemSelectionFrame itemTarget;
    protected ModuleSelectionFrame moduleTarget;
    protected List<ClickableTinkerSlider> sliders;
    protected Map<String, Object> propertyStrings;
    protected ClickableTinkerSlider selectedSlider;
    protected EntityPlayerSP player;
    protected Map<String, Class> tweaks;


    public ModuleTweakFrame(
            EntityPlayerSP player,
            MusePoint2D topleft, MusePoint2D bottomright,
            Colour borderColour, Colour insideColour,
            ItemSelectionFrame itemTarget, ModuleSelectionFrame moduleTarget) {
        super(topleft.times(1 / SCALERATIO), bottomright.times(1 / SCALERATIO), borderColour, insideColour);
        this.itemTarget = itemTarget;
        this.moduleTarget = moduleTarget;
        this.player = player;
    }

    @Override
    public void update(double mousex, double mousey) {
        mousex /= SCALERATIO;
        mousey /= SCALERATIO;
        if (itemTarget.getSelectedItem() != null && moduleTarget.getSelectedModule() != null) {
            ItemStack stack = itemTarget.getSelectedItem().getItem();
            IPowerModule module = moduleTarget.getSelectedModule().getModule();
            if (ModuleManager.INSTANCE.itemHasModule(itemTarget.getSelectedItem().getItem(), moduleTarget.getSelectedModule().getModule().getDataName())) {
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
            selectedSlider.moveSlider(mousex, mousey);
        }
    }

    @Override
    public void draw() {
        if (sliders != null) {
            IPowerModule module = moduleTarget.getSelectedModule().getModule();

            GL11.glPushMatrix();
            GL11.glScaled(SCALERATIO, SCALERATIO, SCALERATIO);
            super.draw();
            MuseRenderer.drawCenteredString("Tinker", (border.left() + border.right()) / 2, border.top() + 2);
            for (ClickableTinkerSlider slider : sliders) {
                slider.draw();
            }
            int nexty = (int) (sliders.size() * 20 + border.top() + 23);
            for (Map.Entry<String, Object> property : propertyStrings.entrySet()) {
                String name = property.getKey();
                String formattedValue;
                formattedValue = MuseStringUtils.formatNumberFromUnits(
                        // double or int cast required
                        (property.getValue() instanceof Double) ?
                        (double) property.getValue() :
                                (int) property.getValue(),
                        ((PowerModuleBase) module).getUnit(name));

                double valueWidth = MuseRenderer.getStringWidth(formattedValue);
                double allowedNameWidth = border.width() - valueWidth - margin * 2;
                List<String> namesList = MuseStringUtils.wrapStringToVisualLength(name, allowedNameWidth);
                for (int i = 0; i < namesList.size(); i++) {
                    MuseRenderer.drawString(namesList.get(i), border.left() + margin, nexty + 9 * i);
                }
                MuseRenderer.drawRightAlignedString(formattedValue, border.right() - margin, nexty + 9 * (namesList.size() - 1) / 2);
                nexty += 9 * namesList.size() + 1;
            }

            GL11.glPopMatrix();
        }
    }

    private void loadTweaks(ItemStack stack, IPowerModule module) {
        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
        NBTTagCompound moduleTag = itemTag.getCompoundTag(module.getDataName());
        propertyStrings = new HashMap();
        tweaks = new HashMap<>();

        Map<String, List<IPropertyModifier>> propertyModifiers = module.getPropertyModifiers();
        for (Map.Entry<String, List<IPropertyModifier>> property : propertyModifiers.entrySet()) {
            Double curDoubleVal = null;
            Integer currIntVal = null;

            for (IPropertyModifier modifier : property.getValue()) {
                // Double
                if (modifier instanceof IPropertyModifierDouble) {
                    curDoubleVal = (double) modifier.applyModifier(moduleTag, curDoubleVal != null ? curDoubleVal : 0);
                    if (modifier instanceof PropertyModifierLinearAdditiveDouble) {
                        tweaks.put(((PropertyModifierLinearAdditiveDouble) modifier).getTradeoffName(), Double.class);
                    }

                    // Integer
                }
//                else if (modifier instanceof IPropertyModifierInteger) {
//                    currIntVal = (int) modifier.applyModifier(moduleTag, currIntVal != null ? currIntVal : 0);
//                    if (modifier instanceof PropertyModifierLinearAdditiveInteger) {
//                        tweaks.put(((PropertyModifierLinearAdditiveInteger) modifier).getTradeoffName(), Integer.class);
//                    }
//                }
            }
            if (curDoubleVal != null) {
                propertyStrings.put(property.getKey(), curDoubleVal);
            }

            if (currIntVal != null) {
                propertyStrings.put(property.getKey(), currIntVal);
            }
        }

        sliders = new LinkedList();
        int y = 0;

        for (String tweak : tweaks.keySet()) {
            y += 20;
            MusePoint2D center = new MusePoint2D((border.left() + border.right()) / 2, border.top() + y);
            ClickableTinkerSlider slider = new ClickableTinkerSlider(
                    center,
                    tweaks.get(tweak).getName().equals("Double"),
                    border.right() - border.left() - 8,
                    moduleTag, tweak);
            sliders.add(slider);
            if (selectedSlider != null && slider.hitBox(center.x(), center.y())) {
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
            IPowerModule module = moduleTarget.getSelectedModule().getModule();
            double tweakValue = MuseMathUtils.clampDouble(selectedSlider.getValue(), 0, 1);

            if (tweaks.containsKey(selectedSlider.name())) {
                MusePacket tweakRequest = null;
                if ( tweaks.get(selectedSlider.name()).equals(Double.class) ) {
                    tweakRequest = new MusePacketTweakRequestDouble(player, item.inventorySlot, module.getDataName(), selectedSlider.name(), tweakValue);
                } else if ( tweaks.get(selectedSlider.name()).equals(Integer.class) ) {
//                    tweakRequest = new MusePacketTweakRequestInteger(player, item.inventorySlot, module.getDataName(), selectedSlider.name(), (int) (tweakValue * 10000));
                }

                if (tweakRequest != null)
                    PacketSender.sendToServer(tweakRequest.getPacket131());
            }
        }
        if (button == 0) {
            selectedSlider = null;
        }
    }
}