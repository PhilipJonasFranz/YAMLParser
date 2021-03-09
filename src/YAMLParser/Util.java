package YAMLParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains Utility-Functions like File-I/O operations.
 * 
 * @author jonas.franz
 *
 */
public class Util {

			/* ---< METHODS >--- */
	/** 
	 * Reads the contents of given file and returns a list 
	 * containing each line as a string. 
	 * @param file The file to read.
	 * @return The contents of the file, each line as a string or null if an exception occurred.
	 */
	public static List<String> readFile(File file) {
		try (Stream<String> s = Files.lines(Paths.get(file.getAbsolutePath()))) {
			return s.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

} 
