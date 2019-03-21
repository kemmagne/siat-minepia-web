package org.guce.siat.web.ct.controller.util.enums;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 *
 * @author ayefou
 */
public class EncodePassword {

    public static void main(String[] args) {
        final ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);

        System.out.println("Password for MINEPDED-AG-SRAV-CAISSIER-SUP : ");
        System.out.println(encoder.encodePassword("root", "MINEPDED-AG-SRAV-CAISSIER-SUP"));

        System.out.println("Password for MINSANTE-AG-BH-AR : ");
        System.out.println(encoder.encodePassword("root", "MINSANTE-AG-BH-AR"));

        System.out.println("Password for ONCC-SUPER : ");
        System.out.println(encoder.encodePassword("root", "ONCC-SUPER"));
    }
}

