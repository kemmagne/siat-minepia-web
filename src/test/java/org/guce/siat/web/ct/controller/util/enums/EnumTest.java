package org.guce.siat.web.ct.controller.util.enums;

import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author tadzotsa
 */
public class EnumTest {

    @Test
    @Ignore
    public void test() {
        int nb = 6;
        int id = 5654;
        System.out.println("PR" + StringUtils.repeat('0', nb - Integer.toString(id).length()) + Integer.toString(id));
    }

}

