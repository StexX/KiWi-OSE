/**
 * 
 */
package kiwi.webservice;

import kiwi.api.webservice.DataAccessService;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author Karsten Jahn
 *
 */
@Test
public class DataAccessTest extends KiWiTest {
	private DataStructured data[] =  {new DataStructured("Name", "BugsBunny"), 
									  new DataStructured("Ears", "1m")};
	private static final String source = "Test Class: DataAccessTest";
	

	@Test
	public void testStructuredData() {
		String name = "Author";
		String value = "Duffy Duck";
		
		DataStructured data = new DataStructured();
		data.setName(name);
		data.setValue(value);
		
		Assert.assertEquals(name, data.getName());
		Assert.assertEquals(value, data.getValue());
	}
}