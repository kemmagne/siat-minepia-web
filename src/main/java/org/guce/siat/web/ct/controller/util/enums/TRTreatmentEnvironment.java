package org.guce.siat.web.ct.controller.util.enums;

/**
 *
 * @author tadzotsa
 */
public enum TRTreatmentEnvironment {

    CONTENEUR("Conteneur"),
    MAGASIN_SPECIALISE("MagasinSpecialise"),
    SOUS_BACHE("SousBache"),
    FOUR("Four");

    /**
     * The label.
     */
    private final String label;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private TRTreatmentEnvironment(final String label) {
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
