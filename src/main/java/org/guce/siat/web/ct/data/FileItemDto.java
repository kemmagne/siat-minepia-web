package org.guce.siat.web.ct.data;

import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;

/**
 *
 * @author tadzotsa
 */
public class FileItemDto {

    public static final String NOM_COMMERCIAL = "NOM_COMMERCIAL";
    public static final String NOM_BOTANIQUE = "NOM_BOTANIQUE";

    private FileItem fileItem;
    private FileItemFieldValue tradeName;
    private FileItemFieldValue botanicalName;

    public FileItem getFileItem() {
        return fileItem;
    }

    public void setFileItem(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    public FileItemFieldValue getTradeName() {
        return tradeName;
    }

    public void setTradeName(FileItemFieldValue tradeName) {
        this.tradeName = tradeName;
    }

    public FileItemFieldValue getBotanicalName() {
        return botanicalName;
    }

    public void setBotanicalName(FileItemFieldValue botanicalName) {
        this.botanicalName = botanicalName;
    }

}
