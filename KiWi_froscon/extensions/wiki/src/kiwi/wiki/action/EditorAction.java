/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * Contributor(s):
 * 
 * 
 */

package kiwi.wiki.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.render.RenderingService;
import kiwi.api.security.CryptoService;
import kiwi.api.user.PasswordGeneratorService;
import kiwi.api.user.UserService;
import kiwi.context.CurrentUserFactory;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;
import kiwi.util.KiWiStringUtils;

import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;


/**
 * 
 * 
 * @author Marek Schmidt
 */

@Name("editorAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
@Synchronized
public class EditorAction implements Serializable {

	private static final long serialVersionUID = -3628049977502899019L;

	@Logger
	private static Log log;
		
	// inject services
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
    private RenderingService renderingPipeline;
	
	@In
	private CryptoService cryptoService;
	
	private boolean renderKeyUpload;
	
    @In(create=true) 
	private User currentUser;
    
    @In
    private PasswordGeneratorService passwordGenerator;
    
    @In
    private ConfigurationService configurationService;

    @In
    private UserService userService;
    
    @In(required=true)
	private ContentItem currentContentItem;
    
    @In 
    FacesMessages facesMessages;
    
    
    private String title;
    private String content;
    private String oldContent;
    
    private boolean encrypt = false;
    private boolean sign = false;
    private String passphrase;
    private File pemFile;
    private KeyPair keyPair;
    
    private User selectedUser;
    private List<User> encryptionUsers;
    private List<User> allUsers;
    
	/**
	 * Start a nested conversation by loading the current content item as editor content.
	 * currentContentEditor is outjected into conversation scope for editing in edit.xhtml; the save button
	 * calls storeContentItem below and uses the currentContentEditor content as the new content item text.
	 * 
	 */
    @Create
    @Begin(join=true)
	public void begin() {
//		log.info("begin nested conversation, parentId is #0", Conversation.instance().getParentId());
//    	Conversation.instance().beginNested();
    	Conversation.instance().setDescription("editorAction, contentItem: " + currentContentItem.getTitle());
		
    	if(currentContentItem.getTextContent() != null) {
			content = renderingPipeline.renderEditor(currentContentItem, currentUser);
			log.info("(conversation: #{conversation.id}) rendering editor content: #0", content);
    	} else {
			content = "<p></p>";   		
			log.info("(conversation: #{conversation.id}) creating empty editor content: #0", content);
    	}
    	oldContent = content;
    	title = currentContentItem.getTitle();
    	
    	if(encryptionUsers == null) {
    		encryptionUsers = new ArrayList<User>();
    	}
    	if(allUsers == null) {
        	allUsers = userService.getUsers();
        	allUsers.remove(userService.getAnonymousUser());	
    	}
    	
	}
	
	
	
	/**
     * Store the current content item from the editor. Ends the nested conversation
     * 
     */
//    @End
    public String storeContentItem() {
    	boolean updated = false;
    	
    	ContentItemService contentItemService = (ContentItemService) Component.getInstance("contentItemService");
    	
    	// update title
    	if(!title.equals(currentContentItem.getTitle())) {
    		log.info("updating content item title from '#0' to '#1'",currentContentItem.getTitle(),title);
    		
    		contentItemService.updateTitle(currentContentItem, title);
    		
    		updated = true;
    	}    	
    	
    	if(currentContentItem.getTextContent() == null || !content.equals(oldContent)) {
	    
    		if(sign) {
    			byte[] signature = cryptoService.signContent(
    					content.getBytes(), currentUser.getPrivateKey());
    			content = KiWiStringUtils.appendHexSignature(content, signature);
    		}
    		
    		if(encrypt) {
    			if(content.startsWith("<p>") && content.endsWith("</p>")) {
    				content = content.substring(3, content.length()-4);
    			}
    			byte[] keyBytes = passwordGenerator.generatePassword(16).getBytes();
//    			byte[] keyBytes = "0123456789abcdef".getBytes();
        		byte[] cipher = cryptoService.encryptSymmetric(content.getBytes(), keyBytes, "AES");
        		
        		// encrypt with currentuser's public key
        		byte[] encryptedKey = cryptoService.encryptAsymmetric(keyBytes, currentUser);
        		// store key
        		String stringValue = KiWiStringUtils.toHex(encryptedKey, encryptedKey.length);
       			configurationService.setUserRSAConfiguration(currentUser,currentUser.getLogin() + "-key:" + currentContentItem.getResource(),stringValue);
        		// encrypt with publik key from users who are allowed to read the text
       			for(User u : encryptionUsers) {
        			byte[] encryptedKeyRecipient = cryptoService.encryptAsymmetric(keyBytes, u);
            		// store key
        			if(encryptedKeyRecipient != null) {
	            		String stringValueRecipient = KiWiStringUtils.toHex(encryptedKeyRecipient, encryptedKeyRecipient.length);
	           			configurationService.setUserRSAConfiguration(u,u.getLogin() + "-key:" + currentContentItem.getResource(),stringValueRecipient);
        			}
        		}
        		
        		content = new String(KiWiStringUtils.toHexHtml(cipher, cipher.length));
        	}
	    	long start = System.currentTimeMillis();
	    	
	    	
	    	// update content (returns true on change)
	    	contentItemService.updateTextContentItem(currentContentItem, content);
				
	    	log.debug("processed parsed content (#0ms)", System.currentTimeMillis()-start);
	    	
//    		currentUser = kiwiEntityManager.merge(currentUser);
    		
    		// update last author
    		currentContentItem.setAuthor(currentUser);
    		
     		// refresh user
//    		kiwiEntityManager.refresh(currentUser);
    		
    		
    		//Conversation.instance().end();
    	
    		log.info("stored new version of content item with kiwi id #0",currentContentItem.getResource().getKiwiIdentifier());
    		
    		updated = true;
     	}
    	
    	if(updated) {
    		// update modification
    		currentContentItem.setModified(new Date());
    		
    		contentItemService.saveContentItem(currentContentItem);
    		
    		return "success";
    	} else {
    		return "";
    	}
    }

    public void cancel() {
//    	Conversation.instance().endAndRedirect();
    }

	/**
	 * @return the currentContentEditor
	 */
	public String getContent() {
		return content;
	}



	/**
	 * @param currentContentEditor the currentContentEditor to set
	 */
	public void setContent(String currentContentEditor) {
		log.info("(conversation: #{conversation.id}) setContent #0", currentContentEditor);
		
		this.content = currentContentEditor;
	}

	/**
	 * @return the currentContentTitle
	 */
	public String getTitle() {
		return title;
	}



	/**
	 * @param currentContentTitle the currentContentTitle to set
	 */
	public void setTitle(String currentContentTitle) {
		this.title = currentContentTitle;
	}



	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}



	public boolean isEncrypt() {
		return encrypt;
	}
	
	public boolean isSign() {
		return sign;
	}



	public void setSign(boolean sign) {
		this.sign = sign;
	}



	public String getPassphrase() {
		return passphrase;
	}
	
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	
	public List<User> getEncryptionUsers() {
		return encryptionUsers;
	}

	public void setEncryptionUsers(List<User> encryptionUsers) {
		this.encryptionUsers = encryptionUsers;
	}
	
	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}



	public void addToEncryptionUsers() {
		encryptionUsers.add(selectedUser);
		allUsers.remove(selectedUser);
	}

	public String checkPEMfile() {
		try {
			PEMReader pemreader = new PEMReader(
					new FileReader(pemFile), new DefaultPasswordFinder(
							passphrase.toCharArray()));
			keyPair = (KeyPair) pemreader.readObject();
//			System.out.println("priv key alg: " + keyPair.getPrivate().getAlgorithm());
//			System.out.println("pub key alg: " + keyPair.getPublic().getAlgorithm());
			
			currentUser.setPublicKey(keyPair.getPublic());
			currentUser.setPrivateKey(keyPair.getPrivate());
			facesMessages.add("Successfully uploaded PEM file.");
			return "success";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			facesMessages.add("The PEM file you provided could not be read. Please upload a correct PEM file.");
			return "/wiki/edit.xhtml";
		} catch (IOException e) {
			e.printStackTrace();
			facesMessages.add("The PEM file you provided could not be read.");
			return "/wiki/edit.xhtml";
		}
	}

	public void listener(UploadEvent event) throws Exception{
        UploadItem item = event.getUploadItem();
        pemFile = item.getFile();
    }
	
	public void setRenderKeyUpload(boolean renderKeyUpload) {
		this.renderKeyUpload = renderKeyUpload;
	}



	public boolean isRenderKeyUpload() {
		if ((currentUser.getPrivateKey() == null) && 
    			(configurationService
    				.getUserConfiguration(currentUser,currentUser.getLogin()+"-key:" + currentContentItem.getResource())
    				.getEncryptedKey() != null)) 
    		renderKeyUpload = true;
    	else 
    		renderKeyUpload = false;
		return renderKeyUpload;
	}

	public static class DefaultPasswordFinder implements PasswordFinder {

        private final char [] password;

        private DefaultPasswordFinder(char [] password) {
            this.password = password;
        }

        @Override
        public char[] getPassword() {
            return Arrays.copyOf(password, password.length);
        }
    }
}
