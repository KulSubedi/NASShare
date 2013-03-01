/**
 * 
 */
package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import thread.MonteCarloSimulationThread;
import utils.ConfigUtils;
import NAS.Detector;
import NAS.SelfPoint;


// TODO: Auto-generated Javadoc
/**
 * The Class MonteCarloCalculation.
 * 
 * @author aknag
 */
public class MonteCarloCalculation {

	/** The n dimension. */
	private static int nDimension;

	/** The n trials. */
	private static long nTrials = 100000; // good value for 128 or 256 dimension
											// binary space

	/** The detectors. */
	private List<Detector> detectors;

	/** The selfpoints. */
	private HashSet<SelfPoint> selfpoints;

	/** The self point radius. */
	private int selfPointRadius;

	/** The detection rate. */
	private double detectionRate = 0.0;

	/** The false rate. */
	private double falseRate = 0.0;

	/**
	 * Instantiates a new monte carlo calculation.
	 * 
	 * @param dimension
	 *            the dimension of the binary space
	 * @param detectors
	 *            the set of generated detectors
	 * @param selfPoints
	 *            the list of self points
	 * @param selfRadius
	 *            the self points' radius
	 */
	public MonteCarloCalculation(int dimension, List<Detector> detectors,
			HashSet<SelfPoint> selfPoints, int selfRadius) {
		nDimension = dimension;
		this.detectors = detectors;
		selfPoints.remove(null);
		this.selfpoints = selfPoints;
		this.selfPointRadius = selfRadius;
	}

	/**
	 * Calculate detector coverage.
	 */
	@SuppressWarnings("unchecked")
	public void calculateDetectorCoverage() {
		nTrials = Long.parseLong(ConfigUtils.loadFileName("SimulationTrials"));

		int nbProcessors = Runtime.getRuntime().availableProcessors();
		ExecutorService eservice = Executors.newFixedThreadPool(nbProcessors*50);
		List<Future> resultsList = new ArrayList();
		
		for (long i = 0; i < nTrials; i++) {
				resultsList.add(eservice.submit(new MonteCarloSimulationThread(
					nDimension, selfpoints, detectors, selfPointRadius)));
		     }	
		
			int resultTask;
			for (Future future : resultsList) {

				try {
					resultTask = (Integer)future.get();
					detectionRate += resultTask;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		
			resultsList.clear();
			eservice.shutdown();
			while(!eservice.isTerminated()) ;
		falseRate = nTrials - detectionRate;
	}

	/**
	 * Gets the detection rate.
	 * 
	 * @return the detection rate
	 */
	public double getDetectionRate() {
		return detectionRate / nTrials;
	}

	/**
	 * Gets the false rate.
	 * 
	 * @return the false rate
	 */
	public double getFalseRate() {
		return falseRate / nTrials;
	}
}
