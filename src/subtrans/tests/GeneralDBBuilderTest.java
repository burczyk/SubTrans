package subtrans.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import subtrans.builders.GeneralDBBuilder;
import subtrans.exceptions.NotADirectoryException;
import subtrans.models.Translation;

public class GeneralDBBuilderTest {
	@Test
	public void testBuilder(){
		GeneralDBBuilder builder = new GeneralDBBuilder(new File("subtitles/eng"), new File("subtitles/pl"));
		try {
			List<Translation> translations = builder.buildDatabase();
			for(Translation translation : translations){
				System.out.println(translation.getSequence() + " <---> " + translation.getTranslation());
			}
			assertTrue(translations.size() > 123);
			System.out.println("Translations size: " + translations.size());
		} catch (NotADirectoryException e) {
			e.printStackTrace();
		}
	}
}
