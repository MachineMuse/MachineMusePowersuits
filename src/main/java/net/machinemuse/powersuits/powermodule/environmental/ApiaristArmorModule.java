package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by User: Andrew
 * Date: 4/21/13
 * Time: 2:03 PM
 */
public class ApiaristArmorModule extends PowerModuleBase {
    public ApiaristArmorModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("forestry", "crafting_material")), 6, 2);
        stack.setItemDamage(3);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), stack);
        addBasePropertyDouble(MPSModuleConstants.APIARIST_ARMOR_ENERGY_CONSUMPTION, 100, "RF");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_APIARIST_ARMOR__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.apiaristArmor;
    }
}