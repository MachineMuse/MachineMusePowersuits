package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.general.NBTTagAccessor;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.powersuits.client.render.item.ArmorModelInstance;
import net.machinemuse.powersuits.client.render.item.IArmorModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelPart;
import org.lwjgl.opengl.GL11;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:16 AM, 29/04/13
 *
 * Ported to Java by lehjr on 11/6/16.
 */
public class RenderPart extends ModelRenderer {
    ModelRenderer parent;

    public RenderPart(ModelBase base, ModelRenderer parent) {
        super (base);
        this.parent = parent;
    }

    @Override
    public void render(float scale) {
        NBTTagCompound renderSpec = ((IArmorModel)(ArmorModelInstance.getInstance())).getRenderSpec();
        int[] colours = renderSpec.getIntArray("colours");
        for (NBTTagCompound nbt : NBTTagAccessor.getValues(renderSpec)) {
            ModelPartSpec part = ModelRegistry.getInstance().getPart(nbt);
            /* checks for None TODO: Null check for Java port.*/
            if (part !=null) {
                if (part.slot == ((IArmorModel)(ArmorModelInstance.getInstance())).getVisibleSection() && part.morph.apply(ArmorModelInstance.getInstance()) == parent) {
                    float prevBrightX = OpenGlHelper.lastBrightnessX;
                    float prevBrightY = OpenGlHelper.lastBrightnessY;

                    // GLOW stuff on
                    if (part.getGlow(nbt)) {
                        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
                        RenderHelper.disableStandardItemLighting();
                        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                    }

                    GL11.glPushMatrix();
                    GL11.glScaled(scale, scale, scale);
                    MuseTextureUtils.bindTexture(part.getTexture(nbt));
                    applyTransform();

                    int ix = part.getColourIndex(nbt);
                    if (ix < colours.length && ix >= 0) {
                        Colour.doGLByInt(colours[ix]);
                    }
                    part.modelSpec.applyOffsetAndRotation(); // not yet implemented


                    /*
                        some notes on model editing and usage:

                        I used blender to work on the models. Also used a blender script called MultiEdit for selecting all the obects
                        in the model to do the UV unwrapping. Watch out though, it may butcher group names, and sometimes material names,
                        so keep a backup.

                        The Wavefront model system in Minecraft Forge is not a full implementation, so only Ka, Kd, and map_Kd are used.
                        Everything else must go to avoid log spam. If the model has a texture, it must be a valid location. Textures also
                        have to be square and multiples of 16.

                        Coloring the models will have to be done through isolationg the bakedQuads for each group and coloring them. Coloring
                        the materials won't work unless each group has it's own. In order to render parts individually, the quads for each need
                        to be isolated anyway.



                     */




//                    part.render();


//                    RenderItem

                    /*

                                            FIXME!!!!! make this work again
//                    part.modelSpec.model.renderPart(part.partName);


                    looks like we can use AnimationProperty to indirecly to get quads




                    things that can be used to render colored quads:
                    net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);




        @Override
        public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand)
        {
            if (side != null) return ImmutableList.of();
            if (quads == null)
            {
                quads = buildQuads(this.state);
            }
            if (blockState instanceof IExtendedBlockState)
            {
                IExtendedBlockState exState = (IExtendedBlockState) blockState;
                if (exState.getUnlistedNames().contains(Properties.AnimationProperty))
                {

                    IModelState QuadsForPart = exState.getValue(Properties.AnimationProperty);
                    if (QuadsForPart != null)
                    {
                        QuadsForPart = new ModelStateComposition(this.state, QuadsForPart);
                        return buildQuads(QuadsForPart);
                    }
                }
            }
            return quads;
        }




                        Things we need:

                        toggle parts on and off --> this is already possible with IModelState (actually OBJState)



                        What I really want is a way to treat the "groups" as seperate models.





                        materialName <-> groupName map
                        groupName <-> quads/model/...w/e; basically a way to isolate those model parts we want to render and add a way to color them as we






    /// **
     * This block is intended to demonstrate how to change the visibility of a group(s)
     * from within the block's class.
     * By right clicking on this block the player increments an integer value in the tile entity
     * for this block, which is then added to a list of strings and passed into the constructor
     * for OBJState. NOTE: this trick only works if your groups are named '1', '2', '3', etc.,
     * otherwise they must be added by name.
     * Holding shift decrements the value in the tile entity.
     * @author shadekiller666
     *
     * /
                    public static class OBJTesseractBlock extends Block implements ITileEntityProvider
                    {
                        public static final OBJTesseractBlock instance = new OBJTesseractBlock();
                        public static final String name = "OBJTesseractBlock";

                        private OBJTesseractBlock()
                        {
                            super(Material.IRON);
                            setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
                            setUnlocalizedName(MODID + ":" + name);
                            setRegistryName(new ResourceLocation(MODID, name));
                        }

                        @Override
                        public TileEntity createNewTileEntity(World worldIn, int meta)
                        {
                            return new OBJTesseractTileEntity();
                        }

                        @Override
                        public boolean isOpaqueCube(IBlockState state) { return false; }

                        @Override
                        public boolean isFullCube(IBlockState state) { return false; }

                        @Override
                        public boolean isVisuallyOpaque() { return false; }

                        @Override
                        public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
                        {
                            if (world.getTileEntity(pos) == null) world.setTileEntity(pos, new OBJTesseractTileEntity());
                            OBJTesseractTileEntity tileEntity = (OBJTesseractTileEntity) world.getTileEntity(pos);

                            if (player.isSneaking())
                            {
                                tileEntity.decrement();
                            }
                            else
                            {
                                tileEntity.increment();
                            }

                            world.markBlockRangeForRenderUpdate(pos, pos);
                            return false;
                        }

                        @Override
                        public boolean hasTileEntity(IBlockState state)
                        {
                            return true;
                        }

                        @Override
                        public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
                        {
                            if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof OBJTesseractTileEntity)
                            {
                                OBJTesseractTileEntity te = (OBJTesseractTileEntity) world.getTileEntity(pos);
                                return ((IExtendedBlockState) state).withProperty(Properties.AnimationProperty, te.state);
                            }
                            return state;
                        }

                        @Override
                        public BlockStateContainer createBlockState()
                        {
                            return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] {Properties.AnimationProperty});
                        }
                    }

                    public static class OBJTesseractTileEntity extends TileEntity
                    {
                        private int counter = 1;
                        private int max = 32;
                        private final List<String> hidden = new ArrayList<String>();
                        private final IModelState state = new IModelState()
                        {
                            private final Optional<TRSRTransformation> value = Optional.of(TRSRTransformation.identity());

                            @Override
                            public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part)
                            {
                                if(part.isPresent())
                                {
                                    // This whole thing is subject to change, but should do for now.
                                    UnmodifiableIterator<String> parts = Models.getParts(part.get());
                                    if(parts.hasNext())
                                    {
                                        String name = parts.next();
                                        // only interested in the root level
                                        if(!parts.hasNext() && hidden.contains(name))
                                        {
                                            return value;
                                        }
                                    }
                                }
                                return Optional.absent();
                            }
                        };

                        public void increment()
                        {
                            if (this.counter == max)
                            {
                                this.counter = 0;
                                this.hidden.clear();
                            }
                            this.counter++;
                            this.hidden.add(Integer.toString(this.counter));
                            TextComponentString text = new TextComponentString("" + this.counter);
                            if (this.worldObj.isRemote) Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(text);
                        }

                        public void decrement()
                        {
                            if (this.counter == 1)
                            {
                                this.counter = max + 1;
                                for (int i = 1; i < max; i++) this.hidden.add(Integer.toString(i));
                            }
                            this.hidden.remove(Integer.toString(this.counter));
                            this.counter--;
                            TextComponentString text = new TextComponentString("" + this.counter);
                            if (this.worldObj.isRemote) Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(text);
                        }

                        public void setMax(int max)
                        {
                            this.max = max;
                        }
                    }

////-----------------------------------------------------------------------------









                    // see: net.minecraft.client.renderer.RenderItem
                    Due to the complexity of this whole thing, we may have to use the



//                    RenderEntityItem





                        The model system and rendering system have completely changed.
                        There is no mechanism for rendering single parts anymore. Models are now rendered as
                        "baked quads". So, my current thinking is to create a models from the existing models,
                        with rendering hooks and a color/glow system for the parts, specifically for the armor
                        slots/ modelBiped areas, such as head, chest, arms, hands, legs, and feet, and then
                        render these models this way.

                        */



                    Colour.WHITE.doGL();
                    GL11.glPopMatrix();

                    // GLOW stuff off
                    if (part.getGlow(nbt)) {
                        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevBrightX, prevBrightY);
                        GL11.glPopAttrib();
                    }
                }
            }
        }
    }

    private void applyTransform() {
//        float degrad = (float) (180F / Math.PI);
//        GL11.glTranslatef(rotationPointX, rotationPointY, rotationPointZ);
//        GL11.glRotatef(rotateAngleZ * degrad, 0.0F, 0.0F, 1.0F);
//        GL11.glRotatef(rotateAngleY * degrad, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(rotateAngleX * degrad, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
        GL11.glTranslated(offsetX, offsetY - 26, offsetZ);
    }
}