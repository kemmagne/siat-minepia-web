package org.guce.siat.web.ct.controller.util.enums;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 *
 * @author tadzotsa
 */
public class EnumTest {

    private static final ShaPasswordEncoder ENCODER = new ShaPasswordEncoder(256);

    @Ignore
    @Test
    public void test() {
        String reportNumber = (5 + 1) + "/" + java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) + "/2015/CCT/CPE/MINADER";
        System.out.println(reportNumber);
    }

    @Ignore
    @Test
    public void testStringBuilder() {
        StringBuilder builder = new StringBuilder();
        builder.append("POIDS_BRUT");
        Assert.assertTrue(builder.length() > 0);
        builder.delete(0, builder.length());
        Assert.assertTrue(builder.length() == 0);
    }

    @Ignore
    @Test
    public void testPattern() {
        String dataTypeProps = "pattern=dd-MM-yyyy HH:mm";
        String pattern = "dd-MM-yyyy";
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(dataTypeProps));
            pattern = properties.getProperty(DataTypePropEnum.PATTERN.getCode(), pattern);
        } catch (IOException ex) {
            System.err.println("Problem occured when trying to load properties of data type : ");
        }
        System.out.println(pattern);
        System.out.println(new SimpleDateFormat(pattern).format(new Date()));
    }

    @Ignore
    @Test
    public void testFormat() {
        //new DecimalFormat(formatPrefix + "000000").format(addedFile.getId())

    }

    @Ignore
    @Test
    public void testSplit() {
        final String containersNumbers = "Numéro,Type,Scellés,Volume,Tonnage,Marque,Quantité colis,Essence;ERDE9869858,10,SC785,74,51,SC8712,10,DEN;DERF9875896,20,SC9586,74,85,SC,10,DEN41;EASZ9856325,10,SC45,485,84,SC,10,DEN;";
        final String[] tab1 = containersNumbers.split(";");
        final int size = tab1.length;
        final int positionScelle = "Type".equalsIgnoreCase(tab1[0].split(",")[1]) ? 2 : 1;
        final StringBuilder builder = new StringBuilder();
        for (int i = 1; i < size; i++) {
            if (StringUtils.isBlank(tab1[i])) {
                continue;
            }
            final String[] tab2 = tab1[i].split(",");
            builder.append(tab2[0]).append("/").append(tab2[positionScelle]).append(" ");
        }
        System.out.println(builder.substring(0, builder.lastIndexOf(" ")));
    }

    @Ignore
    @Test
    public void testOrder() {
        List<TestObject> objects = new ArrayList<>();
        objects.add(new TestObject(1, "a"));
        objects.add(new TestObject(10, "a"));
        objects.add(new TestObject(5, "a"));
        objects.add(new TestObject(4, "a"));
        objects.add(new TestObject(3, "a"));
        System.out.println(objects);
        Collections.sort(objects, new Comparator<TestObject>() {
            @Override
            public int compare(TestObject o1, TestObject o2) {
                return o2.getId() - o1.getId();
            }
        });
        System.out.println(objects);
    }

//    @Ignore
    @Test
    public void test01() {

        final List<String> logins = Arrays.asList("MINADER-AG-AR-CC", "MINADER-AG-AR-BOIS", "MINADER-AG-AR-AUTRES",
                "MINADER-AG-INSP-CC", "MINADER-AG-INSP-BOIS", "MINADER-AG-INSP-AUTRES", "MINADER-AG-AGENT",
                "MINADER-AG-SIGN-CC", "MINADER-AG-SIGN-BOIS", "MINADER-AG-SIGN-AUTRES", "MINADER-AG-GA");
        final List<String> logins1 = Arrays.asList("CCIMA", "EKATA", "MOMONOUNI", "GOUROUMAHA", "AYABI", "NGANOGO",
                "PERABI", "NZOKOA", "SENABIONO", "EMPOAGHAM", "KENFACKJP", "NIMAYA", "KAMDEM", "ZEBAZE",
                "AKONO", "TONYE", "JAMMULUMI", "KONGUEP", "FOKOUEN", "IJANGFONGOH", "TOUANDOP", "TAZOJACOB", "MEBIAME");
        Arrays.asList("PPPDLAP-SUPER").forEach((login) -> {
            generateUpdateDb(login, "root");
        });
    }

    private static void generateUpdateDb(final String login, final String password) {
        String q = "UPDATE USERS SET FIRST_LOGIN = 0, PASSWORD='" + ENCODER.encodePassword(password, login) + "' WHERE LOGIN='" + login + "';";
        System.out.println(q);
    }

    private class TestObject {

        private int id;
        private String value;

        public TestObject(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return id + "";
        }

    }

}
