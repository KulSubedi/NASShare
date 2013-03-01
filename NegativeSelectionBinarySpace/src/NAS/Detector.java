/*
 * 
 */
package NAS;

import cern.colt.bitvector.BitVector;

// TODO: Auto-generated Javadoc
/**
 * The Class Detector.
 */

/**
 * The Class Detector.
 */

public class Detector extends Sphere {

	/** The radius. */
	private int radius = 0;

	/** The coverage. */
	private double coverage = 0.0;

	/**
	 * Instantiates a new detector.
	 * 
	 * @param center
	 *            the center point of the detector
	 * @param radius
	 *            the detector radius
	 */
	public Detector(BitVector center, int radius) {
		super(center);
		this.radius = radius;
		this.coverage = nDistribution.cumulativeProbability(0, radius);
	}

	/**
	 * Gets the coverage.
	 * @return  the coverage of the detector
	 */
	public double getCoverage() {
		return coverage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see NAS.Sphere#getRadius()
	 */
	/**
	 * Gets the radius.
	 *
	 * @return the radius of the detector
	 */
	public int getRadius() {
		return radius;
	}
}