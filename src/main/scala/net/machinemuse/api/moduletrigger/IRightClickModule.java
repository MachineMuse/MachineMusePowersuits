package net.machinemuse.api.moduletrigger;

import net.machinemuse.api.IPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRightClickModule extends IPowerModule {
    public ActionResult onRightClick(EntityPlayer player, World world, ItemStack item, EnumHand hand);

    public EnumActionResult onItemUse(ItemStack itemStack,
                                      EntityPlayer player,
                                      World world,
                                      BlockPos pos,
                                      EnumHand hand,
                                      EnumFacing facing,
                                      float hitX,
                                      float hitY,
                                      float hitZ);

    @SuppressWarnings("incomplete-switch")
    public boolean onItemUseFirst(
            ItemStack itemStack, EntityPlayer player, World world,
            BlockPos pos,
            EnumFacing side, float hitX, float hitY, float hitZ);

    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4);

    public EnumAction getItemUseAction(ItemStack stack);
}
