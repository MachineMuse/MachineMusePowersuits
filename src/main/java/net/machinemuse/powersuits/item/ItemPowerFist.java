package net.machinemuse.powersuits.item;

//import appeng.api.implementations.items.IAEWrench;

import appeng.api.implementations.items.IAEWrench;
import cofh.api.item.IToolHammer;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItem;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItemHandler;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItemProvider;
import crazypants.enderio.api.tool.ITool;
import forestry.api.arboriculture.IToolGrafter;
import mekanism.api.IMekWrench;
import net.machinemuse.numina.api.item.IModeChangingItem;
import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.IPowerModule;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.tool.GrafterModule;
import net.machinemuse.powersuits.powermodule.tool.OmniWrenchModule;
import net.machinemuse.powersuits.powermodule.tool.RefinedStorageWirelessModule;
import net.machinemuse.powersuits.powermodule.weapon.MeleeAssistModule;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseCommonStrings;
import net.machinemuse.powersuits.utils.MuseHeatUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//import mods.railcraft.api.core.items.IToolCrowbar;


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
        @Optional.Interface(iface = "forestry.api.arboriculture.IToolGrafter", modid = "forestry", striprefs = true),
        @Optional.Interface(iface = "com.raoulvdberge.refinedstorage.api.network.item.INetworkItemProvider", modid = "refinedstorage", striprefs = true),

//        @Optional.Interface(iface = "mods.railcraft.api.core.items.IToolCrowbar", modid = "Railcraft", striprefs = true),
//        @Optional.Interface(iface = "powercrystals.minefactoryreloaded.api.IMFRHammer", modid = "MineFactoryReloaded", striprefs = true),
        @Optional.Interface(iface = "cofh.api.item.IToolHammer", modid = "cofhcore", striprefs = true),
//        @Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraft|Core", striprefs = true),
        @Optional.Interface(iface = "appeng.api.implementations.items.IAEWrench", modid = "appliedenergistics2", striprefs = true)
})
public class ItemPowerFist extends MPSItemElectricTool
        implements
        IToolGrafter,
        IToolHammer,
        INetworkItemProvider,
//        IToolCrowbar,
        IAEWrench,
//        IToolWrench,
        ITool,
        IMekWrench,
        IModularItem,
        IModeChangingItem {

    public ItemPowerFist() {
        super(0.0f, 0.0f, ToolMaterial.DIAMOND); // FIXME
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setCreativeTab(Config.getCreativeTab());
    }



    /**
     * FORGE: Overridden to allow custom tool effectiveness
     */
    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return 1.0f;
    }

    /**
     * Current implementations of this method in child classes do not use the
     * entry argument beside stack. They just raise the damage on the stack.
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entityBeingHit, EntityLivingBase entityDoingHitting) {
        if (ModuleManager.INSTANCE.itemHasActiveModule(stack, OmniWrenchModule.MODULE_OMNI_WRENCH)) {
            entityBeingHit.rotationYaw += 90.0f;
            entityBeingHit.rotationYaw %= 360.0f;
        }
        if (entityDoingHitting instanceof EntityPlayer && ModuleManager.INSTANCE.itemHasActiveModule(stack, MeleeAssistModule.MODULE_MELEE_ASSIST)) {
            EntityPlayer player = (EntityPlayer) entityDoingHitting;
            double drain = ModuleManager.INSTANCE.computeModularProperty(stack, MeleeAssistModule.PUNCH_ENERGY);
            if (ElectricItemUtils.getPlayerEnergy(player) > drain) {
                ElectricItemUtils.drainPlayerEnergy(player, drain);
                double damage = ModuleManager.INSTANCE.computeModularProperty(stack, MeleeAssistModule.PUNCH_DAMAGE);
                double knockback = ModuleManager.INSTANCE.computeModularProperty(stack, MeleeAssistModule.PUNCH_KNOCKBACK);
                DamageSource damageSource = DamageSource.causePlayerDamage(player);
                if (entityBeingHit.attackEntityFrom(damageSource, (float) (int) damage)) {
                    Vec3d lookVec = player.getLookVec();
                    entityBeingHit.addVelocity(lookVec.x * knockback, Math.abs(lookVec.y + 0.2f) * knockback, lookVec.z * knockback);
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
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            for (IPowerModule module : ModuleManager.INSTANCE.getModulesOfType(IBlockBreakingModule.class)) {
                if (ModuleManager.INSTANCE.itemHasActiveModule(stack, module.getDataName())) {
                    if (((IBlockBreakingModule)module).onBlockDestroyed(stack, worldIn, state, pos, entityLiving)) {
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
        return (float) ModuleManager.INSTANCE.computeModularProperty(itemStack, MeleeAssistModule.PUNCH_DAMAGE);
    }

    @Override
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStackIn = playerIn.getHeldItem(handIn);

        // Only one right click module should be active at a time.
        IPowerModule iPowerModulemodule = ModuleManager.INSTANCE.getModule(getActiveMode(itemStackIn));
        if (iPowerModulemodule instanceof IRightClickModule) {
            return ((IRightClickModule) iPowerModulemodule).onItemRightClick(itemStackIn, worldIn, playerIn, handIn);
        }
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    /**
     * returns the action that specifies what animation to play when the items
     * is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    /**
     * Called when the right click button is released
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        String mode = this.getActiveMode(itemStack);
        IPowerModule module = ModuleManager.INSTANCE.getModule(mode);
        if (module != null)
            ((IRightClickModule)module).onPlayerStoppedUsing(itemStack, worldIn, entityLiving, timeLeft);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        String mode = this.getActiveMode(itemStack);
        IPowerModule module = ModuleManager.INSTANCE.getModule(mode);
        if (module instanceof IRightClickModule)
            return ((IRightClickModule)module).onItemUseFirst(itemStack, player, world, pos, side, hitX, hitY, hitZ, hand);
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        String mode = this.getActiveMode(itemStack);
        IPowerModule module2;
        IPowerModule module = module2 = ModuleManager.INSTANCE.getModule(mode);
        if (module2 instanceof IRightClickModule) {
            return ((IRightClickModule)module2).onItemUse(itemStack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        return EnumActionResult.PASS;
    }

    @Optional.Method(modid = "forestry")
    public float getSaplingModifier(ItemStack stack, World world, EntityPlayer player, BlockPos pos) {
        if (ModuleManager.INSTANCE.itemHasActiveModule(stack, GrafterModule.MODULE_GRAFTER)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.INSTANCE.computeModularProperty(stack, GrafterModule.GRAFTER_ENERGY_CONSUMPTION));
            MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.computeModularProperty(stack, GrafterModule.GRAFTER_HEAT_GENERATION));
            return 100.0f;
        }
        return 0.0f;
    }

    // The Item/ItemTool version doesn't give us the player, so we can't override that.
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if(state.getMaterial().isToolNotRequired())
            return true;

        for (IPowerModule module : ModuleManager.INSTANCE.getModulesOfType(IBlockBreakingModule.class)) {
            if (ModuleManager.INSTANCE.itemHasActiveModule(stack, module.getDataName()) && ((IBlockBreakingModule)module).canHarvestBlock(stack, state, player)) {
                return true;
            }
        }
        return false;
    }

    /* TE Crescent Hammer */
    @Override
    public boolean isUsable(ItemStack itemStack, EntityLivingBase entityLivingBase, Entity entity) {
        return entityLivingBase instanceof EntityPlayer && this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }
    /* TE Crescent Hammer */
    @Override
    public boolean isUsable(ItemStack itemStack, EntityLivingBase entityLivingBase, BlockPos blockPos) {
        return entityLivingBase instanceof EntityPlayer && this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* TE Crescent Hammer */
    @Override
    public void toolUsed(ItemStack itemStack, EntityLivingBase entityLivingBase, Entity entity) {

    }

    /* TE Crescent Hammer */
    @Override
    public void toolUsed(ItemStack itemStack, EntityLivingBase entityLivingBase, BlockPos blockPos) {

    }
//
//    /* Railcraft Crowbar */
//    @Override
//    public boolean canWhack(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, BlockPos blockPos) {
//        return this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
//    }
//
//    /* Railcraft Crowbar */
//    @Override
//    public boolean canLink(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
//        return this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
//    }
//
//    /* Railcraft Crowbar */
//    @Override
//    public boolean canBoost(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
//        return this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
//    }
//
//    /* Railcraft Crowbar */
//    @Override
//    public void onLink(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
//
//    }
//
//    /* Railcraft Crowbar */
//    @Override
//    public void onWhack(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, BlockPos blockPos) {
//
//    }
//
//    /* Railcraft Crowbar */
//    @Override
//    public void onBoost(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
//
//    }

    /* AE wrench */
    @Override
    public boolean canWrench(ItemStack itemStack, EntityPlayer entityPlayer, BlockPos blockPos) {
        return this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

//    /* Buildcraft Wrench */
//    @Override
//    public void wrenchUsed(EntityPlayer player, int i, int i1, int i2) {
//    }
//
//    /* Buildcraft Wrench */
//    @Override
//    public boolean canWrench(EntityPlayer player, int i, int i1, int i2) {
//        return this.getActiveMode(player.getHeldItem()).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
//    }

    /* EnderIO Tool */
    @Override
    public void used(@Nonnull EnumHand enumHand, @Nonnull EntityPlayer entityPlayer, @Nonnull BlockPos blockPos) {

    }

    /* EnderIO Tool */
    @Override
    public boolean canUse(@Nonnull EnumHand enumHand, @Nonnull EntityPlayer entityPlayer, @Nonnull BlockPos blockPos) {
        return this.getActiveMode(entityPlayer.getHeldItem(enumHand)).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* EnderIO Tool */
    @Override
    public boolean shouldHideFacades(ItemStack itemStack, EntityPlayer entityPlayer) {
        return this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    /* Mekanism Wrench */
    @Override
    public boolean canUseWrench(ItemStack itemStack, EntityPlayer entityPlayer, BlockPos blockPos) {
        return this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    @Override
    @Nonnull
    @Optional.Method(modid = "refinedstorage")
    public INetworkItem provide(INetworkItemHandler handler, EntityPlayer player, ItemStack itemStackIn) {
        return RefinedStorageWirelessModule.provide(handler, player, itemStackIn);
    }


    /* IModeChangingItem -------------------------------------------------------------------------- */
    @Nullable
    @Override
    public TextureAtlasSprite getModeIcon(String mode, ItemStack stack, EntityPlayer player) {
        IPowerModule module = ModuleManager.INSTANCE.getModule(mode);
        if (module != null)
            return module.getIcon(stack);
        return null;
    }

    @Override
    public List<String> getValidModes(ItemStack stack) {
        List<String> modes = new ArrayList<>();
        for (IPowerModule module : ModuleManager.INSTANCE.getModulesOfType(IRightClickModule.class)) {
            if (module.isValidForItem(stack))
                if (ModuleManager.INSTANCE.itemHasModule(stack, module.getDataName()))
                    modes.add(module.getDataName());
        }
        return modes;
    }
}