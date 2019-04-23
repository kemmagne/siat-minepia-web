package org.guce.siat.web.ct.controller.util.enums;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 *
 * @author ayefou
 */
public class EncodePassword {

	public static void main(String[] args) {
		//test01();
		generateUpdateDb();
	}

	private static void test01() {
		final ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);

		System.out.println("Password for MINEPDED-AG-SRAV-CAISSIER-SUP : ");
		System.out.println(encoder.encodePassword("root", "MINEPDED-AG-SRAV-CAISSIER-SUP"));

		System.out.println("Password for MINSANTE-AG-BH-AR : ");
		System.out.println(encoder.encodePassword("root", "MINSANTE-AG-BH-AR"));

		System.out.println("Password for ONCC-SUPER : ");
		System.out.println(encoder.encodePassword("root", "ONCC-SUPER"));
	}

	private static void generateUpdateDb() {
		final ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		String[] logins = new String[]{"CCIMA-CB-LITT-SIGN", "CCIMA-CB-NORD-CO", "CCIMA-CB-NORD-AR", "CCIMA-CB-NORD-AC1", "CCIMA-CB-NORD-TRAIT", "CCIMA-CB-NORD-SIGN", "CCIMA-CB-NORDOU-CO", "CCIMA-CB-NORDOU-AR", "CCIMA-CB-NORDOU-AC1", "CCIMA-CB-NORDOU-TRAIT", "CCIMA-CB-NORDOU-SIGN", "CCIMA-CB-OUEST-CO", "CCIMA-CB-OUEST-AR", "CCIMA-CB-OUEST-AC1", "CCIMA-CB-OUEST-TRAIT", "CCIMA-CB-OUEST-SIGN", "CCIMA-CB-SUD-CO", "CCIMA-CB-SUD-AR", "CCIMA-CB-SUD-AC1", "CCIMA-CB-SUD-TRAIT", "CCIMA-CB-SUD-SIGN", "CCIMA-CB-SUDOU-CO", "CCIMA-CB-SUDOU-AR", "CCIMA-CB-SUDOU-AC1", "CCIMA-CB-SUDOU-TRAIT", "CCIMA-CB-SUDOU-SIGN", "DECLARANT", "CCIMA-CB-ADAM-CAISSIER", "CCIMA-CB-CENTRE-CAISSIER", "CCIMA-CB-EST-CAISSIER", "CCIMA-CB-LITT-CAISSIER", "CCIMA-CB-ENORD-CAISSIER", "CCIMA-SUP", "CCIMA-SUP2", "CCIMA", "ONDOAJ", "TEFAKM", "EKOKAP", "TCHEBETCHOUR", "NKONOH ", "KALAMEU", "KALAMEUSEDAR", "HAWAREC", "DJOMOM", "NGUELEP", "KALAMEUS", "KALAMEUCAI", "KALAMEUSIG", "FODOUOPV", "NKEPWANP", "TCHANQUEO", "CLARISSEOBE", "SEDIMO", "SEDIMO", "HAWAD", "KOUEKOJ", "NTOLOC", "LOEBLAISE", "HALIDOUB", "NDASSAHR", "ROOT", "ADMIN-CCIMA", "CCIMA-MIN", "HAWAD", "ADMINCCIMA", "CCIMA-D-CO", "CCIMA-SD-CO", "CCIMA-CS-CO", "CCIMA-CB-ADAM-CO", "CCIMA-CB-ADAM-AR", "CCIMA-CB-ADAM-AC1", "CCIMA-CB-ADAM-TRAIT", "CCIMA-CB-ADAM-SIGN", "CCIMA-CB-CENTRE-CO", "CCIMA-CB-CENTRE-AR", "CCIMA-CB-CENTRE-AC1", "CCIMA-CB-CENTRE-TRAIT", "CCIMA-CB-CENTRE-SIGN", "CCIMA-CB-EST-CO", "CCIMA-CB-EST-AR", "CCIMA-CB-EST-AC1", "CCIMA-CB-EST-TRAIT", "CCIMA-CB-EST-SIGN", "CCIMA-CB-ENORD-CO", "CCIMA-CB-ENORD-AR", "CCIMA-CB-ENORD-AC1", "CCIMA-CB-ENORD-TRAIT", "CCIMA-CB-ENORD-SIGN", "CCIMA-CB-LITT-CO", "HAWADIALLO", "CCIMA-CB-LITT-AC1", "CCIMA-CB-LITT-TRAIT"};
		for (String login : logins) {
			String q = "UPDATE USERS SET PASSWORD='"+encoder.encodePassword("root", login)+"' WHERE LOGIN='" + login + "';";
			System.out.println(q);
		}
	}
}
