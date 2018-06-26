package org.guce.siat.web.ct.controller.util.enums;

/**
 * The Enum LocaleValues.
 */
public enum DataTypePropEnum {

    /**
     * The inputtextarea.
     */
    PATTERN("pattern");

    /**
     * The inputnumber.
     */
    //	INPUTNUMBER("inputNumber");
    /**
     * The code.
     */
    private final String code;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private DataTypePropEnum(final String code) {
        this.code = code.intern();
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public String getCode() {
        return this.code;
    }
}
