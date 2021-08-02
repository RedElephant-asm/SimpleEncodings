package org.SimpleEncodings;

/**
 * @author Savchenko Kirill
 * @version 1.0
 *
 * Класс, являющий собой модели кодировок.
 */
public class Encoding {

    /**
     * Массив шаблонов, которые описывают формат символов в данной кодировке.
     */
    private SymbolTemplate[] templates;

    /**
     * Название кодировки.
     */
    private String encodingName;

    public Encoding(SymbolTemplate[] templates, String encodingName) {
        this.templates = templates;
        this.encodingName = encodingName;
    }

    public Encoding() {
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
        ByteTemplate currentByteTemplate;
        int valuablePart = 0;
        for (int byteCounter = 0; byteCounter < symbol.getBytes().length; byteCounter++){
            currentByteTemplate = currentSymbolTemplate.getByteTemplates()[byteCounter];
            valuablePart |= (currentByteTemplate.getByteValuablePart(symbol.getBytes()[byteCounter]) << currentByteTemplate.getValuablePartSize());
        }
        return valuablePart;
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
