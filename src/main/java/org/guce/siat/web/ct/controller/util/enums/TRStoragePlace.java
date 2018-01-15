package org.guce.siat.web.ct.controller.util.enums;

/**
 *
 * @author tadzotsa
 */
public enum TRStoragePlace {

    MAGASIN("magasin"),
    HANGAR("hangar"),
    AIR_LIBRE("airLibre");

    /**
     * The label.
     */
    private final String label;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private TRStoragePlace(final String label) {
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
