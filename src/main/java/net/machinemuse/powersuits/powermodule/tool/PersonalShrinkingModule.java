// TODO: fix when Compact Machines portss



//package net.machinemuse.powersuits.powermodule.tool;
//
//import cpw.mods.fml.common.registry.GameRegistry;
//import net.machinemuse.api.IModularItem;
//import net.machinemuse.api.moduletrigger.IPlayerTickModule;
//import net.machinemuse.api.moduletrigger.IRightClickModule;
//import net.machinemuse.powersuits.item.ItemComponent;
//import net.machinemuse.powersuits.powermodule.PowerModuleBase;
//import net.machinemuse.utils.MuseCommonStrings;
//import net.machinemuse.utils.MuseItemUtils;
//import net.minecraft.block.Block;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.EnumActionResult;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//import java.util.List;
//
///**
// * Created by User: Korynkai
// * 5:41 PM 2014-11-19
// */
//
//public class PersonalShrinkingModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
//    public static final String MODULE_CM_PSD = "Personal Shrinking Device";
//    private final ItemStack cpmPSD;
//
//    public PersonalShrinkingModule(List<IModularItem> validItems) {
//        super(validItems);
//        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 4));
//        cpmPSD = GameRegistry.findItemStack("CompactMachines", "psd", 1);
//        addInstallCost(cpmPSD);
//    }
//
//    @Override
//    public String getCategory() {
//        return MuseCommonStrings.CATEGORY_TOOL;
//    }
//
//    @Override
//    public String getDataName() {
//        return MODULE_CM_PSD;
//    }
//
//    @Override
//    public String getUnlocalizedName() {
//        return "cmPSD";
//    }
//
//    @Override
//    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        cpmPSD.getItem().onItemRightClick(item, world, player);
//
//        return null;
//    }
//
//    @Override
//    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        return null;
//    }
//
//    @Override
//    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        if (world.isRemote && player instanceof EntityPlayerMP) {
//            int block = Block.getIdFromBlock(world.getBlock(x, y, z));
//            if (block == Block.getIdFromBlock(GameRegistry.findBlock("CompactMachines", "machine"))) {
//                return false;
//            } else if (block == Block.getIdFromBlock(GameRegistry.findBlock("CompactMachines", "innerwall"))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        if (!MuseItemUtils.getCanShrink(item)) {
//            MuseItemUtils.setCanShrink(item, true);
//        }
//    }
//
//    @Override
//    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//        if (MuseItemUtils.getCanShrink(item)) {
//            MuseItemUtils.setCanShrink(item, false);
//        }
//    }
//
//    public float minF(float a, float b) {
//        return a < b ? a : b;
//    }
//
//    @Override
//    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
//
//    }
//
//    @Override
//    public TextureAtlasSprite getIcon(ItemStack item) {
//        return super.getIcon(item);
//    }
//}