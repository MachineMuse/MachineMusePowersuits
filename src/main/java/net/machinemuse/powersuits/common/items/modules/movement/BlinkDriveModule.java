package net.machinemuse.powersuits.common.items.modules.movement;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.player.MusePlayerUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_BLINK_DRIVE;

public class BlinkDriveModule extends PowerModuleBase implements IRightClickModule {
    public static final String BLINK_DRIVE_ENERGY_CONSUMPTION = "Blink Drive Energy Consuption";
    public static final String BLINK_DRIVE_RANGE = "Blink Drive Range";

    public BlinkDriveModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(BLINK_DRIVE_ENERGY_CONSUMPTION, 1000, "J");
        addBaseProperty(BLINK_DRIVE_RANGE, 5, "m");
        addTradeoffProperty("Range", BLINK_DRIVE_ENERGY_CONSUMPTION, 3000);
        addTradeoffProperty("Range", BLINK_DRIVE_RANGE, 59);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MODULE_BLINK_DRIVE;
    }

    @Override
    public String getUnlocalizedName() { return "blinkDrive";
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        SoundEvent enderman_portal =  SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport"));
        double range = ModuleManager.computeModularProperty(itemStackIn, BLINK_DRIVE_RANGE);
        double energyConsumption = ModuleManager.computeModularProperty(itemStackIn, BLINK_DRIVE_ENERGY_CONSUMPTION);
        if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption) {
            NuminaPlayerUtils.resetFloatKickTicks(playerIn);
            ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);
            worldIn.playSound(playerIn, playerIn.getPosition(), enderman_portal, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
             MuseLogger.logDebug("Range: " + range);
            RayTraceResult hitRayTrace = MusePlayerUtils.doCustomRayTrace(playerIn.world, playerIn, true, range);

            MuseLogger.logDebug("Hit:" + hitRayTrace);
            MusePlayerUtils.teleportEntity(playerIn, hitRayTrace);
            worldIn.playSound(playerIn, playerIn.getPosition(), enderman_portal, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
        }
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.blinkDrive;
    }
}
