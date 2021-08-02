package org.SimpleEncodings;

/**
 * @author Savchenko Kirill
 * @version 1.0
 *
 * Класс, являющий собой модель символа.
 */
public class Symbol {

    /**
     * Массив составных байтов символа.
     */
    private byte[] bytes;

    public Symbol(byte[] bytes){
        this.bytes = bytes;
    }

    public Symbol(){

    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
