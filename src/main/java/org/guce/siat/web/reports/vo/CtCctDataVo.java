package org.guce.siat.web.reports.vo;

import java.util.Date;
import java.util.List;

/**
 * The Class CtCctDataVo.
 */
public class CtCctDataVo {

    /**
     * The controler list.
     */
    private List<CtCctControllerDataVo> controlerList;

    /**
     * The place.
     */
    private String place;

    /**
     * The date.
     */
    private Date date;

    /**
     * The Hour.
     */
    private String Hour;

    /**
     * The quantity.
     */
    private String quantity;

    /**
     * The container number.
     */
    private String containerNumber;

    /**
     * The lot number.
     */
    private String lotNumber;

    /**
     * The libelle.
     */
    private String libelle;

    /**
     * The norme respect.
     */
    private String normeRespect;

    /**
     * The labelling number.
     */
    private String labellingNumber;

    /**
     * The temperature.
     */
    private String temperature;

    /**
     * The aspect.
     */
    private String aspect;

    /**
     * The origine certificate.
     */
    private String origineCertificate;

    /**
     * The general phytosanitary certificate.
     */
    private String generalPhytosanitaryCertificate;

    /**
     * The special phytosanitary certificate.
     */
    private String specialPhytosanitaryCertificate;

    /**
     * The quality certification.
     */
    private String qualityCertification;

    /**
     * The veterinary health certificate.
     */
    private String veterinaryHealthCertificate;

    /**
     * The health certificate.
     */
    private String healthCertificate;

    /**
     * The conformity certificate.
     */
    private String conformityCertificate;

    /**
     * The other quality certificate.
     */
    private String otherQualityCertificate;

    /**
     * The decision.
     */
    private String decision;

    /**
     * The observation.
     */
    private String observation;

    /**
     * The motif.
     */
    private String motif;

    /**
     * The inspection fee fcfa.
     */
    private String inspectionFeeFcfa;

    /**
     * The processing fee.
     */
    private String processingFee;

    /**
     * The special other fees.
     */
    private String specialOtherFees;

    /**
     * The nature.
     */
    private String nature;

    /**
     * Package quality
     */
    private String packageQuality;

    /**
     * Native country
     */
    private String nativeCountry;

    /**
     * Origin of country
     */
    private String originCountry;

    /**
     * Transport Means
     */
    private String transportMeans;

    /**
     * Bill of loading
     */
    private String billOfLoading;

    /**
     * Controller name
     */
    private String controller;

    /**
     * Sender
     */
    private String sender;

    /**
     * Recipient
     */
    private String recipient;

    /**
     * Production Date
     */
    private Date productionDate;

    /**
     * expirationDate
     */
    private Date expirationDate;

    /**
     * Import Licence Number
     */
    private String importLicenceNumber;

    /**
     * Import Licence Date
     */
    private Date importLicenceDate;

    /**
     * Import licence deliver
     */
    private String importLicenceDeliver;

    /**
     * Respect des normes nationales et internationales
     */
    private String respectNorme;

    /**
     * Source de propagation de la peste
     */
    private String sourcePropagationPeste;

    /**
     * Existence des documents d'auto-controle
     */
    private String autocontrolDocument;

    /**
     * Suivi des équipements d'auto-controle
     */
    private String autocontrolEquipement;

    /**
     * Analyse du processus
     */
    private String processAnalyse;

    /**
     * Existance Casque
     */
    private String casque;

    /**
     * Gants
     */
    private String gants;

    /**
     * Combinaison
     */
    private String combinaison;

    /**
     * Chaussures de sécurités
     */
    private String chaussureSecurite;

    /**
     * Eau
     */
    private String water;

    /**
     * Evacuation des déchet
     */
    private String dechet;

    /**
     * Procédures
     */
    private String procedure;

    /**
     *
     */
    private String controller2;

    private String numeroDecisionPVI;
    private String articleReglemente;
    private String exporterName;
    private String pviDestination;
    private String pviSituationArticle;
    private boolean pviCouvertDoc;
    private String pviReference;
    private String pviNatureArticleInspecte;
    private String pviQuantite;
    private String pviDispositionsPrises;
    
    private Date inspectionStartDate;
    private Date inspectionEndDate;
    private Date signatureDate;
    private String inspector, signatory, signaturePlace;

    /**
     * pulvérisation
     */
    private List<String> typeTraitement;

    private String etatDateDernierTraitement;

    /**
     * Produit utilisé
     */
    private String produitUtilise;

    /**
     * Dosage
     */
    private String dosage;

    private List<String> environnementStockage;

    private String environnementTransport;

    private String conditionClimatique;

    private String mesureProtection;

    private String observations;

    private String signatairePVIPhyto;

    /**
     * Gets the controler list.
     *
     * @return the controler list
     */
    public List<CtCctControllerDataVo> getControlerList() {
        return controlerList;
    }

    /**
     * Sets the controler list.
     *
     * @param controlerList the new controler list
     */
    public void setControlerList(final List<CtCctControllerDataVo> controlerList) {
        this.controlerList = controlerList;
    }

    /**
     * Gets the place.
     *
     * @return the place
     */
    public String getPlace() {
        return place;
    }

    /**
     * Sets the place.
     *
     * @param place the new place
     */
    public void setPlace(final String place) {
        this.place = place;
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
     * Gets the hour.
     *
     * @return the hour
     */
    public String getHour() {
        return Hour;
    }

    /**
     * Sets the hour.
     *
     * @param hour the new hour
     */
    public void setHour(final String hour) {
        Hour = hour;
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(final String quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the container number.
     *
     * @return the container number
     */
    public String getContainerNumber() {
        return containerNumber;
    }

    /**
     * Sets the container number.
     *
     * @param containerNumber the new container number
     */
    public void setContainerNumber(final String containerNumber) {
        this.containerNumber = containerNumber;
    }

    /**
     * Gets the lot number.
     *
     * @return the lot number
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * Sets the lot number.
     *
     * @param lotNumber the new lot number
     */
    public void setLotNumber(final String lotNumber) {
        this.lotNumber = lotNumber;
    }

    /**
     * Gets the libelle.
     *
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Sets the libelle.
     *
     * @param libelle the new libelle
     */
    public void setLibelle(final String libelle) {
        this.libelle = libelle;
    }

    /**
     * Gets the norme respect.
     *
     * @return the norme respect
     */
    public String getNormeRespect() {
        return normeRespect;
    }

    /**
     * Sets the norme respect.
     *
     * @param normeRespect the new norme respect
     */
    public void setNormeRespect(final String normeRespect) {
        this.normeRespect = normeRespect;
    }

    /**
     * Gets the labelling number.
     *
     * @return the labelling number
     */
    public String getLabellingNumber() {
        return labellingNumber;
    }

    /**
     * Sets the labelling number.
     *
     * @param labellingNumber the new labelling number
     */
    public void setLabellingNumber(final String labellingNumber) {
        this.labellingNumber = labellingNumber;
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
     * Gets the aspect.
     *
     * @return the aspect
     */
    public String getAspect() {
        return aspect;
    }

    /**
     * Sets the aspect.
     *
     * @param aspect the new aspect
     */
    public void setAspect(final String aspect) {
        this.aspect = aspect;
    }

    /**
     * Gets the origine certificate.
     *
     * @return the origine certificate
     */
    public String getOrigineCertificate() {
        return origineCertificate;
    }

    /**
     * Sets the origine certificate.
     *
     * @param origineCertificate the new origine certificate
     */
    public void setOrigineCertificate(final String origineCertificate) {
        this.origineCertificate = origineCertificate;
    }

    /**
     * Gets the general phytosanitary certificate.
     *
     * @return the general phytosanitary certificate
     */
    public String getGeneralPhytosanitaryCertificate() {
        return generalPhytosanitaryCertificate;
    }

    /**
     * Sets the general phytosanitary certificate.
     *
     * @param generalPhytosanitaryCertificate the new general phytosanitary
     * certificate
     */
    public void setGeneralPhytosanitaryCertificate(final String generalPhytosanitaryCertificate) {
        this.generalPhytosanitaryCertificate = generalPhytosanitaryCertificate;
    }

    /**
     * Gets the special phytosanitary certificate.
     *
     * @return the special phytosanitary certificate
     */
    public String getSpecialPhytosanitaryCertificate() {
        return specialPhytosanitaryCertificate;
    }

    /**
     * Sets the special phytosanitary certificate.
     *
     * @param specialPhytosanitaryCertificate the new special phytosanitary
     * certificate
     */
    public void setSpecialPhytosanitaryCertificate(final String specialPhytosanitaryCertificate) {
        this.specialPhytosanitaryCertificate = specialPhytosanitaryCertificate;
    }

    /**
     * Gets the quality certification.
     *
     * @return the quality certification
     */
    public String getQualityCertification() {
        return qualityCertification;
    }

    /**
     * Sets the quality certification.
     *
     * @param qualityCertification the new quality certification
     */
    public void setQualityCertification(final String qualityCertification) {
        this.qualityCertification = qualityCertification;
    }

    /**
     * Gets the veterinary health certificate.
     *
     * @return the veterinary health certificate
     */
    public String getVeterinaryHealthCertificate() {
        return veterinaryHealthCertificate;
    }

    /**
     * Sets the veterinary health certificate.
     *
     * @param veterinaryHealthCertificate the new veterinary health certificate
     */
    public void setVeterinaryHealthCertificate(final String veterinaryHealthCertificate) {
        this.veterinaryHealthCertificate = veterinaryHealthCertificate;
    }

    /**
     * Gets the health certificate.
     *
     * @return the health certificate
     */
    public String getHealthCertificate() {
        return healthCertificate;
    }

    /**
     * Sets the health certificate.
     *
     * @param healthCertificate the new health certificate
     */
    public void setHealthCertificate(final String healthCertificate) {
        this.healthCertificate = healthCertificate;
    }

    /**
     * Gets the conformity certificate.
     *
     * @return the conformity certificate
     */
    public String getConformityCertificate() {
        return conformityCertificate;
    }

    /**
     * Sets the conformity certificate.
     *
     * @param conformityCertificate the new conformity certificate
     */
    public void setConformityCertificate(final String conformityCertificate) {
        this.conformityCertificate = conformityCertificate;
    }

    /**
     * Gets the other quality certificate.
     *
     * @return the other quality certificate
     */
    public String getOtherQualityCertificate() {
        return otherQualityCertificate;
    }

    /**
     * Sets the other quality certificate.
     *
     * @param otherQualityCertificate the new other quality certificate
     */
    public void setOtherQualityCertificate(final String otherQualityCertificate) {
        this.otherQualityCertificate = otherQualityCertificate;
    }

    /**
     * Gets the decision.
     *
     * @return the decision
     */
    public String getDecision() {
        return decision;
    }

    /**
     * Sets the decision.
     *
     * @param decision the new decision
     */
    public void setDecision(final String decision) {
        this.decision = decision;
    }

    /**
     * Gets the observation.
     *
     * @return the observation
     */
    public String getObservation() {
        return observation;
    }

    /**
     * Sets the observation.
     *
     * @param observation the new observation
     */
    public void setObservation(final String observation) {
        this.observation = observation;
    }

    /**
     * Gets the motif.
     *
     * @return the motif
     */
    public String getMotif() {
        return motif;
    }

    /**
     * Sets the motif.
     *
     * @param motif the new motif
     */
    public void setMotif(final String motif) {
        this.motif = motif;
    }

    /**
     * Gets the inspection fee fcfa.
     *
     * @return the inspection fee fcfa
     */
    public String getInspectionFeeFcfa() {
        return inspectionFeeFcfa;
    }

    /**
     * Sets the inspection fee fcfa.
     *
     * @param inspectionFeeFcfa the new inspection fee fcfa
     */
    public void setInspectionFeeFcfa(final String inspectionFeeFcfa) {
        this.inspectionFeeFcfa = inspectionFeeFcfa;
    }

    /**
     * Gets the processing fee.
     *
     * @return the processing fee
     */
    public String getProcessingFee() {
        return processingFee;
    }

    /**
     * Sets the processing fee.
     *
     * @param processingFee the new processing fee
     */
    public void setProcessingFee(final String processingFee) {
        this.processingFee = processingFee;
    }

    /**
     * Gets the special other fees.
     *
     * @return the special other fees
     */
    public String getSpecialOtherFees() {
        return specialOtherFees;
    }

    /**
     * Sets the special other fees.
     *
     * @param specialOtherFees the new special other fees
     */
    public void setSpecialOtherFees(final String specialOtherFees) {
        this.specialOtherFees = specialOtherFees;
    }

    /**
     * Gets the nature.
     *
     * @return the nature
     */
    public String getNature() {
        return nature;
    }

    /**
     * Sets the nature.
     *
     * @param nature the new nature
     */
    public void setNature(final String nature) {
        this.nature = nature;
    }

    public String getPackageQuality() {
        return packageQuality;
    }

    public void setPackageQuality(String packageQuality) {
        this.packageQuality = packageQuality;
    }

    public String getNativeCountry() {
        return nativeCountry;
    }

    public void setNativeCountry(String nativeCountry) {
        this.nativeCountry = nativeCountry;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getTransportMeans() {
        return transportMeans;
    }

    public void setTransportMeans(String transportMeans) {
        this.transportMeans = transportMeans;
    }

    public String getBillOfLoading() {
        return billOfLoading;
    }

    public void setBillOfLoading(String billOfLoading) {
        this.billOfLoading = billOfLoading;
    }

    public String getController() {
        if (controller == null || controller.isEmpty()) {
            if (controlerList != null && controlerList.size() > 0) {
                controller = new StringBuilder(controlerList.get(0).getName()).append(" / ").append(controlerList.get(0).getQuality()).append(" / ").append(controlerList.get(0).getService()).toString();
            }
        }
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getImportLicenceNumber() {
        return importLicenceNumber;
    }

    public void setImportLicenceNumber(String importLicenceNumber) {
        this.importLicenceNumber = importLicenceNumber;
    }

    public Date getImportLicenceDate() {
        return importLicenceDate;
    }

    public void setImportLicenceDate(Date importLicenceDate) {
        this.importLicenceDate = importLicenceDate;
    }

    public String getImportLicenceDeliver() {
        return importLicenceDeliver;
    }

    public void setImportLicenceDeliver(String importLicenceDeliver) {
        this.importLicenceDeliver = importLicenceDeliver;
    }

    public String getRespectNorme() {
        return respectNorme;
    }

    public void setRespectNorme(String respectNorme) {
        this.respectNorme = respectNorme;
    }

    public String getSourcePropagationPeste() {
        return sourcePropagationPeste;
    }

    public void setSourcePropagationPeste(String sourcePropagationPeste) {
        this.sourcePropagationPeste = sourcePropagationPeste;
    }

    public String getAutocontrolDocument() {
        return autocontrolDocument;
    }

    public void setAutocontrolDocument(String autocontrolDocument) {
        this.autocontrolDocument = autocontrolDocument;
    }

    public String getAutocontrolEquipement() {
        return autocontrolEquipement;
    }

    public void setAutocontrolEquipement(String autocontrolEquipement) {
        this.autocontrolEquipement = autocontrolEquipement;
    }

    public String getProcessAnalyse() {
        return processAnalyse;
    }

    public void setProcessAnalyse(String processAnalyse) {
        this.processAnalyse = processAnalyse;
    }

    public String getCasque() {
        return casque;
    }

    public void setCasque(String casque) {
        this.casque = casque;
    }

    public String getGants() {
        return gants;
    }

    public void setGants(String gants) {
        this.gants = gants;
    }

    public String getCombinaison() {
        return combinaison;
    }

    public void setCombinaison(String combinaison) {
        this.combinaison = combinaison;
    }

    public String getChaussureSecurite() {
        return chaussureSecurite;
    }

    public void setChaussureSecurite(String chaussureSecurite) {
        this.chaussureSecurite = chaussureSecurite;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getDechet() {
        return dechet;
    }

    public void setDechet(String dechet) {
        this.dechet = dechet;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getController2() {
        if (controller2 == null || controller2.isEmpty()) {
            if (controlerList != null && controlerList.size() > 1) {
                controller2 = new StringBuilder(controlerList.get(1).getName()).append(" / ").append(controlerList.get(1).getQuality()).append(" / ").append(controlerList.get(1).getService()).toString();
            }
        }
        return controller2;
    }

    public void setController2(String controller2) {
        this.controller2 = controller2;
    }

    public List<String> getTypeTraitement() {
        return typeTraitement;
    }

    public void setTypeTraitement(List<String> typeTraitement) {
        this.typeTraitement = typeTraitement;
    }

    public String getEtatDateDernierTraitement() {
        return etatDateDernierTraitement;
    }

    public void setEtatDateDernierTraitement(String etatDateDernierTraitement) {
        this.etatDateDernierTraitement = etatDateDernierTraitement;
    }

    public String getProduitUtilise() {
        return produitUtilise;
    }

    public void setProduitUtilise(String produitUtilise) {
        this.produitUtilise = produitUtilise;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public List<String> getEnvironnementStockage() {
        return environnementStockage;
    }

    public void setEnvironnementStockage(List<String> environnementStockage) {
        this.environnementStockage = environnementStockage;
    }

    public String getEnvironnementTransport() {
        return environnementTransport;
    }

    public void setEnvironnementTransport(String environnementTransport) {
        this.environnementTransport = environnementTransport;
    }

    public String getConditionClimatique() {
        return conditionClimatique;
    }

    public void setConditionClimatique(String conditionClimatique) {
        this.conditionClimatique = conditionClimatique;
    }

    public String getMesureProtection() {
        return mesureProtection;
    }

    public void setMesureProtection(String mesureProtection) {
        this.mesureProtection = mesureProtection;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getSignatairePVIPhyto() {
        return signatairePVIPhyto;
    }

    public void setSignatairePVIPhyto(String signatairePVIPhyto) {
        this.signatairePVIPhyto = signatairePVIPhyto;
    }

    public String getNumeroDecisionPVI() {
        return numeroDecisionPVI;
    }

    public void setNumeroDecisionPVI(String numeroDecisionPVI) {
        this.numeroDecisionPVI = numeroDecisionPVI;
    }

    public String getArticleReglemente() {
        return articleReglemente;
    }

    public void setArticleReglemente(String articleReglemente) {
        this.articleReglemente = articleReglemente;
    }

    public String getExporterName() {
        return exporterName;
    }

    public void setExporterName(String exporterName) {
        this.exporterName = exporterName;
    }

    public String getPviDestination() {
        return pviDestination;
    }

    public void setPviDestination(String pviDestination) {
        this.pviDestination = pviDestination;
    }

    public String getPviSituationArticle() {
        return pviSituationArticle;
    }

    public void setPviSituationArticle(String pviSituationArticle) {
        this.pviSituationArticle = pviSituationArticle;
    }

    public boolean isPviCouvertDoc() {
        return pviCouvertDoc;
    }

    public void setPviCouvertDoc(boolean pviCouvertDoc) {
        this.pviCouvertDoc = pviCouvertDoc;
    }

    public String getPviReference() {
        return pviReference;
    }

    public void setPviReference(String pviReference) {
        this.pviReference = pviReference;
    }

    public String getPviNatureArticleInspecte() {
        return pviNatureArticleInspecte;
    }

    public void setPviNatureArticleInspecte(String pviNatureArticleInspecte) {
        this.pviNatureArticleInspecte = pviNatureArticleInspecte;
    }

    public String getPviQuantite() {
        return pviQuantite;
    }

    public void setPviQuantite(String pviQuantite) {
        this.pviQuantite = pviQuantite;
    }

    public String getPviDispositionsPrises() {
        return pviDispositionsPrises;
    }

    public void setPviDispositionsPrises(String pviDispositionsPrises) {
        this.pviDispositionsPrises = pviDispositionsPrises;
    }

    public Date getInspectionStartDate() {
        return inspectionStartDate;
    }

    public void setInspectionStartDate(Date inspectionStartDate) {
        this.inspectionStartDate = inspectionStartDate;
    }

    public Date getInspectionEndDate() {
        return inspectionEndDate;
    }

    public void setInspectionEndDate(Date inspectionEndDate) {
        this.inspectionEndDate = inspectionEndDate;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getSignatory() {
        return signatory;
    }

    public void setSignatory(String signatory) {
        this.signatory = signatory;
    }

    public Date getSignatureDate() {
        return signatureDate;
    }

    public void setSignatureDate(Date signatureDate) {
        this.signatureDate = signatureDate;
    }

    public String getSignaturePlace() {
        return signaturePlace;
    }

    public void setSignaturePlace(String signaturePlace) {
        this.signaturePlace = signaturePlace;
    }
    
   
}

