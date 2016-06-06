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
import org.guce.siat.web.reports.vo.EhMinaderFileItemVo;
import org.guce.siat.web.reports.vo.EhMinaderFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class EhMinaderExporter.
 */
public class EhMinaderExporter extends AbstractReportInvoker
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(EhMinaderExporter.class);

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new eh minader exporter.
	 *
	 * @param file
	 *           the file
	 */
	public EhMinaderExporter(final File file)
	{
		super("EH_MINADER", "EH_MINADER");
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
		final SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.PATTERN_DDMMYYYY);
		final EhMinaderFileVo ehMinaderFileVo = new EhMinaderFileVo();

		final List<EhMinaderFileItemVo> fileItemVos = new ArrayList<EhMinaderFileItemVo>();

		if ((file != null) && (file.getClient() != null))
		{
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			ehMinaderFileVo.setSessionDate(new Date());
			ehMinaderFileVo.setDecisionDate(new Date());
			ehMinaderFileVo.setDecisionPlace("-");
			ehMinaderFileVo.setReferenceNumber(file.getClient().getCommerceApprovalRegistrationNumberFile());

			try
			{
				if (file.getClient().getCommerceApprovalObtainedDate() != null)
				{
					final Date convertedDate = formatter.parse(file.getClient().getCommerceApprovalObtainedDate());
					ehMinaderFileVo.setReferenceDate(convertedDate);
				}
			}
			catch (final ParseException e)
			{
				LOG.error(Objects.toString(e));
			}

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{

					switch (fileFieldValue.getFileField().getCode())
					{
						case"NUMERO_EH_MINADER":
							ehMinaderFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "REPRESENTANT_CAMEROUN_RAISONSOCIALE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								ehMinaderFileVo.setRepresentativeCameroon(fileFieldValue.getValue());
							}
							break;

						case "FABRICANT_MATIERE_ACTIVE_RAISONSOCIALE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								ehMinaderFileVo.setMaterialManufacturer(fileFieldValue.getValue());
							}

							break;

						case "PROMOTEUR_RAISONSOCIALE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								ehMinaderFileVo.setPropertyDeveloper(fileFieldValue.getValue());
							}

							break;

						case "FORMULATEUR_RAISONSOCIALE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								ehMinaderFileVo.setFormulator(fileFieldValue.getValue());
							}

							break;

						case "NUMERO_EH":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								ehMinaderFileVo.setHomologationNumber(fileFieldValue.getValue());
							}

							break;

						default:
							break;
					}

					if (StringUtils.isNotBlank(file.getClient().getCompanyName()))
					{
						ehMinaderFileVo.setCompany(file.getClient().getCompanyName());
					}
					if (StringUtils.isNotBlank(file.getClient().getFullAddress()))
					{
						ehMinaderFileVo.setCompanyAddress(file.getClient().getFullAddress());
					}
				}

				final List<FileItem> fileItemList = file.getFileItemsList();

				if (CollectionUtils.isNotEmpty(fileItemList))
				{
					for (final FileItem fileItem : fileItemList)
					{
						final EhMinaderFileItemVo fileItemVo = new EhMinaderFileItemVo();

						final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

						if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
						{
							for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
							{
								switch (fileItemFieldValue.getFileItemField().getCode())
								{
									case "NOM_COMMERCIAL":
										fileItemVo.setProductTradeName(fileItemFieldValue.getValue());
										break;

									case "MATIERE_ACTIVE":
										fileItemVo.setActiveMatter(fileItemFieldValue.getValue());
										break;

									case "CONCENTRATION":
										fileItemVo.setConcentration(fileItemFieldValue.getValue());
										break;

									case "TYPE_FORMULATION":
										fileItemVo.setTrainingType(fileItemFieldValue.getValue());
										break;

									case "PAYS_ORIGINE_LIBELLE":
										fileItemVo.setOriginCountry(fileItemFieldValue.getValue());
										break;

									case "PRESENTATION":
										fileItemVo.setPresentation(fileItemFieldValue.getValue());
										break;

									case "SPECIALITE":
										fileItemVo.setSpecialty(fileItemFieldValue.getValue());
										break;

									case "USAGE":
										fileItemVo.setUse(fileItemFieldValue.getValue());
										break;

									case "DOSES_UTILISATION":
										fileItemVo.setUsingDose(fileItemFieldValue.getValue());
										break;

									case "ETIQUETAGE":
										fileItemVo.setLabelling(fileItemFieldValue.getValue());

										break;

									default:
										break;
								}
							}
						}
						fileItemVos.add(fileItemVo);
					}
				}
				ehMinaderFileVo.setFileItemList(fileItemVos);
			}
		}

		return new JRBeanCollectionDataSource(Collections.singleton(ehMinaderFileVo));
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
