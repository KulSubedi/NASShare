/*
 * 
 */
package NAS;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

import cern.colt.bitvector.BitVector;

// TODO: Auto-generated Javadoc
/**
 * The Class Sphere.
 */
public abstract class Sphere {

	/** The center. */
	private BitVector center;

	/** static instance of normal distribution. */
	protected static NormalDistribution nDistribution;

	/**
	 * Sets the distribution.
	 * 
	 * @param dimensions
	 *            the new distribution
	 */
	public static void setDistribution(int dimensions) {
		double mean = dimensions / 2.0;
		double sd = Math.sqrt(dimensions) / 2.0;

		nDistribution = new NormalDistribution(mean, sd);
	}

	/**
	 * Instantiates a new sphere.
	 * 
	 * @param center
	 *            the center of sphere
	 */
	public Sphere(BitVector center) {
		this.center = center;
	}

	/**
	 * Gets the center.
	 * @return  the center
	 */
	public BitVector getCenter() {
		return center;
	}

	/**
	 * Gets the radius.
	 * 
	 * @return the radius
	 */
	public abstract int getRadius();

	/**
	 * Gets the center distance.
	 * 
	 * @param s
	 *            the instance of sphere class
	 * @return the center distance
	 */
	public int getCenterDistance(Sphere s) {
		BitVector v = center.copy();
		v.xor(s.getCenter());
		return v.cardinality(); // total number of 1's of the result BitVector
	}

	/**
	 * Gets the center distance.
	 * 
	 * @param vector
	 *            the BitVector
	 * @return Hamming distance
	 */
	public int getCenterDistance(BitVector vector) {
		BitVector v = center.copy();
		v.xor(vector);
		return v.cardinality(); // total number of 1's of the result BitVector
	}

	/**
	 * This function creates an arbitrary random vector for detector.
	 * 
	 * @param dimension
	 *            the dimension
	 * @return BitVector of the given dimension
	 */
	public static BitVector getRandomPoint(int dimension) {
		if (dimension < 1)
			// return new BitVector(0);
			return null;
		else {
			BitVector bv = new BitVector(dimension);
			Random generator = new Random();
			for (int i = 0; i < dimension; i++) {
				if (generator.nextBoolean())
					bv.set(i); // efficient way to create the random binary bits
			}
			return bv;
		}
	}
}
