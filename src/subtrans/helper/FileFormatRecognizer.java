package subtrans.helper;

import java.io.File;
import java.util.List;

import subtrans.model.FileFormat;
import subtrans.reader.GeneralFileReader;

public class FileFormatRecognizer {
	private static final String MICRO_DVD_REGEXP = "\\{\\d+\\}\\{\\d+\\}.+";
	private static final String TMP_REGEXP = "\\d\\d:\\d\\d:\\d\\d:";
	
	public static FileFormat getFileFormat(File file){
		GeneralFileReader reader = new GeneralFileReader(file);
		List<String> lines = reader.readFile();
		if(lines.get(0).matches(MICRO_DVD_REGEXP)){
			return FileFormat.MICRO_DVD;
		} else if(lines.get(0).matches(TMP_REGEXP)){
			return FileFormat.TMP;
		} else {
			return FileFormat.NOT_RECOGNIZED;
		}
		
	}
}
