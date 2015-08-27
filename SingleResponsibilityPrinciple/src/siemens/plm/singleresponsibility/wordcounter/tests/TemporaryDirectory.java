package siemens.plm.singleresponsibility.wordcounter.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemporaryDirectory  {

	private Path directoryPath;

	public TemporaryDirectory() throws IOException {
		directoryPath = Files.createTempDirectory(null);
		File directory = directoryPath.toFile();
		directory.deleteOnExit();
	}
	
	public String getName() {
		return directoryPath.toString();
	}

}
