package net.machinemuse.powersuits.item;

import forestry.api.core.IArmorNaturalist;
//import micdoodle8.mods.galacticraft.api.item.IBreathableArmor;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.powermodule.armor.ApiaristArmorModule;
import net.machinemuse.powersuits.powermodule.misc.AirtightSealModule;
import net.machinemuse.powersuits.powermodule.misc.ThaumGogglesModule;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
//import thaumcraft.api.IGoggles;
//import thaumcraft.api.nodes.IRevealer;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "forestry.api.core.IArmorNaturalist", modid = "Forestry", striprefs = true)//,
//        @Optional.Interface(iface = "thaumcraft.api.IGoggles", modid = "Thaumcraft", striprefs = true),
//        @Optional.Interface(iface = "thaumcraft.api.nodes.IRevealer", modid = "Thaumcraft", striprefs = true),
//        @Optional.Interface(iface = "micdoodle8.mods.galacticraft.api.item.IBreathableArmor", modid = "GalacticraftCore", striprefs = true)
})
public class ItemPowerArmorHelmet extends ItemPowerArmor implements IArmorNaturalist
//        , IBreathableArmor, IGoggles, IRevealer
{
    private final String iconpath;
    public ItemPowerArmorHelmet() {
        super(0, 0);
        this.iconpath = MuseRenderer.ICON_PREFIX + "armorhead";
        this.setUnlocalizedName("powerArmorHelmet");
    }

//    public boolean showIngamePopups(final ItemStack itemstack, final EntityLivingBase player) {
//        return ModuleManager.itemHasActiveModule(itemstack, ThaumGogglesModule.MODULE_THAUM_GOGGLES);
//    }
//
//    public boolean showNodes(final ItemStack itemstack, final EntityLivingBase player) {
//        return ModuleManager.itemHasActiveModule(itemstack, ThaumGogglesModule.MODULE_THAUM_GOGGLES);
//    }

    @Optional.Method(modid = "Forestry")
    public boolean canSeePollination(final EntityPlayer player, final ItemStack helm, final boolean doSee) {
        return ModuleManager.itemHasActiveModule(helm, ApiaristArmorModule.MODULE_APIARIST_ARMOR);
    }
//
//    @Optional.Method(modid = "GalacticraftCore")
//    public boolean handleGearType(final IBreathableArmor.EnumGearType geartype) {
//        return geartype == EnumGearType.HELMET;
//    }
//
//    @Optional.Method(modid = "GalacticraftCore")
//    public boolean canBreathe(final ItemStack helm, final EntityPlayer player, final IBreathableArmor.EnumGearType geartype) {
//        return ModuleManager.itemHasActiveModule(helm, AirtightSealModule.AIRTIGHT_SEAL_MODULE);
//    }
}