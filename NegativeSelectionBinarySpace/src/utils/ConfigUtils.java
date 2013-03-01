/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigUtils.
 * 
 * @author Javier
 */
public class ConfigUtils {

	/** The Constant logger. */
	public static final Logger logger = Logger.getLogger(ConfigUtils.class
			.getCanonicalName());
	
	/** The prop. */
	public static Properties prop = null;

	/**
	 * Load property.
	 */
	public static void loadProperty() {
		prop = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream("configs/configuration.properties");
			prop.load(fis);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Load file name.
	 *
	 * @param keyAtrribute the attribute name of the property file
	 * @return the filename
	 */
	public static String loadFileName(String keyAtrribute) {
		String fileName = prop.getProperty(keyAtrribute);
		return fileName;
	}

	/**
	 * Load strings.
	 * 
	 * @param fileName name of the file 
	 *            
	 * @return the string[] that has the file contents for all the lines
	 */
	public static String[] loadStrings(String fileName) {
		List<String> strings = new ArrayList<String>();
		try {
			BufferedReader bf = new BufferedReader(new FileReader(fileName));
			String line = bf.readLine();
			while (line != null) {
				strings.add(line.trim());
				line = bf.readLine();
			}

		} catch (IOException ex) {
			Logger.getLogger(ConfigUtils.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return strings.toArray(new String[0]);
	}

	/**
	 * Configures the Logger manager with specified configFile properties file.
	 * 
	 * @param configFile
	 *            path of properties file
	 * @see LogManager
	 */
	public static void configLoggers(String configFile) {
		try {
			FileInputStream fis = new FileInputStream(configFile);
			LogManager.getLogManager().readConfiguration(fis);
			fis.close();
		} catch (IOException e) {
			logger.log(Level.WARNING, "Logging Properties File is invalid");
		}
	}
}
