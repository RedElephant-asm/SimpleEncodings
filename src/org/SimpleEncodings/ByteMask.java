package org.SimpleEncodings;

/**
 * Класс, являющий собой модель маски байта.
 */
public class ByteMask {

    private byte mask;

    public ByteMask(byte mask) {
        this.mask = mask;
    }

    public ByteMask() {
    }

    public byte getMask() {
        return mask;
    }

    public void setMask(byte mask) {
        this.mask = mask;
    }
}
