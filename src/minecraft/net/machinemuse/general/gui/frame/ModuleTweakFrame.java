package net.machinemuse.general.gui.frame;

import cpw.mods.fml.common.network.Player;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.IPropertyModifier;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.general.gui.clickable.ClickableTinkerSlider;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.packets.MusePacketTweakRequest;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.machinemuse.powersuits.powermodule.PropertyModifierLinearAdditive;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.render.MuseRenderer;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class ModuleTweakFrame extends ScrollableFrame {
    protected static double SCALERATIO = 0.75;
    protected ItemSelectionFrame itemTarget;
    protected ModuleSelectionFrame moduleTarget;
    protected List<ClickableTinkerSlider> sliders;
    protected Map<String, Double> propertyStrings;
    protected ClickableTinkerSlider selectedSlider;
    protected EntityClientPlayerMP player;

    public ModuleTweakFrame(
            EntityClientPlayerMP player,
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
            if (MuseItemUtils.itemHasModule(itemTarget.getSelectedItem().getItem(), moduleTarget.getSelectedModule().getModule().getName())) {
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
            GL11.glPushMatrix();
            GL11.glScaled(SCALERATIO, SCALERATIO, SCALERATIO);
            super.draw();
            MuseRenderer.drawCenteredString("Tinker", (border.left() + border.right()) / 2, border.top() + 2);
            for (ClickableTinkerSlider slider : sliders) {
                slider.draw();
            }
            int nexty = (int) (sliders.size() * 20 + border.top() + 14);
            for (Map.Entry<String, Double> property : propertyStrings.entrySet()) {
                nexty += 9;
                String[] str = {property.getKey() + ':',
                        MuseStringUtils.formatNumberFromUnits(property.getValue(), PowerModule.getUnit(property.getKey()))};
                MuseRenderer.drawStringsJustified(Arrays.asList(str), border.left() + 4, border.right() - 4, nexty);

            }
            GL11.glPopMatrix();
        }
    }

    private void loadTweaks(ItemStack stack, IPowerModule module) {
        NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        NBTTagCompound moduleTag = itemTag.getCompoundTag(module.getName());

        propertyStrings = new HashMap();
        Set<String> tweaks = new HashSet<String>();

        Map<String, List<IPropertyModifier>> propertyModifiers = module.getPropertyModifiers();
        for (Map.Entry<String, List<IPropertyModifier>> property : propertyModifiers.entrySet()) {
            double currValue = 0;
            for (IPropertyModifier modifier : property.getValue()) {
                currValue = modifier.applyModifier(moduleTag, currValue);
                if (modifier instanceof PropertyModifierLinearAdditive) {
                    tweaks.add(((PropertyModifierLinearAdditive) modifier).getTradeoffName());
                }
            }
            propertyStrings.put(property.getKey(), currValue);

        }

        sliders = new LinkedList();
        int y = 0;
        for (String tweak : tweaks) {
            y += 20;
            ClickableTinkerSlider slider = new ClickableTinkerSlider(
                    new MusePoint2D((border.left() + border.right()) / 2, border.top() + y),
                    border.right() - border.left() - 8,
                    moduleTag, tweak);
            sliders.add(slider);
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
            MusePacket tweakRequest = new MusePacketTweakRequest((Player) player, item.inventorySlot, module.getName(), selectedSlider.name(),
                    selectedSlider.value());
            player.sendQueue.addToSendQueue(tweakRequest.getPacket250());
        }
        if (button == 0) {
            selectedSlider = null;
        }
    }
}
