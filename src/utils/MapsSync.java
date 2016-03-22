package utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MapsSync {

	private static final String PROJECT_MAPS_DIR = "./DevMaps";

	public static void sync() throws IOException {
		String path = Config.get(Config.SC_DEV_MAPS_PATH_PROP);
		if (path != null) {
			System.out.println("Syncronizing maps...");
			File projectDir = new File(PROJECT_MAPS_DIR);
			File[] projectMaps = projectDir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.isFile() && (f.getName().endsWith(".scm") || f.getName().endsWith(".scx"));
				}
			});

			File scMapsDir = new File(path);
			File[] scMaps = scMapsDir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.isFile() && (f.getName().endsWith(".scm") || f.getName().endsWith(".scx"));
				}
			});

			// Filter all maps in scMapsFolder that are not in the projectFolder
			List<File> scNotInProject = Arrays.stream(scMaps)
					.filter(scMap -> Arrays.stream(projectMaps)
							.allMatch(projectMap -> !projectMap.getName().equals(scMap.getName())))
					.collect(Collectors.toList());
			
			// Copy maps in sc that are not in project
			for (File file : scNotInProject) {
				FileOutputStream os = new FileOutputStream(projectDir.getPath() + "/" + file.getName());
				Files.copy(file.toPath(), os);
				os.close();
				System.out.println(file.getName() + " to projectMaps");
			}
			// Copy from Project to SC (overwite always)
			for (File file : projectMaps) {
				File f = new File(path + "/" + file.getName());
				if (f.exists())
					f.delete();				
				FileOutputStream os = new FileOutputStream(path + "/" + file.getName());
				Files.copy(file.toPath(), os);
				os.close();
				System.out.println(file.getName() + " to scMaps");
			}
		}
	}
}
