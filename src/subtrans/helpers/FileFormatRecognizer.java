package subtrans.helpers;

import java.io.File;
import java.util.List;

import subtrans.models.FileFormat;
import subtrans.readers.GeneralFileReader;

public class FileFormatRecognizer {
	private static final String MICRO_DVD_REGEXP = "\\{\\d+\\}\\{\\d+\\}.+";
	
	public static FileFormat getFileFormat(File file){
		GeneralFileReader reader = new GeneralFileReader(file);
		List<String> lines = reader.readFile();
		if(lines.get(0).matches(MICRO_DVD_REGEXP)){
			return FileFormat.MICRO_DVD;
		} else {
			return FileFormat.NOT_RECOGNIZED;
		}
		
	}
}
