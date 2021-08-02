package org.SimpleEncodings;

/**
 * @author Savchenko Kirill
 * @version 1.0
 *
 * Класс являет собой модель шаблона символа.
 */
public class SymbolTemplate {

    /**
     * Стандартный шаблон для символов кодировки ASCII 128.
     */
    public static final SymbolTemplate ASCII128_DEFAULT_TEMPLATE = new SymbolTemplate(
            new ByteTemplate((byte) 0b01111111, (byte) 0b10000000, (byte) 7, new byte[]{0, 6})
    );

    /**
     * Неопределенный шаблон (помогает избежать использования null при построении циклов поиска в массивах).
     */
    public static final SymbolTemplate UNDEFINED_TEMPLATE = new SymbolTemplate();

    /**
     * Набор шаблонов символов данной кодировки.
     */
    private ByteTemplate[] byteTemplates;

    public SymbolTemplate(ByteTemplate... byteTemplates) {
        this.byteTemplates = byteTemplates;
    }

    private SymbolTemplate() {

    }

    /**
     * Назначением функции является проверка символа на соответствие данному шаблону.
     * @param symbol
     * Проверяемый символ.
     * @return
     * true, если проверяемый символ соответствует данному шаблону, в противном случае false.
     */
    public boolean isSymbolMatchTheTemplate(Symbol symbol){
        if (symbol.getBytes().length != byteTemplates.length){
            return false;
        }
        for (int byteCounter = 0; byteCounter < byteTemplates.length; byteCounter++){
            if (!byteTemplates[byteCounter].isByteMatchTheTemplate(symbol.getBytes()[byteCounter])){
                return false;
            }
        }
        return true;
    }

    public ByteTemplate[] getByteTemplates() {
        return byteTemplates;
    }

    public void setByteTemplates(ByteTemplate[] byteTemplates) {
        this.byteTemplates = byteTemplates;
    }
}
