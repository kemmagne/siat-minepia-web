package org.guce.siat.web.reports.exporter;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.web.reports.vo.FimexFileVo;


/**
 * The Class FimexExporter.
 */
public class FimexExporter extends AbstractReportInvoker
{


	/** The file. */
	private final File file;





	/**
	 * Instantiates a new fimex exporter.
	 *
	 * @param file
	 *           the file
	 */
	public FimexExporter(final File file)
	{
		super("FIMEX", "FIMEX");
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
		final FimexFileVo fileVo = new FimexFileVo();
		if (file != null)
		{
			fileVo.setRecordId(file.getNumeroDossier());
			fileVo.setDateSignature(Calendar.getInstance().getTime());
			fileVo.setDelegationEn(file.getBureau().getService().getSubDepartment().getOrganism().getLabelEn());
			fileVo.setDelegationFr(file.getBureau().getService().getSubDepartment().getOrganism().getLabelFr());
			fileVo.setValid(file.getAssignedUser().getFirstName() + " " + file.getAssignedUser().getLastName());
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "CLIENT_NUMERO_CONTRIBUABLE":
							fileVo.setNumeroContribuable(fileFieldValue.getValue());
							break;
						case "CLIENT_RAISON_SOCIALE":
							fileVo.setChargerName(fileFieldValue.getValue());
							break;
						case "DONNEES_GENERALES_PATENTE_ACTIVITE_PRINCIPALE":
							fileVo.setActivitePrincipale(fileFieldValue.getValue());
							break;
						case "CLIENT_ADRESSE":
							fileVo.setChargerAddress1(fileFieldValue.getValue());
							break;
						case "CLIENT_CITY":
							fileVo.setChargerCity(fileFieldValue.getValue());
							break;
						case "TYPE_DEMANDE":
						{
							if (fileFieldValue.getValue() != null)
							{
								switch (fileFieldValue.getValue())
								{
									case "T51":
										fileVo.setTitleFr("ATTESTATION D'INSCRIPTION AU FICHIER DES IMPORTATEURS");
										fileVo.setTitleEn("IMPORTER'S FILE REGISTRATION CERTIFICATE");
										break;
									case "T51S":
										fileVo.setTitleFr("AUTORISATION SPECIALE");
										fileVo.setTitleEn("SPECIAL AUTHORIZATION");
										break;
									case "T52":
										fileVo.setTitleFr("ATTESTATION D'INSCRIPTION AU FICHIER DES EXPORTATEURS");
										fileVo.setTitleEn("EXPORTER'S FILE REGISTRATION CERTIFICATE");
										break;
									default:
										break;
								}
							}
						}
						default:
							break;
					}

				}
			}
			Collections.sort(file.getFileItemsList().get(0).getItemFlowsList(), new Comparator<ItemFlow>()
			{
				@Override
				public int compare(final ItemFlow o1, final ItemFlow o2)
				{
					return (o1.getCreated().after(o2.getCreated()) ? -1 : (o1.getCreated().equals(o2.getCreated()) ? 0 : 1));
				}
			});
			fileVo.setSign(file.getFileItemsList().get(0).getItemFlowsList().get(0).getSender().getFirstName() + " "
					+ file.getFileItemsList().get(0).getItemFlowsList().get(0).getSender().getLastName());
		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(fileVo));

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
