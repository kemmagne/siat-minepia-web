package org.guce.siat.web.ct.data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author yenke
 */
public class GlobalQuantityListingData implements Serializable {

    private static final long serialVersionUID = 8842048170730589629L;

    private String processName;
    private String expecNumber;
    private String subfileNumber;
    private String exporterNiu;
    private String exporterName;
    private String productType;
    private String destinationCountry;
    private String step;
    private Date entryDate;
    private Date releaseDate;
    private String globalDelay;
    private String officeCode;
    private String officeLabel;
    private String cdaName;
    private String volume;
    private String quantity;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getExpecNumber() {
        return expecNumber;
    }

    public void setExpecNumber(String expecNumber) {
        this.expecNumber = expecNumber;
    }

    public String getSubfileNumber() {
        return subfileNumber;
    }

    public void setSubfileNumber(String subfileNumber) {
        this.subfileNumber = subfileNumber;
    }

    public String getExporterNiu() {
        return exporterNiu;
    }

    public void setExporterNiu(String exporterNiu) {
        this.exporterNiu = exporterNiu;
    }

    public String getExporterName() {
        return exporterName;
    }

    public void setExporterName(String exporterName) {
        this.exporterName = exporterName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGlobalDelay() {
        return globalDelay;
    }

    public void setGlobalDelay(String globalDelay) {
        this.globalDelay = globalDelay;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeLabel() {
        return officeLabel;
    }

    public void setOfficeLabel(String officeLabel) {
        this.officeLabel = officeLabel;
    }

    public String getCdaName() {
        return cdaName;
    }

    public void setCdaName(String cdaName) {
        this.cdaName = cdaName;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
