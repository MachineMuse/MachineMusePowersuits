package net.machinemuse.powersuits.client.new_modelspec;

public class ModelPartSpecRegistry {
    private static ModelPartSpecRegistry INSTANCE;

    public static ModelPartSpecRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (ModelPartSpecRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ModelPartSpecRegistry();
                }
            }
        }
        return INSTANCE;
    }

    private ModelPartSpecRegistry() {
    }
    
}
