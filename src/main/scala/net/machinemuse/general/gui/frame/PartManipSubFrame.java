//package net.machinemuse.general.gui.frame;
//
//import net.machinemuse.general.gui.clickable.ClickableItem;
//import net.machinemuse.numina.general.MuseLogger;
//import net.machinemuse.numina.general.MuseMathUtils;
//import net.machinemuse.numina.geometry.Colour;
//import net.machinemuse.numina.geometry.MuseRect;
//import net.machinemuse.numina.geometry.MuseRelativeRect;
//import net.machinemuse.numina.network.PacketSender;
//import net.machinemuse.numina.render.RenderState;
//import net.machinemuse.powersuits.client.render.modelspec.ModelPartSpec;
//import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
//import net.machinemuse.powersuits.client.render.modelspec.ModelSpec;
//import net.machinemuse.powersuits.network.packets.MusePacketCosmeticInfo;
//import net.machinemuse.utils.MuseItemUtils;
//import net.machinemuse.utils.render.GuiIcons;
//import net.machinemuse.utils.render.MuseRenderer;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.EntityClientPlayerMP;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemArmor;
//import net.minecraft.nbt.NBTBase;
//import net.minecraft.nbt.NBTTagCompound;
//import org.lwjgl.opengl.GL11;
//import scala.*;
//import scala.collection.TraversableOnce;
//import scala.reflect.ClassTag$;
//
//import java.io.Serializable;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 1:46 AM, 30/04/13
// *
// * Ported to Java by lehjr on 11/2/16.
// */
//public class PartManipSubFrame {
////    Array<ModelPartSpec> specs;
////    boolean open;
////    double mousex;
////    double mousey;
////    ModelSpec model;
////    ColourPickerFrame colourframe;
////    ItemSelectionFrame itemSelector;
////    MuseRelativeRect border;
//
//    private ModelSpec model;
//    private ColourPickerFrame colourframe;
//    private ItemSelectionFrame itemSelector;
//    private MuseRelativeRect border;
//    private ModelPartSpec[] specs;
//    private boolean open;
//    private double mousex;
//    private double mousey;
//
//    public PartManipSubFrame(ModelSpec model, ColourPickerFrame colourframe, ItemSelectionFrame itemSelector, MuseRelativeRect border) {
//        this.model = model;
//        this.colourframe = colourframe;
//        this.itemSelector = itemSelector;
//        this.border = border;
//
//
//
//        this.specs = (ModelPartSpec[])((TraversableOnce)model.apply().values().filter((Function1)new Serializable() {
//            public boolean apply(ModelPartSpec spec) {
//                return PartManipSubFrame.this.isValidArmor(PartManipSubFrame.this.getSelectedItem(), spec.slot());
//            }
//        })).toArray(ClassTag$.MODULE$.apply((Class)ModelPartSpec.class));
//
//
//
//
//        this.open = true;
//        this.mousex = 0.0;
//        this.mousey = 0.0;
//    }
//
//    public int getArmorSlot() {
//        return ((ItemArmor)this.getSelectedItem().getItem().getItem()).armorType;
//    }
//
//    //        def getSelectedItem = itemSelector.getSelectedItem
//    public ClickableItem getSelectedItem() {
//        return this.itemSelector().getSelectedItem();
//    }
//
//    public NBTTagCompound getRenderTag() {
//        return MuseItemUtils.getMuseRenderTag(this.getSelectedItem().getItem(), this.getArmorSlot());
//    }
//
//    public NBTTagCompound getItemTag() {
//        return MuseItemUtils.getMuseItemTag(this.getSelectedItem().getItem());
//    }
//
//    public boolean isValidArmor(ClickableItem clickie, int slot) {
//        return clickie != null && clickie.getItem().getItem().isValidArmor(clickie.getItem(), slot, (Entity) Minecraft.getMinecraft().thePlayer);
//    }
//
//    public NBTTagCompound getSpecTag(ModelPartSpec spec) {
//        return this.getRenderTag().getCompoundTag(ModelRegistry.makeName(spec));
//    }
//
//
////        def getOrDontGetSpecTag(spec: ModelPartSpec): Option[NBTTagCompound] = {
////                val name = ModelRegistry.makeName(spec)
////        if (!getRenderTag.hasKey(name)) None
////        else Some(getRenderTag.getCompoundTag(name))
////  }
//
//    public Option<NBTTagCompound> getOrDontGetSpecTag(ModelPartSpec spec) {
//        String name = ModelRegistry.makeName(spec);
//        return (Option<NBTTagCompound>)(this.getRenderTag().hasKey(name) ? new Some((Object)this.getRenderTag().getCompoundTag(name)) : None$.MODULE$);
//    }
//
//
//
//    //        def getOrMakeSpecTag(spec: ModelPartSpec): NBTTagCompound = {
////                val name = ModelRegistry.makeName(spec)
////        if (!getRenderTag.hasKey(name)) {
////            val k = new NBTTagCompound()
////            spec.multiSet(k, None, None, None)
////            getRenderTag.setTag(name, k)
////            k
////        } else {
////            getRenderTag.getCompoundTag(name)
////        }
////  }
//
//    public NBTTagCompound getOrMakeSpecTag(ModelPartSpec spec) {
//        String name = ModelRegistry.makeName(spec);
//        NBTTagCompound compoundTag;
//        if (this.getRenderTag().hasKey(name)) {
//            compoundTag = this.getRenderTag().getCompoundTag(name);
//        }
//        else {
//            NBTTagCompound k = new NBTTagCompound();
//            spec.multiSet(k, (Option<String>)None$.MODULE$, (Option<Object>)None$.MODULE$, (Option<Object>)None$.MODULE$);
//            this.getRenderTag().setTag(name, (NBTBase)k);
//            compoundTag = k;
//        }
//        return compoundTag;
//    }
//
////        def updateItems() {
////            specs = model.apply.values.filter(spec => isValidArmor(getSelectedItem, spec.slot)).toArray
////            border.setHeight(if (specs.size > 0) specs.size * 8 + 10 else 0)
////        }
//
//    public void updateItems() {
//        this.specs = ((ModelPartSpec[])((TraversableOnce)this.model().apply().values().filter((Function1)new PartManipSubFrame$$anonfun$updateItems.PartManipSubFrame$$anonfun$updateItems$1(this))).toArray(ClassTag$.MODULE$.apply((Class)ModelPartSpec.class)));
//        this.border().setHeight((Predef$.MODULE$.refArrayOps((Object[])this.specs()).size() > 0) ? (Predef$.MODULE$.refArrayOps((Object[])this.specs()).size() * 8 + 10) : 0.0);
//    }
//
//
//    //        def drawPartial(min: Double, max: Double) {
////            if (specs.size > 0) {
////                ModelRegistry.getName(model).foreach(s => MuseRenderer.drawString(s, border.left + 8, border.top))
////                drawOpenArrow(min, max)
////                if (open) {
////                    ((border.top + 8) /: specs) {
////                        case (y, spec) => {
////                            drawSpecPartial(border.left, y, spec, min, max)
////                            y + 8
////                        }
////                    }
////                }
////            }
////        }
//
//    public void drawPartial(double min, double max) {
//        if (Predef$.MODULE$.refArrayOps((Object[])this.specs()).size() > 0) {
//            ((MuseBiMap<Object, T>)ModelRegistry.getName((T)this.model()).foreach((Function1)new PartManipSubFrame$$anonfun$drawPartial.PartManipSubFrame$$anonfun$drawPartial$1(this));
//            this.drawOpenArrow(min, max);
//            if (this.open()) {
//                Predef$.MODULE$.refArrayOps((Object[])this.specs()).$div$colon((Object)BoxesRunTime.boxToDouble(this.border().top() + 8), (Function2)new PartManipSubFrame$$anonfun$drawPartial.PartManipSubFrame$$anonfun$drawPartial$2(this, min, max));
//            }
//        }
//    }
//
//    //        def decrAbove(index: Int) {
////            for (spec <- specs) {
////                val tagname = ModelRegistry.makeName(spec)
////                val player = Minecraft.getMinecraft.thePlayer
////                val tagdata = getOrDontGetSpecTag(spec)
////                tagdata.foreach(e => {
////                        val oldindex = spec.getColourIndex(e)
////                if (oldindex >= index && oldindex > 0) {
////                    spec.setColourIndex(e, oldindex - 1)
////                    if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, e).getPacket131)
////                }
////      })
////
////            }
////        }
//
//
//    //
//    public void decrAbove(int index) {
//        Predef$.MODULE$.refArrayOps((Object[])this.specs()).foreach((Function1)new PartManipSubFrame$$anonfun$decrAbove.PartManipSubFrame$$anonfun$decrAbove$1(this, index));
//    }
//
////    def drawSpecPartial(x: Double, y: Double, spec: ModelPartSpec, ymino: Double, ymaxo: Double) = {
////        val tag = getSpecTag(spec)
////        val selcomp = if (tag.hasNoTags) 0 else if (spec.getGlow(tag)) 2 else 1
////        val selcolour = spec.getColourIndex(tag)
////        new GuiIcons.TransparentArmor(x, y, null, null, ymino, null, ymaxo)
////        new GuiIcons.NormalArmor(x + 8, y, null, null, ymino, null, ymaxo)
////        new GuiIcons.GlowArmor(x + 16, y, null, null, ymino, null, ymaxo)
////        new GuiIcons.SelectedArmorOverlay(x + selcomp * 8, y, null, null, ymino, null, ymaxo)
////        val textstartx = ((x + 28) /: colourframe.colours) {
////            case (acc, colour) =>
////                new GuiIcons.ArmourColourPatch(acc, y, new Colour(colour), null, ymino, null, ymaxo)
////                acc + 8
////        }
////        if (selcomp > 0) {
////            new GuiIcons.SelectedArmorOverlay(x + 28 + selcolour * 8, y, null, null, ymino, null, ymaxo)
////        }
////        MuseRenderer.drawString(spec.displayName, textstartx + 4, y)
////    }
//
//    public void drawSpecPartial(double x, double y, ModelPartSpec spec, double ymino, double ymaxo) {
//        NBTTagCompound tag = this.getSpecTag(spec);
//        int selcomp = tag.hasNoTags() ? 0 : (spec.getGlow(tag) ? 2 : 1);
//        int selcolour = spec.getColourIndex(tag);
//        new GuiIcons.TransparentArmor(x, y, null, null, Predef$.MODULE$.double2Double(ymino), null, Predef$.MODULE$.double2Double(ymaxo));
//        new GuiIcons.NormalArmor(x + 8, y, null, null, Predef$.MODULE$.double2Double(ymino), null, Predef$.MODULE$.double2Double(ymaxo));
//        new GuiIcons.GlowArmor(x + 16, y, null, null, Predef$.MODULE$.double2Double(ymino), null, Predef$.MODULE$.double2Double(ymaxo));
//        new GuiIcons.SelectedArmorOverlay(x + selcomp * 8, y, null, null, Predef$.MODULE$.double2Double(ymino), null, Predef$.MODULE$.double2Double(ymaxo));
//        double textstartx = BoxesRunTime.unboxToDouble(Predef$.MODULE$.intArrayOps(this.colourframe().colours()).$div$colon((Object)BoxesRunTime.boxToDouble(x + 28), (Function2)new PartManipSubFrame$$anonfun.PartManipSubFrame$$anonfun$1(this, y, ymino, ymaxo)));
//        if (selcomp > 0) {
//            new GuiIcons.SelectedArmorOverlay(x + 28 + selcolour * 8, y, null, null, Predef$.MODULE$.double2Double(ymino), null, Predef$.MODULE$.double2Double(ymaxo));
//        }
//        else {
//            BoxedUnit unit = BoxedUnit.UNIT;
//        }
//        MuseRenderer.drawString(spec.displayName(), textstartx + 4, y);
//    }
//
//
//
//
//
//    public void drawOpenArrow(double min, double max) {
//        RenderState.texturelessOn();
//        Colour.LIGHTBLUE.doGL();
//        GL11.glBegin(4);
//        if (this.open) {
//            GL11.glVertex2d(this.border.left() + 3, MuseMathUtils.clampDouble(this.border.top() + 3, min, max));
//            GL11.glVertex2d(this.border.left() + 5, MuseMathUtils.clampDouble(this.border.top() + 7, min, max));
//            GL11.glVertex2d(this.border.left() + 7, MuseMathUtils.clampDouble(this.border.top() + 3, min, max));
//        }
//        else {
//            GL11.glVertex2d(this.border.left() + 3, MuseMathUtils.clampDouble(this.border.top() + 3, min, max));
//            GL11.glVertex2d(this.border.left() + 3, MuseMathUtils.clampDouble(this.border.top() + 7, min, max));
//            GL11.glVertex2d(this.border.left() + 7, MuseMathUtils.clampDouble(this.border.top() + 5, min, max));
//        }
//        GL11.glEnd();
//        Colour.WHITE.doGL();
//        RenderState.texturelessOff();
//    }
//
////        def getBorder: MuseRect = {
////        if (open) border.setHeight(9 + 9 * specs.size)
////        else border.setHeight(9)
////        border
////  }
//
//    public MuseRect getBorder() {
//        if (this.open) {
//            this.border.setHeight(9 + 9 * this.specs.length);
//        }
//        else {
//            this.border.setHeight(9.0);
//        }
//        return this.border;
//    }
//
//    public boolean tryMouseClick(double x, double y) {
//        boolean b;
//        if (x < this.border.left() || x > this.border.right() || y < this.border.top() || y > this.border.bottom()) {
//            b = false;
//        }
//        else if (x > this.border.left() + 2 && x < this.border.left() + 8 && y > this.border.top() + 2 && y < this.border.top() + 8) {
//            this.open =(!this.open);
//            this.getborder;
//            b = true;
//        }
//        else if (x < this.border.left() + 24 && y > this.border.top() + 8) {
//            int lineNumber = (int)((y - this.border.top() - 8) / 8);
//            int columnNumber = (int)((x - this.border.left()) / 8);
//            ModelPartSpec spec = this.specs[RichInt$.MODULE$.max$extension(Predef$.MODULE$.intWrapper(RichInt$.MODULE$.min$extension(Predef$.MODULE$.intWrapper(lineNumber), Predef$.MODULE$.refArrayOps((Object[])this.specs()).size() - 1)), 0)];
//            MuseLogger.logDebug(new StringBuilder().append((Object)"Line ").append((Object)BoxesRunTime.boxToInteger(lineNumber)).append((Object)" Column ").append((Object)BoxesRunTime.boxToInteger(columnNumber)).toString());
//            switch (columnNumber) {
//                default: {
//                    b = false;
//                    break;
//                }
//                case 2: {
//                    String tagname = ModelRegistry.makeName(spec);
//                    EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//                    NBTTagCompound tagdata = this.getOrMakeSpecTag(spec);
//                    spec.setGlow(tagdata, true);
//                    if (player.worldObj.isRemote) {
//                        PacketSender.sendToServer(new MusePacketCosmeticInfo((EntityPlayer)player, this.getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
//                    }
//                    this.updateItems();
//                    b = true;
//                    break;
//                }
//                case 1: {
//                    String tagname2 = ModelRegistry.makeName(spec);
//                    EntityClientPlayerMP player2 = Minecraft.getMinecraft().thePlayer;
//                    NBTTagCompound tagdata2 = this.getOrMakeSpecTag(spec);
//                    spec.setGlow(tagdata2, false);
//                    if (player2.worldObj.isRemote) {
//                        PacketSender.sendToServer(new MusePacketCosmeticInfo((EntityPlayer)player2, this.getSelectedItem().inventorySlot, tagname2, tagdata2).getPacket131());
//                    }
//                    this.updateItems();
//                    b = true;
//                    break;
//                }
//                case 0: {
//                    NBTTagCompound renderTag = this.getRenderTag();
//                    String tagname3 = ModelRegistry.makeName(spec);
//                    EntityClientPlayerMP player3 = Minecraft.getMinecraft().thePlayer;
//                    renderTag.removeTag(ModelRegistry.makeName(spec));
//                    if (player3.worldObj.isRemote) {
//                        PacketSender.sendToServer(new MusePacketCosmeticInfo((EntityPlayer)player3, this.getSelectedItem().inventorySlot, tagname3, new NBTTagCompound()).getPacket131());
//                    }
//                    this.updateItems();
//                    b = true;
//                    break;
//                }
//            }
//        }
//        else if (x > this.border.left() + 28 && x < this.border.left() + 28 + Predef$.MODULE$.intArrayOps(this.colourframe.colours()).size() * 8) {
//            int lineNumber2 = (int)((y - this.border.top() - 8) / 8);
//            int columnNumber2 = (int)((x - this.border.left() - 28) / 8);
//            ModelPartSpec spec2 = this.specs[RichInt$.MODULE$.max$extension(Predef$.MODULE$.intWrapper(RichInt$.MODULE$.min$extension(Predef$.MODULE$.intWrapper(lineNumber2), Predef$.MODULE$.refArrayOps((Object[])this.specs()).size() - 1)), 0)];
//            String tagname4 = ModelRegistry.makeName(spec2);
//            EntityClientPlayerMP player4 = Minecraft.getMinecraft().thePlayer;
//            NBTTagCompound tagdata3 = this.getOrMakeSpecTag(spec2);
//            spec2.setColourIndex(tagdata3, columnNumber2);
//            if (player4.worldObj.isRemote) {
//                PacketSender.sendToServer(new MusePacketCosmeticInfo((EntityPlayer)player4, this.getSelectedItem().inventorySlot, tagname4, tagdata3).getPacket131());
//            }
//            b = true;
//        }
//        else {
//            b = false;
//        }
//        return b;
//    }
//
//
//
////    class PartManipSubFrame(val model: ModelSpec, val colourframe: ColourPickerFrame, val itemSelector: ItemSelectionFrame, val border: MuseRelativeRect) {
////        var specs: Array[ModelPartSpec] = model.apply.values.filter(spec => isValidArmor(getSelectedItem, spec.slot)).toArray
////        var open: Boolean = true
////        var mousex: Double = 0
////        var mousey: Double = 0
////
//
////
////        def tryMouseClick(x: Double, y: Double): Boolean = {
////        if (x < border.left || x > border.right || y < border.top || y > border.bottom) false
////        else if (x > border.left + 2 && x < border.left + 8 && y > border.top + 2 && y < border.top + 8) {
////            open = !open
////            getBorder
////            true
////        } else if (x < border.left + 24 && y > border.top + 8) {
////            val lineNumber = ((y - border.top - 8) / 8).toInt
////            val columnNumber = ((x - border.left) / 8).toInt
////            val spec = specs(lineNumber.min(specs.size - 1).max(0))
////            MuseLogger.logDebug("Line " + lineNumber + " Column " + columnNumber)
////            columnNumber match {
////                case 0 => {
////                    val renderTag = getRenderTag
////                    val tagname = ModelRegistry.makeName(spec)
////                    val player = Minecraft.getMinecraft.thePlayer
////                    renderTag.removeTag(ModelRegistry.makeName(spec))
////                    if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, new NBTTagCompound()).getPacket131)
////                    updateItems()
////                    true
////                }
////                case 1 => {
////                    val tagname = ModelRegistry.makeName(spec)
////                    val player = Minecraft.getMinecraft.thePlayer
////                    val tagdata = getOrMakeSpecTag(spec)
////                    spec.setGlow(tagdata, false)
////                    if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, tagdata).getPacket131)
////                    updateItems()
////                    true
////                }
////                case 2 => {
////                    val tagname = ModelRegistry.makeName(spec)
////                    val player = Minecraft.getMinecraft.thePlayer
////                    val tagdata = getOrMakeSpecTag(spec)
////                    spec.setGlow(tagdata, true)
////                    if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, tagdata).getPacket131)
////                    updateItems()
////                    true
////                }
////                case _ => false
////            }
////        } else if (x > border.left + 28 && x < border.left + 28 + colourframe.colours.size * 8) {
////            val lineNumber = ((y - border.top - 8) / 8).toInt
////            val columnNumber = ((x - border.left - 28) / 8).toInt
////            val spec = specs(lineNumber.min(specs.size - 1).max(0))
////            val tagname = ModelRegistry.makeName(spec)
////            val player = Minecraft.getMinecraft.thePlayer
////            val tagdata = getOrMakeSpecTag(spec)
////            spec.setColourIndex(tagdata, columnNumber)
////            if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, tagdata).getPacket131)
////            true
////        }
////        else false
////  }
////
////    }
//
//
//}
//
//
//
//
//
//
//
//
////
//////
////// Decompiled by Procyon v0.5.30
//////
////
////package net.machinemuse.general.gui.frame;
////
////        import net.machinemuse.numina.scala.MuseBiMap;
////        import scala.runtime.AbstractFunction1;
////        import scala.Serializable;
////        import net.minecraft.client.entity.EntityClientPlayerMP;
////        import net.machinemuse.numina.network.PacketSender;
////        import net.minecraft.entity.player.EntityPlayer;
////        import net.machinemuse.powersuits.network.packets.MusePacketCosmeticInfo;
////        import net.machinemuse.numina.general.MuseLogger;
////        import scala.collection.mutable.StringBuilder;
////        import scala.runtime.RichInt$;
////        import net.machinemuse.numina.geometry.MuseRect;
////        import net.machinemuse.numina.general.MuseMathUtils;
////        import org.lwjgl.opengl.GL11;
////        import net.machinemuse.numina.render.RenderState;
////        import net.machinemuse.utils.render.MuseRenderer;
////        import scala.runtime.BoxedUnit;
////        import net.machinemuse.numina.geometry.Colour;
////        import net.machinemuse.utils.render.GuiIcons;
////        import scala.Function2;
////        import scala.runtime.BoxesRunTime;
////        import scala.Predef$;
////        import scala.reflect.ClassTag$;
////        import scala.Function1;
////        import scala.collection.TraversableOnce;
////        import net.minecraft.nbt.NBTBase;
////        import scala.None$;
////        import scala.Some;
////        import scala.Option;
////        import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry$;
////        import net.minecraft.entity.Entity;
////        import net.minecraft.client.Minecraft;
////        import net.machinemuse.utils.MuseItemUtils;
////        import net.minecraft.nbt.NBTTagCompound;
////        import net.machinemuse.general.gui.clickable.ClickableItem;
////        import net.minecraft.item.ItemArmor;
////        import net.machinemuse.powersuits.client.render.modelspec.ModelPartSpec;
////        import net.machinemuse.numina.geometry.MuseRelativeRect;
////        import net.machinemuse.powersuits.client.render.modelspec.ModelSpec;
////        import scala.reflect.ScalaSignature;
////
// public class PartManipSubFrame
////{
//
////
////    public ModelSpec model() {
////        return this.model;
////    }
////
////    public ColourPickerFrame colourframe() {
////        return this.colourframe;
////    }
////
////    public ItemSelectionFrame itemSelector() {
////        return this.itemSelector;
////    }
////
////    public MuseRelativeRect border() {
////        return this.border;
////    }
////
////    public ModelPartSpec[] specs() {
////        return this.specs;
////    }
////
////    public void specs_$eq(ModelPartSpec[] x$1) {
////        this.specs = x$1;
////    }
////
////    public boolean open() {
////        return this.open;
////    }
////
////    public void open_$eq(boolean x$1) {
////        this.open = x$1;
////    }
////
////    public double mousex() {
////        return this.mousex;
////    }
////
////    public void mousex_$eq(double x$1) {
////        this.mousex = x$1;
////    }
////
////    public double mousey() {
////        return this.mousey;
////    }
////
////    public void mousey_$eq(double x$1) {
////        this.mousey = x$1;
////    }
////
//
////
//
////
//
//
////
//
////
//
////
//
////
//
////}