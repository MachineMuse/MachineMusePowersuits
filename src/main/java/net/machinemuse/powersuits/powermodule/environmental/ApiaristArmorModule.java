package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
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
    public static final String MODULE_APIARIST_ARMOR = "Apiarist Armor";
    public static final String APIARIST_ARMOR_ENERGY_CONSUMPTION = "Apiarist Armor Energy Consumption";

    public ApiaristArmorModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ItemStack stack = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("forestry", "craftingMaterial")), 6);
        stack.setItemDamage(3);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), stack);
        addBaseProperty(APIARIST_ARMOR_ENERGY_CONSUMPTION, 10, "J");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ARMOR;
    }

    @Override
    public String getDataName() {
        return MODULE_APIARIST_ARMOR;
    }

    @Override
    public String getUnlocalizedName() {
        return "apiaristArmor";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.apiaristArmor;
    }
}