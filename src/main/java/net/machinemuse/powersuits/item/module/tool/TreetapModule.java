package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.utils.ElectricItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by User: Andrew2448
 * 7:45 PM 4/23/13
 *
 * Updated by leon on 6/14/16.
 */
public class TreetapModule extends PowerModuleBase implements IRightClickModule {
    public static final String TREETAP_ENERGY_CONSUMPTION = "Energy Consumption";
    public static ItemStack resin;
    public static Block rubber_wood;
    public static ItemStack emulatedTool;
    private Method attemptExtract;
    private boolean isIC2Classic;

    public static ItemStack treetap;

    public TreetapModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        if (ModCompatibility.isIndustrialCraftClassicLoaded()) {
            emulatedTool = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ic2", "itemTreetapElectric")), 1);
            treetap = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("ic2", "itemTreetap")), 1);
            resin = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("ic2", "misc_resource")), 1, 4);
            rubber_wood =  Block.REGISTRY.getObject(new ResourceLocation("ic2", "blockRubWood"));
            try {
                attemptExtract = treetap.getItem().getClass().
                        getDeclaredMethod("attemptExtract", ItemStack.class, EntityPlayer.class, World.class, BlockPos.class, EnumFacing.class, List.class);
            } catch (Exception ignored) {

            }
            isIC2Classic = true;
        } else {
            emulatedTool = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("ic2", "electric_treetap")), 1);
            treetap = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("ic2", "treetap")), 1);
            resin = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("ic2", "misc_resource")), 1, 4);
            rubber_wood =  Block.REGISTRY.getObject(new ResourceLocation("ic2", "rubber_wood"));
            try {
                attemptExtract = treetap.getItem().getClass().
                                getDeclaredMethod("attemptExtract", EntityPlayer.class, World.class, BlockPos.class, EnumFacing.class, IBlockState.class, List.class);
            } catch (Exception ignored) {

            }
            isIC2Classic = false;
        }
        addBasePropertyDouble(TREETAP_ENERGY_CONSUMPTION, 100);
        addInstallCost(emulatedTool);
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        try {
            // IC2 Classic
            if (isIC2Classic) {
                if (block == rubber_wood && ModuleManager.getInstance().computeModularPropertyDouble(itemStack, TREETAP_ENERGY_CONSUMPTION) < ElectricItemUtils.getPlayerEnergy(player)) {
                    if (attemptExtract.invoke( "attemptExtract", null, player, world, pos, facing, null).equals(true)) {
                        ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.getInstance().computeModularPropertyInteger(itemStack, TREETAP_ENERGY_CONSUMPTION));
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
            // IC2 Experimental
            else {
                if (block == rubber_wood && ModuleManager.getInstance().computeModularPropertyDouble(itemStack, TREETAP_ENERGY_CONSUMPTION) < ElectricItemUtils.getPlayerEnergy(player)) {
                    if (attemptExtract.invoke( "attemptExtract", player, world, pos, facing, state, null).equals(true)) {
                        ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.getInstance().computeModularPropertyInteger(itemStack, TREETAP_ENERGY_CONSUMPTION));
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
            return EnumActionResult.PASS;
        } catch (Exception ignored) {

        }
        return EnumActionResult.FAIL;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_TOOL;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(emulatedTool).getParticleTexture();
    }
}