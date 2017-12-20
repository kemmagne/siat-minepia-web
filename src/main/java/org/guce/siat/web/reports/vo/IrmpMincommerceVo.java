/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guce.siat.web.reports.vo;

import java.util.Date;

/**
 *
 * @author yenke
 */
public class IrmpMincommerceVo extends AbstractFileVo<IrmpMincommerceFileItemVo>{
    
    private String numeroAttestation;
    private String numeroArreteApprobation;
    private Date dateSignature;

    public String getNumeroAttestation() {
        return numeroAttestation;
    }

    public void setNumeroAttestation(String numeroAttestation) {
        this.numeroAttestation = numeroAttestation;
    }

    public String getNumeroArreteApprobation() {
        return numeroArreteApprobation;
    }

    public void setNumeroArreteApprobation(String numeroArreteApprobation) {
        this.numeroArreteApprobation = numeroArreteApprobation;
    }

    public Date getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(Date dateSignature) {
        this.dateSignature = dateSignature;
    }
    
    
}
