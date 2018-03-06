package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.numina.utils.misc.RayTraceUtils;
import net.machinemuse.numina.utils.module.helpers.BlinkDriveHelper;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlinkDriveModule extends PowerModuleBase implements IRightClickModule {
    public static final String BLINK_DRIVE_ENERGY_CONSUMPTION = "Blink Drive Energy Consuption";
    public static final String BLINK_DRIVE_RANGE = "Blink Drive Range";

    public BlinkDriveModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addBasePropertyDouble(BLINK_DRIVE_ENERGY_CONSUMPTION, 1000, "J");
        addBasePropertyDouble(BLINK_DRIVE_RANGE, 5, "m");
        addTradeoffPropertyDouble("Range", BLINK_DRIVE_ENERGY_CONSUMPTION, 3000);
        addTradeoffPropertyDouble("Range", BLINK_DRIVE_RANGE, 59);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_MOVEMENT;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        SoundEvent enderman_portal =  SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport"));
        double range = ModuleManager.getInstance().computeModularPropertyDouble(itemStackIn, BLINK_DRIVE_RANGE);
        int energyConsumption = ModuleManager.getInstance().computeModularPropertyInteger(itemStackIn, BLINK_DRIVE_ENERGY_CONSUMPTION);
        if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption) {
            NuminaPlayerUtils.resetFloatKickTicks(playerIn);
            ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);
            worldIn.playSound(playerIn, playerIn.getPosition(), enderman_portal, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
             MuseLogger.logDebug("Range: " + range);
            RayTraceResult hitRayTrace = RayTraceUtils.doCustomRayTrace(playerIn.world, playerIn, true, range);

            MuseLogger.logDebug("Hit:" + hitRayTrace);
            BlinkDriveHelper.teleportEntity(playerIn, hitRayTrace);
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
