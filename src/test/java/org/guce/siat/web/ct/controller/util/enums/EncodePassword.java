/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guce.siat.web.ct.controller.util.enums;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 *
 * @author ayefou
 */
public class EncodePassword {

	public static void main(String[] args) {
		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		System.out.println("Password : ");
		System.out.println(encoder.encodePassword("root", "MINEPDED-AG-SRAV-CAISSIER-SUP"));
	}
}
