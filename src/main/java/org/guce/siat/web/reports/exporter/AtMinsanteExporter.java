/*
 *
 */
package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.web.reports.vo.AtMinsanteFileItemVo;
import org.guce.siat.web.reports.vo.AtMinsanteFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AtMinsanteExporter.
 */
public class AtMinsanteExporter extends AbstractReportInvoker {

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AtMinsanteExporter.class);

	/**
	 * The file.
	 */
	private final File file;

	/**
	 * Instantiates a new at minsante exporter.
	 *
	 * @param file the file
	 */
	public AtMinsanteExporter(final File file) {
		super("AT_MINSANTE", "AT_MINSANTE");
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource() {
		final AtMinsanteFileVo atMinsanteFileVo = new AtMinsanteFileVo();

		final List<AtMinsanteFileItemVo> fileItemVos = new ArrayList<AtMinsanteFileItemVo>();

		if ((file != null)) {

			atMinsanteFileVo.setDecisionDate(file.getSignatureDate());
			atMinsanteFileVo.setDecisionPlace(file.getAssignedUser().getAdministration().getLabelFr());

			if (file.getClient() != null) {
				atMinsanteFileVo.setImporter(file.getClient().getCompanyName());
			}

			if (file.getCountryOfOrigin() != null) {
				atMinsanteFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
			}

			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			for (final FileFieldValue fileFieldValue : fileFieldValueList) {
				switch (fileFieldValue.getFileField().getCode()) {
					case "NUMERO_AT_MINSANTE":
						atMinsanteFileVo.setDecisionNumber(fileFieldValue.getValue());
						break;
					case "DATE_VALIDITE_AVIS_TECHNIQUE":
						if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
							try {
								if (file.getClient() != null && file.getClient().getCommerceApprovalObtainedDate() != null) {
									atMinsanteFileVo.setValidityDate(new SimpleDateFormat(DateUtils.PATTERN_DDMMYYYY).parse(file
											.getClient().getCommerceApprovalObtainedDate()));
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
					final AtMinsanteFileItemVo fileItemVo = new AtMinsanteFileItemVo();

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
