package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.core.ct.model.InspectionController;
import org.guce.siat.core.ct.model.InspectionReport;
import static org.guce.siat.web.reports.exporter.ReportCommand.IMAGES_PATH;
import org.guce.siat.web.reports.vo.CtCctControllerDataVo;
import org.guce.siat.web.reports.vo.CtCctDataVo;

/**
 * The Class CtCctExporter.
 */
public class CtCctExporter extends AbstractReportInvoker {

    /**
     * The inspection report.
     */
    private final InspectionReport inspectionReport;
    protected static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";
    private static final String CONSTAT_BUNDLE_TRUE = "constatBundleTrue";
    private static final String CONSTAT_BUNDLE_FALSE = "constatBundleFalse";

    /**
     * Instantiates a new ct cct exporter.
     *
     * @param inspectionReport the inspection report
     */
    public CtCctExporter(InspectionReport inspectionReport) {
        super("CT_CCT", "CT_CCT");
        this.inspectionReport = inspectionReport;
    }

    public CtCctExporter(String jasperFileName, String pdfFileName, InspectionReport inspectionReport) {
        super(jasperFileName, pdfFileName);
        this.inspectionReport = inspectionReport;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {
        final CtCctDataVo entryInspectionFindingDataVo = new CtCctDataVo();
        System.out.println("inspectionReport : " + inspectionReport);
        try {
            if ((inspectionReport != null)) {
                final List<CtCctControllerDataVo> controllerDataVoList = new ArrayList<>();
                //General Information
                if (StringUtils.isNotBlank(inspectionReport.getPlace())) {
                    entryInspectionFindingDataVo.setPlace(inspectionReport.getPlace());
                }
                if (inspectionReport.getReportDate() != null) {
                    entryInspectionFindingDataVo.setDate(inspectionReport.getReportDate());
                }
                if (StringUtils.isNotBlank(inspectionReport.getReportTime().toString())) {
                    entryInspectionFindingDataVo.setHour(inspectionReport.getReportTime().toString());
                }
                //Echantillons
                entryInspectionFindingDataVo.setQuantity(String.valueOf(inspectionReport.getSample().getTakenQuantity()));
                entryInspectionFindingDataVo.setContainerNumber(String.valueOf(inspectionReport.getSample().getContainerNumber()));
                entryInspectionFindingDataVo.setLotNumber(String.valueOf(inspectionReport.getSample().getLotNumber()));
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
                if (inspectionReport.getStandardCompliance() != null && inspectionReport.getStandardCompliance() == true) {
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
                if (inspectionReport.getOriginCertificate() == true) {
                    entryInspectionFindingDataVo.setOrigineCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                } else {
                    entryInspectionFindingDataVo.setOrigineCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_FALSE));
                }
                if (inspectionReport.getPhytoGeneralCertificate() == true) {
                    entryInspectionFindingDataVo.setGeneralPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(CONSTAT_BUNDLE_TRUE));
                } else {
                    entryInspectionFindingDataVo.setGeneralPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(CONSTAT_BUNDLE_FALSE));
                }
                if (inspectionReport.getPhytoSpecialCertificate() == true) {
                    entryInspectionFindingDataVo.setSpecialPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(CONSTAT_BUNDLE_TRUE));
                } else {
                    entryInspectionFindingDataVo.setSpecialPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(CONSTAT_BUNDLE_FALSE));
                }
                if (inspectionReport.getQualityAttestation() == true) {
                    entryInspectionFindingDataVo.setQualityCertification(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                } else {
                    entryInspectionFindingDataVo.setQualityCertification(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_FALSE));
                }
                if (inspectionReport.getSanitaryVetCertificate() == true) {
                    entryInspectionFindingDataVo.setVeterinaryHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(CONSTAT_BUNDLE_TRUE));
                } else {
                    entryInspectionFindingDataVo.setVeterinaryHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(CONSTAT_BUNDLE_FALSE));
                }
                if (inspectionReport.getWholesomenessCertificate() == true) {
                    entryInspectionFindingDataVo.setHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                } else {
                    entryInspectionFindingDataVo.setHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_FALSE));
                }
                if (inspectionReport.getConformityCertificate() == true) {
                    entryInspectionFindingDataVo.setConformityCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_TRUE));
                } else {
                    entryInspectionFindingDataVo.setConformityCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(CONSTAT_BUNDLE_FALSE));
                }

                if (!Objects.equals(inspectionReport.getOtherGoodness(), null)) {
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
                if (inspectionReport.getFileItem().getFile().getDestinataire().equalsIgnoreCase("MINADER")) {
                    // Specific field for MINADER
                    if (inspectionReport.getFileItem().getFile().getCountryOfProvenance() != null) {
                        entryInspectionFindingDataVo.setNativeCountry(inspectionReport.getFileItem().getFile().getCountryOfProvenance().getCountryName());
                    }
                    if (inspectionReport.getFileItem().getFile().getCountryOfOrigin() != null) {
                        entryInspectionFindingDataVo.setOriginCountry(inspectionReport.getFileItem().getFile().getCountryOfOrigin().getCountryName());
                    }
                    entryInspectionFindingDataVo.setSender(new StringBuilder(inspectionReport.getFileItem().getFile().getClient().getCompanyName()).append(" ").append(inspectionReport.getFileItem().getFile().getClient().getFullAddress()).toString());
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
                    if (CollectionUtils.isNotEmpty(controllerDataVoList)) {
                        entryInspectionFindingDataVo.setSignatairePVIPhyto(controllerDataVoList.get(0).getName());
                    } else {
                        entryInspectionFindingDataVo.setSignatairePVIPhyto(inspectionReport.getControllerName());
                    }
//                    entryInspectionFindingDataVo.setTypeTraitement(inspectionReport.getTypeTraitement());
                    entryInspectionFindingDataVo.setEtatDateDernierTraitement(inspectionReport.getEtatDateDernierTraitement());
                    entryInspectionFindingDataVo.setProduitUtilise(inspectionReport.getProduitUtilise());
                    entryInspectionFindingDataVo.setDosage(inspectionReport.getDosage());
//                    entryInspectionFindingDataVo.setEnvironnementStockage(inspectionReport.getEnvironnementStockage());
                    entryInspectionFindingDataVo.setEnvironnementTransport(inspectionReport.getEnvironnementTransport());
                    entryInspectionFindingDataVo.setConditionClimatique(inspectionReport.getConditionClimatique());
                    entryInspectionFindingDataVo.setMesureProtection(inspectionReport.getMesureProtection());
                    entryInspectionFindingDataVo.setObservations(inspectionReport.getObservations());
                    entryInspectionFindingDataVo.setNumeroDecisionPVI(null);
                    entryInspectionFindingDataVo.setArticleReglemente(inspectionReport.getArticleReglemente());
                    entryInspectionFindingDataVo.setExporterName(inspectionReport.getItemFlow().getFileItem().getFile().getClient().getCompanyName());
                    entryInspectionFindingDataVo.setPviCouvertDoc(inspectionReport.isPviCouvertDoc());
                    entryInspectionFindingDataVo.setPviDestination(inspectionReport.getPviDestination());
                    entryInspectionFindingDataVo.setPviDispositionsPrises(inspectionReport.getPviDispositionsPrises());
                    entryInspectionFindingDataVo.setPviNatureArticleInspecte(inspectionReport.getPviNatureArticleInspecte());
                    entryInspectionFindingDataVo.setPviQuantite(inspectionReport.getItemFlow().getFileItem().getQuantity());
                    entryInspectionFindingDataVo.setPviReference(inspectionReport.getItemFlow().getFileItem().getFile().getNumeroDossier());
                    entryInspectionFindingDataVo.setPviSituationArticle(inspectionReport.getPviSituationArticle());
                    for (FileFieldValue fileFieldValue : inspectionReport.getFileItem().getFile().getFileFieldValueList()) {
                        if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("DESTINATAIRE_RAISONSOCIALE")) {
                            entryInspectionFindingDataVo.setRecipient(fileFieldValue.getValue());
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
                    for (FileItemFieldValue fileItemFieldValue : inspectionReport.getFileItem().getFileItemFieldValueList()) {
                        if ("UNITE".equals(fileItemFieldValue.getFileItemField().getCode()) && StringUtils.isNotBlank(fileItemFieldValue.getValue())) {
                            entryInspectionFindingDataVo.setPviQuantite(entryInspectionFindingDataVo.getPviQuantite() + " " + fileItemFieldValue.getValue());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JRBeanCollectionDataSource(Collections.singleton(entryInspectionFindingDataVo));
    }

    /**
     * Gets the inspection report.
     *
     * @return the inspection report
     */
    public InspectionReport getInspectionReport() {
        return inspectionReport;
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
