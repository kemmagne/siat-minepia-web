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
		if ((file != null)) {
			vtdMinsanteVo.setDecisionPlace(file.getBureau().getAddress());
			vtdMinsanteVo.setDecisionDate(file.getSignatureDate());
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			java.util.Date signatoryDate = null;

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
							vtdMinsanteVo.setTransportWay(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
							vtdMinsanteVo.setEnterringCustomsOffice(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_RAISONSOCIALE":
							vtdMinsanteVo.setSupplierName(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_NUM_CONNAISSEMENT_LTA":
							lta = fileFieldValue.getValue();
							break;
						case "INFORMATIONS_GENERALES_LETTRE_TRANSPORT_DATE_LETTRE_TRANSPORT":
							bl = fileFieldValue.getValue();
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

						default:
							break;
					}
				}
			}
			vtdMinsanteVo.setBillOfLanding(lta == null ? bl : (lta + (bl == null ? "" : " / " + bl)));
			vtdMinsanteVo.setAttestation(vtdMinsanteVo.getDecisionNumber()
					+ PREFIX
					+ new SimpleDateFormat("dd/MM/yyyy").format(signatoryDate == null ? java.util.Calendar.getInstance().getTime()
							: signatoryDate));
		}

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
