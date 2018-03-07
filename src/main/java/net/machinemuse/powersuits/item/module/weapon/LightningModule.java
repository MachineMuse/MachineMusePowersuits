package net.machinemuse.powersuits.item.module.weapon;


import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.numina.utils.misc.RayTraceUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
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

/**
 * Created by User: Andrew2448
 * 5:56 PM 6/14/13
 */
public class LightningModule extends PowerModuleBase implements IRightClickModule {
    public static final String LIGHTNING_ENERGY_CONSUMPTION = "Energy Consumption";
    public static final String HEAT = "Heat Emission";

    public LightningModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        addBasePropertyInt(LIGHTNING_ENERGY_CONSUMPTION, 196000, "");
        addBasePropertyDouble(HEAT, 100, "");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_WEAPON;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        System.out.println("player energy before: " + ElectricItemUtils.getPlayerEnergy(playerIn));

        if (hand == EnumHand.MAIN_HAND) {
            try {
                double range = 64;
                int energyConsumption = ModuleManager.getInstance().computeModularPropertyInteger(itemStackIn, LIGHTNING_ENERGY_CONSUMPTION);

                System.out.println("energy usage: " + energyConsumption);

                if (energyConsumption < ElectricItemUtils.getPlayerEnergy(playerIn)) {
                    System.out.println("drained energy: " + ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption));

                    System.out.println("player energy after: " + ElectricItemUtils.getPlayerEnergy(playerIn));


                    MuseHeatUtils.heatPlayerLegacy(playerIn, ModuleManager.getInstance().computeModularPropertyDouble(itemStackIn, HEAT));
                    RayTraceResult raytraceResult = RayTraceUtils.doCustomRayTrace(playerIn.world, playerIn, true, range);
                    worldIn.spawnEntity(new EntityLightningBolt(playerIn.world, raytraceResult.hitVec.x, raytraceResult.hitVec.y, raytraceResult.hitVec.z, false));
                }
            } catch (Exception ignored) {
                return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
            }
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
}