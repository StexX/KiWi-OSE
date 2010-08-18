package kiwi.service.query.sparql;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;

import kiwi.api.query.sparql.SparqlUpdateServiceLocal;
import kiwi.api.query.sparql.SparqlUpdateServiceRemote;
import kiwi.api.security.CryptoService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.kbase.KiWiTriple;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * TODO: the parsing process is awful right now... improvement is necessary!!!
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Name("sparqlUpdateService")
@AutoCreate
@Scope(ScopeType.STATELESS)
@Stateless
public class SparqlUpdateServiceImpl implements SparqlUpdateServiceLocal, SparqlUpdateServiceRemote, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// the triple store used by this KiWi system
	@In(value = "tripleStore", create = true)
	private TripleStore tripleStore;
	
	@In(create = true)
	private CryptoService cryptoService;
	
	@In(create = true)
	private UserService userService;
	
	@Logger
	private Log log;
	
	@Override
	public boolean parse(String input, byte[] signature) {
		String[] tokens = input.split(" ");
		int tokenPointer = 0;
		String token = tokens[tokenPointer];
		
		String subject = null;
		String delVar1 = null;
		String delVar3 = null;
		String delProperty = null;
		String delObject = null;
		String insVar1 = null;
		String insProperty = null;
		String insObject = null;
		String insLiteral = null;
		
		while(token.trim().equals("")) {
			tokenPointer++;
			token = tokens[tokenPointer].trim();
		}
		
		if(token.trim().equals("MODIFY")) {
			tokenPointer++;
			token = tokens[tokenPointer].trim();
			while(token.trim().equals("")) {
				tokenPointer++;
				token = tokens[tokenPointer].trim();
			}
			
			if(token.trim().equals("GRAPH")) {
				subject = token.substring(1, token.length()-1);
			}
			tokenPointer++;
			token = tokens[tokenPointer];
			while(token.equals("")) {
				tokenPointer++;
				token = tokens[tokenPointer].trim();
			}
			if(token.startsWith("<") && 
					token.endsWith(">")) {
				subject = token.substring(1, token.length()-1);
			}
		} else {
			return false;
		}
		
		if(!cryptoService.verifySignature(input.getBytes(), signature, userService.getUserByUri(subject))) {
			return false;
		}
		
		
		tokenPointer++;
		token = tokens[tokenPointer].trim();
		while(token.trim().equals("")) {
			tokenPointer++;
			token = tokens[tokenPointer].trim();
		}
		if(token.trim().equals("DELETE")) {
			tokenPointer++;
			token = tokens[tokenPointer].trim();
			while(token.trim().equals("")) {
				tokenPointer++;
				token = tokens[tokenPointer].trim();
			}
			if(token.startsWith("?")) {
				// variable
				delVar1 = token.substring(1);
			}
			tokenPointer++;
			token = tokens[tokenPointer].trim();
			while(token.trim().equals("")) {
				tokenPointer++;
				token = tokens[tokenPointer].trim();
			}
			if(token.startsWith("<") && 
					token.endsWith(">")) {
				delProperty = token.substring(1, token.length()-1);
			}
			tokenPointer++;
			token = tokens[tokenPointer].trim();
			while(token.trim().equals("")) {
				tokenPointer++;
				token = tokens[tokenPointer].trim();
			}
			if(token.startsWith("?")) {
				// variable
				delVar3 = token.substring(1);
			}
		} else {
			return false;
		}
		
		tokenPointer++;
		token = tokens[tokenPointer].trim();
		while(token.trim().equals("")) {
			tokenPointer++;
			token = tokens[tokenPointer].trim();
		}
		if(token.trim().equals("INSERT")) {
			tokenPointer++;
			token = tokens[tokenPointer].trim();
			while(token.trim().equals("")) {
				tokenPointer++;
				token = tokens[tokenPointer].trim();
			}
			if(token.startsWith("?")) {
				// variable
				insVar1 = token.substring(1);
			}
			tokenPointer++;
			token = tokens[tokenPointer].trim();
			while(token.trim().equals("")) {
				tokenPointer++;
				token = tokens[tokenPointer].trim();
			}
			if(token.startsWith("<") && 
					token.endsWith(">")) {
				insProperty = token.substring(1, token.length()-1);
			}
			tokenPointer++;
			token = tokens[tokenPointer].trim();
			while(token.trim().equals("")) {
				tokenPointer++;
				token = tokens[tokenPointer].trim();
			}
			if(token.startsWith("<") && 
					token.endsWith(">")) {
				insObject = token.substring(1, token.length()-1);
			}
			if(token.startsWith("\"") && 
					token.endsWith("\"")) {
				insLiteral = token.substring(1, token.length()-1);
			}
		} else {
			return false;
		}
		
		if(subject == null || delProperty == null) {
			return false;
		}
		List<KiWiTriple> triples2delete = tripleStore.getTriplesBySP(
				tripleStore.createUriResource(subject),
				tripleStore.createUriResource(delProperty));
		
		for(KiWiTriple t : triples2delete) {
			tripleStore.removeTriple(t);
		}
		
		if(subject == null || insProperty == null || 
				(insObject == null && insLiteral == null)) {
			return false;
		}

		if(insObject == null) {
			tripleStore.createTriple(
					tripleStore.createUriResource(subject), 
					tripleStore.createUriResource(insProperty), 
					tripleStore.createLiteral(insLiteral));
		} else {
			tripleStore.createTriple(
					tripleStore.createUriResource(subject), 
					tripleStore.createUriResource(insProperty), 
					tripleStore.createLiteral(insObject));
		}
		
		return true;
	}
	
}
