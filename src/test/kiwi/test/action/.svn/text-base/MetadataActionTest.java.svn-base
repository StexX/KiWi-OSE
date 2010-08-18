

package kiwi.test.action;


import kiwi.test.base.KiWiTest;
import kiwi.wiki.action.MetadataAction;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;


@Test
public class MetadataActionTest extends KiWiTest {

    @Test
    public void test() throws Exception {

// setupDatabase();
        new FacesRequest("/home.xhtml") {
            @Override
            protected void invokeApplication() {
                Identity.setSecurityEnabled(false);

                Component.getInstance("kiwiEntityManager");
                Component.getInstance("entityManager");
                Component.getInstance("tripleStore");

                final MetadataAction mda =
                        (MetadataAction) Component
                                .getInstance("metadataAction");

                Assert.assertNotNull(mda);
                // TODO test if getMetadataSet contains a
// UIMetadata
                // TODO test if saving a new metadata works
                // TODO test if editing metadata works
                // TODO test if removing a metadata works
            }
        }.run();

// shutdownDatabase();
    }

}
