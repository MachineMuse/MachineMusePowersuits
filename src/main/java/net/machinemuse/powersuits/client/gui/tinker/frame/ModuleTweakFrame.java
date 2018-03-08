package net.machinemuse.powersuits.client.gui.tinker.frame;

import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.IModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.nbt.*;
import net.machinemuse.numina.math.geometry.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.utils.render.MuseRenderer;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.machinemuse.powersuits.client.gui.tinker.clickable.ClickableItem;
import net.machinemuse.powersuits.client.gui.tinker.clickable.ClickableTinkerSlider;
import net.machinemuse.powersuits.network.packets.MusePacketTweakRequestDouble;
import net.machinemuse.powersuits.network.packets.MusePacketTweakRequestInteger;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.entity.EntityPlayerSP;
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
    protected Map<String, Double> propertyStringsDouble;
    protected Map<String, Integer> propertyStringsInt;
    protected ClickableTinkerSlider selectedSlider;
    protected EntityPlayerSP player;

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
            ItemStack module = moduleTarget.getSelectedModule().getModule();
            if (ModuleManager.getInstance().itemHasModule(itemTarget.getSelectedItem().getItem(), moduleTarget.getSelectedModule().getModule().getUnlocalizedName())) {
                loadTweaks(stack, module);
            } else {
                sliders = null;
                propertyStringsDouble = null;
                propertyStringsInt= null;
            }
        } else {
            sliders = null;
            propertyStringsDouble = null;
            propertyStringsInt = null;
        }
        if (selectedSlider != null) {
            selectedSlider.value();
            System.out.println("value: " + selectedSlider.value());

            selectedSlider.moveSlider(mousex, mousey);
        }
    }

    @Override
    public void draw() {
        if (sliders != null) {
            GL11.glPushMatrix();
            GL11.glScaled(SCALERATIO, SCALERATIO, SCALERATIO);
            super.draw();
            MuseRenderer.drawCenteredString("Tinker", (border.left() + border.right()) / 2, border.top() + 2);
            for (ClickableTinkerSlider slider : sliders) {
                slider.draw();
            }
            int nexty = (int) (sliders.size() * 20 + border.top() + 23);
            for (Map.Entry<String, Double> property : propertyStringsDouble.entrySet()) {
                String formattedValue = MuseStringUtils.formatNumberFromUnits(property.getValue(), PowerModuleBase.getUnit(property.getKey()));
                String name = property.getKey();
                double valueWidth = MuseRenderer.getStringWidth(formattedValue);
                double allowedNameWidth = border.width() - valueWidth - margin * 2;
                List<String> namesList = MuseStringUtils.wrapStringToVisualLength(name, allowedNameWidth);
                for(int i=0; i<namesList.size();i++) {
                    MuseRenderer.drawString(namesList.get(i), border.left() + margin, nexty + 9*i);
                }
                MuseRenderer.drawRightAlignedString(formattedValue, border.right() - margin, nexty + 9 * (namesList.size()-1)/2);
                nexty += 9*namesList.size()+1;

            }

            for (Map.Entry<String, Integer> property : propertyStringsInt.entrySet()) {
                String formattedValue = MuseStringUtils.formatNumberFromUnits(property.getValue(), PowerModuleBase.getUnit(property.getKey()));
                String name = property.getKey();
                double valueWidth = MuseRenderer.getStringWidth(formattedValue);
                double allowedNameWidth = border.width() - valueWidth - margin * 2;
                List<String> namesList = MuseStringUtils.wrapStringToVisualLength(name, allowedNameWidth);
                for(int i=0; i<namesList.size();i++) {
                    MuseRenderer.drawString(namesList.get(i), border.left() + margin, nexty + 9*i);
                }
                MuseRenderer.drawRightAlignedString(formattedValue, border.right() - margin, nexty + 9 * (namesList.size()-1)/2);
                nexty += 9*namesList.size()+1;
            }
            GL11.glPopMatrix();
        }
    }

    private void loadTweaks(ItemStack stack, ItemStack module) {
        NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
        NBTTagCompound moduleTag = itemTag.getCompoundTag(module.getUnlocalizedName());

        propertyStringsDouble = new HashMap();
        propertyStringsInt = new HashMap<>();
        Set<String> tweaks = new HashSet<>();
        Map<String, List<IPropertyModifier>> propertyModifiers = ((IModule)module.getItem()).getPropertyModifiers();
        for (Map.Entry<String, List<IPropertyModifier>> property : propertyModifiers.entrySet()) {
            double currValueDouble = 0;
            for (IPropertyModifier modifier : property.getValue()) {
                if (modifier instanceof IPropertyModifierDouble) {
                    currValueDouble = (double) modifier.applyModifier(moduleTag, currValueDouble);
                    if (modifier instanceof PropertyModifierLinearAdditiveDouble) {
                        tweaks.add(((PropertyModifierLinearAdditiveDouble) modifier).getTradeoffName());
                    }
                }
            }
            propertyStringsDouble.put(property.getKey(), currValueDouble);
        }

        for (Map.Entry<String, List<IPropertyModifier>> property : propertyModifiers.entrySet()) {
            int currValueInt = 0;
            for (IPropertyModifier modifier : property.getValue()) {
                if (modifier instanceof IPropertyModifierInteger) {
                    currValueInt = (int) modifier.applyModifier(moduleTag, currValueInt);
                    if (modifier instanceof PropertyModifierLinearAdditiveInteger) {
                        tweaks.add(((PropertyModifierLinearAdditiveInteger) modifier).getTradeoffName());
                    }
                }
            }
            propertyStringsInt.put(property.getKey(), currValueInt);
        }

        sliders = new LinkedList();
        int y = 0;
        for (String tweak : tweaks) {
//            if (propertyStringsInt.containsKey(tweak)) {
                System.out.println("tweak: " + tweak);
                System.out.println("x: " + ((border.left() + border.right()) / 2));

//            }

            y += 20;
            MusePoint2D center = new MusePoint2D((border.left() + border.right()) / 2, border.top() + y);
            ClickableTinkerSlider slider = new ClickableTinkerSlider(
                    center,
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
            ItemStack module = moduleTarget.getSelectedModule().getModule();
            String tweakName = selectedSlider.name();

            // TODO:
            MusePacket tweakRequest;
            if (propertyStringsDouble!= null && propertyStringsDouble.containsKey(tweakName)) {
                tweakRequest = new MusePacketTweakRequestDouble(player, item.inventorySlot, module.getUnlocalizedName(), selectedSlider.name(), selectedSlider.value());
            } else {
                 //for now this is scaling the value up so the value is 3 decimal places to the left before casting as an integer. This allows the ability to preserve some semblance of precision without using decimals.
                tweakRequest = new MusePacketTweakRequestInteger(player, item.inventorySlot, module.getUnlocalizedName(), selectedSlider.name(), (int) Math.round(1000*selectedSlider.value()));
            }

            PacketSender.sendToServer(tweakRequest.getPacket131());
        }
        if (button == 0) {
            selectedSlider = null;
        }
    }
}