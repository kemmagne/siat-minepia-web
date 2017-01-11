/*
 *
 */
package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.web.reports.vo.AieMinaderFileItemVo;
import org.guce.siat.web.reports.vo.AieMinaderFileVo;



/**
 * The Class AieMinaderExporter.
 */
public class AieMinaderExporter extends AbstractReportInvoker
{

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new aie minader exporter.
	 *
	 * @param file
	 *           the file
	 */
	public AieMinaderExporter(final File file)
	{
		super("AIE_MINADER", "AIE_MINADER");
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
		final AieMinaderFileVo aieMinaderFileVo = new AieMinaderFileVo();

		aieMinaderFileVo.setDecisionDate(file.getSignatureDate());


		if ((file != null))
		{
			aieMinaderFileVo.setDecisionPlace(file.getBureau().getAddress());
			aieMinaderFileVo.setImporter(file.getClient().getCompanyName());

			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "NUMERO_AIE_MINADER":
							aieMinaderFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}

			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<AieMinaderFileItemVo> fileItemVos = new ArrayList<AieMinaderFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final AieMinaderFileItemVo fileItemVo = new AieMinaderFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
					fileItemVo.setUseArea("zone d'utilisation");

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "USAGE":
									fileItemVo.setUse(fileItemFieldValue.getValue());
									break;
								case "TYPE_FORMULATION":
									fileItemVo.setFormulationType(fileItemFieldValue.getValue());
									break;
								case "MATIERE_ACTIVE":
									fileItemVo.setActiveSubstance(fileItemFieldValue.getValue());
									break;
								case "CONCENTRATION":
									fileItemVo.setConcentration(fileItemFieldValue.getValue());
									break;
								case "DOSES_UTILISATION":
									fileItemVo.setTreatmentDose(fileItemFieldValue.getValue());
									break;
								case "NOM_COMMERCIAL":
									fileItemVo.setTradeName(fileItemFieldValue.getValue());
									break;
								default:
									break;
							}
						}
					}

					fileItemVos.add(fileItemVo);
				}
			}

			aieMinaderFileVo.setFileItemList(fileItemVos);

		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(aieMinaderFileVo));

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
