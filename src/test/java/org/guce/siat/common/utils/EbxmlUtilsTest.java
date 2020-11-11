package org.guce.siat.common.utils;

import java.io.File;
import java.util.Map;

/**
 *
 * @author ht
 */
public class EbxmlUtilsTest {

    public static void main(String[] args) throws Exception {

        String path = "C:\\Users\\ht\\Downloads\\20201027-130317-94846@172.24.2.20.ebxml";
        byte[] bytes = org.apache.commons.io.FileUtils.readFileToByteArray(new File(path));
        Map<String, Object> map = EbxmlUtils.ebxmlToMap(bytes);
    }

}
