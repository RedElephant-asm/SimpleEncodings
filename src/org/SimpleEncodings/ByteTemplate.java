package org.SimpleEncodings;

/**
 * @author Savchenko Kirill
 * @version 1.0
 *
 * Класс, являющий собой модель маски байта.
 */
public class ByteTemplate {

    /**
     * Маска ценной части байта.
     */
    private byte valuablePartMask;

    /**
     *  Маска идентификационной части байта.
     */
    private byte identifyingPartMask;

    /**
     * Количество бит для значимой части байта.
     */
    private byte valuablePartSize;

    /**
     * Массив диапазонов частей ценной части байта.
     */
    private byte[][] valuablePartIntervals;

    public ByteTemplate(byte valuablePartsMask, byte identifyingPartsMask, byte valuablePartSize, byte[]... valuablePartIntervals) {
        this.valuablePartMask = valuablePartsMask;
        this.identifyingPartMask = identifyingPartsMask;
        this.valuablePartSize = valuablePartSize;
        this.valuablePartIntervals = valuablePartIntervals;
    }

    public ByteTemplate() {

    }

    /**
     * Назначением функции является получение значения закодированной в байте информации.
     * @param byteArg
     * Закодированный, по правилам соответствующей кодировки, байт инофрмации.
     * @return
     * Значащая чатсь закодированного байта информации.
     */
    public byte getByteValuablePart(byte byteArg) {
        byte byteValue = 0, currentIntervalMask, currentIntervalMaskLength;
        for (byte[] currentValuableInterval : valuablePartIntervals) {
            currentIntervalMaskLength = (byte) (currentValuableInterval[1] - currentValuableInterval[0] + 1);
            currentIntervalMask = (byte)( ( (1 << currentIntervalMaskLength) - 1) << currentValuableInterval[0] );
            byteValue |= (byte) (byteArg & currentIntervalMask);
        }
        return byteValue;
    }

    /**
     * Назначением функции является проверка байта на соответствие данному шаблону байта.
     * @param byteArg
     * Проверяемый байт.
     * @return
     * true, если проверяемый байт соответствует данному шаблону, в противном случае false.
     */
    public boolean isByteMatchTheTemplate(byte byteArg) {
        return (byteArg & ( ~valuablePartMask ) ) == identifyingPartMask;
    }

    public byte getValuePartMask() {
        return valuablePartMask;
    }

    public void setValuePartMask(byte valuablePartMask) {
        this.valuablePartMask = valuablePartMask;
    }

    public void setValuePartIntervals(byte[][] valuablePartIntervals){
	    this.valuablePartIntervals = valuablePartIntervals;
    }	 
    
    public byte[][] getValuePartIntervals(){
        return this.valuablePartIntervals;
    }

    public byte getIdentifyingPartMask() {
        return identifyingPartMask;
    }

    public void setIdentifyingPartMask(byte identifyingPartMask) {
        this.identifyingPartMask = identifyingPartMask;
    }

    public byte getValuablePartSize() {
        return valuablePartSize;
    }

    public void setValuablePartSize(byte valuablePartSize) {
        this.valuablePartSize = valuablePartSize;
    }
}
