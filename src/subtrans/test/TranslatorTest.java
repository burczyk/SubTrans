package subtrans.test;

import java.io.File;
import java.util.List;

import org.junit.Test;

import subtrans.builder.GeneralDBBuilder;
import subtrans.exception.NotADirectoryException;
import subtrans.model.TextSequence;
import subtrans.model.Translation;
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

			// TmpReader reader = new TmpReader(new File("subtitles/new/short.txt"));
			TmpReader reader = new TmpReader(new File("subtitles/new/House.S08E06.HDTV.XviD-LOL.[VTV].avi-ENG.txt"));
			List<TextSequence> sequences = reader.readFile();

			for (TextSequence sequence : sequences) {
				List<Translation> list = translator.translate(sequence.getSequence());
				System.out.println(list);
			}

		} catch (NotADirectoryException e) {
			e.printStackTrace();
		}
	}
}
