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
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.core.ct.model.InspectionController;
import org.guce.siat.core.ct.model.InspectionReport;
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
    protected static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";
    private static final String CONSTAT_BUNDLE_TRUE = "constatBundleTrue";
    private static final String CONSTAT_BUNDLE_FALSE = "constatBundleFalse";

    private final InspectionReport inspectionReport;
    private final String referenceNumber;
    private final File file;

    public CtPviExporter(File file, InspectionReport inspectionReport) {
        super("CCT_CT_E_PVI", "CCT_CT_E_PVI");
        this.file = file;
        this.inspectionReport = inspectionReport;
        this.referenceNumber = null;
    }

    public CtPviExporter(File file, InspectionReport inspectionReport, final String referenceNumber) {
        super("CCT_CT_E_PVI", "CCT_CT_E_PVI");
        this.file = file;
        this.inspectionReport = inspectionReport;
        this.referenceNumber = referenceNumber;
    }

    @Override
    protected JRBeanCollectionDataSource getReportDataSource() {

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
                for (InspectionController inspectionController : inspectionReport.getInspectionControllerList()) {

                    CtCctControllerDataVo controllerDataVo = new CtCctControllerDataVo();

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
            if (file.getDestinataire().equalsIgnoreCase("MINADER")) {
                // Specific field for MINADER
                if (file.getCountryOfProvenance() != null) {
                    entryInspectionFindingDataVo.setNativeCountry(file.getCountryOfProvenance().getCountryName());
                }
                if (file.getCountryOfOrigin() != null) {
                    entryInspectionFindingDataVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
                }
                entryInspectionFindingDataVo.setSender(new StringBuilder(file.getClient().getCompanyName()).append(" ").append(file.getClient().getFullAddress()).toString());
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
                if (!controllerDataVoList.isEmpty()) {
                    entryInspectionFindingDataVo.setSignatairePVIPhyto(controllerDataVoList.get(0).getName());
                } else {
                    entryInspectionFindingDataVo.setSignatairePVIPhyto(inspectionReport.getControllerName());
                }
                entryInspectionFindingDataVo.setSignatureDate(file.getSignatureDate());
                if (file.getSignatory() != null) {
                    entryInspectionFindingDataVo.setSignatory(String.format("%s %s", file.getSignatory().getLastName(), file.getSignatory().getFirstName()));
                }
                entryInspectionFindingDataVo.setSignaturePlace(file.getBureau().getLabelFr());
                entryInspectionFindingDataVo.setTypeTraitement(inspectionReport.getTreatmentTypesList());
                entryInspectionFindingDataVo.setEtatDateDernierTraitement(inspectionReport.getEtatDateDernierTraitement());
                entryInspectionFindingDataVo.setProduitUtilise(inspectionReport.getProduitUtilise());
                entryInspectionFindingDataVo.setDosage(inspectionReport.getDosage());
                entryInspectionFindingDataVo.setEnvironnementStockage(inspectionReport.getStorageEnvsList());
                entryInspectionFindingDataVo.setEnvironnementTransport(inspectionReport.getEnvironnementTransport());
                entryInspectionFindingDataVo.setConditionClimatique(inspectionReport.getConditionClimatique());
                entryInspectionFindingDataVo.setMesureProtection(inspectionReport.getMesureProtection());
                entryInspectionFindingDataVo.setObservations(inspectionReport.getObservations());
                entryInspectionFindingDataVo.setArticleReglemente(inspectionReport.getArticleReglemente());
                entryInspectionFindingDataVo.setExporterName(file.getClient().getCompanyName());
                entryInspectionFindingDataVo.setPviCouvertDoc(inspectionReport.isPviCouvertDoc());
                entryInspectionFindingDataVo.setPviDestination(inspectionReport.getPviDestination());
                entryInspectionFindingDataVo.setPviDispositionsPrises(inspectionReport.getPviDispositionsPrises());
                entryInspectionFindingDataVo.setPviNatureArticleInspecte(inspectionReport.getPviNatureArticleInspecte());
                entryInspectionFindingDataVo.setPviQuantite(inspectionReport.getPviQuantite());
                entryInspectionFindingDataVo.setPviReference(file.getNumeroDossier());
                entryInspectionFindingDataVo.setPviSituationArticle(inspectionReport.getPviSituationArticle());
                entryInspectionFindingDataVo.setInspectionStartDate(inspectionReport.getInspectionStartDate());
                entryInspectionFindingDataVo.setInspectionEndDate(inspectionReport.getInspectionEndDate());

                if (file.getAssignedUser() != null) {
                    entryInspectionFindingDataVo.setInspector(String.format("%s %s", file.getAssignedUser().getLastName(), file.getAssignedUser().getFirstName()));
                }

                for (FileFieldValue fileFieldValue : file.getFileFieldValueList()) {
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
                    if (fileFieldValue.getFileField().getCode().equalsIgnoreCase("NUMERO_CCT_CT_E_PVI")) {
                        entryInspectionFindingDataVo.setNumeroDecisionPVI(fileFieldValue.getValue());
                    }
                    if (fileFieldValue.getFileField().getCode().equals("INFORMATIONS_GENERALES_PAYS_DESTINATION_NOMPAYS")) {
                        if (StringUtils.isBlank(entryInspectionFindingDataVo.getPviDestination())) {
                            entryInspectionFindingDataVo.setPviDestination(fileFieldValue.getValue());
                        }
                    }
                }

                if (this.referenceNumber != null) {
                    entryInspectionFindingDataVo.setNumeroDecisionPVI(referenceNumber);
                }
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
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "phytosanitaire", "jpg"));
        return jRParameters;
    }

}
