package net.machinemuse.powersuits.powermodule.mining_enhancement;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IEnchantmentModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public class FortuneModule extends PowerModuleBase implements IEnchantmentModule, IToggleableModule {
    public FortuneModule(EnumModuleTarget moduleTargetIn) {
        super(moduleTargetIn);
    }

    @Override
    public Enchantment getEnchantment() {
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return null;
    }

    @Override
    public EnumModuleCategory getCategory() {
        return null;
    }

    @Override
    public String getDataName() {
        return null;
    }
}
