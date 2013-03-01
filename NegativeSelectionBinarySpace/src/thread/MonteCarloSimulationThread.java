/**
 * 
 */
package thread;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import NAS.Detector;
import NAS.SelfPoint;
import NAS.Sphere;

import cern.colt.bitvector.BitVector;


// TODO: Auto-generated Javadoc
/**
 * The Class MonteCarloSimulationThread.
 *
 * @author aknag
 */
@SuppressWarnings("rawtypes")
public class MonteCarloSimulationThread implements Callable {

	/** The size. */
	private int size;
	
	/** The selfpoints. */
	private HashSet<SelfPoint> selfpoints;
	
	/** The self radius. */
	private int selfRadius;
	
	/** The detectors. */
	private List<Detector> detectors;
	
	/** The bv. */
	private BitVector bv;
	
	/** The detection rate. */
	private int detectionRate = 0;

	/**
	 * Instantiates a new monte carlo simulation thread.
	 *
	 * @param dimension the dimension
	 * @param selfs the selfs
	 * @param detector the detector
	 * @param radius the radius
	 */
	public MonteCarloSimulationThread(int dimension, HashSet<SelfPoint> selfs,
			List<Detector> detector, int radius) {
		size = dimension;
		selfRadius = radius;
		selfpoints = selfs;
		detectors = detector;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Object call() throws Exception {
		Thread.sleep(0, 100);
		for (int i = 1; i < 2; i++) {
			bv = Sphere.getRandomPoint(size);
			// to exclude the self points in consideration
			for (SelfPoint sp : selfpoints) {
				if (sp.getCenterDistance(bv) <= selfRadius) {
					i = i - 1;
					break;
				}
			}
			// check any of the detectors
			for (Detector dt : detectors) {
				if (dt.getCenterDistance(bv) <= dt.getRadius()) {
					detectionRate++;
					break;
				}
			}
		}
		return detectionRate;
	}

}
