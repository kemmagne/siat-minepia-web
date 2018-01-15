package org.guce.siat.web.ct.controller.util;

/**
 *
 * @author tadzotsa
 */
public class CustumMap {

    private String value;
    private String label;

    public CustumMap() {
    }

    public CustumMap(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
