package org.guce.siat.web.ct.controller.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tadzotsa
 */
public class Utils {
    
    public final static String COTONPRODUCTTYPE = "COTON";

    private Utils() {
    }

    public static Map<String, String> getProductTypePackaging() {

        Map<String, String> map = new HashMap<>();

        map.put("CF", "SACS");
        map.put("CC", "SACS");
        map.put("GR", "BILLES");
        map.put("BT", "COLIS");
        map.put("OA", "PIECES");
        map.put("PS", "COLIS");
        map.put("COTON", "BALLES");

        return map;
    }

    public static List<String> getCacaProductsTypes() {
        return Arrays.asList("CF", "CC");
    }

    public static List<String> getWoodProductsTypes() {
        return Arrays.asList("GR", "BT", "OA", "PS");
    }

}
