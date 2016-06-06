package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.guce.siat.web.reports.vo.VtpMinsanteFileItemVo;
import org.guce.siat.web.reports.vo.VtpMinsanteFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class VtpMinsanteExporter.
 */
public class VtpMinsanteExporter extends AbstractReportInvoker
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(VtpMinsanteExporter.class);

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new vtp minsante exporter.
	 *
	 * @param file
	 *           the file
	 */
	public VtpMinsanteExporter(final File file)
	{
		super("VTP_MINSANTE", "VTP_MINSANTE");
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

		final VtpMinsanteFileVo vtpMinsanteVo = new VtpMinsanteFileVo();
		vtpMinsanteVo.setDecisionDate(Calendar.getInstance().getTime());

		if ((file != null))
		{
			vtpMinsanteVo.setDecisionPlace(file.getBureau().getAddress());
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "NUMERO_VTP_MINSANTE":
							vtpMinsanteVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_PERMIS_EXPLOITATION_NUMERO_FACTURE":
							vtpMinsanteVo.setInvoice(fileFieldValue.getValue());
							break;
						case "PAYS_ORIGINE_LIBELLE":
							vtpMinsanteVo.setCountryOfOrigin(fileFieldValue.getValue());
							break;
						case "PAYS_PROVENANCE_LIBELLE":
							vtpMinsanteVo.setCountryOfProvenance(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_RAISONSOCIALE":
							vtpMinsanteVo.setProvider(fileFieldValue.getValue());
							break;
						case "SIGNATAIRE_DATE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								try
								{
									vtpMinsanteVo.setDecisionDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue()));
								}
								catch (final ParseException e)
								{
									LOG.error(Objects.toString(e));
								}
							}

							break;
						case "SIGNATAIRE_LIEU":
							vtpMinsanteVo.setDecisionPlace(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}


			if ((file.getClient() != null))
			{
				vtpMinsanteVo.setImporter(file.getClient().getCompanyName());
				vtpMinsanteVo.setAddress(file.getClient().getFirstAddress());
				vtpMinsanteVo.setProfession(file.getClient().getProfession());
			}

			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<VtpMinsanteFileItemVo> fileItemVos = new ArrayList<VtpMinsanteFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final VtpMinsanteFileItemVo fileItemVo = new VtpMinsanteFileItemVo();
					fileItemVo.setCode(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemCode() : null);
					fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);
					fileItemVo.setQuantity(Double.valueOf(fileItem.getQuantity()));

					fileItemVos.add(fileItemVo);
				}
			}

			vtpMinsanteVo.setFileItemList(fileItemVos);
		}

		return new JRBeanCollectionDataSource(Collections.singleton(vtpMinsanteVo));
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
		jRParameters.put("MINSANTE_LOGO", getRealPath(IMAGES_PATH, "minsante", "jpg"));
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
