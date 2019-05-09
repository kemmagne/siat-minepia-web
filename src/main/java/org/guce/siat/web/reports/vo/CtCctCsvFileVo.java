package org.guce.siat.web.reports.vo;

import java.io.ByteArrayInputStream;
import java.util.Date;

/**
 * The Class CtCctCsqFileVo.
 */
public class CtCctCsvFileVo extends AbstractFileVo<CtCctCsvFileItemVo> {

    /**
     * The consignor name.
     */
    private String consignorName;

    /**
     * The consignor address1.
     */
    private String consignorAddress1;

    /**
     * The consignor address2.
     */
    private String consignorAddress2;

    /**
     * The consignor country.
     */
    private String consignorCountry;

    /**
     * The consignor telephone.
     */
    private String consignorTelephone;

    /**
     * The consignee name.
     */
    private String consigneeName;

    /**
     * The consignee address1.
     */
    private String consigneeAddress1;

    /**
     * The consignee address2.
     */
    private String consigneeAddress2;

    /**
     * The consignee country.
     */
    private String consigneeCountry;

    /**
     * The consignee telephone.
     */
    private String consigneeTelephone;

    /**
     * The certificate reference number.
     */
    private String certificateReferenceNumber;

    /**
     * The veterinary authority.
     */
    private String veterinaryAuthority;

    /**
     * The lc number.
     */
    private String lcNumber;

    /**
     * The cites permit number.
     */
    private String citesPermitNumber;

    /**
     * The expected border post.
     */
    private String expectedBorderPost;

    /**
     * The total number of packages.
     */
    private String totalNumberOfPackages;

    /**
     * The signatory name.
     */
    private String signatoryName;

    /**
     * The signatory address.
     */
    private String signatoryAddress;

    /**
     * The signatory position.
     */
    private String signatoryPosition;

    /**
     * The country of origin.
     */
    private String countryOfOrigin;

    /**
     * The zone of origin.
     */
    private String zoneOfOrigin;

    /**
     * The country of destination.
     */
    private String countryOfDestination;

    /**
     * The zone of destination.
     */
    private String zoneOfDestination;

    /**
     * The place of origin name.
     */
    private String placeOfOriginName;

    /**
     * The place of origin address.
     */
    private String placeOfOriginAddress;

    /**
     * The place of shipment.
     */
    private String placeOfShipment;
    /**
     * The place of shipment.
     */
    private String placeOfLoading;

    /**
     * The expedition date.
     */
    private Date expeditionDate;

    /**
     * The expedition hour.
     */
    private String expeditionHour;

    /**
     * The mode of transport.
     */
    private String modeOfTransport;

    /**
     * The means of transport.
     */
    private String meansOfTransport;

    /**
     * The transport identification.
     */
    private String transportIdentification;

    /**
     * The LTA reference of lading number.
     */
    private String ladingNumberLTA;

    /**
     * The cdeparture date of goods
     */
    private String cvsDepartureDate;

    /**
     * The Product Temperature
     */
    private String cvsProductTemperature;

    /**
     * The Nber of Packages Unit
     */
    private String cvsNbPackagedUnit;

    /**
     * The Nature of goods
     */
    private String cvsPackageNature;

    /**
     * The commodity usage purpose
     */
    private String cvsGoodFor;

    /**
     * The Species of good.
     */
    private String cvsGoodSpecies;

    /**
     * The Nature of good.
     */
    private String cvsGoodNature;

    /**
     * The Treatment of good.
     */
    private String cvsGoodTreatment;

    /**
     * The Packages Number of good.
     */
    private String cvsGoodPackageNumber;

    /**
     * The Packages Number Approved of good.
     */
    private String cvsGoodPackageApproved;

    /**
     * The Net Weight of good.
     */
    private String cvsGoodPackageNetWeight;
    /**
     * The Id containers seals of good.
     */
    private String cvsIdContainersSeals;
    /**
     * The permit CITES Number.
     */
    private String cvsPermitCITES;

    private ByteArrayInputStream qrCode;

    /**
     * Gets the consignor name.
     *
     * @return the consignor name
     */
    public String getConsignorName() {
        return consignorName;
    }

    /**
     * Sets the consignor name.
     *
     * @param consignorName the new consignor name
     */
    public void setConsignorName(final String consignorName) {
        this.consignorName = consignorName;
    }

    /**
     * Gets the consignor address1.
     *
     * @return the consignor address1
     */
    public String getConsignorAddress1() {
        return consignorAddress1;
    }

    /**
     * Sets the consignor address1.
     *
     * @param consignorAddress1 the new consignor address1
     */
    public void setConsignorAddress1(final String consignorAddress1) {
        this.consignorAddress1 = consignorAddress1;
    }

    /**
     * Gets the consignor address2.
     *
     * @return the consignor address2
     */
    public String getConsignorAddress2() {
        return consignorAddress2;
    }

    /**
     * Sets the consignor address2.
     *
     * @param consignorAddress2 the new consignor address2
     */
    public void setConsignorAddress2(final String consignorAddress2) {
        this.consignorAddress2 = consignorAddress2;
    }

    /**
     * Gets the consignor country.
     *
     * @return the consignor country
     */
    public String getConsignorCountry() {
        return consignorCountry;
    }

    /**
     * Sets the consignor country.
     *
     * @param consignorCountry the new consignor country
     */
    public void setConsignorCountry(final String consignorCountry) {
        this.consignorCountry = consignorCountry;
    }

    /**
     * Gets the consignor telephone.
     *
     * @return the consignor telephone
     */
    public String getConsignorTelephone() {
        return consignorTelephone;
    }

    /**
     * Sets the consignor telephone.
     *
     * @param consignorTelephone the new consignor telephone
     */
    public void setConsignorTelephone(final String consignorTelephone) {
        this.consignorTelephone = consignorTelephone;
    }

    /**
     * Gets the consignee name.
     *
     * @return the consignee name
     */
    public String getConsigneeName() {
        return consigneeName;
    }

    /**
     * Sets the consignee name.
     *
     * @param consigneeName the new consignee name
     */
    public void setConsigneeName(final String consigneeName) {
        this.consigneeName = consigneeName;
    }

    /**
     * Gets the consignee address1.
     *
     * @return the consignee address1
     */
    public String getConsigneeAddress1() {
        return consigneeAddress1;
    }

    /**
     * Sets the consignee address1.
     *
     * @param consigneeAddress1 the new consignee address1
     */
    public void setConsigneeAddress1(final String consigneeAddress1) {
        this.consigneeAddress1 = consigneeAddress1;
    }

    /**
     * Gets the consignee address2.
     *
     * @return the consignee address2
     */
    public String getConsigneeAddress2() {
        return consigneeAddress2;
    }

    /**
     * Sets the consignee address2.
     *
     * @param consigneeAddress2 the new consignee address2
     */
    public void setConsigneeAddress2(final String consigneeAddress2) {
        this.consigneeAddress2 = consigneeAddress2;
    }

    /**
     * Gets the consignee country.
     *
     * @return the consignee country
     */
    public String getConsigneeCountry() {
        return consigneeCountry;
    }

    /**
     * Sets the consignee country.
     *
     * @param consigneeCountry the new consignee country
     */
    public void setConsigneeCountry(final String consigneeCountry) {
        this.consigneeCountry = consigneeCountry;
    }

    /**
     * Gets the consignee telephone.
     *
     * @return the consignee telephone
     */
    public String getConsigneeTelephone() {
        return consigneeTelephone;
    }

    /**
     * Sets the consignee telephone.
     *
     * @param consigneeTelephone the new consignee telephone
     */
    public void setConsigneeTelephone(final String consigneeTelephone) {
        this.consigneeTelephone = consigneeTelephone;
    }

    /**
     * Gets the certificate reference number.
     *
     * @return the certificate reference number
     */
    public String getCertificateReferenceNumber() {
        return certificateReferenceNumber;
    }

    /**
     * Sets the certificate reference number.
     *
     * @param certificateReferenceNumber the new certificate reference number
     */
    public void setCertificateReferenceNumber(final String certificateReferenceNumber) {
        this.certificateReferenceNumber = certificateReferenceNumber;
    }

    /**
     * Gets the veterinary authority.
     *
     * @return the veterinary authority
     */
    public String getVeterinaryAuthority() {
        return veterinaryAuthority;
    }

    /**
     * Sets the veterinary authority.
     *
     * @param veterinaryAuthority the new veterinary authority
     */
    public void setVeterinaryAuthority(final String veterinaryAuthority) {
        this.veterinaryAuthority = veterinaryAuthority;
    }

    /**
     * Gets the lc number.
     *
     * @return the lc number
     */
    public String getLcNumber() {
        return lcNumber;
    }

    /**
     * Sets the lc number.
     *
     * @param lcNumber the new lc number
     */
    public void setLcNumber(final String lcNumber) {
        this.lcNumber = lcNumber;
    }

    /**
     * Gets the cites permit number.
     *
     * @return the cites permit number
     */
    public String getCitesPermitNumber() {
        return citesPermitNumber;
    }

    /**
     * Sets the cites permit number.
     *
     * @param citesPermitNumber the new cites permit number
     */
    public void setCitesPermitNumber(final String citesPermitNumber) {
        this.citesPermitNumber = citesPermitNumber;
    }

    /**
     * Gets the expected border post.
     *
     * @return the expected border post
     */
    public String getExpectedBorderPost() {
        return expectedBorderPost;
    }

    /**
     * Sets the expected border post.
     *
     * @param expectedBorderPost the new expected border post
     */
    public void setExpectedBorderPost(final String expectedBorderPost) {
        this.expectedBorderPost = expectedBorderPost;
    }

    /**
     * Gets the total number of packages.
     *
     * @return the total number of packages
     */
    public String getTotalNumberOfPackages() {
        return totalNumberOfPackages;
    }

    /**
     * Sets the total number of packages.
     *
     * @param totalNumberOfPackages the new total number of packages
     */
    public void setTotalNumberOfPackages(final String totalNumberOfPackages) {
        this.totalNumberOfPackages = totalNumberOfPackages;
    }

    /**
     * Gets the signatory name.
     *
     * @return the signatory name
     */
    public String getSignatoryName() {
        return signatoryName;
    }

    /**
     * Sets the signatory name.
     *
     * @param signatoryName the new signatory name
     */
    public void setSignatoryName(final String signatoryName) {
        this.signatoryName = signatoryName;
    }

    /**
     * Gets the signatory address.
     *
     * @return the signatory address
     */
    public String getSignatoryAddress() {
        return signatoryAddress;
    }

    /**
     * Sets the signatory address.
     *
     * @param signatoryAddress the new signatory address
     */
    public void setSignatoryAddress(final String signatoryAddress) {
        this.signatoryAddress = signatoryAddress;
    }

    /**
     * Gets the signatory position.
     *
     * @return the signatory position
     */
    public String getSignatoryPosition() {
        return signatoryPosition;
    }

    /**
     * Sets the signatory position.
     *
     * @param signatoryPosition the new signatory position
     */
    public void setSignatoryPosition(final String signatoryPosition) {
        this.signatoryPosition = signatoryPosition;
    }

    /**
     * Gets the country of origin.
     *
     * @return the country of origin
     */
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    /**
     * Sets the country of origin.
     *
     * @param countryOfOrigin the new country of origin
     */
    public void setCountryOfOrigin(final String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    /**
     * Gets the zone of origin.
     *
     * @return the zone of origin
     */
    public String getZoneOfOrigin() {
        return zoneOfOrigin;
    }

    /**
     * Sets the zone of origin.
     *
     * @param zoneOfOrigin the new zone of origin
     */
    public void setZoneOfOrigin(final String zoneOfOrigin) {
        this.zoneOfOrigin = zoneOfOrigin;
    }

    /**
     * Gets the country of destination.
     *
     * @return the country of destination
     */
    public String getCountryOfDestination() {
        return countryOfDestination;
    }

    /**
     * Sets the country of destination.
     *
     * @param countryOfDestination the new country of destination
     */
    public void setCountryOfDestination(final String countryOfDestination) {
        this.countryOfDestination = countryOfDestination;
    }

    /**
     * Gets the zone of destination.
     *
     * @return the zone of destination
     */
    public String getZoneOfDestination() {
        return zoneOfDestination;
    }

    /**
     * Sets the zone of destination.
     *
     * @param zoneOfDestination the new zone of destination
     */
    public void setZoneOfDestination(final String zoneOfDestination) {
        this.zoneOfDestination = zoneOfDestination;
    }

    /**
     * Gets the place of origin name.
     *
     * @return the place of origin name
     */
    public String getPlaceOfOriginName() {
        return placeOfOriginName;
    }

    /**
     * Sets the place of origin name.
     *
     * @param placeOfOriginName the new place of origin name
     */
    public void setPlaceOfOriginName(final String placeOfOriginName) {
        this.placeOfOriginName = placeOfOriginName;
    }

    /**
     * Gets the place of origin address.
     *
     * @return the place of origin address
     */
    public String getPlaceOfOriginAddress() {
        return placeOfOriginAddress;
    }

    /**
     * Sets the place of origin address.
     *
     * @param placeOfOriginAddress the new place of origin address
     */
    public void setPlaceOfOriginAddress(final String placeOfOriginAddress) {
        this.placeOfOriginAddress = placeOfOriginAddress;
    }

    public String getPlaceOfLoading() {
        return placeOfLoading;
    }

    public void setPlaceOfLoading(String placeOfLoading) {
        this.placeOfLoading = placeOfLoading;
    }

    /**
     * Gets the place of shipment.
     *
     * @return the place of shipment
     */
    public String getPlaceOfShipment() {
        return placeOfShipment;
    }

    /**
     * Sets the place of shipment.
     *
     * @param placeOfShipment the new place of shipment
     */
    public void setPlaceOfShipment(final String placeOfShipment) {
        this.placeOfShipment = placeOfShipment;
    }

    /**
     * Gets the expedition date.
     *
     * @return the expedition date
     */
    public Date getExpeditionDate() {
        return expeditionDate;
    }

    /**
     * Sets the expedition date.
     *
     * @param expeditionDate the new expedition date
     */
    public void setExpeditionDate(final Date expeditionDate) {
        this.expeditionDate = expeditionDate;
    }

    /**
     * Gets the expedition hour.
     *
     * @return the expedition hour
     */
    public String getExpeditionHour() {
        return expeditionHour;
    }

    /**
     * Sets the expedition hour.
     *
     * @param expeditionHour the new expedition hour
     */
    public void setExpeditionHour(final String expeditionHour) {
        this.expeditionHour = expeditionHour;
    }

    /**
     * Gets the mode of transport.
     *
     * @return the mode of transport
     */
    public String getModeOfTransport() {
        return modeOfTransport;
    }

    public ByteArrayInputStream getQrCode() {
        return qrCode;
    }

    public void setQrCode(ByteArrayInputStream qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * Sets the mode of transport.
     *
     * @param modeOfTransport the new mode of transport
     */
    public void setModeOfTransport(final String modeOfTransport) {
        this.modeOfTransport = modeOfTransport;
    }

    /**
     * Gets the means of transport.
     *
     * @return the means of transport
     */
    public String getMeansOfTransport() {
        return meansOfTransport;
    }

    /**
     * Sets the means of transport.
     *
     * @param meansOfTransport the new means of transport
     */
    public void setMeansOfTransport(final String meansOfTransport) {
        this.meansOfTransport = meansOfTransport;
    }

    /**
     * Gets the transport identification.
     *
     * @return the transport identification
     */
    public String getTransportIdentification() {
        return transportIdentification;
    }

    /**
     * Sets the transport identification.
     *
     * @param transportIdentification the new transport identification
     */
    public void setTransportIdentification(final String transportIdentification) {
        this.transportIdentification = transportIdentification;
    }

    public String getLadingNumberLTA() {
        return ladingNumberLTA;
    }

    public void setLadingNumberLTA(String ladingNumberLTA) {
        this.ladingNumberLTA = ladingNumberLTA;
    }

    public String getCvsDepartureDate() {
        return cvsDepartureDate;
    }

    public void setCvsDepartureDate(String cvsDepartureDate) {
        this.cvsDepartureDate = cvsDepartureDate;
    }

    public String getCvsProductTemperature() {
        return cvsProductTemperature;
    }

    public void setCvsProductTemperature(String cvsProductTemperature) {
        this.cvsProductTemperature = cvsProductTemperature;
    }

    public String getCvsNbPackagedUnit() {
        return cvsNbPackagedUnit;
    }

    public void setCvsNbPackagedUnit(String cvsNbPackagedUnit) {
        this.cvsNbPackagedUnit = cvsNbPackagedUnit;
    }

    public String getCvsPackageNature() {
        return cvsPackageNature;
    }

    public void setCvsPackageNature(String cvsPackageNature) {
        this.cvsPackageNature = cvsPackageNature;
    }

    public String getCvsGoodFor() {
        return cvsGoodFor;
    }

    public void setCvsGoodFor(String cvsGoodFor) {
        this.cvsGoodFor = cvsGoodFor;
    }

    public String getCvsGoodSpecies() {
        return cvsGoodSpecies;
    }

    public void setCvsGoodSpecies(String cvsGoodSpecies) {
        this.cvsGoodSpecies = cvsGoodSpecies;
    }

    public String getCvsGoodNature() {
        return cvsGoodNature;
    }

    public void setCvsGoodNature(String cvsGoodNature) {
        this.cvsGoodNature = cvsGoodNature;
    }

    public String getCvsGoodTreatment() {
        return cvsGoodTreatment;
    }

    public void setCvsGoodTreatment(String cvsGoodTreatment) {
        this.cvsGoodTreatment = cvsGoodTreatment;
    }

    public String getCvsGoodPackageNumber() {
        return cvsGoodPackageNumber;
    }

    public void setCvsGoodPackageNumber(String cvsGoodPackageNumber) {
        this.cvsGoodPackageNumber = cvsGoodPackageNumber;
    }

    public String getCvsGoodPackageApproved() {
        return cvsGoodPackageApproved;
    }

    public void setCvsGoodPackageApproved(String cvsGoodPackageApproved) {
        this.cvsGoodPackageApproved = cvsGoodPackageApproved;
    }

    public String getCvsGoodPackageNetWeight() {
        return cvsGoodPackageNetWeight;
    }

    public void setCvsGoodPackageNetWeight(String cvsGoodPackageNetWeight) {
        this.cvsGoodPackageNetWeight = cvsGoodPackageNetWeight;
    }

    public String getCvsIdContainersSeals() {
        return cvsIdContainersSeals;
    }

    public void setCvsIdContainersSeals(String cvsIdContainersSeals) {
        this.cvsIdContainersSeals = cvsIdContainersSeals;
    }

    public String getCvsPermitCITES() {
        return cvsPermitCITES;
    }

    public void setCvsPermitCITES(String cvsPermitCITES) {
        this.cvsPermitCITES = cvsPermitCITES;
    }

}
