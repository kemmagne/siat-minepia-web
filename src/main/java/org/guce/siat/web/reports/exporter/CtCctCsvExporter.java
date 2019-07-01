package org.guce.siat.web.reports.exporter;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.ItemFlowData;
import org.guce.siat.common.model.User;
import org.guce.siat.common.utils.QRCodeUtils;
import org.guce.siat.core.ct.model.ApprovedDecision;
import org.guce.siat.web.reports.vo.CtCctCsvFileItemVo;
import org.guce.siat.web.reports.vo.CtCctCsvFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CtCctCsvExporter.
 */
public class CtCctCsvExporter extends AbstractReportInvoker {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CtCctCsvExporter.class);

    /**
     * The file.
     */
    private final File file;

    private final User user;

    private final ApprovedDecision decision;

    /**
     * Instantiates a new ct cct csv exporter.
     *
     * @param file the file
     * @param connected the user connected
     * @param decision the decision updated by operator
     */
    public CtCctCsvExporter(final File file, User connected, ApprovedDecision decision) {
        super("CT_CCT_CSV", "CT_CCT_CSV");
        this.file = file;
        this.user = connected;
        this.decision = decision;
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final CtCctCsvFileVo ctCctCsvFileVo = new CtCctCsvFileVo();

        if ((file != null)) {
            final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
            ctCctCsvFileVo.setDecisionPlace(file.getBureau().getLabelFr());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (file.getSignatureDate() != null) {
                ctCctCsvFileVo.setDate(sdf.format(file.getSignatureDate()));
            } else {
                ctCctCsvFileVo.setDate(sdf.format(Calendar.getInstance().getTime()));
            }
            ctCctCsvFileVo.setDecisionDate(Calendar.getInstance().getTime());
            if (user != null) {
                ctCctCsvFileVo.setSignatoryName(user.getFirstName());
                ctCctCsvFileVo.setSignatoryPosition(user.getPosition().getLabelFr());
                ctCctCsvFileVo.setVeterinaryAuthority(user.getAdministration().getLabelFr());
            }
            if (ctCctCsvFileVo.getVeterinaryAuthority() == null) {
                ctCctCsvFileVo.setVeterinaryAuthority(file.getBureau().getLabelFr());
            }
            if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
                for (final FileFieldValue fileFieldValue : fileFieldValueList) {
                    switch (fileFieldValue.getFileField().getCode()) {
                        case "CERTIFICATE_NUMBER":
                            if (ctCctCsvFileVo.getCertificateReferenceNumber() == null) {
                                ctCctCsvFileVo.setCertificateReferenceNumber(fileFieldValue.getValue());
                            }
                            break;
                        case "NUMERO_CCT_CT":
                            if (ctCctCsvFileVo.getCertificateReferenceNumber() == null) {
                                ctCctCsvFileVo.setCertificateReferenceNumber(fileFieldValue.getValue());
                            }
                            break;
                        case "CODE_BUREAU":
//                            ctCctCsvFileVo.setVeterinaryAuthority(fileFieldValue.getValue());
                            break;
                        case "PAYS_ORIGINE":
                            ctCctCsvFileVo.setCountryOfOrigin(fileFieldValue.getValue());
                            break;
                        case "PAYS_DESTINATION":
                            ctCctCsvFileVo.setCountryOfDestination(fileFieldValue.getValue());
                            break;
                        case "LIEU_CHARGEMENT":
                            ctCctCsvFileVo.setPlaceOfLoading(fileFieldValue.getValue());
                            break;
                        case "TRANSITAIRE_RAISONSOCIALE":
                            ctCctCsvFileVo.setConsigneeName(fileFieldValue.getValue());
                            break;
                        case "TRANSITAIRE_ADRESSE_ADRESSE1":
                            ctCctCsvFileVo.setConsigneeAddress1(fileFieldValue.getValue());
                            break;
                        case "TRANSITAIRE_TELEPHONE_FIXE_NUMERO":
                            ctCctCsvFileVo.setConsigneeTelephone(fileFieldValue.getValue());
                            break;
                        case "DESTINATAIRE_RAISON_SOCIALE":
                            ctCctCsvFileVo.setConsigneeName(fileFieldValue.getValue());
                            break;
                        case "DESTINATAIRE_ADRESSE_ADRESSE1":
                            ctCctCsvFileVo.setConsigneeAddress1(fileFieldValue.getValue());
                            break;
                        case "DESTINATAIRE_ADRESSE_ADRESSE2":
                            ctCctCsvFileVo.setConsigneeAddress2(fileFieldValue.getValue());
                            break;
                        case "DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
                            ctCctCsvFileVo.setConsigneeCountry(fileFieldValue.getValue());
                            break;
                        case "DESTINATAIRE_TELEPHONE_FIXE_NUMERO":
                            ctCctCsvFileVo.setConsigneeTelephone(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_PAYS_ORIGINE_NOM_PAYS":
                            ctCctCsvFileVo.setCountryOfOrigin(fileFieldValue.getValue());
                            break;
                        case "ZONR_ORIGINE_NOM":
                            ctCctCsvFileVo.setZoneOfOrigin(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_PAYS_DESTINATION_NOM_PAYS":
                            ctCctCsvFileVo.setCountryOfDestination(fileFieldValue.getValue());
                            break;
                        case "ZONE_DESTINATION_NOM":
                            ctCctCsvFileVo.setZoneOfDestination(fileFieldValue.getValue());
                            break;
                        case "LIEU_ORIGINE_NOM":
                            ctCctCsvFileVo.setPlaceOfOriginName(fileFieldValue.getValue());
                            break;
                        case "LIEU_ORIGINE_ADRESSE":
                            ctCctCsvFileVo.setPlaceOfOriginAddress(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
                            ctCctCsvFileVo.setPlaceOfShipment(fileFieldValue.getValue());
                            break;

                        case "EXPEDITION_DATE_EXPEDITION_DATE":
                            if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                                try {
                                    ctCctCsvFileVo.setExpeditionDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue()));
                                } catch (final ParseException e) {
                                    LOG.error(Objects.toString(e), e);
                                }
                            }
                            break;
                        case "EXPEDITION_DATE_EXPEDITION_HEURE":
                            ctCctCsvFileVo.setExpeditionHour(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
                            ctCctCsvFileVo.setModeOfTransport(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
                            ctCctCsvFileVo.setMeansOfTransport(fileFieldValue.getValue());
                            break;
                        case "TRANSPORT_IDENTIFICATION":
                            ctCctCsvFileVo.setTransportIdentification(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_SIGNATAIRE_NOM":
                            ctCctCsvFileVo.setSignatoryName(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_SIGNATAIRE_LIEU":
                            ctCctCsvFileVo.setSignatoryAddress(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_SIGNATAIRE_QUALITE":
                            ctCctCsvFileVo.setSignatoryPosition(fileFieldValue.getValue());
                            break;
                        case "TOTAL_NBR_LOTS_COLIS":
                            ctCctCsvFileVo.setTotalNumberOfPackages(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_TRANSPORT_NUM_CONNAISSEMENT_LTA":
                            ctCctCsvFileVo.setLadingNumberLTA(fileFieldValue.getValue());
                            break;
                        case "DATE_DEPART": {
                            ctCctCsvFileVo.setCvsDepartureDate(fileFieldValue.getValue());
                            break;
                        }
                        case "TEMPERATURE_PRODUIT": {
                            ctCctCsvFileVo.setCvsProductTemperature(fileFieldValue.getValue());
                            break;
                        }
                        case "NOMBRE_UNITES_EMBALLES": {
                            ctCctCsvFileVo.setCvsNbPackagedUnit(fileFieldValue.getValue());
                            break;
                        }
                        case "NATURE_EMBALLAGE": {
                            ctCctCsvFileVo.setCvsPackageNature(fileFieldValue.getValue());
                            break;
                        }
                        case "MARCHANDISE_POUR": {
                            ctCctCsvFileVo.setCvsGoodFor(fileFieldValue.getValue());
                            break;
                        }
                        case "MARCHANDISE_ESPECE": {
                            ctCctCsvFileVo.setCvsGoodSpecies(fileFieldValue.getValue());
                            break;
                        }
                        case "MARCHANDISE_NATURE": {
                            ctCctCsvFileVo.setCvsGoodNature(fileFieldValue.getValue());
                            break;
                        }
                        case "MARCHANDISE_TRAITEMENT": {
                            ctCctCsvFileVo.setCvsGoodTreatment(fileFieldValue.getValue());
                            break;
                        }
                        case "MARCHANDISE_NB_COLIS": {
                            ctCctCsvFileVo.setCvsGoodPackageNumber(fileFieldValue.getValue());
                            break;
                        }
                        case "MARCHANDISE_NB_APPROUVES": {
                            ctCctCsvFileVo.setCvsGoodPackageApproved(fileFieldValue.getValue());
                            break;
                        }
                        case "MARCHANDISE_POIDS_NET": {
                            ctCctCsvFileVo.setCvsGoodPackageNetWeight(fileFieldValue.getValue());
                            break;
                        }
                        case "ID_CONTENEURS_SCELLES": {
                            ctCctCsvFileVo.setCvsIdContainersSeals(fileFieldValue.getValue());
                            break;
                        }
                        case "NO_PERMIS_CITES": {
                            ctCctCsvFileVo.setCvsPermitCITES(fileFieldValue.getValue());
                            break;
                        }
                        case "EXPEDITEUR_RAISON_SOCIAL": {
                            ctCctCsvFileVo.setConsignorName(fileFieldValue.getValue());
                            break;
                        }
                        case "EXPEDITEUR_ADRESSE1": {
                            ctCctCsvFileVo.setConsignorAddress1(fileFieldValue.getValue());
                            break;
                        }
                        case "EXPEDITEUR_TELEPHONE_MOBILE_NUMERO": {
                            ctCctCsvFileVo.setConsignorTelephone(fileFieldValue.getValue());
                            break;
                        }
                        case "CCT_CONTENEURS_CONTENEUR": {
                            String value = fileFieldValue.getValue();
                            ctCctCsvFileVo.setCvsIdContainersSeals(value);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }

            if (StringUtils.isNotBlank(ctCctCsvFileVo.getCvsIdContainersSeals())) {
                final String[] tab1 = ctCctCsvFileVo.getCvsIdContainersSeals().split(";");
                final int size = tab1.length;
                final StringBuilder builder = new StringBuilder();
                for (int i = 1; i < size; i++) {
                    if (StringUtils.isBlank(tab1[i])) {
                        continue;
                    }
                    final String[] tab2 = tab1[i].split(",");
                    builder.append(tab2[0]).append("/").append(tab2[3]).append("; ");
                }
                ctCctCsvFileVo.setCvsIdContainersSeals(builder.substring(0, builder.lastIndexOf(" ")));
            }

            if (decision != null) {
                if (decision.getDepartureDate() != null) {
                    ctCctCsvFileVo.setCvsDepartureDate(sdf.format(decision.getDepartureDate()));
                }
                if (decision.getProductTemperature() != null) {
                    ctCctCsvFileVo.setCvsProductTemperature(decision.getProductTemperature());
                }
                if (decision.getContainerSeals() != null) {
                    ctCctCsvFileVo.setCvsIdContainersSeals(decision.getContainerSeals());
                }
                if (decision.getNumberOfUnitPackaged() != null) {
                    ctCctCsvFileVo.setCvsNbPackagedUnit(decision.getNumberOfUnitPackaged());
                }
                if (decision.getTypeOfPagkaging() != null) {
                    ctCctCsvFileVo.setCvsPackageNature(decision.getTypeOfPagkaging());
                }
                if (decision.getGoodsCertifiedFor() != null) {
                    ctCctCsvFileVo.setCvsGoodFor(decision.getGoodsCertifiedFor());
                }
                if (decision.getGoodsSpecies() != null) {
                    ctCctCsvFileVo.setCvsGoodSpecies(decision.getGoodsSpecies());
                }
                if (decision.getGoodsNature() != null) {
                    ctCctCsvFileVo.setCvsGoodNature(decision.getGoodsNature());
                }
                if (decision.getGoodsTreatment() != null) {
                    ctCctCsvFileVo.setCvsGoodTreatment(decision.getGoodsTreatment());
                }
                if (decision.getGoodsPackageNumber() != null) {
                    ctCctCsvFileVo.setCvsGoodPackageNumber(decision.getGoodsPackageNumber());
                }
                if (decision.getGoodsAgreementReference() != null) {
                    ctCctCsvFileVo.setCvsGoodPackageNetWeight(decision.getGoodsAgreementReference());
                }
                if (decision.getGoodsNetWeight() != null) {
                    ctCctCsvFileVo.setCvsGoodPackageNetWeight(decision.getGoodsNetWeight());
                }
                if (decision.getOfficialPosition() != null) {
                    ctCctCsvFileVo.setSignatoryPosition(decision.getOfficialPosition());
                }
            }
            if (CollectionUtils.isNotEmpty(file.getFileItemsList())) {
                List<ItemFlow> ifs = file.getFileItemsList().get(0).getItemFlowsList();
                if (CollectionUtils.isNotEmpty(ifs)) {
                    List<String> cctCsvAcceptFlowList = new ArrayList<>();
                    cctCsvAcceptFlowList.add("FL_CT_CVS_03");
                    cctCsvAcceptFlowList.add("FL_CT_CVS_07");
                    for (ItemFlow ifl : ifs) {
                        if (cctCsvAcceptFlowList.contains(ifl.getFlow().getCode())) {
                            List<ItemFlowData> itemFlowDataList = ifl.getItemFlowsDataList();

                            for (final ItemFlowData itemFlowData : itemFlowDataList) {
                                switch (itemFlowData.getDataType().getLabel()) {
                                    case "Titre et Qualité":
                                        ctCctCsvFileVo.setSignatoryPosition(itemFlowData.getValue());
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }
                    }
                }

            }

            ctCctCsvFileVo.setDecisionNumber("/MINEP/SG/BNO");

            final List<FileItem> fileItemList = file.getFileItemsList();

            final List<CtCctCsvFileItemVo> fileItemVos = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(fileItemList)) {
                for (final FileItem fileItem : fileItemList) {
                    final CtCctCsvFileItemVo fileItemVo = new CtCctCsvFileItemVo();

                    fileItemVo.setNumberOfPackages(fileItem.getQuantity());
                    fileItemVo.setCode(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemCode() : null);

                    final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

                    if (CollectionUtils.isNotEmpty(fileItemFieldValueList)) {
                        for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
                            switch (fileItemFieldValue.getFileItemField().getCode()) {
                                case "NUMERO_CONTENEUR":
                                    fileItemVo.setContainerNumber(fileItemFieldValue.getValue());
                                    break;
                                case "SPECIFICATION_TECHNIQUE":
                                    fileItemVo.setDesc(fileItemFieldValue.getValue());
                                    break;
//                                case "QUANTITE":
//                                    fileItemVo.setNumberOfPackages(fileItemFieldValue.getValue());
//                                    break;
                                case "QUANTITE_TOTALE":
                                    fileItemVo.setTotalQuantity(fileItemFieldValue.getValue());
                                    break;
//                                case "NBR_LOTS_COLIS":
//                                    fileItemVo.setNumberOfPackages(fileItemFieldValue.getValue());
//                                    break;
                                case "POIDS_NET":
                                    fileItemVo.setNetWeight(fileItemFieldValue.getValue());
                                case "TEMPERATURE":
                                    fileItemVo.setTemperature(fileItemFieldValue.getValue());
                                    break;
                                case "TYPE_COLIS":
                                    fileItemVo.setTypeOfPackaging(fileItemFieldValue.getValue());
                                    break;
                                case "USAGE":
                                    fileItemVo.setUse(fileItemFieldValue.getValue());
                                    break;
                                case "NOM_SCIENTIFIQUE":
                                    fileItemVo.setScientificName(fileItemFieldValue.getValue());
                                    break;
                                case "NATURE":
                                    fileItemVo.setNature(fileItemFieldValue.getValue());
                                    break;
                                case "TYPE_TRAITEMENT":
                                    fileItemVo.setTreatmentType(fileItemFieldValue.getValue());
                                    break;
                                case "ETABLISSEMENTS":
                                    fileItemVo.setApprovedNumberOfEstablishments(fileItemFieldValue.getValue());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    fileItemVos.add(fileItemVo);
                }
            }
            //	ctCctCsvFileVo.setSignatoryName();

            String qrContent = "Nom Importateur : " + ctCctCsvFileVo.getConsigneeName()
                    + " Nom Fournisseur : " + ctCctCsvFileVo.getConsignorName()
                    + " Referencence Dedouanement : " + ctCctCsvFileVo.getCertificateReferenceNumber()
                    + " Numero BL ou LTA : " + ctCctCsvFileVo.getLadingNumberLTA();
            ctCctCsvFileVo.setQrCode(new ByteArrayInputStream(QRCodeUtils.generateQR(qrContent, 512)));

            ctCctCsvFileVo.setFileItemList(fileItemVos);
        }

        return new JRBeanCollectionDataSource(Collections.singleton(ctCctCsvFileVo));
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

}
