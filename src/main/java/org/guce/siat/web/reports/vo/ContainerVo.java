package org.guce.siat.web.reports.vo;

import java.math.BigDecimal;

/**
 *
 * @author ht
 */
public class ContainerVo {

    private String contNumber;
    private String contSeal1;
    private String contDenomination;
    private Integer contNumberOfPackages;
    private BigDecimal contGrossMass;
    private BigDecimal contVolume;
    private String contMark;
    private String contType;
    private Boolean contRefrigerated;

    private String productType;

    private String contDenominationLabel;
    private String contNumberOfPackagesLabel;
    private String contQuantityLabel;

    public String getContNumber() {
        return contNumber;
    }

    public void setContNumber(String contNumber) {
        this.contNumber = contNumber;
    }

    public String getContSeal1() {
        return contSeal1;
    }

    public void setContSeal1(String contSeal1) {
        this.contSeal1 = contSeal1;
    }

    public String getContDenomination() {
        return contDenomination;
    }

    public void setContDenomination(String contDenomination) {
        this.contDenomination = contDenomination;
    }

    public Integer getContNumberOfPackages() {
        return contNumberOfPackages;
    }

    public void setContNumberOfPackages(Integer contNumberOfPackages) {
        this.contNumberOfPackages = contNumberOfPackages;
    }

    public BigDecimal getContGrossMass() {
        return contGrossMass;
    }

    public void setContGrossMass(BigDecimal contGrossMass) {
        this.contGrossMass = contGrossMass;
    }

    public BigDecimal getContVolume() {
        return contVolume;
    }

    public void setContVolume(BigDecimal contVolume) {
        this.contVolume = contVolume;
    }

    public String getContMark() {
        return contMark;
    }

    public void setContMark(String contMark) {
        this.contMark = contMark;
    }

    public String getContType() {
        return contType;
    }

    public void setContType(String contType) {
        this.contType = contType;
    }

    public Boolean getContRefrigerated() {
        return contRefrigerated;
    }

    public void setContRefrigerated(Boolean contRefrigerated) {
        this.contRefrigerated = contRefrigerated;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getContDenominationLabel() {
        return contDenominationLabel;
    }

    public void setContDenominationLabel(String contDenominationLabel) {
        this.contDenominationLabel = contDenominationLabel;
    }

    public String getContNumberOfPackagesLabel() {
        return contNumberOfPackagesLabel;
    }

    public void setContNumberOfPackagesLabel(String contNumberOfPackagesLabel) {
        this.contNumberOfPackagesLabel = contNumberOfPackagesLabel;
    }

    public String getContQuantityLabel() {
        return contQuantityLabel;
    }

    public void setContQuantityLabel(String contQuantityLabel) {
        this.contQuantityLabel = contQuantityLabel;
    }

}
