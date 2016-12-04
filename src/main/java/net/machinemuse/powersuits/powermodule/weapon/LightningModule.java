package net.machinemuse.powersuits.powermodule.weapon;


import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 5:56 PM 6/14/13
 */
public class LightningModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_LIGHTNING = "Lightning Summoner";
    public static final String LIGHTNING_ENERGY_CONSUMPTION = "Energy Consumption";
    public static final String HEAT = "Heat Emission";

    public LightningModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        addBaseProperty(LIGHTNING_ENERGY_CONSUMPTION, 490000, "");
        addBaseProperty(HEAT, 100, "");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_LIGHTNING;
    }

    @Override
    public String getUnlocalizedName() {
        return "lightningSummoner";
    }

    @Override
    public String getDescription() {
        return "Allows you to summon lightning for a large energy cost.";
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        try {
            double range = 64;
            double energyConsumption = ModuleManager.computeModularProperty(itemStackIn, LIGHTNING_ENERGY_CONSUMPTION);
            if (energyConsumption < ElectricItemUtils.getPlayerEnergy(playerIn)) {
                ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);
                MuseHeatUtils.heatPlayer(playerIn, ModuleManager.computeModularProperty(itemStackIn, HEAT));
                RayTraceResult raytraceResult = MusePlayerUtils.doCustomRayTrace(playerIn.worldObj, playerIn, true, range);
                worldIn.spawnEntityInWorld(new EntityLightningBolt(playerIn.worldObj, raytraceResult.hitVec.xCoord, raytraceResult.hitVec.yCoord, raytraceResult.hitVec.zCoord, true));

                /*for (int x = (int)playerIn.posX-1; x < (int)playerIn.posX+2; x++) {
                    for (int z = (int)playerIn.posZ-1; z < (int)playerIn.posZ+2; z++) {
                        if (playerIn.canPlayerEdit(x, (int)playerIn.posY, z, 1, item)) {
                            int id = worldIn.getBlockId(x, (int)playerIn.posY, z);
                            if (id == 0) {
                                worldIn.setBlock(x, (int)playerIn.posY, z, Block.fire.blockID);
                            }
                        }
                    }
                }*/
            }
        } catch (Exception ignored) {
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
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
        return MuseIcon.lightning;
    }
}