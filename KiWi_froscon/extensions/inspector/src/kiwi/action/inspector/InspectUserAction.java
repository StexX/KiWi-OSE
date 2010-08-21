package kiwi.action.inspector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.importexport.ExportService;
import kiwi.api.user.ProfileService;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.memory.MemoryStore;

@Name("inspectUserAction")
@Scope(ScopeType.PAGE)
@AutoCreate
public class InspectUserAction {

	@Logger
	private Log log;
	
	@In(create = true)
	private User currentUser;
	
	@In("kiwi.core.exportService")
	private ExportService exportService;
	
	@In
	private ProfileService profileService;
	
	private String userRDF;
	
	private boolean editMode = false;
	
	
	public void setUserRDF(String userRDF) {
		this.userRDF = userRDF;
	}
	
	public String getUserRDF() {
		
		if(userRDF == null) {
			userRDF = new String("");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
	
			List<ContentItem> items = new LinkedList<ContentItem>();
			items.add(currentUser.getResource().getContentItem());
			
			exportService.exportItems(items, "application/rdf+xml", out);
			try {
				userRDF += new String(out.toByteArray(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("exception while dumping RDF: #0",e.getMessage());
			}
		}

		return userRDF;
	}

	public boolean isEditMode() {
		return editMode;
	}
	
	public void switchToEditMode() {
		this.editMode = true;
	}
	
	public void switchToReadMode() {
		this.editMode = false;
	}
	
	public void store() {
		
		switchToReadMode();
		
		Repository myRepository = new SailRepository(new MemoryStore());
		RepositoryConnection myCon = null;
		try {
			myRepository.initialize();
		
			myCon = myRepository.getConnection();
			
			if(userRDF != null) {
				StringReader str_reader = new StringReader(userRDF);
				myCon.add(str_reader,
						((KiWiUriResource) currentUser.getResource()).getUri(), 
						RDFFormat.RDFXML);
				
				if(profileService.importProfileInformation(myCon, 
						((KiWiUriResource) currentUser.getResource()).getUri(), 
						currentUser)) {
					log.debug("ProfileService successfully imported data");
				}
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
			log.error("RepositoryException #0 ", e.getMessage());
		} catch (RDFParseException e) {
			e.printStackTrace();
			log.error("RDFParseException #0 ", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("IOException #0 ", e.getMessage());
		} finally {
			try {
				myCon.close();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
	}
}
