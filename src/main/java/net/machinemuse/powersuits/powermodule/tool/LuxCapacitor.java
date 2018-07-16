package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseHeatUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LuxCapacitor extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_LUX_CAPACITOR = "Lux Capacitor";
    public static final String ENERGY = "Lux Capacitor Energy Consumption";
    public static final String RED =  "Lux Capacitor Red Hue";
    public static final String GREEN = "Lux Capacitor Green Hue";
    public static final String BLUE = "Lux Capacitor Blue Hue";

    public LuxCapacitor(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.GLOWSTONE_DUST, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.IRON_INGOT, 2));
        addBaseProperty(ENERGY, 100, "J");
        addTradeoffProperty("Red", RED, 1, "%");
        addTradeoffProperty("Green", GREEN, 1, "%");
        addTradeoffProperty("Blue", BLUE, 1, "%");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_LUX_CAPACITOR;
    }

    @Override
    public String getUnlocalizedName() {
        return "luxCapacitor";
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
        if (!worldIn.isRemote) {
            double energyConsumption = ModuleManager.INSTANCE.computeModularProperty(itemStackIn, ENERGY);
             MuseHeatUtils.heatPlayer(playerIn, energyConsumption / 500);
            if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);

                double red = ModuleManager.INSTANCE.computeModularProperty(itemStackIn, RED);
                double green = ModuleManager.INSTANCE.computeModularProperty(itemStackIn, GREEN);
                double blue = ModuleManager.INSTANCE.computeModularProperty(itemStackIn, BLUE);

                EntityLuxCapacitor luxCapacitor = new EntityLuxCapacitor(worldIn, playerIn, new Colour(red, green, blue));
                worldIn.spawnEntity(luxCapacitor);
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

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.luxCapacitor;
    }
}
