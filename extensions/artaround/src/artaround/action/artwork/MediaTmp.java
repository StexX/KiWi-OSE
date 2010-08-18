/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder
 */
package artaround.action.artwork;


/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
public class MediaTmp {

	private String mimeType;
	
	private String fileName;

	public MediaTmp(){
		
	}
	
	public MediaTmp(String mimeType, String fileName){
		this.mimeType = mimeType;
		this.fileName = fileName;
	}
	
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
		
}
