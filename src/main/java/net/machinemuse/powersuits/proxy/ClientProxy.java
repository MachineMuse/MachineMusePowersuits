package net.machinemuse.powersuits.proxy;

import net.machinemuse.powersuits.client.events.EventRegisterRenderers;
import net.machinemuse.powersuits.client.events.ModelBakeEventHandler;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.client.events.RenderEventHandler;
import net.machinemuse.powersuits.client.modelspec.ModelSpecXMLReader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.net.URL;

import static net.machinemuse.powersuits.common.MuseConstants.MODID;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        OBJLoader.INSTANCE.addDomain(MODID);

        //--------------------------------------------
//        int nummatches = 0;
//        double closenessThreshold = 0.01D;
//
//        List<EnumColour> matchList = new ArrayList<>();
//        while (nummatches < 8) {
//            EnumColour match;
//            System.out.println("Closeness threshold: " + closenessThreshold);
//
//            for (EnumColour testEnumColour : EnumColour.values()) {
//                match = testEnumColour.findEnumColourDupes(Collections.emptyList(), closenessThreshold);
//
//                if (match!= null && !matchList.contains(match)) {
//                    System.out.println("EnumColour Match: " + match.getName() + " at index " + match.getIndex() + " matches: " + testEnumColour.getName() + " at index " + testEnumColour.getIndex());
//
//
//                    System.out.println("-----------------------------------------------------------------------------------");
//                    System.out.println();
//                    matchList.add(match);
//                    nummatches = matchList.size();
//                }
//            }
//            closenessThreshold += 0.1D;
//        }














//        ModelLoaderRegistry.registerLoader(MPSOBJLoader.INSTANCE);
//        MPSOBJLoader.INSTANCE.addDomain(MODID);

    }


    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);



    }

    @Override
    public void registerEvents() {
        super.registerEvents();
//        MinecraftForge.EVENT_BUS.register(TextureStitchEventHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.getInstance());


        System.out.println("doing something here!!!");

        MinecraftForge.EVENT_BUS.register(MuseIcon.getInstance());

        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());

        MinecraftForge.EVENT_BUS.register(EventRegisterRenderers.getInstance());
    }


    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        URL resource = ClientProxy.class.getResource("/assets/powersuits/models/item/armor/modelspec.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(resource);
        URL otherResource = ClientProxy.class.getResource("/assets/powersuits/models/item/armor/armor2.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(otherResource);

    }
}
