package net.machinemuse.powersuits.item.tool;

import appeng.api.implementations.items.IAEWrench;
import cofh.api.item.IToolHammer;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItem;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItemHandler;
import com.raoulvdberge.refinedstorage.api.network.item.INetworkItemProvider;
import forestry.api.arboriculture.IToolGrafter;
import mekanism.api.IMekWrench;
import net.machinemuse.numina.api.capability_ports.inventory.IModeChangingItemCapability;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.numina.utils.module.helpers.WeightHelper;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.capabilities.MPSCapProvider;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.module.tool.GrafterModule;
import net.machinemuse.powersuits.item.module.tool.OmniWrenchModule;
import net.machinemuse.powersuits.item.module.tool.RefinedStorageWirelessModule;
import net.machinemuse.powersuits.item.module.weapon.MeleeAssistModule;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

//import crazypants.enderio.api.tool.ITool;

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
//        @Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "EnderIO", striprefs = true),
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
//        ITool,
        IMekWrench,
        IMuseItem,
        IModeChangingItem
{
    public ItemPowerFist() {
        super(0.0f, 0.0f, ToolMaterial.DIAMOND); // FIXME
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setCreativeTab(MPSConfig.getInstance().getCreativeTab());
        this.setUnlocalizedName("powerfist");
    }
















//    /**
//     * FORGE: Overridden to allow custom tool effectiveness
//     */
//    @Override
//    public float getStrVsBlock(ItemStack stack, IBlockState state) {
//        return 1.0f;
//    }

    /**
     * Current implementations of this method in child classes do not use the
     * entry argument beside stack. They just raise the damage on the stack.
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entityBeingHit, EntityLivingBase entityDoingHitting) {
        if (ModuleManager.getInstance().itemHasActiveModule(stack, MPSModuleConstants.MODULE_OMNI_WRENCH)) {
            entityBeingHit.rotationYaw += 90.0f;
            entityBeingHit.rotationYaw %= 360.0f;
        }
        if (entityDoingHitting instanceof EntityPlayer && ModuleManager.getInstance().itemHasActiveModule(stack, MPSModuleConstants.MODULE_MELEE_ASSIST)) {
            EntityPlayer player = (EntityPlayer) entityDoingHitting;
            int drain = ModuleManager.getInstance().computeModularPropertyInteger(stack, MeleeAssistModule.PUNCH_ENERGY);
            if (ElectricItemUtils.getPlayerEnergy(player) > drain) {
                ElectricItemUtils.drainPlayerEnergy(player, drain);
                double damage = ModuleManager.getInstance().computeModularPropertyInteger(stack, MeleeAssistModule.PUNCH_DAMAGE);
                double knockback = ModuleManager.getInstance().computeModularPropertyDouble(stack, MeleeAssistModule.PUNCH_KNOCKBACK);
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
            for (ItemStack module : ModuleManager.getInstance().getBlockBreakingModules()) {
                if (ModuleManager.getInstance().itemHasActiveModule(stack, module.getUnlocalizedName())) {
                    if (module.getItem().onBlockDestroyed(stack, worldIn, state, pos, entityLiving)) {
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
        return (float) ModuleManager.getInstance().computeModularPropertyDouble(itemStack, MeleeAssistModule.PUNCH_DAMAGE);
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        // Only one right click module should be active at a time.
        IItemHandler modeChangingCapability = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modeChangingCapability != null && modeChangingCapability instanceof IModeChangingItemCapability) {
            ItemStack module = ((IModeChangingItemCapability) modeChangingCapability).getActiveModule();
            if (module.getItem() instanceof IRightClickModule) {
                return ((IRightClickModule) module.getItem()).onItemRightClick(itemStackIn, worldIn, playerIn, hand);
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
    public void onPlayerStoppedUsing(ItemStack itemStackIn, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        IItemHandler modeChangingCapability = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modeChangingCapability != null && modeChangingCapability instanceof IModeChangingItemCapability) {
            ItemStack module = ((IModeChangingItemCapability) modeChangingCapability).getActiveModule();
            if (!module.isEmpty())
                module.getItem().onPlayerStoppedUsing(itemStackIn, worldIn, entityLiving, timeLeft);
        }
    }

    public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        IItemHandler modeChangingCapability = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modeChangingCapability != null && modeChangingCapability instanceof IModeChangingItemCapability) {
            ItemStack module = ((IModeChangingItemCapability) modeChangingCapability).getActiveModule();
            if (module.getItem() instanceof IRightClickModule)
                return ((IRightClickModule)module.getItem()).onItemUseFirst(itemStackIn, playerIn, world, pos, side, hitX, hitY, hitZ, hand);
        }
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        IItemHandler modeChangingCapability = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modeChangingCapability != null && modeChangingCapability instanceof IModeChangingItemCapability) {
            ItemStack module = ((IModeChangingItemCapability) modeChangingCapability).getActiveModule();
            if (module.getItem() instanceof IRightClickModule)
                return ((IRightClickModule) module.getItem()).onItemUse(itemStackIn, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        return EnumActionResult.PASS;
    }

    @Optional.Method(modid = "forestry")
    public float getSaplingModifier(ItemStack itemStack, World world, EntityPlayer entityPlayer, BlockPos blockPos)  {
        if (ModuleManager.getInstance().itemHasActiveModule(itemStack, MPSModuleConstants.MODULE_GRAFTER)) {
            ElectricItemUtils.drainPlayerEnergy(entityPlayer, ModuleManager.getInstance().computeModularPropertyInteger(itemStack, GrafterModule.GRAFTER_ENERGY_CONSUMPTION));
            MuseHeatUtils.heatPlayerLegacy(entityPlayer, ModuleManager.getInstance().computeModularPropertyDouble(itemStack, GrafterModule.GRAFTER_HEAT_GENERATION));
            return 100.0f;
        }
        return 0.0f;
    }

    // The Item/ItemTool version doesn't give us the player, so we can't override that.
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if(state.getMaterial().isToolNotRequired())
            return true;

        for (ItemStack module : ModuleManager.getInstance().getBlockBreakingModules()) {
            if (ModuleManager.getInstance().itemHasActiveModule(stack, module.getUnlocalizedName()) && ((IBlockBreakingModule)module.getItem()).canHarvestBlock(stack, state, player)) {
                return true;
            }
        }
        return false;
    }

    /* TE Crescent Hammer */
    @Override
    public boolean isUsable(ItemStack itemStackIn, EntityLivingBase entityLivingBase, Entity entity) {
        IItemHandler modeChangingCapability = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modeChangingCapability != null && modeChangingCapability instanceof IModeChangingItemCapability) {
            return entityLivingBase instanceof EntityPlayer &&
                    (((IModeChangingItemCapability) modeChangingCapability).getActiveModule().getItem() instanceof OmniWrenchModule);
        }
        return false;
    }

    /* TE Crescent Hammer */
    @Override
    public boolean isUsable(ItemStack itemStackIn, EntityLivingBase entityLivingBase, BlockPos blockPos) {
        IItemHandler modeChangingCapability = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modeChangingCapability != null && modeChangingCapability instanceof IModeChangingItemCapability) {
            return entityLivingBase instanceof EntityPlayer &&
                    (((IModeChangingItemCapability) modeChangingCapability).getActiveModule().getItem() instanceof OmniWrenchModule);
        }
        return false;
    }

    /* TE Crescent Hammer */
    @Override
    public void toolUsed(ItemStack itemStack, EntityLivingBase entityLivingBase, Entity entity) {

    }

    /* TE Crescent Hammer */
    @Override
    public void toolUsed(ItemStack itemStack, EntityLivingBase entityLivingBase, BlockPos blockPos) {

    }
////
////    /* Railcraft Crowbar */
////    @Override
////    public boolean canWhack(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, BlockPos blockPos) {
////        return this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
////    }
////
////    /* Railcraft Crowbar */
////    @Override
////    public boolean canLink(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
////        return this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
////    }
////
////    /* Railcraft Crowbar */
////    @Override
////    public boolean canBoost(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
////        return this.getActiveMode(itemStack).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
////    }
////
////    /* Railcraft Crowbar */
////    @Override
////    public void onLink(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
////
////    }
////
////    /* Railcraft Crowbar */
////    @Override
////    public void onWhack(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, BlockPos blockPos) {
////
////    }
////
////    /* Railcraft Crowbar */
////    @Override
////    public void onBoost(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
////
////    }

    /* AE wrench */
    @Override
    public boolean canWrench(ItemStack itemStackIn, EntityPlayer entityPlayer, BlockPos blockPos) {
        IItemHandler modeChangingCapability = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modeChangingCapability != null && modeChangingCapability instanceof IModeChangingItemCapability) {
            return (((IModeChangingItemCapability) modeChangingCapability).getActiveModule().getItem() instanceof OmniWrenchModule);
        }
        return false;
    }

//
////    /* Buildcraft Wrench */
////    @Override
////    public void wrenchUsed(EntityPlayer player, int i, int i1, int i2) {
////    }
////
////    /* Buildcraft Wrench */
////    @Override
////    public boolean canWrench(EntityPlayer player, int i, int i1, int i2) {
////        return this.getActiveMode(player.getHeldItem()).equals(OmniWrenchModule.MODULE_OMNI_WRENCH);
////    }
//



//    /* EnderIO Tool */
//    @Override
//    public void used(@Nonnull EnumHand enumHand, @Nonnull EntityPlayer entityPlayer, @Nonnull BlockPos blockPos) {
//
//    }
//
//    /* EnderIO Tool */
//    @Override
//    public boolean canUse(@Nonnull EnumHand enumHand, @Nonnull EntityPlayer entityPlayer, @Nonnull BlockPos blockPos) {
//        ItemStack itemStackIn = entityPlayer.getHeldItem(enumHand);
//        return this.getActiveMode(itemStackIn).equals(MPSModuleConstants.MODULE_OMNI_WRENCH);
//    }
//
//    /* EnderIO Tool */
//    @Override
//    public boolean shouldHideFacades(ItemStack itemStack, EntityPlayer entityPlayer) {
//        return this.getActiveMode(itemStack).equals(MPSModuleConstants.MODULE_OMNI_WRENCH);
//    }

    /* Mekanism Wrench */
    @Override
    public boolean canUseWrench(ItemStack itemStackIn, EntityPlayer entityPlayer, BlockPos blockPos) {
        IItemHandler modeChangingCapability = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modeChangingCapability != null && modeChangingCapability instanceof IModeChangingItemCapability) {
            return (((IModeChangingItemCapability) modeChangingCapability).getActiveModule().getItem() instanceof OmniWrenchModule);
        }
        return false;
    }


    /* IModeChangingItem -------------------------------------------------------------------------- */

//    @Nullable
//    @Override
//    @SideOnly(Side.CLIENT)
//    public TextureAtlasSprite getModeIcon(String mode, ItemStack stack, EntityPlayer player) {
//        ItemStack module = ModuleManager.getInstance().getModule(mode);
//        if (module != null)
//            return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(module).getParticleTexture();
//        return null;
//    }
//
//    @Override
//    public List<String> getValidModes(ItemStack stack) {
//        List<String> modes = new ArrayList<>();
//        for (ItemStack module : ModuleManager.getInstance().getRightClickModules()) {
//            if (((IRightClickModule)module.getItem()).isValidForItem(stack))
//                if (ModuleManager.getInstance().itemHasModule(stack, module.getUnlocalizedName()))
//                    modes.add(module.getUnlocalizedName());
//        }
//        return modes;
//    }
//
//    @Override
//    public String getActiveMode(ItemStack stack) {
//        String modeFromNBT = NuminaItemUtils.getTagCompound(stack).getString("mode");
//        if (modeFromNBT.isEmpty()) {
//            List<String> validModes = getValidModes(stack);
//            return (validModes!=null && (validModes.size() > 0) ? validModes.get(0) : "");
//        }
//        else {
//            return modeFromNBT;
//        }
//    }
//
//    @Override
//    public void setActiveMode(ItemStack stack, String newMode) {
//        NuminaItemUtils.getTagCompound(stack).setString("mode", newMode);
//    }
//
//    @Override
//    public void cycleMode(ItemStack stack, EntityPlayer player, int dMode) {
//        List<String> modes = this.getValidModes(stack);
//        if (modes.size() > 0) {
//            int newindex = clampMode(modes.indexOf(this.getActiveMode(stack)) + dMode, modes.size());
//            String newmode = (String)modes.get(newindex);
//            this.setActiveMode(stack, newmode);
//            PacketSender.sendToServer(new MusePacketModeChangeRequest(player, newmode, player.inventory.currentItem));
//        }
//    }
//
//    private static int clampMode(int selection, int modesSize) {
//        return (selection > 0) ? (selection % modesSize) : ((selection + modesSize * -selection) % modesSize);
//    }
//
//
//    /* nextMode and prevMode are used for getting the icons to display in the mode selection */
//    @Override
//    public String nextMode(ItemStack stack, EntityPlayer player) {
//        List<String> modes = getValidModes(stack);
//        if (modes.size() > 0) {
//            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) + 1, modes.size());
//            return (String)modes.get(newindex);
//        }
//        else return "";
//    }
//
//    @Override
//    public String prevMode(ItemStack stack, EntityPlayer player) {
//        List<String> modes = this.getValidModes(stack);
//        if (modes.size() > 0) {
//            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) - 1, modes.size());
//            return (String)modes.get(newindex);
//        }
//        else return "";
//    }

    @Override
    @Nonnull
    public INetworkItem provide(INetworkItemHandler handler, EntityPlayer player, ItemStack itemStackIn) {
        return RefinedStorageWirelessModule.provide(handler, player, itemStackIn);
    }

    /* IMuseItem ------------------------------------------------------------------------------- */
    @Override // FIXME: check to see if this is needed or not.
    public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
        List<String> info = new ArrayList<>();
        info.add("Detailed Summary");
        info.add(formatInfo("Armor", getArmorDouble(player, stack)));
        info.add(formatInfo("Energy Storage", this.getEnergyStored(stack)) + 'J');
        info.add(formatInfo("Weight", WeightHelper.getTotalWeight(stack)) + 'g');
        return info;
    }

//    @Override
//    public double getPlayerEnergy(EntityPlayer player) {
//        return ElectricItemUtils.getPlayerEnergy(player);
//    }
//
//    @Override
//    public void drainPlayerEnergy(EntityPlayer player, double drainAmount) {
//        ElectricItemUtils.drainPlayerEnergy(player, drainAmount);
//    }
//
//    @Override
//    public void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
//        ElectricItemUtils.givePlayerEnergy(player, joulesToGive);
//    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new MPSCapProvider(stack, nbt);
    }
}