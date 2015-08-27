package siemens.plm.singleresponsibility.wordcounter.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

public class TemporaryFile {
	private Path name;

	public TemporaryFile(String parentDirectory, String contents) throws IOException {
		name = Files.createTempFile(Paths.get(parentDirectory), null, null, new FileAttribute[]{});

		File file = name.toFile();
		file.deleteOnExit();
		
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(contents);
			writer.close();
		}
	}
}
