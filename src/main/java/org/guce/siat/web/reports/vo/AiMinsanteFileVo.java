package org.guce.siat.web.reports.vo;

import java.util.Date;

/**
 * The Class AiMinsanteFileVo.
 */
public class AiMinsanteFileVo extends AbstractFileVo<AiMinsanteFileItemVo> {

    /**
     * The importer.
     */
    private String importer;

    /**
     * The validity date.
     */
    private Date validityDate;

    /**
     * The origin country.
     */
    private String originCountry;

    private String pharmacistName;

    private String pharmacistFunction;

    private String pharmacyName;

    private String importerName;

    private String importerAddress;

    private String manufacturerName;

    private String manufacturerAddress;

    private String supplierName;

    private String supplierAddress;

    private String transportMode;

    private String enterringCustomsOffice;

    private String productTypeFr;

    private String productTypeEn;

    private String productCommercialName;

    private String productINN;

    private String productQuantity;

    private String productPresentation;

    private String productForm;

    private String productDosage;

    private String reportTitle;

    /**
     * Gets the importer.
     *
     * @return the importer
     */
    public String getImporter() {
        return importer;
    }

    /**
     * Sets the importer.
     *
     * @param importer the new importer
     */
    public void setImporter(final String importer) {
        this.importer = importer;
    }

    /**
     * Gets the validity date.
     *
     * @return the validity date
     */
    public Date getValidityDate() {
        return validityDate;
    }

    /**
     * Sets the validity date.
     *
     * @param validityDate the new validity date
     */
    public void setValidityDate(final Date validityDate) {
        this.validityDate = validityDate;
    }

    /**
     * Gets the origin country.
     *
     * @return the origin country
     */
    public String getOriginCountry() {
        return originCountry;
    }

    /**
     * Sets the origin country.
     *
     * @param originCountry the new origin country
     */
    public void setOriginCountry(final String originCountry) {
        this.originCountry = originCountry;
    }

    public String getPharmacistName() {
        return pharmacistName;
    }

    public void setPharmacistName(String pharmacistName) {
        this.pharmacistName = pharmacistName;
    }

    public String getPharmacistFunction() {
        return pharmacistFunction;
    }

    public void setPharmacistFunction(String pharmacistFunction) {
        this.pharmacistFunction = pharmacistFunction;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerAddress() {
        return manufacturerAddress;
    }

    public void setManufacturerAddress(String manufacturerAddress) {
        this.manufacturerAddress = manufacturerAddress;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getEnterringCustomsOffice() {
        return enterringCustomsOffice;
    }

    public void setEnterringCustomsOffice(String enterringCustomsOffice) {
        this.enterringCustomsOffice = enterringCustomsOffice;
    }

    public String getProductCommercialName() {
        return productCommercialName;
    }

    public void setProductCommercialName(String productCommercialName) {
        this.productCommercialName = productCommercialName;
    }

    public String getProductINN() {
        return productINN;
    }

    public void setProductINN(String productINN) {
        this.productINN = productINN;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductPresentation() {
        return productPresentation;
    }

    public void setProductPresentation(String productPresentation) {
        this.productPresentation = productPresentation;
    }

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    public String getProductDosage() {
        return productDosage;
    }

    public void setProductDosage(String productDosage) {
        this.productDosage = productDosage;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getImporterName() {
        return importerName;
    }

    public void setImporterName(String importerName) {
        this.importerName = importerName;
    }

    public String getImporterAddress() {
        return importerAddress;
    }

    public void setImporterAddress(String importerAddress) {
        this.importerAddress = importerAddress;
    }

    public String getProductTypeFr() {
        return productTypeFr;
    }

    public void setProductTypeFr(String productTypeFr) {
        this.productTypeFr = productTypeFr;
    }

    public String getProductTypeEn() {
        return productTypeEn;
    }

    public void setProductTypeEn(String productTypeEn) {
        this.productTypeEn = productTypeEn;
    }

}
