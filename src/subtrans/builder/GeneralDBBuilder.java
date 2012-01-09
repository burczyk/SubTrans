package subtrans.builder;

import java.io.File;

import subtrans.exception.NotADirectoryException;
import subtrans.helper.FileFormatRecognizer;
import subtrans.helper.TheSameFileNameFilter;
import subtrans.interfaces.IReader;
import subtrans.interfaces.ITranslationBase;
import subtrans.model.Translation;
import subtrans.reader.MicroDVDReader;
import subtrans.sqlite.SQLiteQueryExecutor;

public class GeneralDBBuilder {
	private File originalSubtitlesDir;
	private File translatedSubtitlesDir;

	private SQLiteQueryExecutor executor = new SQLiteQueryExecutor("translations/translations.db");

	public GeneralDBBuilder(File originalSubtitlesDir, File translatedSubtitlesDir) {
		this.originalSubtitlesDir = originalSubtitlesDir;
		this.translatedSubtitlesDir = translatedSubtitlesDir;
	}

	public SQLiteQueryExecutor buildDatabase() throws NotADirectoryException {
		if (originalSubtitlesDir.isDirectory() && translatedSubtitlesDir.isDirectory()) {
			for (File originalFile : originalSubtitlesDir.listFiles()) {
				System.out.println("DBBuilder plik: " + originalFile.getName());
				if (!executor.fileExist(originalFile.getName())) {
					executor.addFile(originalFile.getName());
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
							for (Translation translation : databaseBuilder.buildDatabase()) {
								executor.addTranslation(translation.getSequence(), translation.getTranslation(), translation.getOccurences());
							}
						}
					} else {
						System.err.println(String.format("Files %s and %s have different subtitles types.", originalFile.getName(), translationFile.getName()));
					}
				}
			}
		} else {
			throw new NotADirectoryException();
		}
		return executor;
	}
}
