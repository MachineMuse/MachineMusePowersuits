package net.machinemuse.powersuits.common.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
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
//    public static final String RED =  "Lux Capacitor Red Hue";
//    public static final String GREEN = "Lux Capacitor Green Hue";
//    public static final String BLUE = "Lux Capacitor Blue Hue";

    public static final String COLOUR = "Lux Capacitor Color Index";


    public LuxCapacitor(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.GLOWSTONE_DUST, 1));
        addInstallCost(new ItemStack(Items.IRON_INGOT, 2));
        addBaseProperty(ENERGY, 100, "J");
//        addTradeoffProperty("Red", RED, 1, "%");
//        addTradeoffProperty("Green", GREEN, 1, "%");
//        addTradeoffProperty("Blue", BLUE, 1, "%");

        addBaseProperty("Color Index", 0);
        addTradeoffProperty("Color Index", COLOUR, 4);

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
    public String getUnlocalizedName() {
        return "luxcapacitor";
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
        if (!worldIn.isRemote) {
            double energyConsumption = ModuleManager.computeModularProperty(itemStackIn, ENERGY);
             MuseHeatUtils.heatPlayer(playerIn, energyConsumption / 500);
            if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption) {
//                ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);

//                double red = ModuleManager.computeModularProperty(itemStackIn, RED);
//                double green = ModuleManager.computeModularProperty(itemStackIn, GREEN);
//                double blue = ModuleManager.computeModularProperty(itemStackIn, BLUE);

                double colourIndex =  ModuleManager.computeModularProperty(itemStackIn, COLOUR);

                System.out.println("value: " + colourIndex);


                //                EntityLuxCapacitor luxCapacitor = new EntityLuxCapacitor(worldIn, playerIn, EnumColour.getColourEnumFromIndex(colourIndex));
//                worldIn.spawnEntity(luxCapacitor);
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
