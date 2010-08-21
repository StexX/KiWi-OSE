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
 * 
 * 
 */
package kiwi.evaluation;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kiwi.model.content.ContentItem;

/**
 * This Class calculates precision/recall metrics
 * @author Fred Durao
 *
 */
public class Performance {

	/**
	 * 
	 */
	static double totalPre = 0;
	
	/**
	 * 
	 */
	static double totalRec = 0;
	
	/**
	 * 
	 */
	static List<PerformanceMetrics> performanceMetrics = new ArrayList<PerformanceMetrics>();

	/**
	 * 
	 */
	static PerformanceMetrics performanceMetric = new PerformanceMetrics();		

	/**
	 * @param returns
	 * @param expectedItems
	 * @param searchRanking
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public static void processContentItemMetrics(List<ContentItem> returns, List<ContentItem> expectedItems, Ranking searchRanking) throws Exception, InterruptedException {		
		int i = 0;
		while (i<1) {
			try {
				double inter = getContentItemIntersection(expectedItems, returns);
				double precision = 0;
				double recall = 0;
				
				
				if (inter != 0) {
					precision = inter / returns.size();
					if (precision>1d) {
						precision = 1d;
					}
					recall = inter / expectedItems.size();
				}

				performanceMetric = new PerformanceMetrics(getNumberFormated(precision, 2),getNumberFormated(recall, 2));
				 performanceMetrics.add(performanceMetric);
				 searchRanking.setPrecision((float)getNumberFormated(precision, 2));
				 searchRanking.setRecall((float)getNumberFormated(recall, 2));

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
			i++;
		}
	}		
	
	
	/**
	 * @param returns
	 * @param expectedItems
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public static void processMetrics(List<String> returns,List<String> expectedItems) throws Exception, InterruptedException {		
		int i = 0;
		while (i<1) {
			try {
				double inter = getItemIntersection(expectedItems, returns);
				double precision = 0;
				double recall = 0;
				if (inter != 0) {
					precision = inter / returns.size();
					if (precision>1d) {
						precision = 1d;
					}
					recall = inter / expectedItems.size();
				}

				performanceMetric = new PerformanceMetrics(getNumberFormated(precision, 2),getNumberFormated(recall, 2));
				 performanceMetrics.add(performanceMetric);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
			i++;
		}
	}	
	
	
	/**
	 * @param pNumber
	 * @param scale
	 * @return
	 */
	private static double getNumberFormated(double pNumber, int scale) {
		double currency = new BigDecimal(pNumber).setScale(scale,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		return currency;
	}

	/**
	 * @param files
	 * @param assets
	 * @return
	 */
	private static double getItemIntersection(List<String> returns, List<String> expectedItems) {
		double result = 0;
		for (String returnedItem : returns) {
			for (String expectedItem : expectedItems) {
				try {
					if (returnedItem.toLowerCase().trim().equals(expectedItem.toLowerCase().trim())) {
						result++;
					}
				} catch (Exception e) {
					System.out.println("ERROR: " + returnedItem + " - " + expectedItem);
				}
			}
		}
		return result;
	}	
	
	/**
	 * @param files
	 * @param assets
	 * @return
	 */
	private static double getContentItemIntersection(List<ContentItem> returns, List<ContentItem> expectedItems) {
		double result = 0;
		for (ContentItem returnedItem : returns) {
			for (ContentItem expectedItem : expectedItems) {
				try {
					if (returnedItem.getId()==expectedItem.getId()) {
						result++;
					}
				} catch (Exception e) {
					System.out.println("ERROR: " + returnedItem.getTitle() + " - " + returnedItem.getTitle());
				}
			}
		}
		return result;
	}		
}



/**
 * @author Administrator
 *
 */
class PerformanceMetrics {
	
	private Double precision;
	
	private Double recall;
	
	public PerformanceMetrics() {
		
	}
	
	public PerformanceMetrics(Double precision,Double recall) {
		this.precision = precision;
		this.recall = recall;
	}
	
	public Double getPrecision() {
		return precision;
	}
	public void setPrecision(Double precision) {
		this.precision = precision;
	}
	public Double getRecall() {
		return recall;
	}
	public void setRecall(Double recall) {
		this.recall = recall;
	}
	
}