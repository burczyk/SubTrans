package subtrans.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import subtrans.builder.MicroDVDDBBuilder;
import subtrans.model.TextSequence;
import subtrans.model.Translation;
import subtrans.reader.MicroDVDReader;

public class BuildersTest {
	@Test
	public void testMicroDVDDBBuilder(){
		MicroDVDReader engReader = new MicroDVDReader(new File("subtitles/eng/house_s01e01.txt"));
		MicroDVDReader plReader = new MicroDVDReader(new File("subtitles/pl/house_s01e01.txt"));
		List<TextSequence> sequencesList = engReader.readFile();
		List<TextSequence> translationsList = plReader.readFile();
		MicroDVDDBBuilder dbBuilder = new MicroDVDDBBuilder(sequencesList, translationsList);
		List<Translation> translations = dbBuilder.buildDatabase();
		for(Translation translation : translations){
			System.out.println(translation.getSequence() + " <---> " + translation.getTranslation());
		}
		assertEquals(translations.size(), 634);
	}
}
