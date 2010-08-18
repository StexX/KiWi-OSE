//$Id: RegisterTest.java 2396 2006-10-27 14:04:45Z gavin $
package kiwi.test.action;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

public class GroovyTest extends SeamTest
{
   
   @Test
   public void testGroovy() throws Exception
   {
            
      new FacesRequest() {

         @Override
         protected void invokeApplication()
         {
             invokeMethod("#{groovyTestAction.execute}");

         }
         
      }.run();
  
   }
   
}
