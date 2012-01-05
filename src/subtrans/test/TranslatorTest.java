package subtrans.test;

import java.io.File;
import java.util.List;

import org.junit.Test;

import subtrans.builder.GeneralDBBuilder;
import subtrans.exception.NotADirectoryException;
import subtrans.model.TextSequence;
import subtrans.reader.TmpReader;
import subtrans.translator.DictionaryTranslator;
import subtrans.translator.HistoricalTranslator;
import subtrans.translator.Translator;

public class TranslatorTest {
	@Test
	public void testTranslator() {
		try {
			HistoricalTranslator ht = new HistoricalTranslator(new GeneralDBBuilder(new File("subtitles/eng"), new File("subtitles/pl")).buildDatabase());
			DictionaryTranslator dt = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
			Translator translator = new Translator(ht, dt);

			TmpReader reader = new TmpReader(new File("subtitles/new/short.txt"));
			List<TextSequence> sequences = reader.readFile();

			for (TextSequence sequence : sequences) {
				translator.translate(sequence.getSequence());
			}

		} catch (NotADirectoryException e) {
			e.printStackTrace();
		}
	}
}
