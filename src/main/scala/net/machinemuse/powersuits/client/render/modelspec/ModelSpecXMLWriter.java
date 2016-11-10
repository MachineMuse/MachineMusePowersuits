//package net.machinemuse.powersuits.client.render.modelspec;
//
//import java.util.List;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 9:10 AM, 29/04/13
// *
// * Ported to Java by lehjr on 11/10/16.
// */
//public class ModelSpecXMLWriter {
//    private ModelSpecXMLWriter() {
//    }
//
//    private static ModelSpecXMLWriter INSTANCE;
//    public static ModelSpecXMLWriter getINSTANCE() {
//        if (INSTANCE == null)
//            INSTANCE = new ModelSpecXMLWriter();
//        return INSTANCE;
//    }
//
//
//
//
//    public void writeRegistry(final String file) {
////        final String s = null;
////        final String s2 = "models";
////        final Null$ module$ = Null$.MODULE$;
////        final TopScope$ module$2 = TopScope$.MODULE$;
////        final boolean b = false;
////        final NodeBuffer $buf = new NodeBuffer();
////        $buf.$amp$plus((Object)new Text("\n        "));
////        $buf.$amp$plus(ModelRegistry$.MODULE$.apply().withFilter((Function1)new ModelSpecXMLWriter$$anonfun.ModelSpecXMLWriter$$anonfun$1()).map((Function1)new ModelSpecXMLWriter$$anonfun.ModelSpecXMLWriter$$anonfun$2(), Iterable$.MODULE$.canBuildFrom()));
////        $buf.$amp$plus((Object)new Text("\n      "));
////        final Elem xmlwrite = new Elem(s, s2, (MetaData)module$, (NamespaceBinding)module$2, b, (Seq)$buf);
////        XML$.MODULE$.save(file, (Node)xmlwrite, XML$.MODULE$.save$default$3(), XML$.MODULE$.save$default$4(), XML$.MODULE$.save$default$5());
//    }
//
//
//
//    public String concatList(final List<String> list) {
//
//
//
//
//        return ModelSpecXMLWriter$.MODULE$.concatList(list);
//    }
//
//
//
//
//
//
////    package net.machinemuse.powersuits.client.render.modelspec
////
////import java.io.{File, FileOutputStream, PrintWriter}
////
////import com.google.gson.Gson
//
//
////    /**
////     * Author: MachineMuse (Claire Semple)
////     * Created: 9:10 AM, 29/04/13
////     */
////    object ModelSpecXMLWriter {
////        def writeRegistry(file: String) {
////            val xmlwrite =
////      <models>
////                    {for ((modelname, modelspec) <- ModelRegistry.getInstance()) yield
////                    <model file={modelspec.filename} textures={concatList(modelspec.textures)}>
////                    {for ((partname, partspec) <- modelspec) yield
////                    <binding slot={partspec.slot.toString} target={partspec.morph.name}>
////            <part defaultcolor={partspec.defaultcolourindex.toString} defaultglow={partspec.defaultglow.toString} polygroup={partspec.partName} name={partspec.displayName}/>
////          </binding>}
////        </model>}
////      </models>
////
////                    scala.xml.XML.save(file, xmlwrite)
////        }
////
////        def concatList(list: Seq[String]): String = list mkString ","
////    }
//
//}