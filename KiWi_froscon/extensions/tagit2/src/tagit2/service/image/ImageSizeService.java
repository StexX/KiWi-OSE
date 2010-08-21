package tagit2.service.image;

import javax.swing.ImageIcon;

import kiwi.model.content.MediaContent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

@Scope(ScopeType.STATELESS)
@Name("tagit2.imageSizeService")
//@Transactional
public class ImageSizeService {
	
	@Logger
	Log log;
	
	public long getImageHeight(MediaContent media, double maxWidth, double maxHeight) {
		
		ImageIcon img = new ImageIcon(media.getData());
		log.info("image has original width: #0 and height: #1", img.getIconWidth(), img.getIconHeight());
		
		double imgHeight = img.getIconHeight();
		double imgWidth = img.getIconWidth();
		
		double origRatio = imgHeight / imgWidth;
		double necRatio = maxHeight / maxWidth;
		
		long height = 100;
		
		if( origRatio > necRatio ) {
			//hight must be min
			if( img.getIconHeight() < maxHeight ) {
				height = Math.round( img.getIconHeight() );
			} else {
				height = Math.round( maxHeight );
			}
		} else {
			if( img.getIconWidth() < maxWidth ) {
				height = Math.round( img.getIconHeight() );
			} else {
				height = Math.round( ( maxWidth / img.getIconWidth() ) * img.getIconHeight() );
			}
		}
		
		log.info("new height is #0", height);
		
		return height;
	}
	
}
