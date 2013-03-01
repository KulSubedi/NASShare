/**
 * 
 */
package testCases;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import utils.ConfigUtils;

import NAS.Detector;
import NAS.SelfPoint;
import NAS.Sphere;
import NAS.Vdetector;
import cern.colt.bitvector.BitVector;

// TODO: Auto-generated Javadoc
/**
 * The Class VdetectorTest.
 *
 * @author aknag
 */
public class VdetectorTest {

	/**
	 * Test generation of a random point.
	 */
	@Test
	public void testGetRandomPoint() {

		BitVector bVector = new BitVector(128);
		BitVector bVector1 = new BitVector(128);
		// set bVector1 to all 1's
		for (int i = 0; i < 128; i++)
			bVector1.set(i);

		assertTrue(Sphere.getRandomPoint(-2) == null);
		assertFalse(Sphere.getRandomPoint(128) == bVector);
		assertFalse(Sphere.getRandomPoint(128) == bVector1);
		assertTrue(Sphere.getRandomPoint(128).size() == 128);
		assertTrue(Sphere.getRandomPoint(128) != null);
	}

	/**
	 * Test of calculating center distance.
	 */
	@Test
	public void testGetCenterDistance1() {
		BitVector bv = new BitVector(128);
		for (int i = 0; i < 128; i++) {
			if ((i % 2) == 1)
				bv.set(i);
		}
		BitVector bv1 = new BitVector(128);
		for (int i = 0; i < 128; i++) {
			if ((i % 2) == 0)
				bv1.set(i);
		}
		SelfPoint sp = new SelfPoint(bv);
		assertEquals(sp.getCenterDistance(bv1), 128);
	}

	/**
	 * Test of calculating center distance.
	 */
	@Test
	public void testGetCenterDistance2() {
		BitVector bv = new BitVector(128);
		for (int i = 0; i < 128; i++) {
			if ((i % 3) == 1)
				bv.set(i);
		}
		BitVector bv1 = new BitVector(128);
		for (int i = 0; i < 128; i++) {
			if ((i % 2) == 1)
				bv1.set(i);
		}
		SelfPoint sp = new SelfPoint(bv);
		assertEquals(sp.getCenterDistance(bv1), 63);
	}

	/**
	 * Test of calculating center distance.
	 */
	@Test
	public void testGetCenterDistance3() {
		BitVector bv = new BitVector(128);
		for (int i = 0; i < 128; i++) {
			if ((i % 2) == 0)
				bv.set(i);
		}
		BitVector bv1 = new BitVector(128);
		for (int i = 0; i < 128; i++) {
			if ((i % 2) == 0)
				bv1.set(i);
		}
		SelfPoint sp = new SelfPoint(bv);
		assertEquals(sp.getCenterDistance(bv1), 0);
	}

	
	/**
	 * Test detectors' maximum coverage.
	 */
	@Test(timeout = 1000)
	public void testGetDetectorCoverage1() {
		// load the property file configurations before testing
		ConfigUtils.loadProperty();
		ConfigUtils
				.configLoggers(ConfigUtils.loadFileName("LoggingProperties"));
		HashSet<SelfPoint> selfPoint = new HashSet<SelfPoint>();
		for (int i = 0; i < 4; i++)
			selfPoint.add(new SelfPoint(Sphere.getRandomPoint(128)));

		int size = 128;
		int selfRadius = 2;
		double coverage = 0.60;

		Vdetector vDetector = new Vdetector(size, selfRadius, coverage);
		vDetector.createDetectors(selfPoint);
		assertTrue(vDetector.getDetectorsCoverage() >= 0.60);
	}

	/**
	 * Test whether detectors overlap with any of self points.
	 */
	@Test(timeout = 1000)
	public void testGetDetectorCoverage2() {
		ConfigUtils.loadProperty();
		ConfigUtils
				.configLoggers(ConfigUtils.loadFileName("LoggingProperties"));
		HashSet<SelfPoint> selfPoint = new HashSet<SelfPoint>();
		for (int i = 0; i < 10; i++)
			selfPoint.add(new SelfPoint(Sphere.getRandomPoint(128)));
		int size = 128;
		int selfRadius = 6;
		double coverage = .95;

		Vdetector vDetector = new Vdetector(size, selfRadius, coverage);
		vDetector.createDetectors(selfPoint);

		List<Detector> detectorPoint = new ArrayList<Detector>();
		for (int i = 0; i < vDetector.getDetectors().size(); i++)
			detectorPoint.add(new Detector(vDetector.getDetectors().get(i)
					.getCenter(), vDetector.getDetectors().get(i).getRadius()));

		for (Detector dt : detectorPoint) {
			for (SelfPoint sp : selfPoint) {
				assertTrue(dt.getCenterDistance(sp.getCenter()) > (dt
						.getRadius() + selfRadius));
			}
		}
	}

	/**
	 * Test if self points with all same entity.
	 */
	@Test(timeout = 1000)
	public void testGetDetectorCoverage3() {
		ConfigUtils.loadProperty();
		ConfigUtils
				.configLoggers(ConfigUtils.loadFileName("LoggingProperties"));
		HashSet<SelfPoint> selfPoint = new HashSet<SelfPoint>();
		BitVector bv = new BitVector(128);
		for (int i = 0; i < 10; i++)
			selfPoint.add(new SelfPoint(bv));

		int size = 128;
		int selfRadius = 6;
		double coverage = .95;

		Vdetector vDetector = new Vdetector(size, selfRadius, coverage);
		vDetector.createDetectors(selfPoint);

		assertTrue(vDetector.getDetectorsCoverage() >= 0.95);
	}

	/**
	 * Test if self points are null.
	 */
	@Test(timeout = 1000)
	public void testGetDetectorCoverage4() {
		ConfigUtils.loadProperty();
		ConfigUtils
				.configLoggers(ConfigUtils.loadFileName("LoggingProperties"));
		HashSet<SelfPoint> selfPoint = new HashSet<SelfPoint>();
		for (int i = 0; i < 10; i++)
			selfPoint.add(null);

		int size = 128;
		int selfRadius = 6;
		double coverage = .95;

		Vdetector vDetector = new Vdetector(size, selfRadius, coverage);
		vDetector.createDetectors(selfPoint);
		// the following test will always true
		assertFalse(vDetector.getDetectorsCoverage() >= 0.95);
	}
}
