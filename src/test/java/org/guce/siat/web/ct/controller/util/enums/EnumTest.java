package org.guce.siat.web.ct.controller.util.enums;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;

/**
 *
 * @author tadzotsa
 */
public class EnumTest {

    @Test
    @Ignore
    public void testEnum() {
        System.out.println(DataTypeEnnumeration.CALENDAR.name());
        System.out.println(DataTypeEnnumeration.CALENDAR.getCode());
    }

}
