package org.guce.siat.web.ct.data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ht
 */
public class PottingReportDto implements Serializable {

    private String number;

    private Date date;

    private String signatory;

    private String tcNumber;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSignatory() {
        return signatory;
    }

    public void setSignatory(String signatory) {
        this.signatory = signatory;
    }

    public String getTcNumber() {
        return tcNumber;
    }

    public void setTcNumber(String tcNumber) {
        this.tcNumber = tcNumber;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" + "number=" + number + ", date=" + date + ", signatory=" + signatory + ", tcNumber=" + tcNumber + '}';
    }

}
