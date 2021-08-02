package org.SimpleEncodings;

/**
 * Класс, являющий собой модель маски байта.
 */
public class ByteMask {

    private byte mask;

    private int[][] valuePartsIntervals;

    public ByteMask(byte mask) {
        this.mask = mask;
    }

    public Byteask() {

    }

    public byte getMask() {
        return mask;
    }

    public void setMask(byte mask) {
        this.mask = mask;
    }

    public void setValuePartsIntervals(int[][] valuePartsIntervals){
	    this.valuePartsIntervals = valuePartsIntervals;
    }	 
    
    public int[][] getValuePartsIntervals(){
        return this.valuePartsIntervals;
    }
} 
