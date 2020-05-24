package org.guce.siat.web.reports.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.guce.siat.common.model.File;
import org.guce.siat.core.ct.model.PottingPresent;
import org.guce.siat.core.ct.model.PottingReport;

/**
 *
 * @author ht
 */
public class CtCctPveFileVo implements Serializable {

    private File file;
    private List<PottingPresent> presents;
    private List<ContainerVo> containers;
    private PottingReport pottingReport;

    private String appointmentDate;
    private String pottingEndDate;

    private String productType;
    private String productTypeCode;
    private boolean cemac;
    private String natureProduit;

    private String pottingPlace;
    private String pottingTown;
    private String loadingPlace;
    private String destinationPlace;
    private String agents;
    private String forwarderName;

    private String atNumber;
    private String atDate;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public PottingReport getPottingReport() {
        return pottingReport;
    }

    public void setPottingReport(PottingReport pottingReport) {
        this.pottingReport = pottingReport;
    }

    public List<PottingPresent> getPresents() {
        return presents;
    }

    public void setPresents(List<PottingPresent> presents) {
        this.presents = presents;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPottingEndDate() {
        return pottingEndDate;
    }

    public void setPottingEndDate(String pottingEndDate) {
        this.pottingEndDate = pottingEndDate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public boolean isCemac() {
        return cemac;
    }

    public void setCemac(boolean cemac) {
        this.cemac = cemac;
    }

    public String getNatureProduit() {
        return natureProduit;
    }

    public void setNatureProduit(String natureProduit) {
        this.natureProduit = natureProduit;
    }

    public String getPottingPlace() {
        return pottingPlace;
    }

    public void setPottingPlace(String pottingPlace) {
        this.pottingPlace = pottingPlace;
    }

    public String getLoadingPlace() {
        return loadingPlace;
    }

    public void setLoadingPlace(String loadingPlace) {
        this.loadingPlace = loadingPlace;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(String destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public String getAgents() {
        return agents;
    }

    public void setAgents(String agents) {
        this.agents = agents;
    }

    public String getForwarderName() {
        return forwarderName;
    }

    public void setForwarderName(String forwarderName) {
        this.forwarderName = forwarderName;
    }

    public String getAtNumber() {
        return atNumber;
    }

    public void setAtNumber(String atNumber) {
        this.atNumber = atNumber;
    }

    public String getAtDate() {
        return atDate;
    }

    public void setAtDate(String atDate) {
        this.atDate = atDate;
    }

    public String getPottingTown() {
        return pottingTown;
    }

    public void setPottingTown(String pottingTown) {
        this.pottingTown = pottingTown;
    }

    public List<ContainerVo> getContainers() {

        if (containers == null) {
            return new ArrayList<>();
        }

        return containers;
    }

    public void setContainers(List<ContainerVo> containers) {
        this.containers = containers;
    }

    public static interface Constants {

        String CEMAC = "CEMAC";
        String BOIS_TRANSFORMES_DEBITES = "DEBITES";
        String BOIS_TRANSFORMES_PLACAGES = "PLACAGES";
        String BOIS_TRANSFORMES_CONTRE_PLAQUES = "CONTREPLAQUES";
        String GRUMES = "GRUMES";
        String PRODUITS_SPECIAUX = "PRODUITS SPECIAUX";
        String OBJET_ART = "OBJETS D'ART";

    }

}
