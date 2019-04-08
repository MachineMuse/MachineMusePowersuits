package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.basemod.Numina;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketInstallModuleRequest {
    protected static int playerID;
    int itemSlot;
    String moduleName;

    public MusePacketInstallModuleRequest() {
    }

    public MusePacketInstallModuleRequest(int playerID, int itemSlot, String moduleName) {
        this.playerID = playerID;
        this.itemSlot = itemSlot;
        this.moduleName = moduleName;
    }

    public static void encode(MusePacketInstallModuleRequest msg, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(msg.playerID);
        packetBuffer.writeInt(msg.itemSlot);
        packetBuffer.writeString(msg.moduleName);
    }

    public static MusePacketInstallModuleRequest decode(PacketBuffer packetBuffer) {
        return new MusePacketInstallModuleRequest(
                packetBuffer.readInt(),
                packetBuffer.readInt(),
                packetBuffer.readString(500));
    }

    public static void handle(MusePacketInstallModuleRequest message, Supplier<NetworkEvent.Context> ctx) {
        MuseLogger.logger.error("this has not been implemented yet");

//        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
//
//
//            EntityPlayerMP player = ctx.getServerHandler().player;
//            player.getServerWorld().addScheduledTask(() -> {
//                int itemSlot = message.itemSlot;
//                String moduleName = message.moduleName;
//                ItemStack stack = player.inventory.getStackInSlot(itemSlot);
//                if (moduleName != null) {
//                    InventoryPlayer inventory = player.inventory;
//                    IPowerModule moduleType = ModuleManager.INSTANCE.getModule(moduleName);
//                    if (moduleType == null || !moduleType.isAllowed()) {
//                        player.sendMessage(new TextComponentString("Server has disallowed this module. Sorry!"));
//                    } else {
//                        NonNullList<ItemStack> cost = ModuleManager.INSTANCE.getInstallCost(moduleName);
//                        if ((!ModuleManager.INSTANCE.itemHasModule(stack, moduleName) && MuseItemUtils.hasInInventory(cost, player.inventory)) || player.capabilities.isCreativeMode) {
//                            MuseNBTUtils.removeMuseValuesTag(stack);
//                            ModuleManager.INSTANCE.itemAddModule(stack, moduleType);
//                            for (ItemStack stackInCost : cost) {
//                                ElectricItemUtils.givePlayerEnergy(player, MPSConfig.INSTANCE.rfValueOfComponent(stackInCost));
//                            }
//
//                            if (!player.capabilities.isCreativeMode) {
//                                MuseItemUtils.deleteFromInventory(cost, inventory);
//                            }
//                            // use builtin handler
//                            player.inventoryContainer.detectAndSendChanges();
//                        }
//                    }
//                }
//            });
//        }
    }
}