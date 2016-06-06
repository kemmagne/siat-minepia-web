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
import org.guce.siat.web.reports.vo.CeaMinmidtFileItemVo;
import org.guce.siat.web.reports.vo.CeaMinmidtFileVo;



/**
 * The Class CeaMinmidtExporter.
 */
public class CeaMinmidtExporter extends AbstractReportInvoker
{

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new cea minmidt exporter.
	 *
	 * @param file
	 *           the file
	 */
	public CeaMinmidtExporter(final File file)
	{
		super("CEA_MINMIDT", "CEA_MINMIDT");
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
		final CeaMinmidtFileVo ceaMinmidtFileVo = new CeaMinmidtFileVo();



		ceaMinmidtFileVo.setDecreeNumber("decreeNumber: 552235");
		ceaMinmidtFileVo.setDecreeDate(Calendar.getInstance().getTime());
		ceaMinmidtFileVo.setDecisionDate(Calendar.getInstance().getTime());
		ceaMinmidtFileVo.setDecisionPlace(file.getBureau().getAddress());


		if (file != null)
		{
			ceaMinmidtFileVo.setCompany(file.getClient().getCompanyName());
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{

					switch (fileFieldValue.getFileField().getCode())
					{
						case "NUMERO_CEA_MINMIDT":
							ceaMinmidtFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
							ceaMinmidtFileVo.setDestinationCountry(fileFieldValue.getValue());
							break;

						default:
							break;
					}
				}
			}


			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<CeaMinmidtFileItemVo> fileItemVos = new ArrayList<CeaMinmidtFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final CeaMinmidtFileItemVo fileItemVo = new CeaMinmidtFileItemVo();

					fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{

							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "POIDS":
									fileItemVo.setWeight(Double.parseDouble(fileItemFieldValue.getValue()));
									break;
								case "TENEUR":
									fileItemVo.setGoldContent(fileItemFieldValue.getValue());
									break;
								case "NOM_COMMERCIAL":
									fileItemVo.setName(fileItemFieldValue.getValue());
									break;

								default:
									break;
							}
						}
					}

					fileItemVos.add(fileItemVo);
				}
			}
			ceaMinmidtFileVo.setFileItemList(fileItemVos);

		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(ceaMinmidtFileVo));

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
