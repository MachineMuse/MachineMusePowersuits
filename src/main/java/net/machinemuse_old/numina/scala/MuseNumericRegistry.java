package net.machinemuse_old.numina.scala;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:30 AM, 29/04/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class MuseNumericRegistry<T> extends MuseBiMap<Integer, T> {
    public T put(final int name, final T elem) {
        return this.putName(name, elem);
    }

    public int put(final T elem, final int name) {
        return this.putElem(elem, name);
    }
}