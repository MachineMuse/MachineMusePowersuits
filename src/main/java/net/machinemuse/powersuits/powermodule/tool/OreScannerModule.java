package net.machinemuse.powersuits.powermodule.tool;

import li.cil.scannable.api.scanning.ScanResultProvider;
import li.cil.scannable.client.ScanManager;
import li.cil.scannable.common.capabilities.CapabilityScanResultProvider;
import li.cil.scannable.common.config.Constants;
import li.cil.scannable.common.config.Settings;
import li.cil.scannable.common.init.Items;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.capabilities.ItemHandlerPowerFist;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class OreScannerModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
    static final ResourceLocation scannerCharge = new ResourceLocation("scannable", "scanner_charge");
    static final ResourceLocation scanner_activate = new ResourceLocation("scannable", "scanner_activate");
    ItemStack scanner;// = ItemStack.EMPTY;

    public OreScannerModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        scanner = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("scannable", "scanner")));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), scanner);
    }

    /**
     * If check if the player is in creative mode or if the player's energy is high enough to do a scan with the installed modules
     */
    private static boolean tryConsumeEnergy(final EntityPlayer player, final List<ItemStack> modules, final boolean simulate) {
        if (player.capabilities.isCreativeMode)
            return true;

        int playerEnergy = ElectricItemUtils.getPlayerEnergy(player);
        if (playerEnergy == 0)
            return false;

        int totalCost = 0;
        for (final ItemStack module : modules) {
            totalCost += getModuleEnergyCost(player, module);
        }

        if (playerEnergy > totalCost) {
            if (!simulate)
                ElectricItemUtils.drainPlayerEnergy(player, totalCost);
            return true;
        }
        return false;
    }

    /**
     * Calculates the energy cost of a given module using Scannable's values
     */
    static int getModuleEnergyCost(final EntityPlayer player, final ItemStack module) {
        final ScanResultProvider provider = module.getCapability(CapabilityScanResultProvider.SCAN_RESULT_PROVIDER_CAPABILITY, null);
        if (provider != null) {
            return provider.getEnergyCost(player, module);
        }

        if (Items.isModuleRange(module)) {
            return Settings.getEnergyCostModuleRange();
        }

        return 0;
    }

    /**
     * Aside from returning the boolean, this also populates the module list.
     * The boolean value indicates whether or not there is any module installed into the scanner that returns a scan result.
     * Some modules, like the range extender, do not return a scan result, but are still valid modules. So this is probably
     * the simplest way of doing this.
     */
    private static boolean collectModules(final ItemStack stack, final List<ItemStack> modules) {
        boolean hasProvider = false;
        final IItemHandler itemHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        assert itemHandler instanceof ItemHandlerPowerFist;
        final IItemHandler activeModules = ((ItemHandlerPowerFist) itemHandler).getActiveModules();
        for (int slot = 0; slot < activeModules.getSlots(); slot++) {
            final ItemStack module = activeModules.getStackInSlot(slot);
            if (module.isEmpty()) {
                continue;
            }

            modules.add(module);
            if (module.hasCapability(CapabilityScanResultProvider.SCAN_RESULT_PROVIDER_CAPABILITY, null)) {
                hasProvider = true;
            }
        }
        return hasProvider;
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_ORE_SCANNER__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        try {
            if (!scanner.isEmpty())
                return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(scanner).getParticleTexture();
        } catch (Exception ignored) {
        }
        return MuseIcon.oreScanner;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    /**
     * Everything below this line ported/copied from li/cil/scannable/common/item/ItemScanner --------------------------------------------------------------------------
     */
    @Override
    public ActionResult onItemRightClick(@Nonnull ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.isSneaking()) {
            playerIn.openGui(ModularPowersuits.getInstance(), 6, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
        } else {
            final List<ItemStack> modules = new ArrayList<>();
            if (!collectModules(itemStackIn, modules)) {
                if (worldIn.isRemote) {
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentTranslation(Constants.MESSAGE_NO_SCAN_MODULES), Constants.CHAT_LINE_ID);
                }
                playerIn.getCooldownTracker().setCooldown(itemStackIn.getItem(), 10);
                return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
            }

            if (!tryConsumeEnergy(playerIn, modules, true)) {
                if (worldIn.isRemote) {
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentTranslation(Constants.MESSAGE_NOT_ENOUGH_ENERGY), Constants.CHAT_LINE_ID);
                }
                playerIn.getCooldownTracker().setCooldown(itemStackIn.getItem(), 10);
                return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
            } else
                playerIn.setActiveHand(hand);
            if (worldIn.isRemote) {
                ScanManager.INSTANCE.beginScan(playerIn, modules);
                Musique.playerSound(playerIn, scannerCharge, SoundCategory.PLAYERS, 2F, 1F, false);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (!(entityLiving instanceof EntityPlayer)) {
            return;
        }

        final EntityPlayer player = (EntityPlayer) entityLiving;
        int chargeTicks = (int) MuseMathUtils.clampDouble(itemStack.getMaxItemUseDuration() - timeLeft, 10, 40);

        if (worldIn.isRemote) {
            // stop sound whether inturrupted or not
            Musique.stopPlayerSound(player, scannerCharge);

            // cancel scan if not scanner charge cycle interrupted
            if (chargeTicks < 40) {
                ScanManager.INSTANCE.cancelScan();
            }
        }

        final List<ItemStack> modules = new ArrayList<>();
        // check for installed modules
        if (!collectModules(itemStack, modules)) {
            return;
        }

        if (worldIn.isRemote) {

            // if charge cycle completed
            if (chargeTicks == 40) {

                // check if player has enough energy
                if (tryConsumeEnergy((EntityPlayer) entityLiving, modules, false)) {

                    // actually run the scan
                    ScanManager.INSTANCE.updateScan(entityLiving, true);
                    Musique.playerSound((EntityPlayer) entityLiving, scanner_activate, SoundCategory.PLAYERS, 1F, 1F, false);
                } else {
                    // cancel scan if not enough power
                    ScanManager.INSTANCE.cancelScan();
                }
            }
        }
        player.getCooldownTracker().setCooldown(itemStack.getItem(), chargeTicks);
    }

    // unused here because total energy cost is controlled outsiode of this mod.
    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (player.getEntityWorld().isRemote) {
            ScanManager.INSTANCE.updateScan(player, false);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {

    }
}