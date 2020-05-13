package org.guce.siat.web.reports.vo;

import org.guce.siat.common.model.FileItem;

/**
 *
 * @author tadzotsa
 */
public class CteInvoiceFileItemVo {

    private FileItem fileItem;

    private String tradeName;

    private String quantity;
    private String quantityLabel;

    private String packagesCount;
    private String packagesCountLabel;

    public FileItem getFileItem() {
        return fileItem;
    }

    public void setFileItem(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityLabel() {
        return quantityLabel;
    }

    public void setQuantityLabel(String quantityLabel) {
        this.quantityLabel = quantityLabel;
    }

    public String getPackagesCount() {
        return packagesCount;
    }

    public void setPackagesCount(String packagesCount) {
        this.packagesCount = packagesCount;
    }

    public String getPackagesCountLabel() {
        return packagesCountLabel;
    }

    public void setPackagesCountLabel(String packagesCountLabel) {
        this.packagesCountLabel = packagesCountLabel;
    }

}
