/**
 * 
 */
package thread;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import NAS.Detector;
import cern.colt.bitvector.BitVector;

// TODO: Auto-generated Javadoc
/**
 * The Class DetectorOvelapThread.
 *
 * @author aknag
 */
public class DetectorOvelapThread implements Runnable {
	
	/** The detector. */
	private List<Detector> detector;
	
	/** The b v. */
	private BitVector bV;
	
	/** The detect. */
	private Detector detect;
	
	/** The check overlap. */
	private boolean checkOverlap = false;
	
	/** The index. */
	private int index = 0;
	
	/** The detector size. */
	private int detectorSize = 0;
	
	/** The candidate radius. */
	private int candidateRadius;

	/** The distance center. */
	private int distanceCenter = 0;
	
	/** The sumof radius. */
	private int sumofRadius = 0;
	
	/** The diff radius. */
	private int diffRadius = 0;

	/**
	 * Instantiates a new detector ovelap thread.
	 *
	 * @param detectorSet the detector set
	 * @param candidateCenter the candidate center
	 * @param radius the radius
	 */
	public DetectorOvelapThread(List<Detector> detectorSet,
			BitVector candidateCenter, int radius) {
		candidateRadius = radius;
		detector = detectorSet;
		bV = candidateCenter;
		detectorSize = detector.size();
	}

	/**
	 * Test overlap.
	 *
	 * @return true, if successful
	 */
	public boolean testOverlap() {
		int noProcessors = Runtime.getRuntime().availableProcessors();
		Thread t[] = new Thread[noProcessors];

		for (int i = 0; i < noProcessors; i++) {
			t[i] = new Thread(this);
			t[i].start();
		}

		try {
			for (int i = 0; i < noProcessors; i++)
				t[i].join();
		} catch (Exception ex) {
			Logger.getLogger(DetectorTestingThread.class.getCanonicalName())
					.log(Level.SEVERE,
							"Problem in joining for detector overlap test");
		}
		return checkOverlap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		while (!checkOverlap) {
			if (index >= detectorSize)
				break;
			detect = getDetector();
			distanceCenter = detect.getCenterDistance(bV);
			sumofRadius = detect.getRadius() + candidateRadius;
			diffRadius = Math.abs(detect.getRadius() - candidateRadius);
			if (distanceCenter <= diffRadius) {
				checkOverlap = true;
			} 
			else if ((distanceCenter > diffRadius) && (distanceCenter < sumofRadius / 2)) {
				checkOverlap = true;
			} 
			else
				checkOverlap = false;
		}
	}

	/**
	 * Gets the detector.
	 *
	 * @return the detector
	 */
	public synchronized Detector getDetector() {

		if (index >= detectorSize) {
			return detector.get(index - 1);
		}

		detect = detector.get(index);
		++index;
		return detect;
	}
}
