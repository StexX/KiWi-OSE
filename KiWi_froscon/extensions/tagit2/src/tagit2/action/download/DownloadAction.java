package tagit2.action.download;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

/**
 * A bean that allows downloading of multimedia images.
 * 
 * Copied from http://seamframework.org/Documentation/HowToUploadAndDownloadFilesInSeam
 * 
 * @author Sebastian Schaffert
 *
 */
@Scope(ScopeType.STATELESS)
@Name("tagit2.downloadAction")
public class DownloadAction {

	@Logger
	private Log log;
	
	@In
	private EntityManager entityManager;
	
	@In(value="#{facesContext.externalContext}")
	private ExternalContext extCtx;
	
	@In(value="#{facesContext}")
	FacesContext facesContext;
	
	@RequestParameter
	private Long contentItemId;
	
	public String download() {
		ContentItem img = entityManager.find(ContentItem.class, contentItemId);
		if(img != null && img.getMediaContent() != null) {
			HttpServletResponse response = (HttpServletResponse)extCtx.getResponse();
			response.setContentType(img.getMediaContent().getMimeType());
	        response.addHeader("Content-disposition", "attachment; filename=\"" + img.getMediaContent().getFileName() +"\"");
			try {
				ServletOutputStream os = response.getOutputStream();
				os.write(img.getMediaContent().getData());
				os.flush();
				os.close();
				facesContext.responseComplete();
			} catch(Exception e) {
				log.error("\nFailure : " + e.toString() + "\n");
			}
		}
		return null;
	}

}

