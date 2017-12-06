package net.machinemuse.powersuits.client.new_modelspec;

public class ModelSpecRegistry {
    private static ModelSpecRegistry INSTANCE;

    public static ModelSpecRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (ModelSpecRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ModelSpecRegistry();
                }
            }
        }
        return INSTANCE;
    }

    private ModelSpecRegistry() {
    }






}