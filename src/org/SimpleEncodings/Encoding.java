package org.SimpleEncodings;

import java.util.Arrays;

/**
 * @author Savchenko Kirill
 * @version 1.0
 *
 * Класс, являющий собой модели кодировок.
 */
public class Encoding {

    public static final Encoding DEFAULT_UTF8 =
            new Encoding(
                    new SymbolTemplate(
                            new ByteTemplate((byte) 0b01111111, (byte) 0b00000000, new byte[]{0, 6})),
                    new SymbolTemplate(
                            new ByteTemplate((byte) 0b00011111, (byte) 0b11000000, new byte[]{0, 4}),
                            new ByteTemplate((byte) 0b00111111, (byte) 0b10000000, new byte[]{0, 5})),
                    new SymbolTemplate(
                            new ByteTemplate((byte) 0b00001111, (byte) 0b11100000, new byte[]{0, 3}),
                            new ByteTemplate((byte) 0b00111111, (byte) 0b10000000, new byte[]{0, 5}),
                            new ByteTemplate((byte) 0b00111111, (byte) 0b10000000, new byte[]{0, 5})),
                    new SymbolTemplate(
                            new ByteTemplate((byte) 0b00000111, (byte) 0b11110000, new byte[]{0, 2}),
                            new ByteTemplate((byte) 0b00111111, (byte) 0b10000000, new byte[]{0, 5}),
                            new ByteTemplate((byte) 0b00111111, (byte) 0b10000000, new byte[]{0, 5}),
                            new ByteTemplate((byte) 0b00111111, (byte) 0b10000000, new byte[]{0, 5})));

    public static final Encoding DEFAULT_UTF16LE =
            new Encoding(
                    new SymbolTemplate(
                            new ByteTemplate((byte) 0b11111111, (byte) 0b00000000, new byte[]{0, 6}),
                            new ByteTemplate((byte) 0b11111111, (byte) 0b00000000, new byte[]{0, 7})),
                    new SymbolTemplate(
                            new ByteTemplate((byte) 0b11111111, (byte) 0b00000000, new byte[]{0, 7}),
                            new ByteTemplate((byte) 0b00000001, (byte) 0b11011110, new byte[]{0, 0}),
                            new ByteTemplate((byte) 0b11111111, (byte) 0b00000000, new byte[]{0, 7}),
                            new ByteTemplate((byte) 0b00000001, (byte) 0b11011000, new byte[]{0, 0})));

    public static final Encoding DEFAULT_ASCII =
            new Encoding (
                    new SymbolTemplate(
                            new ByteTemplate((byte) 0b01111111, (byte) 0b10000000, new byte[]{0, 6})));

    public static final Encoding UNKNOWN_ENCODING = new Encoding();

    public static final double      MINIMAL_SYMBOL_GROUP_MATCH_RATIO =  0.8;
    public static final double      MINIMAL_BYTE_GROUP_MATCH_RATIO =    0.8;

    /**
     * Массив шаблонов, которые описывают формат символов в данной кодировке.
     */
    private SymbolTemplate[] templates;

    /**
     * Название кодировки.
     */
    private String encodingName;

    public Encoding(SymbolTemplate... templates) {
        this.templates = templates;
    }

    public Encoding() {
    }

    /**
     * Назначением функции является конвертация массива предположительно соответствующих данной кодировке байт в массив
     * соответствующих данной кодировке символов.
     * @param bytes
     * Массив байтов, из которого будет происходить конвртация.
     * @return
     * Количество байт, которые были успешно использованы при конвертации.
     */
    public Symbol[] convertEncodedByteArrayToEncodedSymbolArray(byte[] bytes){
        Symbol[] startupSymbolArray = new Symbol[bytes.length];
        int correctSymbolCounter = 0;
        boolean isCurrentSymbolVariantCorrect;
        Symbol currentSymbolVariant;
        for (int byteArrayOffset = 0; byteArrayOffset < bytes.length; byteArrayOffset++){
            for (SymbolTemplate currentTemplate : this.templates){
                currentSymbolVariant = new Symbol(Arrays.copyOfRange(bytes, byteArrayOffset, byteArrayOffset + currentTemplate.getByteTemplates().length));
                isCurrentSymbolVariantCorrect = currentTemplate.isSymbolMatchTheTemplate(currentSymbolVariant);
                if (isCurrentSymbolVariantCorrect){
                    /*
                    При обнаружении соответствия текущего варианта символа тому или иному шаблону данной кодировки, текущее вмещение внутри массива символов
                    будет увеличиваться на длину текущего варианта символа, уменьшенную на 1 для того, чтобы не портить архитектуру кода внешними ярлыками/
                    дополнительными условными конструкциями.
                     */
                    byteArrayOffset += currentSymbolVariant.getBytes().length - 1;
                    startupSymbolArray[correctSymbolCounter++] = currentSymbolVariant;
                }
            }
        }
        /*
        Вариант динамического выделения памяти, ориентированный на более выскокое потребление памяти за операцию(две операции)
        против варианта с использованием ArrayList, который инкапсулирует похожую функциональность, за исключением того, что по умолчанию
        обьем выделенной памяти под массив, который хранит ArrayList будет увеличиваться на размер одного вхождения каждый раз при
        добавлении нового элемента, что в случае с такой операцией, как считывание большого количества символов из файла и последующей
        передачей данного массива в данную функцию может породить соразмерно огромное количество операций выделения-освобождения памяти из
        кучи(с использованием сборщика мусора).
         */
        Symbol[] finalSymbolArray = Arrays.copyOfRange(startupSymbolArray, 0, correctSymbolCounter);
        return finalSymbolArray;
    }

    /**
     * Назначением функции является проверка на соответствие массива байт данной кодировке.
     * @param bytes
     * Указатель на массив проверяемых байтов.
     * @return
     * true в том случае, если процент байт, соответствующих данной кодировке, больше или равен
     * проценту, соответствующему MINIMAL_BYTE_GROUP_MATCH_RATIO.
     */
    public boolean isArrayOfBytesMatchTheEncoding(byte[] bytes){
        return isArrayOfBytesMatchTheEncoding(bytes, MINIMAL_BYTE_GROUP_MATCH_RATIO);
    }

    /**
     * Назначением функции является проверка на соответствие массива байт данной кодировке.
     * @param bytes
     * Указатель на массив проверяемых байтов.
     * @param ratio
     * Минимальный процент соответствующих байтов.
     * @return
     * true в том случае, если процент байт, соответствующих данной кодировке, больше или равен
     * проценту, соответствующему ratio.
     */
    public boolean isArrayOfBytesMatchTheEncoding(byte[] bytes, double ratio){
        int bytesMatchCount = getCorrectByteCount(bytes);
        return ((double)bytesMatchCount) / bytes.length >= ratio;
    }

    /**
     * Назначением функции является проверка на соответствия массива символов данной кодировке.
     * @param symbols
     * Указатель на массив проверяемых символов.
     * @return
     * true в том случае, если процент символов, соответствующих данной кодировке, больше или равен
     * проценту, соответствующему MINIMAL_SYMBOL_GROUP_MATCH_RATIO.
     */
    public boolean isArrayOfSymbolsMatchTheEncoding(Symbol[] symbols){
        return isArrayOfSymbolsMatchTheEncoding(symbols, MINIMAL_SYMBOL_GROUP_MATCH_RATIO);
    }

    /**
     * Назначением функции является проверка на соответствия массива символов данной кодировке.
     * @param symbols
     * Указатель на массив проверяемых символов.
     * @param ratio
     * Минимальный процент соответствующих символов.
     * @return
     * true в том случае, если процент символов, соответствующих данной кодировке, больше или равен
     * проценту, соответствующему ratio.
     */
    public boolean isArrayOfSymbolsMatchTheEncoding(Symbol[] symbols, double ratio){
        int matchCount = 0;
        for (Symbol currentSymbol : symbols) {
            if (isSymbolMatchTheEncoding(currentSymbol)){
                matchCount++;
            }
        }
        return ((double)matchCount) / symbols.length >= ratio;
    }

    /**
     * Назначением функции является подсчет вхождений массива байтов, которые соответствуют данной кодировке.
     * @param bytes
     * Массив байтов.
     * @return
     * Колличество вхождений массива байтов, которые соответствуют данной кодировке.
     */
    public int getCorrectByteCount(byte[] bytes){
        int correctByteCount = 0;
        boolean isCurrentSymbolVariantCorrect;
        Symbol currentSymbolVariant;
        for (int byteArrayOffset = 0; byteArrayOffset < bytes.length; byteArrayOffset++){
            for (SymbolTemplate currentTemplate : this.templates){
                currentSymbolVariant = new Symbol(Arrays.copyOfRange(bytes, byteArrayOffset, byteArrayOffset + currentTemplate.getByteTemplates().length));
                isCurrentSymbolVariantCorrect = currentTemplate.isSymbolMatchTheTemplate(currentSymbolVariant);
                if (isCurrentSymbolVariantCorrect){
                    /*
                    При обнаружении соответствия текущего варианта символа тому или иному шаблону данной кодировки, текущее вмещение внутри массива символов
                    будет увеличиваться на длину текущего варианта символа, уменьшенную на 1 для того, чтобы не портить архитектуру кода внешними ярлыками/
                    дополнительными условными конструкциями.
                     */
                    byteArrayOffset += currentSymbolVariant.getBytes().length - 1;
                    correctByteCount += currentSymbolVariant.getBytes().length;
                }
            }
        }
        return correctByteCount;
    }

    /**
     * Назначением функции является проверка на соответствие символа данной кодировке.
     * @param symbol
     * Проверяемый символ.
     * @return
     * true в том случае, если символ соответствует данной кодировке, в противном случае false.
     */
    public boolean isSymbolMatchTheEncoding(Symbol symbol){
        SymbolTemplate template = findTemplateByByteCount(symbol.getBytes().length);
        if (template == SymbolTemplate.UNDEFINED_TEMPLATE){
            return false;
        }
        return template.isSymbolMatchTheTemplate(symbol);
    }

    /**
     * Назначением функции является поиск шаблонов символов данной кодировки по указанной длинне.
     * @param byteCount
     * Длинна шаблона символа.
     * @return
     * Указатель на подходящий шаблон символов.
     */
    public SymbolTemplate findTemplateByByteCount(int byteCount){
        for (SymbolTemplate currentTemplate : this.templates) {
            if (currentTemplate.getByteTemplates().length == byteCount){
                return currentTemplate;
            }
        }
        return SymbolTemplate.UNDEFINED_TEMPLATE;
    }

    /**
     * Назначением функции является получение численного эквивалента символа в данной кодировке.
     * @return
     * Численное значение декодированного символа.
     */
    public int getSymbolValuablePart(Symbol symbol){
        if (! isSymbolMatchTheEncoding(symbol)){
            return 0;
        }
        SymbolTemplate currentSymbolTemplate = findTemplateByByteCount(symbol.getBytes().length);
        return currentSymbolTemplate.getSymbolValuablePart(symbol);
    }

    public SymbolTemplate[] getTemplates() {
        return templates;
    }

    public void setTemplates(SymbolTemplate[] templates) {
        this.templates = templates;
    }

    public String getEncodingName() {
        return encodingName;
    }

    public void setEncodingName(String encodingName) {
        this.encodingName = encodingName;
    }
}
