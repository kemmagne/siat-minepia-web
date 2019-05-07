/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guce.siat.web.reports.vo;

import java.math.BigDecimal;

/**
 *
 * @author Ulrich ETEME
 */
public class WoodSpecificationVo {
    
    private String woodSpecies;
    
    private String numMarqueGrume;
    
    private String numColis;
    
    private Integer nbrePieces;
    
    private BigDecimal longueurGrume;
    
    private BigDecimal largeur;
    
    private Integer diamGrosBout;
    
    private Integer diamPetitBout;
    
    private Integer diamMoyen;
    
    private Integer epaisseur;
    
    private BigDecimal volume;
    
    private String fournisseur;
    
    private String observations;

    public String getWoodSpecies() {
        return woodSpecies;
    }

    public void setWoodSpecies(String woodSpecies) {
        this.woodSpecies = woodSpecies;
    }

    public String getNumMarqueGrume() {
        return numMarqueGrume;
    }

    public void setNumMarqueGrume(String numMarqueGrume) {
        this.numMarqueGrume = numMarqueGrume;
    }

    public String getNumColis() {
        return numColis;
    }

    public void setNumColis(String numColis) {
        this.numColis = numColis;
    }

    public Integer getNbrePieces() {
        return nbrePieces;
    }

    public void setNbrePieces(Integer nbrePieces) {
        this.nbrePieces = nbrePieces;
    }

    public BigDecimal getLongueurGrume() {
        return longueurGrume;
    }

    public void setLongueurGrume(BigDecimal longueurGrume) {
        this.longueurGrume = longueurGrume;
    }

    public BigDecimal getLargeur() {
        return largeur;
    }

    public void setLargeur(BigDecimal largeur) {
        this.largeur = largeur;
    }

    public Integer getDiamGrosBout() {
        return diamGrosBout;
    }

    public void setDiamGrosBout(Integer diamGrosBout) {
        this.diamGrosBout = diamGrosBout;
    }

    public Integer getDiamPetitBout() {
        return diamPetitBout;
    }

    public void setDiamPetitBout(Integer diamPetitBout) {
        this.diamPetitBout = diamPetitBout;
    }

    public Integer getDiamMoyen() {
        return diamMoyen;
    }

    public void setDiamMoyen(Integer diamMoyen) {
        this.diamMoyen = diamMoyen;
    }

    public Integer getEpaisseur() {
        return epaisseur;
    }

    public void setEpaisseur(Integer epaisseur) {
        this.epaisseur = epaisseur;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
