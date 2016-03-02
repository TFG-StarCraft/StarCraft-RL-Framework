package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class BwapiConfig {

	public static void rewriteBwapi_ini(String key, String name) {
		File f = new File(Config.get(Config.BWAPI_CONFIG));

		ArrayList<String> a = new ArrayList<>();

		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				String string = (String) sc.nextLine();
				if (string.startsWith(key)) {
					String value = name.substring((Config.get(Config.SC_PATH_PROP)).length()).replace("\\", "/");
					// Prevent adding an / before the path (dosnt work)
					if (value.startsWith("/")) {
						value = value.substring(1);
					}
					string = key + value;
				}
				a.add(string);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f));
			
			for (String string : a) {
				pw.println(string);
			}
			
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
