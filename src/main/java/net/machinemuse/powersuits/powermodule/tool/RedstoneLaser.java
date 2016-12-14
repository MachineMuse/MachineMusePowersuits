//package net.machinemuse.powersuits.powermodule.tool;
//
//import net.machinemuse.api.IModularItem;
//import net.machinemuse.api.moduletrigger.IRightClickModule;
//import net.machinemuse.powersuits.common.ModularPowersuits;
//import net.machinemuse.powersuits.item.ItemComponent;
//import net.machinemuse.powersuits.powermodule.PowerModuleBase;
//import net.machinemuse.utils.MuseCommonStrings;
//import net.machinemuse.utils.MuseItemUtils;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.world.World;
//import net.minecraftforge.common.util.ForgeDirection;
//
//import java.util.List;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 2:27 PM, 5/8/13
// *
// * Ported to Java by lehjr on 12/12/16.
// */
//public class RedstoneLaser extends PowerModuleBase implements IRightClickModule {
//    public RedstoneLaser(String name, List<IModularItem> validItems) {
//        super(name, validItems);
//        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
//    }
//
//    @Override
//    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
//
//    }
//
//    @Override
//    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
//        return false;
//    }
//
//    @Override
//    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideIndex, float hitX, float hitY, float hitZ) {
//        ForgeDirection side = ForgeDirection.getOrientation(sideIndex);
//    int xo = x + side.offsetX;
//    int yo = y + side.offsetY;
//    int zo = z + side.offsetZ;
//    val redlaserid = RedstoneLaser;
//    switch (world.getBlock(xo, yo, zo)) {
//        case 0:
//            world.setBlock(xo, yo, zo, redlaserid);
//            break;
//        case`redlaserid`: world.setBlockMetadataWithNotify(xo, yo, zo, redlaserid, world.getBlockMetadata(xo, yo, zo) + 1);
//            break;
//        default:
//            break;
//
//    }
//
//
//
//        return true;
//    }
//
//    @Override
//    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
//        player.openGui(ModularPowersuits.INSTANCE(), 2, world, (int)player.posX, (int)player.posY, (int)player.posZ);
//    }
//
//    @Override
//    public String getCategory() {
//        return MuseCommonStrings.CATEGORY_SPECIAL;
//    }
//
//    @Override
//    public String getDataName() {
//        return "Redstone Laser";
//    }
//
//    @Override
//    public String getTextureFile() {
//        return null;
//    }
//}
//
////
////class RedstoneLaser(list: java.util.List[IModularItem]) extends PowerModuleBase(list) with IRightClickModule {
//
////        override def getUnlocalizedName = "redstoneLaser"
////
////        def getTextureFile: String = "laser"
////
////        def onItemUse(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
////        false
////        }
////
////        def onItemUseFirst(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, sideIndex: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
//
////        }
////
////        def onPlayerStoppedUsing(itemStack: ItemStack, world: World, player: EntityPlayer, par4: Int) {}
////
//
////
////        }
