package org.guce.siat.web.ct.controller.util.enums;

/**
 *
 * @author tadzotsa
 */
public enum TRConditioning {

    CONTENEUR("conteneur"),
    AIR_LIBRE("airLibre"),
    SOUS_BACHE("SousBache");

    /**
     * The label.
     */
    private final String label;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private TRConditioning(final String label) {
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
