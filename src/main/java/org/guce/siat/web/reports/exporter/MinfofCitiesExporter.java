package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.guce.siat.web.reports.vo.MinfofCitiesFileItemVo;
import org.guce.siat.web.reports.vo.MinfofCitiesFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class MinfofCitiesExporter.
 */
public class MinfofCitiesExporter extends AbstractReportInvoker
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(MinfofCitiesExporter.class);

	/** The file. */
	private final File file;


	/**
	 * Instantiates a new minfof cities exporter.
	 *
	 * @param file
	 *           the file
	 */
	public MinfofCitiesExporter(final File file)
	{
		super("MINFOF_CITIES", "MINFOF_CITIES");
		this.file = file;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
	 */
	@Override
	protected JRBeanCollectionDataSource getReportDataSource()
	{
		final MinfofCitiesFileVo minfofCitiesFileVo = new MinfofCitiesFileVo();


		minfofCitiesFileVo.setOperationType("");
		minfofCitiesFileVo.setDecisionDate(Calendar.getInstance().getTime());

		minfofCitiesFileVo.setHolderCountry("holderCountry");
		minfofCitiesFileVo.setSpecialConditions("specialConditions");
		minfofCitiesFileVo.setSpecialConditionsStampNumber("specialConditionsStampNumber");
		minfofCitiesFileVo.setOrganismName("organismName");
		minfofCitiesFileVo.setOrganismAdress("organismAdress");
		minfofCitiesFileVo.setOrganismCountry("organismCountry");
		minfofCitiesFileVo.setLicensePlace("licensePlace");
		minfofCitiesFileVo.setExportApproach("exportApproach");
		minfofCitiesFileVo.setExportationDate(Calendar.getInstance().getTime());


		if ((file != null))
		{

			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{

					switch (fileFieldValue.getFileField().getCode())
					{
						case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
							minfofCitiesFileVo.setExportationPort(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_PERMIS_DELIVREUR_ADRESSE_PAYSADDRESS_NOMPAYS":
							minfofCitiesFileVo.setHolderCountry(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_PERMIS_DELIVREUR_ADRESSE_ADRESSE1":
							minfofCitiesFileVo.setHolderAdress(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_PERMIS_DELIVREUR_RAISONSOCIALE":
							minfofCitiesFileVo.setHolderName(fileFieldValue.getValue());
							break;
						case "NUMERO_CO_CITIES_MINFOF":
							minfofCitiesFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_DESTINATAIRE_RAISONSOCIALE":
							minfofCitiesFileVo.setReceiverName(fileFieldValue.getValue());
							break;

						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE1":
							minfofCitiesFileVo.setReceiverAdress("Address 1/Address1:" + fileFieldValue.getValue());
							break;

						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE2":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								final String receiverAddress1 = minfofCitiesFileVo.getReceiverAdress();
								minfofCitiesFileVo.setReceiverAdress(receiverAddress1 + "/" + "Address 2/Address2:"
										+ fileFieldValue.getValue());
							}
							break;

						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
							minfofCitiesFileVo.setReceiverCountry(fileFieldValue.getValue());
							break;

						case "INFORMATIONS_GENERALES_PERMIS_DATE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								try
								{
									minfofCitiesFileVo.setLicenseDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue()));
								}
								catch (final ParseException e)
								{
									LOG.error(Objects.toString(e));
								}

							}
							break;

						case "INFORMATIONS_GENERALES_TRANSPORT_NUM_CONNAISSEMENT_LTA":
							minfofCitiesFileVo.setLading(fileFieldValue.getValue());
							break;

						default:
							break;
					}
				}
			}


			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<MinfofCitiesFileItemVo> fileItemVos = new ArrayList<MinfofCitiesFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final MinfofCitiesFileItemVo fileItemVo = new MinfofCitiesFileItemVo();

					final Calendar calendarThree = Calendar.getInstance();

					fileItemVo.setBrand("brand");
					fileItemVo.setSource("source");
					fileItemVo.setAnnex("annex");
					fileItemVo.setReportCountry("reportCountry");
					fileItemVo.setLicenceNumber("licenceNumber");
					fileItemVo.setDate(calendarThree.getTime());
					fileItemVo.setQuota("quota");
					fileItemVo.setTotalExports("totalExports");
					fileItemVo.setSamplingCountries("samplingCountries");
					fileItemVo.setNativeCountry("nativeCountry");
					fileItemVo.setLivestockCountries("livestockCountries");
					fileItemVo.setReproductionCountries("reproductionCountries");

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
					fileItemVo.setQuantity(Double.parseDouble(fileItem.getQuantity()));
					fileItemVo.setIdentificationNumber(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{

							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "NOM_COMMERCIAL":
									fileItemVo.setCommonName(fileItemFieldValue.getValue());
									break;
								case "NOM_SCIENTIFIQUE":
									fileItemVo.setScientificName(fileItemFieldValue.getValue());
									break;
								case "FAUNE_AGE_PRODUIT":
									fileItemVo.setAge(fileItemFieldValue.getValue());
									break;
								case "FAUNE_SEXE_PRODUIT":
									fileItemVo.setSex(fileItemFieldValue.getValue());
									break;
								case "PAYS_ORIGINE_NOM_PAYS":
									fileItemVo.setOriginCountry(fileItemFieldValue.getValue());
									break;

								default:
									break;
							}
						}
					}

					fileItemVos.add(fileItemVo);
				}
			}

			minfofCitiesFileVo.setFileItemList(fileItemVos);

		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(minfofCitiesFileVo));

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
