package subtrans.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import subtrans.exception.NotADirectoryException;
import subtrans.helper.FileFormatRecognizer;
import subtrans.helper.TheSameFileNameFilter;
import subtrans.interfaces.IReader;
import subtrans.interfaces.ITranslationBase;
import subtrans.model.Translation;
import subtrans.reader.MicroDVDReader;
import subtrans.util.TranslationSet;

public class GeneralDBBuilder {
	private File originalSubtitlesDir;
	private File translatedSubtitlesDir;

	public GeneralDBBuilder(File originalSubtitlesDir, File translatedSubtitlesDir) {
		this.originalSubtitlesDir = originalSubtitlesDir;
		this.translatedSubtitlesDir = translatedSubtitlesDir;
	}

	public List<Translation> buildDatabase() throws NotADirectoryException {
		Set<Translation> translationsSet = new HashSet<Translation>();
		if (originalSubtitlesDir.isDirectory() && translatedSubtitlesDir.isDirectory()) {
			for (File originalFile : originalSubtitlesDir.listFiles()) {
				File translationFile = translatedSubtitlesDir.listFiles(new TheSameFileNameFilter(originalFile.getName()))[0];
				if (FileFormatRecognizer.getFileFormat(originalFile).equals(FileFormatRecognizer.getFileFormat(translationFile))) {
					ITranslationBase databaseBuilder = null;
					switch (FileFormatRecognizer.getFileFormat(originalFile)) {
						case MICRO_DVD:
							IReader sequencesReader = new MicroDVDReader(originalFile);
							IReader translationsReader = new MicroDVDReader(translationFile);
							databaseBuilder = new MicroDVDDBBuilder(sequencesReader.readFile(), translationsReader.readFile());
							break;
						case NOT_RECOGNIZED:
							break;
					}

					if (databaseBuilder != null) {
						Translation translationInSet;
						for (Translation translation : databaseBuilder.buildDatabase()) {
							if ((translationInSet = TranslationSet.containsSequence(translationsSet, translation.getSequence(), translation.getTranslation())) != null) {
								translationInSet.increaseOccurences();
							} else {
								translationsSet.add(translation);
							}
						}

					}

				} else {
					System.err.println(String.format("Files %s and %s have different subtitles types.", originalFile.getName(), translationFile.getName()));
				}
			}
		} else {
			throw new NotADirectoryException();
		}
		return new ArrayList<Translation>(translationsSet);
	}
}
