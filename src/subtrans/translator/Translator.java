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
			// for (Translation translation : translations) {
			// System.out.println(translation.getSequence() + " <---> " +
			// translation.getOccurences() + ": " +
			// translation.getTranslation());
			// }
			System.out.println(translations.get(0).getSequence() + " <---> " + translations.get(0).getOccurences() + ": |" + translations.get(0).getTranslation() + "|");
		}
		return null;
	}

}
