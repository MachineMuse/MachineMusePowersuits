//package net.machinemuse.general.gui.frame;
//
//import net.minecraft.util.StatCollector;
//import net.machinemuse.numina.geometry.MusePoint2D;
//import net.machinemuse.numina.general.MuseLogger;
//import java.util.List;
//import net.machinemuse.utils.render.GuiIcons;
//import scala.Predef$;
//import scala.collection.mutable.Buffer$;
//import scala.collection.mutable.Buffer;
//import net.minecraft.nbt.NBTBase;
//import net.minecraft.client.entity.EntityClientPlayerMP;
//import net.minecraft.item.Item;
//import net.minecraft.nbt.NBTTagCompound;
//import net.machinemuse.numina.network.MusePacket;
//import net.machinemuse.numina.network.PacketSender;
//import net.minecraft.entity.player.EntityPlayer;
//import net.machinemuse.powersuits.network.packets.MusePacketColourInfo;
//import net.minecraft.client.Minecraft;
//import scala.reflect.ClassTag$;
//import scala.Array$;
//import scala.runtime.BoxedUnit;
//import net.machinemuse.powersuits.item.ItemPowerArmor;
//import scala.Some;
//import net.machinemuse.utils.MuseItemUtils;
//import scala.None$;
//import net.minecraft.nbt.NBTTagIntArray;
//import scala.Function0;
//import scala.Function1;
//import scala.Option;
//import net.machinemuse.general.gui.clickable.ClickableSlider;
//import net.machinemuse.numina.geometry.DrawableMuseRect;
//import net.machinemuse.numina.geometry.Colour;
//import net.machinemuse.numina.geometry.MuseRect;
//import scala.reflect.ScalaSignature;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 4:19 AM, 03/05/13
// *
// * Ported to Java by lehjr on 11/2/16.
// */
//
//public class ColourPickerFrame implements IGuiFrame {
//    private final MuseRect borderRef;
//    private final Colour insideColour;
//    private final Colour borderColour;
//    private final ItemSelectionFrame itemSelector;
//    private final DrawableMuseRect border;
//    private final ClickableSlider rslider;
//    private final ClickableSlider gslider;
//    private final ClickableSlider bslider;
//    private ClickableSlider selectedSlider;
//    private int selectedColour;
//    private int decrAbove;
//
//    public ColourPickerFrame(final MuseRect borderRef, final Colour insideColour, final Colour borderColour, final ItemSelectionFrame itemSelector) {
//        this.borderRef = borderRef;
//        this.insideColour = insideColour;
//        this.borderColour = borderColour;
//        this.itemSelector = itemSelector;
//        this.border = new DrawableMuseRect(borderRef, insideColour, borderColour);
//        this.rslider = new ClickableSlider(new MusePoint2D(this.border().centerx(), this.border().top() + 8), this.border().width() - 10, StatCollector.translateToLocal("gui.red"));
//        this.gslider = new ClickableSlider(new MusePoint2D(this.border().centerx(), this.border().top() + 24), this.border().width() - 10, StatCollector.translateToLocal("gui.green"));
//        this.bslider = new ClickableSlider(new MusePoint2D(this.border().centerx(), this.border().top() + 40), this.border().width() - 10, StatCollector.translateToLocal("gui.blue"));
//        this.selectedSlider = null;
//        this.selectedColour = 0;
//        this.decrAbove = -1;
//    }
//
//    public Colour borderColour() {
//        return this.borderColour;
//    }
//
//    public ItemSelectionFrame itemSelector() {
//        return this.itemSelector;
//    }
//
//    public DrawableMuseRect border() {
//        return this.border;
//    }
//
//    public ClickableSlider rslider() {
//        return this.rslider;
//    }
//
//    public ClickableSlider gslider() {
//        return this.gslider;
//    }
//
//    public ClickableSlider bslider() {
//        return this.bslider;
//    }
//
//    public int selectedColour() {
//        return this.selectedColour;
//    }
//
//    public int[] colours() {
//        return (getOrCreateColourTag() != null) ? getOrCreateColourTag().func_150302_c() /*getIntArray()*/ : new int[0];
//    }
//
//    public NBTTagIntArray getOrCreateColourTag() {
//        if (this.itemSelector().getSelectedItem() == null) {
//            return null;
//        }
//        final NBTTagCompound renderSpec = MuseItemUtils.getMuseRenderTag(this.itemSelector().getSelectedItem().getItem());
//        Some some;
//        if (renderSpec.hasKey("colours") && renderSpec.getTag("colours") instanceof NBTTagIntArray) {
//            some = new Some((Object)renderSpec.getTag("colours"));
//        }
//        else {
//            final Item item = this.itemSelector().getSelectedItem().getItem().getItem();
//            if (item instanceof ItemPowerArmor) {
//                final ItemPowerArmor itemPowerArmor = (ItemPowerArmor)item;
//                final int[] intArray = { itemPowerArmor.getColorFromItemStack(this.itemSelector().getSelectedItem().getItem()).getInt(), itemPowerArmor.getGlowFromItemStack(this.itemSelector().getSelectedItem().getItem()).getInt() };
//                renderSpec.setIntArray("colours", intArray);
//                final BoxedUnit unit = BoxedUnit.UNIT;
//            }
//            else {
//                final int[] intArray2 = (int[])Array$.MODULE$.empty(ClassTag$.MODULE$.Int());
//                renderSpec.setIntArray("colours", intArray2);
//                final BoxedUnit unit2 = BoxedUnit.UNIT;
//            }
//            final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//            if (player.worldObj.isRemote) {
//                PacketSender.sendToServer(new MusePacketColourInfo((EntityPlayer)player, this.itemSelector().getSelectedItem().inventorySlot, this.colours()));
//            }
//            some = new Some((Object)renderSpec.getTag("colours"));
//        }
//        return (Option<NBTTagIntArray>)some;
//    }
//
//    public Option<NBTTagIntArray> setColourTagMaybe(final int[] newarray) {
//        if (this.itemSelector().getSelectedItem() == null) {
//            return (Option<NBTTagIntArray>)None$.MODULE$;
//        }
//        final NBTTagCompound renderSpec = MuseItemUtils.getMuseRenderTag(this.itemSelector().getSelectedItem().getItem());
//        renderSpec.setTag("colours", (NBTBase)new NBTTagIntArray(newarray));
//        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//        if (player.worldObj.isRemote) {
//            PacketSender.sendToServer(new MusePacketColourInfo((EntityPlayer)player, this.itemSelector().getSelectedItem().inventorySlot, this.colours()));
//        }
//        return (Option<NBTTagIntArray>)new Some((Object)renderSpec.getTag("colours"));
//    }
//
//    public Buffer<Object> importColours() {
//        final Buffer colours = (Buffer)Buffer$.MODULE$.empty();
//        return (Buffer<Object>)colours;
//    }
//
//    public void refreshColours() {
//    }
//
//    @Override
//    public void onMouseUp(final double x, final double y, final int button) {
//        this.selectedSlider = null;
//    }
//
//    @Override
//    public void update(final double mousex, final double mousey) {
//        if (this.selectedSlider != null) {
//            this.selectedSlider.setValueByX(mousex);
//            if (colours().length > selectedColour) {
//                colours(selectedColour) = Colour.getInt(rslider.value(), gslider.value(), bslider.value(), 1.0);
//                EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//                if (player.worldObj.isRemote)
//                    PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem().inventorySlot, colours()));
//            }
//        }
//    }
//
//
//    //  def update(mousex: Double, mousey: Double) {
////    selectedSlider.foreach(s => {
////      s.setValueByX(mousex)
////      if (colours.size > selectedColour) {
////        colours(selectedColour) = Colour.getInt(rslider.value, gslider.value, bslider.value, 1.0)
////        val player = Minecraft.getMinecraft.thePlayer
////        if (player.worldObj.isRemote)
////          PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem.inventorySlot, colours))
////      }
////    })
////  }
//
//
//
//    @Override
//    public void draw() {
//        this.border().draw();
//        this.rslider().draw();
//        this.gslider().draw();
//        this.bslider().draw();
//        Predef$.MODULE$.intArrayOps(this.colours()).indices().foreach((Function1)new ColourPickerFrame$$anonfun$draw.ColourPickerFrame$$anonfun$draw$1(this));
//        new GuiIcons.ArmourColourPatch(this.border().left() + 8 + Predef$.MODULE$.intArrayOps(this.colours()).size() * 8, this.border().bottom() - 16, Colour.WHITE, null, null, null, null);
//        new GuiIcons.SelectedArmorOverlay(this.border().left() + 8 + this.selectedColour() * 8, this.border().bottom() - 16, Colour.WHITE, null, null, null, null);
//        new GuiIcons.MinusSign(this.border().left() + 8 + this.selectedColour() * 8, this.border().bottom() - 24, Colour.RED, null, null, null, null);
//        new GuiIcons.PlusSign(this.border().left() + 8 + Predef$.MODULE$.intArrayOps(this.colours()).size() * 8, this.border().bottom() - 16, Colour.GREEN, null, null, null, null);
//    }
//
//    @Override
//    public List<String> getToolTip(final int x, final int y) {
//        return null;
//    }
//
//    public void onSelectColour(final int i) {
//        final Colour c = new Colour(this.colours()[i]);
//        this.rslider().setValue(c.r);
//        this.gslider().setValue(c.g);
//        this.bslider().setValue(c.b);
//        this.selectedColour_$eq(i);
//    }
//
//    @Override
//    public void onMouseDown(final double x, final double y, final int button) {
//        if (this.rslider().hitBox(x, y)) {
//            this.selectedSlider = this.rslider();
//        }
//        else if (this.gslider().hitBox(x, y)) {
//            this.selectedSlider = this.gslider();
//        }
//        else if (this.bslider().hitBox(x, y)) {
//            this.selectedSlider = this.bslider();
//        }
//        else {
//            this.selectedSlider = null;
//        }
//        if (y > this.border().bottom() - 16 && y < this.border().bottom() - 8) {
//            final int colourCol = (int)(x - this.border().left() - 8.0) / 8;
//            if (colourCol >= 0 && colourCol < Predef$.MODULE$.intArrayOps(this.colours()).size()) {
//                this.onSelectColour(colourCol);
//                final BoxedUnit boxedUnit = BoxedUnit.UNIT;
//            }
//            else if (colourCol == Predef$.MODULE$.intArrayOps(this.colours()).size()) {
//                MuseLogger.logDebug("Adding");
//                this.getOrCreateColourTag().map((Function1)new ColourPickerFrame$$anonfun$onMouseDown.ColourPickerFrame$$anonfun$onMouseDown$1(this));
//            }
//            else {
//                final BoxedUnit boxedUnit = BoxedUnit.UNIT;
//            }
//        }
//        else {
//            final BoxedUnit boxedUnit = BoxedUnit.UNIT;
//        }
//        if (y > this.border().bottom() - 24 && y < this.border().bottom() - 16 && x > this.border().left() + 8 + this.selectedColour() * 8 && x < this.border().left() + 16 + this.selectedColour() * 8) {
//            this.getOrCreateColourTag().foreach((Function1)new ColourPickerFrame$$anonfun$onMouseDown.ColourPickerFrame$$anonfun$onMouseDown$2(this));
//        }
//    }
//
//    public int[] getIntArray(final NBTTagIntArray e) {
//        return e.func_150302_c();
//    }
//}