//package net.machinemuse.powersuits.powermodule.weapon;
//
//import net.machinemuse.numina.item.IModularItem;
//import net.machinemuse.numina.module.IRightClickModule;
//import net.machinemuse.powersuits.client.event.MuseIcon;
//import net.machinemuse.powersuits.powermodule.PowerModuleBase;
//import net.machinemuse.powersuits.utils.MuseCommonStrings;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
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
//public class SonicWeaponModule extends PowerModuleBase implements IRightClickModule {
//
//    public static final String MODULE_SONIC_WEAPON = "Sonic Weapon";
//
//    public SonicWeaponModule(EnumModuleTarget moduleTarget) { {
//        super(validItems);
//    }
//
//    @Override
//    public String getCategory() {
//        return MuseCommonStrings.CATEGORY_WEAPON;
//    }
//
//    @Override
//    public String getDataName() {
//        return MODULE_SONIC_WEAPON;
//    }
//
//    @Override
//    public String getUnlocalizedName() { return "sonicWeapon";
//    }
//
//    @Override
//    public String getDescription() {
//        return "A high-amplitude, complex-frequency soundwave generator can have shattering or disorienting effects on foes and blocks alike.";
//    }
//
//    @Override
//    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
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
//        return null;
//    }
//
//    @Override
//    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
//    }
//
//    @Override
//    public TextureAtlasSprite getIcon(ItemStack item) {
//        return MuseIcon.sonicWeapon;
//    }
//}