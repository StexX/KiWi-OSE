package kiwi.test.service;

import kiwi.api.content.ContentItemService;
import kiwi.api.rating.RatingService;
import kiwi.api.user.UserService;
import kiwi.exception.TextContentNotChangedException;
import kiwi.exception.UserExistsException;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class RatingServiceTest extends KiWiTest {

	private ContentItem item;
	
 	private User user1 = null, user2 = null, author = null;

	@Test
	public void testSetRating() throws Exception {
    	String[] ontologies = {
    			"ontology_kiwi.owl",
    	};
    	setupDatabase(ontologies);

		/*
		 * Test the ratingService and it's functionalities.
		 */
		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				RatingService rs = (RatingService) Component.getInstance("ratingService");
				

             	UserService        us = (UserService) Component.getInstance("userService");
             	try {
					user1 = us.createUser("kiwiknows1","Szaby", "Grünwald", "1234");
					user2 = us.createUser("kiwiknows2","Rolf", "Sint", "3456");
					author= us.createUser("kiwiauthor","blah", "blah", "ultraSecurePW");
//					us.saveUser(user1);
//					us.saveUser(user2);
//					us.saveUser(author);
				} catch (UserExistsException e) {
				}

				ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
				item = cs.createContentItem();
				item.setAuthor(author);
				cs.updateTitle(item, "TestContentItem");
				cs.updateTextContentItem(item, "<p>some text content</p>");
				item = cs.saveContentItem(item);

				Assert.assertEquals(rs.getNrOfRatings  (item),	0);
				Assert.assertEquals(rs.getRatingAverage(item), 	0.0);
				Assert.assertFalse (rs.userRated(item, user1));
				Assert.assertEquals(item.getRating(), null);
				
				rs.setRating(item, user1, 2);
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				RatingService rs = (RatingService) Component.getInstance("ratingService");

				Assert.assertEquals(rs.getNrOfRatings  (item),	1);
				Assert.assertEquals(rs.getRatingAverage(item), 	2.0);
				Assert.assertTrue  (rs.userRated(item, user1));
				Assert.assertEquals(item.getRating(), rs.getRatingAverage(item));
				
				rs.setRating(item, user2, 3);
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				RatingService rs = (RatingService) Component.getInstance("ratingService");
				
				Assert.assertEquals(rs.getNrOfRatings  (item),	2);
				Assert.assertEquals(rs.getRatingAverage(item), 	2.5);
				Assert.assertEquals(item.getRating(), rs.getRatingAverage(item));
				rs.setRating(item, user2, 5);
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				RatingService rs = (RatingService) Component.getInstance("ratingService");
				
				Assert.assertEquals(rs.getNrOfRatings  (item),	2);
				Assert.assertEquals(rs.getRatingAverage(item), 	3.5);
				Assert.assertEquals(item.getRating(), rs.getRatingAverage(item));
				rs.cancelRating(item, user2);
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				RatingService rs = (RatingService) Component.getInstance("ratingService");
				
				Assert.assertFalse  (rs.userRated(item, user2));
				Assert.assertEquals(rs.getNrOfRatings  (item),	1);
				Assert.assertEquals(rs.getRatingAverage(item), 	2.0);
				Assert.assertEquals(item.getRating(), rs.getRatingAverage(item));
				
				rs.cancelRating(item, user1);
			}
		}.run();

		new FacesRequest() {

			@Override
			protected void invokeApplication() {

				Identity.setSecurityEnabled(false);

				RatingService rs = (RatingService) Component.getInstance("ratingService");
				
				Assert.assertFalse  (rs.userRated(item, user2));
				Assert.assertEquals(rs.getNrOfRatings  (item),	0);
				Assert.assertEquals(rs.getRatingAverage(item), 	0.0);
				Assert.assertEquals(item.getRating(), null);
				
				rs.cancelRating(item, user1);
			}
		}.run();
}
}
