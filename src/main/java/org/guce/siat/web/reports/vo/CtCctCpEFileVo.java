package org.guce.siat.web.reports.vo;

import java.util.Date;

/**
 * The Class CtCctCpEFileVo.
 */
public class CtCctCpEFileVo extends AbstractFileVo<CtCctCpEFileItemVo> {

    private String exporterName;

    private String exporterAddress;

    private String exporter;

    private String exporterCountry;

    private String exporterTown;

    private String exporterPoBox;

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

    private String consignee;

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

    private String consigneeTown;

    private String consigneePoBox;

    /**
     * The means of transport.
     */
    private String meansOfTransport;

    /**
     * The point of entry.
     */
    private String pointOfEntry;

    /**
     * The local plant protection organisation.
     */
    private String localPlantProtectionOrganisation;

    /**
     * The foreign plant protection organisation.
     */
    private String foreignPlantProtectionOrganisation;

    /**
     * The origin.
     */
    private String origin;

    /**
     * The additional declaration.
     */
    private String additionalDeclaration;

    /**
     * The type of treatment.
     */
    private String typeOfTreatment;

    /**
     * The duration.
     */
    private String duration;

    /**
     * The temperature.
     */
    private String temperature;

    private boolean fumigation;
    private boolean disenfection;
    private String chemicalProduct;
    private String additionalInfos;
    private String lotsCount;
    private String packaging;
    private String names;
    private String quantities;

    private boolean transit;

    /**
     * The chemical product active matter.
     */
    private String chemicalProductActiveMatter;

    /**
     * The concentration.
     */
    private String concentration;

    /**
     * The date.
     */
    private Date date;

    /**
     * The additional information.
     */
    private String additionalInformation;

    /**
     * The signatory name.
     */
    private String signatoryName;

    /**
     * The phytosanitary certificate number.
     */
    private String phytosanitaryCertificateNumber;

    /**
     * The copy or original.
     */
    private String copyOrOriginal;

    /**
     * The packaging type.
     */
    private String packagingType;

    /**
     * The based on.
     */
    private String basedOn;

    /**
     * The packaged or repackaged.
     */
    private String packagedOrRepackaged;

    private String treatmentsCarriedOut;

    private String quantity;
    private String packagingMark;
    private String numberOfPackages;
    private String packageNature;
    private String productName;
    private String botanicalNameOfPlants;

    private String destination;
    private String deliveryPlace;

    private String commodities;
    private String containersNumbers;
    private String netWeight;
    private String grossWeight;
    private String lotsNumbers;

    private Date treatmentDate;

    private String namesAnnex;
    private String lotsNumbersAnnex;
    private String containersNumbersAnnex;

    private String userAddInfos;

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
     * Gets the means of transport.
     *
     * @return the means of transport
     */
    public String getMeansOfTransport() {
        return meansOfTransport;
    }

    public String getConsigneeTown() {
        return consigneeTown;
    }

    public void setConsigneeTown(String consigneeTown) {
        this.consigneeTown = consigneeTown;
    }

    public String getConsigneePoBox() {
        return consigneePoBox;
    }

    public void setConsigneePoBox(String consigneePoBox) {
        this.consigneePoBox = consigneePoBox;
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
     * Gets the point of entry.
     *
     * @return the point of entry
     */
    public String getPointOfEntry() {
        return pointOfEntry;
    }

    /**
     * Sets the point of entry.
     *
     * @param pointOfEntry the new point of entry
     */
    public void setPointOfEntry(final String pointOfEntry) {
        this.pointOfEntry = pointOfEntry;
    }

    /**
     * Gets the local plant protection organisation.
     *
     * @return the local plant protection organisation
     */
    public String getLocalPlantProtectionOrganisation() {
        return localPlantProtectionOrganisation;
    }

    /**
     * Sets the local plant protection organisation.
     *
     * @param localPlantProtectionOrganisation the new local plant protection
     * organisation
     */
    public void setLocalPlantProtectionOrganisation(final String localPlantProtectionOrganisation) {
        this.localPlantProtectionOrganisation = localPlantProtectionOrganisation;
    }

    /**
     * Gets the foreign plant protection organisation.
     *
     * @return the foreign plant protection organisation
     */
    public String getForeignPlantProtectionOrganisation() {
        return foreignPlantProtectionOrganisation;
    }

    /**
     * Sets the foreign plant protection organisation.
     *
     * @param foreignPlantProtectionOrganisation the new foreign plant
     * protection organisation
     */
    public void setForeignPlantProtectionOrganisation(final String foreignPlantProtectionOrganisation) {
        this.foreignPlantProtectionOrganisation = foreignPlantProtectionOrganisation;
    }

    /**
     * Gets the origin.
     *
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the origin.
     *
     * @param origin the new origin
     */
    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    /**
     * Gets the additional declaration.
     *
     * @return the additional declaration
     */
    public String getAdditionalDeclaration() {
        return additionalDeclaration;
    }

    /**
     * Sets the additional declaration.
     *
     * @param additionalDeclaration the new additional declaration
     */
    public void setAdditionalDeclaration(final String additionalDeclaration) {
        this.additionalDeclaration = additionalDeclaration;
    }

    /**
     * Gets the type of treatment.
     *
     * @return the type of treatment
     */
    public String getTypeOfTreatment() {
        return typeOfTreatment;
    }

    /**
     * Sets the type of treatment.
     *
     * @param typeOfTreatment the new type of treatment
     */
    public void setTypeOfTreatment(final String typeOfTreatment) {
        this.typeOfTreatment = typeOfTreatment;
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Sets the duration.
     *
     * @param duration the new duration
     */
    public void setDuration(final String duration) {
        this.duration = duration;
    }

    /**
     * Gets the temperature.
     *
     * @return the temperature
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature.
     *
     * @param temperature the new temperature
     */
    public void setTemperature(final String temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets the chemical product active matter.
     *
     * @return the chemical product active matter
     */
    public String getChemicalProductActiveMatter() {
        return chemicalProductActiveMatter;
    }

    /**
     * Sets the chemical product active matter.
     *
     * @param chemicalProductActiveMatter the new chemical product active matter
     */
    public void setChemicalProductActiveMatter(final String chemicalProductActiveMatter) {
        this.chemicalProductActiveMatter = chemicalProductActiveMatter;
    }

    /**
     * Gets the concentration.
     *
     * @return the concentration
     */
    public String getConcentration() {
        return concentration;
    }

    /**
     * Sets the concentration.
     *
     * @param concentration the new concentration
     */
    public void setConcentration(final String concentration) {
        this.concentration = concentration;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(final Date date) {
        this.date = date;
    }

    /**
     * Gets the additional information.
     *
     * @return the additional information
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Sets the additional information.
     *
     * @param additionalInformation the new additional information
     */
    public void setAdditionalInformation(final String additionalInformation) {
        this.additionalInformation = additionalInformation;
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
     * Gets the phytosanitary certificate number.
     *
     * @return the phytosanitary certificate number
     */
    public String getPhytosanitaryCertificateNumber() {
        return phytosanitaryCertificateNumber;
    }

    /**
     * Sets the phytosanitary certificate number.
     *
     * @param phytosanitaryCertificateNumber the new phytosanitary certificate
     * number
     */
    public void setPhytosanitaryCertificateNumber(final String phytosanitaryCertificateNumber) {
        this.phytosanitaryCertificateNumber = phytosanitaryCertificateNumber;
    }

    /**
     * Gets the copy or original.
     *
     * @return the copy or original
     */
    public String getCopyOrOriginal() {
        return copyOrOriginal;
    }

    /**
     * Sets the copy or original.
     *
     * @param copyOrOriginal the new copy or original
     */
    public void setCopyOrOriginal(final String copyOrOriginal) {
        this.copyOrOriginal = copyOrOriginal;
    }

    /**
     * Gets the packaging type.
     *
     * @return the packaging type
     */
    public String getPackagingType() {
        return packagingType;
    }

    /**
     * Sets the packaging type.
     *
     * @param packagingType the new packaging type
     */
    public void setPackagingType(final String packagingType) {
        this.packagingType = packagingType;
    }

    /**
     * Gets the based on.
     *
     * @return the based on
     */
    public String getBasedOn() {
        return basedOn;
    }

    /**
     * Sets the based on.
     *
     * @param basedOn the new based on
     */
    public void setBasedOn(final String basedOn) {
        this.basedOn = basedOn;
    }

    /**
     * Gets the packaged or repackaged.
     *
     * @return the packaged or repackaged
     */
    public String getPackagedOrRepackaged() {
        return packagedOrRepackaged;
    }

    /**
     * Sets the packaged or repackaged.
     *
     * @param packagedOrRepackaged the new packaged or repackaged
     */
    public void setPackagedOrRepackaged(final String packagedOrRepackaged) {
        this.packagedOrRepackaged = packagedOrRepackaged;
    }

    public String getPackagingMark() {
        return packagingMark;
    }

    public void setPackagingMark(String packagingMark) {
        this.packagingMark = packagingMark;
    }

    public String getNumberOfPackages() {
        return numberOfPackages;
    }

    public void setNumberOfPackages(String numberOfPackages) {
        this.numberOfPackages = numberOfPackages;
    }

    public String getPackageNature() {
        return packageNature;
    }

    public void setPackageNature(String packageNature) {
        this.packageNature = packageNature;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBotanicalNameOfPlants() {
        return botanicalNameOfPlants;
    }

    public void setBotanicalNameOfPlants(String botanicalNameOfPlants) {
        this.botanicalNameOfPlants = botanicalNameOfPlants;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isFumigation() {
        return fumigation;
    }

    public void setFumigation(boolean fumigation) {
        this.fumigation = fumigation;
    }

    public boolean isDisenfection() {
        return disenfection;
    }

    public void setDisenfection(boolean disenfection) {
        this.disenfection = disenfection;
    }

    public String getChemicalProduct() {
        return chemicalProduct;
    }

    public void setChemicalProduct(String chemicalProduct) {
        this.chemicalProduct = chemicalProduct;
    }

    public String getAdditionalInfos() {
        return additionalInfos;
    }

    public void setAdditionalInfos(String additionalInfos) {
        this.additionalInfos = additionalInfos;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public String getCommodities() {
        return commodities;
    }

    public void setCommodities(String commodities) {
        this.commodities = commodities;
    }

    public String getContainersNumbers() {
        return containersNumbers;
    }

    public void setContainersNumbers(String containersNumbers) {
        this.containersNumbers = containersNumbers;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getLotsNumbers() {
        return lotsNumbers;
    }

    public void setLotsNumbers(String lotsNumbers) {
        this.lotsNumbers = lotsNumbers;
    }

    public String getTreatmentsCarriedOut() {
        return treatmentsCarriedOut;
    }

    public void setTreatmentsCarriedOut(String treatmentsCarriedOut) {
        this.treatmentsCarriedOut = treatmentsCarriedOut;
    }

    public String getLotsCount() {
        return lotsCount;
    }

    public void setLotsCount(String lotsCount) {
        this.lotsCount = lotsCount;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
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

    public boolean isTransit() {
        return transit;
    }

    public void setTransit(boolean transit) {
        this.transit = transit;
    }

    public String getQuantities() {
        return quantities;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }

    public Date getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(Date treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public String getNamesAnnex() {
        return namesAnnex;
    }

    public void setNamesAnnex(String namesAnnex) {
        this.namesAnnex = namesAnnex;
    }

    public String getLotsNumbersAnnex() {
        return lotsNumbersAnnex;
    }

    public void setLotsNumbersAnnex(String lotsNumbersAnnex) {
        this.lotsNumbersAnnex = lotsNumbersAnnex;
    }

    public String getContainersNumbersAnnex() {
        return containersNumbersAnnex;
    }

    public void setContainersNumbersAnnex(String containersNumbersAnnex) {
        this.containersNumbersAnnex = containersNumbersAnnex;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getExporter() {
        return exporter;
    }

    public void setExporter(String exporter) {
        this.exporter = exporter;
    }

    public String getExporterCountry() {
        return exporterCountry;
    }

    public void setExporterCountry(String exporterCountry) {
        this.exporterCountry = exporterCountry;
    }

    public String getExporterTown() {
        return exporterTown;
    }

    public void setExporterTown(String exporterTown) {
        this.exporterTown = exporterTown;
    }

    public String getExporterPoBox() {
        return exporterPoBox;
    }

    public void setExporterPoBox(String exporterPoBox) {
        this.exporterPoBox = exporterPoBox;
    }

    public String getUserAddInfos() {
        return userAddInfos;
    }

    public void setUserAddInfos(String userAddInfos) {
        this.userAddInfos = userAddInfos;
    }

}
