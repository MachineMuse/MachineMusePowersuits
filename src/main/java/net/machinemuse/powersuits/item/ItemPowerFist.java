package net.machinemuse.powersuits.item;

import appeng.api.implementations.items.IAEWrench;
import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.api.tool.ITool;
import forestry.api.arboriculture.IToolGrafter;
import mekanism.api.IMekWrench;
import mods.railcraft.api.core.items.IToolCrowbar;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.numina.item.NuminaItemUtils;
import net.machinemuse.numina.network.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.tool.GrafterModule;
import net.machinemuse.powersuits.powermodule.tool.OmniWrenchModule;
import net.machinemuse.powersuits.powermodule.weapon.MeleeAssistModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseHeatUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IMFRHammer;

import java.util.List;

/**
 * Describes the modular power tool.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 10/26/16.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "mekanism.api.IMekWrench", modid = "Mekanism", striprefs = true),
        @Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "EnderIO", striprefs = true),
        @Optional.Interface(iface = "mrtjp.projectred.api.IScrewdriver", modid = "ProjRed|Core", striprefs = true),
        @Optional.Interface(iface = "com.bluepowermod.api.misc.IScrewdriver", modid = "bluepower", striprefs = true),
        @Optional.Interface(iface = "forestry.api.arboriculture.IToolGrafter", modid = "Forestry", striprefs = true),
        @Optional.Interface(iface = "mods.railcraft.api.core.items.IToolCrowbar", modid = "Railcraft", striprefs = true),
        @Optional.Interface(iface = "powercrystals.minefactoryreloaded.api.IMFRHammer", modid = "MineFactoryReloaded", striprefs = true),
        @Optional.Interface(iface = "cofh.api.item.IToolHammer", modid = "CoFHCore", striprefs = true),
        @Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraft|Core", striprefs = true),
        @Optional.Interface(iface = "appeng.api.implementations.items.IAEWrench", modid = "appliedenergistics2", striprefs = true) })
public class ItemPowerFist extends MPSItemElectricTool implements
        IModularItem,
        IToolGrafter,
        IToolHammer,
        IMFRHammer,
        IToolCrowbar,
        IAEWrench,
        IToolWrench,
        com.bluepowermod.api.misc.IScrewdriver,
        mrtjp.projectred.api.IScrewdriver,
        ITool,
        IMekWrench,
        IModeChangingModularItem
{
    public final String iconpath = MuseIcon.ICON_PREFIX + "handitem";

    public ItemPowerFist() {
        super(0, ToolMaterial.EMERALD);
        setMaxStackSize(1);
        setMaxDamage(0);
        setCreativeTab(Config.getCreativeTab());
        setUnlocalizedName("powerFist");
    }


    // FIXME: CHECKME!! these 2 were not overridden in 1.7.10 but should have been according to the comments.
    /**
     * Returns the strength of the stack against a given block. 1.0F base,
     * (Quality+1)*2 if correct blocktype, 1.5F if sword
     * this is actually "getStrVsBlock"
     */
    @Override
    public float func_150893_a( ItemStack stack,  Block block) {
        return 1.0F;
    }

    /**
     * FORGE: Overridden to allow custom tool effectiveness
     *
     * This is actually getStrVsBlock
     */
    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return this.func_150893_a(stack, block);
    }

    // END CHECKME!! ===================================================================


    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(iconpath);
    }

    /**
     * Current implementations of this method in child classes do not use the
     * entry argument beside stack. They just raise the damage on the stack.
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entityBeingHit, EntityLivingBase entityDoingHitting) {
        if (ModuleManager.itemHasActiveModule(stack, OmniWrenchModule.MODULE_OMNI_WRENCH)) {
            entityBeingHit.rotationYaw += 90.0f;
            entityBeingHit.rotationYaw %= 360.0f;
        }
        if (entityDoingHitting instanceof EntityPlayer && ModuleManager.itemHasActiveModule(stack, MeleeAssistModule.MODULE_MELEE_ASSIST)) {
            EntityPlayer player = (EntityPlayer)entityDoingHitting;
            double drain = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_ENERGY);
            if (ElectricItemUtils.getPlayerEnergy(player) > drain) {
                ElectricItemUtils.drainPlayerEnergy(player, drain);
                double damage = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_DAMAGE);
                double knockback = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_KNOCKBACK);
                DamageSource damageSource = DamageSource.causePlayerDamage(player);
                if (entityBeingHit.attackEntityFrom(damageSource, (int)damage)) {
                    Vec3 lookVec = player.getLookVec();
                    entityBeingHit.addVelocity(lookVec.xCoord * knockback, Math.abs(lookVec.yCoord + 0.2f) * knockback, lookVec.zCoord * knockback);
                }
            }
        }
        return true;
    }

    /**
     * Called when a block is destroyed using this tool.
     * <p/>
     * Returns: Whether to increment player use stats with this item
     */
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            for (IBlockBreakingModule module : ModuleManager.getBlockBreakingModules()) {
                if (ModuleManager.itemHasActiveModule(stack, module.getDataName())) {
                    if (module.onBlockDestroyed(stack, world, block, x, y, z, (EntityPlayer) entity)) {
                        return true;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_DAMAGE), 0));
        return multimap;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }

    /**
     * Return the enchantability factor of the item. In this case, 0. Might add
     * an enchantability module later :P
     */
    @Override
    public int getItemEnchantability() {
        return 0;
    }

    /**
     * Return the name for this tool's material.
     */
    @Override
    public String getToolMaterialName() {
        return this.toolMaterial.toString();
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack par1stack, ItemStack par2stack) {
        return false;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    /**
     * Called when the right click button is pressed
     */
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        for (IRightClickModule module : ModuleManager.getRightClickModules()) {
            if (module.isValidForItem(itemStack) && ModuleManager.itemHasActiveModule(itemStack, module.getDataName())) {
                module.onRightClick(player, world, itemStack);
            }
        }
        return itemStack;
    }

    /**
     * returns the action that specifies what animation to play when the items
     * is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    /**
     * Called when the right click button is released
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
        String mode = getActiveMode(itemStack, player);
        IPowerModule module = ModuleManager.getModule(mode);
        if (module != null)
            ((IRightClickModule)module).onPlayerStoppedUsing(itemStack, world, player, par4);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        String mode = getActiveMode(itemStack, player);
        IPowerModule module = ModuleManager.getModule(mode);
        return module instanceof IRightClickModule && ((IRightClickModule)module).onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        String mode = getActiveMode(itemStack, player);
        IPowerModule module = ModuleManager.getModule(mode);
        if (module instanceof IRightClickModule)
            return ((IRightClickModule) module).onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        return false;
    }

    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        if (block.getMaterial().isToolNotRequired())
            return true;

        for (IBlockBreakingModule module : ModuleManager.getBlockBreakingModules()) {
            if (ModuleManager.itemHasActiveModule(stack, module.getDataName()) && module.canHarvestBlock(stack, block, meta, player)) {
                return true;
            }
        }
        return false;
    }

    @Optional.Method(modid = "Forestry")
    @Override
    public float getSaplingModifier(ItemStack itemStack, World world, EntityPlayer entityPlayer, int x, int y, int z) {
        if (ModuleManager.itemHasActiveModule(itemStack, GrafterModule.MODULE_GRAFTER)) {
            ElectricItemUtils.drainPlayerEnergy(entityPlayer, ModuleManager.computeModularProperty(itemStack, GrafterModule.GRAFTER_ENERGY_CONSUMPTION));
            MuseHeatUtils.heatPlayer(entityPlayer, ModuleManager.computeModularProperty(itemStack, GrafterModule.GRAFTER_HEAT_GENERATION));
            return 100.0f;
        }
        return 0.0f;
    }

    /* TE Crescent Hammer */
    @Override
    public boolean isUsable(ItemStack itemStack, EntityLivingBase entityLivingBase, int i, int i1, int i2) {
        return entityLivingBase instanceof EntityPlayer && this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* TE Crescent Hammer */
    @Optional.Method(modid = "CoFHCore")
    @Override
    public void toolUsed(ItemStack itemStack, EntityLivingBase entityLivingBase, int i, int i1, int i2) {}

    /* Railcraft Crowbar */
    @Optional.Method(modid = "Railcraft")
    @Override
    public boolean canWhack(EntityPlayer entityPlayer, ItemStack itemStack, int i, int i1, int i2) {
        return getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Railcraft Crowbar */
    @Optional.Method(modid = "Railcraft")
    @Override
    public boolean canLink(EntityPlayer entityPlayer, ItemStack itemStack, EntityMinecart entityMinecart) {
        return getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Railcraft Crowbar */
    @Optional.Method(modid = "Railcraft")
    @Override
    public boolean canBoost(EntityPlayer entityPlayer, ItemStack itemStack, EntityMinecart entityMinecart) {
        return getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Railcraft Crowbar */
    @Optional.Method(modid = "Railcraft")
    @Override
    public void onLink(EntityPlayer entityPlayer, ItemStack itemStack, EntityMinecart entityMinecart) {}

    /* Railcraft Crowbar */
    @Optional.Method(modid = "Railcraft")
    @Override
    public void onWhack(EntityPlayer entityPlayer, ItemStack itemStack, int i, int i1, int i2) {}

    /* Railcraft Crowbar */
    @Optional.Method(modid = "Railcraft")
    @Override
    public void onBoost(EntityPlayer entityPlayer, ItemStack itemStack, EntityMinecart entityMinecart) {}

    /* AE wrench */
    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public boolean canWrench(ItemStack itemStack, EntityPlayer entityPlayer, int i, int i1, int i2) {
        return getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Buildcraft Wrench */
    @Optional.Method(modid = "BuildCraft|Core")
    @Override
    public void wrenchUsed(EntityPlayer entityPlayer, int i, int i1, int i2) {}

    /* Buildcraft Wrench */
    @Optional.Method(modid = "BuildCraft|Core")
    @Override
    public boolean canWrench(EntityPlayer entityPlayer, int i, int i1, int i2) {
        return getActiveMode(entityPlayer.getHeldItem(), entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Bluepower Screwdriver */
    @Optional.Method(modid = "ProjRed|Core")
    @Override
    public boolean damage(ItemStack itemStack, int i, EntityPlayer entityPlayer, boolean b) {
        return getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* ProjectRed Screwdriver */
    @Optional.Method(modid = "ProjRed|Core")
    @Override
    public void damageScrewdriver(EntityPlayer entityPlayer, ItemStack itemStack) {}

    /* ProjectRed Screwdriver */
    @Optional.Method(modid = "ProjRed|Core")
    @Override
    public boolean canUse(EntityPlayer entityPlayer, ItemStack itemStack) {
        return getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* EnderIO Tool */
    @Optional.Method(modid = "EnderIO")
    @Override
    public void used(ItemStack itemStack, EntityPlayer entityPlayer, int i, int i1, int i2) {}

    /* EnderIO Tool */
    @Optional.Method(modid = "EnderIO")
    @Override
    public boolean canUse(ItemStack itemStack, EntityPlayer entityPlayer, int i, int i1, int i2) {
        return getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* EnderIO Tool */
    @Optional.Method(modid = "EnderIO")
    @Override
    public boolean shouldHideFacades(ItemStack itemStack, EntityPlayer entityPlayer) {
        return getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Mekanism Wrench */
    @Optional.Method(modid = "Mekanism")
    @Override
    public boolean canUseWrench(EntityPlayer entityPlayer, int i, int i1, int i2) {
        return getActiveMode(entityPlayer.getHeldItem(), entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* IModeChangingItem -------------------------------------------------------------------------- */
    @Override
    public void setActiveMode(ItemStack itemStack, String newMode) {
        NuminaItemUtils.getTagCompound(itemStack).setString("mode", newMode);
    }

    @Override
    public String getActiveMode(ItemStack itemStack, EntityPlayer player) {
        return getActiveMode(itemStack);
    }

    @Override
    public void cycleMode(ItemStack itemStack, EntityPlayer player, int dMode) {
        List<String> modes = getValidModes(itemStack, player);
        if (!modes.isEmpty()) {
            int newindex = clampMode(modes.indexOf(getActiveMode(itemStack, player)) + dMode, modes.size());
            String newmode = modes.get(newindex);
            setActiveMode(itemStack, newmode);
            PacketSender.sendToServer(new MusePacketModeChangeRequest(player,newmode, player.inventory.currentItem));
        }
    }

    @Override
    public String nextMode(ItemStack itemStack, EntityPlayer player) {
        List<String> modes = getValidModes(itemStack, player);
        if (!modes.isEmpty()) {
            int newindex = clampMode(modes.indexOf(getActiveMode(itemStack, player)) + 1, modes.size());
            return modes.get(newindex);
        } else {
            return "";
        }
    }

    @Override
    public String prevMode(ItemStack itemStack, EntityPlayer player) {
        List<String> modes = getValidModes(itemStack, player);
        if (!modes.isEmpty()) {
            int newindex = clampMode(modes.indexOf(getActiveMode(itemStack, player)) - 1, modes.size());
            return modes.get(newindex);
        } else {
            return "";
        }
    }

    @Override
    public List<String> getValidModes(ItemStack itemStack, EntityPlayer player) {
        return getValidModes(itemStack);
    }

    private int clampMode(int selection, int modesSize) {
        if (selection > 0) {
            return selection % modesSize;
        } else {
            return (selection + modesSize * (-selection)) % modesSize;
        }
    }

    /* IModeChangingModularItem ------------------------------------------------------------------- */
    @Override
    public void cycleModeForItem(ItemStack itemStack, EntityPlayer player, int dMode) {
        if (itemStack != null) {
            this.cycleMode(itemStack, player, dMode);
        }
    }

    @Override
    public IIcon getModeIcon(String mode, ItemStack itemStack, EntityPlayer player) {
        if (!mode.isEmpty())
            return ModuleManager.getModule(mode).getIcon(itemStack);
        return null;
    }

    @Override
    public List<String> getValidModes(ItemStack itemStack) {
        return ModuleManager.getValidModes(itemStack);
    }

    @Override
    public String getActiveMode(ItemStack itemStack) {
        String modeFromNBT = NuminaItemUtils.getTagCompound(itemStack).getString("mode");
        if (!modeFromNBT.isEmpty()) {
            return modeFromNBT;
        } else {
            List<String> validModes = getValidModes(itemStack);
            if (!validModes.isEmpty()) {
                return validModes.get(0);
            } else {
                return "";
            }
        }
    }
}
