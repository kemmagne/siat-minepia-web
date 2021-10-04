package org.guce.siat.web.ct.controller;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author ht
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:spring-config.xml")
//@WebAppConfiguration
//@IntegrationTest("server.port:0")
public class FileItemCctDetailControllerTest {

    @Ignore
    @Test
    public void testSomeMethod() {
    }

//    @Ignore
    @Test
    public void testParseNumber() {
        try {
            String s = "15254.32";
            System.out.println((long) Integer.parseInt(s));
        } catch (NumberFormatException nfe) {
            System.err.println("Error during parsing");
        }
    }

}
