package net.machinemuse.numina.scala;

import net.machinemuse.numina.general.MuseLogger;

import java.util.HashMap;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:30 AM, 29/04/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class MuseBiMap<S, T> {
    private HashMap<S, T> nameMap;// = new HashMap<>();
    private HashMap<T, S> elemMap;//= new HashMap<>();

    public MuseBiMap() {
        this.nameMap = (HashMap<S, T>)new HashMap();
        this.elemMap = (HashMap<T, S>)new HashMap();
    }

    public T get(final S name) {
        return this.nameMap.get(name);
    }

    public Iterable<T> elems() {
        return nameMap.values();
    }

    public Iterable<S> names() {
        return elemMap.values();
    }

    public T putName(final S name, final T elem) {
        final T value = this.nameMap.get(name);
        if (value != null) {
            MuseLogger.logError(name + " already a member!");
            return value;
        } else {
            nameMap.put(name, elem);
            elemMap.put(elem, name);
            return elem;
        }
    }

    public S putElem(final T elem, final S name) {
        final T value = nameMap.get(name);
        if (value != null) {
            MuseLogger.logError(name + " already a member!");
        } else {
            nameMap.put(name, elem);
            elemMap.put(elem, name) ;
        }
        return name;
    }

    public HashMap<S, T> apply() {
        return this.nameMap;
    }

    public HashMap<T, S> inverse() {
        return this.elemMap;
    }

    public S getName(final T elem) {
        return elemMap.get(elem);
    }

    public S removeElem(final T elem) {
        final S name = this.getName(elem);
        if (name != null) {
            nameMap.remove(name);
            elemMap.remove(elem);
        }
        return name;
    }

    public T removeName(final S name) {
        final T value = this.get(name);
        if (value != null) {
            nameMap.remove(name);
            elemMap.remove(value);
        }
        return value;
    }
}