package org.guce.siat.web.ct.controller.util.enums;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author tadzotsa
 */
public class EnumTest {

    @Test
//    @Ignore
    public void testEnum() {
        System.out.println(DataTypeEnnumeration.CALENDAR.name());
        System.out.println(DataTypeEnnumeration.CALENDAR.getCode());
    }

    @Test
//    @Ignore
    public void test() {
        int nb = 6;
        int id = 5654;
        System.out.println("PR" + repeat("0", nb - Integer.toString(id).length()) + Integer.toString(id));
    }

    private String repeat(String c, int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s += c;
        }
        return s;
    }

}

