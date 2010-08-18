package kiwi.test.service;

import java.io.InputStream;

import kiwi.test.base.KiWiTest;

import org.testng.annotations.Test;

@Test
public class FoafSslAuthenticationTest extends KiWiTest {

	public void testFoafFiles() throws Exception {
		
		new FacesRequest() {
			
			@Override
			protected void invokeApplication() {
				InputStream is = this.getClass()
					.getResourceAsStream("foaf_files/henry_card.rdf");
				
//				FoafSslAuthenticationServlet 
			}
			
		}.run();
	}
	
}
