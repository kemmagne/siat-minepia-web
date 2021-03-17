package org.guce.siat.web.reports.exporter;

import org.junit.Test;

/**
 *
 * @author tadzotsa
 */
public class CtCctTreatmentExporterTest {

    @Test
    public void test() {
        int year = 2021;
        String s = String.format("%s/%s/%s/ATP/MINADER/SG/DRCQ", "EP000081EX01C", "CTE000841AT", year); //MessageFormat.format(, );
        System.out.println(s);
    }

}
