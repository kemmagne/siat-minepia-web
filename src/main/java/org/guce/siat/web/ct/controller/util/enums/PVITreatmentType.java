package org.guce.siat.web.ct.controller.util.enums;

/**
 * The Enum LocaleValues.
 */
public enum PVITreatmentType {

    PULVERISATION("pulverisation"),
    TREMPAGE("trempage"),
    FUMIGRATION("fumigation"),
    CHALEUR("chaleur");

    /**
     * The label.
     */
    private final String label;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private PVITreatmentType(final String label) {
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
