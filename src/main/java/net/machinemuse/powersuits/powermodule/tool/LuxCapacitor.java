package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
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

import java.util.List;

public class LuxCapacitor extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_LUX_CAPACITOR = "Lux Capacitor";
    public static final String ENERGY = "Lux Capacitor Energy Consumption";
    public static final String RED =  "Lux Capacitor Red Hue";
    public static final String GREEN = "Lux Capacitor Green Hue";
    public static final String BLUE = "Lux Capacitor Blue Hue";

    public LuxCapacitor(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.GLOWSTONE_DUST, 1));
        addInstallCost(new ItemStack(Items.IRON_INGOT, 2));
        addBaseProperty(ENERGY, 100, "J");
        addTradeoffProperty("Red", RED, 1, "%");
        addTradeoffProperty("Green", GREEN, 1, "%");
        addTradeoffProperty("Blue", BLUE, 1, "%");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_LUX_CAPACITOR;
    }

    @Override
    public String getUnlocalizedName() { return "luxCapacitor";
    }

    @Override
    public String getDescription() {
        return "Launch a virtually infinite number of attractive light sources at the wall.";
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        player.setItemInUse(itemStack, 10);
//        if (!world.isRemote) {
//            double energyConsumption = ModuleManager.computeModularProperty(itemStack, ENERGY);
//            // MuseHeatUtils.heatPlayer(player, energyConsumption / 500);
//            if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
//                ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
//
//                double red = ModuleManager.computeModularProperty(itemStack, RED);
//                double green = ModuleManager.computeModularProperty(itemStack, GREEN);
//                double blue = ModuleManager.computeModularProperty(itemStack, BLUE);
//                EntityLuxCapacitor luxCapacitor = new EntityLuxCapacitor(world, player, red, green, blue);
//                world.spawnEntityInWorld(luxCapacitor);
//            }
//        }
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
