package org.guce.siat.web.common.util;

import java.io.Serializable;

import org.guce.siat.common.model.Appointment;
import org.guce.siat.core.ct.model.AnalyseOrder;
import org.guce.siat.core.ct.model.AnalyseResult;
import org.guce.siat.core.ct.model.ApprovedDecision;
import org.guce.siat.core.ct.model.CCTCPParamValue;
import org.guce.siat.core.ct.model.InspectionReport;
import org.guce.siat.core.ct.model.InterceptionNotification;
import org.guce.siat.core.ct.model.PaymentData;
import org.guce.siat.core.ct.model.TreatmentInfos;
import org.guce.siat.core.ct.model.TreatmentOrder;
import org.guce.siat.core.ct.model.TreatmentResult;

/**
 * The Class CctSpecificDecisionHistory.
 */
/**
 *
 */
public class CctSpecificDecisionHistory implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -7311974274002561736L;
    /**
     * The last decision ir.
     */
    private InspectionReport lastDecisionIR;

    /**
     * The last decision app.
     */
    private Appointment lastDecisionApp;

    /**
     * The last analyst order.
     */
    private AnalyseOrder lastAnalyseOrder;

    /**
     * The last treatment order.
     */
    private TreatmentOrder lastTreatmentOrder;

    /**
     * The last analyse result.
     */
    private AnalyseResult lastAnalyseResult;

    /**
     * The last treatment result.
     */
    private TreatmentResult lastTreatmentResult;
    /**
     * The decision details ir.
     */
    private InspectionReport decisionDetailsIR;

    /**
     * The decision details app.
     */
    private Appointment decisionDetailsApp;

    /**
     * The decision details ao.
     */
    private AnalyseOrder decisionDetailsAO;

    /**
     * The decision details to.
     */
    private TreatmentOrder decisionDetailsTO;

    /**
     * The decision details ar.
     */
    private AnalyseResult decisionDetailsAR;

    /**
     * The decision details tr.
     */
    private TreatmentResult decisionDetailsTR;

    /**
     * The decision details pay dada.
     */
    private PaymentData decisionDetailsPayData;

    /**
     * The last payment data.
     */
    private PaymentData lastPaymentData;

    private InterceptionNotification lastInterceptionNotification;

    private InterceptionNotification decisionDetailsNI;

    private TreatmentInfos lastTreatmentInfos;

    private TreatmentInfos decisionDetailsTI;

    private ApprovedDecision approvedDecision;

    private CCTCPParamValue cCTCPParamValue;

    /**
     * Gets the last decision ir.
     *
     * @return the last decision ir
     */
    public InspectionReport getLastDecisionIR() {
        return lastDecisionIR;
    }

    /**
     * Sets the last decision ir.
     *
     * @param lastDecisionIR the new last decision ir
     */
    public void setLastDecisionIR(final InspectionReport lastDecisionIR) {
        this.lastDecisionIR = lastDecisionIR;
    }

    /**
     * Gets the last decision app.
     *
     * @return the last decision app
     */
    public Appointment getLastDecisionApp() {
        return lastDecisionApp;
    }

    /**
     * Sets the last decision app.
     *
     * @param lastDecisionApp the new last decision app
     */
    public void setLastDecisionApp(final Appointment lastDecisionApp) {
        this.lastDecisionApp = lastDecisionApp;
    }

    /**
     * Gets the last analyse order.
     *
     * @return the last analyse order
     */
    public AnalyseOrder getLastAnalyseOrder() {
        return lastAnalyseOrder;
    }

    /**
     * Sets the last analyse order.
     *
     * @param lastAnalyseOrder the new last analyse order
     */
    public void setLastAnalyseOrder(final AnalyseOrder lastAnalyseOrder) {
        this.lastAnalyseOrder = lastAnalyseOrder;
    }

    /**
     * Gets the last treatment order.
     *
     * @return the last treatment order
     */
    public TreatmentOrder getLastTreatmentOrder() {
        return lastTreatmentOrder;
    }

    /**
     * Sets the last treatment order.
     *
     * @param lastTreatmentOrder the new last treatment order
     */
    public void setLastTreatmentOrder(final TreatmentOrder lastTreatmentOrder) {
        this.lastTreatmentOrder = lastTreatmentOrder;
    }

    /**
     * Gets the last analyse result.
     *
     * @return the last analyse result
     */
    public AnalyseResult getLastAnalyseResult() {
        return lastAnalyseResult;
    }

    /**
     * Sets the last analyse result.
     *
     * @param lastAnalyseResult the new last analyse result
     */
    public void setLastAnalyseResult(final AnalyseResult lastAnalyseResult) {
        this.lastAnalyseResult = lastAnalyseResult;
    }

    /**
     * Gets the last treatment result.
     *
     * @return the last treatment result
     */
    public TreatmentResult getLastTreatmentResult() {
        return lastTreatmentResult;
    }

    /**
     * Sets the last treatment result.
     *
     * @param lastTreatmentResult the new last treatment result
     */
    public void setLastTreatmentResult(final TreatmentResult lastTreatmentResult) {
        this.lastTreatmentResult = lastTreatmentResult;
    }

    /**
     * Gets the decision details ir.
     *
     * @return the decision details ir
     */
    public InspectionReport getDecisionDetailsIR() {
        return decisionDetailsIR;
    }

    /**
     * Sets the decision details ir.
     *
     * @param decisionDetailsIR the new decision details ir
     */
    public void setDecisionDetailsIR(final InspectionReport decisionDetailsIR) {
        this.decisionDetailsIR = decisionDetailsIR;
    }

    /**
     * Gets the decision details app.
     *
     * @return the decision details app
     */
    public Appointment getDecisionDetailsApp() {
        return decisionDetailsApp;
    }

    /**
     * Sets the decision details app.
     *
     * @param decisionDetailsApp the new decision details app
     */
    public void setDecisionDetailsApp(final Appointment decisionDetailsApp) {
        this.decisionDetailsApp = decisionDetailsApp;
    }

    /**
     * Gets the decision details ao.
     *
     * @return the decision details ao
     */
    public AnalyseOrder getDecisionDetailsAO() {
        return decisionDetailsAO;
    }

    /**
     * Sets the decision details ao.
     *
     * @param decisionDetailsAO the new decision details ao
     */
    public void setDecisionDetailsAO(final AnalyseOrder decisionDetailsAO) {
        this.decisionDetailsAO = decisionDetailsAO;
    }

    /**
     * Gets the decision details to.
     *
     * @return the decision details to
     */
    public TreatmentOrder getDecisionDetailsTO() {
        return decisionDetailsTO;
    }

    /**
     * Sets the decision details to.
     *
     * @param decisionDetailsTO the new decision details to
     */
    public void setDecisionDetailsTO(final TreatmentOrder decisionDetailsTO) {
        this.decisionDetailsTO = decisionDetailsTO;
    }

    /**
     * Gets the decision details ar.
     *
     * @return the decision details ar
     */
    public AnalyseResult getDecisionDetailsAR() {
        return decisionDetailsAR;
    }

    /**
     * Sets the decision details ar.
     *
     * @param decisionDetailsAR the new decision details ar
     */
    public void setDecisionDetailsAR(final AnalyseResult decisionDetailsAR) {
        this.decisionDetailsAR = decisionDetailsAR;
    }

    /**
     * Gets the decision details tr.
     *
     * @return the decision details tr
     */
    public TreatmentResult getDecisionDetailsTR() {
        return decisionDetailsTR;
    }

    /**
     * Sets the decision details tr.
     *
     * @param decisionDetailsTR the new decision details tr
     */
    public void setDecisionDetailsTR(final TreatmentResult decisionDetailsTR) {
        this.decisionDetailsTR = decisionDetailsTR;
    }

    /**
     * Gets the decision details pay data.
     *
     * @return the decision details pay data
     */
    public PaymentData getDecisionDetailsPayData() {
        return decisionDetailsPayData;
    }

    /**
     * Sets the decision details pay data.
     *
     * @param decisionDetailsPayData the new decision details pay data
     */
    public void setDecisionDetailsPayData(final PaymentData decisionDetailsPayData) {
        this.decisionDetailsPayData = decisionDetailsPayData;
    }

    /**
     * Gets the last payment data.
     *
     * @return the last payment data
     */
    public PaymentData getLastPaymentData() {
        return lastPaymentData;
    }

    /**
     * Sets the last payment data.
     *
     * @param lastPaymentData the new last payment data
     */
    public void setLastPaymentData(final PaymentData lastPaymentData) {
        this.lastPaymentData = lastPaymentData;
    }

    public InterceptionNotification getLastInterceptionNotification() {
        return lastInterceptionNotification;
    }

    public void setLastInterceptionNotification(InterceptionNotification lastInterceptionNotification) {
        this.lastInterceptionNotification = lastInterceptionNotification;
    }

    public InterceptionNotification getDecisionDetailsNI() {
        return decisionDetailsNI;
    }

    public void setDecisionDetailsNI(InterceptionNotification decisionDetailsNI) {
        this.decisionDetailsNI = decisionDetailsNI;
    }

    public TreatmentInfos getLastTreatmentInfos() {
        return lastTreatmentInfos;
    }

    public void setLastTreatmentInfos(final TreatmentInfos lastTreatmentInfos) {
        this.lastTreatmentInfos = lastTreatmentInfos;
    }

    public TreatmentInfos getDecisionDetailsTI() {
        return decisionDetailsTI;
    }

    public void setDecisionDetailsTI(final TreatmentInfos decisionDetailsTI) {
        this.decisionDetailsTI = decisionDetailsTI;
    }

    public ApprovedDecision getApprovedDecision() {
        return approvedDecision;
    }

    public void setApprovedDecision(ApprovedDecision approvedDecision) {
        this.approvedDecision = approvedDecision;
    }

    public CCTCPParamValue getcCTCPParamValue() {
        return cCTCPParamValue;
    }

    public void setcCTCPParamValue(CCTCPParamValue cCTCPParamValue) {
        this.cCTCPParamValue = cCTCPParamValue;
    }

}
