package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.guce.siat.web.reports.vo.PivpsrpMinaderFileItemVo;
import org.guce.siat.web.reports.vo.PivpsrpMinaderFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PivpsrpMinaderExporter.
 */
public class PivpsrpMinaderExporter extends AbstractReportInvoker {

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PivpsrpMinaderExporter.class);

	/**
	 * The file.
	 */
	private final File file;

	/**
	 * Instantiates a new as minader exporter.
	 *
	 * @param file the file
	 */
	public PivpsrpMinaderExporter(final File file) {
		super("PIVPSRP_MINADER", "PIVPSRP_MINADER");
		this.file = file;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource() {
		final PivpsrpMinaderFileVo pivpsrpMinaderFileVo = new PivpsrpMinaderFileVo();

		pivpsrpMinaderFileVo.setArrivalDate(Calendar.getInstance().getTime());
		pivpsrpMinaderFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
		pivpsrpMinaderFileVo.setSessionDate(Calendar.getInstance().getTime());
		pivpsrpMinaderFileVo.setDecisionDate(file.getSignatureDate());
		pivpsrpMinaderFileVo.setDecisionPlace(file.getBureau().getAddress());
		pivpsrpMinaderFileVo.setReference("REFERENCE");

		if ((file != null)) {
			//"Validité : Valable jusqu'au "+($F{validityDate}!= null? new java.text.SimpleDateFormat("dd MMMM yyyy").format($F{validityDate}) : "-")

			if (file.getClient() != null) {
				pivpsrpMinaderFileVo.setImporter(file.getClient().getCompanyName());
			}

			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
				for (final FileFieldValue fileFieldValue : fileFieldValueList) {

					switch (fileFieldValue.getFileField().getCode()) {
						case "NUMERO_PIVPSRP_MINADER":
							pivpsrpMinaderFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_POINT_ENTREE_LIBELLE":
							pivpsrpMinaderFileVo.setEntryPoint(fileFieldValue.getValue());
							break;

						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
							pivpsrpMinaderFileVo.setTransportMode(fileFieldValue.getValue());
							break;

						case "FOURNISSEUR_ADRESSE_ADRESSE1":
							pivpsrpMinaderFileVo.setSupplierAddress("Address 1/Adresse1:" + fileFieldValue.getValue());
							break;

						case "FOURNISSEUR_ADRESSE_ADRESSE2":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								final String supplierAddress1 = pivpsrpMinaderFileVo.getSupplierAddress();
								pivpsrpMinaderFileVo.setSupplierAddress(supplierAddress1 + "/" + "Address 2/Adresse2:"
										+ fileFieldValue.getValue());
							}
							break;

						case "DATE_VALIDITE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								try {
									pivpsrpMinaderFileVo.setValidityDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue
											.getValue()));
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

			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<PivpsrpMinaderFileItemVo> fileItemVos = new ArrayList<PivpsrpMinaderFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList)) {
				for (final FileItem fileItem : fileItemList) {
					final PivpsrpMinaderFileItemVo fileItemVo = new PivpsrpMinaderFileItemVo();

					fileItemVo.setQuantity(Double.parseDouble("7582"));
					fileItemVo.setHarmfulOrganism("HARMFULORGANISM");
					fileItemVo.setDesignation(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList)) {
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {

							switch (fileItemFieldValue.getFileItemField().getCode()) {
								case "NOM_COMMERCIAL":
									fileItemVo.setName(fileItemFieldValue.getValue());
									break;
								case "CONDITIONNEMENT_LIBELLE":
									fileItemVo.setPackaging(fileItemFieldValue.getValue());
									break;

								default:
									break;
							}
						}
					}

					fileItemVos.add(fileItemVo);
				}
			}

			pivpsrpMinaderFileVo.setFileItemList(fileItemVos);

		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(pivpsrpMinaderFileVo));

		return dataSource;

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

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

}
