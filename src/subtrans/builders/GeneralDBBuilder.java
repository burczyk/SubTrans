package subtrans.builders;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import subtrans.exceptions.NotADirectoryException;
import subtrans.helpers.FileFormatRecognizer;
import subtrans.helpers.TheSameFileNameFilter;
import subtrans.interfaces.IReader;
import subtrans.interfaces.ITranslationBase;
import subtrans.models.Translation;
import subtrans.readers.MicroDVDReader;

public class GeneralDBBuilder {
	private File originalSubtitlesDir;
	private File translatedSubtitlesDir;
	
	public GeneralDBBuilder(File originalSubtitlesDir, File translatedSubtitlesDir) {
		this.originalSubtitlesDir = originalSubtitlesDir;
		this.translatedSubtitlesDir = translatedSubtitlesDir;
	}

	public List<Translation> buildDatabase() throws NotADirectoryException{
		List<Translation> translations = new LinkedList<Translation>();
		if(originalSubtitlesDir.isDirectory() && translatedSubtitlesDir.isDirectory()){
			for(File originalFile: originalSubtitlesDir.listFiles()){
				File translationFile = translatedSubtitlesDir.listFiles(new TheSameFileNameFilter(originalFile.getName()))[0];
				if(FileFormatRecognizer.getFileFormat(originalFile).equals(FileFormatRecognizer.getFileFormat(translationFile))){
					ITranslationBase databaseBuilder = null;
					switch(FileFormatRecognizer.getFileFormat(originalFile)){
						case MICRO_DVD:
							IReader sequencesReader = new MicroDVDReader(originalFile);
							IReader translationsReader = new MicroDVDReader(translationFile);
							databaseBuilder = new MicroDVDDBBuilder(sequencesReader.readFile(), translationsReader.readFile());
							break;
						case NOT_RECOGNIZED:
							break;
					}
					
					if(databaseBuilder != null){
						translations.addAll(databaseBuilder.buildDatabase());
					}
					
				} else {
					System.err.println(String.format("Files %s and %s have different subtitles types.", originalFile.getName(), translationFile.getName()));
				}
			}
		} else {
			throw new NotADirectoryException();
		}
		return translations;
	}
}
