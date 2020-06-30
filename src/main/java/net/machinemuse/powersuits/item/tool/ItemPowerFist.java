package net.machinemuse.powersuits.item.tool;

import appeng.api.implementations.items.IAEWrench;
import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import com.google.common.collect.Multimap;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItem;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItemHandler;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItemProvider;
import crazypants.enderio.api.tool.ITool;
import forestry.api.arboriculture.IToolGrafter;
import mekanism.api.IMekWrench;
import net.machinemuse.numina.constants.NuminaNBTConstants;
import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.heat.MuseHeatUtils;
import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.numina.module.IMiningEnhancementModule;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.capabilities.MPSCapProvider;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.powermodule.tool.RefinedStorageWirelessModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

//import mods.railcraft.api.core.items.IToolCrowbar;


/**
 * Describes the modular power tool.
 *
 * @author MachineMuse
 * <p>
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
        @Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraft|Core", striprefs = true),
        @Optional.Interface(iface = "appeng.api.implementations.items.IAEWrench", modid = "appliedenergistics2", striprefs = true)
})
public class ItemPowerFist extends MPSItemElectricTool
        implements
        IToolGrafter,
        IToolHammer,
        INetworkItemProvider,
//        IToolCrowbar,
        IAEWrench,
        IToolWrench,
        ITool,
        IMekWrench,
        IModularItem,
        IModeChangingItem {

    public ItemPowerFist(String regName) {
        super(0.0f, 0.0f, ToolMaterial.IRON); // FIXME
        this.setRegistryName(regName);
        this.setTranslationKey(new StringBuilder(MODID).append(".").append("powerFist").toString());
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setCreativeTab(MPSConfig.INSTANCE.mpsCreativeTab);
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return oldStack.isItemEqual(newStack);
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
        if (ModuleManager.INSTANCE.itemHasActiveModule(stack, MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME)) {
            entityBeingHit.rotationYaw += 90.0f;
            entityBeingHit.rotationYaw %= 360.0f;
        }
        if (entityDoingHitting instanceof EntityPlayer && ModuleManager.INSTANCE.itemHasActiveModule(stack, MPSModuleConstants.MODULE_MELEE_ASSIST__DATANAME)) {
            EntityPlayer player = (EntityPlayer) entityDoingHitting;
            double drain = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.PUNCH_ENERGY);
            if (ElectricItemUtils.getPlayerEnergy(player) > drain) {
                ElectricItemUtils.drainPlayerEnergy(player, (int) drain);
                double damage = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.PUNCH_DAMAGE);
                double knockback = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.PUNCH_KNOCKBACK);
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
            int playerEnergy = ElectricItemUtils.getPlayerEnergy((EntityPlayer) entityLiving);
            for (IPowerModule module : ModuleManager.INSTANCE.getModulesOfType(IBlockBreakingModule.class)) {
                if (ModuleManager.INSTANCE.itemHasActiveModule(stack, module.getDataName())) {
                    if (((IBlockBreakingModule) module).onBlockDestroyed(stack, worldIn, state, pos, entityLiving, playerEnergy)) {
                        return true;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Called before a block is broken.  Return true to prevent default block harvesting.
     *
     * Note: In SMP, this is called on both client and server sides!
     *
     * @param itemstack The current ItemStack
     * @param pos Block's position in world
     * @param player The Player that is wielding the item
     * @return True to prevent harvesting, false to continue as normal
     */
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        super.onBlockStartBreak(itemstack, pos, player);
        String moduleDataName = getActiveMode(itemstack);
        IPowerModule module = ModuleManager.INSTANCE.getModule(moduleDataName);

        if (module != null && module instanceof IMiningEnhancementModule) {
            return (((IMiningEnhancementModule) module).onBlockStartBreak(itemstack, pos, player));
        }
        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack itemStack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, itemStack);

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.PUNCH_DAMAGE), 0));
        }
        return multimap;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    /**
     * Enchantments -----------------------------------------------------------------------
     */
    // TODO: for enchantment modules
    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Override
    public int getItemEnchantability() {
        return 0; // :P
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return super.getHarvestLevel(stack, toolClass, player, blockState);
    }
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    /**
     * Return the id for this tool's material.
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
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 72000;
    }

    /**
     * Called when the right click button is pressed
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStackIn = playerIn.getHeldItem(handIn);
        if (handIn == EnumHand.MAIN_HAND) {
            // Only one right click module should be active at a time.
            IPowerModule iPowerModulemodule = ModuleManager.INSTANCE.getModule(getActiveMode(itemStackIn));
            if (iPowerModulemodule instanceof IRightClickModule) {
                return ((IRightClickModule) iPowerModulemodule).onItemRightClick(itemStackIn, worldIn, playerIn, handIn);
            }
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
            ((IRightClickModule) module).onPlayerStoppedUsing(itemStack, worldIn, entityLiving, timeLeft);
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
            return ((IRightClickModule) module).onItemUseFirst(itemStack, player, world, pos, side, hitX, hitY, hitZ, hand);
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(hand == EnumHand.MAIN_HAND) {
            ItemStack itemStack = player.getHeldItem(hand);
            String mode = this.getActiveMode(itemStack);
            IPowerModule module = ModuleManager.INSTANCE.getModule(mode);
            if (module instanceof IRightClickModule) {
                return ((IRightClickModule) module).onItemUse(itemStack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            }
        }
        return EnumActionResult.PASS;
    }

    @Optional.Method(modid = "forestry")
    public float getSaplingModifier(ItemStack stack, World world, EntityPlayer player, BlockPos pos) {
        if (ModuleManager.INSTANCE.itemHasActiveModule(stack, MPSModuleConstants.MODULE_GRAFTER__DATANAME)) {
            ElectricItemUtils.drainPlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.GRAFTER_ENERGY_CONSUMPTION));
            MuseHeatUtils.heatPlayer(player, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.GRAFTER_HEAT_GENERATION));
            return 100.0f;
        }
        return 0.0f;
    }

    // The Item/ItemTool version doesn't give us the player, so we can't override that.
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player, BlockPos pos, int playerEnergy) {
        if (state.getMaterial().isToolNotRequired())
            return true;
        for (IPowerModule module : ModuleManager.INSTANCE.getModulesOfType(IBlockBreakingModule.class)) {
            if (ModuleManager.INSTANCE.itemHasActiveModule(stack, module.getDataName()) && ((IBlockBreakingModule) module).canHarvestBlock(stack, state, player, pos, playerEnergy)) {
                return true;
            }
        }
        return false;
    }

    /* TE Crescent Hammer */
    @Override
    public boolean isUsable(ItemStack itemStack, EntityLivingBase entityLivingBase, Entity entity) {
        return entityLivingBase instanceof EntityPlayer && this.getActiveMode(itemStack).equals(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME);
    }

    /* TE Crescent Hammer */
    @Override
    public boolean isUsable(ItemStack itemStack, EntityLivingBase entityLivingBase, BlockPos blockPos) {
        return entityLivingBase instanceof EntityPlayer && this.getActiveMode(itemStack).equals(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME);
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
        return this.getActiveMode(itemStack).equals(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME);
    }

    /* Buildcraft Wrench */
    @Override
    public void wrenchUsed(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, RayTraceResult rayTraceResult) {

    }

    /* Buildcraft Wrench */
    @Override
    public boolean canWrench(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, RayTraceResult rayTraceResult) {
        return this.getActiveMode(entityPlayer.getHeldItem(enumHand)).equals(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME);
    }

    /* EnderIO Tool */
    @Override
    public void used(@Nonnull EnumHand enumHand, @Nonnull EntityPlayer entityPlayer, @Nonnull BlockPos blockPos) {

    }

    /* EnderIO Tool */
    @Override
    public boolean canUse(@Nonnull EnumHand enumHand, @Nonnull EntityPlayer entityPlayer, @Nonnull BlockPos blockPos) {
        return this.getActiveMode(entityPlayer.getHeldItem(enumHand)).equals(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME);
    }

    /* EnderIO Tool */
    @Override
    public boolean shouldHideFacades(ItemStack itemStack, EntityPlayer entityPlayer) {
        return this.getActiveMode(itemStack).equals(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME);
    }

    /* Mekanism Wrench */
    @Override
    public boolean canUseWrench(ItemStack itemStack, EntityPlayer entityPlayer, BlockPos blockPos) {
        return this.getActiveMode(itemStack).equals(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME);
    }

    @Override
    public boolean canUseWrench(EntityPlayer player, EnumHand hand, ItemStack itemStack, RayTraceResult rayTrace) {
        return this.getActiveMode(itemStack).equals(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME);
    }

    @Nonnull
    @Override
    @Optional.Method(modid = "refinedstorage")
    public INetworkItem provide(INetworkItemHandler handler, EntityPlayer player, ItemStack itemStack, int slot) {
        return RefinedStorageWirelessModule.provide(handler, player, itemStack, slot);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new MPSCapProvider(stack);
    }

    @Override
    public boolean showDurabilityBar(final ItemStack stack) {
        int capacity = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, NuminaNBTConstants.MAXIMUM_ENERGY);
        if (capacity > 0) {
            return true;
        }
        return false;
    }

    @Override
    public double getDurabilityForDisplay(final ItemStack stack) {
        int capacity = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, NuminaNBTConstants.MAXIMUM_ENERGY);
        int energy = Math.min(capacity, (int) Math.round(MuseItemUtils.getDoubleOrZero(stack, NuminaNBTConstants.CURRENT_ENERGY)));
        return 1 - energy / (float) capacity;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}