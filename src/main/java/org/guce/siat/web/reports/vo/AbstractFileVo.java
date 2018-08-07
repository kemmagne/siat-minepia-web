package org.guce.siat.web.reports.vo;

import java.util.Date;
import java.util.List;

/**
 * The Class AbstractFileVo.
 *
 * @param <T> the generic type
 */
public class AbstractFileVo<T extends AbstractFileItemVo> {

    /**
     * The decision number.
     */
    private String decisionNumber;

    /**
     * The decision date.
     */
    private Date decisionDate;
    
    /**
     * The decision place.
     */
    private String decisionPlace;

    /**
     * The file item list.
     */
    private List<T> fileItemList;

    /**
     * The signatory.
     */
    private String signatory;

    /**
     * Gets the decision number.
     *
     * @return the decision number
     */
    public String getDecisionNumber() {
        return decisionNumber;
    }

    /**
     * Sets the decision number.
     *
     * @param decisionNumber the new decision number
     */
    public void setDecisionNumber(final String decisionNumber) {
        this.decisionNumber = decisionNumber;
    }

    /**
     * Gets the decision date.
     *
     * @return the decision date
     */
    public Date getDecisionDate() {
        return decisionDate;
    }

    /**
     * Sets the decision date.
     *
     * @param decisionDate the new decision date
     */
    public void setDecisionDate(final Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    /**
     * Gets the file item list.
     *
     * @return the fileItemList
     */
    public List<T> getFileItemList() {
        return fileItemList;
    }

    /**
     * Sets the file item list.
     *
     * @param fileItemList the fileItemList to set
     */
    public void setFileItemList(final List<T> fileItemList) {
        this.fileItemList = fileItemList;
    }

    /**
     * Gets the decision place.
     *
     * @return the decision place
     */
    public String getDecisionPlace() {
        return decisionPlace;
    }

    /**
     * Sets the decision place.
     *
     * @param decisionPlace the new decision place
     */
    public void setDecisionPlace(final String decisionPlace) {
        this.decisionPlace = decisionPlace;
    }

    /**
     * Gets the signatory.
     *
     * @return the signatory
     */
    public String getSignatory() {
        return signatory;
    }

    /**
     * Sets the signatory.
     *
     * @param signatory the new signatory
     */
    public void setSignatory(final String signatory) {
        this.signatory = signatory;
    }

}
