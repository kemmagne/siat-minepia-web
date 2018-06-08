package org.guce.siat.web.reports.exporter;

import com.google.common.base.Objects;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.core.ct.model.InspectionController;
import org.guce.siat.core.ct.model.InspectionReport;
import org.guce.siat.web.ct.controller.util.Utils;
import static org.guce.siat.web.reports.exporter.ReportCommand.IMAGES_PATH;
import org.guce.siat.web.reports.vo.CtCctControllerDataVo;
import org.guce.siat.web.reports.vo.CtCctDataVo;

/**
 *
 * @author tadzotsa
 */
public class CtPviExporter extends AbstractReportInvoker {

    /**
     * The inspection report.
     */
    private final InspectionReport inspectionReport;
    private final File file;
    protected static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";
    private static final String CONSTAT_BUNDLE_TRUE = "constatBundleTrue";
    private static final String CONSTAT_BUNDLE_FALSE = "constatBundleFalse";

    public CtPviExporter(final InspectionReport inspectionReport) {
        super("CCT_CT_E_PVI", "CCT_CT_E_PVI");
        this.inspectionReport = inspectionReport;
        this.file = inspectionReport.getItemFlow().getFileItem().getFile();
    }

    @Override
    protected JRBeanCollectionDataSource getReportDataSource() {
        if (inspectionReport == null) {
            return null;
        }
        final CtCctDataVo entryInspectionFindingDataVo = new CtCctDataVo();

        try {
            final List<CtCctControllerDataVo> controllerDataVoList = new ArrayList<>();
            //General Information
            if (StringUtils.isNotBlank(inspectionReport.getPlace())) {
                entryInspectionFindingDataVo.setPlace(inspectionReport.getPlace());
            }
            if (inspectionReport.getReportDate() != null) {
                entryInspectionFindingDataVo.setDate(inspectionReport.getReportDate());
            }
            if (inspectionReport.getReportTime() != null && StringUtils.isNotBlank(inspectionReport.getReportTime().toString())) {
                entryInspectionFindingDataVo.setHour(inspectionReport.getReportTime().toString());
            }
            //Echantillons
            if (inspectionReport.getSample() != null) {
                entryInspectionFindingDataVo.setQuantity(String.valueOf(inspectionReport.getSample().getTakenQuantity()));
                entryInspectionFindingDataVo.setContainerNumber(String.valueOf(inspectionReport.getSample().getContainerNumber()));
                entryInspectionFindingDataVo.setLotNumber(String.valueOf(inspectionReport.getSample().getLotNumber()));
            }
            //Controllers
            if (CollectionUtils.isNotEmpty(inspectionReport.getInspectionControllerList())) {
                for (final InspectionController inspectionController : inspectionReport.getInspectionControllerList()) {
                    final CtCctControllerDataVo controllerDataVo = new CtCctControllerDataVo();
                    if (StringUtils.isNotBlank(inspectionController.getName())) {
                        controllerDataVo.setName(inspectionController.getName());
                    }
                    if (StringUtils.isNotBlank(inspectionController.getPosition())) {
                        controllerDataVo.setQuality(inspectionController.getPosition());
                    }
                    if (StringUtils.isNotBlank(inspectionController.getService())) {
                        controllerDataVo.setService(inspectionController.getService());
                    }
                    controllerDataVoList.add(controllerDataVo);
                }
            }
            //Etiquetage
            if (StringUtils.isNotBlank(inspectionReport.getLabel())) {
                entryInspectionFindingDataVo.setLibelle(inspectionReport.getLabel());
            }
            if (inspectionReport.getStandardCompliance() != null && BooleanUtils.isTrue(inspectionReport.getStandardCompliance())) {
                entryInspectionFindingDataVo.setNormeRespect(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_TRUE));
            } else {
                entryInspectionFindingDataVo.setNormeRespect(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
            }
            entryInspectionFindingDataVo.setLabellingNumber(String.valueOf(inspectionReport.getStandardNumber()));
            //Temperature
            entryInspectionFindingDataVo.setTemperature(String.valueOf(inspectionReport.getTemperature()));
            if (StringUtils.isNotBlank(inspectionReport.getAspect())) {
                entryInspectionFindingDataVo.setAspect(inspectionReport.getAspect());
            }
            //Certificats
            if (BooleanUtils.isTrue(inspectionReport.getOriginCertificate())) {
                entryInspectionFindingDataVo.setOrigineCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_TRUE));
            } else {
                entryInspectionFindingDataVo.setOrigineCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
            }
            if (BooleanUtils.isTrue(inspectionReport.getPhytoGeneralCertificate())) {
                entryInspectionFindingDataVo.setGeneralPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                        getCurrentLocale()).getString(CONSTAT_BUNDLE_TRUE));
            } else {
                entryInspectionFindingDataVo.setGeneralPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                        getCurrentLocale()).getString(CONSTAT_BUNDLE_FALSE));
            }
            if (BooleanUtils.isTrue(inspectionReport.getPhytoSpecialCertificate())) {
                entryInspectionFindingDataVo.setSpecialPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                        getCurrentLocale()).getString(CONSTAT_BUNDLE_TRUE));
            } else {
                entryInspectionFindingDataVo.setSpecialPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                        getCurrentLocale()).getString(CONSTAT_BUNDLE_FALSE));
            }
            if (BooleanUtils.isTrue(inspectionReport.getQualityAttestation())) {
                entryInspectionFindingDataVo.setQualityCertification(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_TRUE));
            } else {
                entryInspectionFindingDataVo.setQualityCertification(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
            }
            if (BooleanUtils.isTrue(inspectionReport.getSanitaryVetCertificate())) {
                entryInspectionFindingDataVo.setVeterinaryHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                        getCurrentLocale()).getString(CONSTAT_BUNDLE_TRUE));
            } else {
                entryInspectionFindingDataVo.setVeterinaryHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                        getCurrentLocale()).getString(CONSTAT_BUNDLE_FALSE));
            }
            if (BooleanUtils.isTrue(inspectionReport.getWholesomenessCertificate())) {
                entryInspectionFindingDataVo.setHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_TRUE));
            } else {
                entryInspectionFindingDataVo.setHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
            }
            if (BooleanUtils.isTrue(inspectionReport.getConformityCertificate())) {
                entryInspectionFindingDataVo.setConformityCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_TRUE));
            } else {
                entryInspectionFindingDataVo.setConformityCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
            }

            if (!Objects.equal(inspectionReport.getOtherGoodness(), null)) {
                entryInspectionFindingDataVo.setOtherQualityCertificate(inspectionReport.getOtherGoodness().getLabelFr());
            }
            //Decision
            if (StringUtils.isNotBlank(inspectionReport.getControllerDecision())) {
                entryInspectionFindingDataVo.setDecision(inspectionReport.getControllerDecision());
            }
            //Observation
            if (StringUtils.isNotBlank(inspectionReport.getObservation())) {
                entryInspectionFindingDataVo.setObservation(inspectionReport.getObservation());
            }
            //Motif
            if (StringUtils.isNotBlank(inspectionReport.getMotif())) {
                entryInspectionFindingDataVo.setMotif(inspectionReport.getMotif());
            }
            //Frais
            if (StringUtils.isNotBlank(inspectionReport.getInspectionFeesFCFA())) {
                entryInspectionFindingDataVo.setInspectionFeeFcfa(inspectionReport.getInspectionFeesFCFA());
            }
            if (StringUtils.isNotBlank(inspectionReport.getInspectionFees())) {
                entryInspectionFindingDataVo.setProcessingFee(inspectionReport.getInspectionFees());
            }
            if (StringUtils.isNotBlank(inspectionReport.getOtherSpecialFees())) {
                entryInspectionFindingDataVo.setSpecialOtherFees(inspectionReport.getOtherSpecialFees());
            }
            //Infraction
            if (StringUtils.isNotBlank(inspectionReport.getInfraction())) {
                entryInspectionFindingDataVo.setNature(inspectionReport.getInfraction());
            }
            if (CollectionUtils.isNotEmpty(controllerDataVoList)) {
                entryInspectionFindingDataVo.setControlerList(controllerDataVoList);
            }
            if (inspectionReport.getItemFlow().getFileItem().getFile().getDestinataire().equalsIgnoreCase("MINADER")) {
                // Specific field for MINADER
                if (inspectionReport.getItemFlow().getFileItem().getFile().getCountryOfProvenance() != null) {
                    entryInspectionFindingDataVo.setNativeCountry(inspectionReport.getItemFlow().getFileItem().getFile().getCountryOfProvenance().getCountryName());
                }
                if (inspectionReport.getItemFlow().getFileItem().getFile().getCountryOfOrigin() != null) {
                    entryInspectionFindingDataVo.setOriginCountry(inspectionReport.getItemFlow().getFileItem().getFile().getCountryOfOrigin().getCountryName());
                }
                entryInspectionFindingDataVo.setSender(new StringBuilder(inspectionReport.getItemFlow().getFileItem().getFile().getClient().getCompanyName()).append(" ").append(inspectionReport.getItemFlow().getFileItem().getFile().getClient().getFullAddress()).toString());
                entryInspectionFindingDataVo.setGants(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
                if (inspectionReport.getGants() != null && inspectionReport.getGants()) {
                    entryInspectionFindingDataVo.setGants(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                }
                entryInspectionFindingDataVo.setCasque(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
                if (inspectionReport.getCasque() != null && inspectionReport.getCasque()) {
                    entryInspectionFindingDataVo.setCasque(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                }
                entryInspectionFindingDataVo.setChaussureSecurite(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
                if (inspectionReport.getChaussureSecurite() != null && inspectionReport.getChaussureSecurite()) {
                    entryInspectionFindingDataVo.setChaussureSecurite(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                }
                entryInspectionFindingDataVo.setRespectNorme(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
                if (inspectionReport.getRespectNorme() != null && inspectionReport.getRespectNorme()) {
                    entryInspectionFindingDataVo.setRespectNorme(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                }
                entryInspectionFindingDataVo.setChaussureSecurite(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
                if (inspectionReport.getChaussureSecurite() != null && inspectionReport.getChaussureSecurite()) {
                    entryInspectionFindingDataVo.setChaussureSecurite(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                }
                entryInspectionFindingDataVo.setSourcePropagationPeste(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
                if (inspectionReport.getSourcePropagationPeste() != null && inspectionReport.getSourcePropagationPeste()) {
                    entryInspectionFindingDataVo.setSourcePropagationPeste(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                }
                entryInspectionFindingDataVo.setCombinaison(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(CONSTAT_BUNDLE_FALSE));
                if (inspectionReport.getCombinaison() != null && inspectionReport.getCombinaison()) {
                    entryInspectionFindingDataVo.setCombinaison(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                }
                entryInspectionFindingDataVo.setWater(inspectionReport.getWater());
                entryInspectionFindingDataVo.setAutocontrolDocument(inspectionReport.getAutocontrolDocument());
                entryInspectionFindingDataVo.setDechet(inspectionReport.getDechet());
                entryInspectionFindingDataVo.setProcedure(inspectionReport.getProcedure());
                entryInspectionFindingDataVo.setProcessAnalyse(inspectionReport.getProcessAnalyse());
                //
                if (controllerDataVoList != null && !controllerDataVoList.isEmpty()) {
                    entryInspectionFindingDataVo.setSignatairePVIPhyto(controllerDataVoList.get(0).getName());
                } else {
                    entryInspectionFindingDataVo.setSignatairePVIPhyto(inspectionReport.getControllerName());
                }
                entryInspectionFindingDataVo.setTypeTraitement(inspectionReport.getTypeTraitement());
                entryInspectionFindingDataVo.setEtatDateDernierTraitement(inspectionReport.getEtatDateDernierTraitement());
                entryInspectionFindingDataVo.setProduitUtilise(inspectionReport.getProduitUtilise());
                entryInspectionFindingDataVo.setDosage(inspectionReport.getDosage());
                entryInspectionFindingDataVo.setEnvironnementStockage(inspectionReport.getEnvironnementStockage());
                entryInspectionFindingDataVo.setEnvironnementTransport(inspectionReport.getEnvironnementTransport());
                entryInspectionFindingDataVo.setConditionClimatique(inspectionReport.getConditionClimatique());
                entryInspectionFindingDataVo.setMesureProtection(inspectionReport.getMesureProtection());
                entryInspectionFindingDataVo.setObservations(inspectionReport.getObservations());
                entryInspectionFindingDataVo.setArticleReglemente(inspectionReport.getArticleReglemente());
                entryInspectionFindingDataVo.setExporterName(inspectionReport.getItemFlow().getFileItem().getFile().getClient().getCompanyName());
                entryInspectionFindingDataVo.setPviCouvertDoc(inspectionReport.isPviCouvertDoc());
                entryInspectionFindingDataVo.setPviDispositionsPrises(inspectionReport.getPviDispositionsPrises());
                entryInspectionFindingDataVo.setPviNatureArticleInspecte(inspectionReport.getPviNatureArticleInspecte());
                entryInspectionFindingDataVo.setPviQuantite(inspectionReport.getPviQuantite());
                entryInspectionFindingDataVo.setPviReference(inspectionReport.getItemFlow().getFileItem().getFile().getNumeroDossier());
                entryInspectionFindingDataVo.setPviSituationArticle(inspectionReport.getPviSituationArticle());
                String typeProduit = null;
                for (FileFieldValue fileFieldValue : inspectionReport.getItemFlow().getFileItem().getFile().getFileFieldValueList()) {
                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("DESTINATAIRE_RAISONSOCIALE")) {
                        entryInspectionFindingDataVo.setRecipient(fileFieldValue.getValue());
                    }
                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("NUMERO_CCT_CT_E_PVI")) {
                        entryInspectionFindingDataVo.setNumeroDecisionPVI(fileFieldValue.getValue());
                    }
                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("TYPE_PRODUIT_CODE")) {
                        typeProduit = fileFieldValue.getValue();
                    }
                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS")) {
                        entryInspectionFindingDataVo.setPviDestination(fileFieldValue.getValue());
                    }
                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("INFORMATIONS_GENERALES_PERMIS_DATE") && !fileFieldValue.getValue().equalsIgnoreCase("-")) {
                        try {
                            entryInspectionFindingDataVo.setImportLicenceDate(new SimpleDateFormat("dd/MM/YYYY").parse(fileFieldValue.getValue()));
                        } catch (ParseException ex) {
                            Logger.getLogger(CtCctExporter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("INFORMATIONS_GENERALES_PERMIS_DELIVREUR_RAISONSOCIALE")) {
                        entryInspectionFindingDataVo.setImportLicenceDeliver(fileFieldValue.getValue());
                    }
                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("INFORMATIONS_GENERALES_PERMIS_NUMERO_PERMIS")) {
                        entryInspectionFindingDataVo.setImportLicenceNumber(fileFieldValue.getValue());
                    }

                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("INFORMATIONS_GENERALES_TRANSPORT_MOYEN_TRANSPORT_LIBELLE")) {
                        entryInspectionFindingDataVo.setTransportMeans(fileFieldValue.getValue());
                    }
                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("INFORMATIONS_GENERALES_TRANSPORT_NUM_CONNAISSEMENT_LTA")) {
                        entryInspectionFindingDataVo.setBillOfLoading(fileFieldValue.getValue());
                    }
//					if (StringUtils.isNotEmpty(entryInspectionFindingDataVo.getTransportMeans()) && StringUtils.isNotEmpty(entryInspectionFindingDataVo.getBillOfLoading())){
//						break;
//					}
                }

                final List<FileItem> fileItemList = file.getFileItemsList();
                BigDecimal quantity = BigDecimal.ZERO;
                String unite = null;
                for (final FileItem fileItem : fileItemList) {
                    final String qtyString = fileItem.getQuantity();
                    if (StringUtils.isNotBlank(qtyString) && Utils.getCacaProductsTypes().contains(typeProduit)) {
                        quantity = quantity.add(new BigDecimal(qtyString));
                        unite = "KG";
                    }
                    final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
                    for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
                        switch (fileItemFieldValue.getFileItemField().getCode()) {
                            case "VOLUME":
                                final String volString = fileItemFieldValue.getValue();
                                if (StringUtils.isNotBlank(volString) && Utils.getWoodProductsTypes().contains(typeProduit)) {
                                    quantity = quantity.add(new BigDecimal(volString));
                                    unite = "M3";
                                }
                                break;
                            case "NOM_COMMERCIAL":
                                entryInspectionFindingDataVo.setPviNatureArticleInspecte(fileItemFieldValue.getValue());
                                break;
                        }
                    }
                }
                entryInspectionFindingDataVo.setPviQuantite(quantity.toString() + " " + unite);
            }
        } catch (Exception e) {
            Logger.getLogger(CtPviExporter.class.getName()).log(Level.SEVERE, "Problem occured", e);
        }

        return new JRBeanCollectionDataSource(Collections.singleton(entryInspectionFindingDataVo));
    }

    /**
     * Gets the current locale.
     *
     * @return the current locale
     */
    public Locale getCurrentLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getJRParameters()
     */
    @Override
    protected Map<String, Object> getJRParameters() {
        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "minader", "jpg"));
        return jRParameters;
    }

}
