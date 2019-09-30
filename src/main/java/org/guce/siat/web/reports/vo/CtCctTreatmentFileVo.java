package org.guce.siat.web.reports.vo;

import java.util.Date;
import java.util.List;

/**
 *
 * @author tadzotsa
 */
public class CtCctTreatmentFileVo {

    /**
     * TA
     */
    private String decisionNumber;
    private String exporterName;
    private String exporterBp;
    private String exporterTel;
    private String exporterFax;
    private String exporterEmail;
    private String supervisor;
    private String treatmentType;
    private String homologationNumber;
    private String concentration;
    private String treatmentDose;
    private String consigneeName;
    private String fileNumber;
    private String decisionPlace;
    private Date decisionDate;
    private Date treatmentDate;
    private List<CtCctTreatmentFileItemVo> fileItemList;

    /**
     * TSS
     */
    private String activeIngredient;
    private String exporterAddress;
    private String treatmentMode;
    private String otherTreatmentMode;
    private String tssConcentration;
    private String applicationDose;
    private String consigneeCountry;
    private String itemNature;
    private String itemQuantity;
    private String productUsed;
    private String otherProductUsed;
    private String productComName;
    private String antidote;
    private Date treatmentTime;
    private String treatmentDuration;
    private String treatmentEnvironment;
    private String otherTreatmentEnvironment;
    private String optimalTemperature;
    private String storagePlace;
    private String otherStoragePlace;
    private boolean sanitaryState;
    private Date uncoveringDate;
    private String conditioning;
    private String weatherCondition;
    private String otherWeatherCondition;
    private boolean treatmentPrevention;
    private boolean residentsInformations;
    private String treatmentCompanyName;
    private String treatmentCompanyAddress;
    private String treatmentCompanyBp;
    private String treatmentCompanyTel;
    private String treatmentCompanyFax;
    private String treatmentCompanyEmail;
    private String staffSecurityNature;
    private String protectionEquipements;
    private boolean preventionPlaquePresent;
    private String generalObservations;
	private boolean productUsedFungicide;
	private boolean productUsedInsecticide;
	private boolean productUsedInsecticideFungicide;
	private boolean treatmentModeFumigation;
	private boolean treatmentModePulverisation;
	private boolean treatmentModeSoaking;
	private boolean treatmentModeHeat;
        private String inspector;
    
        private String signatory;

    public String getDecisionNumber() {
        return decisionNumber;
    }

    public void setDecisionNumber(String decisionNumber) {
        this.decisionNumber = decisionNumber;
    }

    public String getExporterName() {
        return exporterName;
    }

    public void setExporterName(String exporterName) {
        this.exporterName = exporterName;
    }

    public String getExporterBp() {
        return exporterBp;
    }

    public void setExporterBp(String exporterBp) {
        this.exporterBp = exporterBp;
    }

    public String getExporterTel() {
        return exporterTel;
    }

    public void setExporterTel(String exporterTel) {
        this.exporterTel = exporterTel;
    }

    public String getExporterFax() {
        return exporterFax;
    }

    public void setExporterFax(String exporterFax) {
        this.exporterFax = exporterFax;
    }

    public String getExporterEmail() {
        return exporterEmail;
    }

    public void setExporterEmail(String exporterEmail) {
        this.exporterEmail = exporterEmail;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public String getHomologationNumber() {
        return homologationNumber;
    }

    public void setHomologationNumber(String homologationNumber) {
        this.homologationNumber = homologationNumber;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getTreatmentDose() {
        return treatmentDose;
    }

    public void setTreatmentDose(String treatmentDose) {
        this.treatmentDose = treatmentDose;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getDecisionPlace() {
        return decisionPlace;
    }

    public void setDecisionPlace(String decisionPlace) {
        this.decisionPlace = decisionPlace;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public List<CtCctTreatmentFileItemVo> getFileItemList() {
        return fileItemList;
    }

    public void setFileItemList(List<CtCctTreatmentFileItemVo> fileItemList) {
        this.fileItemList = fileItemList;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public String getExporterAddress() {
        return exporterAddress;
    }

    public void setExporterAddress(String exporterAddress) {
        this.exporterAddress = exporterAddress;
    }

    public String getTreatmentMode() {
        return treatmentMode;
    }

    public void setTreatmentMode(String treatmentMode) {
        this.treatmentMode = treatmentMode;
    }

    public String getOtherTreatmentMode() {
        return otherTreatmentMode;
    }

    public void setOtherTreatmentMode(String otherTreatmentMode) {
        this.otherTreatmentMode = otherTreatmentMode;
    }

    public String getTssConcentration() {
        return tssConcentration;
    }

    public void setTssConcentration(String tssConcentration) {
        this.tssConcentration = tssConcentration;
    }

    public String getApplicationDose() {
        return applicationDose;
    }

    public void setApplicationDose(String applicationDose) {
        this.applicationDose = applicationDose;
    }

    public String getConsigneeCountry() {
        return consigneeCountry;
    }

    public void setConsigneeCountry(String consigneeCountry) {
        this.consigneeCountry = consigneeCountry;
    }

    public String getItemNature() {
        return itemNature;
    }

    public void setItemNature(String itemNature) {
        this.itemNature = itemNature;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getProductUsed() {
        return productUsed;
    }

    public void setProductUsed(String productUsed) {
        this.productUsed = productUsed;
    }

    public String getOtherProductUsed() {
        return otherProductUsed;
    }

    public void setOtherProductUsed(String otherProductUsed) {
        this.otherProductUsed = otherProductUsed;
    }

    public String getProductComName() {
        return productComName;
    }

    public void setProductComName(String productComName) {
        this.productComName = productComName;
    }

    public String getAntidote() {
        return antidote;
    }

    public void setAntidote(String antidote) {
        this.antidote = antidote;
    }

    public Date getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(Date treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getTreatmentDuration() {
        return treatmentDuration;
    }

    public void setTreatmentDuration(String treatmentDuration) {
        this.treatmentDuration = treatmentDuration;
    }

    public String getTreatmentEnvironment() {
        return treatmentEnvironment;
    }

    public void setTreatmentEnvironment(String treatmentEnvironment) {
        this.treatmentEnvironment = treatmentEnvironment;
    }

    public String getOtherTreatmentEnvironment() {
        return otherTreatmentEnvironment;
    }

    public void setOtherTreatmentEnvironment(String otherTreatmentEnvironment) {
        this.otherTreatmentEnvironment = otherTreatmentEnvironment;
    }

    public String getOptimalTemperature() {
        return optimalTemperature;
    }

    public void setOptimalTemperature(String optimalTemperature) {
        this.optimalTemperature = optimalTemperature;
    }

    public String getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(String storagePlace) {
        this.storagePlace = storagePlace;
    }

    public String getOtherStoragePlace() {
        return otherStoragePlace;
    }

    public void setOtherStoragePlace(String otherStoragePlace) {
        this.otherStoragePlace = otherStoragePlace;
    }

    public boolean isSanitaryState() {
        return sanitaryState;
    }

    public void setSanitaryState(boolean sanitaryState) {
        this.sanitaryState = sanitaryState;
    }

    public Date getUncoveringDate() {
        return uncoveringDate;
    }

    public void setUncoveringDate(Date uncoveringDate) {
        this.uncoveringDate = uncoveringDate;
    }

    public String getConditioning() {
        return conditioning;
    }

    public void setConditioning(String conditioning) {
        this.conditioning = conditioning;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getOtherWeatherCondition() {
        return otherWeatherCondition;
    }

    public void setOtherWeatherCondition(String otherWeatherCondition) {
        this.otherWeatherCondition = otherWeatherCondition;
    }

    public boolean isTreatmentPrevention() {
        return treatmentPrevention;
    }

    public void setTreatmentPrevention(boolean treatmentPrevention) {
        this.treatmentPrevention = treatmentPrevention;
    }

    public boolean isResidentsInformations() {
        return residentsInformations;
    }

    public void setResidentsInformations(boolean residentsInformations) {
        this.residentsInformations = residentsInformations;
    }

    public String getTreatmentCompanyName() {
        return treatmentCompanyName;
    }

    public void setTreatmentCompanyName(String treatmentCompanyName) {
        this.treatmentCompanyName = treatmentCompanyName;
    }

    public String getTreatmentCompanyAddress() {
        return treatmentCompanyAddress;
    }

    public void setTreatmentCompanyAddress(String treatmentCompanyAddress) {
        this.treatmentCompanyAddress = treatmentCompanyAddress;
    }

    public String getStaffSecurityNature() {
        return staffSecurityNature;
    }

    public void setStaffSecurityNature(String staffSecurityNature) {
        this.staffSecurityNature = staffSecurityNature;
    }

    public String getProtectionEquipements() {
        return protectionEquipements;
    }

    public void setProtectionEquipements(String protectionEquipements) {
        this.protectionEquipements = protectionEquipements;
    }

    public boolean isPreventionPlaquePresent() {
        return preventionPlaquePresent;
    }

    public void setPreventionPlaquePresent(boolean preventionPlaquePresent) {
        this.preventionPlaquePresent = preventionPlaquePresent;
    }

    public String getGeneralObservations() {
        return generalObservations;
    }

    public void setGeneralObservations(String generalObservations) {
        this.generalObservations = generalObservations;
    }

    public String getTreatmentCompanyTel() {
        return treatmentCompanyTel;
    }

    public void setTreatmentCompanyTel(String treatmentCompanyTel) {
        this.treatmentCompanyTel = treatmentCompanyTel;
    }

    public String getTreatmentCompanyFax() {
        return treatmentCompanyFax;
    }

    public void setTreatmentCompanyFax(String treatmentCompanyFax) {
        this.treatmentCompanyFax = treatmentCompanyFax;
    }

    public String getTreatmentCompanyEmail() {
        return treatmentCompanyEmail;
    }

    public void setTreatmentCompanyEmail(String treatmentCompanyEmail) {
        this.treatmentCompanyEmail = treatmentCompanyEmail;
    }

    public String getTreatmentCompanyBp() {
        return treatmentCompanyBp;
    }

    public void setTreatmentCompanyBp(String treatmentCompanyBp) {
        this.treatmentCompanyBp = treatmentCompanyBp;
    }

	public boolean isProductUsedFungicide() {
		return productUsedFungicide;
	}

	public void setProductUsedFungicide(boolean productUsedFungicide) {
		this.productUsedFungicide = productUsedFungicide;
	}

	public boolean isProductUsedInsecticide() {
		return productUsedInsecticide;
	}

	public void setProductUsedInsecticide(boolean productUsedInsecticide) {
		this.productUsedInsecticide = productUsedInsecticide;
	}

	public boolean isProductUsedInsecticideFungicide() {
		return productUsedInsecticideFungicide;
	}

	public void setProductUsedInsecticideFungicide(boolean productUsedInsecticideFungicide) {
		this.productUsedInsecticideFungicide = productUsedInsecticideFungicide;
	}

	public boolean isTreatmentModeFumigation() {
		return treatmentModeFumigation;
	}

	public void setTreatmentModeFumigation(boolean treatmentModeFumigation) {
		this.treatmentModeFumigation = treatmentModeFumigation;
	}

	public boolean isTreatmentModePulverisation() {
		return treatmentModePulverisation;
	}

	public void setTreatmentModePulverisation(boolean treatmentModePulverisation) {
		this.treatmentModePulverisation = treatmentModePulverisation;
	}

	public boolean isTreatmentModeSoaking() {
		return treatmentModeSoaking;
	}

	public void setTreatmentModeSoaking(boolean treatmentModeSoaking) {
		this.treatmentModeSoaking = treatmentModeSoaking;
	}

	public boolean isTreatmentModeHeat() {
		return treatmentModeHeat;
	}

	public void setTreatmentModeHeat(boolean treatmentModeHeat) {
		this.treatmentModeHeat = treatmentModeHeat;
	}	

    public String getSignatory() {
        return signatory;
    }

    public void setSignatory(String signatory) {
        this.signatory = signatory;
    }

    public Date getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(Date treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }
     

    /**
     *
     */
}
