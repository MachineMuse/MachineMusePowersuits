package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.basemod.Numina;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

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
public class MusePacketSalvageModuleRequest {
    protected static int playerID;
    int itemSlot;
    String moduleName;

    public MusePacketSalvageModuleRequest() {
    }

    public MusePacketSalvageModuleRequest(int playerID, int itemSlot, String moduleName) {
        this.playerID = playerID;
        this.itemSlot = itemSlot;
        this.moduleName = moduleName;
    }

    public static void encode(MusePacketSalvageModuleRequest msg, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(msg.playerID);
        packetBuffer.writeInt(msg.itemSlot);
        packetBuffer.writeString(msg.moduleName);
    }

    public static MusePacketSalvageModuleRequest decode(PacketBuffer packetBuffer) {
        return new MusePacketSalvageModuleRequest(
                packetBuffer.readInt(),
                packetBuffer.readInt(),
                packetBuffer.readString(500));
    }

    public static void handle(MusePacketSalvageModuleRequest message, Supplier<NetworkEvent.Context> ctx) {
        MuseLogger.logger.error("this has not been implemented yet");


//        if (ctx.side == Side.SERVER) {
//            final EntityPlayerMP player = ctx.getServerHandler().player;
//            player.getServerWorld().addScheduledTask(() -> {
//                int itemSlot= message.itemSlot;
//                String moduleName = message.moduleName;
//                if (moduleName != null) {
//                    ItemStack stack = player.inventory.getStackInSlot(itemSlot);
//                    NonNullList<ItemStack> refund = ModuleManager.INSTANCE.getInstallCost(moduleName);
//                    if (ModuleManager.INSTANCE.itemHasModule(stack, moduleName)) {
//                        MuseNBTUtils.removeMuseValuesTag(stack);
//                        ModuleManager.INSTANCE.removeModule(stack, moduleName);
//                        for (ItemStack refundItem : refund) {
//                            MuseItemUtils.giveOrDropItemWithChance(player, refundItem.copy(), MPSConfig.INSTANCE.getSalvageChance());
//                        }
//
//                        IPowerModule module = ModuleManager.INSTANCE.getModule(moduleName);
//                        if (stack.getItem() instanceof IModeChangingItem && module instanceof IRightClickModule) {
//                            if (module instanceof IEnchantmentModule)
//                                ((IEnchantmentModule) module).removeEnchantment(stack);
//                            ((IModeChangingItem) stack.getItem()).setActiveMode(stack, "");
//                        }
//                    }
//                }
//            });
//        }
    }
}