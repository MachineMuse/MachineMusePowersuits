package net.machinemuse.numina.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:46 AM, 11/13/13
 * <p>
 * Ported to Java lehjr on 10/10/16.
 */
public class MuseTileEntity extends TileEntity {
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    public Integer getInteger(NBTTagCompound nbt, String name) {
        if (nbt.hasKey(name))
            return nbt.getInteger(name);
        else
            return null;
    }

    public Double getDouble(NBTTagCompound nbt, String name) {
        if (nbt.hasKey(name))
            return nbt.getDouble(name);
        else
            return null;
    }

    public Boolean getBoolean(NBTTagCompound nbt, String name) {
        if (nbt.hasKey(name))
            return nbt.getBoolean(name);
        else
            return null;
    }

    @Nonnull
    public ItemStack getItemStack(NBTTagCompound nbt, String name) {
        if (nbt.hasKey(name))
            return new ItemStack(nbt.getCompoundTag(name));
        else
            return ItemStack.EMPTY;
    }

    public void writeItemStack(NBTTagCompound nbt, String name, @Nonnull ItemStack stack) {
        NBTTagCompound itemnbt = new NBTTagCompound();
        stack.writeToNBT(itemnbt);
        nbt.setTag(name, itemnbt);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }
}