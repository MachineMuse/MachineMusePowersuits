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
    private HashMap<S, T> nameMap;
    private HashMap<T, S> elemMap;

    public MuseBiMap() {
        this.nameMap = (HashMap<S, T>)new HashMap();
        this.elemMap = (HashMap<T, S>)new HashMap();
    }

    public T get(S name) {
        return this.nameMap.get(name);
    }

    public Iterable<T> elems() {
        return nameMap.values();
    }

    public Iterable<S> names() {
        return elemMap.values();
    }

    public T putName(S name, T elem) {
        T value = this.nameMap.get(name);
        if (value != null) {
            MuseLogger.logError(name + " already a member!");
            //------------------------------------
//            nameMap.remove(name);
//            nameMap.put(name, elem);
//
//            elemMap.remove(elem);
//            elemMap.put(elem, name);
//            //------------------------------------

            return value;
        } else {
            nameMap.put(name, elem);
            elemMap.put(elem, name);
            return elem;
        }
    }

    public S putElem(T elem, S name) {
//        S value = elemMap.get(elem);
//        if (value != null) {
//            //------------------------------------
//            nameMap.remove(name);
//            nameMap.put(name, elem);
//
//            elemMap.remove(elem);
//            elemMap.put(elem, name);
//            //------------------------------------
//
//            MuseLogger.logError(name + " already a member!");
//            return value;
//        } else {
//            nameMap.put(name, elem);
//            elemMap.put(elem, name) ;
//        }
//        return name;

        T value = nameMap.get(name);
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

    public S getName(T elem) {
        return elemMap.get(elem);
    }

    public S removeElem(T elem) {
        S name = this.getName(elem);
        if (name != null) {
            nameMap.remove(name);
            elemMap.remove(elem);
        }
        return name;
    }

    public T removeName(S name) {
        T value = this.get(name);
        if (value != null) {
            nameMap.remove(name);
            elemMap.remove(value);
        }
        return value;
    }
}