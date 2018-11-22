package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.guce.siat.web.reports.vo.VtpMinsanteFileItemVo;
import org.guce.siat.web.reports.vo.VtpMinsanteFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class VtpMinsanteExporter.
 */
public class VtpMinsanteExporter extends AbstractReportInvoker {

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(VtpMinsanteExporter.class);
	private static final String PREFIX = "/SG/DPML/SDM du ";
	
	/**
	 * The file.
	 */
	private final File file;

	/**
	 * Instantiates a new vtp minsante exporter.
	 *
	 * @param file the file
	 */
	public VtpMinsanteExporter(final File file) {
		super("VTP_MINSANTE", "VTP_MINSANTE");
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource() {

		final VtpMinsanteFileVo vtpMinsanteVo = new VtpMinsanteFileVo();
		java.util.Date signatoryDate = null;
		String supplierPhoneCode = null;
		String supplierPhoneNumber = null;
		String supplierMobileCode = null;
		String supplierMobileNumber = null;
		String supplierFaxCode = null;
		String supplierFaxNumber = null;
		if ((file != null)) {
			vtpMinsanteVo.setDecisionDate(file.getSignatureDate());
			vtpMinsanteVo.setDecisionPlace(file.getBureau().getAddress());
			vtpMinsanteVo.setFileItemList(new ArrayList<VtpMinsanteFileItemVo>());
			if (file.getClient() != null){
				vtpMinsanteVo.setClientAddress(file.getClient().getFirstAddress());
				vtpMinsanteVo.setClientCity(file.getClient().getCity());
				if (file.getClient().getCountry() != null)
					vtpMinsanteVo.setClientCountry(file.getClient().getCountry().getCountryName());
				vtpMinsanteVo.setClientInscriptionCode(file.getClient().getCommerceApprovalRegistrationNumberFile());
				vtpMinsanteVo.setClientInscriptionDate(file.getClient().getCommerceApprovalObtainedDate());
				vtpMinsanteVo.setClientInscriptionIssueDate(file.getClient().getCommerceApprovalValidityDate());
				vtpMinsanteVo.setClientMail(file.getClient().getEmail());
				vtpMinsanteVo.setClientName(file.getClient().getCompanyName());
				vtpMinsanteVo.setClientPhone(file.getClient().getPhone() + "/" + file.getClient().getMobile());
				vtpMinsanteVo.setClientPobox(file.getClient().getPostalCode());
				vtpMinsanteVo.setClientTaxpayerNumber(file.getClient().getNumContribuable());
			}			
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
				for (final FileFieldValue fileFieldValue : fileFieldValueList) {
					switch (fileFieldValue.getFileField().getCode()) {
						case "NUMERO_VTP_MINSANTE":
							vtpMinsanteVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_RAISONSOCIALE":
							vtpMinsanteVo.setSupplierName(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_ADRESSE1":
							vtpMinsanteVo.setSupplierAddress(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_PAYSADDRESS_NOMPAYS":
							vtpMinsanteVo.setSupplierCountry(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_VILLE":
							vtpMinsanteVo.setSupplierCity(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_BP":
							vtpMinsanteVo.setSupplierPobox(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_ADRESSEELECTRONIQUE":
							vtpMinsanteVo.setSupplierMail(fileFieldValue.getValue());
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
						case "FACTURE__NUMERO_FACTURE":
							vtpMinsanteVo.setInvoiceNumber(fileFieldValue.getValue());
							break;
						case "FACTURE__DATE_FACTURE":
							vtpMinsanteVo.setInvoiceDate(fileFieldValue.getValue());
							break;
						case "FACTURE__MONTANT_TOTAL":
							vtpMinsanteVo.setInvoiceAmount(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
							vtpMinsanteVo.setTransportWay(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
							vtpMinsanteVo.setCustomPlace(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TERMES_VENTE_CODE_DEVISE":
							vtpMinsanteVo.setCurrency(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TERMES_VENTE_MONTANT":
							vtpMinsanteVo.setTotalAmount(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TERMES_VENTE_MONTANT_TOTAL_DEVISE":
							vtpMinsanteVo.setFobCurrencyValue(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TERMES_MONTANT_TOTAL_FCFA":
							vtpMinsanteVo.setFobCfaValue(fileFieldValue.getValue());
							break;
						case "PAYS_ORIGINE_LIBELLE":
							vtpMinsanteVo.setOriginCountry(fileFieldValue.getValue());
							break;
						case "PAYS_PROVENANCE_LIBELLE":
							vtpMinsanteVo.setDestinationCountry(fileFieldValue.getValue());
							break;
						case "SIGNATAIRE_DATE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								try {
									signatoryDate = new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue());
								} catch (final ParseException e) {
									LOG.error(Objects.toString(e), e);
								}
							}

							break;
						case "SIGNATAIRE_LIEU":
							vtpMinsanteVo.setDecisionPlace(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}
			vtpMinsanteVo.setAttestation(vtpMinsanteVo.getDecisionNumber()
					+ PREFIX
					+ new SimpleDateFormat("dd/MM/yyyy").format(signatoryDate == null ? java.util.Calendar.getInstance().getTime()
							: signatoryDate));
			vtpMinsanteVo.setSupplierPhone(supplierPhoneCode + supplierPhoneNumber + "/" + supplierMobileCode + supplierMobileNumber);
			vtpMinsanteVo.setSupplierFax(supplierFaxCode + supplierFaxNumber);
			if (vtpMinsanteVo.getInvoiceAmount() == null){
				vtpMinsanteVo.setInvoiceAmount(vtpMinsanteVo.getFobCfaValue());
			}
			
			final List<FileItem> fileItemsList = file.getFileItemsList();
			if (CollectionUtils.isNotEmpty(fileItemsList)){
				for (FileItem fileItem : fileItemsList){
					final VtpMinsanteFileItemVo fileItemVo = new VtpMinsanteFileItemVo();
					
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
					vtpMinsanteVo.getFileItemList().add(fileItemVo);
				}
			}
		
		}

		return new JRBeanCollectionDataSource(Collections.singleton(vtpMinsanteVo));
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
