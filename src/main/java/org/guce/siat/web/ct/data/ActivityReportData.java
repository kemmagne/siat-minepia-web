/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guce.siat.web.ct.data;

import java.io.Serializable;

/**
 *
 * @author yenke
 */
public class ActivityReportData implements Serializable{
    
    
    private String processName;
    private String fileReceivedCount;
    private String fileSignedCount;
    private String fileReceivedSignedCount;
    private String fileRejectedCount;
    private String fileReceivedRejectedCount;
    private String filePendingCount;
    private String officeCode;
    private String officeLabel;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getFileReceivedCount() {
        return fileReceivedCount;
    }

    public void setFileReceivedCount(String fileReceivedCount) {
        this.fileReceivedCount = fileReceivedCount;
    }

    public String getFileSignedCount() {
        return fileSignedCount;
    }

    public void setFileSignedCount(String fileSignedCount) {
        this.fileSignedCount = fileSignedCount;
    }

    public String getFileReceivedSignedCount() {
        return fileReceivedSignedCount;
    }

    public void setFileReceivedSignedCount(String fileReceivedSignedCount) {
        this.fileReceivedSignedCount = fileReceivedSignedCount;
    }

    public String getFileRejectedCount() {
        return fileRejectedCount;
    }

    public void setFileRejectedCount(String fileRejectedCount) {
        this.fileRejectedCount = fileRejectedCount;
    }

    public String getFileReceivedRejectedCount() {
        return fileReceivedRejectedCount;
    }

    public void setFileReceivedRejectedCount(String fileReceivedRejectedCount) {
        this.fileReceivedRejectedCount = fileReceivedRejectedCount;
    }

    public String getFilePendingCount() {
        return filePendingCount;
    }

    public void setFilePendingCount(String filePendingCount) {
        this.filePendingCount = filePendingCount;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeLabel() {
        return officeLabel;
    }

    public void setOfficeLabel(String officeLabel) {
        this.officeLabel = officeLabel;
    }
    
    
    
}
