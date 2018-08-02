package org.guce.siat.web.ct.controller.util.enums;

/**
 *
 * @author tadzotsa
 */
public enum TRProductUsed {

    FONGICIDE("Fongicide"),
    INSECTICIDE("Insecticide"),
    INSECTICIDE_FONGICIDE("InsecticideFongicide");

    /**
     * The label.
     */
    private final String label;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private TRProductUsed(final String label) {
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
