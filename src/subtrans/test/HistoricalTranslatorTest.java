package subtrans.test;

import java.io.File;

import org.junit.Test;

import subtrans.builder.GeneralDBBuilder;
import subtrans.exception.NotADirectoryException;
import subtrans.translator.HistoricalTranslator;

public class HistoricalTranslatorTest {

	@Test
	public void testHistoricalTranslator() {
		try {
			HistoricalTranslator ht = new HistoricalTranslator(new GeneralDBBuilder(new File("subtitles/eng"), new File("subtitles/pl")).buildDatabase());
			System.out.println(ht.translate("okay"));
		} catch (NotADirectoryException e) {
			e.printStackTrace();
		}
	}
}
