package org.guce.siat.web.reports.exporter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.lookup.ServiceUtility;
import org.guce.siat.common.model.Container;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.service.ApplicationPropretiesService;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.common.utils.QRCodeGenerator;
import org.guce.siat.core.ct.model.TreatmentInfosCCSMinsante;
import org.guce.siat.web.ct.controller.util.Utils;
import org.guce.siat.web.reports.vo.CcsMinsanteFileItemVo;
import org.guce.siat.web.reports.vo.CcsMinsanteFileVo;

/**
 * The Class CcsMinsanteExporter.
 */
public class CcsMinsanteExporter extends AbstractReportInvoker {

    private static final String FORM_FIELD_CHECKBOX_CHECKED_VALUE = "X";
    private static final String FORM_FIELD_CHECKBOX_NOCHECKED_VALUE = "";

    private static final String CONFORME_O = "O";
    private static final String CONFRME_N = "N";
    
    private static final String PACKAGING_CONTAINERIZED = "Conteneurisé";
    private static final String PACKAGING_CONVENTIONNAL = "Conventionnel";
    /**
     * The file.
     */
    private final File file;
    private final TreatmentInfosCCSMinsante treatmentInfos;

    public CcsMinsanteExporter(File file, TreatmentInfosCCSMinsante treatmentInfos) {
        super("CCS_MINSANTE", "CCS_MINSANTE");
        this.file = file;
        this.treatmentInfos = treatmentInfos;
    }

    public CcsMinsanteExporter(File file, String jasperFileName, TreatmentInfosCCSMinsante treatmentInfos) {
        super(jasperFileName, "CCS_MINSANTE");
        this.file = file;
        this.treatmentInfos = treatmentInfos;
    }

    public CcsMinsanteExporter(File file, String jasperFileName, String fileName, TreatmentInfosCCSMinsante treatmentInfos) {
        super(jasperFileName, fileName);
        this.file = file;
        this.treatmentInfos = treatmentInfos;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final CcsMinsanteFileVo ccsMinsanteVo = new CcsMinsanteFileVo();
        List<FileItem> fileItems = file.getFileItemsList();
        List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
        List<Container> containers = file.getContainers();

        int modifIndex = 0;
        String senderName = org.apache.commons.lang3.StringUtils.EMPTY, senderAddress = org.apache.commons.lang3.StringUtils.EMPTY, transportWay = org.apache.commons.lang3.StringUtils.EMPTY,
                goodsName = org.apache.commons.lang3.StringUtils.EMPTY, diNumber = org.apache.commons.lang3.StringUtils.EMPTY,
                blNumber = org.apache.commons.lang3.StringUtils.EMPTY, goodsWeight = org.apache.commons.lang3.StringUtils.EMPTY,
                container20Number = org.apache.commons.lang3.StringUtils.EMPTY, container40Number = org.apache.commons.lang3.StringUtils.EMPTY, originCountry = org.apache.commons.lang3.StringUtils.EMPTY,
                makerCountry = org.apache.commons.lang3.StringUtils.EMPTY, signatureDate = org.apache.commons.lang3.StringUtils.EMPTY;

        for (FileFieldValue fileFieldValue : fileFieldValueList) {
            switch (fileFieldValue.getFileField().getCode()) {
                case "DOCUMENTS_NUMERO_DI":
                    diNumber = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_NUMERO_BL":
                    blNumber = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_PAYS_ORIGINE_NOM_PAYS":
                    originCountry = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "EXPORTATEUR_RAISONSOCIALE":
                    senderName = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "EXPORTATEUR_ADRESSE_ADRESSE1":
                    senderAddress = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_NB_CONTENEUR20":
                    container20Number = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_NB_CONTENEUR40":
                    container40Number = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
                    transportWay = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "SIGNATAIRE_DATE":
                    signatureDate = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;

            }
        }
        ccsMinsanteVo.setDecisionNumber(file.getParent() != null ? file.getParent().getNumeroDossier() : file.getNumeroDossier());
        if (file.getSignatory() != null) {
            ccsMinsanteVo.setSignatory(String.format("%s %s", file.getSignatory().getFirstName(), file.getSignatory().getLastName()));
        }
        if (file.getSignatureDate() != null) {
            ccsMinsanteVo.setSignatureDate(DateUtils.formatSimpleDate(DateUtils.FRENCH_DATE, file.getSignatureDate()));
        } else {
            ccsMinsanteVo.setSignatureDate(DateUtils.formatSimpleDate(DateUtils.FRENCH_DATE, java.util.Calendar.getInstance().getTime()));
        }
        ccsMinsanteVo.setBlNumber(blNumber);
        ccsMinsanteVo.setDiNumber(diNumber);
        ccsMinsanteVo.setProvider(senderName);
        ccsMinsanteVo.setProviderAddress(senderAddress);
        ccsMinsanteVo.setNumberContainers20(container20Number);
        ccsMinsanteVo.setNumberContainers40(container40Number);
        ccsMinsanteVo.setProvenance(originCountry);
        ccsMinsanteVo.setShip(transportWay);
        modifIndex = 1;
        ccsMinsanteVo.setFileItemList(new ArrayList<CcsMinsanteFileItemVo>());
        if (CollectionUtils.isNotEmpty(fileItems)) {
            for (FileItem fileItem : fileItems) {
                final CcsMinsanteFileItemVo fileItemVo = new CcsMinsanteFileItemVo();
                String quantity = null;
                for (FileItemFieldValue fileItemFieldValue : fileItem.getFileItemFieldValueList()) {
                    switch (fileItemFieldValue.getFileItemField().getCode()) {
                        case "AMM":
                            fileItemVo.setAmm(fileItemFieldValue.getValue());
                            break;
                        case "UNITE":
                            fileItemVo.setUnit(fileItemFieldValue.getValue());
                            break;
                        case "POIDS":
                            quantity = fileItemFieldValue.getValue();
                            break;
                        case "DESCRIPTION":
                            fileItemVo.setDesc(fileItemFieldValue.getValue());
                            break;
                        default:
                            break;
                    }
                }
                if (StringUtils.isNotEmpty(fileItem.getQuantity())) {
                    quantity = fileItem.getQuantity();
                }
                fileItemVo.setQuantity(StringUtils.isNotEmpty(quantity) ? Double.parseDouble(quantity) : 0);
                fileItemVo.setFobValue(fileItem.getFobValue());
                if (fileItem.getNsh() != null) {
                    fileItemVo.setCode(fileItem.getNsh().getGoodsItemCode());
                }
                if (fileItemVo.getDesignation() == null || StringUtils.isEmpty(fileItemVo.getDesignation())) {
                    if (fileItem.getNsh() != null) {
                        fileItemVo.setDesignation(fileItem.getNsh().getGoodsItemDesc());
                    }
                }
                ccsMinsanteVo.getFileItemList().add(fileItemVo);
            }
        }

        if (file.getClient() != null) {
            String companyName = file.getClient().getCompanyName();
            String companyAddress = org.apache.commons.lang3.StringUtils.EMPTY;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(file.getClient().getFullAddress())) {
                companyAddress = file.getClient().getFullAddress();
            } else if (org.apache.commons.lang3.StringUtils.isNotEmpty(file.getClient().getFirstAddress())) {
                companyAddress = file.getClient().getFirstAddress();
            }
            ccsMinsanteVo.setImporter(companyName);
            ccsMinsanteVo.setImporterAddress(companyAddress);
        }
        boolean hasContainers = false;
        ccsMinsanteVo.setPackaging(PACKAGING_CONVENTIONNAL.toUpperCase());
        if (CollectionUtils.isNotEmpty(containers)) {
            hasContainers = true;

            Collection<String> containerNumbers = CollectionUtils.collect(containers, new Transformer() {
                @Override
                public String transform(Object input) {
                    Container container = (Container) input;
                    return container.getContNumber();
                }
            });

            ccsMinsanteVo.setContainersNumbers(org.apache.commons.lang.StringUtils.join(containerNumbers, " - "));
            ccsMinsanteVo.setPackaging(PACKAGING_CONTAINERIZED.toUpperCase());
        }
        ccsMinsanteVo.setLotsNumber("");
        ccsMinsanteVo.setHasContainers(hasContainers);
        if (treatmentInfos != null) {
            if (treatmentInfos.getItemFlow() != null && treatmentInfos.getItemFlow().getSender() != null) {
                ccsMinsanteVo.setController(String.format("%s %s", treatmentInfos.getItemFlow().getSender().getFirstName(), treatmentInfos.getItemFlow().getSender().getLastName()));
            }
            if (treatmentInfos.isCcsMinsanteDrugProducts()) {
                ccsMinsanteVo.setProductType1(treatmentInfos.isDrugs() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                ccsMinsanteVo.setProductType2(treatmentInfos.isMedicalDevices() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                ccsMinsanteVo.setProductType3(treatmentInfos.isTropicalCorticosteroids() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                ccsMinsanteVo.setProductType4(treatmentInfos.isLaboratoryProducts() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                ccsMinsanteVo.setProductType5(treatmentInfos.isPackagingSfProducts() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);

                ccsMinsanteVo.setDocConformeA(treatmentInfos.isConformeA() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeA(treatmentInfos.getConformityObservationA() != null ? treatmentInfos.getConformityObservationA() : "");

                ccsMinsanteVo.setDocConformeAmm(treatmentInfos.isConformeAMM() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeAmm(treatmentInfos.getConformityObservationAMM() != null ? treatmentInfos.getConformityObservationAMM() : "");

                ccsMinsanteVo.setDocConformeAi(treatmentInfos.isConformeAI() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeAi(treatmentInfos.getConformityObservationAI() != null ? treatmentInfos.getConformityObservationAI() : "");

                ccsMinsanteVo.setDocConformeVt(treatmentInfos.isConformeVT() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeVt(treatmentInfos.getConformityObservationVT() != null ? treatmentInfos.getConformityObservationVT() : "");

                ccsMinsanteVo.setDocConformeAoi(treatmentInfos.isConformeAOI() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeAoi(treatmentInfos.getConformityObservationAOI() != null ? treatmentInfos.getConformityObservationAOI() : "");

                ccsMinsanteVo.setDocConformeCc(treatmentInfos.isConformeCC() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeCc(treatmentInfos.getConformityObservationCC() != null ? treatmentInfos.getConformityObservationCC() : "");

                ccsMinsanteVo.setDocConformeCbpsd(treatmentInfos.isConformeCBPSD() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeCbpsd(treatmentInfos.getConformityObservationCBPSD() != null ? treatmentInfos.getConformityObservationCBPSD() : "");

            } else {
                ccsMinsanteVo.setProductType1(treatmentInfos.isProductFoodIHC() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                ccsMinsanteVo.setProductType2(treatmentInfos.isThriftShop() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                ccsMinsanteVo.setProductType3(treatmentInfos.isFleaMarket() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                ccsMinsanteVo.setProductType4(treatmentInfos.isVehicle() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                ccsMinsanteVo.setProductType5(treatmentInfos.isHygienSanitationProducts() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);

                ccsMinsanteVo.setDocConformeA(treatmentInfos.isConformeA() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeA(treatmentInfos.getConformityObservationA() != null ? treatmentInfos.getConformityObservationA() : "");

                ccsMinsanteVo.setDocConformeAmmAmc(treatmentInfos.isConformeAmmAmc() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeAmmAmc(treatmentInfos.getConformityObservationAmmAmc() != null ? treatmentInfos.getConformityObservationAmmAmc() : "");

                ccsMinsanteVo.setDocConformeAi(treatmentInfos.isConformeAI() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeAi(treatmentInfos.getConformityObservationAI() != null ? treatmentInfos.getConformityObservationAI() : "");

                ccsMinsanteVo.setDocConformeAtq(treatmentInfos.isConformeATQ() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeAtq(treatmentInfos.getConformityObservationATQ() != null ? treatmentInfos.getConformityObservationATQ()  : "");

                ccsMinsanteVo.setDocConformeCfCd(treatmentInfos.isConformeCFCD() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeCfCd(treatmentInfos.getConformityObservationCFCD() != null ? treatmentInfos.getConformityObservationCFCD() : "");

                ccsMinsanteVo.setDocConformeCc(treatmentInfos.isConformeCC() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeCc(treatmentInfos.getConformityObservationCC() != null ? treatmentInfos.getConformityObservationCC() : "");

                ccsMinsanteVo.setDocConformeCapcm(treatmentInfos.isConformeCAPCM() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeCapcm(treatmentInfos.getConformityObservationCAPCM() != null ? treatmentInfos.getConformityObservationCAPCM() : "");

                ccsMinsanteVo.setDocConformeCe(treatmentInfos.isConformeCE() ? CONFORME_O : CONFRME_N);
                ccsMinsanteVo.setObservationDocConformeCe(treatmentInfos.getConformityObservationCE() != null ? treatmentInfos.getConformityObservationCE() : "");
            }
        }

        String qrContent = getDocumentPageUrl(file);
        try {
            InputStream qrCode = new ByteArrayInputStream(new QRCodeGenerator().generateQR(qrContent, 512));
            ccsMinsanteVo.setQrCode(qrCode);
            if (file.getSignatory() != null) {
                try {
                    InputStream signatorySignature = getUserStampSignatureService().getUserSignature(file.getSignatory());
                    if (signatorySignature != null) {
                        ccsMinsanteVo.setSignatorySignature(signatorySignature);
                    }
                } catch (Exception ex) {
                }
                try {
                    InputStream signatoryStamp = getUserStampSignatureService().getUserStamp(file.getSignatory());
                    if (signatoryStamp != null) {
                        ccsMinsanteVo.setSignatoryStamp(signatoryStamp);
                    }
                } catch (Exception ex) {
                }
            }
            if (treatmentInfos.getItemFlow() != null && treatmentInfos.getItemFlow().getSender() != null) {
                try {
                    InputStream controllerSignature = getUserStampSignatureService().getUserSignature(treatmentInfos.getItemFlow().getSender());
                    if (controllerSignature != null) {
                        ccsMinsanteVo.setControllerSignature(controllerSignature);
                    }
                } catch (Exception ex) {
                }
                try {
                    InputStream controllerStamp = getUserStampSignatureService().getUserStamp(treatmentInfos.getItemFlow().getSender());
                    if (controllerStamp != null) {
                        ccsMinsanteVo.setControllerStamp(controllerStamp);
                    }
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
            logger.error(file.getNumeroDossier(), ex);
        }

        return new JRBeanCollectionDataSource(Collections.singleton(ccsMinsanteVo));
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getJRParameters()
     */
    @Override
    protected Map<String, Object> getJRParameters() {

        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINSANTE_LOGO", getRealPath(IMAGES_PATH, "minsante", "png"));
        return jRParameters;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

    private String getDocumentPageUrl(File file) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        String documentPageUrl = "/pages/unsecure/document.xhtml";
        String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, documentPageUrl));
        url = Utils.getFinalDetailPageUrl(file, url, false, false);
        ApplicationPropretiesService applicationPropretiesService = ServiceUtility.getBean(ApplicationPropretiesService.class);
        String environment = applicationPropretiesService.getAppEnv();
        String urlPrefix;
        switch (environment) {
            case "standalone":
            case "production":
                urlPrefix = "https://siat.guichetunique.cm";
                break;
            case "test":
                urlPrefix = "https://testsiat.guichetunique.cm";
                break;
            default:
                urlPrefix = "https://localhost:40081";
                break;
        }
        url = urlPrefix.concat(url);
        return url;
    }

}
