package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.web.reports.vo.CpMinepdedFileItemVo;
import org.guce.siat.web.reports.vo.CpMinepdedFileVo;



/**
 * The Class CpMinepdedExporter.
 */
public class CpMinepdedExporter extends AbstractReportInvoker
{

	/** The file. */
	private final File file;


	/**
	 * Instantiates a new cp minepded exporter.
	 *
	 * @param file
	 *           the file
	 */
	public CpMinepdedExporter(final File file)
	{
		super("CP_MINEPDED", "CP_MINEPDED");
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource()
	{
		final CpMinepdedFileVo cpMinepdedFileVo = new CpMinepdedFileVo();

		final List<CpMinepdedFileItemVo> fileItemVos = new ArrayList<CpMinepdedFileItemVo>();

		if ((file != null))
		{
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			cpMinepdedFileVo.setDecisionDate(file.getSignatureDate());

			for (final FileFieldValue fileFieldValue : fileFieldValueList)
			{

				switch (fileFieldValue.getFileField().getCode())
				{
					case "REPONSE_PAYS_IMPORTATEUR_PAYS_IMPORTATEUR_NOMPAYS":
						if (StringUtils.isNotBlank(fileFieldValue.getValue()))
						{
							cpMinepdedFileVo.setImportingCountry(fileFieldValue.getValue());
						}
						break;

					case "EXPORTATION_AUTORITES_NATIONALES_DESIGNEES_AUTORITES_EXPORTATEUR_ADRESSE_PAYSADDRESS_NOMPAYS":
						if (StringUtils.isNotBlank(fileFieldValue.getValue()))
						{
							cpMinepdedFileVo.setName(fileFieldValue.getValue());
						}
						break;

					case "EXPORTATION_AUTORITES_NATIONALES_DESIGNEES_AUTORITES_EXPORTATEUR_ADRESSE_ADRESSE1":
						if (StringUtils.isNotBlank(fileFieldValue.getValue()))
						{
							cpMinepdedFileVo.setAdresse(fileFieldValue.getValue());
						}
						break;

					case "EXPORTATION_PREPARATION_A_EXPORTER_B_NOTIFICATIONEXPORTATION":
						if (StringUtils.isNotBlank(fileFieldValue.getValue()))
						{
							cpMinepdedFileVo.setReferenceNumber(fileFieldValue.getValue());
						}
						break;

					default:
						break;
				}
			}
			final List<FileItem> fileItemList = file.getFileItemsList();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final CpMinepdedFileItemVo fileItemVo = new CpMinepdedFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "NOM_PRODUIT_APRES_NIR":
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
			cpMinepdedFileVo.setFileItemList(fileItemVos);

		}
		return new JRBeanCollectionDataSource(Collections.singleton(cpMinepdedFileVo));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getJRParameters()
	 */
	@Override
	protected Map<String, Object> getJRParameters()
	{
		final Map<String, Object> jRParameters = super.getJRParameters();
		jRParameters.put("ROTTERDAM_LOGO", getRealPath(IMAGES_PATH, "rotterdam", "jpg"));
		return jRParameters;
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
