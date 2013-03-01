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
 * The Class DetectorTestingThread.
 *
 * @author aknag
 */
public class DetectorTestingThread implements Runnable {

	/** The detector. */
	private List<Detector> detector;
	
	/** The bitvector */
	private BitVector bV;
	
	/** The temp distance. */
	private int tempDistance = 0;
	
	/** The check detector. */
	private boolean checkDetector = false;
	
	/** The index. */
	private int index = 0;
	
	/** The detector size. */
	private int detectorSize = 0;

	/**
	 * Instantiates a new detector testing thread.
	 *
	 * @param detectorSet the detector set
	 * @param candidateCenter the candidate center
	 */
	public DetectorTestingThread(List<Detector> detectorSet,
			BitVector candidateCenter) {
		detector = detectorSet;
		bV = candidateCenter;
		detectorSize = detector.size();
	}

	/**
	 * Test detector.
	 *
	 * @return whether this is a valid candidate point or not
	 */
	public boolean testDetector() {
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
					.log(Level.SEVERE, "Problem in joining for detectors");
		}

		return checkDetector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		while (!checkDetector) {
			if (index >= detectorSize)
				break;
			Detector detect = getDetector();
			tempDistance = detect.getCenterDistance(bV);
			checkDetector = (tempDistance <= detect.getRadius()) ? true : false;
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

		Detector detect = detector.get(index);
		++index;
		return detect;
	}
}
