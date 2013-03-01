/*
 * 
 */
package test;

import hash.HashGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import utils.ConfigUtils;
import NAS.Detector;
import NAS.SelfPoint;
import NAS.Vdetector;
import cern.colt.bitvector.BitVector;

// TODO: Auto-generated Javadoc
/**
 * The Class RunSimulation.
 */
public class RunSimulation {

	/** The logger instance. */
	private static Logger logger;

	/** The family names. */
	private static String[] familyNames = ConfigUtils.loadStrings(ConfigUtils
			.loadFileName("FamilyNames"));

	/** The pass. */
	private static String[] pass = ConfigUtils.loadStrings(ConfigUtils
			.loadFileName("Passwords"));

	/** The total testpoints. */
	private int size;

	/** The total testpoints. */
	private int totalNoSelfPoints;

	/** The total testpoints. */
	private int proportionRandomness;

	/** The total testpoints. */
	private int randomPassLen;

	/** The total testpoints. */
	private int selfRadius;

	/** The total testpoints. */
	private int totalTestpoints;

	/** The reuse data. */
	private boolean reuseData = false;

	/** The coverage. */
	private double coverage;

	/** The self points. */
	private HashSet<SelfPoint> selfPoints;
	
	/** Monte Carlo program instance. */
	private MonteCarloCalculation mcc;

	/** The Output Log file handler. */
	private String handlerString = ConfigUtils.loadFileName("OutputLogFile");

	/** The save self points. */
	private boolean saveSelfPoints = ConfigUtils.loadFileName("SaveSelfPoints")
			.equalsIgnoreCase("true");

	/** The save detector points. */
	private boolean saveDetectorPoints = ConfigUtils.loadFileName(
			"SaveDetectorPoints").equalsIgnoreCase("true");

	/** The save self location. */
	private String saveSelfLocation = ConfigUtils
			.loadFileName("SaveSelfLocation");

	/** The save detector location. */
	private String saveDetectorLocation = ConfigUtils
			.loadFileName("SaveDetectorLocation");

	/** The time stamp value. */
	private String timeStamp = "";

	/** The fh. */
	private FileHandler fh;

	/** The end time. */
	private Date startTime,endTime; 
	
	/** The diff time. */
	private long diffTime;
	/**
	 * Simulation epoch.
	 * 
	 * @param oneLineInput
	 *            the one line input
	 */
	public void simulationEpoch(String oneLineInput) {

		if (oneLineInput == null || oneLineInput.length() == 0) {
			logger.log(Level.SEVERE, "The line is null or blank");
		}
		String[] parameters = oneLineInput.split("\\s+");

		try {
			if (Integer.parseInt(parameters[8]) == 1) {
				reuseData = true;
			} else
				reuseData = false;

		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse Reuse Data parameter");
		}

		try {
			size = Integer.parseInt(parameters[0]);
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse dimension");
		}
		try {
			totalNoSelfPoints = Integer.parseInt(parameters[2]);
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE,
					"Can not parse total number of self points");
		}
		try {
			proportionRandomness = Integer.parseInt(parameters[3]);
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE,
					"Can not parse the proportion of randomness");
		}
		try {
			randomPassLen = Integer.parseInt(parameters[4]);
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the random password length");
		}
		try {
			selfRadius = Integer.parseInt(parameters[5]);
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the confusion parameter");
		}
		try {
			coverage = Double.parseDouble(parameters[6]);
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the minimum coverage");
		}
		try {
			totalTestpoints = Integer.parseInt(parameters[7]);
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the total test points");
		}
		// the timestamp value is date and time in 24 hour format
		timeStamp = new SimpleDateFormat("yyyyMMddkkmmss").format(new Date());

		String currentFileName = handlerString + "_" + timeStamp + ".log";
		try {
			fh = new FileHandler(currentFileName, 30000, 4);
			logger = Logger.getLogger(RunSimulation.class.getCanonicalName()
					+ "" + timeStamp);
			logger.addHandler(fh);
		} catch (SecurityException e) {
			logger.log(Level.SEVERE,
					"Can not create the log file for Security Exception");
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"Can not create the log file for IO exception");
		}
		// save the input line to the first line of the log file
		logger.log(Level.INFO, "" + oneLineInput);

		String encrptionMethod = parameters[1];

		if (!reuseData) {
			logger.log(Level.INFO, "Self points creation started");
			// create self points
			selfPoints = new HashSet<SelfPoint>();

			BitVector[] hashes = null;
			HashGenerator generator;
			try {
				generator = new HashGenerator(encrptionMethod);
				hashes = generator.getRandomHashPasswords(familyNames, pass,
						totalNoSelfPoints, proportionRandomness, randomPassLen);
			} catch (NoSuchAlgorithmException e) {
				logger.log(Level.SEVERE, "error creating generator", e);
			}

			for (int i = 0; i < totalNoSelfPoints; i++) {

				selfPoints.add(new SelfPoint(hashes[i]));
			}
		} else {
			logger.log(Level.INFO, "Using Reused data");
		}
		// save the self points
		if (saveSelfPoints) {
			currentFileName = saveSelfLocation + "_" + timeStamp + ".txt";
			saveSelfPoints(currentFileName);
		}
		startTime = new Date();
		logger.log(Level.INFO, "Detector creation started");
		Vdetector vDetector = new Vdetector(size, selfRadius, coverage);
		if (selfPoints == null || selfPoints.isEmpty()) {
			logger.log(Level.SEVERE,
					"The first line of input should not set Reuse Data parameter");
			System.exit(1);
		}
		vDetector.createDetectors(selfPoints);
		endTime = new Date();
		//convert the time difference in seconds
		diffTime = (endTime.getTime()-startTime.getTime())/1000;
		// Calculate the detector statistics
		List<Detector> detectors = vDetector.getDetectors();

		// save the detector points
		if (saveDetectorPoints) {
			currentFileName = saveDetectorLocation + "_" + timeStamp + ".txt";
			saveDetectorPoints(currentFileName, detectors);
		}
		DescriptiveStatistics stats = new DescriptiveStatistics();

		for (Detector dt : detectors) {
			stats.addValue(dt.getRadius());
		}

		int minVal = (int) stats.getMin();
		int maxVal = (int) stats.getMax();
		double avgVal = stats.getMean();
		double stdVal = stats.getStandardDeviation();

		logger.log(Level.INFO, "Computing started for test points");
		// calculate Detection Rate
		double detectionRate = 0.0;
		mcc = new MonteCarloCalculation(size, detectors, selfPoints, selfRadius);
		 mcc.calculateDetectorCoverage();
		 detectionRate = detectionRate / totalTestpoints;
		detectionRate = mcc.getDetectionRate();

		// Logging the values
		logger.log(Level.INFO, "" + parameters[2] + ", " + parameters[5] + ", "
				+ parameters[6] + ", " + detectors.size() + ", "
				+ detectionRate + ", " + diffTime + ", " + minVal
				+ ", " + maxVal + ", " + avgVal + ", " + stdVal + ", "
				+ detectionRate);

		fh.close();
	}

	/**
	 * Save detector points.
	 *
	 * @param currentFileName the current file name
	 * @param detectors the list of detectors
	 */
	private void saveDetectorPoints(String currentFileName,
			List<Detector> detectors) {
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(
					currentFileName));
			String printString = null;
			for (Detector dt : detectors) {
				printString = "";
				for (int i = 0; i < size; i++) {
					if (dt.getCenter().get(i))
						printString += "1";
					else
						printString += "0";
				}
				bf.write(printString + "," + dt.getRadius() + "\n");
			}
			bf.close();
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "error saving detector points", ex);
		}

	}

	/**
	 * Save self points.
	 *
	 * @param currentFileName the current file name
	 */
	private void saveSelfPoints(String currentFileName) {

		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(
					currentFileName));
			String printString = null;
			for (SelfPoint sf : selfPoints) {
				printString = "";
				for (int i = 0; i < size; i++) {
					if (sf.getCenter().get(i))
						printString += "1";
					else
						printString += "0";
				}
				bf.write(printString + "," + selfRadius + "\n");
			}
			bf.close();
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "error saving self points", ex);
		}
	}

	/**
	 * Experiment suit.
	 * 
	 * @param filename
	 *            the filename
	 */
	public void experimentSuit(String filename) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(filename));
			String line = file.readLine().trim();
			while (line != null) {
				if (!"".equals(line)) {
					simulationEpoch(line);
				}
				line = file.readLine();
			}

		} catch (IOException ex) {
			logger.log(Level.SEVERE, "error realding file", ex);
		}
	}
}
