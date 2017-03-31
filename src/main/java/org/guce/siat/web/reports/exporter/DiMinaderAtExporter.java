package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.web.reports.vo.DiMinaderAtFileItemVo;
import org.guce.siat.web.reports.vo.DiMinaderAtFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DiMinaderAtExporter.
 */
public class DiMinaderAtExporter extends AbstractReportInvoker {

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DiMinaderAtExporter.class);

	/**
	 * The file.
	 */
	private final File file;

	/**
	 * Instantiates a new as minader exporter.
	 *
	 * @param file the file
	 */
	public DiMinaderAtExporter(final File file) {
		super("DI_MINADER_AT", "DI_MINADER_AT");
		this.file = file;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource() {
		final DiMinaderAtFileVo diMinaderAtFileVo = new DiMinaderAtFileVo();
		final SimpleDateFormat formatter2 = new SimpleDateFormat(DateUtils.PATTERN_DDMMYYYY);
		final List<DiMinaderAtFileItemVo> fileItemVos = new ArrayList<DiMinaderAtFileItemVo>();

		if (file != null) {
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			diMinaderAtFileVo.setDecisionDate(new Date());
			diMinaderAtFileVo.setDecisionPlace("-");

			if (file.getCountryOfOrigin() != null) {

				diMinaderAtFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
			}

			if (file.getClient() != null) {
				diMinaderAtFileVo.setImporter(file.getClient().getCompanyName());

			}

			if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
				for (final FileFieldValue fileFieldValue : fileFieldValueList) {
					switch (fileFieldValue.getFileField().getCode()) {
						case "NUMERO_DI_MINADER_AT":
							diMinaderAtFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "INFORMATION_ARRIVEE_POINT_ENTREE_UNLOCODE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								diMinaderAtFileVo.setEntryPoint(fileFieldValue.getValue());
							}
							break;

						case "INFORMATION_ARRIVEE_DATE_ARRIVEE_PREVUE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								try {
									final Date convertedDate = formatter2.parse(fileFieldValue.getValue());
									diMinaderAtFileVo.setArrivalDate(convertedDate);
								} catch (final ParseException e) {
									LOG.error(Objects.toString(e),e);
								}
							}
							break;

						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_CODE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								diMinaderAtFileVo.setTransportMode(fileFieldValue.getValue());
							}
							break;

						case "DATE_VALIDITE_DECLARATION":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								try {
									final Date convertedDate = formatter2.parse(fileFieldValue.getValue());
									diMinaderAtFileVo.setValidityDate(convertedDate);
								} catch (final ParseException e) {
									LOG.error(Objects.toString(e),e);
								}
							}
							break;

						case "INFORMATIONS_GENERALES_CERTIFICAT_EXPERTISE_NUMERO":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								diMinaderAtFileVo.setCertificateHolder(fileFieldValue.getValue());
							}
							break;

						case "INFORMATIONS_GENERALES_AGREMENT_REFERENCE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								diMinaderAtFileVo.setReference(fileFieldValue.getValue());
							}
							break;

						case "INFORMATIONS_GENERALES_AGREMENT_DATE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								diMinaderAtFileVo.setReferenceDate(fileFieldValue.getValue());
							}
							break;

						case "INFORMATION_ARRIVEE_DESTINATION_PRODUIT":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								diMinaderAtFileVo.setDestinationCountry(fileFieldValue.getValue());
							}
							break;

						default:
							break;
					}
				}
			}
			final List<FileItem> fileItemList = file.getFileItemsList();

			if (CollectionUtils.isNotEmpty(fileItemList)) {
				for (final FileItem fileItem : fileItemList) {
					final DiMinaderAtFileItemVo fileItemVo = new DiMinaderAtFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
					fileItemVo.setQuantity(Double.valueOf(fileItem.getQuantity()));
					if (CollectionUtils.isNotEmpty(fileItemFieldValueList)) {
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
							switch (fileItemFieldValue.getFileItemField().getCode()) {
								case "MODELE":
									fileItemVo.setDeviceModel(fileItemFieldValue.getValue());
									break;

								case "MARQUE":
									fileItemVo.setDeviceBrand(fileItemFieldValue.getValue());
									break;

								case "NOM_COMMERCIAL":
									fileItemVo.setDeviceName(fileItemFieldValue.getValue());
									break;

								default:
									break;
							}
						}
					}
					fileItemVos.add(fileItemVo);
				}
			}
			diMinaderAtFileVo.setFileItemList(fileItemVos);

		}

		return new JRBeanCollectionDataSource(Collections.singleton(diMinaderAtFileVo));
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
