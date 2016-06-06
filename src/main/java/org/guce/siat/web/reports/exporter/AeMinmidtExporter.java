/*
 *
 */
package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.web.reports.vo.AeMinmidtFileItemVo;
import org.guce.siat.web.reports.vo.AeMinmidtFileVo;



/**
 * The Class AeMinmidtExporter.
 */
public class AeMinmidtExporter extends AbstractReportInvoker
{

	/** The file. */
	private final File file;



	/**
	 * Instantiates a new ae minmidt exporter.
	 *
	 * @param file
	 *           the file
	 */
	public AeMinmidtExporter(final File file)
	{
		super("AE_MINMIDT", "AE_MINMIDT");
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource()
	{

		final AeMinmidtFileVo aeMinmidtFileVo = new AeMinmidtFileVo();


		aeMinmidtFileVo.setValidityDate(Calendar.getInstance().getTime());
		aeMinmidtFileVo.setDecisionDate(Calendar.getInstance().getTime());
		aeMinmidtFileVo.setDecisionPlace(file.getBureau().getAddress());


		if ((file != null))
		{

			aeMinmidtFileVo.setImporter(file.getClient().getCompanyName());

			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "NUMERO_AE_MINMIDT":
							aeMinmidtFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}

			if (file.getCountryOfOrigin() != null)
			{
				aeMinmidtFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
			}

			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<AeMinmidtFileItemVo> fileItemVos = new ArrayList<AeMinmidtFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final AeMinmidtFileItemVo fileItemVo = new AeMinmidtFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
					fileItemVo.setQuantity(Double.parseDouble(fileItem.getQuantity()));
					fileItemVo.setDesignation(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);
					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
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

			aeMinmidtFileVo.setFileItemList(fileItemVos);

		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(aeMinmidtFileVo));

		return dataSource;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile()
	{
		return file;
	}
}
