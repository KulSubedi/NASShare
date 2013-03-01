/*
 * 
 */
package NAS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import thread.DetectorOvelapThread;
import thread.DetectorTestingThread;
import thread.SelfTestingThread;
import utils.ConfigUtils;
import cern.colt.bitvector.BitVector;

// TODO: Auto-generated Javadoc
/**
 * The Class Vdetector.
 */
public class Vdetector {

	// private static Logger logger =
	// Logger.getLogger(Vdetector.class.getCanonicalName());
	/** The selfs list. */
	private List<SelfPoint> selfsList = new ArrayList<SelfPoint>(); // store all
																	// self
																	// points
	// store all the detector points
	/** The detectors. */
	private List<Detector> detectors;

	/** The dimensions. */
	private int dimensions = 128; // default length

	// default radius for self point and it is
	// assumed that the radius is constant for all self points
	/** The self radius. */
	private static int selfRadius = 5;

	/** The self coverage. */
	private double selfCoverage;

	/** The detectors coverage. */
	private double detectorsCoverage;

	/** The minimum coverage. */
	private double minimumCoverage = 0.98;

	/** The time. */
	private int time;

	/** The minimum threshold. */
	private double minimumThreshold = 0.40;

	/** The failed attempts. */
	private int failedAttempts = 0;

	/** The max failed attempts. */
	private static int maxFailedAttempts = 50;

	/**
	 * Instantiates a new vdetector.
	 * 
	 * @param dimensions
	 *            the dimensions
	 * @param selfRadius
	 *            the self radius
	 * @param minimumCoverage
	 *            the minimum coverage
	 */
	public Vdetector(int dimensions, int selfRadius, double minimumCoverage) {
		super();
		this.dimensions = dimensions;
		this.selfRadius = selfRadius;
		this.minimumCoverage = minimumCoverage;

		Sphere.setDistribution(dimensions);
		SelfPoint.setRadius(selfRadius);
	}

	/**
	 * This method creates the detectors with variable length. In this
	 * implementation the overlap between two detectors are considered and the
	 * total coverage is estimated using cumulative density function.
	 * 
	 * @param selfs
	 *            the hashset of selfpoint
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createDetectors(HashSet<SelfPoint> selfs) {
		// for every random vector calculate distance between this and each of
		// self points
		// check exit conditions of parameter
		selfs.remove(null);
		if (selfs == null || selfs.size() == 0)
			return;

		detectorsCoverage = 0.0;
		selfsList.addAll(selfs);

		detectors = new ArrayList<Detector>();
		time = 0;
		boolean checkFail = false;
		maxFailedAttempts = Integer.parseInt(ConfigUtils
				.loadFileName("MaxFailedAttempts"));
		
		outer: while (true) {
			if (failedAttempts == maxFailedAttempts
					&& detectors.size() > selfsList.size())
				break;

			int candidateRadius = Integer.MAX_VALUE;
			int tempDistance = 0;
			time++;
			// logger.log(Level.INFO, "time: "+ time);

			BitVector candidateCenter = Sphere.getRandomPoint(dimensions);
			DetectorTestingThread dThread = new DetectorTestingThread(detectors, candidateCenter);
			checkFail = dThread.testDetector();
			
			//not a valid candidate detector 
			//so increase the failed attempt counter and start over
			if(checkFail) {
				failedAttempts++;						
				checkFail = true;
				continue outer;
			}
			
			SelfTestingThread sThread = new SelfTestingThread(selfsList, candidateCenter, selfRadius);
			candidateRadius = sThread.testSelf();
			
			if(candidateRadius <= 0) {
				failedAttempts++;						
				checkFail = true;
				continue outer;
			}
			

			// some heuristic to minimize overlap: here we consider at most half
			// overlap
			
			DetectorOvelapThread doThread = new DetectorOvelapThread(detectors, candidateCenter, candidateRadius);
			checkFail = doThread.testOverlap();
			if(checkFail) {
				failedAttempts++;
				checkFail = true;
				continue outer;
			}
				

			checkFail = false;
			failedAttempts = 0;

			// Add the newly created detector to the list
			Detector detector = new Detector(candidateCenter, candidateRadius);
			detectors.add(detector);
			detectorsCoverage += detector.getCoverage();

			if (detectorsCoverage >= minimumCoverage)
				break;
		}
	}

	/**
	 * Gets the detectors.
	 * 
	 * @return the list of detectors
	 */
	public List<Detector> getDetectors() {
		return detectors;
	}

	/**
	 * Gets the detectors coverage.
	 * 
	 * @return total detector coverage
	 */
	public double getDetectorsCoverage() {
		return detectorsCoverage;
	}

	/**
	 * Gets the time.
	 * 
	 * @return time taken to create the detectors
	 */
	public int getTime() {
		return time;
	}
}
