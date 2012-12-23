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
	protected static final int READ_ERROR = -150;

	protected Player player;

	protected ByteArrayOutputStream bytes;

	protected Packet250CustomPayload packet;
	protected DataOutputStream data;
	protected int id;

	protected MusePacket(Player player) {
		this.player = player;
		this.bytes = new ByteArrayOutputStream();
		this.data = new DataOutputStream(bytes);
		int id = MusePacketHandler.getTypeID(this);
		writeInt(id, data);
	}

	private String listBytes(byte[] bytes) {
		String s = "";
		for (byte b : bytes) {
			s = s + Byte.toString(b) + " ~ ";
		}
		return s;
	}

	protected MusePacket(Player player, DataInputStream data) {
		this.player = player;
	}

	/**
	 * Gets the MC packet associated with this MusePacket
	 * 
	 * @return Packet250CustomPayload
	 */
	public Packet250CustomPayload getPacket() {
		byte[] output = bytes.toByteArray();
		return new Packet250CustomPayload(Config.getNetworkChannelName(),
				bytes.toByteArray());
	}

	/**
	 * Called by the network manager since it does all the packet mapping
	 */
	public abstract void handleSelf();

	public static int readInt(DataInputStream data) {
		try {
			int read = data.readInt();
			return read;
		} catch (IOException e) {
			MuseLogger.logError("PROBLEM WRITING INT TO PACKET D:");
			e.printStackTrace();
			return READ_ERROR;
		}
	}

	public static void writeInt(int i, DataOutputStream data) {
		try {
			data.writeInt(i);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

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
			NBTTagCompound nbt,
			DataOutputStream data) throws IOException
	{
		if (nbt == null)
		{
			data.writeShort(-1);
		}
		else
		{
			byte[] compressednbt = CompressedStreamTools.compress(nbt);
			data.writeShort((short) compressednbt.length);
			data.write(compressednbt);
		}
	}
}
