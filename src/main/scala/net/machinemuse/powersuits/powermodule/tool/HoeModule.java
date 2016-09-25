package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

public class HoeModule extends PowerModuleBase implements IPowerModule, IRightClickModule {
    public static final String MODULE_HOE = "Rototiller";
    public static final String HOE_ENERGY_CONSUMPTION = "Hoe Energy Consumption";
    public static final String HOE_SEARCH_RADIUS = "Hoe Search Radius";


    public HoeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));

        addBaseProperty(HOE_ENERGY_CONSUMPTION, 50);
        addTradeoffProperty("Search Radius", HOE_ENERGY_CONSUMPTION, 950);
        addTradeoffProperty("Search Radius", HOE_SEARCH_RADIUS, 8, "m");
    }

    @Override
    public ActionResult onRightClick(EntityPlayer player, World world, ItemStack item, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, item);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

        world.getBiomeProvider().getBiomeGenerator(pos, Biomes.PLAINS);





        double energyConsumed = ModuleManager.computeModularProperty(itemStack, HOE_ENERGY_CONSUMPTION);
        if (player.canPlayerEdit(pos, side, itemStack) && ElectricItemUtils.getPlayerEnergy(player) > energyConsumed) {
            UseHoeEvent event = new UseHoeEvent(player, itemStack, world, pos);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return EnumActionResult.FAIL;
            }

            if (event.getResult() == Event.Result.ALLOW) {
                ElectricItemUtils.drainPlayerEnergy(player, energyConsumed);
                return EnumActionResult.SUCCESS;
            }

            if (world.isRemote) {
                System.out.println("returning fail because world is remote");

                return EnumActionResult.FAIL;
            }
            double radius = (int) ModuleManager.computeModularProperty(itemStack, HOE_SEARCH_RADIUS);
            for (int i = (int) Math.floor(-radius); i < radius; i++) {
                for (int j = (int) Math.floor(-radius); j < radius; j++) {
                    if (i * i + j * j < radius * radius) {
                        BlockPos blockPos = pos.add(i, 0, j);
                        if (enumHoe(itemStack, player, world, blockPos, side) == EnumActionResult.SUCCESS) {
                            world.markBlockRangeForRenderUpdate(blockPos, blockPos);
                            System.out.println("block named " + world.getBlockState(blockPos).getBlock().getLocalizedName() + " at pos " + blockPos.toString() + " success to set as farmland");
                        }

                        else
                            System.out.println("block named " + world.getBlockState(blockPos).getBlock().getLocalizedName() + " at pos " + blockPos.toString() + " failed to set as farmland");
                    }
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    /*
     * Vanilla Hoe code
     */
    private EnumActionResult enumHoe(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        int hook = ForgeEventFactory.onHoeUse(itemStack, player, world, pos);
        if (hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (side != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
            if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                this.setBlock(itemStack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                return EnumActionResult.SUCCESS;
            }

            if (block == Blocks.DIRT) {
                switch ((BlockDirt.DirtType) iblockstate.getValue(BlockDirt.VARIANT)) {
                    case DIRT:
                        this.setBlock(itemStack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                        return EnumActionResult.SUCCESS;
                    case COARSE_DIRT:
                        this.setBlock(itemStack, player, world, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                        return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }

    protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!worldIn.isRemote)
        {
            worldIn.setBlockState(pos, state, 11);
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, HOE_ENERGY_CONSUMPTION));
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {

    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_HOE;
    }

    @Override
    public String getUnlocalizedName() {
        return "hoe";
    }

    @Override
    public String getDescription() {
        return "An automated tilling addon to make it easy to till large swaths of land at once.";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(Items.GOLDEN_HOE)).getParticleTexture();
    }
}
