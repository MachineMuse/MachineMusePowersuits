//package net.machinemuse.powersuits.item.module.tool;
//
//import net.machinemuse.numina.module.EnumModuleCategory;
//import net.machinemuse.numina.module.EnumModuleTarget;
//import net.machinemuse.numina.module.IRightClickModule;
//import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.EnumActionResult;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
// // TODO: revisit... replace with something "better"?
//import javax.annotation.Nonnull;
//
///**
// * Created by User: Andrew2448
// * 4:39 PM 4/21/13
// * updated by MachineMuse, adapted from OpenComputers srench
// * <p>
// * Ported to Java by lehjr on 10/11/16.
// */
//public class ItemModuleOmniWrench extends ItemAbstractPowerModule implements IRightClickModule {
//    public ItemModuleOmniWrench() {
//        super(EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
////        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
////        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
//    }
//
//    @Override
//    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
//    }
//
//    @Override
//    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        return EnumActionResult.PASS;
//    }
//
//    @Override
//    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        return StolenWrenchCode.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
//    }
//
//    @Override
//    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
//
//    }
//
//    @Override
//    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
//        return 0;
//    }
//}
