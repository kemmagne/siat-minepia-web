package org.guce.siat.web.reports.exporter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.utils.QRCodeGenerator;
import org.guce.siat.web.reports.vo.CcsMinsanteFileItemVo;
import org.guce.siat.web.reports.vo.CcsMinsanteFileVo;

/**
 * The Class CcsMinsanteExporter.
 */
public class CcsMinsanteExporter extends AbstractReportInvoker {

    /**
     * The file.
     */
    private final File file;

    /**
     * Instantiates a new ccs minsante exporter.
     *
     * @param file the file
     */
    public CcsMinsanteExporter(final File file) {
        super("CCS_MINSANTE", "CCS_MINSANTE");
        this.file = file;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final CcsMinsanteFileVo ccsMinsanteVo = new CcsMinsanteFileVo();
        String bl = null;
        String lta = null;
        String supplierPhoneCode = null;
        String supplierPhoneNumber = null;
        String supplierMobileCode = null;
        String supplierMobileNumber = null;
        String supplierFaxCode = null;
        String supplierFaxNumber = null;
        if ((file != null)) {
            ccsMinsanteVo.setDecisionPlace(file.getBureau().getAddress());
            ccsMinsanteVo.setDecisionDate(file.getSignatureDate());
            final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
//			java.util.Date signatoryDate = null;

            if (file.getClient() != null) {
                ccsMinsanteVo.setClientAddress(file.getClient().getFirstAddress());
                ccsMinsanteVo.setClientCity(file.getClient().getCity());
                ccsMinsanteVo.setFileItemList(new ArrayList<CcsMinsanteFileItemVo>());
                if (file.getClient().getCountry() != null) {
                    ccsMinsanteVo.setClientCountry(file.getClient().getCountry().getCountryName());
                }
                ccsMinsanteVo.setClientInscriptionCode(file.getClient().getCommerceApprovalRegistrationNumberFile());
                ccsMinsanteVo.setClientInscriptionDate(file.getClient().getCommerceApprovalObtainedDate());
                ccsMinsanteVo.setClientInscriptionIssueDate(file.getClient().getCommerceApprovalValidityDate());
                ccsMinsanteVo.setClientMail(file.getClient().getEmail());
                ccsMinsanteVo.setClientName(file.getClient().getCompanyName());
                ccsMinsanteVo.setClientPhone(file.getClient().getPhone() + "/" + file.getClient().getMobile());
                ccsMinsanteVo.setClientPobox(file.getClient().getPostalCode());
                ccsMinsanteVo.setClientTaxpayerNumber(file.getClient().getNumContribuable());
            }

            if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
                for (final FileFieldValue fileFieldValue : fileFieldValueList) {
                    switch (fileFieldValue.getFileField().getCode()) {
                        case "NUMERO_DI":
                            ccsMinsanteVo.setDiNumber(fileFieldValue.getValue());
                            break;
                        case "NUMERO_VTP":
                            ccsMinsanteVo.setVtpNumber(fileFieldValue.getValue());
                            break;
                        case "NUMERO_CCS_MINSANTE":
                            ccsMinsanteVo.setDecisionNumber(fileFieldValue.getValue());
                            break;
                        case "PHARMACIEN__NOM_STRUCTURE_PHARMACIEN":
                            ccsMinsanteVo.setPharmacy(fileFieldValue.getValue());
                            break;
                        case "PHARMACIEN__RAISON_SOCIALE":
                            ccsMinsanteVo.setPharmacistName(fileFieldValue.getValue());
                            break;
                        case "PHARMACIEN__ADRESSE__ADRESSE1":
                            ccsMinsanteVo.setPharmacyRoad(fileFieldValue.getValue());
                            break;
                        case "PHARMACIEN__ADRESSE__BP":
                            ccsMinsanteVo.setPharmacyPoBox(fileFieldValue.getValue());
                            break;
                        case "PHARMACIEN__TELEPHONE_FIXE__NUMERO":
                            ccsMinsanteVo.setPharmacyTel(fileFieldValue.getValue());
                            break;
                        case "FACTURE__NUMERO_FACTURE":
                            ccsMinsanteVo.setInvoiceNumber(fileFieldValue.getValue());
                            break;
                        case "FACTURE__DATE_FACTURE":
                            ccsMinsanteVo.setInvoiceDate(fileFieldValue.getValue());
                            break;
                        case "FACTURE__MONTANT_TOTAL":
                            ccsMinsanteVo.setInvoiceAmount(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
                            ccsMinsanteVo.setLoadingCustomsOffice(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
                            ccsMinsanteVo.setTransportMode(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_TERMES_VENTE_CODE_DEVISE":
                            ccsMinsanteVo.setCurrency(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
                            ccsMinsanteVo.setTransportWay(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
                            ccsMinsanteVo.setEnterringCustomsOffice(fileFieldValue.getValue());
                            break;
                        case "FOURNISSEUR_RAISONSOCIALE":
                            ccsMinsanteVo.setSupplierName(fileFieldValue.getValue());
                            break;
                        case "FOURNISSEUR_ADRESSE_ADRESSE1":
                            ccsMinsanteVo.setSupplierAddress(fileFieldValue.getValue());
                            break;
                        case "FOURNISSEUR_ADRESSE_PAYSADDRESS_NOMPAYS":
                            ccsMinsanteVo.setSupplierCountry(fileFieldValue.getValue());
                            break;
                        case "FOURNISSEUR_ADRESSE_VILLE":
                            ccsMinsanteVo.setSupplierCity(fileFieldValue.getValue());
                            break;
                        case "FOURNISSEUR_ADRESSE_BP":
                            ccsMinsanteVo.setSupplierPobox(fileFieldValue.getValue());
                            break;
                        case "FOURNISSEUR_ADRESSE_ADRESSEELECTRONIQUE":
                            ccsMinsanteVo.setSupplierMail(fileFieldValue.getValue());
                            break;
                        case "FOURNISSEUR_TELEPHONE_FIXE_INDICATIFPAYS":
                            supplierPhoneCode = fileFieldValue.getValue();
                            break;
                        case "FOURNISSEUR_TELEPHONE_FIXE_NUMERO":
                            supplierPhoneNumber = fileFieldValue.getValue();
                            break;
                        case "FOURNISSEUR_TELEPHONE_MOBILE_INDICATIFPAYS":
                            supplierMobileCode = fileFieldValue.getValue();
                            break;
                        case "FOURNISSEUR_TELEPHONE_MOBILE_NUMERO":
                            supplierMobileNumber = fileFieldValue.getValue();
                            break;
                        case "FOURNISSEUR_FAX_INDICATIFPAYS":
                            supplierFaxCode = fileFieldValue.getValue();
                            break;
                        case "FOURNISSEUR_FAX_NUMERO":
                            supplierFaxNumber = fileFieldValue.getValue();
                            break;
                        case "INFORMATIONS_GENERALES_TRANSPORT_NUM_CONNAISSEMENT_LTA":
                            lta = fileFieldValue.getValue();
                            break;
                        case "INFORMATIONS_GENERALES_LETTRE_TRANSPORT_DATE_LETTRE_TRANSPORT":
                            bl = fileFieldValue.getValue();
                            break;
                        case "SIGNATAIRE_DATE":
                            ccsMinsanteVo.setSignatoryDate(fileFieldValue.getValue());
//							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
//								try {
//									signatoryDate = new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue());
//								} catch (final ParseException e) {
//									LOG.error(Objects.toString(e), e);
//								}
//							}
                            break;
                        case "DATE_VALIDITE":
                            ccsMinsanteVo.setExpirationDate(fileFieldValue.getValue());
                            break;

                        default:
                            break;
                    }
                }
                final List<FileItem> fileItemsList = file.getFileItemsList();
                if (CollectionUtils.isNotEmpty(fileItemsList)) {
                    for (FileItem fileItem : fileItemsList) {
                        final CcsMinsanteFileItemVo fileItemVo = new CcsMinsanteFileItemVo();

                        fileItemVo.setQuantity(Double.parseDouble(fileItem.getQuantity()));
                        fileItemVo.setFobValue(fileItem.getFobValue());
                        for (FileItemFieldValue fileItemFieldValue : fileItem.getFileItemFieldValueList()) {
                            switch (fileItemFieldValue.getFileItemField().getCode()) {
                                case "AMM":
                                    fileItemVo.setAmm(fileItemFieldValue.getValue());
                                    break;
                                case "UNITE":
                                    fileItemVo.setUnit(fileItemFieldValue.getValue());
                                    break;
                                case "NOM_COMMERCIAL":
                                    fileItemVo.setDesignation(fileItemFieldValue.getValue());
                                    break;
                                default:
                                    break;
                            }
                        }
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
            }
            ccsMinsanteVo.setAttestation(file.getNumeroDemande() + " / " + file.getNumeroDossier());
            ccsMinsanteVo.setSupplierPhone(supplierPhoneCode + supplierPhoneNumber + "/" + supplierMobileCode + supplierMobileNumber);
            ccsMinsanteVo.setSupplierFax(supplierFaxCode + supplierFaxNumber);
            ccsMinsanteVo.setBillOfLanding(lta == null ? bl : (lta + (bl == null ? "" : " / " + bl)));
//			ccsMinsanteVo.setAttestation(ccsMinsanteVo.getDecisionNumber()
//					+ PREFIX
//					+ new SimpleDateFormat("dd/MM/yyyy").format(signatoryDate == null ? java.util.Calendar.getInstance().getTime()
//							: signatoryDate));
        }

        String qrContent = "N° Dossier : " + file.getNumeroDemande()
                + " N° Facture : " + ccsMinsanteVo.getInvoiceNumber()
                + " Date facture : " + ccsMinsanteVo.getInvoiceDate()
                + " Importateur : " + ccsMinsanteVo.getClientName()
                + " Fournisseur : " + ccsMinsanteVo.getSupplierName();
        try {
            ccsMinsanteVo.setQrCode(new ByteArrayInputStream(new QRCodeGenerator().generateQR(qrContent, 512)));
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
        jRParameters.put("MINSANTE_LOGO", getRealPath(IMAGES_PATH, "minsante", "jpg"));
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

}
