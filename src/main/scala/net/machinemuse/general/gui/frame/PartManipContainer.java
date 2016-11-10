//package net.machinemuse.general.gui.frame;
//
//import net.machinemuse.numina.geometry.Colour;
//import net.machinemuse.numina.geometry.MusePoint2D;
//import net.machinemuse.numina.geometry.MuseRect;
//import net.machinemuse.numina.geometry.MuseRelativeRect;
//import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
//import net.machinemuse.powersuits.client.render.modelspec.ModelSpec;
//import org.lwjgl.opengl.GL11;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 6:39 PM, 29/04/13
// *
// * Ported to Java by lehjr on 11/9/16.
// */
//public class PartManipContainer extends ScrollableFrame
//{
//    private ItemSelectionFrame itemSelect;
//    private ColourPickerFrame colourSelect;
//    private MusePoint2D topleft;
//    private MusePoint2D bottomright;
//    private Integer lastItemSlot;
//    private int lastColour;
//    private int lastColourIndex;
//    private List<PartManipSubFrame> modelframes = new ArrayList<>();
//
//    public PartManipContainer(ItemSelectionFrame itemSelect,
//                              ColourPickerFrame colourSelect,
//                              MusePoint2D topleft,
//                              MusePoint2D bottomright,
//                              Colour borderColour,
//                              Colour insideColour) {
//        super(topleft, bottomright, borderColour, insideColour);
//
//        this.itemSelect = itemSelect;
//        this.colourSelect = colourSelect;
//        this.lastItemSlot = null;
//        this.lastColour = this.getColour();
//        this.lastColourIndex = this.getColourIndex();
//        this.modelframes =
//
//
//
//
//                (Seq<PartManipSubFrame>)((Tuple2)ModelRegistry$.MODULE$.apply().values().$div$colon((Object)new Tuple2((Object)Seq$.MODULE$.empty(), (Object)None$.MODULE$), (Function2)new Serializable() {
//                    public Tuple2<Seq<PartManipSubFrame>, Option<PartManipSubFrame>> apply(Tuple2<Seq<PartManipSubFrame>, Option<PartManipSubFrame>> x0$1, ModelSpec x1$1) {
//                        Tuple2 tuple2 = new Tuple2((Object)x0$1, (Object)x1$1);
//                        if (tuple2 != null) {
//                            Tuple2 tuple3 = (Tuple2)tuple2._1();
//                            ModelSpec modelspec = (ModelSpec)tuple2._2();
//                            if (tuple3 != null) {
//                                Seq frameseq = (Seq)tuple3._1();
//                                Option prev = (Option)tuple3._2();
//                                if (modelspec != null) {
//                                    PartManipSubFrame newframe = PartManipContainer.this.createNewFrame(modelspec, (Option<PartManipSubFrame>)prev);
//                                    return (Tuple2<Seq<PartManipSubFrame>, Option<PartManipSubFrame>>)new Tuple2(frameseq.$colon$plus((Object)newframe, Seq$.MODULE$.canBuildFrom()), (Object)new Some((Object)newframe));
//                                }
//                            }
//                        }
//                        throw new MatchError((Object)tuple2);
//                    }
//                }))._1();
//    }
//
//
//
//
//
//    public ItemSelectionFrame itemSelect() {
//        return this.itemSelect;
//    }
//
//    public ColourPickerFrame colourSelect() {
//        return this.colourSelect;
//    }
//
//    public Option<ItemStack> getItem() {
//        return (Option<ItemStack>)Option$.MODULE$.apply((Object)this.itemSelect().getSelectedItem()).map((Function1)new PartManipContainer$$anonfun$getItem.PartManipContainer$$anonfun$getItem$1(this));
//    }
//
//    public int getItemSlot() {
//        return (Option<Object>)Option$.MODULE$.apply((Object)this.itemSelect().getSelectedItem()).map((Function1)new PartManipContainer$$anonfun$getItemSlot.PartManipContainer$$anonfun$getItemSlot$1(this));
//    }
//
//    public Option<Object> lastItemSlot() {
//        return this.lastItemSlot;
//    }
//
//    public void lastItemSlot_$eq(Option<Object> x$1) {
//        this.lastItemSlot = x$1;
//    }
//
//    public int getColour() {
//        Option<ItemStack> item = this.getItem();
//        None$ module$ = None$.MODULE$;
//        if (item == null) {
//            if (module$ == null) {
//                return Colour.WHITE.getInt();
//            }
//        }
//        else if (item.equals(module$)) {
//            return Colour.WHITE.getInt();
//        }
//        if (this.colourSelect().selectedColour() < Predef$.MODULE$.intArrayOps(this.colourSelect().colours()).size() && this.colourSelect().selectedColour() >= 0) {
//            return this.colourSelect().colours()[this.colourSelect().selectedColour()];
//        }
//        return Colour.WHITE.getInt();
//    }
//
//    public int lastColour() {
//        return this.lastColour;
//    }
//
//    public void lastColour_$eq(int x$1) {
//        this.lastColour = x$1;
//    }
//
//    public int getColourIndex() {
//        return this.colourSelect().selectedColour();
//    }
//
//    public int lastColourIndex() {
//        return this.lastColourIndex;
//    }
//
//    public void lastColourIndex_$eq(int x$1) {
//        this.lastColourIndex = x$1;
//    }
//
//    public List<PartManipSubFrame> modelframes() {
//
//
//
//
//
//
//
//        return this.modelframes;
//    }
//
//
//
//
//    val modelframes: Seq[PartManipSubFrame] =
//        ((Seq.empty[PartManipSubFrame], None: Option[PartManipSubFrame]) /: ModelRegistry.getInstance().apply.values) {
//    case ((frameseq, prev), modelspec: ModelSpec) => {
//        val newframe = createNewFrame(modelspec, prev)
//        (frameseq :+ newframe, Some(newframe))
//    }
//}._1
//
//
//
//
//
//
//
//    public PartManipSubFrame createNewFrame(ModelSpec modelspec, PartManipSubFrame prev) {
//        MuseRelativeRect newborder = new MuseRelativeRect(this.topleft.x() + 4, this.topleft.y() + 4, this.bottomright.x(), this.topleft.y() + 10);
//        newborder.setBelow(prev.border());
//        return new PartManipSubFrame(modelspec, this.colourSelect(), this.itemSelect(), newborder);
//    }
//
//    @Override
//    public void onMouseDown(double x, double y, int button) {
//        if (button == 0) {
//            for (PartManipSubFrame frame : modelframes) {
//                frame.tryMouseClick(x, y + currentscrollpixels);
//            }
//        }
//    }
//
//    @Override
//    public void update(double mousex, double mousey) {
//        super.update(mousex, mousey);
//        if (lastItemSlot != getItemSlot()) {
//            lastItemSlot = getItemSlot();
//            colourSelect.refreshColours();
//
//            double x = 0;
//            for (PartManipSubFrame subframe : modelframes) {
//                subframe.updateItems();
//                x += subframe.border().bottom();
//            }
//            this.totalsize = (int) x;
//        }
//        if (colourSelect.decrAbove() > -1) {
//            decrAbove(colourSelect.decrAbove());
////            colourSelect.decrAbove() = -1;
//            this.colourSelect().decrAbove_$eq(-1);
//        }
//
//    }
//
//    public void decrAbove(int index) {
//        for(PartManipSubFrame frame : modelframes) frame.decrAbove(index);
//    }
//
//    @Override
//    public void draw() {
//        super.preDraw();
//        GL11.glPushMatrix();
//        GL11.glTranslated(0.0, (double)(-this.currentscrollpixels), 0.0);
//        for (PartManipSubFrame f : modelframes)
//            f.drawPartial(currentscrollpixels + 4 + border.top(), this.currentscrollpixels + border.bottom() - 4);
//        GL11.glPopMatrix();
//        super.postDraw();
//    }
//}
////class PartManipContainer(val itemSelect: ItemSelectionFrame, val colourSelect: ColourPickerFrame, topleft: MusePoint2D, bottomright: MusePoint2D, borderColour: Colour, insideColour: Colour)
////        extends ScrollableFrame(topleft, bottomright, borderColour, insideColour) {
////
////
////        def getItem = Option(itemSelect.getSelectedItem).map(e => e.getItem)
////
////        def getItemSlot = Option(itemSelect.getSelectedItem).map(e => e.inventorySlot)
////        var lastItemSlot: Option[Int] = None
////
////        def getColour = if(getItem != None && colourSelect.selectedColour < colourSelect.colours.size && colourSelect.selectedColour >= 0)
////        colourSelect.colours(colourSelect.selectedColour)
////        else
////        Colour.WHITE.getInt
////        var lastColour = getColour
////
////        def getColourIndex = colourSelect.selectedColour
////        var lastColourIndex = getColourIndex
////
////
//
//
