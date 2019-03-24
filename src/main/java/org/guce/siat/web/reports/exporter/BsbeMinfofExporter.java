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
import org.guce.siat.core.ct.model.WoodSpecification;
import org.guce.siat.web.reports.vo.WoodSpecificationVo;
import org.guce.siat.web.reports.vo.BsbeMinfofFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Ulrich ETEME
 */
public class BsbeMinfofExporter extends AbstractReportInvoker {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(BsbeMinfofExporter.class);

    /**
     * The file.
     */
    private final File file;
    
    private final List<WoodSpecification> specsList;

    /**
     * Instantiates a new bsbe MINFOF exporter.
     *
     * @param file the file
     * @param specsList
     */
    public BsbeMinfofExporter(final File file, final List<WoodSpecification> specsList) {
        super("BSBE_MINFOF", "BSBE_MINFOF");
        this.file = file;
        this.specsList = specsList;
    }

    /**
     * Dans le cadre de la procédure bsbe MINFOF, il est possible de
     * générer deux types de rapport. D'où le paramètre jasperFileName
     *
     * @param jasperFileName
     * @param file
     * @param specsList
     */
    public BsbeMinfofExporter(String jasperFileName, final File file, final List<WoodSpecification> specsList) {
        super(jasperFileName, "BSBE_MINFOF");
        this.file = file;
        this.specsList = specsList;
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final BsbeMinfofFileVo bsbeMinfofVo = new BsbeMinfofFileVo();

        if ((file != null)) {
            bsbeMinfofVo.setDecisionDate(file.getSignatureDate());
            bsbeMinfofVo.setReferenceEforce(file.getNumeroDemande());
            final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
            if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
                for (final FileFieldValue fileFieldValue : fileFieldValueList) {
                    switch (fileFieldValue.getFileField().getCode()) {
                        case "BULLETIN_SPECIFICATION_NUMERO_ENREGISTREMENT":
                            bsbeMinfofVo.setDecisionNumber(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_BSB_CERTIFICAT_ENREGISTREMENT_NUMERO_ENREGISTREMENT":
                            bsbeMinfofVo.setRegistrationNumber(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
                            bsbeMinfofVo.setLoadingPlace(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_LIEU_EXPORTATION_LIBELLE":
                            bsbeMinfofVo.setDischargePlace(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_PAYS_DESTINATION_NOM_PAYS":
                            bsbeMinfofVo.setDestinationCountry(fileFieldValue.getValue());
                            break;
                        case "SIGNATAIRE_NOM":
                            bsbeMinfofVo.setSignatory(fileFieldValue.getValue());
                            break;
                        case "SIGNATAIRE_LIEU":
                            bsbeMinfofVo.setDecisionPlace(fileFieldValue.getValue());
                            break;
                        case "SIGNATAIRE_DATE":
                            if (StringUtils.isNotBlank(fileFieldValue.getValue()) && bsbeMinfofVo.getDecisionDate() == null) {
                                try {
                                    if (fileFieldValue.getValue() != null) {
                                        bsbeMinfofVo.setDecisionDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue()));
                                    } else {
                                        bsbeMinfofVo.setDecisionDate(java.util.Calendar.getInstance().getTime());
                                    }
                                } catch (final ParseException e) {
                                    LOG.error(Objects.toString(e), e);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            if ((file.getClient() != null)) {
                bsbeMinfofVo.setImporter(file.getClient().getCompanyName());
            }

            final List<WoodSpecificationVo> woodSpecVos = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(specsList)) {
                for (final WoodSpecification woodSpec : specsList) {
                    final WoodSpecificationVo woodSpecVo = new WoodSpecificationVo();
                    woodSpecVo.setDiamGrosBout(woodSpec.getDiamGrosBout());
                    woodSpecVo.setDiamMoyen(woodSpec.getDiamMoyen());
                    woodSpecVo.setDiamPetitBout(woodSpec.getDiamPetitBout());
                    woodSpecVo.setFournisseur(woodSpec.getFournisseur());
                    woodSpecVo.setLongueurGrume(woodSpec.getLongueurGrume());
                    woodSpecVo.setNumMarqueGrume(woodSpec.getNumMarqueGrume());
                    woodSpecVo.setObservations(woodSpec.getObservations());
                    woodSpecVo.setVolume(woodSpec.getVolume());
                    woodSpecVo.setWoodSpecies(woodSpec.getWoodSpecies());
                    woodSpecVos.add(woodSpecVo);
                }
            }

            bsbeMinfofVo.setWoodSpecs(woodSpecVos);
        }

        return new JRBeanCollectionDataSource(Collections.singleton(bsbeMinfofVo));
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getJRParameters()
     */
    @Override
    protected Map<String, Object> getJRParameters() {

        final Map<String, Object> jRParameters = super.getJRParameters();
        return jRParameters;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

    public List<WoodSpecification> getSpecsList() {
        return specsList;
    }

}
