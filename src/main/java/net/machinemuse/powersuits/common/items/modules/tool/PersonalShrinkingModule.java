//package net.machinemuse.powersuits.common.items.modules.tool;
//
//import net.machinemuse.api.IModularItem;
//import net.machinemuse.api.moduletrigger.IPlayerTickModule;
//import net.machinemuse.api.moduletrigger.IRightClickModule;
//import net.machinemuse.powersuits.common.items.ItemComponent;
//import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
//import net.machinemuse.utils.MuseCommonStrings;
//import net.machinemuse.utils.MuseItemUtils;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.*;
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
///*
//    TODO: the mechanics have changed a bit. This module will req
// */
//public class PersonalShrinkingModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
//    public static final String MODULE_CM_PSD = "Personal Shrinking Device";
//    private final ItemStack cpmPSD = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("cm2", "psd")), 1);
//    public PersonalShrinkingModule(List<IModularItem> validItems) {
//        super(validItems);
//        NBTTagCompound nbt = new NBTTagCompound();
//        nbt.setInteger("fluid", 4000);
//        cpmPSD.setTagCompound(nbt);
//        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 4));
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
//    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
//        return new ActionResult(EnumActionResult.FAIL, itemStackIn);
//    }
//
//    @Override
//    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        return EnumActionResult.PASS;
//    }
//
//    @Override
//    public EnumActionResult onItemUseFirst(ItemStack itemStackIn, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        return cpmPSD.getItem().onItemUseFirst(itemStackIn, player, world, pos, side, hitX, hitY, hitZ, hand);
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
//        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(cpmPSD).getParticleTexture();
//    }
//}
