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
//import scala.collection.Iterable;
//import scala.collection.Iterator;
//import scala.collection.TraversableOnce;
//import scala.reflect.ClassTag$;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 1:46 AM, 30/04/13
// *
// * Ported to Java by lehjr on 11/2/16.
// */
//public class PartManipSubFrame {
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
//        /* TODO: cleanup and simplify in 1.10.2
//         * Scala method of filtering the list and building a new array based on boolean values.
//
//        var specs: Array[ModelPartSpec] = model.apply.values.filter(spec => isValidArmor(getSelectedItem, spec.slot)).toArray
//        model.apply().values().filter(Function1<ModelPartSpec, Object> p) */
//        this.specs = getSpecs();
//
//        this.open = true;
//        this.mousex = 0.0;
//        this.mousey = 0.0;
//    }
//
//    private ModelPartSpec[] getSpecs() {
//        List<ModelPartSpec> specsArray = new ArrayList<>();
//        Iterator<ModelPartSpec> specIt = model.apply().values().iterator();
//        ModelPartSpec spec;
//        while (specIt.hasNext()) {
//            spec = specIt.next();
//            if (isValidArmor(getSelectedItem(), spec.slot()))
//                specsArray.add(spec);
//        }
//        return (ModelPartSpec[]) specsArray.toArray();
//    }
//
//    public int getArmorSlot() {
//        return ((ItemArmor)this.getSelectedItem().getItem().getItem()).armorType;
//    }
//
//    public ClickableItem getSelectedItem() {
//        return this.itemSelector.getSelectedItem();
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
//    public NBTTagCompound getOrDontGetSpecTag(ModelPartSpec spec) {
//        String name = ModelRegistry.makeName(spec);
//        return this.getRenderTag().hasKey(name) ? this.getRenderTag().getCompoundTag(name) : null;
//    }
//
//    public NBTTagCompound getOrMakeSpecTag(ModelPartSpec spec) {
//        String name = ModelRegistry.makeName(spec);
//        NBTTagCompound compoundTag;
//        if (this.getRenderTag().hasKey(name)) {
//            compoundTag = this.getRenderTag().getCompoundTag(name);
//        }
//        else {
//            NBTTagCompound k = new NBTTagCompound();
//            spec.multiSet(k, null, None$.MODULE$, None$.MODULE$); // FIXME!! null will probably fail
//            this.getRenderTag().setTag(name, (NBTBase)k);
//            compoundTag = k;
//        }
//        return compoundTag;
//    }
//
//    public void updateItems() {
//        this.specs = getSpecs();
//        this.border.setHeight((specs.length > 0) ? (specs.length * 8 + 10) : 0);
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
////        if (specs.length > 0) {
////
////
////            ((MuseBiMap<Object, T>)ModelRegistry.getName((T)this.model())
////            ModelRegistry.getName(model);
////
////
////
////
////                    .foreach(s => MuseRenderer.drawString(/* STRING */s, border.left() + 8, border.top()))
////                drawOpenArrow(min, max);
////                if (open) {
////                    ((border.top + 8) /: specs) {
////                        case (y, spec) => {
////                            drawSpecPartial(border.left, y, spec, min, max);
////                            y + 8
////                        }
////                    }
////                }
////            }
////        }
//
//
//
//
//        if (specs.length > 0) {
//            ((MuseBiMap<Object, T>)ModelRegistry.getName((T)this.model()).foreach((Function1)new PartManipSubFrame$$anonfun$drawPartial.PartManipSubFrame$$anonfun$drawPartial$1(this));
//            this.drawOpenArrow(min, max);
//            if (this.open()) {
//                Predef$.MODULE$.refArrayOps((Object[])this.specs()).$div$colon((Object)BoxesRunTime.boxToDouble(this.border().top() + 8), (Function2)new PartManipSubFrame$$anonfun$drawPartial.PartManipSubFrame$$anonfun$drawPartial$2(this, min, max));
//            }
//        }
//    }
//
//    public void decrAbove(int index) {
//        for (ModelPartSpec spec : specs) {
//            String tagname = ModelRegistry.makeName(spec);
//            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//            NBTTagCompound tagdata = getOrDontGetSpecTag(spec);
//
//            if (tagdata != null) {
//                int oldindex = spec.getColourIndex(tagdata);
//                if (oldindex >= index && oldindex > 0) {
//                    spec.setColourIndex(tagdata, oldindex - 1);
//                    if (player.worldObj.isRemote)
//                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
//                }
//            }
//        }
//    }
//
//
//
//
//
//
//
////    def drawSpecPartial(x: Double, y: Double, spec: ModelPartSpec, ymino: Double, ymaxo: Double) = {
////        val tag = getSpecTag(spec)
////        val selcomp = if (tag.hasNoTags) 0 else if (spec.getGlow(tag)) 2 else 1
////        val selcolour = spec.getColourIndex(tag)
////        new GuiIcons.TransparentArmor(x, y, null, null, ymino, null, ymaxo)
////        new GuiIcons.NormalArmor(x + 8, y, null, null, ymino, null, ymaxo)
////        new GuiIcons.GlowArmor(x + 16, y, null, null, ymino, null, ymaxo)
////        new GuiIcons.SelectedArmorOverlay(x + selcomp * 8, y, null, null, ymino, null, ymaxo)
//
//    /* /: fold left operator */
//
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
//        new GuiIcons.TransparentArmor(x, y, null, null, ymino, null, ymaxo);
//        new GuiIcons.NormalArmor(x + 8, y, null, null, ymino, null, ymaxo);
//        new GuiIcons.GlowArmor(x + 16, y, null, null, ymino, null, ymaxo);
//        new GuiIcons.SelectedArmorOverlay(x + selcomp * 8, y, null, null, ymino, null, ymaxo);
//
//        double textstartx = Predef.Double2double((Predef.intArrayOps(colourframe.colours())./:(scala.runtime.BoxesRunTime.boxToDouble((x.+(28))))) ({
//        case (acc, colour) =>
//        new GuiIcons.ArmourColourPatch(Predef.Double2double(acc), y, new Colour(colour), null, Predef.double2Double(ymino), null, Predef.double2Double(ymaxo));
//        Predef.double2Double(Predef.Double2double(acc).+(8))
//    }));
//        if (selcomp > 0) {
//            new GuiIcons.SelectedArmorOverlay(x + 28 + selcolour * 8, y, null, null, ymino, null, ymaxo)
//        }
//        MuseRenderer.drawString(spec.displayName, textstartx + 4, y)
//
//
//
//
//
//        double textstartx = BoxesRunTime.unboxToDouble(Predef$.MODULE$.intArrayOps(this.colourframe.colours()).$div$colon((Object)BoxesRunTime.boxToDouble(x + 28), (Function2)new PartManipSubFrame$$anonfun.PartManipSubFrame$$anonfun$1(this, y, ymino, ymaxo)));
//
//
//
//
//        if (selcomp > 0) {
//            new GuiIcons.SelectedArmorOverlay(x + 28 + selcolour * 8, y, null, null, ymino, null, ymaxo);
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
//            this.getBorder();
//            b = true;
//        }
//        else if (x < this.border.left() + 24 && y > this.border.top() + 8) {
//            int lineNumber = (int)((y - this.border.top() - 8) / 8);
//            int columnNumber = (int)((x - this.border.left()) / 8);
//
//
//            //TODO: check this, it seems backwards; ie min should be 0 and max should be size -1
////            val spec = specs(lineNumber.min(specs.size - 1).max(0))
//            ModelPartSpec spec = specs[Math.max(Math.min(lineNumber, specs.length - 1), 0)];
//            MuseLogger.logDebug("Line " + lineNumber + " Column " + columnNumber);
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
//                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player, this.getSelectedItem().inventorySlot, tagname, tagdata).getPacket131());
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
//                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player2, this.getSelectedItem().inventorySlot, tagname2, tagdata2).getPacket131());
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
//                        PacketSender.sendToServer(new MusePacketCosmeticInfo(player3, this.getSelectedItem().inventorySlot, tagname3, new NBTTagCompound()).getPacket131());
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
//            ModelPartSpec spec2 = specs[Math.max(Math.min(lineNumber2, specs.length - 1), 0)];
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
//}