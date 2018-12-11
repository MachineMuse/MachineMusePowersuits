package net.machinemuse.numina.capabilities.energy.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Blacklist for items that don't behave normally and may cause issues like unlimited power
 */
public enum BlackList {
    INSTANCE;

    public static List<String> blacklistModIds = new ArrayList<String>() {{
        add("appliedenergistics2");
        add("extracells");
    }};
}