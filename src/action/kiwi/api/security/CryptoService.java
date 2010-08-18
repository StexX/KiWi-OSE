package kiwi.api.security;

import java.security.PrivateKey;
import java.security.PublicKey;

import kiwi.model.user.User;

public interface CryptoService {

	/**
	 * Used to encrypt input bytes and to return the cipher text. Currently, only AES 
	 * works/has been tested as a cipher algorithm.
	 * 
	 * The size of the key byte array must be a multiple of 8.
	 * 
	 * @param input
	 * @param keyBytes
	 * @param alg
	 * @return
	 */
	public byte[] encryptSymmetric(byte[] input, byte[] keyBytes, String alg);

	/**
	 * Used to decrypt a cipher text and to return the plain text. Currently, only AES 
	 * works/has been tested as a cipher algorithm.
	 * 
	 * The size of the key byte array must be a multiple of 8. If another key has been used
	 * to encrypt the plain text, it will, of course, not return any meaningful text.
	 * 
	 * @param input
	 * @param keyBytes
	 * @param alg
	 * @return
	 */
	public byte[] decryptSymmetric(byte[] cipherBytes, byte[] keyBytes, String alg);

	public byte[] encryptAsymmetric(byte[] input, User user);

	public byte[] decryptAsymmetric(byte[] input, PrivateKey privKey);

	public byte[] signContent(byte[] content, PrivateKey privateKey);

	public byte[] generateHash(byte[] content);

	public boolean verifySignature(byte[] content, byte[] signature, User user);

	public PublicKey getUsersPublikKey(User user);
	
}
