package org.guce.siat.web.reports.vo;

import java.util.Date;
import java.util.List;

/**
 *
 * @author tadzotsa
 */
public class CtCctCqeFileVo {

    private String certificateNumber;
    private String decisionPlace;
    private String exporterName;
    private String exporterAddress;
    private String consigneeName;
    private String consigneeAddress;
    private String countryOfOrigin;
    private String countryOfDestination;
    private String meanOfTransport;
    private String productCategory;
    private String packager;
    private String entryPhytoPolice;
    private String outPhytoPolice;
    private String forwarder;
    private String eguceNumber;
    private String preservationTemperature;
    private String totalQuantity;
    private String validity;
    private Date decisionDate;
    private String officerName;
    private List<CtCctCqeFileItemVo> fileItemList;

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getDecisionPlace() {
        return decisionPlace;
    }

    public void setDecisionPlace(String decisionPlace) {
        this.decisionPlace = decisionPlace;
    }

    public String getExporterName() {
        return exporterName;
    }

    public void setExporterName(String exporterName) {
        this.exporterName = exporterName;
    }

    public String getExporterAddress() {
        return exporterAddress;
    }

    public void setExporterAddress(String exporterAddress) {
        this.exporterAddress = exporterAddress;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getCountryOfDestination() {
        return countryOfDestination;
    }

    public void setCountryOfDestination(String countryOfDestination) {
        this.countryOfDestination = countryOfDestination;
    }

    public String getMeanOfTransport() {
        return meanOfTransport;
    }

    public void setMeanOfTransport(String meanOfTransport) {
        this.meanOfTransport = meanOfTransport;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getPackager() {
        return packager;
    }

    public void setPackager(String packager) {
        this.packager = packager;
    }

    public String getEntryPhytoPolice() {
        return entryPhytoPolice;
    }

    public void setEntryPhytoPolice(String entryPhytoPolice) {
        this.entryPhytoPolice = entryPhytoPolice;
    }

    public String getOutPhytoPolice() {
        return outPhytoPolice;
    }

    public void setOutPhytoPolice(String outPhytoPolice) {
        this.outPhytoPolice = outPhytoPolice;
    }

    public String getForwarder() {
        return forwarder;
    }

    public void setForwarder(String forwarder) {
        this.forwarder = forwarder;
    }

    public String getEguceNumber() {
        return eguceNumber;
    }

    public void setEguceNumber(String eguceNumber) {
        this.eguceNumber = eguceNumber;
    }

    public String getPreservationTemperature() {
        return preservationTemperature;
    }

    public void setPreservationTemperature(String preservationTemperature) {
        this.preservationTemperature = preservationTemperature;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public List<CtCctCqeFileItemVo> getFileItemList() {
        return fileItemList;
    }

    public void setFileItemList(List<CtCctCqeFileItemVo> fileItemList) {
        this.fileItemList = fileItemList;
    }

}

