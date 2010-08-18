package kiwi.api.importexport.importer;

import java.util.Collection;
import java.util.Set;

import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

/**
 * Extension to the standard Importer interface.
 * Specifies the additional method to import temporary 
 * Sesame repository connections into the KiWi triplestore.
 * 
 * @author 	Stephanie Stroka 
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
public interface RDFImporter extends Importer {

	/**
	 * Imports temporary repository 
	 * connections into the KiWi triplestore
	 * 
	 * @param myCon
	 * @param types
	 * @param tags
	 * @param user
	 * @return
	 * @throws RepositoryException
	 */
	public int importDataSesame(RepositoryConnection myCon, 
			Set<KiWiUriResource> types, Set<ContentItem> tags, 
			User user, Collection<ContentItem> output) throws RepositoryException;
	
}
