package org.guce.siat.web.reports.vo;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.User;
import org.guce.siat.core.ct.model.PaymentData;

/**
 *
 * @author tadzotsa
 */
public class CteInvoiceVo {

    private File file;

    private PaymentData paymentData;

    private String personToContact;

    private String forwaderNiu;
    private String forwaderName;
    private String forwaderEmail;
    private String forwaderPhone;
    private String forwaderBp;

    private String productTypeCode;
    private String productTypeLabel;

    private String operationType;

    private User signatory;
    private Date signatureDate;

    private InputStream qrCode;

    private List<CteInvoiceFileItemVo> fileItemVosList;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public PaymentData getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(PaymentData paymentData) {
        this.paymentData = paymentData;
    }

    public String getPersonToContact() {
        return personToContact;
    }

    public void setPersonToContact(String personToContact) {
        this.personToContact = personToContact;
    }

    public String getForwaderNiu() {
        return forwaderNiu;
    }

    public void setForwaderNiu(String forwaderNiu) {
        this.forwaderNiu = forwaderNiu;
    }

    public String getForwaderName() {
        return forwaderName;
    }

    public void setForwaderName(String forwaderName) {
        this.forwaderName = forwaderName;
    }

    public String getForwaderEmail() {
        return forwaderEmail;
    }

    public void setForwaderEmail(String forwaderEmail) {
        this.forwaderEmail = forwaderEmail;
    }

    public String getForwaderPhone() {
        return forwaderPhone;
    }

    public void setForwaderPhone(String forwaderPhone) {
        this.forwaderPhone = forwaderPhone;
    }

    public String getForwaderBp() {
        return forwaderBp;
    }

    public void setForwaderBp(String forwaderBp) {
        this.forwaderBp = forwaderBp;
    }

    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public String getProductTypeLabel() {
        return productTypeLabel;
    }

    public void setProductTypeLabel(String productTypeLabel) {
        this.productTypeLabel = productTypeLabel;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public User getSignatory() {
        return signatory;
    }

    public void setSignatory(User signatory) {
        this.signatory = signatory;
    }

    public Date getSignatureDate() {
        return signatureDate;
    }

    public void setSignatureDate(Date signatureDate) {
        this.signatureDate = signatureDate;
    }

    public InputStream getQrCode() {
        return qrCode;
    }

    public void setQrCode(InputStream qrCode) {
        this.qrCode = qrCode;
    }

    public List<CteInvoiceFileItemVo> getFileItemVosList() {
        return fileItemVosList;
    }

    public void setFileItemVosList(List<CteInvoiceFileItemVo> fileItemVosList) {
        this.fileItemVosList = fileItemVosList;
    }

}
