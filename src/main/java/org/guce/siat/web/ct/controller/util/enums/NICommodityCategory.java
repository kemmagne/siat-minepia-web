package org.guce.siat.web.ct.controller.util.enums;

/**
 *
 * @author tadzotsa
 */
public enum NICommodityCategory {

    VEGETAUX_PLANTATION("VegetauxPlantation"),
    FRUITS_LEGUMES("FuitsLegumes"),
    PLANTES_POTS("PlantesPots"),
    FLEURES_RAMEAUX("FleuresRameaux"),
    BULBES_TUBERCULES("BulbesTubercules"),
    SEMENCES("Semences"),
    POMMES_TERRE_SEMENCE("PommesTerreSemence"),
    BOIS_ECORCE("BoisEcorce"),
    POMMES_TERRE_CONSO("PommesTerreConso"),
    TERRE_MILIEU_CULTURE("TerreMilieuCulture"),
    CULTURE_TISSUS("CultureTissus"),
    DENREES_STOCKEES("DenreesStockees");

    /**
     * The label.
     */
    private final String label;

    /**
     * Instantiates a new locale values.
     *
     * @param code the code
     */
    private NICommodityCategory(final String label) {
        this.label = label.intern();
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

}
