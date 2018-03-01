package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.math.geometry.Colour;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
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
    public static final String ENERGY = "Lux Capacitor Energy Consumption";
    public static final String RED =  "Lux Capacitor Red Hue";
    public static final String GREEN = "Lux Capacitor Green Hue";
    public static final String BLUE = "Lux Capacitor Blue Hue";

    public LuxCapacitor(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addInstallCost(new ItemStack(Items.GLOWSTONE_DUST, 1));
        addInstallCost(new ItemStack(Items.IRON_INGOT, 2));
        addBasePropertyDouble(ENERGY, 100, "J");
        addTradeoffPropertyDouble("Red", RED, 1, "%");
        addTradeoffPropertyDouble("Green", GREEN, 1, "%");
        addTradeoffPropertyDouble("Blue", BLUE, 1, "%");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_TOOL;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
        if (!worldIn.isRemote) {
            int energyConsumption = ModuleManager.getInstance().computeModularPropertyInteger(itemStackIn, ENERGY);
             MuseHeatUtils.heatPlayerLegacy(playerIn, energyConsumption / 500);
            if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);

                double red = ModuleManager.getInstance().computeModularPropertyDouble(itemStackIn, RED);
                double green = ModuleManager.getInstance().computeModularPropertyDouble(itemStackIn, GREEN);
                double blue = ModuleManager.getInstance().computeModularPropertyDouble(itemStackIn, BLUE);

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