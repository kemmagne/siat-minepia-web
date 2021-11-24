package org.guce.siat.web.reports.vo;

import java.io.InputStream;

/**
 * The Class CcsMinsanteFileVo.
 */
public class CcsMinsanteFileVo extends AbstractFileVo<CcsMinsanteFileItemVo> {


    /**
     * The country of provenance.
     */
    private String provenance;

    /**
     * The provider.
     */
    private String provider;

    /**
     * The provider address.
     */
    private String providerAddress;

    /**
     * The importer.
     */
    private String importer;

    /**
     * The importer address.
     */
    private String importerAddress;

    /**
     * The ship.
     */
    private String ship;

    /**
     * The signature Date.
     */
    private String signatureDate;

    private String productType1;

    private String productType2;

    private String productType3;

    private String productType4;

    private String productType5;

    private String diNumber;

    private String blNumber;

    private String lotsNumber;

    private String packaging;

    private String numberContainers20;

    private String numberContainers40;

    private String controller;

    private String docConformeA;

    private String observationDocConformeA;

    private String docConformeAmmAmc;

    private String observationDocConformeAmmAmc;
    
    private String docConformeAi;
    
    private String observationDocConformeAi;
    
    private String docConformeAtq;
    
    private String observationDocConformeAtq;
    
    private String docConformeCc;
    
    private String observationDocConformeCc;
    
    private String docConformeCfCd;
    
    private String observationDocConformeCfCd;
    
    private String docConformeCapcm;
    
    private String observationDocConformeCapcm;

    private String docConformeCe;
    
    private String observationDocConformeCe;
    
    private String docConformeAmm;
    
    private String observationDocConformeAmm;
    
    private String docConformeAoi;
    
    private String observationDocConformeAoi;
    
    private String docConformeVt;

    private String observationDocConformeVt;
    
    private String docConformeCbpsd;
    
    private String observationDocConformeCbpsd;
    
    private String containersNumbers;
    
    private Boolean hasContainers;

    private InputStream qrCode;
    
    private InputStream controllerStamp;
    
    private InputStream controllerSignature;
    
    private InputStream signatoryStamp;
    
    private InputStream signatorySignature;

    public CcsMinsanteFileVo() {
    }
    

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }

    public String getImporterAddress() {
        return importerAddress;
    }

    public void setImporterAddress(String importerAddress) {
        this.importerAddress = importerAddress;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public String getSignatureDate() {
        return signatureDate;
    }

    public void setSignatureDate(String signatureDate) {
        this.signatureDate = signatureDate;
    }

    public String getProductType1() {
        return productType1;
    }

    public void setProductType1(String productType1) {
        this.productType1 = productType1;
    }

    public String getProductType2() {
        return productType2;
    }

    public void setProductType2(String productType2) {
        this.productType2 = productType2;
    }

    public String getProductType3() {
        return productType3;
    }

    public void setProductType3(String productType3) {
        this.productType3 = productType3;
    }

    public String getProductType4() {
        return productType4;
    }

    public void setProductType4(String productType4) {
        this.productType4 = productType4;
    }

    public String getProductType5() {
        return productType5;
    }

    public void setProductType5(String productType5) {
        this.productType5 = productType5;
    }

    public String getDiNumber() {
        return diNumber;
    }

    public void setDiNumber(String diNumber) {
        this.diNumber = diNumber;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getLotsNumber() {
        return lotsNumber;
    }

    public void setLotsNumber(String lotsNumber) {
        this.lotsNumber = lotsNumber;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getNumberContainers20() {
        return numberContainers20;
    }

    public void setNumberContainers20(String numberContainers20) {
        this.numberContainers20 = numberContainers20;
    }

    public String getNumberContainers40() {
        return numberContainers40;
    }

    public void setNumberContainers40(String numberContainers40) {
        this.numberContainers40 = numberContainers40;
    }


    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getDocConformeA() {
        return docConformeA;
    }

    public void setDocConformeA(String docConformeA) {
        this.docConformeA = docConformeA;
    }

    public String getObservationDocConformeA() {
        return observationDocConformeA;
    }

    public void setObservationDocConformeA(String observationDocConformeA) {
        this.observationDocConformeA = observationDocConformeA;
    }

    public String getDocConformeAmmAmc() {
        return docConformeAmmAmc;
    }

    public void setDocConformeAmmAmc(String docConformeAmmAmc) {
        this.docConformeAmmAmc = docConformeAmmAmc;
    }

    public String getObservationDocConformeAmmAmc() {
        return observationDocConformeAmmAmc;
    }

    public void setObservationDocConformeAmmAmc(String observationDocConformeAmmAmc) {
        this.observationDocConformeAmmAmc = observationDocConformeAmmAmc;
    }

    public String getDocConformeAi() {
        return docConformeAi;
    }

    public void setDocConformeAi(String docConformeAi) {
        this.docConformeAi = docConformeAi;
    }

    public String getObservationDocConformeAi() {
        return observationDocConformeAi;
    }

    public void setObservationDocConformeAi(String observationDocConformeAi) {
        this.observationDocConformeAi = observationDocConformeAi;
    }

    public String getDocConformeAtq() {
        return docConformeAtq;
    }

    public void setDocConformeAtq(String docConformeAtq) {
        this.docConformeAtq = docConformeAtq;
    }

    public String getObservationDocConformeAtq() {
        return observationDocConformeAtq;
    }

    public void setObservationDocConformeAtq(String observationDocConformeAtq) {
        this.observationDocConformeAtq = observationDocConformeAtq;
    }

    public String getDocConformeCc() {
        return docConformeCc;
    }

    public void setDocConformeCc(String docConformeCc) {
        this.docConformeCc = docConformeCc;
    }

    public String getObservationDocConformeCc() {
        return observationDocConformeCc;
    }

    public void setObservationDocConformeCc(String observationDocConformeCc) {
        this.observationDocConformeCc = observationDocConformeCc;
    }

    public String getDocConformeCfCd() {
        return docConformeCfCd;
    }

    public void setDocConformeCfCd(String docConformeCfCd) {
        this.docConformeCfCd = docConformeCfCd;
    }

    public String getObservationDocConformeCfCd() {
        return observationDocConformeCfCd;
    }

    public void setObservationDocConformeCfCd(String observationDocConformeCfCd) {
        this.observationDocConformeCfCd = observationDocConformeCfCd;
    }

    public String getDocConformeCapcm() {
        return docConformeCapcm;
    }

    public void setDocConformeCapcm(String docConformeCapcm) {
        this.docConformeCapcm = docConformeCapcm;
    }

    public String getObservationDocConformeCapcm() {
        return observationDocConformeCapcm;
    }

    public void setObservationDocConformeCapcm(String observationDocConformeCapcm) {
        this.observationDocConformeCapcm = observationDocConformeCapcm;
    }

    public String getDocConformeCe() {
        return docConformeCe;
    }

    public void setDocConformeCe(String docConformeCe) {
        this.docConformeCe = docConformeCe;
    }

    public String getObservationDocConformeCe() {
        return observationDocConformeCe;
    }

    public void setObservationDocConformeCe(String observationDocConformeCe) {
        this.observationDocConformeCe = observationDocConformeCe;
    }

    public String getDocConformeAmm() {
        return docConformeAmm;
    }

    public void setDocConformeAmm(String docConformeAmm) {
        this.docConformeAmm = docConformeAmm;
    }

    public String getObservationDocConformeAmm() {
        return observationDocConformeAmm;
    }

    public void setObservationDocConformeAmm(String observationDocConformeAmm) {
        this.observationDocConformeAmm = observationDocConformeAmm;
    }

    public String getDocConformeAoi() {
        return docConformeAoi;
    }

    public void setDocConformeAoi(String docConformeAoi) {
        this.docConformeAoi = docConformeAoi;
    }

    public String getObservationDocConformeAoi() {
        return observationDocConformeAoi;
    }

    public void setObservationDocConformeAoi(String observationDocConformeAoi) {
        this.observationDocConformeAoi = observationDocConformeAoi;
    }

    public String getDocConformeVt() {
        return docConformeVt;
    }

    public void setDocConformeVt(String docConformeVt) {
        this.docConformeVt = docConformeVt;
    }

    public String getObservationDocConformeVt() {
        return observationDocConformeVt;
    }

    public void setObservationDocConformeVt(String observationDocConformeVt) {
        this.observationDocConformeVt = observationDocConformeVt;
    }

    public String getDocConformeCbpsd() {
        return docConformeCbpsd;
    }

    public void setDocConformeCbpsd(String docConformeCbpsd) {
        this.docConformeCbpsd = docConformeCbpsd;
    }

    public String getObservationDocConformeCbpsd() {
        return observationDocConformeCbpsd;
    }

    public void setObservationDocConformeCbpsd(String observationDocConformeCbpsd) {
        this.observationDocConformeCbpsd = observationDocConformeCbpsd;
    }

    public InputStream getQrCode() {
        return qrCode;
    }

    public void setQrCode(InputStream qrCode) {
        this.qrCode = qrCode;
    }

    public InputStream getControllerStamp() {
        return controllerStamp;
    }

    public void setControllerStamp(InputStream controllerStamp) {
        this.controllerStamp = controllerStamp;
    }

    public InputStream getControllerSignature() {
        return controllerSignature;
    }

    public void setControllerSignature(InputStream controllerSignature) {
        this.controllerSignature = controllerSignature;
    }

    public InputStream getSignatoryStamp() {
        return signatoryStamp;
    }

    public void setSignatoryStamp(InputStream signatoryStamp) {
        this.signatoryStamp = signatoryStamp;
    }

    public InputStream getSignatorySignature() {
        return signatorySignature;
    }

    public void setSignatorySignature(InputStream signatorySignature) {
        this.signatorySignature = signatorySignature;
    }

    public String getContainersNumbers() {
        return containersNumbers;
    }

    public void setContainersNumbers(String containersNumbers) {
        this.containersNumbers = containersNumbers;
    }

    public Boolean getHasContainers() {
        return hasContainers;
    }

    public void setHasContainers(Boolean hasContainers) {
        this.hasContainers = hasContainers;
    }

    
}
