/*
 *
 */
package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.web.reports.vo.AtMinepiaFileItemVo;
import org.guce.siat.web.reports.vo.AtMinepiaFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AtMinepiaExporter.
 */
public class AtMinepiaExporter extends AbstractReportInvoker {

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AtMinepiaExporter.class);

	/**
	 * The file.
	 */
	private final File file;

	/**
	 * Instantiates a new at minepia exporter.
	 *
	 * @param file the file
	 */
	public AtMinepiaExporter(final File file) {
		super("AT_MINEPIA", "AT_MINEPIA");
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource() {
		final AtMinepiaFileVo atMinsanteFileVo = new AtMinepiaFileVo();
		final List<AtMinepiaFileItemVo> fileItemVos = new ArrayList<AtMinepiaFileItemVo>();
		final SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.PATTERN_DDMMYYYY);

		if (file != null) {
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			atMinsanteFileVo.setDecisionDate(file.getSignatureDate());
			atMinsanteFileVo.setDecisionPlace(file.getBureau().getAddress());
			if (file.getClient() != null) {
				atMinsanteFileVo.setImporter(file.getClient().getCompanyName());
			}

			if (file.getCountryOfOrigin() != null) {
				atMinsanteFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
			}

			for (final FileFieldValue fileFieldValue : fileFieldValueList) {
				switch (fileFieldValue.getFileField().getCode()) {
					case "NUMERO_AT_MINEPIA":
						atMinsanteFileVo.setDecisionNumber(fileFieldValue.getValue());
						break;
					case "DATE_VALIDITE_AVIS_TECHNIQUE":
						if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
							try {
								if (file.getClient() != null && file.getClient().getCommerceApprovalObtainedDate() != null) {
									final Date convertedDate = formatter.parse(file.getClient().getCommerceApprovalObtainedDate());
									atMinsanteFileVo.setValidityDate(convertedDate);
								}
							} catch (final ParseException e) {
								LOG.error(Objects.toString(e), e);
							}
						}

						break;

					default:
						break;
				}

			}

			final List<FileItem> fileItemList = file.getFileItemsList();

			if (CollectionUtils.isNotEmpty(fileItemList)) {
				for (final FileItem fileItem : fileItemList) {
					final AtMinepiaFileItemVo fileItemVo = new AtMinepiaFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList)) {
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
							switch (fileItemFieldValue.getFileItemField().getCode()) {
								case "NOM_COMMERCIAL":
									fileItemVo.setDesignation(fileItemFieldValue.getValue());
									break;

								case "QUANTITE_IMPORTEE":
									fileItemVo.setQuantity(Double.valueOf(fileItemFieldValue.getValue()));
									break;

								case "UNITE":
									fileItemVo.setMeasurementUnit(fileItemFieldValue.getValue());
									break;

								default:
									break;
							}
						}
					}
					fileItemVos.add(fileItemVo);
				}
			}
			atMinsanteFileVo.setFileItemList(fileItemVos);
		}

		return new JRBeanCollectionDataSource(Collections.singleton(atMinsanteFileVo));

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
