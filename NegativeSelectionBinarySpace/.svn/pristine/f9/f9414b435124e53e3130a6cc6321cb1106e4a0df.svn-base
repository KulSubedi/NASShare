/*
 * 
 */
package NAS;

import cern.colt.bitvector.BitVector;

// TODO: Auto-generated Javadoc
/**
 * The Class SelfPoint.
 */
public class SelfPoint extends Sphere {

	/** The radius. */
	private static int radius;

	/** The coverage. */
	private static double coverage;

	/** The center. */
	private BitVector center;

	/*
	 * (non-Javadoc)
	 * 
	 * @see NAS.Sphere#getCenter()
	 */
	/**
	 * Gets the center.
	 *
	 * @return center vector
	 */
	public BitVector getCenter() {
		return center;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see NAS.Sphere#getRadius()
	 */
	/**
	 * Gets the radius.
	 *
	 * @return radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Sets the radius.
	 * @param radius  the new radius
	 */
	public static void setRadius(int radius) {
		SelfPoint.radius = radius;
		coverage = nDistribution.cumulativeProbability(0, radius);
	}

	/**
	 * Gets the coverage.
	 * @return  the coverage of the self point
	 */
	public static double getCoverage() {
		return coverage;
	}

	/**
	 * Instantiates a new self point.
	 * 
	 * @param center
	 *            the center coordinate of the self point
	 */
	public SelfPoint(BitVector center) {
		super(center);
		this.center = center;
	}
}
