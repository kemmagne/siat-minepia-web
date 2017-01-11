package org.guce.siat.web.reports.exporter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.web.reports.vo.CatMinaderFileVo;


/**
 * The Class CatMinaderExporter.
 */
public class CatMinaderExporter extends AbstractReportInvoker
{

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new cat minader exporter.
	 *
	 * @param file
	 *           the file
	 */
	public CatMinaderExporter(final File file)
	{
		super("CAT_MINADER", "CAT_MINADER");
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
		final CatMinaderFileVo catMinaderFileVo = new CatMinaderFileVo();


		catMinaderFileVo.setDecisionDate(file.getSignatureDate());
		catMinaderFileVo.setDecisionPlace(file.getBureau().getAddress());
		catMinaderFileVo.setSessionDate(file.getSignatureDate());
		catMinaderFileVo.setTreatmentEquipment("Appareil de traitement");


		if (file != null)
		{
			if (file.getClient() != null)
			{
				catMinaderFileVo.setImporter(file.getClient().getCompanyName());
			}

			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{

					switch (fileFieldValue.getFileField().getCode())
					{

						case "NUMERO_CAT_MINADER":
							catMinaderFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "RESULTATS_ESSAIS_APPAREIL_TRAITEMENT_CLASSE_APPAREIL":
							catMinaderFileVo.setClassDevice(fileFieldValue.getValue());
							break;

						default:
							break;
					}
				}
			}


		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(catMinaderFileVo));

		return dataSource;
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
		jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "minader", "jpg"));
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
