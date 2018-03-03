package net.machinemuse.powersuits.item.module.debug;

import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * This module is used for debugging and may change
 */
public class DebugModule extends PowerModuleBase implements IRightClickModule {
    ItemStack emulatedTool;

//     <mekanism:energycube>§a.withTag({tier: 3, mekData: {energyStored: 1.28E8}})


    public DebugModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ALLITEMS, resourceDommain, UnlocalizedName);
        emulatedTool = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ic2", "lapotron_crystal")), 1);
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound mek = new NBTTagCompound();
        mek.setInteger("energyStored", 0);

        nbt.setInteger("tier", 3);
        nbt.setTag("mekData", mek);





        //
//
//
//
//
//
//        emulatedTool.setTagCompound();
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        System.out.println("Doing something here");

        IItemHandler inventoryHandler = itemStackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if(inventoryHandler !=null) {
            System.out.println("doing something here");


            if (inventoryHandler.getSlots() > 0) {
                ItemStack test = inventoryHandler.getStackInSlot(0);
                if (test.isEmpty()) {
                    for (int i=0; i< playerIn.inventory.getSizeInventory(); i++) {
                        ItemStack test2 = playerIn.inventory.getStackInSlot(i);
                        if (!test2.isEmpty() && test2 != itemStackIn) {
                            inventoryHandler.insertItem(0, test2, false);
                            playerIn.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                            break;
                        }
                    }
                    if (inventoryHandler.getStackInSlot(0).isEmpty())
                        inventoryHandler.insertItem(0, emulatedTool, false);
                } else {
                    test = inventoryHandler.extractItem(0, 1, false);
                    if (test.isEmpty())
                        for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++) {
                            if (playerIn.inventory.getStackInSlot(i) == ItemStack.EMPTY) {
                                playerIn.inventory.setInventorySlotContents(i, test);
                                break;
                            }
                        }
                }
            }
        } else System.out.println("Power fist does not have capability");
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
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
