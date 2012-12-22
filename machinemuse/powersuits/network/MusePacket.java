/**
 * 
 */
package machinemuse.powersuits.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.MuseLogger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.Player;

/**
 * @author MachineMuse
 * 
 */
public abstract class MusePacket {
	protected Player player;

	protected ByteArrayOutputStream bytes;

	protected Packet250CustomPayload packet;

	protected MusePacket(Player player) {
		this.player = player;
	}

	/**
	 * Prepares a Custom-Payload packet for writing
	 * 
	 * @param id
	 * @return
	 */
	public DataOutputStream wrapNewPacket(MusePacketHandler.PacketTypes type) {
		bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		int id = type.ordinal();
		try {
			data.write(id);
		} catch (IOException e) {
			MuseLogger.logError("PROBLEM WRITING PACKET TO SEND D:");
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Prepares a Custom-Payload packet for sending after data has been written
	 * 
	 */
	public void endWrapPacket() {

		packet = new Packet250CustomPayload(Config.getNetworkChannelName(),
				bytes.toByteArray());
	}

	/**
	 * Gets the MC packet associated with this MusePacket
	 * 
	 * @return Packet250CustomPayload
	 */
	public Packet250CustomPayload getPacket() {
		return packet;
	}

	/**
	 * Called by the network manager since it does all the packet mapping
	 */
	public abstract void handleSelf();

	/**
	 * Reads a ItemStack from the InputStream
	 */
	public static ItemStack readItemStack(DataInputStream data)
			throws IOException
	{
		ItemStack stack = null;
		short itemID = data.readShort();

		if (itemID >= 0)
		{
			byte stackSize = data.readByte();
			short damageAmount = data.readShort();
			stack = new ItemStack(itemID, stackSize, damageAmount);
			stack.stackTagCompound = readNBTTagCompound(data);
		}

		return stack;
	}

	/**
	 * Writes the ItemStack's ID (short), then size (byte), then damage. (short)
	 */
	public static void writeItemStack(ItemStack stack,
			DataOutputStream outputStream) throws IOException
	{
		if (stack == null)
		{
			outputStream.writeShort(-1);
		}
		else
		{
			outputStream.writeShort(stack.itemID);
			outputStream.writeByte(stack.stackSize);
			outputStream.writeShort(stack.getItemDamage());
			NBTTagCompound nbt = null;

			if (stack.getItem().isDamageable()
					|| stack.getItem().getShareTag())
			{
				nbt = stack.stackTagCompound;
			}

			writeNBTTagCompound(nbt, outputStream);
		}
	}

	/**
	 * Reads a compressed NBTTagCompound from the InputStream
	 */
	public static NBTTagCompound readNBTTagCompound(
			DataInputStream data) throws IOException
	{
		short length = data.readShort();

		if (length < 0)
		{
			return null;
		}
		else
		{
			byte[] fullData = new byte[length];
			data.readFully(fullData);
			return CompressedStreamTools.decompress(fullData);
		}
	}

	/**
	 * Writes a compressed NBTTagCompound to the OutputStream
	 */
	protected static void writeNBTTagCompound(
			NBTTagCompound par0NBTTagCompound,
			DataOutputStream par1DataOutputStream) throws IOException
	{
		if (par0NBTTagCompound == null)
		{
			par1DataOutputStream.writeShort(-1);
		}
		else
		{
			byte[] var2 = CompressedStreamTools.compress(par0NBTTagCompound);
			par1DataOutputStream.writeShort((short) var2.length);
			par1DataOutputStream.write(var2);
		}
	}

}
