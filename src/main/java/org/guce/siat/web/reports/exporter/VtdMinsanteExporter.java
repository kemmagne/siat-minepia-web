package org.guce.siat.web.reports.exporter;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.utils.QRCodeUtils;
import org.guce.siat.web.reports.vo.VtdMinsanteFileItemVo;
import org.guce.siat.web.reports.vo.VtdMinsanteFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class VtdMinsanteExporter.
 */
public class VtdMinsanteExporter extends AbstractReportInvoker {

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(VtdMinsanteExporter.class);
	private static final String PREFIX = "/SG/DPML/SDM du ";

	/**
	 * The file.
	 */
	private final File file;

	/**
	 * Instantiates a new vtd minsante exporter.
	 *
	 * @param file the file
	 */
	public VtdMinsanteExporter(final File file) {
		super("VTD_MINSANTE", "VTD_MINSANTE");
		this.file = file;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource() {

		final VtdMinsanteFileVo vtdMinsanteVo = new VtdMinsanteFileVo();
		String bl = null;
		String lta = null;
		String supplierPhoneCode = null;
		String supplierPhoneNumber = null;
		String supplierMobileCode = null;
		String supplierMobileNumber = null;
		String supplierFaxCode = null;
		String supplierFaxNumber = null;
		if ((file != null)) {
			vtdMinsanteVo.setDecisionPlace(file.getBureau().getAddress());
			vtdMinsanteVo.setDecisionDate(file.getSignatureDate());
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			java.util.Date signatoryDate = null;
			
			if (file.getClient() != null){
				vtdMinsanteVo.setClientAddress(file.getClient().getFirstAddress());
				vtdMinsanteVo.setClientCity(file.getClient().getCity());
				if (file.getClient().getCountry() != null)
					vtdMinsanteVo.setClientCountry(file.getClient().getCountry().getCountryName());
				vtdMinsanteVo.setClientInscriptionCode(file.getClient().getCommerceApprovalRegistrationNumberFile());
				vtdMinsanteVo.setClientInscriptionDate(file.getClient().getCommerceApprovalObtainedDate());
				vtdMinsanteVo.setClientInscriptionIssueDate(file.getClient().getCommerceApprovalValidityDate());
				vtdMinsanteVo.setClientMail(file.getClient().getEmail());
				vtdMinsanteVo.setClientName(file.getClient().getCompanyName());
				vtdMinsanteVo.setClientPhone(file.getClient().getPhone() + "/" + file.getClient().getMobile());
				vtdMinsanteVo.setClientPobox(file.getClient().getPostalCode());
				vtdMinsanteVo.setClientTaxpayerNumber(file.getClient().getNumContribuable());
			}		

			if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
				for (final FileFieldValue fileFieldValue : fileFieldValueList) {
					switch (fileFieldValue.getFileField().getCode()) {
						case "NUMERO_VTD_MINSANTE":
							vtdMinsanteVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__NOM_STRUCTURE_PHARMACIEN":
							vtdMinsanteVo.setPharmacy(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__RAISON_SOCIALE":
							vtdMinsanteVo.setPharmacistName(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__ADRESSE__ADRESSE1":
							vtdMinsanteVo.setPharmacyRoad(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__ADRESSE__BP":
							vtdMinsanteVo.setPharmacyPoBox(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__TELEPHONE_FIXE__NUMERO":
							vtdMinsanteVo.setPharmacyTel(fileFieldValue.getValue());
							break;
						case "FACTURE__NUMERO_FACTURE":
							vtdMinsanteVo.setInvoiceNumber(fileFieldValue.getValue());
							break;
						case "FACTURE__DATE_FACTURE":
							vtdMinsanteVo.setInvoiceDate(fileFieldValue.getValue());
							break;
						case "FACTURE__MONTANT_TOTAL":
							vtdMinsanteVo.setInvoiceAmount(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
							vtdMinsanteVo.setLoadingCustomsOffice(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
							vtdMinsanteVo.setTransportMode(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TERMES_VENTE_CODE_DEVISE":
							vtdMinsanteVo.setCurrency(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
							vtdMinsanteVo.setTransportWay(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
							vtdMinsanteVo.setEnterringCustomsOffice(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_RAISONSOCIALE":
							vtdMinsanteVo.setSupplierName(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_ADRESSE1":
							vtdMinsanteVo.setSupplierAddress(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_PAYSADDRESS_NOMPAYS":
							vtdMinsanteVo.setSupplierCountry(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_VILLE":
							vtdMinsanteVo.setSupplierCity(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_BP":
							vtdMinsanteVo.setSupplierPobox(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_ADRESSEELECTRONIQUE":
							vtdMinsanteVo.setSupplierMail(fileFieldValue.getValue());
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
							vtdMinsanteVo.setSignatoryDate(fileFieldValue.getValue());
//							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
//								try {
//									signatoryDate = new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue());
//								} catch (final ParseException e) {
//									LOG.error(Objects.toString(e), e);
//								}
//							}
							break;
						case "DATE_VALIDITE":
							vtdMinsanteVo.setExpirationDate(fileFieldValue.getValue());
							break;

						default:
							break;
					}
				}
				final List<FileItem> fileItemsList = file.getFileItemsList();
			if (CollectionUtils.isNotEmpty(fileItemsList)){
				for (FileItem fileItem : fileItemsList){
					final VtdMinsanteFileItemVo fileItemVo = new VtdMinsanteFileItemVo();
					
					fileItemVo.setQuantity(Double.parseDouble(fileItem.getQuantity()));
					fileItemVo.setFobValue(fileItem.getFobValue());
					for (FileItemFieldValue fileItemFieldValue : fileItem.getFileItemFieldValueList()){
						switch (fileItemFieldValue.getFileItemField().getCode()){
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
					if (fileItem.getNsh() != null)
						fileItemVo.setCode(fileItem.getNsh().getGoodsItemCode());
					if (fileItemVo.getDesignation() == null || StringUtils.isEmpty(fileItemVo.getDesignation())){
						if (fileItem.getNsh() != null){
							fileItemVo.setDesignation(fileItem.getNsh().getGoodsItemDesc());
						}
					}
					vtdMinsanteVo.getFileItemList().add(fileItemVo);
				}
			}
			}
			vtdMinsanteVo.setAttestation(file.getNumeroDemande());
			vtdMinsanteVo.setSupplierPhone(supplierPhoneCode + supplierPhoneNumber + "/" + supplierMobileCode + supplierMobileNumber);
			vtdMinsanteVo.setSupplierFax(supplierFaxCode + supplierFaxNumber);
			vtdMinsanteVo.setBillOfLanding(lta == null ? bl : (lta + (bl == null ? "" : " / " + bl)));
//			vtdMinsanteVo.setAttestation(vtdMinsanteVo.getDecisionNumber()
//					+ PREFIX
//					+ new SimpleDateFormat("dd/MM/yyyy").format(signatoryDate == null ? java.util.Calendar.getInstance().getTime()
//							: signatoryDate));
		}
		
		
		String qrContent = "N° Dossier : " + file.getNumeroDemande() +
				" N° Facture : " + vtdMinsanteVo.getInvoiceNumber() +
				" Date facture : " + vtdMinsanteVo.getInvoiceDate() + 
				" Importateur : " + vtdMinsanteVo.getClientName() + 
				" Fournisseur : " + vtdMinsanteVo.getSupplierName();
		vtdMinsanteVo.setQrCode(new ByteArrayInputStream(QRCodeUtils.generateQR(qrContent, 512)));

		return new JRBeanCollectionDataSource(Collections.singleton(vtdMinsanteVo));
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
