package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.web.reports.vo.DiMinaderSFileItemVo;
import org.guce.siat.web.reports.vo.DiMinaderSFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class DiMinaderSExporter.
 */
public class DiMinaderSExporter extends AbstractReportInvoker
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(DiMinaderSExporter.class);

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new di minader s exporter.
	 *
	 * @param file
	 *           the file
	 */
	public DiMinaderSExporter(final File file)
	{
		super("DI_MINADER_S", "DI_MINADER_S");
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
		final DiMinaderSFileVo diMinaderSFileVo = new DiMinaderSFileVo();

		final List<DiMinaderSFileItemVo> fileItemVos = new ArrayList<DiMinaderSFileItemVo>();

		if (file != null)
		{
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			diMinaderSFileVo.setDecisionDate(file.getSignatureDate());
			diMinaderSFileVo.setDecisionPlace(file.getAssignedUser().getAdministration().getLabelFr());

			if (file.getCountryOfOrigin() != null)
			{
				diMinaderSFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
			}

			if ((file.getClient() != null))
			{
				diMinaderSFileVo.setImporter(file.getClient().getCompanyName());
				diMinaderSFileVo.setDeclarantName(file.getClient().getCompanyName());
				diMinaderSFileVo.setDeclarantAddress(file.getClient().getFullAddress());
			}

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "NUMERO_DI_MINADER_S":
							diMinaderSFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_AGREMENT_REFERENCE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderSFileVo.setReference(fileFieldValue.getValue());
							}
							break;

						case "REFERENCE_NUMERO_ARRETE_HOMOLOGATION":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderSFileVo.setApprovalNumber(fileFieldValue.getValue());
							}
							break;

						case "REFERENCE_NOM_DETENTEUR":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderSFileVo.setApprovalHolder(fileFieldValue.getValue());
							}
							break;

						case "INFORMATION_ARRIVEE_DESTINATION_PRODUIT":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderSFileVo.setDestinationCountry(fileFieldValue.getValue());
							}
							break;

						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_CODE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderSFileVo.setTransportMode(fileFieldValue.getValue());
							}
							break;

						case "INFORMATION_ARRIVEE_DATE_ARRIVEE_PREVUE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								try
								{
									diMinaderSFileVo.setArrivalDate(new SimpleDateFormat(DateUtils.PATTERN_DDMMYYYY).parse(fileFieldValue
											.getValue()));
								}
								catch (final ParseException e)
								{
									LOG.error(Objects.toString(e));
								}
							}
							break;

						case "INFORMATION_ARRIVEE_POINT_ENTREE_UNLOCODE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderSFileVo.setEntryPoint(fileFieldValue.getValue());
							}
							break;

						case "DATE_VALIDITE_DECLARATION":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								try
								{
									diMinaderSFileVo.setValidityDate(new SimpleDateFormat(DateUtils.PATTERN_DDMMYYYY).parse(fileFieldValue
											.getValue()));
								}
								catch (final ParseException e)
								{
									LOG.error(Objects.toString(e));
								}
							}
							break;

						default:
							break;
					}
				}
			}
			final List<FileItem> fileItemList = file.getFileItemsList();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final DiMinaderSFileItemVo fileItemVo = new DiMinaderSFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
					fileItemVo.setQuantity(Double.valueOf(fileItem.getQuantity()));
					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
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
			diMinaderSFileVo.setFileItemList(fileItemVos);

		}

		return new JRBeanCollectionDataSource(Collections.singleton(diMinaderSFileVo));
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
