/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s):
 * sschaffe
 * 
 */
package kiwi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: Please comment on whatever this is...
 * 
 * @author 
 *
 */
public class KiWiStringUtils {

	private static String digits = "0123456789abcdef";
	
	/**
	 * This method adds a space before every html tag and 
	 * replaces spaces in html tags 
	 *
	 * @param s
	 * @return a clean string
	 */
	public static String prepareHtmlText(String s) {
		StringBuffer buf = new StringBuffer();
		boolean tagStart = false;
		for(char c : s.toCharArray()) {
			// if char == '<'
			if(c == '\u003c') {
				buf.append(" <");
				tagStart = true;
			}
			// if the char isn't viewable
			else if(c < 0x20) {
				// do nothing
			}
			// if char == '>'
			else if(c == '\u003e') {
				buf.append("> ");
				tagStart = false;
			}
			// if char == ' ' inside of a html/xml tag
			else if(c == '\u0020' && tagStart) {
				// append replacement character
				buf.append("{attrGap}");
			}
			// else append the current char
			else {
				buf.append(c);
			}
		}
		return buf.toString();
	}
	
	
	/**
	 * @param link
	 * @return
	 */
	public static String extractBrackets(String link){
		String newS = new String();
		for (int i = 0; i < link.length(); i++) {
			 newS = link.replace("\"","");
		}
		return newS.trim();
	}
	
	public static String toHex(byte[] data, int length) {
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i < length; i++) {
			int v = data[i] & 0xff;
			sb.append(digits.charAt(v >> 4));
			sb.append(digits.charAt(v & 0xf));
		}
		
		return sb.toString();
	}
	
	public static byte[] fromHex(String hexdata, int length) {
		byte[] data = new byte[length/2];
		
		int j = 0;
		for(int i=0; i < length; i=i+2) {
			int byteLeft = 0;
			switch(hexdata.charAt(i)) {
			case '0':
				byteLeft = 0;
				break;
			case '1':
				byteLeft = 1;
				break;
			case '2':
				byteLeft = 2;
				break;
			case '3':
				byteLeft = 3;
				break;
			case '4':
				byteLeft = 4;
				break;
			case '5':
				byteLeft = 5;
				break;
			case '6':
				byteLeft = 6;
				break;
			case '7':
				byteLeft = 7;
				break;
			case '8':
				byteLeft = 8;
				break;
			case '9':
				byteLeft = 9;
				break;
			case 'a':
				byteLeft = 10;
				break;
			case 'b':
				byteLeft = 11;
				break;
			case 'c':
				byteLeft = 12;
				break;
			case 'd':
				byteLeft = 13;
				break;
			case 'e':
				byteLeft = 14;
				break;
			case 'f':
				byteLeft = 15;
				break;
			}
			
			int byteRight = 0;
			switch(hexdata.charAt(i+1)) {
			case '0':
				byteRight = 0;
				break;
			case '1':
				byteRight = 1;
				break;
			case '2':
				byteRight = 2;
				break;
			case '3':
				byteRight = 3;
				break;
			case '4':
				byteRight = 4;
				break;
			case '5':
				byteRight = 5;
				break;
			case '6':
				byteRight = 6;
				break;
			case '7':
				byteRight = 7;
				break;
			case '8':
				byteRight = 8;
				break;
			case '9':
				byteRight = 9;
				break;
			case 'a':
				byteRight = 10;
				break;
			case 'b':
				byteRight = 11;
				break;
			case 'c':
				byteRight = 12;
				break;
			case 'd':
				byteRight = 13;
				break;
			case 'e':
				byteRight = 14;
				break;
			case 'f':
				byteRight = 15;
				break;
			}
			
			data[j] = (byte) (byteLeft * 16 + byteRight);
			j++;
		}
		
		return data;
	}
	
	public static String toHexHtml(byte[] data, int length) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<p>");
		sb.append("-----BEGIN AES ENCRYPTED MESSAGE-----<br/><br/>");
		for(int i=0; i < length; i++) {
			if(i % 32 == 0 && i != 0) {
				sb.append("<br/>");
			}
			int v = data[i] & 0xff;
			sb.append(digits.charAt(v >> 4));
			sb.append(digits.charAt(v & 0xf));
		}
		sb.append("<br/><br/>------END AES ENCRYPTED MESSAGE------");
		sb.append("</p>");
		
		return sb.toString();
	}
	
	public static String appendHexSignature(String content, byte[] signature) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(content);
		sb.append("<br/><br/>");
		sb.append("-----BEGIN RSA SIGNATURE-----<br/><br/>");
		for(int i=0; i < signature.length; i++) {
			if(i % 32 == 0 && i != 0) {
				sb.append("<br/>");
			}
			int v = signature[i] & 0xff;
			sb.append(digits.charAt(v >> 4));
			sb.append(digits.charAt(v & 0xf));
		}
		sb.append("<br/><br/>------END RSA SIGNATURE------");
		
		return sb.toString();
	}
	
	public static String extractAESCipher(String formattedAES) {
		if(formattedAES != null) {
			String begin = new String("-----BEGIN AES ENCRYPTED MESSAGE-----");
			String end = new String("------END AES ENCRYPTED MESSAGE------");
			Pattern patternBegin = Pattern.compile("" +
					begin + ".*" + end);
			Matcher m = patternBegin.matcher(formattedAES);
			if(m.find()) {
				String result = m.group();
				result = result.substring(begin.length(), result.length()-end.length());
				result = result.replaceAll("<br/>", "");
				result = result.replaceAll("<br>", "");
				result = result.replaceAll("</br>", "");
				return result;
			}
		}
		return null;
	}
	
	public static String extractRSASignature(String formattedRSA) {
		if(formattedRSA != null) {
			String begin = new String("-----BEGIN RSA SIGNATURE-----");
			String end = new String("------END RSA SIGNATURE------");
			Pattern patternBegin = Pattern.compile("" +
					begin + ".*" + end);
			Matcher m = patternBegin.matcher(formattedRSA);
			if(m.find()) {
				String result = m.group();
				result = result.substring(begin.length(), result.length()-end.length());
				result = result.replaceAll("<br/>", "");
				result = result.replaceAll("<br>", "");
				result = result.replaceAll("</br>", "");
				return result;
			}
		}
		return null;
	}
	
	public static String extractWithoutRSASignature(String formattedRSA) {
		if(formattedRSA != null) {
			String begin = new String("(<br/><br/>|<br></br><br></br>)-----BEGIN RSA SIGNATURE-----");
			String end = new String("------END RSA SIGNATURE------");
			Pattern patternBegin = Pattern.compile("" +
					begin + ".*" + end);
			Matcher m = patternBegin.matcher(formattedRSA);
			if(m.find()) {
				String rsaBlock = m.group();
				String result = formattedRSA.replaceAll(rsaBlock, "");
				return result;
			} else {
				return formattedRSA;
			}
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) {
//		byte test = (byte) 0xff;
//		byte[] data = new byte[] {test};
//		String hex = KiWiStringUtils.toHex(data, data.length);
//		byte[] data_conv = KiWiStringUtils.fromHex(hex, hex.length());
//		String hex2 = KiWiStringUtils.toHex(data_conv, data.length);
//		StringBuilder sb = new StringBuilder();
//		sb.append("bla");
//		sb.append("</p>");
//		System.out.println(sb.toString());
		
		String content = "bla bla";
		byte[] signature = new byte[] {1, 2, 3, 1, 2, 3, 1, 2, 3, 1};
		String output = appendHexSignature(content, signature);
		
		System.out.println(output);
		
		System.out.println("signature: " + extractRSASignature(output));
	}
}
