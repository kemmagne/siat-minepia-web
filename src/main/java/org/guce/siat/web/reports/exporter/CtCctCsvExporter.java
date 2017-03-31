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
import org.guce.siat.web.reports.vo.CtCctCsvFileItemVo;
import org.guce.siat.web.reports.vo.CtCctCsvFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CtCctCsvExporter.
 */
public class CtCctCsvExporter extends AbstractReportInvoker {

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CtCctCsvExporter.class);

	/**
	 * The file.
	 */
	private final File file;

	/**
	 * Instantiates a new ct cct csv exporter.
	 *
	 * @param file the file
	 */
	public CtCctCsvExporter(final File file) {
		super("CT_CCT_CSV", "CT_CCT_CSV");
		this.file = file;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource() {

		final CtCctCsvFileVo ctCctCsvFileVo = new CtCctCsvFileVo();

		if ((file != null)) {
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			ctCctCsvFileVo.setDecisionPlace(file.getBureau().getLabelFr());
			ctCctCsvFileVo.setDecisionDate(Calendar.getInstance().getTime());
			ctCctCsvFileVo.setSignatoryName(file.getAssignedUser().getFirstName());
			ctCctCsvFileVo.setSignatoryPosition(file.getAssignedUser().getPosition().getLabelFr());
			if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
				for (final FileFieldValue fileFieldValue : fileFieldValueList) {
					switch (fileFieldValue.getFileField().getCode()) {
						case "TRANSITAIRE_RAISONSOCIALE":
							ctCctCsvFileVo.setConsignorName(fileFieldValue.getValue());
							break;
						case "TRANSITAIRE_ADRESSE_ADRESSE1":
							ctCctCsvFileVo.setConsignorAddress1(fileFieldValue.getValue());
							break;
						case "TRANSITAIRE_TELEPHONE_FIXE_NUMERO":
							ctCctCsvFileVo.setConsignorTelephone(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_CERTIFICAT_EXPERTISE_NUMERO":
							ctCctCsvFileVo.setCertificateReferenceNumber(fileFieldValue.getValue());
							break;

						case "DESTINATAIRE_RAISONSOCIALE":
							ctCctCsvFileVo.setConsigneeName(fileFieldValue.getValue());
							break;
						case "DESTINATAIRE_ADRESSE_ADRESSE1":
							ctCctCsvFileVo.setConsigneeAddress1(fileFieldValue.getValue());
							break;
						case "DESTINATAIRE_ADRESSE_ADRESSE2":
							ctCctCsvFileVo.setConsigneeAddress2(fileFieldValue.getValue());
							break;
						case "DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
							ctCctCsvFileVo.setConsigneeCountry(fileFieldValue.getValue());
							break;
						case "DESTINATAIRE_TELEPHONE_FIXE_NUMERO":
							ctCctCsvFileVo.setConsigneeTelephone(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_PAYS_ORIGINE_NOM_PAYS":
							ctCctCsvFileVo.setCountryOfOrigin(fileFieldValue.getValue());
							break;
						case "ZONR_ORIGINE_NOM":
							ctCctCsvFileVo.setZoneOfOrigin(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_PAYS_DESTINATION_NOM_PAYS":
							ctCctCsvFileVo.setCountryOfDestination(fileFieldValue.getValue());
							break;
						case "ZONE_DESTINATION_NOM":
							ctCctCsvFileVo.setZoneOfDestination(fileFieldValue.getValue());
							break;
						case "LIEU_ORIGINE_NOM":
							ctCctCsvFileVo.setPlaceOfOriginName(fileFieldValue.getValue());
							break;
						case "LIEU_ORIGINE_ADRESSE":
							ctCctCsvFileVo.setPlaceOfOriginAddress(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
							ctCctCsvFileVo.setPlaceOfShipment(fileFieldValue.getValue());
							break;

						case "EXPEDITION_DATE_EXPEDITION_DATE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
								try {
									ctCctCsvFileVo.setExpeditionDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue()));
								} catch (final ParseException e) {
									LOG.error(Objects.toString(e), e);
								}
							}
							break;
						case "EXPEDITION_DATE_EXPEDITION_HEURE":
							ctCctCsvFileVo.setExpeditionHour(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
							ctCctCsvFileVo.setModeOfTransport(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
							ctCctCsvFileVo.setMeansOfTransport(fileFieldValue.getValue());
							break;
						case "TRANSPORT_IDENTIFICATION":
							ctCctCsvFileVo.setTransportIdentification(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_SIGNATAIRE_NOM":
							ctCctCsvFileVo.setSignatoryName(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_SIGNATAIRE_LIEU":
							ctCctCsvFileVo.setSignatoryAddress(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_SIGNATAIRE_QUALITE":
							ctCctCsvFileVo.setSignatoryPosition(fileFieldValue.getValue());
							break;
						case "TOTAL_NBR_LOTS_COLIS":
							ctCctCsvFileVo.setTotalNumberOfPackages(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}

			ctCctCsvFileVo.setDecisionNumber("/MINEP/SG/BNO");

			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<CtCctCsvFileItemVo> fileItemVos = new ArrayList<CtCctCsvFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList)) {
				for (final FileItem fileItem : fileItemList) {
					final CtCctCsvFileItemVo fileItemVo = new CtCctCsvFileItemVo();

					fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);
					fileItemVo.setCode(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemCode() : null);

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList)) {
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
							switch (fileItemFieldValue.getFileItemField().getCode()) {
								case "NUMERO_CONTENEUR":
									fileItemVo.setContainerNumber(fileItemFieldValue.getValue());
									break;
								case "QUANTITE_TOTALE":
									fileItemVo.setTotalQuantity(fileItemFieldValue.getValue());
									break;
								case "NBR_LOTS_COLIS":
									fileItemVo.setNumberOfPackages(fileItemFieldValue.getValue());
									break;
								case "POIDS_NET":
									fileItemVo.setNetWeight(fileItemFieldValue.getValue());
								case "TEMPERATURE":
									fileItemVo.setTemperature(fileItemFieldValue.getValue());
									break;
								case "TYPE_COLIS":
									fileItemVo.setTypeOfPackaging(fileItemFieldValue.getValue());
									break;
								case "USAGE":
									fileItemVo.setUse(fileItemFieldValue.getValue());
									break;
								case "NOM_SCIENTIFIQUE":
									fileItemVo.setScientificName(fileItemFieldValue.getValue());
									break;
								case "NATURE":
									fileItemVo.setNature(fileItemFieldValue.getValue());
									break;
								case "TYPE_TRAITEMENT":
									fileItemVo.setTreatmentType(fileItemFieldValue.getValue());
									break;
								case "ETABLISSEMENTS":
									fileItemVo.setApprovedNumberOfEstablishments(fileItemFieldValue.getValue());
									break;
								default:
									break;
							}
						}
					}

					fileItemVos.add(fileItemVo);
				}
			}
			//	ctCctCsvFileVo.setSignatoryName();
			ctCctCsvFileVo.setFileItemList(fileItemVos);
		}

		return new JRBeanCollectionDataSource(Collections.singleton(ctCctCsvFileVo));
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

}
