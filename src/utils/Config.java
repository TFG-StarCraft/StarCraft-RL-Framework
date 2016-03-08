package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	public static final String CONFIG_FILE_NAME = "config.properties";

	public static final String SC_PATH_PROP = "StarCraftFolder";
	public static final String SC_DEV_MAPS_PATH_PROP = "DevMapsFolder";
	public static final String SC_CURRENT_MAP = "CurrentMap";
	public static final String BWAPI_CONFIG = "bwapi.ini_path";
	public static final String ACTIONS_DEFAULT = "SelectedActions";
	
	public static Properties prop;
	
	public static void init() {
		try {
			InputStream inputStream = new FileInputStream(CONFIG_FILE_NAME);
			prop = new Properties();
			prop.load(inputStream);
			inputStream.close();
		} catch (FileNotFoundException e) {
			createDefaultProps();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String get(String n) {
		String s = prop.getProperty(n);

		return s;
	}
	
	public static void set(String k, String v) {
		prop.setProperty(k, v);
		
		try {
			prop.store(new FileOutputStream(CONFIG_FILE_NAME), "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void createDefaultProps() {
		prop = new Properties();

		prop.setProperty(SC_PATH_PROP, "");
		prop.setProperty(SC_DEV_MAPS_PATH_PROP, "");
		prop.setProperty(SC_CURRENT_MAP, "");
		prop.setProperty(BWAPI_CONFIG, "");
		prop.setProperty(ACTIONS_DEFAULT, Long.toString(~0));
		
		try {
			prop.store(new FileOutputStream(CONFIG_FILE_NAME), "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
