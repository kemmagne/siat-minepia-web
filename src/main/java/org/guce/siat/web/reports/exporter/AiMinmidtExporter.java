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
import org.guce.siat.web.reports.vo.AiMinmidtFileItemVo;
import org.guce.siat.web.reports.vo.AiMinmidtFileVo;


/**
 * The Class AiMinmidtExporter.
 */
public class AiMinmidtExporter extends AbstractReportInvoker
{

	/** The file. */
	private final File file;


	/**
	 * Instantiates a new ai minmidt exporter.
	 *
	 * @param file
	 *           the file
	 */
	public AiMinmidtExporter(final File file)
	{
		super("AI_MINMIDT", "AI_MINMIDT");
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
		final AiMinmidtFileVo aiMinmidtFileVo = new AiMinmidtFileVo();

		aiMinmidtFileVo.setValidityDate(Calendar.getInstance().getTime());
		aiMinmidtFileVo.setDecisionDate(Calendar.getInstance().getTime());
		aiMinmidtFileVo.setDecisionPlace(file.getBureau().getAddress());


		if (file != null)
		{

			aiMinmidtFileVo.setImporter(file.getClient().getCompanyName());

			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case"NUMERO_AI_MINMIDT":
							aiMinmidtFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}

			if (file.getCountryOfOrigin() != null)
			{
				aiMinmidtFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
			}

			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<AiMinmidtFileItemVo> fileItemVos = new ArrayList<AiMinmidtFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final AiMinmidtFileItemVo fileItemVo = new AiMinmidtFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
					fileItemVo.setQuantity(Double.parseDouble(fileItem.getQuantity()));
					fileItemVo.setDesignation(fileItem.getNsh().getGoodsItemDesc());

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

			aiMinmidtFileVo.setFileItemList(fileItemVos);

		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(aiMinmidtFileVo));

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
