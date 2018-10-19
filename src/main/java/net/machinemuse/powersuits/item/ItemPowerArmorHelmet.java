package net.machinemuse.powersuits.item;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.IArmorNaturalist;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.powermodule.armor.ApiaristArmorModule;
import net.machinemuse.powersuits.powermodule.misc.AirtightSealModule;
import net.machinemuse.powersuits.powermodule.misc.ThaumGogglesModule;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
@Optional.InterfaceList({
        @Optional.Interface (iface = "forestry.api.core.IArmorNaturalist", modid = "Forestry", striprefs = true),
        @Optional.Interface (iface = "thaumcraft.api.IGoggles", modid = "Thaumcraft", striprefs = true),
        @Optional.Interface (iface = "thaumcraft.api.nodes.IRevealer", modid = "Thaumcraft", striprefs = true),
        @Optional.Interface (iface = "micdoodle8.mods.galacticraft.api.item.IBreathableArmor", modid = "GalacticraftCore", striprefs = true)
        })
public class ItemPowerArmorHelmet extends ItemPowerArmor implements
        IArmorNaturalist,
        IBreathableArmor,
        IGoggles,
        IRevealer {
    static final String iconpath = MuseRenderer.ICON_PREFIX + "armorhead";

    public ItemPowerArmorHelmet() {
        super(0, 0);
        setUnlocalizedName("powerArmorHelmet");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(iconpath);
    }

    @Optional.Method(modid = "Thaumcraft")
    @Override
    public boolean showIngamePopups(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return ModuleManager.itemHasActiveModule(itemStack, ThaumGogglesModule.MODULE_THAUM_GOGGLES);
    }

    @Override
    public boolean showNodes(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return ModuleManager.itemHasActiveModule(itemStack, ThaumGogglesModule.MODULE_THAUM_GOGGLES);
    }

    @Optional.Method(modid = "Forestry")
    @Override
    public boolean canSeePollination(EntityPlayer entityPlayer, ItemStack itemStack, boolean b) {
        return ModuleManager.itemHasActiveModule(itemStack, ApiaristArmorModule.MODULE_APIARIST_ARMOR);
    }

    @Optional.Method(modid = "GalacticraftCore")
    @Override
    public boolean handleGearType(EnumGearType gearType) {
        return gearType == EnumGearType.HELMET;
    }

    @Optional.Method(modid = "GalacticraftCore")
    @Override
    public boolean canBreathe(ItemStack itemStack, EntityPlayer entityPlayer, EnumGearType enumGearType) {
        return ModuleManager.itemHasActiveModule(itemStack, AirtightSealModule.AIRTIGHT_SEAL_MODULE);
    }
}