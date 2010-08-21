package kiwi.service.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateless;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.security.CryptoServiceLocal;
import kiwi.api.security.CryptoServiceRemote;
import kiwi.api.user.KiWiProfileFacade;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.security.KeyStoreServlet.DefaultPasswordFinder;
import kiwi.util.KiWiStringUtils;

import org.bouncycastle.openssl.PEMReader;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Stateless
@Scope(ScopeType.STATELESS)
@Name("cryptoService")
@AutoCreate
public class CryptoServiceImpl implements CryptoServiceRemote, CryptoServiceLocal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4804016119240340343L;

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@Override
	public byte[] encryptSymmetric(byte[] input, byte[] keyBytes, String alg) {
		SecretKeySpec key = new SecretKeySpec(keyBytes, alg);
		
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
			
//			System.out.println("Input: " + new String(input));
			
			int outputBufferSize = (input.length-(input.length % 16)) + 16;
			
//			System.out.println("output buffer size: " + outputBufferSize);
			byte[] cipherText = new byte[outputBufferSize];
			
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			int ctlength = cipher.update(input, 0, input.length, cipherText, 0);
			ctlength += cipher.doFinal(cipherText, ctlength);
			
//			System.out.println("Encrypted: " + KiWiStringUtils.toHex(cipherText, ctlength));
			
			return cipherText;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (ShortBufferException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public byte[] decryptSymmetric(byte[] cipherBytes, byte[] keyBytes, String alg) {
		SecretKeySpec key = new SecretKeySpec(keyBytes, alg);
		
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
			
//			System.out.println("Cipher-Msg: " + new String(KiWiStringUtils.toHex(cipherBytes, cipherBytes.length)));
			byte[] plain = new byte[cipherBytes.length];
			
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			int ptLength = cipher.update(cipherBytes, 0, cipherBytes.length, plain, 0);
			ptLength += cipher.doFinal(plain, ptLength);
			
			if(ptLength != cipherBytes.length) {
				byte[] tmp = new byte[ptLength];
				for(int i=0; i < ptLength; i++) {
					tmp[i] = plain[i];
				}
				return tmp;
			}
			
//			System.out.println("decrypted plain text: " + new String(plain));
			
			return plain;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (ShortBufferException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public byte[] encryptAsymmetric(byte[] input, User user) {
		try {
			PublicKey pubkey = getUsersPublikKey(user);
			Cipher cipher = Cipher.getInstance("RSA", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, pubkey);
			byte[] ciph = cipher.doFinal(input);
			
			return ciph;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public byte[] decryptAsymmetric(byte[] input, PrivateKey privKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA", "BC");
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			byte[] plain = cipher.doFinal(input);
			
			return plain;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public byte[] signContent(byte[] content, PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance("SHA1withRSA", "BC");
			
			signature.initSign(privateKey);
			signature.update(content);
			
			return signature.sign();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public boolean verifySignature(byte[] content, byte[] signature, User user) {
		if(getUsersPublikKey(user) != null) {
			try {
				Signature sign = Signature.getInstance("SHA1withRSA", "BC");
				sign.initVerify(getUsersPublikKey(user));
				sign.update(content);
				
				return sign.verify(signature);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Override
	public byte[] generateHash(byte[] content) {
		try {
			MessageDigest hash = MessageDigest.getInstance("SHA-1", "BC");
			hash.update(content);
			return hash.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public PublicKey getUsersPublikKey(User user) {
		PublicKey pubkey = user.getPublicKey();
		try {
			if(pubkey == null) {
				KiWiProfileFacade profile = kiwiEntityManager.find(KiWiProfileFacade.class, 
					((KiWiUriResource) user.getResource()).getUri());
				if(profile.getPublic_exponent() == null || profile.getModulus() == null) {
					return null;
				}
				BigInteger exp = new BigInteger(profile.getPublic_exponent(),10);
				BigInteger mod = new BigInteger(profile.getModulus(),16);
				
				RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(mod, exp);
				KeyFactory keyfactory = KeyFactory.getInstance("RSA", "BC");
				pubkey = keyfactory.generatePublic(pubKeySpec);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return pubkey;
	}
	
	public static void main(String[] args) {
		
		PublicKey publicKey = null;
		PrivateKey privateKey = null;
		
		try {
			PEMReader pemreader = new PEMReader(
					new FileReader(new File("/home/sstroka/kiwi_key_certificates/sebastians_key.pem")), new DefaultPasswordFinder(
							new char[] {'b','d','S','u',',','9','A','?'}));
			KeyPair keyPair = (KeyPair) pemreader.readObject();

			publicKey = keyPair.getPublic();
			privateKey = keyPair.getPrivate();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		User user = new User();
//		user.setPublicKey(publicKey);
//		
//		byte[] inputBytes = new byte[] {
//				60, 112, 62, 98, 108, 97, 60, 47, 112, 62
//		};
		CryptoServiceImpl cryptoService = new CryptoServiceImpl();
//		PasswordGeneratorServiceImpl passwordGenerator = new PasswordGeneratorServiceImpl();
////		String password = passwordGenerator.generatePassword(16);
//		String password = "0123456789abcdef";
////		System.out.println(password.getBytes().length);
//		byte[] keyBytes = password.getBytes();
////		byte[] keyBytes = new byte[] {
////				0x00, 0x01, 0x02, 0x03, 
////				0x04, 0x05, 0x06, 0x07,
////				0x00, 0x01, 0x02, 0x03, 
////				0x04, 0x05, 0x06, 0x07
////		};
//		System.out.println("plain text: " + new String(inputBytes));
//		byte[] cipher = cryptoService.encryptSymmetric(inputBytes, keyBytes, "AES");
//		
//		// encrypt with currentuser's public key
//		byte[] encryptedKey = cryptoService.encryptAsymmetric(keyBytes, user);
//		// store key
//		String stringValueEncryptedKey = KiWiStringUtils.toHex(encryptedKey, encryptedKey.length);
//		
//		System.out.println("cipher bytes: " + new String(cipher));
//		String cipherBlockForm = KiWiStringUtils.toHexHtml(cipher, cipher.length);
//		System.out.println("html hex: " + cipherBlockForm);
//		
//		String cipherString = KiWiStringUtils.extractAESCipher(cipherBlockForm);
//		System.out.println("cipher string hex: " + cipherString);
//		byte[] cipher_new = KiWiStringUtils.fromHex(cipherString,cipherString.length());
//		System.out.println("cipher bytes: " + new String(cipher_new));
//		
//		byte[] fromHexEncryptedKey = KiWiStringUtils.fromHex(stringValueEncryptedKey, stringValueEncryptedKey.length());
//		byte[] decryptedKey = cryptoService.decryptAsymmetric(fromHexEncryptedKey, privateKey);
//		
//		byte[] plain = cryptoService.decryptSymmetric(cipher, decryptedKey, "AES");
//		System.out.println("plain text: " + new String(plain));
		byte[] query = new byte[] {77, 79, 68, 73, 70, 89, 32, 71, 82, 65, 80, 
				72, 32, 60, 104, 116, 116, 112, 58, 47, 47, 49, 57, 50, 46, 49, 
				54, 56, 46, 53, 54, 46, 49, 48, 49, 58, 56, 48, 56, 48, 47, 75, 
				105, 87, 105, 47, 117, 115, 101, 114, 47, 119, 97, 115, 116, 108, 
				62, 32, 32, 32, 32, 32, 32, 32, 68, 69, 76, 69, 84, 69, 32, 63, 
				112, 101, 114, 115, 32, 60, 104, 116, 116, 112, 58, 47, 47, 119, 
				119, 119, 46, 107, 105, 119, 105, 45, 112, 114, 111, 106, 101, 
				99, 116, 46, 101, 117, 47, 107, 105, 119, 105, 47, 99, 111, 114, 
				101, 47, 115, 107, 121, 112, 101, 65, 99, 99, 111, 117, 110, 116, 
				62, 32, 63, 97, 99, 99, 32, 32, 32, 32, 32, 32, 32, 73, 78, 83, 
				69, 82, 84, 32, 63, 112, 101, 114, 115, 32, 60, 104, 116, 116, 
				112, 58, 47, 47, 119, 119, 119, 46, 107, 105, 119, 105, 45, 112, 
				114, 111, 106, 101, 99, 116, 46, 101, 117, 47, 107, 105, 119, 105, 
				47, 99, 111, 114, 101, 47, 115, 107, 121, 112, 101, 65, 99, 99, 
				111, 117, 110, 116, 62, 32, 34, 119, 97, 115, 116, 108, 95, 115, 
				107, 121, 112, 101, 34};
		byte[] signature = cryptoService.signContent(query, privateKey);
		String htmlForm = KiWiStringUtils.appendHexSignature("", signature);
		System.out.println(KiWiStringUtils.extractRSASignature(htmlForm));
		
		User user = new User();
		user.setPublicKey(publicKey);
		if(cryptoService.verifySignature(query, signature, user))
			System.out.println("verified");
		else
			System.out.println("not verified");
//		
//		User user = new User();
//		user.setPublicKey(publicKey);
//		String text2 = KiWiStringUtils.extractWithoutRSASignature(htmlForm);
//		System.out.println(text2);
//		if(cryptoService.verifySignature(text2.getBytes(), signature, user))
//			System.out.println("signature verified");
//		else 
//			System.out.println("signature not verified");
	}
}
