package kiwi.action.kqb;

/**
 * Action to allow the KWQL Query Builder to 
 * save and load queries
 * 
 * 
 * @author Andreas Hartl
 */


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.security.Identity;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.usertype.*;

import java.util.regex.*;

@Stateless
@Name("KQBAction")
public class KQBAction implements KQB
{
	 @PersistenceContext
     private EntityManager em;
	 
	 @In
	 private Identity identity;	// used to get current user name; queries are associated with each user
	
	 private long ID;
	 
	 private String delim = new Character((char)191).toString();   // char code 191 .. this should not occur in kwql or sparql query strings
	 
	 /**
	  * save a query to the database
	  */
   @WebRemote
   public String SaveQuery(String name, String value, boolean overwrite) 
   {
	   // a user must be logged in to be able to save or load queries
	   String username = identity.getUsername();
	   if (username==null)
	   {
		   return ("error: You must be logged in");
	   }
	   String query = "select d.data from KQBDB d where d.name='" + username + "'";
	   String query2 = "select d.ID from KQBDB d where d.name='" + username + "'";
	   List res = null;
	   List res2 = null;
		  try
		  {
			  res = em.createQuery(query).getResultList();
			  res2 = em.createQuery(query2).getResultList();
		  }
		  catch (Exception e)
		  {
			  return e.getMessage();
		  }
		  if (res == null)
			  return "error occured";
		 
		  Pattern p1 = Pattern.compile("^[a-zA-Z0-9_]+$");
		  Pattern p2 = Pattern.compile("^[a-zA-Z0-9_\"\\(\\)*<>,.|!#+-\\\\@ ]+$");	  
		  Matcher m1 = p1.matcher(name);
		  Matcher m2 = p2.matcher(value);
		  if (!m1.matches())
			  return "error: " + name + " is not a valid name. Please use only alphanumeric characters or the underscore";
		  if (!m2.matches())
			  return "error: " + value + " does not comply with the regular expression for characters allowed in kwql code";
		  
	   if (res.size()==0)
	   {
		   String str = name + delim + value+delim;
		   
		   KQBDB db = new KQBDB(username, str);
		   try
		   {
			   em.persist(db);
			   em.flush();
		   }
		   catch (Exception e)
		   {
			   return "exception: " + e.getMessage();
		   }
		   return "query saved";
	   }
	   else
	   {
		   this.ID = (Long)(res2.get(0));
		   String input = res.get(0).toString();
 
		   KQBDB test = null;
		   try
		   {
			   test = em.find(KQBDB.class, this.ID);
			   if (test==null) return "error, username not found: " + username;
			   // if a query with the given name already exists, delete it first if we
			   // have to overwrite it
			   if (overwrite == true)
			   {
				   String data = input;
				   int i = data.indexOf(name+delim);
				   if (i < 0)
					   return "error: no saved queries found";
				   int i2 = data.indexOf(delim, i + name.length() + 1);
				   
				   String n = "";

					   if (i>0)
						   n += data.substring(0, i);
					   if (i2 < data.length()-1)
						   n += data.substring(i2+1, data.length());
					   input = n;
			   }
			   input = input + name + delim + value + delim;
			   test.setData(input);
			   em.flush();
		   }
		   catch (Exception e)
		   {
			   return "exception: " + e.getMessage();
		   }
		   
		   return "query saved";
		   
	   }
   }
 
   /**
    * returs the query data of a user in the form its saved in the DB,
    * e.g. name query pairs delimited by delim
    */
   @WebRemote
   public String ShowQueries(String dummy)
   {
	   String username = identity.getUsername();
	
	   if (username==null)
	   {
		   return ("notloggedin");
	   }

	  String query = "select d.data from KQBDB d where d.name='" + username + "'";
	  List res = null;

	  try
	  {
		  em.flush();
		  res = em.createQuery(query).getResultList();
	  }
	  catch (Exception e)
	  {
		  return "error: exception while accessing database";
	  }
	  if (res == null)
		  return "error: result is null";
     return res.get(0).toString();

   }
   
   /**
    * 
    * delete an existing query
    */
   @WebRemote
   public String DeleteQuery (String name)
   {
	   String username = identity.getUsername();
	   if (username==null)
	   {
		   return ("error: You must be logged in");
	   }
	   
	   String query2 = "select d.ID from KQBDB d where d.name='" + username + "'";
	   List res2 = null;
		  try
		  {
			  res2 = em.createQuery(query2).getResultList();
		  }
		  catch (Exception e)
		  {
			  return e.getMessage();
		  }
		  if (res2 == null)
			  return "error occured";
	   
	   KQBDB test = null;
	   try
	   {
		   long id = (Long)(res2.get(0));
		   test = em.find(KQBDB.class, id);
		   if (test==null) return "error: user not found: " + username;

		   String data = test.getData();
		   int i = data.indexOf(name+delim);
		   if (i < 0)
			   return "error: no saved queries found";
		   int i2 = data.indexOf(delim, i + name.length() + 1);
		   
		   String n = "";

			   if (i>0)
				   n += data.substring(0, i);
			   if (i2 < data.length()-1)
				   n += data.substring(i2+1, data.length());

		   test.setData(n);
		   em.flush();
		  
	   }
	   catch (Exception e)
	   {
		   return "error";
	   }
	   return "query deleted";
	 
	 
   }
   

}