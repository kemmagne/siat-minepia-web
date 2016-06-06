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
import org.guce.siat.web.reports.vo.AiMinsanteFileItemVo;
import org.guce.siat.web.reports.vo.AiMinsanteFileVo;


/**
 * The Class AiMinsanteExporter.
 */
public class AiMinsanteExporter extends AbstractReportInvoker
{

	/** The file. */
	private final File file;



	/**
	 * Instantiates a new ai minsante exporter.
	 *
	 * @param file
	 *           the file
	 */
	public AiMinsanteExporter(final File file)
	{
		super("AI_MINSANTE", "AI_MINSANTE");
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
		final AiMinsanteFileVo aiMinsanteFileVo = new AiMinsanteFileVo();

		aiMinsanteFileVo.setValidityDate(Calendar.getInstance().getTime());
		aiMinsanteFileVo.setDecisionDate(Calendar.getInstance().getTime());
		aiMinsanteFileVo.setDecisionPlace(file.getBureau().getAddress());


		if ((file != null))
		{

			aiMinsanteFileVo.setImporter(file.getClient().getCompanyName());

			if (file.getCountryOfOrigin() != null)
			{
				aiMinsanteFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
			}


			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case"NUMERO_AI_MINSANTE":
							aiMinsanteFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}

			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<AiMinsanteFileItemVo> fileItemVos = new ArrayList<AiMinsanteFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final AiMinsanteFileItemVo fileItemVo = new AiMinsanteFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
					fileItemVo.setDesignation(fileItem.getNsh().getGoodsItemDesc());
					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "QUANTITE_IMPORTEE":
									fileItemVo.setQuantity(Double.parseDouble(fileItemFieldValue.getValue()));
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

			aiMinsanteFileVo.setFileItemList(fileItemVos);

		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(aiMinsanteFileVo));

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
