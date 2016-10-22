//package net.machinemuse.general.gui.frame;
//
//import net.machinemuse.general.gui.clickable.ClickableSlider;
//import net.machinemuse.numina.general.MuseLogger;
//import net.machinemuse.numina.geometry.Colour;
//import net.machinemuse.numina.geometry.DrawableMuseRect;
//import net.machinemuse.numina.geometry.MusePoint2D;
//import net.machinemuse.numina.geometry.MuseRect;
//import net.machinemuse.numina.network.PacketSender;
//import net.machinemuse.powersuits.item.ItemPowerArmor;
//import net.machinemuse.powersuits.network.packets.MusePacketColourInfo;
//import net.machinemuse.utils.MuseItemUtils;
//import net.machinemuse.utils.render.GuiIcons;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.EntityClientPlayerMP;
//import net.minecraft.item.Item;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagIntArray;
//import net.minecraft.util.StatCollector;
//import org.apache.commons.lang3.ArrayUtils;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 4:19 AM, 03/05/13
// *
// * Ported to Java by lehjr on 10/20/16.
// */
//public class ColourPickerFrame implements IGuiFrame {
//    private final MuseRect borderRef;
//    private final Colour insideColour;
//    private final Colour borderColour;
//    private final ItemSelectionFrame itemSelector;
//    private final DrawableMuseRect border;
//    private final ClickableSlider rslider;
//    private final ClickableSlider gslider;
//    private final ClickableSlider bslider;
//    //    private Option<ClickableSlider> selectedSlider;
//    private ClickableSlider selectedSlider;
//    private int selectedColour;
//    private int decrAbove;
//
//
//    public ColourPickerFrame(final MuseRect borderRef, final Colour insideColour, final Colour borderColour, final ItemSelectionFrame itemSelector) {
//        this.borderRef = borderRef;
//        this.insideColour = insideColour;
//        this.borderColour = borderColour;
//        this.itemSelector = itemSelector;
//        this.border = new DrawableMuseRect(borderRef, insideColour, borderColour);
//        this.rslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + (double)8), this.border.width() - (double)10, StatCollector.translateToLocal((String)"gui.red"));
//        this.gslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + (double)24), this.border.width() - (double)10, StatCollector.translateToLocal((String)"gui.green"));
//        this.bslider = new ClickableSlider(new MusePoint2D(this.border.centerx(), this.border.top() + (double)40), this.border.width() - (double)10, StatCollector.translateToLocal((String)"gui.blue"));
//        this.selectedSlider = null;
//        this.selectedColour = 0;
//        this.decrAbove = -1;
//    }
//
//    int[] colours() {
//        int[] ret = getOrCreateColourTag().func_150302_c();
//        return (ret != null) ? ret : new int[0];
//    }
//
////    public int[] colours() {
////        return (int[])this.getOrCreateColourTag().map((Function1)new scala.Serializable(this){
////
////            public final int[] apply(NBTTagIntArray e) {
////                return e.func_150302_c();
////            }
////        }).getOrElse((Function0)new scala.Serializable(this){
////
////            public final int[] apply() {
////                return new int[0];
////            }
////        });
////    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    @Override
//    public void onMouseDown(double x, double y, int button) {
//        if (rslider.hitBox(x, y)) {
//            selectedSlider = rslider;
//        } else if (gslider.hitBox(x, y)) {
//            selectedSlider = gslider;
//        } else if (bslider.hitBox(x, y)) {
//            selectedSlider = bslider;
//        } else {
//            selectedSlider = null;
//        }
//        // add
//        if (y > border.bottom() - 16 && y < border.bottom() - 8) {
//            int colourCol = ((int)(x - border.left() - 8.0)) / 8;
//            if (colourCol >= 0 && colourCol < colours().length) {
//                onSelectColour(colourCol);
//            } else if (colourCol == colours().length) {
//                // append White
//                MuseLogger.logDebug("Adding");
//                int[] intArray = ArrayUtils.addAll(getIntArray(getOrCreateColourTag()), new int[]{Colour.WHITE.getInt()});
//            }
//            // remove
//            if (y > border.bottom() - 24 && y < border.bottom() - 16 && x > border.left() + 8 + selectedColour * 8 && x < border.left() + 16 + selectedColour * 8) {
//
//
//                NBTTagIntArray nbtTagIntArray = getOrCreateColourTag();
//
//                b.
//
//
//                for (NBTTagCompound tag: ) {
//
//                }
//
//
//
//                .foreach(e => {
//        if (getIntArray(e).size > 1) {
//          setColourTagMaybe( getIntArray(e) diff Array(getIntArray(e)(selectedColour)))
//          decrAbove = selectedColour
//          if (selectedColour == getIntArray(e).size) {
//            selectedColour = selectedColour - 1
//          }
//          val player = Minecraft.getMinecraft.thePlayer
//          if (player.worldObj.isRemote)
//            PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem.inventorySlot, e.func_150302_c))
//        }
//      })
//
//            }
//
//        }
//    }
//
//    @Override
//    public void onMouseDown(double x, double y, int button) {
////        BoxedUnit boxedUnit;
//        if (this.rslider.hitBox(x, y)) {
//            this.selectedSlider = this.rslider;
//        }
//        else if (this.gslider.hitBox(x, y)) {
//            this.selectedSlider = this.gslider;
//        }
//        else if (this.bslider.hitBox(x, y)) {
//            this.selectedSlider = this.bslider;
//        }
//        else {
//            this.selectedSlider = null;
//        }
//        if (y > this.border.bottom() - (double)16 && y < this.border.bottom() - (double)8) {
//            int colourCol = (int)(x - this.border.left() - 8.0) / 8;
//            if (colourCol >= 0 && colourCol < Predef..MODULE$.intArrayOps(this.colours()).size()) {
//                this.onSelectColour(colourCol);
//                boxedUnit = BoxedUnit.UNIT;
//            } else if (colourCol == Predef..MODULE$.intArrayOps(this.colours()).size()) {
//                MuseLogger.logDebug("Adding");
//                boxedUnit = this.getOrCreateColourTag().map((Function1)new scala.Serializable(this){
//                    private final /* synthetic */ ColourPickerFrame $outer;
//
//                    public final Option<NBTTagIntArray> apply(NBTTagIntArray e) {
//                        return this.$outer.setColourTagMaybe((int[])Predef..MODULE$.intArrayOps(this.$outer.getIntArray(e)).$colon$plus((Object)scala.runtime.BoxesRunTime.boxToInteger((int)Colour.WHITE.getInt()), ClassTag..MODULE$.Int()));
//                    }
//                });
//            } else {
//                boxedUnit = BoxedUnit.UNIT;
//            }
//        } else {
//            boxedUnit = BoxedUnit.UNIT;
//        }
//        if (y > this.border().bottom() - (double)24 && y < this.border().bottom() - (double)16 && x > this.border().left() + (double)8 + (double)(this.selectedColour() * 8) && x < this.border().left() + (double)16 + (double)(this.selectedColour() * 8)) {
//            this.getOrCreateColourTag().foreach((Function1)new scala.Serializable(this){
//                private final /* synthetic */ ColourPickerFrame $outer;
//
//                public final void apply(NBTTagIntArray e) {
//                    if (Predef..MODULE$.intArrayOps(this.$outer.getIntArray(e)).size() > 1) {
//                        this.$outer.setColourTagMaybe((int[])Predef..MODULE$.intArrayOps(this.$outer.getIntArray(e)).diff((scala.collection.GenSeq)Predef..MODULE$.wrapIntArray(new int[]{this.$outer.getIntArray(e)[this.$outer.selectedColour()]})));
//                        this.$outer.decrAbove_$eq(this.$outer.selectedColour());
//                        if (this.$outer.selectedColour() == Predef..MODULE$.intArrayOps(this.$outer.getIntArray(e)).size()) {
//                            this.$outer.selectedColour_$eq(this.$outer.selectedColour() - 1);
//                        }
//                        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//                        if (player.worldObj.isRemote) {
//                            PacketSender$.MODULE$.sendToServer(new MusePacketColourInfo((EntityPlayer)player, this.$outer.itemSelector().getSelectedItem().inventorySlot, e.func_150302_c()));
//                        }
//                    }
//                }
//            });
//        }
//    }
//
//
//    @Override
//    public void onMouseDown(final double x, final double y, final int button) {
//        if (this.rslider.hitBox(x, y)) {
//            this.selectedSlider = this.rslider;
//        }
//        else if (this.gslider.hitBox(x, y)) {
//            this.selectedSlider = this.gslider;
//        }
//        else if (this.bslider.hitBox(x, y)) {
//            this.selectedSlider = this.bslider;
//        }
//        else {
//            this.selectedSlider = null;
//        }
//        if (y > this.border.bottom() - 16 && y < this.border.bottom() - 8) {
//            final int colourCol = (int)(x - this.border.left() - 8.0) / 8;
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
//        if (y > this.border.bottom() - 24 && y < this.border.bottom() - 16 && x > this.border.left() + 8 + this.selectedColour * 8 && x < this.border.left() + 16 + this.selectedColour * 8) {
//            this.getOrCreateColourTag().foreach((Function1)new ColourPickerFrame$$anonfun$onMouseDown.ColourPickerFrame$$anonfun$onMouseDown$2(this));
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    @Override
//    public void onMouseUp(double x, double y, int button) {
//        selectedSlider = null;
//    }
//
//    @Override
//    public void update(double mousex, double mousey) {
//
//            selectedSlider.map(s => {
//                    s.setValueByX(mousex);
//            if (colours().length > selectedColour) {
//                colours(selectedColour) = Colour.getInt(rslider.value(), gslider.value(), bslider.value(), 1.0);
//                EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//                if (player.worldObj.isRemote)
//                    PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem().inventorySlot, colours()));
//            }
//        })
//
//    }
//
//    @Override
//    public void draw() {
//        border.draw();
//        rslider.draw();
//        gslider.draw();
//        bslider.draw();
//        for (int i = 0; i < colours().length; i++) {
//            new GuiIcons.ArmourColourPatch(border.left() + 8 + i * 8, border.bottom() - 16, new Colour(colours(i)), null, null, null, null);
//        }
//        new GuiIcons.ArmourColourPatch(border.left() + 8 + colours().length * 8, border.bottom() - 16, Colour.WHITE, null, null, null, null);
//        new GuiIcons.SelectedArmorOverlay(border.left() + 8 + selectedColour * 8, border.bottom() - 16, Colour.WHITE, null, null, null, null);
//        new GuiIcons.MinusSign(border.left() + 8 + selectedColour * 8, border.bottom() - 24, Colour.RED, null, null, null, null);
//        new GuiIcons.PlusSign(border.left() + 8 + colours().length * 8, border.bottom() - 16, Colour.GREEN, null, null, null, null);
//    }
//
//    @Override
//    public List<String> getToolTip(int x, int y) {
//        return null;
//    }
//
//    NBTTagIntArray getOrCreateColourTag() {
//        if (itemSelector.getSelectedItem() == null)
//            return null;
//        NBTTagCompound renderSpec = MuseItemUtils.getMuseRenderTag(itemSelector.getSelectedItem().getItem());
//        if (renderSpec.hasKey("colours") && renderSpec.getTag("colours") instanceof NBTTagIntArray)
//            return (NBTTagIntArray)renderSpec.getTag("colours");
//        else {
//            Item itembase = itemSelector.getSelectedItem().getItem().getItem();
//
//
//            if (itembase instanceof ItemPowerArmor) {
//                int[] intArray = {
//                        ((ItemPowerArmor) itembase).getColorFromItemStack(itemSelector.getSelectedItem().getItem()).getInt(),
//                        ((ItemPowerArmor) itembase).getGlowFromItemStack(itemSelector.getSelectedItem().getItem()).getInt()};
//
//                renderSpec.setIntArray("colours", intArray);
//            } else {
//                int[] intArray = new int[0];
//                renderSpec.setIntArray("colours", intArray);
//            }
//            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//            if (player.worldObj.isRemote)
//                PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem().inventorySlot, colours()));
//            return (NBTTagIntArray)renderSpec.getTag("colours");
//        }
//    }
//
//    NBTTagIntArray setColourTagMaybe(int[] newarray) {
//        if (itemSelector.getSelectedItem() == null)
//            return null;
//        NBTTagCompound renderSpec = MuseItemUtils.getMuseRenderTag(itemSelector.getSelectedItem().getItem());
//        renderSpec.setTag("colours", new NBTTagIntArray(newarray));
//        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//        if (player.worldObj.isRemote)
//            PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem().inventorySlot, colours()));
//        return (NBTTagIntArray)renderSpec.getTag("colours");
//    }
//
////    public Option<NBTTagIntArray> setColourTagMaybe(final int[] newarray) {
//    public NBTTagIntArray setColourTagMaybe(final int[] newarray) {
//        if (this.itemSelector().getSelectedItem() == null) {
//            return (NBTTagIntArray)None$.MODULE$;
//        }
//        final NBTTagCompound renderSpec = MuseItemUtils.getMuseRenderTag(this.itemSelector().getSelectedItem().getItem());
//        renderSpec.setTag("colours", (NBTBase)new NBTTagIntArray(newarray));
//        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//        if (player.worldObj.isRemote) {
//            PacketSender$.MODULE$.sendToServer(new MusePacketColourInfo((EntityPlayer)player, this.itemSelector().getSelectedItem().inventorySlot, this.colours()));
//        }
//        return (Option<NBTTagIntArray>)new Some((Object)renderSpec.getTag("colours"));
//    }
//
//    public Buffer<Object> importColours() {
//        final Buffer colours = (Buffer)Buffer$.MODULE$.empty();
//        return (Buffer<Object>)colours;
//    }
//
////        int[] importColours () {
////            return new int[0];
//////            colours = mutable.Buffer.empty[Int]
//////            return colours;
////        }
//
//        public void refreshColours() {
//        //    getOrCreateColourTag.map(coloursTag => {
//        //      val colourints: Array[Int] = coloursTag.intArray
//        //      val colourset: HashSet[Int] = HashSet.empty ++ colours ++ colourints
//        //      val colourarray = colourset.toArray
//        //      coloursTag.intArray = colourarray
//        //    })
//        }
//
//    public void onSelectColour(final int i) {
//        final Colour c = new Colour(this.colours()[i]);
//        this.rslider().setValue(c.r);
//        this.gslider().setValue(c.g);
//        this.bslider().setValue(c.b);
//        this.selectedColour =i;
//    }
//
//    public int[] getIntArray(final NBTTagIntArray e) {
//        return e.func_150302_c();
//    }
//}
//public class ColourPickerFrame implements IGuiFrame
//{
//    private final MuseRect borderRef;
//    private final Colour insideColour;
//    private final Colour borderColour;
//    private final ItemSelectionFrame itemSelector;
//    private final DrawableMuseRect border;
//    private final ClickableSlider rslider;
//    private final ClickableSlider gslider;
//    private final ClickableSlider bslider;
//    private Option<ClickableSlider> selectedSlider;
//    private int selectedColour;
//    private int decrAbove;
//
//    public MuseRect borderRef() {
//        return this.borderRef;
//    }
//
//    public Colour insideColour() {
//        return this.insideColour;
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
//
//
//    public Option<ClickableSlider> selectedSlider() {
//        return this.selectedSlider;
//    }
//
//    public void selectedSlider_$eq(final Option<ClickableSlider> x$1) {
//        this.selectedSlider = x$1;
//    }
//
//    public int selectedColour() {
//        return this.selectedColour;
//    }
//
//    public void selectedColour_$eq(final int x$1) {
//        this.selectedColour = x$1;
//    }
//
//    public int decrAbove() {
//        return this.decrAbove;
//    }
//
//    public void decrAbove_$eq(final int x$1) {
//        this.decrAbove = x$1;
//    }
//
//    public Option<NBTTagIntArray> getOrCreateColourTag() {
//        if (this.itemSelector().getSelectedItem() == null) {
//            return (Option<NBTTagIntArray>)None$.MODULE$;
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
//                PacketSender$.MODULE$.sendToServer(new MusePacketColourInfo((EntityPlayer)player, this.itemSelector().getSelectedItem().inventorySlot, this.colours()));
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
//            PacketSender$.MODULE$.sendToServer(new MusePacketColourInfo((EntityPlayer)player, this.itemSelector().getSelectedItem().inventorySlot, this.colours()));
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
//        this.selectedSlider_$eq((Option<ClickableSlider>)None$.MODULE$);
//    }
//
//    @Override
//    public void update(final double mousex, final double mousey) {
//        this.selectedSlider().foreach((Function1)new ColourPickerFrame$$anonfun$update.ColourPickerFrame$$anonfun$update$1(this, mousex));
//    }
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
//
//    public int[] getIntArray(final NBTTagIntArray e) {
//        return e.func_150302_c();
//    }
//
//
//}
//
//public class ColourPickerFrame
//
//
//    public MuseRect borderRef() {
//        return this.borderRef;
//    }
//
//    public Colour insideColour() {
//        return this.insideColour;
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
//
//
//    public Option<ClickableSlider> selectedSlider() {
//        return this.selectedSlider;
//    }
//
//    public void selectedSlider_$eq(Option<ClickableSlider> x$1) {
//        this.selectedSlider = x$1;
//    }
//
//    public int selectedColour() {
//        return this.selectedColour;
//    }
//
//    public void selectedColour_$eq(int x$1) {
//        this.selectedColour = x$1;
//    }
//
//    public int decrAbove() {
//        return this.decrAbove;
//    }
//
//    public void decrAbove_$eq(int x$1) {
//        this.decrAbove = x$1;
//    }
//
//    public Option<NBTTagIntArray> getOrCreateColourTag() {
//        Some some;
//        if (this.itemSelector().getSelectedItem() == null) {
//            return None..MODULE$;
//        }
//        NBTTagCompound renderSpec = MuseItemUtils.getMuseRenderTag(this.itemSelector().getSelectedItem().getItem());
//        if (renderSpec.hasKey("colours") && renderSpec.getTag("colours") instanceof NBTTagIntArray) {
//            some = new Some((Object)((NBTTagIntArray)renderSpec.getTag("colours")));
//        } else {
//            Item item = this.itemSelector().getSelectedItem().getItem().getItem();
//            if (item instanceof ItemPowerArmor) {
//                ItemPowerArmor itemPowerArmor = (ItemPowerArmor)item;
//                int[] intArray = new int[]{itemPowerArmor.getColorFromItemStack(this.itemSelector().getSelectedItem().getItem()).getInt(), itemPowerArmor.getGlowFromItemStack(this.itemSelector().getSelectedItem().getItem()).getInt()};
//                renderSpec.setIntArray("colours", intArray);
//                BoxedUnit boxedUnit = BoxedUnit.UNIT;
//            } else {
//                int[] intArray = (int[])Array..MODULE$.empty(ClassTag..MODULE$.Int());
//                renderSpec.setIntArray("colours", intArray);
//                BoxedUnit boxedUnit = BoxedUnit.UNIT;
//            }
//            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//            if (player.worldObj.isRemote) {
//                PacketSender$.MODULE$.sendToServer(new MusePacketColourInfo((EntityPlayer)player, this.itemSelector().getSelectedItem().inventorySlot, this.colours()));
//            }
//            some = new Some((Object)((NBTTagIntArray)renderSpec.getTag("colours")));
//        }
//        return some;
//    }
//
//    public Option<NBTTagIntArray> setColourTagMaybe(int[] newarray) {
//        if (this.itemSelector().getSelectedItem() == null) {
//            return None..MODULE$;
//        }
//        NBTTagCompound renderSpec = MuseItemUtils.getMuseRenderTag(this.itemSelector().getSelectedItem().getItem());
//        renderSpec.setTag("colours", (NBTBase)new NBTTagIntArray(newarray));
//        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//        if (player.worldObj.isRemote) {
//            PacketSender$.MODULE$.sendToServer(new MusePacketColourInfo((EntityPlayer)player, this.itemSelector().getSelectedItem().inventorySlot, this.colours()));
//        }
//        return new Some((Object)((NBTTagIntArray)renderSpec.getTag("colours")));
//    }
//
//    public Buffer<Object> importColours() {
//        Buffer colours2 = (Buffer)Buffer..MODULE$.empty();
//        return colours2;
//    }
//
//    public void refreshColours() {
//    }
//
//    @Override
//    public void onMouseUp(double x, double y, int button) {
//        this.selectedSlider_$eq((Option<ClickableSlider>)None..MODULE$);
//    }
//
//    @Override
//    public void update(double mousex, double mousey) {
//        this.selectedSlider().foreach((Function1)new scala.Serializable(this, mousex){
//            private final /* synthetic */ ColourPickerFrame $outer;
//            private final double mousex$1;
//
//            public final void apply(ClickableSlider s) {
//                s.setValueByX(this.mousex$1);
//                if (Predef..MODULE$.intArrayOps(this.$outer.colours()).size() > this.$outer.selectedColour()) {
//                    this.$outer.colours()[this.$outer.selectedColour()] = Colour.getInt(this.$outer.rslider().value(), this.$outer.gslider().value(), this.$outer.bslider().value(), 1.0);
//                    EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//                    if (player.worldObj.isRemote) {
//                        PacketSender$.MODULE$.sendToServer(new MusePacketColourInfo((EntityPlayer)player, this.$outer.itemSelector().getSelectedItem().inventorySlot, this.$outer.colours()));
//                    }
//                }
//            }
//        });
//    }
//
//    @Override
//    public void draw() {
//        this.border().draw();
//        this.rslider().draw();
//        this.gslider().draw();
//        this.bslider().draw();
//        Predef..MODULE$.intArrayOps(this.colours()).indices().foreach((Function1)new scala.Serializable(this){
//            private final /* synthetic */ ColourPickerFrame $outer;
//
//            public final GuiIcons.ArmourColourPatch apply(int i) {
//                return new GuiIcons.ArmourColourPatch(this.$outer.border().left() + (double)8 + (double)(i * 8), this.$outer.border().bottom() - (double)16, new Colour(this.$outer.colours()[i]), null, null, null, null);
//            }
//        });
//        new GuiIcons.ArmourColourPatch(this.border().left() + (double)8 + (double)(Predef..MODULE$.intArrayOps(this.colours()).size() * 8), this.border().bottom() - (double)16, Colour.WHITE, null, null, null, null);
//        new GuiIcons.SelectedArmorOverlay(this.border().left() + (double)8 + (double)(this.selectedColour() * 8), this.border().bottom() - (double)16, Colour.WHITE, null, null, null, null);
//        new GuiIcons.MinusSign(this.border().left() + (double)8 + (double)(this.selectedColour() * 8), this.border().bottom() - (double)24, Colour.RED, null, null, null, null);
//        new GuiIcons.PlusSign(this.border().left() + (double)8 + (double)(Predef..MODULE$.intArrayOps(this.colours()).size() * 8), this.border().bottom() - (double)16, Colour.GREEN, null, null, null, null);
//    }
//
//    @Override
//    public List<String> getToolTip(int x, int y) {
//        return null;
//    }
//
//    public void onSelectColour(int i) {
//        Colour c = new Colour(this.colours()[i]);
//        this.rslider().setValue(c.r);
//        this.gslider().setValue(c.g);
//        this.bslider().setValue(c.b);
//        this.selectedColour_$eq(i);
//    }
//
//
//    public int[] getIntArray(NBTTagIntArray e) {
//        return e.func_150302_c();
//    }
//
//
//}
