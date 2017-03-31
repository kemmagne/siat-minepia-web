package org.guce.siat.web.ct.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.guce.siat.common.model.Appointment;
import org.guce.siat.core.ct.model.InspectionController;
import org.guce.siat.core.ct.model.InspectionReport;
import org.guce.siat.core.ct.model.Sample;
import org.guce.siat.core.gr.utils.enums.CertficatGoodness;
import org.guce.siat.web.ct.controller.util.enums.DecisionsSuiteVisite;


/**
 * The Class InspectionReportData.
 */
public class InspectionReportData implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8265575069445636893L;

	/** The inspection place. */
	private String inspectionPlace;

	/** The inspection date. */
	private Date inspectionDate;

	/** The inspection time. */
	private Date inspectionTime;

	/** The origin certificate. */
	private Boolean originCertificate;

	/** The phyto general certificate. */
	private Boolean phytoGeneralCertificate;

	/** The phyto special certificate. */
	private Boolean phytoSpecialCertificate;

	/** The quality attestation. */
	private Boolean qualityAttestation;

	/** The sanitary vet certificate. */
	private Boolean sanitaryVetCertificate;

	/** The wholesomeness certificate. */
	private Boolean wholesomenessCertificate;

	/** The conformity certificate. */
	private Boolean conformityCertificate;

	/** The other certificate. */
	private String otherCertificate;

	/** The observation. */
	private String observation;

	private CertficatGoodness otherGoodness;
	/** The controller decision. */
	private String controllerDecision;

	/** The quarantined culture place. */
	private String quarantinedCulturePlace;

	/** The motif. */
	private String motif;

	/** The inspection fees fcfa. */
	private String inspectionFeesFCFA;

	/** The inspection fees. */
	private String inspectionFees;

	/** The other special fees. */
	private String otherSpecialFees;

	/** The infraction. */
	private String infraction;

	/** The samples. */
	private List<Sample> samples;

	/** The controllers. */
	private List<InspectionController> controllers;

	/** The temperature list. */
	private List<InspectionReportTemperatureVo> temperatureList;

	/** The etiquetage list. */
	private List<InspectionReportEtiquetageVo> etiquetageList;

	/** The decisions suite visite. */
	private DecisionsSuiteVisite decisionsSuiteVisite;

	/**
	 * Transform to report list.
	 *
	 * @param appointment
	 *           the appointment
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionReport> transformToReportList(final Appointment appointment)
	{
		final List<InspectionReport> inspectionReports = new ArrayList<InspectionReport>();
		for (final Sample echantillon : this.samples)
		{
			final InspectionReport inspectionReport = new InspectionReport();
			inspectionReport.setPlace(this.inspectionPlace);
			inspectionReport.setReportDate(this.inspectionDate);
			inspectionReport.setReportTime(this.inspectionTime);
			inspectionReport.setControllerDecision(this.controllerDecision);
			inspectionReport.setQuarantinedCulturePlace(this.quarantinedCulturePlace);
			inspectionReport.setOtherGoodness(this.otherGoodness);
			inspectionReport.setObservation(this.observation);
			inspectionReport.setMotif(this.motif);
			inspectionReport.setInspectionFees(this.inspectionFees);
			inspectionReport.setOtherCertificate(this.otherCertificate);
			inspectionReport.setInspectionFeesFCFA(this.inspectionFeesFCFA);
			inspectionReport.setQuarantinedCulturePlace(this.getQuarantinedCulturePlace());
			inspectionReport.setOtherSpecialFees(this.otherSpecialFees);
			inspectionReport.setOriginCertificate(this.originCertificate);
			inspectionReport.setPhytoGeneralCertificate(this.phytoGeneralCertificate);
			inspectionReport.setPhytoSpecialCertificate(this.phytoSpecialCertificate);
			inspectionReport.setQualityAttestation(this.qualityAttestation);
			inspectionReport.setSanitaryVetCertificate(this.sanitaryVetCertificate);
			inspectionReport.setWholesomenessCertificate(this.wholesomenessCertificate);
			inspectionReport.setConformityCertificate(this.conformityCertificate);

			final InspectionReportTemperatureVo temperatureVo = ((List<InspectionReportTemperatureVo>) CollectionUtils.select(
					this.temperatureList, new Predicate()
					{
						@Override
						public boolean evaluate(final Object object)
						{
							return ((InspectionReportTemperatureVo) object).getFileItem().getId()
									.equals(echantillon.getFileItem().getId());
						}
					})).get(0);
			inspectionReport.setAspect(temperatureVo.getAspect());
			inspectionReport.setTemperature(temperatureVo.getValue());
			final InspectionReportEtiquetageVo etiquetageVo = ((List<InspectionReportEtiquetageVo>) CollectionUtils.select(
					this.etiquetageList, new Predicate()
					{
						@Override
						public boolean evaluate(final Object object)
						{
							return ((InspectionReportEtiquetageVo) object).getFileItem().getId()
									.equals(echantillon.getFileItem().getId());
						}
					})).get(0);
			inspectionReport.setLabel(etiquetageVo.getLabel());
			inspectionReport.setStandardCompliance(etiquetageVo.getStandardCompliance());
			inspectionReport.setStandardNumber(etiquetageVo.getStandardNumber());
			inspectionReport.setOriginCertificate(this.originCertificate);
			inspectionReport.setSample(echantillon);
			inspectionReport.setInfraction(this.infraction);
			inspectionReport.setInspectionControllerList(this.controllers);
			for (final InspectionController controller : controllers)
			{
				controller.setInspection(inspectionReport);
			}
			inspectionReport.setFileItem(echantillon.getFileItem());

			inspectionReport.setAppointment(appointment);

			inspectionReports.add(inspectionReport);
		}


		return inspectionReports;
	}

	/**
	 * Gets the inspection place.
	 *
	 * @return the inspection place
	 */
	public String getInspectionPlace()
	{
		return inspectionPlace;
	}

	/**
	 * Sets the inspection place.
	 *
	 * @param inspectionPlace
	 *           the inspectionPlace to set
	 */
	public void setInspectionPlace(final String inspectionPlace)
	{
		this.inspectionPlace = inspectionPlace;
	}

	/**
	 * Gets the inspection date.
	 *
	 * @return the inspectionDate
	 */
	public Date getInspectionDate()
	{
		return inspectionDate;
	}

	/**
	 * Sets the inspection date.
	 *
	 * @param inspectionDate
	 *           the inspectionDate to set
	 */
	public void setInspectionDate(final Date inspectionDate)
	{
		this.inspectionDate = inspectionDate;
	}

	/**
	 * Gets the inspection time.
	 *
	 * @return the inspectionTime
	 */
	public Date getInspectionTime()
	{
		return inspectionTime;
	}

	/**
	 * Sets the inspection time.
	 *
	 * @param inspectionTime
	 *           the inspectionTime to set
	 */
	public void setInspectionTime(final Date inspectionTime)
	{
		this.inspectionTime = inspectionTime;
	}

	/**
	 * Gets the samples.
	 *
	 * @return the samples
	 */
	public List<Sample> getSamples()
	{
		return samples;
	}

	/**
	 * Sets the samples.
	 *
	 * @param samples
	 *           the samples to set
	 */
	public void setSamples(final List<Sample> samples)
	{
		this.samples = samples;
	}

	/**
	 * Gets the controllers.
	 *
	 * @return the controllers
	 */
	public List<InspectionController> getControllers()
	{
		return controllers;
	}

	/**
	 * Sets the controllers.
	 *
	 * @param controllers
	 *           the controllers to set
	 */
	public void setControllers(final List<InspectionController> controllers)
	{
		this.controllers = controllers;
	}

	/**
	 * Gets the temperature list.
	 *
	 * @return the temperatureList
	 */
	public List<InspectionReportTemperatureVo> getTemperatureList()
	{
		return temperatureList;
	}

	/**
	 * Sets the temperature list.
	 *
	 * @param temperatureList
	 *           the temperatureList to set
	 */
	public void setTemperatureList(final List<InspectionReportTemperatureVo> temperatureList)
	{
		this.temperatureList = temperatureList;
	}

	/**
	 * Gets the origin certificate.
	 *
	 * @return the originCertificate
	 */
	public Boolean getOriginCertificate()
	{
		return originCertificate;
	}

	/**
	 * Sets the origin certificate.
	 *
	 * @param originCertificate
	 *           the originCertificate to set
	 */
	public void setOriginCertificate(final Boolean originCertificate)
	{
		this.originCertificate = originCertificate;
	}

	/**
	 * Gets the phyto general certificate.
	 *
	 * @return the phytoGeneralCertificate
	 */
	public Boolean getPhytoGeneralCertificate()
	{
		return phytoGeneralCertificate;
	}

	/**
	 * Sets the phyto general certificate.
	 *
	 * @param phytoGeneralCertificate
	 *           the phytoGeneralCertificate to set
	 */
	public void setPhytoGeneralCertificate(final Boolean phytoGeneralCertificate)
	{
		this.phytoGeneralCertificate = phytoGeneralCertificate;
	}

	/**
	 * Gets the phyto special certificate.
	 *
	 * @return the phytoSpecialCertificate
	 */
	public Boolean getPhytoSpecialCertificate()
	{
		return phytoSpecialCertificate;
	}

	/**
	 * Sets the phyto special certificate.
	 *
	 * @param phytoSpecialCertificate
	 *           the phytoSpecialCertificate to set
	 */
	public void setPhytoSpecialCertificate(final Boolean phytoSpecialCertificate)
	{
		this.phytoSpecialCertificate = phytoSpecialCertificate;
	}

	/**
	 * Gets the quality attestation.
	 *
	 * @return the qualityAttestation
	 */
	public Boolean getQualityAttestation()
	{
		return qualityAttestation;
	}

	/**
	 * Sets the quality attestation.
	 *
	 * @param qualityAttestation
	 *           the qualityAttestation to set
	 */
	public void setQualityAttestation(final Boolean qualityAttestation)
	{
		this.qualityAttestation = qualityAttestation;
	}

	/**
	 * Gets the sanitary vet certificate.
	 *
	 * @return the sanitaryVetCertificate
	 */
	public Boolean getSanitaryVetCertificate()
	{
		return sanitaryVetCertificate;
	}

	/**
	 * Sets the sanitary vet certificate.
	 *
	 * @param sanitaryVetCertificate
	 *           the sanitaryVetCertificate to set
	 */
	public void setSanitaryVetCertificate(final Boolean sanitaryVetCertificate)
	{
		this.sanitaryVetCertificate = sanitaryVetCertificate;
	}

	/**
	 * Gets the wholesomeness certificate.
	 *
	 * @return the wholesomenessCertificate
	 */
	public Boolean getWholesomenessCertificate()
	{
		return wholesomenessCertificate;
	}

	/**
	 * Sets the wholesomeness certificate.
	 *
	 * @param wholesomenessCertificate
	 *           the wholesomenessCertificate to set
	 */
	public void setWholesomenessCertificate(final Boolean wholesomenessCertificate)
	{
		this.wholesomenessCertificate = wholesomenessCertificate;
	}

	/**
	 * Gets the conformity certificate.
	 *
	 * @return the conformityCertificate
	 */
	public Boolean getConformityCertificate()
	{
		return conformityCertificate;
	}

	/**
	 * Sets the conformity certificate.
	 *
	 * @param conformityCertificate
	 *           the conformityCertificate to set
	 */
	public void setConformityCertificate(final Boolean conformityCertificate)
	{
		this.conformityCertificate = conformityCertificate;
	}

	/**
	 * Gets the other certificate.
	 *
	 * @return the otherCertificate
	 */
	public String getOtherCertificate()
	{
		return otherCertificate;
	}

	/**
	 * Sets the other certificate.
	 *
	 * @param otherCertificate
	 *           the otherCertificate to set
	 */
	public void setOtherCertificate(final String otherCertificate)
	{
		this.otherCertificate = otherCertificate;
	}

	/**
	 * Gets the observation.
	 *
	 * @return the observation
	 */
	public String getObservation()
	{
		return observation;
	}

	/**
	 * Sets the observation.
	 *
	 * @param observation
	 *           the observation to set
	 */
	public void setObservation(final String observation)
	{
		this.observation = observation;
	}

	/**
	 * Gets the motif.
	 *
	 * @return the motif
	 */
	public String getMotif()
	{
		return motif;
	}

	/**
	 * Sets the motif.
	 *
	 * @param motif
	 *           the motif to set
	 */
	public void setMotif(final String motif)
	{
		this.motif = motif;
	}

	/**
	 * Gets the inspection fees fcfa.
	 *
	 * @return the inspectionFeesFCFA
	 */
	public String getInspectionFeesFCFA()
	{
		return inspectionFeesFCFA;
	}

	/**
	 * Sets the inspection fees fcfa.
	 *
	 * @param inspectionFeesFCFA
	 *           the inspectionFeesFCFA to set
	 */
	public void setInspectionFeesFCFA(final String inspectionFeesFCFA)
	{
		this.inspectionFeesFCFA = inspectionFeesFCFA;
	}

	/**
	 * Gets the inspection fees.
	 *
	 * @return the inspectionFees
	 */
	public String getInspectionFees()
	{
		return inspectionFees;
	}

	/**
	 * Sets the inspection fees.
	 *
	 * @param inspectionFees
	 *           the inspectionFees to set
	 */
	public void setInspectionFees(final String inspectionFees)
	{
		this.inspectionFees = inspectionFees;
	}

	/**
	 * Gets the other special fees.
	 *
	 * @return the otherSpecialFees
	 */
	public String getOtherSpecialFees()
	{
		return otherSpecialFees;
	}

	/**
	 * Sets the other special fees.
	 *
	 * @param otherSpecialFees
	 *           the otherSpecialFees to set
	 */
	public void setOtherSpecialFees(final String otherSpecialFees)
	{
		this.otherSpecialFees = otherSpecialFees;
	}

	/**
	 * Gets the infraction.
	 *
	 * @return the infraction
	 */
	public String getInfraction()
	{

		return infraction;
	}

	/**
	 * Sets the infraction.
	 *
	 * @param infraction
	 *           the infraction to set
	 */
	public void setInfraction(final String infraction)
	{
		this.infraction = infraction;
	}

	/**
	 * Gets the etiquetage list.
	 *
	 * @return the etiquetageList
	 */
	public List<InspectionReportEtiquetageVo> getEtiquetageList()
	{
		return etiquetageList;
	}

	/**
	 * Sets the etiquetage list.
	 *
	 * @param etiquetageList
	 *           the etiquetageList to set
	 */
	public void setEtiquetageList(final List<InspectionReportEtiquetageVo> etiquetageList)
	{
		this.etiquetageList = etiquetageList;
	}

	/**
	 * Gets the controller decision.
	 *
	 * @return the controllerDecision
	 */
	public String getControllerDecision()
	{
		return controllerDecision;
	}

	/**
	 * Sets the controller decision.
	 *
	 * @param controllerDecision
	 *           the controllerDecision to set
	 */
	public void setControllerDecision(final String controllerDecision)
	{
		this.controllerDecision = controllerDecision;
	}

	/**
	 * Gets the quarantined culture place.
	 *
	 * @return the quarantinedCulturePlace
	 */
	public String getQuarantinedCulturePlace()
	{
		return quarantinedCulturePlace;
	}

	/**
	 * Sets the quarantined culture place.
	 *
	 * @param quarantinedCulturePlace
	 *           the quarantinedCulturePlace to set
	 */
	public void setQuarantinedCulturePlace(final String quarantinedCulturePlace)
	{
		this.quarantinedCulturePlace = quarantinedCulturePlace;
	}

	/**
	 * Gets the decisions suite visite.
	 *
	 * @return the decisionsSuiteVisite
	 */
	public DecisionsSuiteVisite getDecisionsSuiteVisite()
	{
		return decisionsSuiteVisite;
	}

	/**
	 * Sets the decisions suite visite.
	 *
	 * @param decisionsSuiteVisite
	 *           the decisionsSuiteVisite to set
	 */
	public void setDecisionsSuiteVisite(final DecisionsSuiteVisite decisionsSuiteVisite)
	{
		this.decisionsSuiteVisite = decisionsSuiteVisite;
	}

	/**
	 * Gets the other goodness.
	 *
	 * @return the other goodness
	 */
	public CertficatGoodness getOtherGoodness()
	{
		return otherGoodness;
	}

	/**
	 * Sets the other goodness.
	 *
	 * @param otherGoodness
	 *           the new other goodness
	 */
	public void setOtherGoodness(final CertficatGoodness otherGoodness)
	{
		this.otherGoodness = otherGoodness;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("InspectionReportData [inspectionPlace=");
		builder.append(inspectionPlace);
		builder.append(", inspectionDate=");
		builder.append(inspectionDate);
		builder.append(", inspectionTime=");
		builder.append(inspectionTime);
		builder.append("]");
		return builder.toString();
	}



}
