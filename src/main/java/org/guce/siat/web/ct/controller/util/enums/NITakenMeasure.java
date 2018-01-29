package org.guce.siat.web.ct.controller.util.enums;

/**
 *
 * @author tadzotsa
 */
public enum NITakenMeasure {

    DESTRUCTION("Destruction"),
    QUARANTAINE("Quarantaine"),
    REFOULEMENT("Refoulement"),
    PARTIES_INFESTEES("PartiesInfestees"),
    TRAITEMENT("Traitement");

    /**
     * The label.
     */
    private final String label;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private NITakenMeasure(final String label) {
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
