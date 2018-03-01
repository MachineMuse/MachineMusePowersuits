//package net.machinemuse.powersuits.item.module.tool;
//
//import li.cil.oc.Settings;
//import li.cil.oc.api.API;
//import li.cil.oc.api.machine.Machine;
//import li.cil.oc.common.GuiType;
//import li.cil.oc.common.item.Terminal;
//import li.cil.oc.common.tileentity.Rack;
//import li.cil.oc.server.PacketSender;
//import li.cil.oc.server.component.Server;
//import net.machinemuse.item.powersuits.module.PowerModuleBase;
//import net.machinemuse.numina.api.module.EnumModuleTarget;
//import net.machinemuse.numina.api.module.IRightClickModule;
//import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
//import net.machinemuse.powersuits.item.ItemComponent;
//import net.machinemuse.powersuits.utils.MuseItemUtils;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.EnumActionResult;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.common.Loader;
//
//import java.util.UUID;
//
////import li.cil.oc.api.internal.Server;
////import li.cil.oc.common.item.Terminal;
////import scala.Enumeration.Value;
//
///**
// * Created by User: Andrew2448
// * 8:42 PM 6/17/13
// */
//public class OCTerminalModule extends PowerModuleBase implements IRightClickModule {
//    private ItemStack terminal;
//
//    public OCTerminalModule(String resourceDommain, String UnlocalizedName) {
//        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
//        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 4));
//        terminal = API.items.get("terminal").createItemStack(1);
//        addInstallCost(terminal);
//    }
//
//    @Override
//    public String getCategory() {
//        return MPSModuleConstants.CATEGORY_TOOL;
//    }
//
//    @Override
//    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        if (!playerIn.isSneaking() && itemStackIn.hasTagCompound()) {
//            String key = itemStackIn.getTagCompound().getString("oc:key");
//            String server = itemStackIn.getTagCompound().getString("oc:server");
//
//            if (key != null && !key.isEmpty() && server != null && !server.isEmpty()) {
//                if (worldIn.isRemote) {
//                    //		    player.openGui( li.cil.oc.OpenComputers$.MODULE$, GuiType.Terminal().id(), world, 0, 0, 0);
//                    playerIn.openGui( Loader.instance().getIndexedModList().get("OpenComputers").getMod() , GuiType.Terminal().id(), worldIn, 0, 0, 0);
//                }
//                playerIn.swingItem();
//            }
//        }
//
//
//
//
//        return null;
//    }
//
//    @Override
//    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        if (worldIn.getTileEntity(pos) instanceof Rack) {
//            Rack rack = (Rack) worldIn.getTileEntity(pos);
//            if (side == rack.facing().ordinal()) {
//                double l = 2 / 16.0;
//                double h = 14 / 16.0;
//                int slot = ((int) (((1 - hitY) - l) / (h - l) * 4));
//                if (slot >= 0 && slot <= 3 && rack.getStackInSlot(slot) != null) {
//                    if (!worldIn.isRemote) {
//                        Server server = ((Server) rack
//                                .server(slot));
//                        Terminal term = rack.terminals()[slot];
//                        Machine machine = server.machine();
//                        String key = UUID.randomUUID().toString();
//
//                        if (!stack.hasTagCompound()) {
//                            stack.setTagCompound(new NBTTagCompound());
//                        } else {
//                            term.keys().$minus$eq(stack.getTagCompound().getString("oc:key"));
//                        }
//
//                        int maxSize = Settings.get().terminalsPerTier()[(Math.min(2, server.tier()))];
//
//                        while (term.keys().size() >= maxSize) {
//                            term.keys().remove(0);
//                        }
//
//                        term.keys().$plus$eq(key);
//
//                        term.connect(machine.node());
//
//                        PacketSender.sendServerState(rack, slot, scala.Option.apply((EntityPlayerMP) playerIn));
//                        stack.getTagCompound().setString("oc:key", key);
//                        stack.getTagCompound().setString("oc:server", machine.node().address());
//                        playerIn.inventory.markDirty();
//                    }
//                }
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        if (world.getTileEntity(pos) instanceof Rack) {
//            Rack rack = (Rack) world.getTileEntity(pos);
//            if (side == rack.facing().ordinal()) {
//                double l = 2 / 16.0;
//                double h = 14 / 16.0;
//                int slot = ((int) (((1 - hitY) - l) / (h - l) * 4));
//                if (slot >= 0 && slot <= 3 && rack.getStackInSlot(slot) != null) {
//                    if (!world.isRemote) {
//                        Server server = ((Server) rack.server(slot));
//                        Terminal term = rack.terminals()[slot];
//                        Machine machine = server.machine();
//                        String key = UUID.randomUUID().toString();
//
//                        if (!stack.hasTagCompound()) {
//                            stack.setTagCompound(new NBTTagCompound());
//                        } else {
//                            term.keys().$minus$eq(stack.getTagCompound().getString("oc:key"));
//                        }
//
//                        int maxSize = Settings.get().terminalsPerTier()[(Math.min(2, server.tier()))];
//
//                        while (term.keys().size() >= maxSize) {
//                            term.keys().remove(0);
//                        }
//
//                        term.keys().$plus$eq(key);
//
//                        term.connect(machine.node());
//
//                        PacketSender.sendServerState(rack, slot, scala.Option.apply((EntityPlayerMP) player));
//                        stack.getTagCompound().setString("oc:key", key);
//                        stack.getTagCompound().setString("oc:server", machine.node().address());
//                        player.inventory.markDirty();
//                    }
//                }
//            }
//        }
//        if (!player.isSneaking() && stack.hasTagCompound()) {
//            String key = stack.getTagCompound().getString("oc:key");
//            String server = stack.getTagCompound().getString("oc:server");
//
//            if (key != null && !key.isEmpty() && server != null && !server.isEmpty()) {
//                if (world.isRemote) {
//                    //                  player.openGui( li.cil.oc.OpenComputers$.MODULE$, GuiType.Terminal().id(), world, 0, 0, 0);
//                    player.openGui( Loader.instance().getIndexedModList().get("OpenComputers").getMod(), GuiType.Terminal().id(), world, 0, 0, 0);
//                }
//                player.swingItem();
//            }
//        }
//        return false;
//
//
//
//
//        return null;
//    }
//
//
//
//    public float minF(float a, float b) {
//        return a < b ? a : b;
//    }
//}
//
//
//// @Override
//// public String getUnlocalizedName() {
////         return MPSModuleConstants.MODULE_OC_TERMINAL;
//// }
////
//// @Override
//// public String getDescription() {
////         return "An OpenComputers terminal integrated in your power tool.";
//// }
////
//
////
//
//
//
