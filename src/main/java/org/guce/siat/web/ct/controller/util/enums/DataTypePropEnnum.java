package org.guce.siat.web.ct.controller.util.enums;

/**
 * The Enum LocaleValues.
 */
public enum DataTypePropEnnum {

    /**
     * The inputtextarea.
     */
    PATTERN("pattern"),
    FILE_TYPES("fileTypes");

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
    private DataTypePropEnnum(final String code) {
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
