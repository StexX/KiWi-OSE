//$Id: RegisterTest.java 2396 2006-10-27 14:04:45Z gavin $
package kiwi.test.action;

import javax.faces.context.FacesContext;

import kiwi.api.content.ContentItemService;

import org.jboss.seam.Component;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

public class RegisterTest extends SeamTest
{
   
   @Test
   public void testLogin() throws Exception
   {
            
      new FacesRequest() {

         @Override
         protected void processValidations() throws Exception
         {
            validateValue("#{kiwi.user.edit.login}", "1ovthafew");
            validateValue("#{kiwi.user.edit.firstName}", "Gavin King");
            validateValue("#{kiwi.user.edit.lastName}", "sadasda");
            validateValue("#{kiwi.user.edit.password}","secret");
            validateValue("#{kiwi.user.edit.email}","abc@gmx.at");

            assert !isValidationFailure();
         }
         
         @Override
         protected void updateModelValues() throws Exception
         {
             setValue("#{kiwi.user.edit.login}", "1ovthafew");
             setValue("#{kiwi.user.edit.firstName}", "Gavin King");
             setValue("#{kiwi.user.edit.lastName}", "sadasda");
             setValue("#{kiwi.user.edit.password}","secret");
             setValue("#{kiwi.user.edit.email}","abc@gmx.at");
         }

         @Override
         protected void invokeApplication()
         {
             invokeMethod("#{kiwi.action.createUser.createUser}");
         }
         

         
      }.run();
      
//      new NonFacesRequest("/registered.jspx")
//      {
//
//         @Override
//         protected void renderResponse()
//         {
//            assert getValue("#{user.username}").equals("1ovthafew");
//            assert getValue("#{user.password}").equals("secret");
//            assert getValue("#{user.name}").equals("Gavin King");
//         }
//         
//      }.run();
//      
//      new FacesRequest("/register.jspx") {
//
//         @Override
//         protected void processValidations() throws Exception
//         {
//            validateValue("#{user.username}", "1ovthafew");
//            validateValue("#{user.name}", "Gavin A King");
//            validateValue("#{user.password}", "password");
//         }
//         
//         @Override
//         protected void updateModelValues() throws Exception
//         {
//            setValue("#{user.username}", "1ovthafew");
//            setValue("#{user.name}", "Gavin A King");
//            setValue("#{user.password}", "password");
//         }
//
//         @Override
//         protected void invokeApplication()
//         {
//            assert invokeMethod("#{register.register}")==null;
//         }
//         
//         @Override
//         protected void renderResponse() throws Exception
//         {
//            assert FacesContext.getCurrentInstance().getMessages().hasNext();
//         }
//         
//         @Override
//         protected void afterRequest()
//         {
//            assert isInvokeApplicationComplete();
//            assert isRenderResponseComplete();
//         }
//         
//      }.run();
      
   }
   
}
