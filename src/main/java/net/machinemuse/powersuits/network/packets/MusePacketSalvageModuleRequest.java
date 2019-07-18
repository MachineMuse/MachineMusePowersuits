package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.module.IEnchantmentModule;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 * <p>
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketSalvageModuleRequest implements IMessage {
    EntityPlayer player;
    int itemSlot;
    String moduleName;

    public MusePacketSalvageModuleRequest() {

    }

    public MusePacketSalvageModuleRequest(EntityPlayer player, int itemSlot, String moduleName) {
        this.player = player;
        this.itemSlot = itemSlot;
        this.moduleName = moduleName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.itemSlot = buf.readInt();
        this.moduleName = MuseByteBufferUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(itemSlot);
        MuseByteBufferUtils.writeUTF8String(buf, moduleName);
    }

    public static class Handler implements IMessageHandler<MusePacketSalvageModuleRequest, IMessage> {
        @Override
        public IMessage onMessage(MusePacketSalvageModuleRequest message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                final EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    int itemSlot= message.itemSlot;
                    String moduleName = message.moduleName;
                    if (moduleName != null) {
                        ItemStack stack = player.inventory.getStackInSlot(itemSlot);
                        NonNullList<ItemStack> refund = ModuleManager.INSTANCE.getInstallCost(moduleName);
                        if (ModuleManager.INSTANCE.itemHasModule(stack, moduleName)) {
                            MuseNBTUtils.removeMuseValuesTag(stack);
                            ModuleManager.INSTANCE.removeModule(stack, moduleName);
                            for (ItemStack refundItem : refund) {
                                if (MuseMathUtils.nextDouble() < MPSConfig.INSTANCE.getSalvageChance()) {
                                    ItemHandlerHelper.giveItemToPlayer(player, refundItem);
                                }
                            }

                            IPowerModule module = ModuleManager.INSTANCE.getModule(moduleName);
                            if (stack.getItem() instanceof IModeChangingItem && module instanceof IRightClickModule) {
                                if (module instanceof IEnchantmentModule)
                                    ((IEnchantmentModule) module).removeEnchantment(stack);
                                ((IModeChangingItem) stack.getItem()).setActiveMode(stack, "");
                            }
                        }
                    }
                });
            }
            return null;
        }
    }
}
