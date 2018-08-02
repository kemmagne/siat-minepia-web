package org.guce.siat.web.ct.controller.util.enums;

/**
 * The Enum LocaleValues.
 */
public enum TRWeatherCondition {

    SAISON_SECHE("saisonSeche"),
    SAISON_PLUVIEUSE("saisonPluvieuse");

    /**
     * The label.
     */
    private final String label;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private TRWeatherCondition(final String label) {
        this.label = label.intern();
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }
}
