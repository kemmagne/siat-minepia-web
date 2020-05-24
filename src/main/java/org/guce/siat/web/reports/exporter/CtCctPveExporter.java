package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.guce.siat.common.model.Container;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.core.ct.model.PottingReport;
import org.guce.siat.core.ct.util.enums.CctExportProductType;
import static org.guce.siat.web.reports.exporter.ReportCommand.IMAGES_PATH;
import org.guce.siat.web.reports.vo.ContainerVo;
import org.guce.siat.web.reports.vo.CtCctPveFileVo;

/**
 *
 * @author ht
 */
public class CtCctPveExporter extends AbstractReportInvoker {

    private final File file;

    public CtCctPveExporter(File file) {
        super("CCT_CT_E_PVE", "CCT_CT_E_PVE");
        this.file = file;
    }

    @Override
    protected JRBeanCollectionDataSource getReportDataSource() {

        CtCctPveFileVo fileVo = new CtCctPveFileVo();

        fileVo.setFile(file);

        PottingReport pottingReport = getPottingReportService().findPottingReportByFile(file);
        fileVo.setPottingReport(pottingReport);

        String productTypeCode = getFileFieldValueService().findValueByFileFieldAndFile(CctExportProductType.getFileFieldCode(), file).getValue();
        fileVo.setProductTypeCode(productTypeCode);
        addJRParameters(productTypeCode);

        List<ContainerVo> containerVos = new ArrayList<>();
        for (Container container : file.getContainers()) {

            ContainerVo containerVo = new ContainerVo();

            containerVo.setContNumber(container.getContNumber());
            containerVo.setContSeal1(container.getContSeal1());
            containerVo.setContDenomination(container.getContDenomination());
            containerVo.setContGrossMass(container.getContGrossMass());
            containerVo.setContVolume(container.getContVolume());
            containerVo.setContMark(container.getContMark());
            containerVo.setContNumberOfPackages(container.getContNumberOfPackages());
            containerVo.setContRefrigerated(container.getContRefrigerated());
            containerVo.setContType(container.getContType());

            containerVo.setProductType(productTypeCode);

            addJRParameters(productTypeCode, containerVo);

            containerVos.add(containerVo);
        }
        fileVo.setContainers(containerVos);

//        List<PottingPresent> presents = getPottingReportService().findPottingPresentsByFile(file);
//        fileVo.setPresents(presents);
//        List<String> list = new ArrayList<>();
//        for (PottingPresent present : presents) {
//            if (!Constants.MINADER_MINISTRY.equals(present.getOrganism())) {
//                continue;
//            }
//
//            list.add(String.format("%s : %s", present.getName(), present.getQuality()));
//        }
//        fileVo.setAgents(StringUtils.join(list, " ; "));
        List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

        for (final FileFieldValue fileFieldValue1 : fileFieldValueList) {
            switch (fileFieldValue1.getFileField().getCode()) {
                case "TYPE_PRODUIT_NOM":
                    fileVo.setProductType(fileFieldValue1.getValue());
                    fileVo.setNatureProduit(getNatureProduit(fileFieldValue1.getValue()));
                    break;
                case "DATE_RDV_FINALE":
                    fileVo.setAppointmentDate(fileFieldValue1.getValue());
                    break;
                case "DATE_EMPOTAGE_EFFECTIF":
                    fileVo.setPottingEndDate(fileFieldValue1.getValue());
                    break;
                case "SITE_EMPOTAGE_NOM":
                    fileVo.setPottingPlace(fileFieldValue1.getValue());
                    break;
                case "SITE_EMPOTAGE_VILLE":
                    fileVo.setPottingTown(fileFieldValue1.getValue());
                    break;
                case "TRANSITAIRE_RAISONSOCIALE":
                    fileVo.setForwarderName(fileFieldValue1.getValue());
                    break;
                case "PV_EMPOTAGE_NUMERO_AT":
                    fileVo.setAtNumber(fileFieldValue1.getValue());
                    break;
                case "PV_EMPOTAGE_DATE_AT":
                    fileVo.setAtDate(fileFieldValue1.getValue());
                    break;
                case "LIEU_EMBARQUEMENT_LIBELLE":
                    fileVo.setLoadingPlace(fileFieldValue1.getValue());
                    break;
                case "LIEU_DESTINATION_LIBELLE":
                    fileVo.setDestinationPlace(fileFieldValue1.getValue());
                    break;
            }
        }

        return new JRBeanCollectionDataSource(Collections.singleton(fileVo));
    }

    @Override
    protected Map<String, Object> getJRParameters() {
        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "phytosanitaire", "jpg"));
        jRParameters.put("DRAFT_IMAGE", getRealPath(IMAGES_PATH, "draft", "jpg"));
        return jRParameters;
    }

    public File getFile() {
        return file;
    }

    private void addJRParameters(String productTypeCode, ContainerVo container) {
        CctExportProductType productType = CctExportProductType.valueOf(productTypeCode);
        switch (productType) {
            case GR:
                container.setContDenominationLabel("ESSENCE");
                container.setContNumberOfPackagesLabel("BILLES");
                container.setContQuantityLabel("VOLUME (m3)");
                break;
            case BT:
                container.setContDenominationLabel("ESSENCE");
                container.setContNumberOfPackagesLabel("N/COLIS");
                container.setContQuantityLabel("VOLUME (m3)");
                break;
            case PS:
                container.setContDenominationLabel("DENOMINATION");
                container.setContNumberOfPackagesLabel("N.COLIS/SAC");
                container.setContQuantityLabel("TONNAGE (t)");
                break;
            case OA:
                container.setContDenominationLabel("DENOMINATION");
                container.setContNumberOfPackagesLabel("N/PIECES");
                container.setContQuantityLabel("TONNAGE (t)");
                break;
            case CC:
                container.setContDenominationLabel("DENOMINATION");
                container.setContNumberOfPackagesLabel("NOMBRE SACS");
                container.setContQuantityLabel("TONNAGE (t)");
                break;
            case CF:
                container.setContDenominationLabel("DENOMINATION");
                container.setContNumberOfPackagesLabel("N. SACS");
                container.setContQuantityLabel("TONNAGE (t)");
                break;
            case COTON:
                container.setContDenominationLabel("DENOMINATION");
                container.setContNumberOfPackagesLabel("N. COLIS");
                container.setContQuantityLabel("TONNAGE (t)");
                break;
        }
    }

    private void addJRParameters(String productTypeCode) {
        CctExportProductType productType = CctExportProductType.valueOf(productTypeCode);
        switch (productType) {
            case GR:
                getJRParameters().put("EMP_ESSENCE_DENOMINATION", "ESSENCE");
                getJRParameters().put("EMP_BILLE_COLIS_PIECE", "BILLES");
                getJRParameters().put("EMP_VOLUME_TONNAGE", "VOLUME (m3)");
                break;
            case BT:
                getJRParameters().put("EMP_ESSENCE_DENOMINATION", "ESSENCE");
                getJRParameters().put("EMP_BILLE_COLIS_PIECE", "N/COLIS");
                getJRParameters().put("EMP_VOLUME_TONNAGE", "VOLUME (m3)");
                break;
            case PS:
                getJRParameters().put("EMP_ESSENCE_DENOMINATION", "DENOMINATION");
                getJRParameters().put("EMP_BILLE_COLIS_PIECE", "N.COLIS/SAC");
                getJRParameters().put("EMP_VOLUME_TONNAGE", "TONNAGE (t)");
                break;
            case OA:
                getJRParameters().put("EMP_ESSENCE_DENOMINATION", "DENOMINATION");
                getJRParameters().put("EMP_BILLE_COLIS_PIECE", "N/PIECES");
                getJRParameters().put("EMP_VOLUME_TONNAGE", "TONNAGE (t)");
                break;
            case CC:
                getJRParameters().put("EMP_ESSENCE_DENOMINATION", "DENOMINATION");
                getJRParameters().put("EMP_BILLE_COLIS_PIECE", "NOMBRE SACS");
                getJRParameters().put("EMP_VOLUME_TONNAGE", "TONNAGE (t)");
                break;
            case CF:
                getJRParameters().put("EMP_ESSENCE_DENOMINATION", "DENOMINATION");
                getJRParameters().put("EMP_BILLE_COLIS_PIECE", "N. SACS");
                getJRParameters().put("EMP_VOLUME_TONNAGE", "TONNAGE (t)");
                break;
            case COTON:
                getJRParameters().put("EMP_ESSENCE_DENOMINATION", "DENOMINATION");
                getJRParameters().put("EMP_BILLE_COLIS_PIECE", "N. COLIS");
                getJRParameters().put("EMP_VOLUME_TONNAGE", "TONNAGE (t)");
                break;
        }
    }

    private String getNatureProduit(String productType) {

        String detail = getFileFieldValueService().findValueByFileFieldAndFile("TYPE_PRODUIT_DETAIL", file).getValue();
        String cemac = getFileFieldValueService().findValueByFileFieldAndFile("CEMAC", file).getValue();
        String debites = getFileFieldValueService().findValueByFileFieldAndFile("DEBITES", file).getValue();
        String placage = getFileFieldValueService().findValueByFileFieldAndFile("PLACAGE", file).getValue();
        String contrePlaque = getFileFieldValueService().findValueByFileFieldAndFile("CONTRE_PLAQUE", file).getValue();

        String natureProduit = "";

        if (Constants.YES_FR.equals(detail)) {
            if (Constants.YES_FR.equals(debites)) {
                natureProduit = Constants.YES_FR.equals(cemac)
                        ? CtCctPveFileVo.Constants.BOIS_TRANSFORMES_DEBITES + CtCctPveFileVo.Constants.CEMAC
                        : CtCctPveFileVo.Constants.BOIS_TRANSFORMES_DEBITES;
            }
            if (Constants.YES_FR.equals(placage)) {
                natureProduit = org.apache.commons.lang.StringUtils.isEmpty(natureProduit) ? (Constants.YES_FR.equals(cemac)
                        ? CtCctPveFileVo.Constants.BOIS_TRANSFORMES_PLACAGES + CtCctPveFileVo.Constants.CEMAC
                        : CtCctPveFileVo.Constants.BOIS_TRANSFORMES_PLACAGES) : (natureProduit + ", " + (Constants.YES_FR.equals(cemac)
                        ? CtCctPveFileVo.Constants.BOIS_TRANSFORMES_PLACAGES + CtCctPveFileVo.Constants.CEMAC
                        : CtCctPveFileVo.Constants.BOIS_TRANSFORMES_PLACAGES));
            }
            if (Constants.YES_FR.equals(contrePlaque)) {
                natureProduit = org.apache.commons.lang.StringUtils.isEmpty(natureProduit) ? (Constants.YES_FR.equals(cemac)
                        ? CtCctPveFileVo.Constants.BOIS_TRANSFORMES_CONTRE_PLAQUES + CtCctPveFileVo.Constants.CEMAC
                        : CtCctPveFileVo.Constants.BOIS_TRANSFORMES_CONTRE_PLAQUES) : (natureProduit + ", " + (Constants.YES_FR.equals(cemac)
                        ? CtCctPveFileVo.Constants.BOIS_TRANSFORMES_CONTRE_PLAQUES + CtCctPveFileVo.Constants.CEMAC
                        : CtCctPveFileVo.Constants.BOIS_TRANSFORMES_CONTRE_PLAQUES));
            }
        } else {
            natureProduit = Constants.YES_FR.equals(cemac) ? productType + CtCctPveFileVo.Constants.CEMAC : productType;
        }

        return natureProduit;
    }

}
