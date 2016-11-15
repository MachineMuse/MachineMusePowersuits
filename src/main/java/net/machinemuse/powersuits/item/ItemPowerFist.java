package net.machinemuse.powersuits.item;

import appeng.api.implementations.items.IAEWrench;
import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import com.bluepowermod.api.misc.IScrewdriver;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.api.tool.ITool;
import forestry.api.arboriculture.IToolGrafter;
import mekanism.api.IMekWrench;
import mods.railcraft.api.core.items.IToolCrowbar;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.numina.item.ModeChangingItem;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.tool.GrafterModule;
import net.machinemuse.powersuits.powermodule.tool.OmniWrenchModule;
import net.machinemuse.powersuits.powermodule.weapon.MeleeAssistModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseHeatUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IMFRHammer;

import javax.annotation.Nullable;
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
public class ItemPowerFist extends MPSItemElectricTool
implements
        IToolGrafter,
        IToolHammer,
        IMFRHammer,
        IToolCrowbar,
        IAEWrench,
        IToolWrench,
        IScrewdriver,
        mrtjp.projectred.api.IScrewdriver,
        ITool, IMekWrench,
        IModularItem,
        IModeChangingModularItem
{
    private final String iconpath = MuseIcon.ICON_PREFIX + "handitem";
    public ItemPowerFist() {
        super(0.0f, Item.ToolMaterial.EMERALD);
//        IModeChangingItem$class.$init$(this);
//        IModeChangingModularItem$class.$init$(this);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setCreativeTab(Config.getCreativeTab());
        this.setUnlocalizedName("powerFist");
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base,
     * (Quality+1)*2 if correct blocktype, 1.5F if sword
     */
    public float getStrVsBlock( ItemStack stack,  Block block) {
        return this.getStrVsBlock(stack, block, 0);
    }

    /**
     * FORGE: Overridden to allow custom tool effectiveness
     */
    public float getStrVsBlock(ItemStack stack,  Block block, int meta) {
        return 1.0f;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(this.iconpath);
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
                if (entityBeingHit.attackEntityFrom(damageSource, (float)(int)damage)) {
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

    /**
     * An itemstack sensitive version of getDamageVsEntity - allows items to
     * handle damage based on
     * itemstack data, like tags. Falls back to getDamageVsEntity.
     *
     * @param par1Entity The entity being attacked (or the attacking mob, if it's a mob
     *                   - vanilla bug?)
     * @param itemStack  The itemstack
     * @return the damage
     */
    public float getDamageVsEntity(Entity par1Entity, ItemStack itemStack) {
        return (float)ModuleManager.computeModularProperty(itemStack, MeleeAssistModule.PUNCH_DAMAGE);
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    /**
     * Return the enchantability factor of the item. In this case, 0. Might add
     * an enchantability module later :P
     */
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
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
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
        String mode = this.getActiveMode(itemStack, player);
        IPowerModule module = ModuleManager.getModule(mode);
        if (module != null)
            ((IRightClickModule)module).onPlayerStoppedUsing(itemStack, world, player, par4);
    }

    public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean onItemUseFirst( ItemStack itemStack,  EntityPlayer player,  World world,  int x,  int y,  int z,  int side,  float hitX,  float hitY,  float hitZ) {
        String mode = this.getActiveMode(itemStack, player);
        IPowerModule module = ModuleManager.getModule(mode);
        return module instanceof IRightClickModule && ((IRightClickModule)module).onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUse( ItemStack itemStack,  EntityPlayer player,  World world,  int x,  int y,  int z,  int side,  float hitX,  float hitY,  float hitZ) {
         String mode = this.getActiveMode(itemStack, player);
         IPowerModule module2;
         IPowerModule module = module2 = ModuleManager.getModule(mode);
        boolean b;
        if (module2 instanceof IRightClickModule) {
            ((IRightClickModule)module2).onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
            b = false;
        }
        else {
            b = false;
        }
        return b;
    }

    @Optional.Method(modid = "Forestry")
    public float getSaplingModifier(ItemStack stack, World world, EntityPlayer player, int x, int y, int z) {
        float n;
        if (ModuleManager.itemHasActiveModule(stack, GrafterModule.MODULE_GRAFTER)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, GrafterModule.GRAFTER_ENERGY_CONSUMPTION));
            MuseHeatUtils.heatPlayer(player, ModuleManager.computeModularProperty(stack, GrafterModule.GRAFTER_HEAT_GENERATION));
            n = 100.0f;
        }
        else {
            n = 0.0f;
        }
        return n;
    }

    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        Object o = new Object();
        if (block.getMaterial().isToolNotRequired())
            return true;

        for (IBlockBreakingModule module : ModuleManager.getBlockBreakingModules()) {
            if (ModuleManager.itemHasActiveModule(stack, module.getDataName()) && module.canHarvestBlock(stack, block, meta, player)) {
                return true;
            }
        }
        return false;
    }

    /* TE Crescent Hammer */
    @Override
    public boolean isUsable(ItemStack itemStack, EntityLivingBase entityLivingBase, int i, int i1, int i2) {
        return entityLivingBase instanceof EntityPlayer && this.getActiveMode(itemStack, (EntityPlayer)entityLivingBase).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* TE Crescent Hammer */
    @Override
    public void toolUsed(ItemStack itemStack, EntityLivingBase entityLivingBase, int i, int i1, int i2) {
    }

    /* Railcraft Crowbar */
    @Override
    public boolean canWhack(EntityPlayer player, ItemStack itemStack, int i, int i1, int i2) {
        return this.getActiveMode(itemStack, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Railcraft Crowbar */
    @Override
    public boolean canLink(EntityPlayer player, ItemStack itemStack, EntityMinecart entityMinecart) {
        return this.getActiveMode(itemStack, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Railcraft Crowbar */
    @Override
    public boolean canBoost(EntityPlayer player, ItemStack itemStack, EntityMinecart entityMinecart) {
        return this.getActiveMode(itemStack, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Railcraft Crowbar */
    @Override
    public void onLink(EntityPlayer player, ItemStack itemStack, EntityMinecart entityMinecart) {
    }

    /* Railcraft Crowbar */
    @Override
    public void onWhack(EntityPlayer player, ItemStack itemStack, int i, int i1, int i2) {
    }

    /* Railcraft Crowbar */
    @Override
    public void onBoost(EntityPlayer player, ItemStack itemStack, EntityMinecart entityMinecart) {
    }

    /* AE wrench */
    @Override
    public boolean canWrench(ItemStack itemStack, EntityPlayer player, int i, int i1, int i2) {
        return this.getActiveMode(itemStack, player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Buildcraft Wrench */
    @Override
    public void wrenchUsed(EntityPlayer player, int i, int i1, int i2) {
    }

    /* Buildcraft Wrench */
    @Override
    public boolean canWrench(EntityPlayer player, int i, int i1, int i2) {
        return this.getActiveMode(player.getHeldItem(), player).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Bluepower Screwdriver */
    @Override
    public boolean damage(ItemStack itemStack, int i, EntityPlayer entityPlayer, boolean b) {
        return this.getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* ProjectRed Screwdriver */
    @Override
    public void damageScrewdriver(EntityPlayer entityPlayer, ItemStack itemStack) {
    }

    /* ProjectRed Screwdriver */
    @Override
    public boolean canUse(EntityPlayer entityPlayer, ItemStack itemStack) {
        return this.getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* EnderIO Tool */
    @Override
    public void used(ItemStack itemStack, EntityPlayer entityPlayer, int i, int i1, int i2) {
    }

    /* EnderIO Tool */
    @Override
    public boolean canUse(ItemStack itemStack, EntityPlayer entityPlayer, int i, int i1, int i2) {
        return this.getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* EnderIO Tool */
    @Override
    public boolean shouldHideFacades(ItemStack itemStack, EntityPlayer entityPlayer) {
        return this.getActiveMode(itemStack, entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Mekanism Wrench */
    @Override
    public boolean canUseWrench(EntityPlayer entityPlayer, int i, int i1, int i2) {
        return this.getActiveMode(entityPlayer.getHeldItem(), entityPlayer).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* A D D E D   B Y   D E C O M P I L E R ------------------------------------------------------------------------------ */
    @Override
    public void setActiveMode(ItemStack stack, String newMode) {
        ModeChangingItem.getInstance().setActiveMode(stack, newMode);
    }

    @Override
    public String getActiveMode(ItemStack stack, EntityPlayer player) {
        return ModeChangingItem.getInstance().getActiveMode(stack, player);
    }

    @Override
    public void cycleMode(ItemStack stack, EntityPlayer player, int dMode) {
        ModeChangingItem.getInstance().cycleMode(stack, player, dMode);
    }

    @Override
    public String nextMode(ItemStack stack, EntityPlayer player) {
        return ModeChangingItem.getInstance().nextMode(stack, player);
    }

    @Override
    public String prevMode(ItemStack stack, EntityPlayer player) {
        return ModeChangingItem.getInstance().prevMode(stack, player);
    }

    @Nullable
    @Override
    public IIcon getModeIcon(String mode, ItemStack stack, EntityPlayer player) {
        return ModeChangingModularItem.getInstance().getModeIcon(mode, stack, player);
    }

    @Override
    public List<String> getValidModes(ItemStack stack, EntityPlayer player) {
        return ModeChangingModularItem.getInstance().getValidModes(stack, player);
    }

    @Override
    public List<String> getValidModes(ItemStack stack) {
        return ModeChangingModularItem.getInstance().getValidModes(stack);
    }

    @Override
    public String getActiveMode(ItemStack stack) {
        return ModeChangingModularItem.getInstance().getActiveMode(stack);
    }


    /* IModularItem ------------------------------------------------------------------------------- */
    @Override
    public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
        return null;
    }

    @Override
    public double getPlayerEnergy(EntityPlayer player) {
        return ElectricItemUtils.getPlayerEnergy(player);
    }

    @Override
    public void drainPlayerEnergy(EntityPlayer player, double drainAmount) {
        ElectricItemUtils.drainPlayerEnergy(player, drainAmount);
    }

    @Override
    public void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
        ElectricItemUtils.givePlayerEnergy(player, joulesToGive);
    }
}