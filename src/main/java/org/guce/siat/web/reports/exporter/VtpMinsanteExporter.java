package org.guce.siat.web.reports.exporter;

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

		if ((file != null)) {
			vtpMinsanteVo.setDecisionDate(file.getSignatureDate());
			vtpMinsanteVo.setDecisionPlace(file.getBureau().getAddress());
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
				for (final FileFieldValue fileFieldValue : fileFieldValueList) {
					switch (fileFieldValue.getFileField().getCode()) {
						case "NUMERO_VTP_MINSANTE":
							vtpMinsanteVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__NOM_STRUCTURE_PHARMACIEN":
							vtpMinsanteVo.setPharmacy(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__RAISON_SOCIALE":
							vtpMinsanteVo.setPharmacistName(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__ADRESSE__ADRESSE1":
							vtpMinsanteVo.setPharmacyRoad(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__ADRESSE__BP":
							vtpMinsanteVo.setPharmacyPoBox(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__TELEPHONE_FIXE__NUMERO":
							vtpMinsanteVo.setPharmacyTel(fileFieldValue.getValue());
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
						case "PAYS_ORIGINE_LIBELLE":
							vtpMinsanteVo.setCountryOfOrigin(fileFieldValue.getValue());
							break;
						case "PAYS_PROVENANCE_LIBELLE":
							vtpMinsanteVo.setCountryOfProvenance(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_RAISONSOCIALE":
							vtpMinsanteVo.setSupplierName(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
							vtpMinsanteVo.setTransportWay(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
							vtpMinsanteVo.setLoadingCustomsOffice(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
							vtpMinsanteVo.setEnterringCustomsOffice(fileFieldValue.getValue());
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
