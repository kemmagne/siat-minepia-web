package org.guce.siat.web.common.util;

/**
 * The Enum CctSpecificDecision.
 */
public enum CctSpecificDecision {

    /**
     * The Inspection report/Constat.
     */
    IR("Inspection report/Constat"),
    /**
     * The Appointment.
     */
    APP("Appointment"),
    /**
     * The Analyse request.
     */
    AN("Analyse request"),
    /**
     * The Treatment request.
     */
    TR("Treatment request"),
    /**
     * The Analyse result.
     */
    ANR("Analyse result"),
    /**
     * The Treatment result.
     */
    TRR("Treatment result"),
    /**
     * The documentary control certificate.
     */
    DCC("Documentary control certificate"),
    /**
     * Phytosanitary Certificate.
     */
    CCT_CP("Phytosanitary Certificate"),
    /**
     * Phytosanitary Certificate.
     */
    CCT_CT_E_BILL("Phytosanitary Certificate Billing");

    /**
     * The label.
     */
    private String label;

    /**
     * Instantiates a new cct specific decision.
     *
     * @param label the label
     */
    private CctSpecificDecision(final String label) {
        this.label = label;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label the new label
     */
    public void setLabel(final String label) {
        this.label = label;
    }

}
