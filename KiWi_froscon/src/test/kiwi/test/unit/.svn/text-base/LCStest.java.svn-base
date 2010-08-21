package kiwi.test.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kiwi.service.revision.LCS;

import org.junit.Test;

public class LCStest {

	private String original;
	private List<String> modifications;
	private List<int[][]> resultMatrices;
	
	private String modifiedForDelete;
	private List<String> originalForDelete;
	private List<int[][]> resultMatricesForDelete;
	
	private String originalXML;
	private String modificationXML;
	private int[][] resultMatrixXML = {	
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 1, 2, 2, 2, 2, 2, 2, 2},
			{0, 1, 2, 3, 3, 3, 3, 3, 3},
			{0, 1, 2, 3, 3, 3, 3, 3, 3},
			{0, 1, 2, 3, 3, 3, 3, 3, 3},
			{0, 1, 2, 3, 3, 3, 3, 3, 3},
			{0, 1, 2, 3, 4, 4, 4, 4, 4},
			{0, 1, 2, 3, 4, 5, 5, 5, 5},
			{0, 1, 2, 3, 4, 5, 6, 6, 6}, 
			{0, 1, 2, 3, 4, 5, 6, 7, 7},
			{0, 1, 2, 3, 4, 5, 6, 7, 8}	};
	private Map<Integer,Map<String,Integer>> xPathsPositions;
	
	private String originalXMLmarek;
	private String modificationXMLmarek;
	private int[][] resultMatrixXMLmarek = {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
			{0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
			{0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
			{0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, 
			{0, 1, 2, 3, 4, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 25, 25, 25, 25, 25, 25, 25},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 25, 26, 26, 26, 26, 26, 26},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 25, 26, 27, 27, 27, 27, 27},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 25, 26, 27, 28, 28, 28, 28},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 25, 26, 27, 28, 29, 29, 29},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 25, 26, 27, 28, 29, 30, 30},
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 25, 26, 27, 28, 29, 30, 31}
	};
	private Map<Integer,Map<String,Integer>> xPathsPositionsmarek;
	
	public LCStest() {
		original = new String("eins zwei drei vier fünf");
		modifiedForDelete = new String("eins zwei drei vier fünf");
		originalXML = new String("<div{attrGap}id=page> <p> paragraph <b> bold </b> </p> <p> nextParagraph </p> </div>");
		xPathsPositions = new HashMap<Integer,Map<String,Integer>>();
		initModifications();
		initOriginalForDelete();
		initModificationsXML();
		
		/* Testing the problem that Marek found */
		originalXMLmarek = "<div{attrGap}xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\"{attrGap}kiwi:type=\"page\"> " +
				"<p> Hello </p> <p> My name is <kiwi:fragment{attrGap}uri=\"http://127.0.0.1:8080/KiWi/content/487d00d8-37ba-45e5-b1df-d6d4a1590f30\"> " +
				"Marek Smurfette </kiwi:fragment> . </p> <p> Lorem ipsum dolor sit amet, </p> <p> sincerely, " +
				"<kiwi:fragment{attrGap}uri=\"http://127.0.0.1:8080/KiWi/content/487d00d8-37ba-45e5-b1df-d6d4a1590f30\"> " +
				"Marek Smurfette </kiwi:fragment> . </p> <p> Bye! </p> </div>";
		initModificationsXMLmarek();
	}
	
//	@Test
//	public void testCalcLCS() {
//		
//		/* Tests modified and added words */
//		List<String> origWords = new ArrayList<String>( 
//				Arrays.asList(original.split(" ") ));
//		assertEquals(modifications.size(), resultMatrices.size());
//		for(int s=0; s < modifications.size(); s++) {
//			String modified = modifications.get(s);
//			List<String> modWords = new ArrayList<String>( 
//					Arrays.asList(modified.split(" ") ));
//			int[][] result = null;
//			try {
//				result = LCS.calcLCS(origWords, modWords, null);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(result != null) {
//				for(int i=0; i <= origWords.size(); i++) {
//					for(int j=0; j <= modWords.size(); j++) {
//						if(result[i][j] != resultMatrices.get(s)[i][j]) {
//							fail("calcLCS returned unexpected matrix for original string '" + 
//									original + "' and modified string '" + modified + "'");
//						} else {
//							//System.out.println("Strings tested: " + original + ", " + modified);
//						}
//					}
//				}
//			}
//		}
//		
//		/* Tests deleted words */
//		List<String> modWords = new ArrayList<String>( 
//				Arrays.asList(modifiedForDelete.split(" ") ));
//		assertEquals(originalForDelete.size(), resultMatricesForDelete.size());
//		for(int s=0; s < originalForDelete.size(); s++) {
//			String original = originalForDelete.get(s);
//			List<String> origWordsForD = new ArrayList<String>( 
//					Arrays.asList(original.split(" ") ));
//			int[][] result = null;
//			try {
//				result = LCS.calcLCS(origWordsForD, modWords, null);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(result != null) {
//				for(int i=0; i <= origWordsForD.size(); i++) {
//					for(int j=0; j <= modWords.size(); j++) {
//						if(result[i][j] != resultMatricesForDelete.get(s)[i][j]) {
//							fail("calcLCS returned unexpected matrix for original string '" + 
//									original + "' and modified string '" + modifiedForDelete + "'");
//						} else {
//							//System.out.println("Strings tested: " + original + ", " + modified);
//						}
//					}
//				}
//			}
//		}
//		
//		/* Tests XML texts */
//		List<String> origXMLWords = new ArrayList<String>( 
//				Arrays.asList(originalXML.split(" ") ));
//
//		List<String> modWordsXML = new ArrayList<String>( 
//				Arrays.asList(modificationXML.split(" ") ));
//		int[][] result = null;
//		try {
//			Map<Integer, Map<String,Integer>> tmpXPathsForWord = 
//				new HashMap<Integer, Map<String,Integer>>();
//			result = LCS.calcLCS(origXMLWords,modWordsXML,tmpXPathsForWord);
//			for(Integer i : tmpXPathsForWord.keySet()) {
//				// TODO test map
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(result != null) {
//			for(int i=0; i <= origXMLWords.size(); i++) {
//				for(int j=0; j <= modWordsXML.size(); j++) {
//					if(result[i][j] != resultMatrixXML[i][j]) {
//						fail("calcLCS returned unexpected matrix for original string '" + 
//								originalXML + "' and modified string '" + modificationXML + "'");
//					} else {
//						//System.out.println("Strings tested: " + original + ", " + modified);
//					}
//				}
//			}
//		}
//		
//		/* Tests Mareks problem */
//		List<String> origMarek = new ArrayList<String>( 
//				Arrays.asList(originalXMLmarek.split(" ") ));
//
//		List<String> modMarek = new ArrayList<String>( 
//				Arrays.asList(modificationXMLmarek.split(" ") ));
//		int[][] result2 = null;
//		try {
//			Map<Integer, Map<String,Integer>> tmpXPathsForWord = 
//				new HashMap<Integer, Map<String,Integer>>();
//			result2 = LCS.calcLCS(origMarek,modMarek,tmpXPathsForWord);
//			for(Integer i : tmpXPathsForWord.keySet()) {
//				// TODO test map
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(result2 != null) {
//			for(int i=0; i <= origMarek.size(); i++) {
//				for(int j=0; j <= modMarek.size(); j++) {
//					if(result2[i][j] != resultMatrixXMLmarek[i][j]) {
//						fail("calcLCS returned unexpected matrix for original string '" + 
//								originalXMLmarek + "' and modified string '" + modificationXMLmarek + "'");
//					} else {
//						//System.out.println("Strings tested: " + original + ", " + modified);
//					}
//				}
//			}
//		}
//	}

	@Test
	public void testReadChanges() {
		/* modified and added words */
//		List<String> origWords = new ArrayList<String>( 
//				Arrays.asList(original.split(" ") ));
//		assertEquals(modifications.size(), resultMatrices.size());
//		for(int i=0; i < resultMatrices.size(); i++) {
//			String modified = modifications.get(i);
//			List<String> modWords = new ArrayList<String>( 
//					Arrays.asList(modified.split(" ") ));
//			LCS.readChanges(resultMatrices.get(i), origWords, modWords, new HashMap<Integer,Map<String,Integer>>());
//		}
//		
//		/* deleted words */
//		List<String> modWords = new ArrayList<String>( 
//				Arrays.asList(modifiedForDelete.split(" ") ));
//		assertEquals(originalForDelete.size(), resultMatricesForDelete.size());
//		for(int i=0; i < resultMatricesForDelete.size(); i++) {
//			String original = originalForDelete.get(i);
//			List<String> origWordsForD = new ArrayList<String>( 
//					Arrays.asList(original.split(" ") ));
//			LCS.readChanges(resultMatricesForDelete.get(i), origWordsForD, modWords,new HashMap<Integer,Map<String,Integer>>());
//		}
//		
//		/* xml texts */
//		List<String> origWordsXML = new ArrayList<String>( 
//				Arrays.asList(originalXML.split(" ") ));
//		List<String> modWordsXLM = new ArrayList<String>( 
//				Arrays.asList(modificationXML.split(" ") ));
//		LCS.readChanges(resultMatrixXML, origWordsXML, modWordsXLM, xPathsPositions);
		
		/* mareks test */
		List<String> origWordsXMLmarek = new ArrayList<String>( 
				Arrays.asList(originalXMLmarek.split(" ") ));
		List<String> modWordsXLMmarek = new ArrayList<String>( 
				Arrays.asList(modificationXMLmarek.split(" ") ));
		LCS.diff(origWordsXMLmarek, modWordsXLMmarek);
	
	}
	
	private void initModifications() {
		modifications = new ArrayList<String>();
		resultMatrices = new ArrayList<int[][]>();
		
		// equal
		modifications.add("eins zwei drei vier fünf");
		int[][] m1 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 5}	};
		resultMatrices.add(m1);
		
		// modified front
		modifications.add("sechs zwei drei vier fünf");
		int[][] m2 = {	{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 1, 1, 1, 1},
						{0, 0, 1, 2, 2, 2},
						{0, 0, 1, 2, 3, 3},
						{0, 0, 1, 2, 3, 4}	};
		resultMatrices.add(m2);
		modifications.add("sechs sieben drei vier fünf");
		int[][] m3 = {	{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 1, 1, 1},
						{0, 0, 0, 1, 2, 2},
						{0, 0, 0, 1, 2, 3}	};
		resultMatrices.add(m3);
		modifications.add("sechs sieben acht vier fünf");
		int[][] m4 = {	{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 1, 1},
						{0, 0, 0, 0, 1, 2}	};
		resultMatrices.add(m4);
		modifications.add("sechs sieben acht neun fünf");
		int[][] m5 = {	{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 1}	};
		resultMatrices.add(m5);
		modifications.add("sechs zwei sieben acht fünf");
		int[][] m6 = {	{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 1, 1, 1, 1},
						{0, 0, 1, 1, 1, 1},
						{0, 0, 1, 1, 1, 1},
						{0, 0, 1, 1, 1, 2}	};
		resultMatrices.add(m6);
		
		// modified back
		modifications.add("eins zwei drei vier sechs");
		int[][] m7 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 4}	};
		resultMatrices.add(m7);
		modifications.add("eins zwei drei sechs sieben");
		int[][] m8 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 3, 3}	};
		resultMatrices.add(m8);
		modifications.add("eins zwei sechs sieben acht");
		int[][] m9 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2}	};
		resultMatrices.add(m9);
		modifications.add("eins sechs sieben acht neun");
		int[][] m10 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1}	};
		resultMatrices.add(m10);
		modifications.add("eins sechs drei acht neun");
		int[][] m11 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 2, 2, 2},
						{0, 1, 1, 2, 2, 2},
						{0, 1, 1, 2, 2, 2}	};
		resultMatrices.add(m11);
		
		// modified middle
		modifications.add("eins zwei sechs vier fünf");
		int[][] m12 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 3, 3},
						{0, 1, 2, 2, 3, 4}	};
		resultMatrices.add(m12);
		modifications.add("eins zwei sechs sieben fünf");
		int[][] m13 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 3}	};
		resultMatrices.add(m13);
		modifications.add("eins sechs sieben acht fünf");
		int[][] m14 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 2}	};
		resultMatrices.add(m14);
		modifications.add("eins sechs drei acht fünf");
		int[][] m15 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 2, 2, 2},
						{0, 1, 1, 2, 2, 2},
						{0, 1, 1, 2, 2, 3}	};
		resultMatrices.add(m15);

		// added words at front
		modifications.add("sechs eins zwei drei vier fünf");
		int[][] m16 = {	{0, 0, 0, 0, 0, 0, 0},
						{0, 0, 1, 1, 1, 1, 1},
						{0, 0, 1, 2, 2, 2, 2},
						{0, 0, 1, 2, 3, 3, 3},
						{0, 0, 1, 2, 3, 4, 4},
						{0, 0, 1, 2, 3, 4, 5}	};
		resultMatrices.add(m16);
		modifications.add("sechs sieben eins zwei drei vier fünf");
		int[][] m17 = {	{0, 0, 0, 0, 0, 0, 0, 0},
						{0, 0, 0, 1, 1, 1, 1, 1},
						{0, 0, 0, 1, 2, 2, 2, 2},
						{0, 0, 0, 1, 2, 3, 3, 3},
						{0, 0, 0, 1, 2, 3, 4, 4},
						{0, 0, 0, 1, 2, 3, 4, 5}	};
		resultMatrices.add(m17);
		
		// added words at back
		modifications.add("eins zwei drei vier fünf sechs");
		int[][] m18 = {	{0, 0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3, 3},
						{0, 1, 2, 3, 4, 4, 4},
						{0, 1, 2, 3, 4, 5, 5}	};
		resultMatrices.add(m18);
		modifications.add("eins zwei drei vier fünf sechs sieben");
		int[][] m19 = {	{0, 0, 0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3, 3, 3},
						{0, 1, 2, 3, 4, 4, 4, 4},
						{0, 1, 2, 3, 4, 5, 5, 5}	};
		resultMatrices.add(m19);

		// added words at middle
		modifications.add("eins zwei sechs sieben drei vier fünf");
		int[][] m20 = {	{0, 0, 0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 3, 3, 3},
						{0, 1, 2, 2, 2, 3, 4, 4},
						{0, 1, 2, 2, 2, 3, 4, 5}	};
		resultMatrices.add(m20);
		modifications.add("eins sechs zwei sieben drei vier acht fünf");
		int[][] m21 = {	{0, 0, 0, 0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1, 1, 1, 1},
						{0, 1, 1, 2, 2, 2, 2, 2, 2},
						{0, 1, 1, 2, 2, 3, 3, 3, 3},
						{0, 1, 1, 2, 2, 3, 4, 4, 4},
						{0, 1, 1, 2, 2, 3, 4, 4, 5}	};
		resultMatrices.add(m21);

		// added words at front & middle
		modifications.add("sechs eins zwei sieben drei acht vier fünf");
		int[][] m22 = {	{0, 0, 0, 0, 0, 0, 0, 0, 0},
						{0, 0, 1, 1, 1, 1, 1, 1, 1},
						{0, 0, 1, 2, 2, 2, 2, 2, 2},
						{0, 0, 1, 2, 2, 3, 3, 3, 3},
						{0, 0, 1, 2, 2, 3, 3, 4, 4},
						{0, 0, 1, 2, 2, 3, 3, 4, 5}	};
		resultMatrices.add(m22);
		
		// added words at middle & back
		modifications.add("eins zwei sechs drei sieben vier fünf acht");
		int[][] m23 = {	{0, 0, 0, 0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2, 2, 2, 2},
						{0, 1, 2, 2, 3, 3, 3, 3, 3},
						{0, 1, 2, 2, 3, 3, 4, 4, 4},
						{0, 1, 2, 2, 3, 3, 4, 5, 5}	};
		resultMatrices.add(m23);
	}
	
	private void initOriginalForDelete() {
		
		originalForDelete = new ArrayList<String>();
		resultMatricesForDelete = new ArrayList<int[][]>();
		
		// delete words at front
		originalForDelete.add("sechs eins zwei drei vier fünf");
		int[][] m1 = {	{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 5}	};
		resultMatricesForDelete.add(m1);
		originalForDelete.add("sechs sieben eins zwei drei vier fünf");
		int[][] m2 = {	{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 5}	};
		resultMatricesForDelete.add(m2);
		
		// delete words at back
		originalForDelete.add("eins zwei drei vier fünf sechs");
		int[][] m3 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 5},
						{0, 1, 2, 3, 4, 5}	};
		resultMatricesForDelete.add(m3);
		originalForDelete.add("eins zwei drei vier fünf sechs sieben");
		int[][] m4 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 5},
						{0, 1, 2, 3, 4, 5},
						{0, 1, 2, 3, 4, 5}	};
		resultMatricesForDelete.add(m4);
		
		// delete words at middle
		originalForDelete.add("eins zwei sechs sieben drei vier fünf");
		int[][] m5 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 5}	};
		resultMatricesForDelete.add(m5);
		originalForDelete.add("eins sechs zwei sieben drei vier acht fünf");
		int[][] m6 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 5}	};
		resultMatricesForDelete.add(m6);
		
		// delete words at front & middle
		originalForDelete.add("sechs eins zwei sieben drei acht vier fünf");
		int[][] m7 = {	{0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 5}	};
		resultMatricesForDelete.add(m7);
		
		// delete words at middle & back
		originalForDelete.add("eins zwei sechs drei sieben vier fünf acht");
		int[][] m8 = {	{0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, 1, 1},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 2, 2, 2},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 3, 3},
						{0, 1, 2, 3, 4, 4},
						{0, 1, 2, 3, 4, 5},
						{0, 1, 2, 3, 4, 5}	};
		resultMatricesForDelete.add(m8);
	}
	
	private void initModificationsXML() {
		
		modificationXML = "<div{attrGap}id=page> <p> paragraph </p> <p> nextParagraph </p> </div>";
		
		Map<String,Integer> posInXpath1 = new HashMap<String, Integer>();
		posInXpath1.put("div[1]",0);
		xPathsPositions.put(1, posInXpath1);
		Map<String,Integer> posInXpath2 = new HashMap<String, Integer>();
		posInXpath2.put("div[1]/p[1]", 0);
		xPathsPositions.put(2, posInXpath2);
		Map<String,Integer> posInXpath3 = new HashMap<String, Integer>();
		posInXpath3.put("div[1]/p[1]", 1);
		xPathsPositions.put(3, posInXpath3);
		Map<String,Integer> posInXpath4 = new HashMap<String, Integer>();
		posInXpath4.put("div[1]/p[1]", 2);
		xPathsPositions.put(4, posInXpath4);
		Map<String,Integer> posInXpath5 = new HashMap<String, Integer>();
		posInXpath5.put("div[1]/p[2]", 0);
		xPathsPositions.put(5, posInXpath5);
		Map<String,Integer> posInXpath6 = new HashMap<String, Integer>();
		posInXpath6.put("div[1]/p[2]", 1);
		xPathsPositions.put(6, posInXpath6);
		Map<String,Integer> posInXpath7 = new HashMap<String, Integer>();
		posInXpath7.put("div[1]/p[2]", 2);
		xPathsPositions.put(7, posInXpath7);
		Map<String,Integer> posInXpath8 = new HashMap<String, Integer>();
		posInXpath8.put("div[1]", 1);
		xPathsPositions.put(8, posInXpath8);
	}
	
	private void initModificationsXMLmarek() {
		
		modificationXMLmarek = "<div{attrGap}xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\"{attrGap}kiwi:type=\"page\"> " +
		"<p> Hello </p> <p> My name is <kiwi:fragment{attrGap}uri=\"http://127.0.0.1:8080/KiWi/content/487d00d8-37ba-45e5-b1df-d6d4a1590f30\"> " +
		"Marek Schmidt </kiwi:fragment> . </p> <p> Lorem ipsum dolor sit amet, </p> <p> sincerely, " +
		"<kiwi:fragment{attrGap}uri=\"http://127.0.0.1:8080/KiWi/content/487d00d8-37ba-45e5-b1df-d6d4a1590f30\"> " +
		"Marek Schmidt </kiwi:fragment> . </p> <p> Bye! </p> </div>";
		xPathsPositionsmarek = new HashMap<Integer, Map<String,Integer>>();
		
		Map<String,Integer> posInXpath1 = new HashMap<String, Integer>();
		posInXpath1.put("div[1]",0);
		xPathsPositionsmarek.put(1, posInXpath1);
		Map<String,Integer> posInXpath2 = new HashMap<String, Integer>();
		posInXpath2.put("div[1]/p[1]", 0);
		xPathsPositionsmarek.put(2, posInXpath2);
		Map<String,Integer> posInXpath3 = new HashMap<String, Integer>();
		posInXpath3.put("div[1]/p[1]", 1);
		xPathsPositionsmarek.put(3, posInXpath3);
		Map<String,Integer> posInXpath4 = new HashMap<String, Integer>();
		posInXpath4.put("div[1]/p[1]", 2);
		xPathsPositionsmarek.put(4, posInXpath4);
		Map<String,Integer> posInXpath5 = new HashMap<String, Integer>();
		posInXpath5.put("div[1]/p[2]", 0);
		xPathsPositionsmarek.put(5, posInXpath5);
		Map<String,Integer> posInXpath6 = new HashMap<String, Integer>();
		posInXpath6.put("div[1]/p[2]", 1);
		xPathsPositionsmarek.put(6, posInXpath6);
		Map<String,Integer> posInXpath7 = new HashMap<String, Integer>();
		posInXpath7.put("div[1]/p[2]", 2);
		xPathsPositionsmarek.put(7, posInXpath7);
		Map<String,Integer> posInXpath8 = new HashMap<String, Integer>();
		posInXpath8.put("div[1]/p[2]", 3);
		xPathsPositionsmarek.put(8, posInXpath8);
		Map<String,Integer> posInXpath9 = new HashMap<String, Integer>();
		posInXpath9.put("div[1]/p[2]/kiwi:fragment[1]", 0);
		xPathsPositionsmarek.put(9, posInXpath9);
		Map<String,Integer> posInXpath10 = new HashMap<String, Integer>();
		posInXpath10.put("div[1]/p[2]/kiwi:fragment[1]", 1);
		xPathsPositionsmarek.put(10, posInXpath10);
		Map<String,Integer> posInXpath11 = new HashMap<String, Integer>();
		posInXpath11.put("div[1]/p[2]/kiwi:fragment[1]", 2);
		xPathsPositionsmarek.put(11, posInXpath11);
		Map<String,Integer> posInXpath12 = new HashMap<String, Integer>();
		posInXpath12.put("div[1]/p[2]/kiwi:fragment[1]", 3);
		xPathsPositionsmarek.put(12, posInXpath12);
		Map<String,Integer> posInXpath13 = new HashMap<String, Integer>();
		posInXpath13.put("div[1]/p[2]", 4);
		xPathsPositionsmarek.put(13, posInXpath13);
		Map<String,Integer> posInXpath14 = new HashMap<String, Integer>();
		posInXpath14.put("div[1]/p[2]", 5);
		xPathsPositionsmarek.put(14, posInXpath14);
		Map<String,Integer> posInXpath15 = new HashMap<String, Integer>();
		posInXpath15.put("div[1]/p[3]", 0);
		xPathsPositionsmarek.put(15, posInXpath15);
		Map<String,Integer> posInXpath16 = new HashMap<String, Integer>();
		posInXpath16.put("div[1]/p[3]", 1);
		xPathsPositionsmarek.put(16, posInXpath16);
		Map<String,Integer> posInXpath17 = new HashMap<String, Integer>();
		posInXpath17.put("div[1]/p[3]", 2);
		xPathsPositionsmarek.put(17, posInXpath17);
		Map<String,Integer> posInXpath18 = new HashMap<String, Integer>();
		posInXpath18.put("div[1]/p[3]", 3);
		xPathsPositionsmarek.put(18, posInXpath18);
		Map<String,Integer> posInXpath19 = new HashMap<String, Integer>();
		posInXpath19.put("div[1]/p[3]", 4);
		xPathsPositionsmarek.put(19, posInXpath19);
		Map<String,Integer> posInXpath20 = new HashMap<String, Integer>();
		posInXpath20.put("div[1]/p[3]", 5);
		xPathsPositionsmarek.put(20, posInXpath20);
		Map<String,Integer> posInXpath21 = new HashMap<String, Integer>();
		posInXpath21.put("div[1]/p[3]", 6);
		xPathsPositionsmarek.put(21, posInXpath21);
		Map<String,Integer> posInXpath22 = new HashMap<String, Integer>();
		posInXpath22.put("div[1]/p[4]", 0);
		xPathsPositionsmarek.put(22, posInXpath22);
		Map<String,Integer> posInXpath23 = new HashMap<String, Integer>();
		posInXpath23.put("div[1]/p[4]", 1);
		xPathsPositionsmarek.put(23, posInXpath23);
		Map<String,Integer> posInXpath24 = new HashMap<String, Integer>();
		posInXpath24.put("div[1]/p[4]/kiwi:fragment[1]", 0);
		xPathsPositionsmarek.put(24, posInXpath24);
		Map<String,Integer> posInXpath25 = new HashMap<String, Integer>();
		posInXpath25.put("div[1]/p[4]/kiwi:fragment[1]", 1);
		xPathsPositionsmarek.put(25, posInXpath25);
		Map<String,Integer> posInXpath26 = new HashMap<String, Integer>();
		posInXpath26.put("div[1]/p[4]/kiwi:fragment[1]", 2);
		xPathsPositionsmarek.put(26, posInXpath26);
		Map<String,Integer> posInXpath27 = new HashMap<String, Integer>();
		posInXpath27.put("div[1]/p[4]/kiwi:fragment[1]", 3);
		xPathsPositionsmarek.put(27, posInXpath27);
		Map<String,Integer> posInXpath28 = new HashMap<String, Integer>();
		posInXpath28.put("div[1]/p[4]", 2);
		xPathsPositionsmarek.put(28, posInXpath28);
		Map<String,Integer> posInXpath29 = new HashMap<String, Integer>();
		posInXpath29.put("div[1]/p[4]", 3);
		xPathsPositionsmarek.put(29, posInXpath29);
		Map<String,Integer> posInXpath30 = new HashMap<String, Integer>();
		posInXpath30.put("div[1]/p[5]", 0);
		xPathsPositionsmarek.put(30, posInXpath30);
		Map<String,Integer> posInXpath31 = new HashMap<String, Integer>();
		posInXpath31.put("div[1]/p[5]", 1);
		xPathsPositionsmarek.put(31, posInXpath31);
		Map<String,Integer> posInXpath32 = new HashMap<String, Integer>();
		posInXpath32.put("div[1]/p[5]", 2);
		xPathsPositionsmarek.put(32, posInXpath32);
		Map<String,Integer> posInXpath33 = new HashMap<String, Integer>();
		posInXpath33.put("div[1]", 1);
		xPathsPositionsmarek.put(33, posInXpath33);
		
	}
}
