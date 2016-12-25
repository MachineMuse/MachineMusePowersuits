package net.machinemuse.numina.network;

import java.io.DataOutputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:15 AM, 09/05/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public final class RichOutputStream {
    DataOutputStream out;

    public static RichOutputStream toRichStream(DataOutputStream out) {
        return new RichOutputStream(out);
    }

    public RichOutputStream(DataOutputStream out) {
        this.out = out;
    }
}