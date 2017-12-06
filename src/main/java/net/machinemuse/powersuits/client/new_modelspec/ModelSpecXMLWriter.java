package net.machinemuse.powersuits.client.new_modelspec;

public class ModelSpecXMLWriter {
    private static ModelSpecXMLWriter INSTANCE;

    public static ModelSpecXMLWriter getInstance() {
        if (INSTANCE == null) {
            synchronized (ModelSpecXMLWriter.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ModelSpecXMLWriter();
                }
            }
        }
        return INSTANCE;
    }

    private ModelSpecXMLWriter() {
    }






}
