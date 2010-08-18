package info.kwarc.swim.api.triplestore;

public interface MetadataProvider {

	/**
	 * This function is called by <code>omdoc2html.xsl</code> and returns the value of the
	 * given property on the resource with the given URI.  Internally, the triple store is queried for a set
	 * of triples by subject (the resource) and predicate (the property).  If there is at least one such 
	 * triple, the object of the first one is returned.
	 * 
	 * @param resource_uri the URI of the resource (= subject)
	 * @param property_qname the QName of the property
	 * @return the literal value of the property, if it is a literal, or the URI, if it is a resource, or an empty string otherwise
	 */
	public abstract String getProperty(String resource_uri,
			String property_qname);

}