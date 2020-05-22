package org.guce.siat.web.reports.vo;

import java.math.BigDecimal;

/**
 *
 * @author ht
 */
public class ContainerVo {

    private String contNumber;
    private String contSeal;
    private String contEssenceDenomination;
    private Integer contNumberOfPackages;
    private BigDecimal contGrossMass;
    private BigDecimal contVolume;
    private String contMark;
    private String contType;
    private Boolean contRefrigerated;

    public String getContNumber() {
        return contNumber;
    }

    public void setContNumber(String contNumber) {
        this.contNumber = contNumber;
    }

    public String getContSeal() {
        return contSeal;
    }

    public void setContSeal(String contSeal) {
        this.contSeal = contSeal;
    }

    public String getContEssenceDenomination() {
        return contEssenceDenomination;
    }

    public void setContEssenceDenomination(String contEssenceDenomination) {
        this.contEssenceDenomination = contEssenceDenomination;
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

}
