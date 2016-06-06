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
import org.guce.siat.web.reports.vo.VtdMinsanteFileItemVo;
import org.guce.siat.web.reports.vo.VtdMinsanteFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class VtdMinsanteExporter.
 */
public class VtdMinsanteExporter extends AbstractReportInvoker
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(VtdMinsanteExporter.class);

	/** The file. */
	private final File file;


	/**
	 * Instantiates a new vtd minsante exporter.
	 *
	 * @param file
	 *           the file
	 */
	public VtdMinsanteExporter(final File file)
	{
		super("VTD_MINSANTE", "VTD_MINSANTE");
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

		final VtdMinsanteFileVo vtdMinsanteVo = new VtdMinsanteFileVo();
		vtdMinsanteVo.setDecisionDate(Calendar.getInstance().getTime());
		vtdMinsanteVo.setDecisionPlace(file.getBureau().getAddress());
		if ((file != null))
		{
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "NUMERO_VTD_MINSANTE":
							vtdMinsanteVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "FACTURE_NUMFACTURE":
							vtdMinsanteVo.setInvoice(fileFieldValue.getValue());
							break;
						case "PAYS_ORIGINE_LIBELLE":
							vtdMinsanteVo.setCountryOfOrigin(fileFieldValue.getValue());
							break;
						case "PAYS_PROVENANCE_LIBELLE":
							vtdMinsanteVo.setCountryOfProvenance(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR":
							vtdMinsanteVo.setProvider(fileFieldValue.getValue());
							break;
						case "SIGNATAIRE_DATE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								try
								{
									vtdMinsanteVo.setDecisionDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue()));
								}
								catch (final ParseException e)
								{
									LOG.error(Objects.toString(e));
								}
							}
							break;
						case "SIGNATAIRE_LIEU":
							vtdMinsanteVo.setDecisionPlace(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}

			if ((file.getClient() != null))
			{
				vtdMinsanteVo.setImporter(file.getClient().getCompanyName());
				vtdMinsanteVo.setAddress(file.getClient().getFirstAddress());
				vtdMinsanteVo.setProfession(file.getClient().getProfession());
			}


			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<VtdMinsanteFileItemVo> fileItemVos = new ArrayList<VtdMinsanteFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final VtdMinsanteFileItemVo fileItemVo = new VtdMinsanteFileItemVo();
					fileItemVo.setCode(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemCode() : null);
					fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);
					fileItemVo.setQuantity(fileItem.getQuantity() != null ? Double.valueOf(fileItem.getQuantity()) : 0);

					fileItemVos.add(fileItemVo);
				}
			}

			vtdMinsanteVo.setFileItemList(fileItemVos);
		}

		return new JRBeanCollectionDataSource(Collections.singleton(vtdMinsanteVo));
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
