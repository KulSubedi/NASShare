/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hash;

import cern.colt.bitvector.BitVector;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class HashGenerator.
 * 
 * @author Javier
 */
public class HashGenerator {

	/** The Constant passwordAlphabet. */
	public static final char[] passwordAlphabet = { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '!', ':', '+', '-', '*' };

	/** The digest. */
	private MessageDigest digest;

	/** The rnd. */
	private Random rnd = new Random();

	/**
	 * Gets the hash passwords.
	 * 
	 * @param password
	 *            the password
	 * @return the hash passwords
	 */
	public byte[] getHashPasswords(String password) {
		return digest.digest(password.getBytes());
	}

	/**
	 * Sets the digest.
	 * 
	 * @param algorithm
	 *            the new digest
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public final void setDigest(String algorithm)
			throws NoSuchAlgorithmException {
		digest = MessageDigest.getInstance(algorithm);
	}

	/**
	 * Instantiates a new hash generator.
	 * 
	 * @param algorithm
	 *            the name of encryption algorithm
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public HashGenerator(String algorithm) throws NoSuchAlgorithmException {
		setDigest(algorithm);
	}

	/**
	 * To bit vector.
	 * 
	 * @param s
	 *            the byte array
	 * @param size
	 *            the size of BitVector
	 * @return the BitVector
	 */
	public BitVector toBitVector(byte[] s, int size) {

		int l = 0;
		long[] longs = new long[s.length / 8];

		for (int i = 0; i < s.length; i += 8) {
			for (int j = 0; j < 8; j++) {
				longs[l] = longs[l] << 8;
				longs[l] = longs[l] + (((int) s[i + j]) & 0xFF);

			}
			l++;
		}
		return new BitVector(longs, size);
	}

	/**
	 * Gets the random hash passwords.
	 * 
	 * @param familyNames
	 *            the family names
	 * @param passList
	 *            the password list
	 * @param sampleSize
	 *            the sample size
	 * @param proportionOfRndPass
	 *            the proportion of random pass
	 * @param rndPassLength
	 *            the random pass length
	 * @return the random hash passwords
	 */
	public BitVector[] getRandomHashPasswords(String[] familyNames,
			String[] passList, int sampleSize, int proportionOfRndPass,
			int rndPassLength) {

		if (proportionOfRndPass < 0 || proportionOfRndPass > 100) {
			throw new IllegalArgumentException(
					"proportionOfRndPass must be between 0 and 100.");
		}
		if (sampleSize <= 0) {
			throw new IllegalArgumentException(
					"sampleSize must be greater than 0.");
		}
		if (familyNames == null || passList == null) {
			throw new IllegalArgumentException(
					"familyNames and passList cannot be null or empty.");
		}
		if (rndPassLength < 1) {
			throw new IllegalArgumentException(
					"length must be larger than 0. Current value: "
							+ rndPassLength);
		}
		BitVector[] output = new BitVector[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			String name = ((char) ('a' + rnd.nextInt(28)))
					+ familyNames[rnd.nextInt(familyNames.length)];
			String pass;
			if (rnd.nextInt(100) < proportionOfRndPass) {
				pass = getRndPassword(rndPassLength);
			} else {
				pass = passList[rnd.nextInt(passList.length)];
			}
			byte[] hash = getHashPasswords(name + pass);
			output[i] = toBitVector(hash, hash.length * 8);
		}

		return output;
	}

	/**
	 * Gets the random password.
	 * 
	 * @param length
	 *            the length
	 * @return the random password
	 */
	private String getRndPassword(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(passwordAlphabet[rnd.nextInt(passwordAlphabet.length)]);
		}
		return sb.toString();
	}
}
