/*
 *
 */
package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.core.ct.model.InspectionController;
import org.guce.siat.core.ct.model.InspectionReport;
import org.guce.siat.web.reports.vo.CtCctControllerDataVo;
import org.guce.siat.web.reports.vo.CtCctDataVo;

import com.google.common.base.Objects;




/**
 * The Class CtCctExporter.
 */
public class CtCctExporter extends AbstractReportInvoker
{

	/** The inspection report. */
	private final InspectionReport inspectionReport;
	protected static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";
	private static final String CONSTAT_BUNDLE_TRUE = "constatBundleTrue";
	private static final String CONSTAT_BUNDLE_FALSE = "constatBundleFalse";

	/**
	 * Instantiates a new ct cct exporter.
	 *
	 * @param inspectionReport
	 *           the inspection report
	 */
	public CtCctExporter(final InspectionReport inspectionReport)
	{
		super("CT_CCT", "CT_CCT");
		this.inspectionReport = inspectionReport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource()
	{
		final CtCctDataVo entryInspectionFindingDataVo = new CtCctDataVo();

		if ((inspectionReport != null))
		{
			final List<CtCctControllerDataVo> controllerDataVoList = new ArrayList<CtCctControllerDataVo>();
			//General Information
			if (StringUtils.isNotBlank(inspectionReport.getPlace()))
			{
				entryInspectionFindingDataVo.setPlace(inspectionReport.getPlace());
			}
			if (inspectionReport.getReportDate() != null)
			{
				entryInspectionFindingDataVo.setDate(inspectionReport.getReportDate());
			}
			if (StringUtils.isNotBlank(inspectionReport.getReportTime().toString()))
			{
				entryInspectionFindingDataVo.setHour(inspectionReport.getReportTime().toString());
			}
			//Echantillons
			entryInspectionFindingDataVo.setQuantity(String.valueOf(inspectionReport.getSample().getTakenQuantity()));
			entryInspectionFindingDataVo.setContainerNumber(String.valueOf(inspectionReport.getSample().getContainerNumber()));
			entryInspectionFindingDataVo.setLotNumber(String.valueOf(inspectionReport.getSample().getLotNumber()));
			//Controllers
			if (CollectionUtils.isNotEmpty(inspectionReport.getInspectionControllerList()))
			{
				for (final InspectionController inspectionController : inspectionReport.getInspectionControllerList())
				{
					final CtCctControllerDataVo controllerDataVo = new CtCctControllerDataVo();
					if (StringUtils.isNotBlank(inspectionController.getName()))
					{
						controllerDataVo.setName(inspectionController.getName());
					}
					if (StringUtils.isNotBlank(inspectionController.getPosition()))
					{
						controllerDataVo.setQuality(inspectionController.getPosition());
					}
					if (StringUtils.isNotBlank(inspectionController.getService()))
					{
						controllerDataVo.setService(inspectionController.getService());
					}
					controllerDataVoList.add(controllerDataVo);
				}
			}
			//Etiquetage
			if (StringUtils.isNotBlank(inspectionReport.getLabel()))
			{
				entryInspectionFindingDataVo.setLibelle(inspectionReport.getLabel());
			}
			if (inspectionReport.getStandardCompliance() == true)
			{
				entryInspectionFindingDataVo.setNormeRespect(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_TRUE));
			}
			else
			{
				entryInspectionFindingDataVo.setNormeRespect(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_FALSE));
			}
			entryInspectionFindingDataVo.setLabellingNumber(String.valueOf(inspectionReport.getStandardNumber()));
			//Temperature
			entryInspectionFindingDataVo.setTemperature(String.valueOf(inspectionReport.getTemperature()));
			if (StringUtils.isNotBlank(inspectionReport.getAspect()))
			{
				entryInspectionFindingDataVo.setAspect(inspectionReport.getAspect());
			}
			//Certificats
			if (inspectionReport.getOriginCertificate() == true)
			{
				entryInspectionFindingDataVo.setOrigineCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_TRUE));
			}
			else
			{
				entryInspectionFindingDataVo.setOrigineCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_FALSE));
			}
			if (inspectionReport.getPhytoGeneralCertificate() == true)
			{
				entryInspectionFindingDataVo.setGeneralPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
						getCurrentLocale()).getString(CONSTAT_BUNDLE_TRUE));
			}
			else
			{
				entryInspectionFindingDataVo.setGeneralPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
						getCurrentLocale()).getString(CONSTAT_BUNDLE_FALSE));
			}
			if (inspectionReport.getPhytoSpecialCertificate() == true)
			{
				entryInspectionFindingDataVo.setSpecialPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
						getCurrentLocale()).getString(CONSTAT_BUNDLE_TRUE));
			}
			else
			{
				entryInspectionFindingDataVo.setSpecialPhytosanitaryCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
						getCurrentLocale()).getString(CONSTAT_BUNDLE_FALSE));
			}
			if (inspectionReport.getQualityAttestation() == true)
			{
				entryInspectionFindingDataVo.setQualityCertification(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_TRUE));
			}
			else
			{
				entryInspectionFindingDataVo.setQualityCertification(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_FALSE));
			}
			if (inspectionReport.getSanitaryVetCertificate() == true)
			{
				entryInspectionFindingDataVo.setVeterinaryHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
						getCurrentLocale()).getString(CONSTAT_BUNDLE_TRUE));
			}
			else
			{
				entryInspectionFindingDataVo.setVeterinaryHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
						getCurrentLocale()).getString(CONSTAT_BUNDLE_FALSE));
			}
			if (inspectionReport.getWholesomenessCertificate() == true)
			{
				entryInspectionFindingDataVo.setHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_TRUE));
			}
			else
			{
				entryInspectionFindingDataVo.setHealthCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_FALSE));
			}
			if (inspectionReport.getConformityCertificate() == true)
			{
				entryInspectionFindingDataVo.setConformityCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_TRUE));
			}
			else
			{
				entryInspectionFindingDataVo.setConformityCertificate(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
						.getString(CONSTAT_BUNDLE_FALSE));
			}

			if (!Objects.equal(inspectionReport.getOtherGoodness(), null))
			{
				entryInspectionFindingDataVo.setOtherQualityCertificate(inspectionReport.getOtherGoodness().getLabelFr());
			}
			//Decision
			if (StringUtils.isNotBlank(inspectionReport.getControllerDecision()))
			{
				entryInspectionFindingDataVo.setDecision(inspectionReport.getControllerDecision());
			}
			//Observation
			if (StringUtils.isNotBlank(inspectionReport.getObservation()))
			{
				entryInspectionFindingDataVo.setObservation(inspectionReport.getObservation());
			}
			//Motif
			if (StringUtils.isNotBlank(inspectionReport.getMotif()))
			{
				entryInspectionFindingDataVo.setMotif(inspectionReport.getMotif());
			}
			//Frais
			if (StringUtils.isNotBlank(inspectionReport.getInspectionFeesFCFA()))
			{
				entryInspectionFindingDataVo.setInspectionFeeFcfa(inspectionReport.getInspectionFeesFCFA());
			}
			if (StringUtils.isNotBlank(inspectionReport.getInspectionFees()))
			{
				entryInspectionFindingDataVo.setProcessingFee(inspectionReport.getInspectionFees());
			}
			if (StringUtils.isNotBlank(inspectionReport.getOtherSpecialFees()))
			{
				entryInspectionFindingDataVo.setSpecialOtherFees(inspectionReport.getOtherSpecialFees());
			}
			//Infraction
			if (StringUtils.isNotBlank(inspectionReport.getInfraction()))
			{
				entryInspectionFindingDataVo.setNature(inspectionReport.getInfraction());
			}
			if (CollectionUtils.isNotEmpty(controllerDataVoList))
			{
				entryInspectionFindingDataVo.setControlerList(controllerDataVoList);
			}
		}

		return new JRBeanCollectionDataSource(Collections.singleton(entryInspectionFindingDataVo));
	}

	/**
	 * Gets the inspection report.
	 *
	 * @return the inspection report
	 */
	public InspectionReport getInspectionReport()
	{
		return inspectionReport;
	}

	/**
	 * Gets the current locale.
	 *
	 * @return the current locale
	 */
	public Locale getCurrentLocale()
	{
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

}
