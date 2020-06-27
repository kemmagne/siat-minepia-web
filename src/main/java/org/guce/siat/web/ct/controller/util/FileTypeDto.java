package org.guce.siat.web.ct.controller.util;

import java.io.Serializable;
import java.util.Map;
import org.guce.siat.common.model.FileType;

/**
 *
 * @author ht
 */
public class FileTypeDto implements Serializable {

    private FileType fileType;
    private Map<String, Object> infos;

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Map<String, Object> getInfos() {
        return infos;
    }

    public void setInfos(Map<String, Object> infos) {
        this.infos = infos;
    }

}
