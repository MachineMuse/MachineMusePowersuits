package net.machinemuse.powersuits.item.module.debug;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This module is used for debugging and may change
 */
public class DebugModule extends PowerModuleBase implements IRightClickModule {
    public DebugModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ALLITEMS, resourceDommain, UnlocalizedName);
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return null;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return null;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return null;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_DEBUG;
    }

    //========
//    public static ItemStack emulatedTool;
//
//
//    public DebugModule(List<IModularItemBase> validItems) {
//        super(validItems);
//        emulatedTool = new ItemStack(MPSItems.capacitor, 1, 4);
//        addInstallCost(new ItemStack(Item.getItemFromBlock(Blocks.DIRT)));
//
//    }
//
//    @Override
//    public boolean isAllowed() {
//        return true;
//    }
//
//    @Override
//    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        System.out.println("Doing something here");
//
//        if(itemStackIn.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
//            IItemHandler inventory = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
//            if (inventory.getSlots() > 0) {
//                ItemStack test = inventory.getStackInSlot(0);
//                if (test.isEmpty()) {
//                    for (int i=0; i< playerIn.inventory.getSizeInventory(); i++) {
//                        ItemStack test2 = playerIn.inventory.getStackInSlot(i);
//                        if (!test2.isEmpty() && test2.getItem() instanceof ItemCapacitor) {
//                            inventory.insertItem(0, test2, false);
//                            playerIn.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
//                            break;
//                        }
//                    }
//                    if (inventory.getStackInSlot(0).isEmpty())
//                        inventory.insertItem(0, emulatedTool, false);
//                } else {
//                    test = inventory.extractItem(0, 1, false);
//                    if (test.isEmpty())
//                        for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++) {
//                            if (playerIn.inventory.getStackInSlot(i) == ItemStack.EMPTY) {
//                                playerIn.inventory.setInventorySlotContents(i, test);
//                                break;
//                            }
//                        }
//                }
//            }
//        } else System.out.println("Power fist does not have capability");
//        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
//    }
//
//
//    @Override
//    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//
//        System.out.println("Doing something here");
//        return EnumActionResult.PASS;
//    }
//
//    @Override
//    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        System.out.println("Doing something here");
//        return EnumActionResult.PASS;
//    }
//
//    @Override
//    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
//
//    }
//
//    @Override
//    public String getCategory() {
//        return MuseCommonStrings.CATEGORY_TOOL;
//    }
//
//    @Override
//    public String getDataName() {
//        return MPSConstants.MODULE_DEBUG;
//    }
//
//    @SideOnly(Side.CLIENT)
//    @Override
//    public TextureAtlasSprite getIcon(ItemStack item) {
//        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(Items.COMPARATOR)).getParticleTexture();
//    }
//
//
////    public static final String IGNITION_ENERGY_CONSUMPTION = "Ignition Energy Consumption";
////    public final ItemStack fas = MPSItems;
////    final Random ran = new Random();
////
////    public FlintAndSteelModule(List<IModularItemBase> validItems) {
////        super(validItems);
////        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
////        addInstallCost(fas);
////        addBasePropertyDouble(IGNITION_ENERGY_CONSUMPTION, 1000, "J");
////    }
////
//




}
