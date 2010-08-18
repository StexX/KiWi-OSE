/**
 * 
 */
package semForm.webservice.transform;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.render.renderlet.XOMRenderlet;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Rolf Sint
 * 
 */
@Path("/sFormService")
@Name("SFormService")
public class SFormService {

	@Logger
	private static Log log;

	private static Document doc = null;

	@In(create = true)
	private KiWiEntityManager kiwiEntityManager;

	@In
	ContentItemService contentItemService;

	@In(create = true)
	private ContentItem currentContentItem;

	@In(create = true)
	private MappingAction mappingAction;

	@In
	private ConfigurationService configurationService;
	
	@GET
	@Path("saveFields")
	@Produces("text/html")
	public String saveFields(@QueryParam("mode") String mode,
			@QueryParam("field0") String field0,
			@QueryParam("field1") String field1,
			@QueryParam("field2") String field2,
			@QueryParam("field3") String field3,
			@QueryParam("field4") String field4,
			@QueryParam("field5") String field5) {

		log.info("modify #0", mode);

		Map<String, String> sm = clearList(field0, field1, field2, field3,
				field4, field5);

		ContentItem currentContentItem = null;
		
		
		//in the case the radio button is on modify, the current contentitem is modified
		if (!mode.trim().equals("create")) {
			currentContentItem = contentItemService
			.getContentItemByUri(mappingAction.getUri());
			// iterate over all fields, fiel1, field2, ...
			storeFields(sm, currentContentItem);
			
			//add type
			String types = mappingAction.get("types");
			log.info(types);
			
			if(types != null && !types.equals("")){
				ContentItem type = contentItemService.getContentItemByUri(types);
				KiWiUriResource kuri = (KiWiUriResource) type.getResource();
				currentContentItem.addType(kuri);
				kiwiEntityManager.persist(currentContentItem);
			}
			
			return "Values saved successfully ...";
			
		} else {
			//in this case a new contentitem will be created
			String types = mappingAction.get("types");
			log.info("Types #0", types);
			
			ContentItem type = contentItemService.getContentItemByUri(types);
			KiWiUriResource kuri = (KiWiUriResource) type.getResource();
			
			ContentItem nci = contentItemService.createContentItem();
			nci.addType(kuri);
			contentItemService.updateTitle(nci, field0); //convention: the first property is always the name
			kiwiEntityManager.persist(nci);
			
			storeFields(sm, nci);
			
			String ret = "";
			if(nci.getTitle() != null)
				ret = "New contentitem created !";
			else
				ret = "New contentitem "+nci.getTitle()+" created !";
			
			return ret;
			
			}

	}

	/**
	 * @param sm
	 * @param currentContentItem
	 */
	private void storeFields(Map<String, String> sm,
			ContentItem ci) {
		for (String field : sm.keySet()) {
			// retrieve the property of a field from the global HashMap,
			// contains a mapping of fields to the uris, e.g. fiel0,
			// foaf/forename
			String property = mappingAction.get(field);
			log.info(property);

			storeValue(ci, property, sm.get(field));
		}
	}

	private Map<String, String> clearList(String field0, String field1,
			String field2, String field3, String field4, String field5) {
		// add all values to a map, containing fields and the new value of the
		// fields
		// remove fields which have a null value, they were not used in the form
		Map<String, String> sm = new HashMap<String, String>();
		if (field0 != null)
			sm.put("field0", field0);
		if (field1 != null)
			sm.put("field1", field1);
		if (field2 != null)
			sm.put("field2", field2);
		if (field3 != null)
			sm.put("field3", field3);
		if (field4 != null)
			sm.put("field4", field4);
		if (field5 != null)
			sm.put("field5", field5);
		return sm;
	}

	@GET
	@Produces("text/html")
	@Path("transform")
	public String transform(@QueryParam("uri") String uri,
			@QueryParam("formFileName") String formFileName) {
		StringWriter sw = new StringWriter();
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(
							Thread.currentThread().getContextClassLoader()
									.getResourceAsStream(formFileName));

			// add "action" to form tag, e.g. <form types = "foaf:Person">
			Element re = doc.getDocumentElement();
			NodeList nl = re.getElementsByTagName("form");
			Element e = (Element) nl.item(0);
			
			String url = configurationService.getBaseUri();
			log.info("baseurl "+url);
			
			e.setAttribute("action",url+"/seam/resource/services/sFormService/saveFields");
			
			String types = e.getAttribute("types");
			log.info("Transform types #0", types);
			mappingAction.put("types", types);
			
			Element radio = doc.createElement("input");
			radio.setAttribute("type", "radio");
			radio.setAttribute("name", "mode");
			radio.setAttribute("value", "modify");
			radio.setTextContent("modify the current ContentItem");			
			radio.setAttribute("checked", "checked");
			
			Element br = doc.createElement("br");

			Element radio1 = doc.createElement("input");
			radio1.setAttribute("type", "radio");
			radio1.setAttribute("name", "mode");
			radio1.setAttribute("value", "create");
			radio1.setTextContent("create new ContentItems");

			NodeList nl1 = e.getElementsByTagName("input");

			log.info("NodeNameo:" + nl1.item(0).getNodeName());
			log.info("NodeName:" + nl1.item(0).getParentNode().getNodeName());
			log
					.info("NodeName1:"
							+ nl1.item(0).getParentNode().getParentNode()
									.getNodeName());

			e.insertBefore(radio, e.getFirstChild());
			e.insertBefore(br, e.getFirstChild());
			e.insertBefore(radio1, e.getFirstChild());

			int l = nl1.getLength();
			
			
			
			// iterate over all input nodes
			int a = 0;
			for (int i = 0; i < l; i++) {
				Element inputNode = (Element) nl1.item(i);
				
				if (inputNode.hasAttribute("property")) {
					inputNode.setAttribute("name", "field" + a);
					String property = inputNode.getAttribute("property");

					ContentItem currentContentItem = contentItemService
							.getContentItemByUri(uri);

					// get the values from the content item and set them in the
					// form
					String value = getValue(property, currentContentItem);
					inputNode.setAttribute("value", value);
					// remove attribute property
					inputNode.removeAttribute("property");

					// add mapping information from this contentItem (and the
					// the single uri) (field to the property), e.g.
					// field0=foaf:firstname
					mappingAction.put("field" + a, property);
					mappingAction.setUri(uri);
					a++;
				}
			}

			// build new DOM tree
			TransformerFactory tranFact = TransformerFactory.newInstance();
			Transformer transfor = tranFact.newTransformer();
			Node node = doc.getDocumentElement();
			Source src = new DOMSource(node);

			Result dest = new StreamResult(sw);
			transfor.transform(src, dest);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	// saves a value newValue to the contentItem and the given property
	private void storeValue(ContentItem ci, String property,
			String newValue) {
		try {
			ci.getResource().setProperty(property, newValue);
			// update textcontent
			Events.instance().raiseEvent(KiWiEvents.METADATA_UPDATED,
					ci);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getValue(String property, ContentItem currentContentItem) {

		log.info("Title of the current contentItem: "
				+ currentContentItem.getTitle());
		log.info("Property is queried ..." + property);

		String value = "";
		try {
			value = currentContentItem.getResource().getProperty(property);
			log.info(value);
		} catch (NonUniqueRelationException e) {
			e.printStackTrace();
		}
		return value;
	}
}
