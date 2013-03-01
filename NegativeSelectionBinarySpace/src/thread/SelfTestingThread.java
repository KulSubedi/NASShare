/**
 * 
 */
package thread;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import NAS.SelfPoint;

import cern.colt.bitvector.BitVector;

// TODO: Auto-generated Javadoc
/**
 * The Class SelfTestingThread.
 *
 * @author aknag
 */
public class SelfTestingThread implements Runnable {
	
	/** The self points. */
	private List<SelfPoint> selfPoints;
	
	/** The bitvector. */
	private BitVector bV;
	
	/** The self radius. */
	private static int selfRadius;
	
	/** The temp distance. */
	private int tempDistance;
	
	/** The self size. */
	private int selfSize;
	
	/** The candidate radius. */
	private int candidateRadius = Integer.MAX_VALUE;
	
	/** The check self. */
	private boolean checkSelf = false;
	
	/** The index. */
	private int index = 0;
	

	/**
	 * Instantiates a new self testing thread.
	 *
	 * @param self the self
	 * @param candidateCenter the candidate center
	 * @param selfRadius the self radius
	 */
	public SelfTestingThread(List<SelfPoint> self, BitVector candidateCenter,
			int selfRadius) {
		SelfTestingThread.selfRadius = selfRadius;
		selfPoints = self;
		bV = candidateCenter;
		selfSize = selfPoints.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!checkSelf) {
			if (index >= selfSize)
				break;
			SelfPoint sp = getSelf();
			tempDistance = sp.getCenterDistance(bV);
			updateCandidateRadius(tempDistance);
		}
	}

	/**
	 * Update candidate radius.
	 *
	 * @param tempDist the temp dist
	 */
	private synchronized void updateCandidateRadius(int tempDist) {
		if ((tempDistance - selfRadius - 1) < candidateRadius)
			candidateRadius = tempDistance - selfRadius - 1;
		 
	}
	
	/**
	 * Gets the self points
	 *
	 * @return non- assigned selfPoint
	 */
	private synchronized SelfPoint getSelf() {
		//
		if (index >= selfSize) {
			return selfPoints.get(index - 1);
		}

		SelfPoint sp = selfPoints.get(index);
		++index;
		return sp;
	}

	/**
	 * Test self points
	 *
	 * @return whether candidate center falls in the self region
	 */
	public int testSelf() {
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
					.log(Level.SEVERE, "Problem in joining for Self List");
		}

		return candidateRadius;
	}

}
