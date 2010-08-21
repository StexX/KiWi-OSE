/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s):
 * 
 * 
 */
package kiwi.service.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kiwi.service.user.PasswordGeneratorServiceImpl;
import kiwi.util.KiWiStringUtils;

import net.java.dev.sommer.foafssl.principals.FoafSslPrincipal;
import net.java.dev.sommer.foafssl.verifier.DereferencingFoafSslVerifier;
import net.java.dev.sommer.foafssl.verifier.FoafSslVerifier;

import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.util.encoders.Base64;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.web.AbstractResource;

/**
 * This service provides the keystore for  * persisting PKCS12 
 * certificates for foaf+ssl. The KeyStoreService will be automatically 
 * created at the application startup and loads/stores certificates of
 * foaf+ssl users.
 * @author Stephanie Stroka (stephanie.stroka@salzburgresearch.at)
 *
 */
@Scope(ScopeType.APPLICATION)
@Name("keyStoreServlet")
@BypassInterceptors
@Startup
public class KeyStoreServlet extends AbstractResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3940320970812261614L;

	/**
	 * keystore to be used to store certificates
	 */
	private KeyStore keyStore;
	
	/**
	 * private RSA key 
	 */
	private PrivateKey privateKey = null;
	
	/**
	 * public RSA key
	 */
    private PublicKey publicKey = null;
    
    /**
     * RSA certificate
     */
    private Certificate certificate = null;
    
    private static FoafSslVerifier FOAF_SSL_VERIFIER = new DereferencingFoafSslVerifier();
    public static final String SIGNATURE_PARAMNAME = "sig";
    public static final String SIGALG_PARAMNAME = "sigalg";
    public static final String TIMESTAMP_PARAMNAME = "ts";
    public static final String WEBID_PARAMNAME = "webid";
    public static final String ERROR_PARAMNAME = "error";
    public static final String AUTHREQISSUER_PARAMNAME = "authreqissuer";
    
	/**
	 * the logger
	 */
	private static Log log;

	/**
	 * Initializes the keystore.
	 * This includes the creation of a KeyStore object, 
	 * which loads the settings of the server keystore, 
	 * which has been created in the jboss/server/default/conf directory.
	 * The private key, the public key and the certificate of the
	 * server keystore are stored in this class.
	 */
	public void initKeystore() {
		log = Logging.getLog(this.getClass());
		
		/*the following variables should be retrieved via 
		 * getInitParam(), so that they can be configured in the web.xml conf file */
		String keystorePath = getServletContext().getRealPath("/")+"../../../conf/server.keystore";
		String keystoreType = null;
        String keystorePassword = "changeit";
        String keyPassword = null;
        InputStream ksInputStream = null;
        if (keyPassword == null)
            keyPassword = keystorePassword;
        String alias = null;
		try {
			try {
                if (keystorePath != null) {
                    ksInputStream = new FileInputStream(keystorePath);
                }
                /* create object and load keystore data */
                keyStore = KeyStore.getInstance((keystoreType != null) ? keystoreType
                        : KeyStore.getDefaultType());
                keyStore.load(ksInputStream, keystorePassword != null ? keystorePassword
                        .toCharArray() : null);
            } finally {
                if (ksInputStream != null) {
                    ksInputStream.close();
                }
            }
		} catch (KeyStoreException e) {
			log.error("The keystore PKCS12 does not exist");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException");
			e.printStackTrace();
		} catch (CertificateException e) {
			log.error("CertificateException");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException");
			e.printStackTrace();
		}
		
		try {
            if (alias == null) {
                Enumeration<String> aliases = keyStore.aliases();
                while (aliases.hasMoreElements()) {
                    String tempAlias = aliases.nextElement();
                    if (keyStore.isKeyEntry(tempAlias)) {
                        alias = tempAlias;
                        break;
                    }
                }
            }
            if (alias == null) {
                log.error("Error configuring servlet, invalid keystore configuration: " +
                		"alias unspecified or couldn't find key at alias: #0 ", alias);
                // TODO throw exception
            }
            /* store private and public key and certificate of the server */
            privateKey = (PrivateKey) keyStore.getKey(alias, keyPassword != null ? keyPassword
                    .toCharArray() : null);
            certificate = keyStore.getCertificate(alias);
            publicKey = certificate.getPublicKey();
        } catch (UnrecoverableKeyException e) {
            log.error("Error configuring servlet (could not load keystore): #0 ", e);
            // TODO throw exception
        } catch (KeyStoreException e) {
        	log.error("Error configuring servlet (could not load keystore): #0 ", e);
        	// TODO throw exception
        } catch (NoSuchAlgorithmException e) {
        	log.error("Error configuring servlet (could not load keystore): #0 ", e);
        	// TODO throw exception
        }
	}
	
	/**
	 * Extracts the web id out of the client certificate and sends it within a 
	 * signed response to the given URL (authreqissuer).
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String replyTo = request.getParameter(AUTHREQISSUER_PARAMNAME);
		Collection<? extends FoafSslPrincipal> verifiedWebIDs = null;
		/*
         * Verifies the certificate passed in the request.
         */
        X509Certificate[] certificates = (X509Certificate[]) request
                .getAttribute("javax.servlet.request.X509Certificate");

        if (certificates == null) {
           if ( brokenBrowser(request)) {
               certificates = (X509Certificate[]) request.getAttribute("javax.servlet.request.ForceX509Certificate");
           }
        }

        if (certificates != null) {
            X509Certificate foafSslCertificate = certificates[0];
            try {
                verifiedWebIDs = FOAF_SSL_VERIFIER.verifyFoafSslCertificate(foafSslCertificate);
            } catch (Exception e) {
                redirect(response,replyTo+"?"+ERROR_PARAMNAME+"="+URLEncoder.encode(e.getMessage()));
                return;
            }
        }

        if ((verifiedWebIDs != null) && (verifiedWebIDs.size() > 0)) {
            try {
                 String authnResp = createSignedResponse(verifiedWebIDs, replyTo);
                 redirect(response, authnResp);
            } catch (InvalidKeyException e) {
            	log.error("Error when signing the response. #0 ", e);
                redirect(response, replyTo+"?"+ERROR_PARAMNAME+"=IdPError");
            } catch (NoSuchAlgorithmException e) {
            	log.error("Error when signing the response. #0 ", e);
                redirect(response, replyTo+"?"+ERROR_PARAMNAME+"=IdPError");
            } catch (SignatureException e) {
            	log.error("Error when signing the response. #0 ", e);
                redirect(response, replyTo+"?"+ERROR_PARAMNAME+"=IdPError");
            } catch (UnsupportedEncodingException e) {
            	log.error("Error when signing the response. #0 ", e);
                redirect(response, replyTo+"?"+ERROR_PARAMNAME+"=IdPError");
			}
        } else {
                redirect(response,noCertError(replyTo));
       }
	}
	
	/**
     * 
     * 
     * @param verifiedWebIDs
     *            a list of webIds identifying the user (only the fist will be
     *            used)
     * @param simpleRequestParam
     *            the service that the response is sent to
     * @param privKey
     *            the private key used by this service
     * @return the URL of the response with the webid, timestamp appended and
     *         signed
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    private String createSignedResponse(Collection<? extends FoafSslPrincipal> verifiedWebIDs,
            String simpleRequestParam) throws NoSuchAlgorithmException,
            UnsupportedEncodingException, InvalidKeyException, SignatureException {
        /*
         * Reads the FoafSsl simple auth request.
         */
        String authnResp = simpleRequestParam;

        String sigAlg = null;
        if ("RSA".equals(privateKey.getAlgorithm())) {
            sigAlg = "SHA1withRSA";
            // sigAlgUri = "rsa-sha1";
        } else if ("DSA".equals(privateKey.getAlgorithm())) {
            sigAlg = "SHA1withDSA";
            // sigAlgUri = "dsa-sha1";
        } else {
            throw new NoSuchAlgorithmException("Unsupported key algorithm type.");
        }

        URI webId = verifiedWebIDs.iterator().next().getUri();
        authnResp += "?" + WEBID_PARAMNAME + "="
                + URLEncoder.encode(webId.toASCIIString(), "UTF-8");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        authnResp += "&" + TIMESTAMP_PARAMNAME + "="
                + URLEncoder.encode(dateFormat.format(Calendar.getInstance().getTime()), "UTF-8");
        // authnResp += "&" + SIGALG_PARAMNAME + "=" +
        // URLEncoder.encode(sigAlgUri, "UTF-8");

        String signedMessage = authnResp;
        Signature signature = Signature.getInstance(sigAlg);
        signature.initSign(privateKey);
        signature.update(signedMessage.getBytes("UTF-8"));
        byte[] signatureBytes = signature.sign();
        authnResp += "&" + SIGNATURE_PARAMNAME + "="
                + URLEncoder.encode(new String(Base64.encode(signatureBytes)), "UTF-8");
        return authnResp;
    }
	
	/**     
    * Redirect request to the given url
    * @param the response
    * @param respUrl the response Url to redirect to
    */
   private void redirect(HttpServletResponse response, String respUrl) {
      response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
      response.setHeader("Location", respUrl);
   }
   
   /**
    * create a URL to which the service will be redirected
    * @param replyTo
    * @return a url to redirect to
    */
   private String noCertError(String replyTo) {
      return replyTo+"?"+ERROR_PARAMNAME+"=nocert";
   }
	
	/**
     * the iPhone browser requires one to force the connection
     * and so do various versions of Safari... (not tested yet)
     */
   private boolean brokenBrowser(HttpServletRequest request) {
      String ua =request.getHeader("User-Agent");
      if (ua == null) return false;
      return (ua.contains("iPhone") && ua.contains("os 2_2"));
   }

	/**
	 * Overwritten method from AbstractResource, which starts a 
	 * new ContextualHttpServletRequest and calls the doGet() method
	 */
	@Override
	public void getResource(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {
		new ContextualHttpServletRequest(request) {
			@Override
            public void process() throws IOException {
				if(keyStore == null) {
					initKeystore();
				}
                doGet(request, response);
            }

		}.run();
		
	}

	/**
	 * Overwritten method that returns the 
	 * resource path, which is the url-ending 
	 * that redirects to the getResource() 
	 * method when it is called
	 */
	@Override
	public String getResourcePath() {
		return "/idp";
	}
	
	/** 
	 * initialize component 
	 */
	@Create  
	public void init(){
		org.jboss.seam.core.Init.instance().addResourceProvider("KeyStoreServlet");  
	}

	/**
	 * @return the publicKey
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	public static void main(String[] args) {
//		BigInteger modulus = new BigInteger(
//				"B89DADF1D46B685A4F9E0C8FB2DE61B9237D95283AB12F" +
//				"9DF57F79464526BAF0636D714B6E29A7F3B95C9FADD039" +
//				"72057BA85A62004DE2092B574E33D85F09812563E4BBEE" +
//				"1B70EE8C0D420A880DD811C0E4B2EC62FB8183AF64F609" +
//				"6551961D769240A867663875D72A50BB6CA56389925224" +
//				"866A756157AA56A58865299971F7F365765863F200A4E9" +
//				"A4908E9EE1B16D3BB00CECAFFBD849BD585743F8611782" +
//				"65BD4DF03B39C666A18385CE433E34462987B2F4B4005C" +
//				"5971A095A185358957667609A4E98F2C75C0929D5171B1" +
//				"AB8ACECD2B8F10917386F29377297E190AA84354C9A518" +
//				"5899A18116353746D581AB6B35B24005E7602A2C1BD5F9" +
//				"EDC211", 16);
//		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(
//				modulus, new BigInteger("65537",10));
//        try {
//			KeyFactory keyfactory = KeyFactory.getInstance("RSA", "BC");
//			PublicKey pubkey = keyfactory.generatePublic(pubKeySpec);
//			
//			System.out.println("enc format: " + pubkey.getFormat());
//			System.out.println("encoding: " + KiWiStringUtils.toHex(pubkey.getEncoded(),pubkey.getEncoded().length));
//		} catch (NoSuchProviderException e) {
//			e.printStackTrace();
//		} catch (InvalidKeySpecException e) {
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
		
		
		try {
			String plainText = new String("This is a plaintext");
			PEMReader pemreader = new PEMReader(
					new FileReader(new File("/home/sstroka/.gnupg/kiwi_sTx_key.pem")), new DefaultPasswordFinder(
							new char[] {'b','d','S','u',',','9','A','?'}));
			KeyPair keyPair = (KeyPair) pemreader.readObject();
			System.out.println("priv key alg: " + keyPair.getPrivate().getAlgorithm());
			System.out.println("pub key alg: " + keyPair.getPublic().getAlgorithm());
			byte[] privEnc = keyPair.getPrivate().getEncoded();
			byte[] pubEnc = keyPair.getPublic().getEncoded();
			
			Cipher cipher = Cipher.getInstance("RSA", "BC");
			PasswordGeneratorServiceImpl passServiceImpl = new PasswordGeneratorServiceImpl();
			System.out.println("PlainText: " + plainText);
			cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
			byte[] ciph = cipher.doFinal(plainText.getBytes());
			System.out.println("Ciphertext: " + KiWiStringUtils.toHex(ciph, ciph.length));
			
			cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
			byte[] plain = cipher.doFinal(ciph);
			System.out.println("PlainText: " + new String(plain));
			
			cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
			cipher.doFinal(plainText.getBytes());
			
			System.out.println(KiWiStringUtils.toHex(privEnc, privEnc.length));
			System.out.println(KiWiStringUtils.toHex(pubEnc, pubEnc.length));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
	}
	
	public static class DefaultPasswordFinder implements PasswordFinder {

        private final char [] password;

        public DefaultPasswordFinder(char [] password) {
            this.password = password;
        }

        @Override
        public char[] getPassword() {
            return Arrays.copyOf(password, password.length);
        }
    }
}
