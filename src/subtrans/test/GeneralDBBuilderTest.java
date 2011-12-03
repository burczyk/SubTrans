package subtrans.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import subtrans.builder.GeneralDBBuilder;
import subtrans.exception.NotADirectoryException;
import subtrans.model.Translation;

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
