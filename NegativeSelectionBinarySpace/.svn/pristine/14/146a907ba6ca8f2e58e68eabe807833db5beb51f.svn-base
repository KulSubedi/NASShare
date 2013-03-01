/*
 * 
 */
package input;

// TODO: Auto-generated Javadoc
/**
 * The Class GenerateInputFile.
 * 
 * @author aknag
 */
public class GenerateInputFile {

	/** The dimension. */
	// currently supported tuning of parameters
	public static String[] dimension = { "128", "256", "512" };

	/** The encryption. */
	public static String[] encryption = { "MD5", "SHA-256", "SHA-512" };

	/** The self points. */
	public static String[] selfPoints = { "1000", "3000", "5000", "8000",
			"10000", "15000", "20000" };

	/** The grid dimension. */
	public static String[] gridDimension = { "32", "64", "128", "256", "512",
			"1024", "2048" };
	/** The test points. */
	public static String[] testPoints = { "10", "15", "20" };

	/** The coverage. */
	public static String[] coverage = { "0.80", "0.85", "0.90", "0.95" };

	/** The randomness. */
	public static String[] randomness = { "40", "50", "60" }; // for time being
																// only first
																// value is
																// considered

	/** The confusion parameter. */
	public static String[] confusionParameter = { "4", "5", "6" }; // for time
																	// being
																	// only
																	// second
																	// value is
																	// considered
																	// in
																	// running
																	// simulations

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		// change of dimension, no. of self-points and grid dimension
		String tobeWrite = "";

		for (int i = 0; i < dimension.length; i++) {
			for (int j = 0; j < selfPoints.length; j++) {
				for (int k = 0; k < gridDimension.length; k++) {
					tobeWrite = dimension[i] + " " + encryption[i] + " "
							+ selfPoints[j] + " " + "40 8 5 0.80" + " "
							+ gridDimension[k] + " " + "0";
					for (int l = 0; l < 10; l++)
						System.err.println(tobeWrite);
				}
			}
		}
	}

}
