package subtrans.tests;

import java.io.File;
import java.util.List;

import org.junit.Test;

import subtrans.builders.GeneralDBBuilder;
import subtrans.exceptions.NotADirectoryException;
import subtrans.models.TextSequence;
import subtrans.readers.TmpReader;
import subtrans.translators.HistoricalTranslator;
import subtrans.translators.Translator;

public class TranslatorTest {
	@Test
	public void testTranslator(){
		try {
			HistoricalTranslator ht = new HistoricalTranslator(new GeneralDBBuilder(new File("subtitles/eng"), new File("subtitles/pl")).buildDatabase());
			Translator translator = new Translator(ht, null);
			
			TmpReader reader = new TmpReader(new File("subtitles/new/House.S08E06.HDTV.XviD-LOL.[VTV].avi-ENG.txt"));
			List<TextSequence> sequences = reader.readFile();
			
			for(TextSequence sequence : sequences){
				translator.translate(sequence.getSequence());
			}
			
		} catch (NotADirectoryException e) {
			e.printStackTrace();
		}
	}
}
