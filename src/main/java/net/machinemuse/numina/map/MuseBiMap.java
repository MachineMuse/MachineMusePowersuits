package net.machinemuse.numina.map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.machinemuse.numina.basemod.MuseLogger;

import java.util.Map;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:30 AM, 29/04/13
 * <p>
 * Ported to Java by lehjr on 11/8/16.
 */
public class MuseBiMap<S, T> {
    private BiMap<S, T> theMap;

    public MuseBiMap() {
        this.theMap = HashBiMap.create();
    }

    public T get(S name) {
        return this.theMap.get(name);
    }

    public Iterable<T> elems() {
        return theMap.values();
    }

    public Iterable<S> names() {
        return theMap.keySet();
    }

    public T putName(S name, T elem) {
        if (this.theMap.containsKey(name)) {
            MuseLogger.logError(name + " already a member!");
        } else {
            this.theMap.put(name, elem);
        }
        return elem;
    }

    public S putElem(T elem, S name) {
        if (theMap.containsKey(name)) {
            MuseLogger.logError(name + " already a member!");
        } else {
            theMap.put(name, elem);
        }
        return name;
    }

    public Map<S,T> apply() {
        return this.theMap;
    }

    public Map<T, S> inverse() {
        return this.theMap.inverse();
    }

    public S getName(T elem) {
        return this.theMap.inverse().get(elem);
    }

    public S removeElem(T elem) {
        return this.theMap.inverse().remove(elem);
    }

    public T removeName(S name) {
        return this.theMap.remove(name);
    }
}