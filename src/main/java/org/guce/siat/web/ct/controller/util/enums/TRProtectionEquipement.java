package org.guce.siat.web.ct.controller.util.enums;

/**
 *
 * @author tadzotsa
 */
public enum TRProtectionEquipement {

    MASQUE("Masque"),
    GANG("Gang"),
    BOTTES("Bottes"),
    COMBINAISON("Combinaison"),
    LUNETTES("Lunettes");

    /**
     * The label.
     */
    private final String label;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private TRProtectionEquipement(final String label) {
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
