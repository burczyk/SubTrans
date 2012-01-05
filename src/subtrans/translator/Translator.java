package subtrans.translator;

import java.util.List;

import subtrans.interfaces.ITranslator;
import subtrans.model.Translation;

public class Translator implements ITranslator {
	private HistoricalTranslator historicalTranslator;
	private DictionaryTranslator dictionaryTranslator;

	public Translator(HistoricalTranslator historicalTranslator, DictionaryTranslator dictionaryTranslator) {
		super();
		this.historicalTranslator = historicalTranslator;
		this.dictionaryTranslator = dictionaryTranslator;
	}

	@Override
	public List<Translation> translate(String sequence) {
		List<Translation> translations = historicalTranslator.translate(sequence);
		if (translations != null && translations.size() > 0) {
			System.out.println(translations.get(0));
		} else {
			try {
				Translation t = dictionaryTranslator.translate(sequence.toLowerCase().replaceAll("\\|", " ").replaceAll("\\.", "")).get(0);
				System.out.println(t);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return null;
	}

}
